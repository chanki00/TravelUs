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
              @click="openImageModal()"
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
        <!-- 장소 검색 패널 -->
        <div class="w-1/5 p-4 border-r h-full overflow-hidden flex flex-col">
          <TripSearch
            :destination="destination"
            :itinerary="itinerary"
            @add-to-itinerary="addToItinerary"
          />
        </div>

        <!-- 일정 목록 -->
        <div class="w-1/5 p-4 border-r h-full overflow-hidden">
          <TripItinerary
            v-model="activeDay"
            :itinerary="itinerary"
            @remove-item="removeFromItinerary"
            @update-memo="updateItineraryMemo"
            @reorder-items="reorderItineraryItems"
          />
        </div>

        <!-- 지도 -->
        <div class="w-2/5 p-4 h-full overflow-hidden">
          <TripPlannerMap
            :locations="[]"
            :itinerary="itinerary"
            :active-day="activeDay"
            :destination="destination"
          />
        </div>

        <!-- 채팅 영역 -->
        <!-- 채팅 영역 -->
        <div class="w-1/5 p-4 h-full overflow-hidden flex flex-col">
          <TripChat :chatroom-id="chatroomId" v-if="chatroomId" />

          <!-- 여행 팁 및 추천 맛집 섹션 -->
          <div
            v-if="aiPlanData && (aiPlanData.tips || aiPlanData.restaurants)"
            class="bg-white rounded-lg shadow p-4 overflow-y-auto max-h-[40%]"
          >
            <div v-if="aiPlanData.tips && aiPlanData.tips.length > 0" class="mb-4">
              <h3 class="font-bold text-gray-700 mb-2">여행 팁</h3>
              <ul class="list-disc pl-5 text-sm text-gray-600">
                <li v-for="(tip, index) in aiPlanData.tips" :key="`tip-${index}`" class="mb-1">
                  {{ tip }}
                </li>
              </ul>
            </div>

            <div v-if="aiPlanData.restaurants && aiPlanData.restaurants.length > 0">
              <h3 class="font-bold text-gray-700 mb-2">추천 맛집</h3>
              <ul class="list-disc pl-5 text-sm text-gray-600">
                <li
                  v-for="(restaurant, index) in aiPlanData.restaurants"
                  :key="`restaurant-${index}`"
                  class="mb-1"
                >
                  {{ restaurant }}
                </li>
              </ul>
            </div>
          </div>
        </div>
      </div>
    </main>

    <TripimageModal :is-open="isImageModalOpen" @close="closeImageModal" @save="onImageSelected" />

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
import { ref, nextTick, onMounted, computed, watch } from 'vue'
import TripPlannerMap from '@/components/trip/TripPlannerMap.vue'
import TripMembers from '@/components/trip/TripMembers.vue'
import TripSearch from '@/components/trip/TripSearch.vue'
import TripItinerary from '@/components/trip/TripItinerary.vue'
// import TripChat from '@/components/trip/Chat.vue'
import TripChat from '@/components/trip/TripChat.vue'
import TripTagModal from '@/components/trip/TripTagModal.vue'
import TripimageModal from '@/components/trip/TripimageModal.vue'
import { Pencil as PencilIcon } from 'lucide-vue-next'
import { useRoute, useRouter } from 'vue-router'
import api from '@/api'
import { useUserStore } from '@/store/user'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()
const user = computed(() => userStore.loginUser)
const chatroomId = ref(parseInt(route.query.chatroomId) || null)

// Get data from query parameters
const tripData = ref({
  destination: route.query.destination || '',
  duration: parseInt(route.query.duration) || 3, // 이미 일수로 전달됨
  members: parseInt(route.query.members) || 1,
  transport: route.query.transport || 'car',
})

// AI generated plan data
const aiPlanData = ref(null)
// 관광지 정보 맵
const attractionsMap = ref({})

