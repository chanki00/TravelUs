<template>
  <div class="collaborative-editor">
    <!-- 현재 편집자 표시 -->
    <div
      v-if="currentEditors.length > 1"
      class="bg-blue-50 border border-blue-200 rounded-lg p-3 mb-4"
    >
      <div class="flex items-center gap-2">
        <users-icon class="h-5 w-5 text-blue-600" />
        <span class="text-sm font-medium text-blue-800">현재 편집 중:</span>
        <div class="flex gap-2">
          <span
            v-for="editor in currentEditors.filter((e) => e.userId !== userId)"
            :key="editor.userId"
            class="inline-flex items-center px-2 py-1 rounded-full text-xs bg-blue-100 text-blue-800"
          >
            {{ editor.userName }}
            <span class="ml-1 w-2 h-2 bg-green-400 rounded-full"></span>
          </span>
        </div>
      </div>
    </div>

    <!-- 충돌 알림 -->
    <div v-if="conflictData" class="bg-yellow-50 border border-yellow-200 rounded-lg p-4 mb-4">
      <div class="flex items-start gap-3">
        <alert-triangle-icon class="h-5 w-5 text-yellow-600 mt-0.5" />
        <div class="flex-1">
          <h4 class="text-sm font-medium text-yellow-800 mb-2">편집 충돌이 감지되었습니다</h4>
          <p class="text-sm text-yellow-700 mb-3">
            {{ conflictData.lastModifiedBy }}님이 {{ formatTime(conflictData.lastModifiedAt) }}에
            수정했습니다.
          </p>
          <div class="flex gap-2">
            <button
              @click="resolveConflict('keep_mine')"
              class="px-3 py-1 bg-yellow-600 text-white text-sm rounded hover:bg-yellow-700"
            >
              내 변경사항 유지
            </button>
            <button
              @click="resolveConflict('accept_theirs')"
              class="px-3 py-1 bg-gray-600 text-white text-sm rounded hover:bg-gray-700"
            >
              상대방 변경사항 수락
            </button>
            <button
              @click="showConflictDetails = !showConflictDetails"
              class="px-3 py-1 border border-yellow-600 text-yellow-600 text-sm rounded hover:bg-yellow-50"
            >
              상세 비교
            </button>
          </div>

          <!-- 충돌 상세 정보 -->
          <div v-if="showConflictDetails" class="mt-3 p-3 bg-white rounded border">
            <div class="grid grid-cols-2 gap-4">
              <div>
                <h5 class="font-medium text-gray-700 mb-2">내 변경사항:</h5>
                <pre class="text-sm bg-gray-50 p-2 rounded">{{
                  JSON.stringify(conflictData.incomingValue, null, 2)
                }}</pre>
              </div>
              <div>
                <h5 class="font-medium text-gray-700 mb-2">현재 저장된 내용:</h5>
                <pre class="text-sm bg-gray-50 p-2 rounded">{{
                  JSON.stringify(conflictData.currentValue, null, 2)
                }}</pre>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 실시간 변경 알림 -->
    <div v-if="recentChanges.length > 0" class="mb-4">
      <div
        v-for="change in recentChanges"
        :key="change.timestamp"
        class="bg-green-50 border border-green-200 rounded p-2 mb-2 text-sm"
      >
        <span class="font-medium text-green-800">{{ change.userName }}</span>
        <span class="text-green-700">님이 {{ getChangeDescription(change.type) }}</span>
        <span class="text-green-600 text-xs ml-2">{{ formatTime(change.timestamp) }}</span>
      </div>
    </div>

    <slot></slot>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, watch } from 'vue'
import { Users as UsersIcon, AlertTriangle as AlertTriangleIcon } from 'lucide-vue-next'
import SockJS from 'sockjs-client'
import Stomp from 'stompjs'
import api from '@/api'

const props = defineProps({
  planId: Number,
  userId: Number,
  userName: String,
})

const emit = defineEmits(['conflict-detected', 'change-received'])

const currentEditors = ref([])
const conflictData = ref(null)
const showConflictDetails = ref(false)
const recentChanges = ref([])
const sessionId = ref(null)
let stompClient = null

