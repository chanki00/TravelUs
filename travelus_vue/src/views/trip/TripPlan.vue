<template>
  <div class="min-h-screen bg-gradient-to-b from-blue-50 to-white">

    <div class="py-8 px-6 max-w-7xl mx-auto">
      <div class="flex flex-col md:flex-row justify-between items-start mb-6">
        <div>
          <h1 class="text-3xl font-bold mb-2">{{ destination }} {{ duration }} 여행 계획</h1>
          <p class="text-gray-500">여행 계획을 확인하고 참여자들과 의견을 나눠보세요</p>
        </div>

        <div class="flex gap-3 mt-4 md:mt-0">
          <TripMembers :members="mockMembers" />
          <button class="px-4 py-2 border rounded-md hover:bg-gray-50">일정 저장</button>
          <button class="px-4 py-2 border rounded-md hover:bg-gray-50">공유하기</button>
          <button class="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700">일정 수정</button>
        </div>
      </div>

      <div class="grid grid-cols-1 lg:grid-cols-12 gap-6">
        <!-- 장소 검색 패널 -->
        <div class="lg:col-span-3 order-2 lg:order-1">
          <div class="mb-4">
            <div class="flex border-b">
              <button 
                v-for="tab in ['restaurants', 'accommodations', 'cafes']" 
                :key="tab"
                :class="[
                  'flex-1 py-2 px-4 text-center',
                  activeTab === tab ? 'border-b-2 border-blue-600 text-blue-600' : 'text-gray-500'
                ]"
                @click="activeTab = tab"
              >
                {{ tabLabels[tab] }}
              </button>
            </div>

            <div class="mt-4">
              <div class="flex items-center gap-2 mb-4">
                <input 
                  type="text" 
                  placeholder="장소 검색하기" 
                  class="flex-1 px-3 py-2 border rounded-md"
                />
                <button class="px-4 py-2 border rounded-md hover:bg-gray-50">검색</button>
              </div>

              <div v-if="activeTab === 'restaurants'" class="space-y-3 max-h-[500px] overflow-y-auto pr-2">
                <div 
                  v-for="place in mockPlaces.restaurants" 
                  :key="place.id"
                  class="border rounded-md p-3 flex items-center gap-3 cursor-pointer hover:border-blue-200"
                >
                  <img :src="place.image" :alt="place.name" class="w-16 h-16 object-cover rounded-md" />
                  <div>
                    <h4 class="font-medium">{{ place.name }}</h4>
                    <div class="flex items-center gap-1 text-sm text-gray-500">
                      <span>{{ place.category }}</span>
                      <span>•</span>
                      <span>★ {{ place.rating }}</span>
                    </div>
                  </div>
                </div>
              </div>

              <div v-if="activeTab === 'accommodations'" class="space-y-3 max-h-[500px] overflow-y-auto pr-2">
                <div 
                  v-for="place in mockPlaces.accommodations" 
                  :key="place.id"
                  class="border rounded-md p-3 flex items-center gap-3 cursor-pointer hover:border-blue-200"
                >
                  <img :src="place.image" :alt="place.name" class="w-16 h-16 object-cover rounded-md" />
                  <div>
                    <h4 class="font-medium">{{ place.name }}</h4>
                    <div class="flex items-center gap-1 text-sm text-gray-500">
                      <span>{{ place.category }}</span>
                      <span>•</span>
                      <span>★ {{ place.rating }}</span>
                    </div>
                  </div>
                </div>
              </div>

              <div v-if="activeTab === 'cafes'" class="space-y-3 max-h-[500px] overflow-y-auto pr-2">
                <div 
                  v-for="place in mockPlaces.cafes" 
                  :key="place.id"
                  class="border rounded-md p-3 flex items-center gap-3 cursor-pointer hover:border-blue-200"
                >
                  <img :src="place.image" :alt="place.name" class="w-16 h-16 object-cover rounded-md" />
                  <div>
                    <h4 class="font-medium">{{ place.name }}</h4>
                    <div class="flex items-center gap-1 text-sm text-gray-500">
                      <span>{{ place.category }}</span>
                      <span>•</span>
                      <span>★ {{ place.rating }}</span>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <!-- 일정 목록 -->
          <div class="mt-6">
            <h2 class="text-xl font-medium mb-4">일정표</h2>

            <div>
              <div class="flex border-b mb-4">
                <button 
                  v-for="(day, index) in itinerary" 
                  :key="index"
                  :class="[
                    'flex-1 py-2 px-4 text-center',
                    activeDay === index ? 'border-b-2 border-blue-600 text-blue-600' : 'text-gray-500'
                  ]"
                  @click="activeDay = index"
                >
                  Day {{ index + 1 }}
                </button>
              </div>

              <div v-for="(day, dayIndex) in itinerary" :key="dayIndex" v-show="activeDay === dayIndex">
                <div class="border rounded-md overflow-hidden">
                  <div class="p-4 border-b bg-gray-50">
                    <h3 class="text-lg font-medium">{{ dayIndex + 1 }}일차</h3>
                  </div>
                  <div class="p-4">
                    <div class="space-y-4">
                      <div 
                        v-for="(item, itemIndex) in day.items" 
                        :key="itemIndex"
                        class="flex items-start gap-3 pb-3 border-b last:border-b-0 last:pb-0"
                      >
                        <div class="text-sm font-medium text-gray-500 w-12">{{ item.time }}</div>
                        <div class="flex-1">
                          <div class="font-medium">{{ item.title }}</div>
                          <div class="text-xs text-blue-600 bg-blue-50 px-2 py-0.5 rounded-full inline-block mt-1">
                            {{ item.type }}
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- 지도 -->
        <div class="lg:col-span-6 order-1 lg:order-2">
          <TripPlannerMap height="800px" />
        </div>

        <!-- 채팅 영역 -->
        <div class="lg:col-span-3 order-3 lg:order-3 relative">
          <div v-if="isChatVisible" class="border rounded-lg h-[800px] flex flex-col">
            <div class="p-4 border-b flex items-center justify-between">
              <h3 class="text-lg font-medium flex items-center gap-2">
                <users-icon class="h-5 w-5" />
                여행 참여자 채팅
              </h3>
              <button @click="isChatVisible = false" class="h-8 w-8 flex items-center justify-center">
                <x-icon class="h-4 w-4" />
              </button>
            </div>
            <div class="flex-1 overflow-hidden flex flex-col p-4">
              <div class="flex-1 overflow-y-auto mb-4 space-y-4 pr-2">
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
            @click="isChatVisible = true"
            class="w-full h-12 flex items-center justify-center gap-2 bg-blue-600 text-white rounded-md hover:bg-blue-700"
          >
            <message-square-icon class="h-5 w-5" />
            채팅 열기
          </button>
        </div>
      </div>
    </div>
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
import TripPlannerMap from '@/components/trip/TripPlannerMap.vue'
import TripMembers from '@/components/trip/TripMembers.vue'