const destination = ref(parseInt(tripData.value.destination) || null)
const planId = ref(null)

// 제목 및 설명 편집 관련 상태
// 수정: duration + 1일로 표시
const title = ref(`${tripData.value.duration - 1}박 ${tripData.value.duration}일 여행 계획`)
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
const isImageModalOpen = ref(false)
const image = ref({})

const tags = ref({
  id: [],
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
// 일정 초기화 함수
const initializeItinerary = () => {
  if (aiPlanData.value?.itinerary && aiPlanData.value.itinerary.length > 0) {
    // AI 생성 데이터가 있으면 변환하여 사용
    itinerary.value = convertAiPlanToItinerary() || []
  } else {
    // 기본 빈 일정 생성
    for (let i = 0; i < tripData.value.duration; i++) {
      itinerary.value.push({ day: i, items: [] })
    }
  }
}

const fetchPlan = async (id) => {
  try {
    const response = await api.get(`/api/v1/plan/${id}`)
    planId.value = id
    destination.value = parseInt(response.data.basicplan.destination) || null
    const resp_duration = response.data.basicplan.duration
    title.value = `${resp_duration}박 ${resp_duration + 1}일 여행 계획`

    // Reset itinerary
    itinerary.value = []
    for (let i = 0; i < resp_duration; i++) {
      itinerary.value.push({ day: i, items: [] })
    }
  } catch (error) {
    console.error('Error fetching plan:', error)
  }
}

// AI 생성 데이터를 itinerary 형식으로 변환
const convertAiPlanToItinerary = () => {
  if (!aiPlanData.value?.itinerary) return

  return aiPlanData.value.itinerary.map((day, index) => {
    return {
      day: index,
      items: day.places.map((place) => {
        // 관광지 ID로 실제 관광지 정보 가져오기
        const attractionId = place.id
        const attractionInfo = attractionsMap.value[attractionId] || null

        return {
          title: place.name,
          type: place.type || '관광',
          memo: place.description || '',
          time: '',
          image: attractionInfo?.image || '/placeholder.svg?height=150&width=150',
          placeData: {
            no: attractionId,
            latitude: attractionInfo?.latitude || 0,
            longitude: attractionInfo?.longitude || 0,
            contentTypeId: getContentTypeId(place.type),
            title: place.name,
            addr: attractionInfo?.addr || '',
            image: attractionInfo?.image || '/placeholder.svg?height=150&width=150',
          },
        }
      }),
    }
  })
}

// 장소 유형에 따른 contentTypeId 반환
const getContentTypeId = (type) => {
  switch (type) {
    case '식당':
      return 39
    case '숙소':
      return 32
    case '문화시설':
      return 14
    case '축제/공연/행사':
      return 15
    case '여행코스':
      return 25
    case '레포츠':
      return 28
    case '쇼핑':
      return 38
    default:
      return 12 // 기본값은 관광지
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

// Open image modal
const openImageModal = () => {
  isImageModalOpen.value = true
}

// Close image modal
const closeImageModal = () => {
  isImageModalOpen.value = false
}

const onImageSelected = (selectedImage) => {
  // 이미지 선택 후 image modal 닫고 tag modal 열기
  image.value = selectedImage
  isImageModalOpen.value = false
  isTagModalOpen.value = true
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
}

// 일정에서 장소 제거
const removeFromItinerary = ({ dayIndex, itemIndex }) => {
  itinerary.value[dayIndex].items.splice(itemIndex, 1)
}

// 일정 메모 업데이트
const updateItineraryMemo = ({ dayIndex, itemIndex, memo }) => {
  console.log('메모 업데이트:', dayIndex, itemIndex, memo)
  itinerary.value[dayIndex].items[itemIndex].memo = memo
}

// 일정 항목 순서 변경
const reorderItineraryItems = ({ dayIndex, fromIndex, toIndex }) => {
  const day = itinerary.value[dayIndex]
  if (!day || !day.items) return

  // 항목 이동
  const item = day.items[fromIndex]
  day.items.splice(fromIndex, 1)
  day.items.splice(toIndex, 0, item)
}

// 일정 생성
const createPlan = async () => {
  try {
    // 이후 plan 생성 요청에 chatroomId 포함
    const planRes = await api.post('/api/v1/plan', {
      destination: tripData.value.destination,
      duration: tripData.value.duration,
      members: tripData.value.members,
      transport: tripData.value.transport,
      userId: user.value.id,
      title: title.value,
      description: description.value,
      likes: 0,
      shares: 0,
      image: image.value.img,
      chatroomId: chatroomId, // ✅ 포함
    })

    planId.value = planRes.data

    for (let i = 0; i < tripData.value.duration; i++) {
      const dayId_resp = await api.get(`/api/v1/plan/tripplandays/${planId.value}/${i + 1}`)

      // 해당 일차의 아이템들 처리
      const dayItems = itinerary.value[i]?.items || []

      for (let j = 0; j < dayItems.length; j++) {
        const item = dayItems[j]

        // placeData가 있는지 확인하고 attractionId 추출
        let attractionId = null
        if (item.placeData && item.placeData.no) {
          attractionId = item.placeData.no
        } else if (item.placeData && item.placeData.attraction_id) {
          attractionId = item.placeData.attraction_id
        } else {
          console.warn(`Day ${i + 1}, Item ${j + 1}: attractionId를 찾을 수 없습니다.`, item)
          continue // 이 아이템은 건너뛰기
        }

        try {
          const itineraryResponse = await api.post(`/api/v1/plan/itinerary`, {
            dayId: dayId_resp.data,
            attractionId: attractionId,
            placeOrder: j + 1,
            memo: item.memo || '',
          })
        } catch (itemError) {
          console.error(`Day ${i + 1}, Item ${j + 1} 저장 실패:`, itemError)
          // 개별 아이템 저장 실패 시에도 계속 진행
        }
      }
    }
    console.log('aaaaaaaaaaaaaaa')
    tags.value.id.forEach(async (tagId) => {
      await api.post(`/api/v1/tag/tripplan/${planId.value}/${tagId}`)
    })

    router.push('/')
  } catch (error) {
    console.error('Error creating plan:', error)
    alert('일정 저장 중 오류가 발생했습니다.')
  }
}
// AI 생성 데이터 파싱
const parseAiPlanData = () => {
  if (route.query.aiPlan) {
    try {
      aiPlanData.value = JSON.parse(route.query.aiPlan)

      // AI 생성 데이터로 제목과 설명 설정
      if (aiPlanData.value.title) {
        title.value = aiPlanData.value.title
      }

      if (aiPlanData.value.description) {
        description.value = aiPlanData.value.description
      }
    } catch (e) {
      console.error('Failed to parse AI plan data:', e)
    }
  }
}
// 관광지 정보 맵 파싱
const parseAttractionsMap = () => {
  if (route.query.attractions) {
    try {
      attractionsMap.value = JSON.parse(route.query.attractions)
    } catch (e) {
      console.error('Failed to parse attractions data:', e)
    }
  }
}

onMounted(() => {
  parseAiPlanData()
  parseAttractionsMap()

  // 가져온 여행 계획 데이터 처리
  if (route.query.itinerary) {
    try {
      const importedItinerary = JSON.parse(route.query.itinerary)
      itinerary.value = importedItinerary

      // 제목과 설명 설정
      if (route.query.title) {
        title.value = route.query.title
      }
      if (route.query.description) {
        description.value = route.query.description
      }
    } catch (e) {
      console.error('Failed to parse imported itinerary:', e)
      initializeItinerary()
    }
  } else {
    initializeItinerary()
  }

  // If planId exists in route params, fetch the plan
  if (route.params.planId) {
    fetchPlan(route.params.planId)
  }
})

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
