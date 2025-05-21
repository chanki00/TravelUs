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
                <pencil-icon
                  class="h-5 w-5 ml-2 text-gray-400"
                />
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
                <pencil-icon
                  class="h-4 w-4 ml-1 text-gray-400 mt-0.5"
                />
              </p>
            </div>
          </div>

          <div class="flex gap-3 mt-4 md:mt-0">
            <TripMembers :members="mockMembers" />
            <button class="px-4 py-2 border rounded-md hover:bg-gray-50">공유하기</button>
            <button @click="openTagModal()" class="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700">
              일정 저장
            </button>
          </div>
        </div>
      </div>
    </header>

    <!-- Main Content -->
    <main class="flex-1 overflow-hidden">
      <div class="h-full flex">
        <!-- 장소 검색 패널 -->
        <div class="w-1/5 p-4 border-r h-full overflow-hidden flex flex-col">
          <TripSearch :destination="destination" @add-to-itinerary="addToItinerary" />
        </div>

        <!-- 일정 목록 -->
        <div class="w-1/5 p-4 border-r h-full overflow-hidden">
          <TripItinerary v-model="activeDay" :itinerary="itinerary" @remove-item="removeFromItinerary" />
        </div>

        <!-- 지도 -->
        <div class="w-2/5 p-4 h-full overflow-hidden">
          <TripPlannerMap :locations="[]" :itinerary="itinerary" :active-day="activeDay"/>
        </div>

        <!-- 채팅 영역 -->
        <div class="w-1/5 p-4 h-full overflow-hidden">
          <TripChat />
        </div>
      </div>
    </main>
    <TripTagModal 
      :is-open="isTagModalOpen" 
      :initial-tags="tags"
      @close="closeTagModal" 
      @save="saveWithTags" 
    />
  </div>
</template>

<script setup>
import { ref, nextTick, onMounted, watch } from 'vue'
import TripPlannerMap from '@/components/trip/TripPlannerMap.vue'
import TripMembers from '@/components/trip/TripMembers.vue'
import TripSearch from '@/components/trip/TripSearch.vue'
import TripItinerary from '@/components/trip/TripItinerary.vue'
import TripChat from '@/components/trip/TripChat.vue'
import TripTagModal from '@/components/trip/TripTagModal.vue'
import { Pencil as PencilIcon } from 'lucide-vue-next'
import { useRoute, useRouter } from 'vue-router'
import api from '@/api'
import { defineProps, defineEmits } from 'vue'

const route = useRoute()
const router = useRouter()

// Get data from query parameters
const tripData = ref({
  destination: route.query.destination || '',
  duration: parseInt(route.query.duration) || 3,
  members: parseInt(route.query.members) || 1,
  transport: route.query.transport || 'car'
})

const destination = ref(parseInt(tripData.value.destination) || null)
const planId = ref(null)

// 제목 및 설명 편집 관련 상태
const title = ref(`${tripData.value.duration}박 ${tripData.value.duration + 1}일 여행 계획`)
const description = ref('여행 계획에 대한 간략한 소개말을 작성해보세요.')
const isEditingTitle = ref(false)
const isEditingDescription = ref(false)
const editedTitle = ref('')
const editedDescription = ref('')
const titleInput = ref(null)
const descriptionTextarea = ref(null)
const activeDay = ref(0)

// 일정 초기화
const itinerary = ref([])

const isTagModalOpen = ref(false)
const tags = ref({
  style: [],
  theme: [],
  season: []
})
onMounted(() => {
  // Initialize itinerary based on duration
  for (let i = 0; i < tripData.value.duration; i++) {
    itinerary.value.push({ day: i, items: [] })
  }
  
  // If planId exists in route params, fetch the plan
  if (route.params.planId) {
    fetchPlan(route.params.planId)
  }
})