// 상태 관리
const activeTab = ref('restaurants')
const activeDay = ref(0)
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

// 탭 레이블
const tabLabels = {
  restaurants: '식당',
  accommodations: '숙소',
  cafes: '카페'
}

// 목업 데이터
const destination = '제주도'
const duration = '3박 4일'

const mockMembers = [
  {
    id: "1",
    name: "여행좋아",
    role: "호스트",
    status: "online",
    image: "https://i.pravatar.cc/150?img=1",
  },
  {
    id: "2",
    name: "산타클로스",
    role: "참여자",
    status: "online",
    image: "https://i.pravatar.cc/150?img=2",
  },
  {
    id: "3",
    name: "맛집탐방러",
    role: "참여자",
    status: "offline",
    image: "https://i.pravatar.cc/150?img=3",
  },
]

const mockPlaces = {
  restaurants: [
    {
      id: "rest1",
      name: "맛있는 식당 1",
      category: "한식",
      rating: 4.5,
      image:
        "https://images.unsplash.com/photo-1414235077428-338989a2e8c0?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=200&q=80",
    },
    {
      id: "rest2",
      name: "좋은 레스토랑",
      category: "양식",
      rating: 4.2,
      image:
        "https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=200&q=80",
    },
    {
      id: "rest3",
      name: "로컬 맛집",
      category: "분식",
      rating: 4.7,
      image:
        "https://images.unsplash.com/photo-1565299585323-38d6b0865b47?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=200&q=80",
    },
  ],
  accommodations: [
    {
      id: "acc1",
      name: "좋은 호텔",
      category: "호텔",
      rating: 4.3,
      image:
        "https://images.unsplash.com/photo-1566073771259-6a8506099945?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=200&q=80",
    },
    {
      id: "acc2",
      name: "아늑한 펜션",
      category: "펜션",
      rating: 4.5,
      image:
        "https://images.unsplash.com/photo-1520250497591-112f2f40a3f4?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=200&q=80",
    },
  ],
  cafes: [
    {
      id: "cafe1",
      name: "뷰 좋은 카페",
      category: "카페",
      rating: 4.6,
      image:
        "https://images.unsplash.com/photo-1495474472287-4d71bcdd2085?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=200&q=80",
    },
    {
      id: "cafe2",
      name: "분위기 카페",
      category: "디저트",
      rating: 4.4,
      image:
        "https://images.unsplash.com/photo-1507133750040-4a8f57021571?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=200&q=80",
    },
  ],
}

const itinerary = [
  {
    day: 1,
    items: [
      { time: "09:00", title: "호텔 체크인 및 짐 보관", type: "기타" },
      { time: "10:30", title: "첫번째 관광지 방문", type: "관광" },
      { time: "13:00", title: "점심 식사", type: "식사" },
      { time: "15:00", title: "두번째 관광지 방문", type: "관광" },
      { time: "18:00", title: "저녁 식사", type: "식사" },
    ],
  },
  {
    day: 2,
    items: [
      { time: "09:00", title: "아침 식사", type: "식사" },
      { time: "10:30", title: "세번째 관광지 방문", type: "관광" },
      { time: "13:00", title: "점심 식사", type: "식사" },
      { time: "15:00", title: "네번째 관광지 방문", type: "관광" },
      { time: "18:00", title: "저녁 식사", type: "식사" },
    ],
  },
  {
    day: 3,
    items: [
      { time: "09:00", title: "아침 식사", type: "식사" },
      { time: "10:30", title: "다섯번째 관광지 방문", type: "관광" },
      { time: "13:00", title: "점심 식사", type: "식사" },
      { time: "15:00", title: "기념품 쇼핑", type: "쇼핑" },
      { time: "18:00", title: "호텔로 복귀 및 체크아웃", type: "기타" },
    ],
  },
]

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
</script>

<style scoped>
/* 추가 스타일이 필요한 경우 여기에 작성 */
</style>