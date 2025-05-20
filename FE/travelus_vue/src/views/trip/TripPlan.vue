<template>
  <div class="min-h-screen bg-gradient-to-b from-blue-50 to-white flex flex-col h-screen">
    <!-- Header -->
    <header class="py-4 px-6 border-b bg-white">
      <div class="max-w-screen-2xl mx-auto">
        <div class="flex flex-col md:flex-row justify-between items-start md:items-center">
          <div class="flex-1">
            <div class="flex items-center gap-2 mb-2">
              <input 
                v-if="isEditingTitle" 
                v-model="editedTitle" 
                @blur="saveTitle"
                @keyup.enter="saveTitle"
                class="text-3xl font-bold w-full border-b border-blue-500 focus:outline-none bg-blue-50 px-2 py-1 rounded"
                ref="titleInput"
                placeholder="여행 제목을 입력하세요"
              />
              <h1 
                v-else 
                class="text-3xl font-bold cursor-pointer group flex items-center" 
                @click="startEditingTitle"
              >
                {{ title }}
                <pencil-icon class="h-5 w-5 ml-2 text-gray-400 opacity-0 group-hover:opacity-100 transition-opacity" />
              </h1>
            </div>
            
            <div class="flex items-start gap-2">
              <textarea 
                v-if="isEditingDescription" 
                v-model="editedDescription" 
                @blur="saveDescription"
                class="text-gray-500 w-full border border-blue-500 focus:outline-none bg-blue-50 px-2 py-1 rounded resize-none"
                rows="2"
                ref="descriptionTextarea"
                placeholder="여행 계획에 대한 설명을 입력하세요"
              ></textarea>
              <p 
                v-else 
                class="text-gray-500 cursor-pointer group flex items-start" 
                @click="startEditingDescription"
              >
                {{ description }}
                <pencil-icon class="h-4 w-4 ml-1 text-gray-400 opacity-0 group-hover:opacity-100 transition-opacity mt-0.5" />
              </p>
            </div>
          </div>

          <div class="flex gap-3 mt-4 md:mt-0">
            <TripMembers :members="mockMembers" />
            <button class="px-4 py-2 border rounded-md hover:bg-gray-50">일정 저장</button>
            <button class="px-4 py-2 border rounded-md hover:bg-gray-50">공유하기</button>
            <button class="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700">일정 수정</button>
          </div>
        </div>
      </div>
    </header>

    <!-- Main Content -->
    <main class="flex-1 overflow-hidden">
      <div class="h-full flex">
        <!-- 장소 검색 패널 -->
        <div class="w-1/5 p-4 border-r h-full overflow-hidden">
          <TripSearch @add-to-itinerary="addToItinerary" />
        </div>

        <!-- 일정 목록 -->
        <div class="w-1/5 p-4 border-r h-full overflow-hidden">
          <TripItinerary 
            :itinerary="itinerary" 
            @remove-item="removeFromItinerary"
          />
        </div>

        <!-- 지도 -->
        <div class="w-2/5 p-4 h-full overflow-hidden">
          <TripPlannerMap :locations="[]" />
        </div>

        <!-- 채팅 영역 -->
        <div class="w-1/5 p-4 h-full overflow-hidden">
          <TripChat />
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, nextTick } from 'vue'
import TripPlannerMap from '@/components/trip/TripPlannerMap.vue'
import TripMembers from '@/components/trip/TripMembers.vue'
import TripSearch from '@/components/trip/TripSearch.vue'
import TripItinerary from '@/components/trip/TripItinerary.vue'
import TripChat from '@/components/trip/TripChat.vue'
import { Pencil as PencilIcon } from 'lucide-vue-next'

// 목업 데이터
const destination = '제주도'
const duration = '3박 4일'

// 제목 및 설명 편집 관련 상태
const title = ref(`${destination} ${duration} 여행 계획`)
const description = ref('여행 계획을 확인하고 참여자들과 의견을 나눠보세요')
const isEditingTitle = ref(false)
const isEditingDescription = ref(false)
const editedTitle = ref('')
const editedDescription = ref('')
const titleInput = ref(null)
const descriptionTextarea = ref(null)

// 제목 편집 시작
const startEditingTitle = () => {
  editedTitle.value = title.value
  isEditingTitle.value = true
  nextTick(() => {
    titleInput.value.focus()
  })
}

// 제목 저장
const saveTitle = () => {
  if (editedTitle.value.trim()) {
    title.value = editedTitle.value
  }
  isEditingTitle.value = false
}

// 설명 편집 시작
const startEditingDescription = () => {
  editedDescription.value = description.value
  isEditingDescription.value = true
  nextTick(() => {
    descriptionTextarea.value.focus()
  })
}

// 설명 저장
const saveDescription = () => {
  if (editedDescription.value.trim()) {
    description.value = editedDescription.value
  }
  isEditingDescription.value = false
}

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

// 빈 일정으로 초기화
const itinerary = ref([
  {
    day: 1,
    items: []
  },
  {
    day: 2,
    items: []
  },
  {
    day: 3,
    items: []
  },
])

// 일정에 장소 추가
const addToItinerary = (item) => {
  // 현재 활성화된 일차에 항목 추가
  const activeDay = 0 // 기본적으로 첫 번째 날에 추가 (실제로는 TripItinerary의 activeDay를 가져와야 함)
  itinerary.value[activeDay].items.push(item)
}

// 일정에서 장소 제거
const removeFromItinerary = ({ dayIndex, itemIndex }) => {
  itinerary.value[dayIndex].items.splice(itemIndex, 1)
}
</script>

<style scoped>
/* 추가 스타일이 필요한 경우 여기에 작성 */
</style>