// WebSocket 연결 및 편집 세션 시작
const initializeCollaboration = () => {
  sessionId.value = generateSessionId()

  const socket = new SockJS(import.meta.env.VITE_BACKEND_URL + '/ws')
  // const socket = new SockJS('http://localhost:8080/ws')
  stompClient = Stomp.over(socket)

  stompClient.connect({}, () => {
    // 계획 편집 채널 구독
    stompClient.subscribe(`/topic/plan/${props.planId}`, (message) => {
      const event = JSON.parse(message.body)
      handleCollaborationEvent(event)
    })

    // 편집 세션 시작 알림
    stompClient.send(
      `/app/plan.join/${props.planId}`,
      {},
      JSON.stringify({
        userId: props.userId,
        sessionId: sessionId.value,
      }),
    )
  })

  // 현재 편집자 목록 조회
  fetchCurrentEditors()
}

// 협업 이벤트 처리
const handleCollaborationEvent = (event) => {
  switch (event.type) {
    case 'USER_JOIN':
      if (event.userId !== props.userId) {
        currentEditors.value.push(event.data)
        addRecentChange(event.userName, 'joined')
      }
      break

    case 'USER_LEAVE':
      currentEditors.value = currentEditors.value.filter((e) => e.userId !== event.userId)
      break

    case 'TITLE_CHANGE':
    case 'DESCRIPTION_CHANGE':
    case 'ITINERARY_CHANGE':
      if (event.userId !== props.userId) {
        emit('change-received', event)
        addRecentChange(event.userName, event.type)
      }
      break

    case 'CONFLICT_DETECTED':
      conflictData.value = event.data
      emit('conflict-detected', event.data)
      break
  }
}

// 변경사항 브로드캐스트
const broadcastChange = (changeType, data) => {
  if (stompClient && stompClient.connected) {
    const event = {
      type: changeType,
      planId: props.planId,
      userId: props.userId,
      userName: props.userName,
      data: data,
      timestamp: Date.now(),
      version: data.version || 1,
    }

    stompClient.send(`/app/plan.change/${props.planId}`, {}, JSON.stringify(event))
  }
}

// 현재 편집자 목록 조회
const fetchCurrentEditors = async () => {
  try {
    const response = await api.get(`/api/v1/plan/${props.planId}/editors`)
    currentEditors.value = response.data
  } catch (error) {
    console.error('Failed to fetch current editors:', error)
  }
}

// 충돌 해결
const resolveConflict = (resolution) => {
  if (resolution === 'keep_mine') {
    // 내 변경사항을 강제로 적용
    emit('force-update', conflictData.value.incomingValue)
  } else if (resolution === 'accept_theirs') {
    // 상대방 변경사항을 수락
    emit('accept-remote-changes', conflictData.value.currentValue)
  }

  conflictData.value = null
  showConflictDetails.value = false
}

// 최근 변경사항 추가
const addRecentChange = (userName, changeType) => {
  recentChanges.value.unshift({
    userName,
    type: changeType,
    timestamp: Date.now(),
  })

  // 5개까지만 유지
  if (recentChanges.value.length > 5) {
    recentChanges.value = recentChanges.value.slice(0, 5)
  }

  // 3초 후 자동 제거
  setTimeout(() => {
    recentChanges.value = recentChanges.value.filter((c) => Date.now() - c.timestamp < 3000)
  }, 3000)
}

// 변경 유형 설명
const getChangeDescription = (changeType) => {
  const descriptions = {
    joined: '편집에 참여했습니다',
    TITLE_CHANGE: '제목을 수정했습니다',
    DESCRIPTION_CHANGE: '설명을 수정했습니다',
    ITINERARY_CHANGE: '일정을 수정했습니다',
  }
  return descriptions[changeType] || '변경사항을 적용했습니다'
}

// 시간 포맷팅
const formatTime = (timestamp) => {
  const date = new Date(timestamp)
  return date.toLocaleTimeString('ko-KR', {
    hour: '2-digit',
    minute: '2-digit',
  })
}

// 세션 ID 생성
const generateSessionId = () => {
  return Math.random().toString(36).substring(2, 15) + Math.random().toString(36).substring(2, 15)
}

// 컴포넌트 마운트/언마운트 처리
onMounted(() => {
  initializeCollaboration()
})

onUnmounted(() => {
  if (stompClient && stompClient.connected) {
    // 편집 세션 종료 알림
    stompClient.send(
      `/app/plan.leave/${props.planId}`,
      {},
      JSON.stringify({
        userId: props.userId,
        sessionId: sessionId.value,
      }),
    )

    stompClient.disconnect()
  }
})

// 외부에서 사용할 수 있도록 expose
defineExpose({
  broadcastChange,
  currentEditors,
  conflictData,
})
</script>

<style scoped>
.collaborative-editor {
  position: relative;
}
</style>
