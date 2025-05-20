<template>
  <div class="h-full flex flex-col">
    <div v-if="isChatVisible" class="border rounded-lg h-full flex flex-col">
      <div class="p-4 border-b flex items-center justify-between">
        <h3 class="text-lg font-medium flex items-center gap-2">
          <users-icon class="h-5 w-5" />
          여행 참여자 채팅
        </h3>
        <button @click="toggleChat" class="h-8 w-8 flex items-center justify-center">
          <x-icon class="h-4 w-4" />
        </button>
      </div>
      <div class="flex-1 overflow-hidden flex flex-col p-4">
        <div class="flex-1 overflow-y-auto mb-4 space-y-4 pr-2 custom-scrollbar">
          <div 
            v-for="msg in messages" 
            :key="msg.id"
            :class="{ 'text-right': msg.senderId === '1' }"
          >
            <div v-if="msg.senderId !== '1'" class="text-xs text-gray-500 mb-1">{{ msg.senderName }}</div>
            <div :class="[
              'flex items-end gap-2',
              msg.senderId === '1' ? 'justify-end' : ''
            ]">
              <div v-if="msg.senderId !== '1'" class="text-xs text-gray-400 self-end">{{ msg.timestamp }}</div>
              <p :class="[
                'p-3 rounded-lg inline-block max-w-[85%] break-words text-left',
                msg.senderId === '1' ? 'bg-blue-600 text-white' : 'bg-gray-100 text-gray-800'
              ]">
                {{ msg.message }}
              </p>
              <div v-if="msg.senderId === '1'" class="text-xs text-gray-400 self-end">{{ msg.timestamp }}</div>
            </div>
          </div>
        </div>
        <div class="flex items-center gap-2 mt-auto">
          <input
            v-model="newMessage"
            placeholder="메시지 보내기..."
            class="flex-1 px-3 py-2 border rounded-md"
            @keyup.enter="handleSendMessage"
          />
          <button 
            @click="handleSendMessage"
            class="p-2 bg-blue-600 text-white rounded-md hover:bg-blue-700"
          >
            <arrow-down-icon class="h-4 w-4" />
          </button>
        </div>
      </div>
    </div>
    <button 
      v-else
      @click="toggleChat"
      class="w-full h-12 flex items-center justify-center gap-2 bg-blue-600 text-white rounded-md hover:bg-blue-700"
    >
      <message-square-icon class="h-5 w-5" />
      채팅 열기
    </button>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { 
  ArrowDown as ArrowDownIcon, 
  Users as UsersIcon, 
  MessageSquare as MessageSquareIcon,
  X as XIcon
} from 'lucide-vue-next'

const isChatVisible = ref(true)
const newMessage = ref('')
const messages = ref([
  { id: 1, senderId: "1", senderName: "여행좋아", message: "다들 계획 확인하셨나요?", timestamp: "오전 10:23" },
  {
    id: 2,
    senderId: "2",
    senderName: "산타클로스",
    message: "네! 첫날 일정이 좀 빡빡해 보이긴 하네요",
    timestamp: "오전 10:25",
  },
  {
    id: 3,
    senderId: "3",
    senderName: "맛집탐방러",
    message: "저는 첫날 점심 식당을 다른 곳으로 바꿨으면 해요. 근처에 더 유명한 곳이 있더라구요.",
    timestamp: "오전 10:30",
  },
  {
    id: 4,
    senderId: "1",
    senderName: "여행좋아",
    message: "맛집탐방러님 추천해주시는 곳이 있으신가요?",
    timestamp: "오전 10:32",
  },
])

// 메시지 전송 처리
const handleSendMessage = () => {
  if (!newMessage.value.trim()) return

  const newMsg = {
    id: messages.value.length + 1,
    senderId: "1", // Current user ID would come from auth context
    senderName: "여행좋아", // Current user name would come from auth context
    message: newMessage.value,
    timestamp: new Date().toLocaleTimeString("ko-KR", { hour: "2-digit", minute: "2-digit" }),
  }

  messages.value.push(newMsg)
  newMessage.value = ""
}

const toggleChat = () => {
  isChatVisible.value = !isChatVisible.value
}
</script>

<style scoped>
.custom-scrollbar::-webkit-scrollbar {
  width: 6px;
}

.custom-scrollbar::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 10px;
}

.custom-scrollbar::-webkit-scrollbar-thumb {
  background: #c5c5c5;
  border-radius: 10px;
}

.custom-scrollbar::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}
</style>
