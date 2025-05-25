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
                <pencil-icon class="h-5 w-5 ml-2 text-gray-400" />
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
                <pencil-icon class="h-4 w-4 ml-1 text-gray-400 mt-0.5" />
              </p>
            </div>
          </div>

          <div class="flex gap-3 mt-4 md:mt-0">
            <TripMembers :members="mockMembers" />
            <button class="px-4 py-2 border rounded-md hover:bg-gray-50">공유하기</button>
            <button
              @click="handleSave()"
              class="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700"
            >
              일정 저장
            </button>
          </div>
        </div>
      </div>
    </header>

    <!-- Main Content -->
    <main class="flex-1 overflow-hidden">
      <div class="h-full flex">
        <div class="w-1/5 p-4 border-r h-full overflow-hidden flex flex-col">
          <TripSearch
            :destination="destination"
            :itinerary="itinerary"
            @add-to-itinerary="addToItinerary"
          />
        </div>

        <div class="w-1/5 p-4 border-r h-full overflow-hidden">
          <TripItinerary
            v-model="activeDay"
            :itinerary="itinerary"
            @remove-item="removeFromItinerary"
            @update-memo="updateItineraryMemo"
            @reorder-items="reorderItineraryItems"
          />
        </div>

        <div class="w-2/5 p-4 h-full overflow-hidden">
          <TripPlannerMap
            :locations="[]"
            :itinerary="itinerary"
            :active-day="activeDay"
            :destination="destination"
          />
        </div>

        <div class="w-1/5 p-4 h-full overflow-hidden flex flex-col">
          <TripChat :chatroom-id="chatroomId" v-if="chatroomId" />

          <div
            v-if="aiPlanData && (aiPlanData.tips || aiPlanData.restaurants)"
            class="bg-white rounded-lg shadow p-4 overflow-y-auto max-h-[40%]"
          >
            <div v-if="aiPlanData.tips?.length" class="mb-4">
              <h3 class="font-bold text-gray-700 mb-2">여행 팁</h3>
              <ul class="list-disc pl-5 text-sm text-gray-600">
                <li v-for="(tip, index) in aiPlanData.tips" :key="`tip-${index}`">{{ tip }}</li>
              </ul>
            </div>

            <div v-if="aiPlanData.restaurants?.length">
              <h3 class="font-bold text-gray-700 mb-2">추천 맛집</h3>
              <ul class="list-disc pl-5 text-sm text-gray-600">
                <li
                  v-for="(restaurant, index) in aiPlanData.restaurants"
                  :key="`restaurant-${index}`"
                >
                  {{ restaurant }}
                </li>
              </ul>
            </div>
          </div>
        </div>
      </div>
    </main>

    <TripimageModal
      :is-open="isImageModalOpen"
      @close="closeImageModal"
      @save="onImageSelected"
      :itinerary="itinerary"
      :title="title"
      :description="description"
    />

    <TripTagModal
      :is-open="isTagModalOpen"
      :initial-tags="tags"
      @close="closeTagModal"
      @save="saveWithTags"
    />
  </div>
</template>