// Fetch plan if editing an existing plan
const fetchPlan = async (id) => {
  try {
    const response = await api.get(`/api/v1/plan/${id}`)
    planId.value = id
    destination.value = parseInt(response.data.basicplan.destination) || null
    const resp_duration = response.data.basicplan.duration
    title.value = `${resp_duration}박 ${resp_duration+1}일 여행 계획`
    
    // Reset itinerary
    itinerary.value = []
    for (let i = 0; i < resp_duration; i++) {
      itinerary.value.push({day: i, items: []})
    }
  } catch (error) {
    console.error('Error fetching plan:', error)
  }
}

// 제목 편집 시작
const startEditingTitle = () => {
  editedTitle.value = title.value
  isEditingTitle.value = true
  nextTick(() => {
    if (titleInput.value) {
      titleInput.value.focus()
    }
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

// Open tag modal
const openTagModal = () => {
  isTagModalOpen.value = true
}

// Close tag modal
const closeTagModal = () => {
  isTagModalOpen.value = false
}
// Save with tags
const saveWithTags = (selectedTags) => {
  tags.value = selectedTags
  isTagModalOpen.value = false
  

  createPlan()
}

const mockMembers = [
  {
    id: '1',
    name: '여행좋아',
    role: '호스트',
    status: 'online',
    image: 'https://i.pravatar.cc/150?img=1',
  },
  {
    id: '2',
    name: '산타클로스',
    role: '참여자',
    status: 'online',
    image: 'https://i.pravatar.cc/150?img=2',
  },
  {
    id: '3',
    name: '맛집탐방러',
    role: '참여자',
    status: 'offline',
    image: 'https://i.pravatar.cc/150?img=3',
  },
]

// 일정에 장소 추가
const addToItinerary = (item) => {
  // 현재 활성화된 일차에 항목 추가
  itinerary.value[activeDay.value].items.push(item)
  console.log(itinerary.value)
}

// 일정에서 장소 제거
const removeFromItinerary = ({ dayIndex, itemIndex }) => {
  itinerary.value[dayIndex].items.splice(itemIndex, 1)
}

// 일정 생성 (이전에 updatePlan이었던 함수를 createPlan으로 변경)
const createPlan = async () => {
  try {
    // Create the plan with the data from TripHumanPlan
    const response = await api.post('/api/v1/plan', {
      destination: tripData.value.destination,
      duration: tripData.value.duration,
      members: tripData.value.members,
      transport: tripData.value.transport,
      userId : 1,
      title: title.value,
      description: description.value,
      likes : 0,
      shares : 0,
    })
    
    planId.value = response.data
    
    for (let i = 0; i < tripData.value.duration; i++) {
      const dayId_resp = await api.get(`/api/v1/plan/tripplandays/${planId.value}/${i + 1}`);
      for (let j = 0; j < itinerary.value[i].items.length; j++){
          const response = await api.post(`/api/v1/plan/itinerary/${dayId_resp.data}/${itinerary.value[i].items[j].placeData.no}/${j+1}`);
      }
    }

    

    router.push("/")
  } catch (error) {
    console.error('Error creating plan:', error)
    alert('일정 저장 중 오류가 발생했습니다.')
  }
}

// 일정 업데이트 (기존 계획을 수정할 때 사용)
// const updatePlan = async () => {
//   if (!planId.value) {
//     // If no planId, create a new plan
//     return createPlan()
//   }
  
//   try {
//     await api.put(`/api/v1/plan/${planId.value}`, {
//       destination: destination.value,
//       duration: itinerary.value.length,
//       members: tripData.value.members,
//       transport: tripData.value.transport,
//       title: title.value,
//       description: description.value,
//       itinerary: itinerary.value
//     })
    
//     router.push(`/planresult/${planId.value}`)
//   } catch (error) {
//     console.error('Error updating plan:', error)
//     alert('일정 업데이트 중 오류가 발생했습니다.')
//   }
// }
</script>

<style scoped>
/* 추가 스타일이 필요한 경우 여기에 작성 */
</style>
