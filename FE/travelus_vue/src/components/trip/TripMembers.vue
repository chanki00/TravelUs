<template>
  <div class="relative">
    <button
      @click="toggleDropdown"
      class="flex items-center gap-2 px-3 py-2 border rounded-md hover:bg-gray-50"
    >
      <div class="flex -space-x-2">
        <img
          v-for="(member, index) in displayMembers"
          :key="member.id"
          :src="member.image || `https://i.pravatar.cc/150?img=${member.id}`"
          :alt="member.name"
          class="w-8 h-8 rounded-full border-2 border-white"
          :style="{ zIndex: displayMembers.length - index }"
        />
        <div
          v-if="totalMembers > 3"
          class="w-8 h-8 rounded-full bg-gray-300 border-2 border-white flex items-center justify-center text-xs font-medium text-gray-600"
        >
          +{{ totalMembers - 3 }}
        </div>
      </div>
      <span class="text-sm font-medium">{{ totalMembers }}명</span>
      <chevron-down-icon class="h-4 w-4" />
    </button>

    <!-- 드롭다운 -->
    <div
      v-if="isDropdownOpen"
      class="absolute right-0 top-full mt-2 w-80 bg-white border rounded-lg shadow-lg z-50"
    >
      <div class="p-4">
        <div class="flex justify-between items-center mb-3">
          <h3 class="font-semibold">참여자 ({{ totalMembers }}명)</h3>
          <button
            @click="openInviteModal"
            class="px-3 py-1 text-sm bg-blue-600 text-white rounded hover:bg-blue-700"
          >
            초대하기
          </button>
        </div>

        <!-- 참여자 목록 -->
        <div class="space-y-2 max-h-60 overflow-y-auto">
          <div
            v-for="member in allMembers"
            :key="member.id"
            class="flex items-center gap-3 p-2 rounded hover:bg-gray-50"
          >
            <div class="relative">
              <img
                :src="member.image || `https://i.pravatar.cc/150?img=${member.id}`"
                :alt="member.name"
                class="w-10 h-10 rounded-full"
              />
              <!-- 온라인 상태 표시 -->
              <div
                v-if="member.isOnline"
                class="absolute -bottom-1 -right-1 w-4 h-4 bg-green-500 border-2 border-white rounded-full"
                title="접속 중"
              ></div>
              <div
                v-else
                class="absolute -bottom-1 -right-1 w-4 h-4 bg-gray-400 border-2 border-white rounded-full"
                title="오프라인"
              ></div>
            </div>

            <div class="flex-1">
              <div class="flex items-center gap-2">
                <span class="font-medium">{{ member.name }}</span>
                <span
                  v-if="member.role === 'host'"
                  class="px-2 py-0.5 text-xs bg-blue-100 text-blue-700 rounded"
                >
                  호스트
                </span>
                <span
                  v-if="member.isOnline"
                  class="px-2 py-0.5 text-xs bg-green-100 text-green-700 rounded"
                >
                  접속 중
                </span>
              </div>
              <p class="text-sm text-gray-500">
                {{ member.isOnline ? '활성' : formatLastSeen(member.lastSeen) }}
              </p>
            </div>

            <!-- 편집 중 표시 -->
            <div v-if="member.isEditing" class="flex items-center gap-1 text-xs text-blue-600">
              <div class="w-2 h-2 bg-blue-600 rounded-full animate-pulse"></div>
              편집 중
            </div>
          </div>
        </div>

        <!-- 초대 대기 중인 사용자 -->
        <div v-if="pendingInvites.length > 0" class="mt-4 pt-3 border-t">
          <h4 class="text-sm font-medium text-gray-700 mb-2">초대 대기 중</h4>
          <div class="space-y-2">
            <div
              v-for="invite in pendingInvites"
              :key="invite.id"
              class="flex items-center gap-3 p-2 rounded bg-yellow-50"
            >
              <img
                :src="invite.user.image || `https://i.pravatar.cc/150?img=${invite.user.id}`"
                :alt="invite.user.name"
                class="w-8 h-8 rounded-full"
              />
              <div class="flex-1">
                <span class="text-sm font-medium">{{ invite.user.name }}</span>
                <p class="text-xs text-gray-500">초대 대기 중</p>
              </div>
              <span class="px-2 py-1 text-xs bg-yellow-200 text-yellow-800 rounded"> 대기 </span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 배경 클릭 시 드롭다운 닫기 -->
    <div v-if="isDropdownOpen" class="fixed inset-0 z-40" @click="closeDropdown"></div>

    <!-- 초대 모달 -->
    <InvitePlanModel
      v-if="isInviteModalOpen"
      :chatroom-id="chatroomId"
      :plan-id="planId"
      @close="closeInviteModal"
      @invite-sent="handleInviteSent"
    />
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted, watch } from 'vue'
import { ChevronDown as ChevronDownIcon } from 'lucide-vue-next'
import InvitePlanModel from './InvitePlanModel.vue'
import api from '@/api'
import SockJS from 'sockjs-client'
import Stomp from 'stompjs'

const props = defineProps({
  chatroomId: {
    type: Number,
    required: true,
  },
  planId: {
    type: Number,
    required: true,
  },
  currentUserId: {
    type: Number,
    required: true,
  },
})

const emit = defineEmits(['members-updated'])

const isDropdownOpen = ref(false)
const isInviteModalOpen = ref(false)
const allMembers = ref([])
const pendingInvites = ref([])
const onlineUsers = ref(new Set())
const editingUsers = ref(new Set())