<script setup>
import { ref, nextTick, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '@/store/user'
import api from '@/api'

import TripPlannerMap from '@/components/trip/TripPlannerMap.vue'
import TripMembers from '@/components/trip/TripMembers.vue'
import TripSearch from '@/components/trip/TripSearch.vue'
import TripItinerary from '@/components/trip/TripItinerary.vue'
import TripChat from '@/components/trip/TripChat.vue'
import TripTagModal from '@/components/trip/TripTagModal.vue'
import TripimageModal from '@/components/trip/TripimageModal.vue'
import { Pencil as PencilIcon } from 'lucide-vue-next'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()
const user = computed(() => userStore.loginUser)

const planId = ref(route.params.planId)
const title = ref('')
const description = ref('')
const destination = ref(null)
const activeDay = ref(0)
const itinerary = ref([])
const isEditingTitle = ref(false)
const isEditingDescription = ref(false)
const editedTitle = ref('')
const editedDescription = ref('')
const titleInput = ref(null)
const descriptionTextarea = ref(null)
const isTagModalOpen = ref(false)
const isImageModalOpen = ref(false)
const image = ref({})
const tags = ref({ id: [] })
const aiPlanData = ref(null)
const chatroomId = ref(null)

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

// Fetch plan data from API
const fetchPlan = async (id) => {
  try {
    // 1. 기본 여행 정보 불러오기
    const response = await api.get(`/api/v1/plan/${id}`)
    const plan = response.data
    console.log(plan)
    title.value = plan?.title || `${plan?.duration}박 ${plan?.duration + 1}일 여행 계획`
    description.value = plan?.description || ''
    destination.value = parseInt(plan?.destination) || null

    // 2. 일정 데이터 별도 불러오기
    const itineraryRes = await api.get(`/api/v1/plan/itinerary/${id}`)
    const rawItinerary = itineraryRes.data // ItineraryPlaceResponseDto[] 배열
    // 3. dayNumber 기준으로 일정 데이터를 그룹핑
    const grouped = rawItinerary.reduce((acc, item) => {
      const day = item.dayNumber
      if (!acc[day]) {
        acc[day] = {
          dayNumber: day,
          dayId: item.dayId,
          items: [],
        }
      }
      acc[day].items.push({
        title: item.title,
        memo: item.memo,
        time: '',
        image: item.image || '/placeholder.svg?height=150&width=150',
        placeData: {
          no: item.no || 0,
          latitude: item.latitude,
          longitude: item.longitude,
          contentTypeId: item.contentId,
          title: item.title,
          addr: item.addr,
          image: item.image || '/placeholder.svg?height=150&width=150',
        },
      })

      return acc
    }, {})

    // dayNumber 순으로 정렬
    const groupedList = Object.values(grouped).sort((a, b) => a.dayNumber - b.dayNumber)
    itinerary.value = groupedList
    // 기타 데이터
    aiPlanData.value = plan.aiSuggestions || null
    tags.value.id.forEach(async (tagId) => {
      await api.post(`/api/v1/tag/tripplan/${planId.value}/${tagId}`)
    })
    chatroomId.value = plan.chatroomId || null
  } catch (error) {
    console.error('Error fetching plan:', error)
  }
}

const startEditingTitle = () => {
  editedTitle.value = title.value
  isEditingTitle.value = true
  nextTick(() => titleInput.value?.focus())
}
const saveTitle = () => {
  if (editedTitle.value.trim()) title.value = editedTitle.value
  isEditingTitle.value = false
}

const startEditingDescription = () => {
  editedDescription.value = description.value
  isEditingDescription.value = true
  nextTick(() => descriptionTextarea.value?.focus())
}
const saveDescription = () => {
  if (editedDescription.value.trim()) description.value = editedDescription.value
  isEditingDescription.value = false
}

const openImageModal = () => {
  isImageModalOpen.value = true
}
const closeImageModal = () => {
  isImageModalOpen.value = false
}
const onImageSelected = (selectedImage) => {
  image.value = selectedImage
  isImageModalOpen.value = false
  isTagModalOpen.value = true
}
const openTagModal = () => {
  isTagModalOpen.value = true
}
const closeTagModal = () => {
  isTagModalOpen.value = false
}
const saveWithTags = async (selectedTags) => {
  tags.value = selectedTags
  isTagModalOpen.value = false

  if (planId.value) {
    // 수정 모드
    await updatePlan()
  } else {
    // 생성 모드 - createPlan() 함수 구현 필요
    console.log('Create plan logic needed')
  }
}

const addToItinerary = (item) => {
  itinerary.value[activeDay.value].items.push(item)
}
const removeFromItinerary = ({ dayIndex, itemIndex }) => {
  itinerary.value[dayIndex].items.splice(itemIndex, 1)
}
const updateItineraryMemo = ({ dayIndex, itemIndex, memo }) => {
  itinerary.value[dayIndex].items[itemIndex].memo = memo
}
const reorderItineraryItems = ({ dayIndex, fromIndex, toIndex }) => {
  const day = itinerary.value[dayIndex]
  const item = day.items[fromIndex]
  day.items.splice(fromIndex, 1)
  day.items.splice(toIndex, 0, item)
}

onMounted(() => {
  if (planId.value) {
    fetchPlan(planId.value)
  }
})

// 여행 계획 업데이트 함수
const updatePlan = async () => {
  try {
    // 1. 기본 계획 정보 업데이트
    await api.put(`/api/v1/plan/${planId.value}`, {
      destination: destination.value,
      duration: itinerary.value.length,
      members: mockMembers.length, // 또는 실제 멤버 수
      transport: 'car', // 기본값 또는 실제 교통수단
      userId: user.value.id,
      title: title.value,
      description: description.value,
      image: image.value.img || '',
      chatroomId: chatroomId.value,
    })

    // 2. 기존 일정 삭제
    await api.delete(`/api/v1/plan/itinerary/${planId.value}`)

    // 3. 새로운 일정 추가
    for (let i = 0; i < itinerary.value.length; i++) {
      const dayId_resp = await api.get(`/api/v1/plan/tripplandays/${planId.value}/${i + 1}`)
      const dayItems = itinerary.value[i]?.items || []

      for (let j = 0; j < dayItems.length; j++) {
        const item = dayItems[j]

        let attractionId = null
        if (item.placeData && item.placeData.no) {
          attractionId = item.placeData.no
        } else {
          console.warn(`Day ${i + 1}, Item ${j + 1}: attractionId를 찾을 수 없습니다.`, item)
          continue
        }

        try {
          await api.post(`/api/v1/plan/itinerary`, {
            dayId: dayId_resp.data,
            attractionId: attractionId,
            placeOrder: j + 1,
            memo: item.memo || '',
          })
        } catch (itemError) {
          console.error(`Day ${i + 1}, Item ${j + 1} 저장 실패:`, itemError)
        }
      }
    }

    // 4. 태그 업데이트 (기존 태그 삭제 후 새로 추가)
    tags.value.id.forEach(async (tagId) => {
      await api.post(`/api/v1/tag/tripplan/${planId.value}/${tagId}`)
    })

    alert('여행 계획이 성공적으로 수정되었습니다.')
    router.push(`/tripdetail/${planId.value}`)
  } catch (error) {
    console.error('Error updating plan:', error)
    alert('일정 수정 중 오류가 발생했습니다.')
  }
}

// 저장하기 버튼 클릭 시 호출할 함수 수정
const handleSave = async () => {
  if (planId.value) {
    // 수정 모드
    openImageModal()
  } else {
    // 생성 모드 (기존 로직)
    openImageModal()
  }
}
</script>

<style scoped>
/* Custom styles if needed */
</style>
