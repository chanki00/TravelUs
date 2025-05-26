<template>
  <div class="h-full flex flex-col bg-white rounded-lg border p-4 shadow-md">
    <!-- 채팅 헤더 -->
    <div class="font-bold text-lg mb-4 flex items-center gap-2">
      <i class="i-lucide-users text-blue-500" /> 여행 참여자 채팅
    </div>

    <!-- 메시지 목록 -->
    <div ref="chatContainer" class="flex-1 overflow-y-auto space-y-2 pr-2 message-container">
      <div
        v-for="(msg, idx) in messages"
        :key="idx"
        :class="['flex', msg.userId === userId ? 'justify-end' : 'justify-start']"
      >
        <div
          :class="[
            'max-w-[70%] px-3 py-2 rounded-lg break-words',
            msg.userId === userId ? 'bg-blue-500 text-white' : 'bg-gray-100 text-gray-800',
          ]"
        >
          <!-- 닉네임 -->
          <div v-if="msg.userId !== userId" class="text-sm font-semibold mb-1">
            익명{{ msg.userId }}
          </div>

          <!-- 내용 -->
          <div class="text-sm whitespace-pre-wrap">{{ msg.content }}</div>

          <!-- 시간 -->
          <div
            class="text-xs mt-1 text-right"
            :class="msg.userId === userId ? 'text-blue-200' : 'text-gray-400'"
          >
            {{ formatTime(msg.createdAt) }}
          </div>
        </div>
      </div>
    </div>

    <!-- 입력창 -->
    <div class="mt-3 flex gap-2 flex-shrink-0">
      <input
        v-model="newMessage"
        @keyup.enter="sendMessage"
        class="flex-1 px-3 py-2 border rounded-full focus:outline-none focus:ring-2 focus:ring-blue-500"
        placeholder="메시지 보내기..."
      />
      <button
        @click="sendMessage"
        class="bg-blue-500 text-white px-4 rounded-full hover:bg-blue-600"
      >
        ⬆
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref, watch, nextTick, onUnmounted } from 'vue'
import SockJS from 'sockjs-client'
import Stomp from 'stompjs'
import { useUserStore } from '@/store/user'
import { userAi } from '@/axios'

const props = defineProps({ chatroomId: Number })
const userStore = useUserStore()
const userId = userStore.loginUser.id

const messages = ref([])
const newMessage = ref('')
const chatContainer = ref(null)
let stompClient = null

// 1. 메시지 불러오기
const fetchChatHistory = async () => {
  console.log('유저아이디', userId)
  console.log('채팅방아이디', props.chatroomId)
  if (!props.chatroomId) return
  try {
    const res = await userAi.get(`/api/v1/chat/${props.chatroomId}`)
    messages.value = res.data
    scrollToBottom()
  } catch (e) {
    console.error('채팅 내역 실패', e)
  }
}

// 2. WebSocket 연결
const connect = () => {
  // const socket = new SockJS('http://INTERNAL_IP_REDACTED:8080/ws')
  const socket = new SockJS('http://localhost:8080/ws')
  stompClient = Stomp.over(socket)

  stompClient.connect({}, () => {
    stompClient.subscribe(`/topic/chat/${props.chatroomId}`, (msg) => {
      const received = JSON.parse(msg.body)
      if (!received.createdAt) {
        received.createdAt = new Date().toISOString()
      }
      messages.value.push(received)
      scrollToBottom()
    })
  })
}

// 3. 전송
const sendMessage = () => {
  if (newMessage.value.trim() && stompClient?.connected) {
    const message = {
      chatroomId: props.chatroomId,
      userId: userId,
      content: newMessage.value,
    }
    stompClient.send(`/app/chat.send/${props.chatroomId}`, {}, JSON.stringify(message))
    newMessage.value = ''
  }
}

// 4. 스크롤 아래로
const scrollToBottom = () => {
  nextTick(() => {
    const el = chatContainer.value
    if (el) el.scrollTop = el.scrollHeight
  })
}

// 5. chatroomId가 변경되거나 mount될 때 처리
watch(
  () => props.chatroomId,
  async (newId) => {
    if (newId) {
      await fetchChatHistory()
      connect()
    }
  },
  { immediate: true },
)

onUnmounted(() => {
  if (stompClient) stompClient.disconnect()
})

// 시간 포맷
const formatTime = (timestamp) => {
  if (!timestamp) return ''
  const date = new Date(timestamp)
  if (isNaN(date)) return ''
  const hours = String(date.getHours()).padStart(2, '0')
  const minutes = String(date.getMinutes()).padStart(2, '0')
  return `${hours}:${minutes}`
}
</script>

<style scoped>
/* 필요시 스타일 커스터마이징 가능 */
</style>
