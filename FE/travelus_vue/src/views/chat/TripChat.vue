<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import SockJS from 'sockjs-client'
import Stomp from 'stompjs'
import { useUserStore } from '@/store/user'

const props = defineProps({
  chatroomId: Number, // 상위에서 planId 등을 넘겨서 매핑
})

const userStore = useUserStore()
const userId = userStore.loginUser.id

const messages = ref([])
const newMessage = ref('')
let stompClient = null

// WebSocket 연결
const connect = () => {
  const socket = new SockJS('http://localhost:8080/ws') // 백엔드 주소
  stompClient = Stomp.over(socket)

  stompClient.connect({}, () => {
    // 구독
    stompClient.subscribe(`/topic/chat/${props.chatroomId}`, (msg) => {
      messages.value.push(JSON.parse(msg.body))
    })
  })
}

// 연결 해제
const disconnect = () => {
  if (stompClient) stompClient.disconnect()
}

// 메시지 전송
const sendMessage = () => {
  if (newMessage.value.trim()) {
    const message = {
      chatroomId: props.chatroomId,
      usersId: userId,
      content: newMessage.value,
    }
    stompClient.send(`/app/chat.send/${props.chatroomId}`, {}, JSON.stringify(message))
    newMessage.value = ''
  }
}

onMounted(() => connect())
onUnmounted(() => disconnect())
</script>

<template>
  <div class="h-full flex flex-col">
    <div class="flex-1 overflow-y-auto p-2 bg-white rounded shadow">
      <div v-for="(msg, idx) in messages" :key="idx" class="mb-1">
        <strong>익명{{ msg.usersId }}:</strong> {{ msg.content }}
      </div>
    </div>

    <div class="mt-2 flex gap-2">
      <input
        v-model="newMessage"
        @keyup.enter="sendMessage"
        class="border px-2 py-1 rounded flex-1"
        placeholder="메시지를 입력하세요"
      />
      <button @click="sendMessage" class="px-4 bg-blue-600 text-white rounded">전송</button>
    </div>
  </div>
</template>