// 표시할 멤버 (최대 3명)
const displayMembers = computed(() => allMembers.value.slice(0, 3))
const totalMembers = computed(() => allMembers.value.length)

let stompClient = null

// STOMP 연결
const connectStomp = () => {
  const socket = new SockJS(import.meta.env.VITE_BACKEND_URL + '/ws')
  // const socket = new SockJS('http://localhost:8080/ws')
  stompClient = Stomp.over(socket)

  stompClient.connect({}, () => {
    console.log('STOMP 연결됨')

    stompClient.subscribe(`/topic/collaboration/${props.chatroomId}`, (message) => {
      const data = JSON.parse(message.body)
      handleWebSocketMessage(data)
    })

    sendPresenceUpdate('ONLINE')
  })
}

// STOMP 메시지 전송 함수
const sendPresenceUpdate = (status) => {
  if (stompClient?.connected) {
    stompClient.send('/app/presence', {}, JSON.stringify({
      type: 'PRESENCE_UPDATE',
      userId: props.currentUserId,
      chatroomId: props.chatroomId,
      status,
    }))
  }
}

const sendEditingStatus = (isEditing) => {
  if (stompClient?.connected) {
    stompClient.send('/app/editing', {}, JSON.stringify({
      type: isEditing ? 'START_EDITING' : 'STOP_EDITING',
      userId: props.currentUserId,
      chatroomId: props.chatroomId,
    }))
  }
}

// WebSocket 메시지 처리
const handleWebSocketMessage = (data) => {
  switch (data.type) {
    case 'USER_ONLINE':
      onlineUsers.value.add(data.userId)
      updateMemberOnlineStatus()
      break
    case 'USER_OFFLINE':
      onlineUsers.value.delete(data.userId)
      editingUsers.value.delete(data.userId)
      updateMemberOnlineStatus()
      break
    case 'USER_EDITING':
      editingUsers.value.add(data.userId)
      updateMemberEditingStatus()
      break
    case 'USER_STOP_EDITING':
      editingUsers.value.delete(data.userId)
      updateMemberEditingStatus()
      break
    case 'NEW_MEMBER_JOINED':
      fetchMembers()
      break
  }
}

// 멤버 상태 업데이트
const updateMemberOnlineStatus = () => {
  allMembers.value.forEach((member) => {
    member.isOnline = onlineUsers.value.has(member.id)
  })
}

const updateMemberEditingStatus = () => {
  allMembers.value.forEach((member) => {
    member.isEditing = editingUsers.value.has(member.id)
  })
}

// 참여자 목록 가져오기
const fetchMembers = async () => {
  try {
    const response = await api.get(`/api/v1/chat/user/${props.chatroomId}`)
    const userIds = response.data

    const memberPromises = userIds.map(async (userId) => {
      try {
        const userResponse = await api.get(`/api/v1/user/${userId}`)
        const user = userResponse.data

        return {
          id: user.id,
          name: user.name,
          image: user.image,
          role: userId === allMembers.value.find((m) => m.role === 'host')?.id ? 'host' : 'member',
          isOnline: onlineUsers.value.has(userId),
          isEditing: editingUsers.value.has(userId),
          lastSeen: new Date(),
        }
      } catch (error) {
        console.error(`사용자 ${userId} 정보 가져오기 실패:`, error)
        return null
      }
    })

    const members = (await Promise.all(memberPromises)).filter(Boolean)

    members.sort((a, b) => {
      if (a.role === 'host') return -1
      if (b.role === 'host') return 1
      return 0
    })

    allMembers.value = members
    emit('members-updated', members)
  } catch (error) {
    console.error('참여자 목록 가져오기 실패:', error)
  }
}

// 초대 대기 목록 가져오기
const fetchPendingInvites = async () => {
  try {
    // 구현 필요
  } catch (error) {
    console.error('대기 중인 초대 가져오기 실패:', error)
  }
}

// 시간 포맷
const formatLastSeen = (date) => {
  if (!date) return '알 수 없음'
  const now = new Date()
  const diff = now - new Date(date)
  const minutes = Math.floor(diff / 60000)
  const hours = Math.floor(diff / 3600000)
  const days = Math.floor(diff / 86400000)
  if (minutes < 1) return '방금 전'
  if (minutes < 60) return `${minutes}분 전`
  if (hours < 24) return `${hours}시간 전`
  return `${days}일 전`
}

// 드롭다운 관련
const toggleDropdown = () => { isDropdownOpen.value = !isDropdownOpen.value }
const closeDropdown = () => { isDropdownOpen.value = false }
const openInviteModal = () => { isInviteModalOpen.value = true; closeDropdown() }
const closeInviteModal = () => { isInviteModalOpen.value = false }
const handleInviteSent = () => { fetchPendingInvites(); closeInviteModal() }

// 마운트 및 언마운트
onMounted(() => {
  fetchMembers()
  fetchPendingInvites()
  connectStomp()
})

onUnmounted(() => {
  sendPresenceUpdate('OFFLINE')
  stompClient?.disconnect()
})

// chatroomId 변경 시 재연결
watch(() => props.chatroomId, (newId, oldId) => {
  if (newId !== oldId) {
    stompClient?.disconnect()
    if (newId) {
      connectStomp()
      fetchMembers()
      fetchPendingInvites()
    }
  }
})

// 외부에서 편집 상태 설정 가능
defineExpose({
  setEditingStatus: sendEditingStatus,
})
</script>

<style scoped></style>