<template>
  <div class="min-h-screen bg-gradient-to-b from-blue-50 to-white flex flex-col h-screen">
    <!-- CollaborativeEditor 래퍼 추가 -->
    <CollaborativeEditor
      :plan-id="parseInt(planId)"
      :user-id="user.id"
      :user-name="user.name"
      @conflict-detected="handleConflictDetected"
      @change-received="handleRemoteChange"
      @force-update="handleForceUpdate"
      @accept-remote-changes="handleAcceptRemoteChanges"
      ref="collaborativeEditor"
    >
      <!-- 기존 Header 내용 -->
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
                  @input="onTitleChange"
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
                  @input="onDescriptionChange"
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
              <TripMembers
  v-if="chatroomId"
  :chatroom-id="chatroomId"
  :plan-id="parseInt(planId)"
  :current-user-id="user.id"
  @members-updated="handleMembersUpdated"
  ref="tripMembers"
/>

              <button class="px-4 py-2 border rounded-md hover:bg-gray-50">공유하기</button>
              <button
                @click="handleSave()"
                class="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700"
                :disabled="isSaving"
              >
                {{ isSaving ? '저장 중...' : '일정 저장' }}
              </button>
            </div>
          </div>
        </div>
      </header>

      <!-- 기존 Main Content -->
      <main class="flex-1 overflow-hidden">
        <div class="h-full flex">
          <div class="w-1/5 p-4 border-r h-screen overflow-hidden flex flex-col">
            <TripSearch
              :destination="destination"
              :itinerary="itinerary"
              @add-to-itinerary="addToItinerary"
            />
          </div>

          <div class="w-1/5 p-4 border-r h-screen overflow-hidden">
            <TripItinerary
              v-model="activeDay"
              :itinerary="itinerary"
              @remove-item="removeFromItinerary"
              @update-memo="updateItineraryMemo"
              @reorder-items="reorderItineraryItems"
            />
          </div>

          <div class="w-2/5 p-4 h-screen overflow-hidden">
            <TripPlannerMap
              :locations="[]"
              :itinerary="itinerary"
              :active-day="activeDay"
              :destination="destination"
            />
          </div>

          <div class="w-1/5 p-4 h-screen overflow-hidden flex flex-col">
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
    </CollaborativeEditor>

    <!-- 기존 모달들 -->
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
import CollaborativeEditor from '@/components/trip/CollaborativeEditor.vue'
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
const isSaving = ref(false)
const currentVersion = ref(1)
const collaborativeEditor = ref(null)

// 디바운스를 위한 타이머
let titleChangeTimer = null
let descriptionChangeTimer = null

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

// 실시간 제목 변경 감지
const onTitleChange = () => {
  clearTimeout(titleChangeTimer)
  titleChangeTimer = setTimeout(() => {
    if (collaborativeEditor.value) {
      collaborativeEditor.value.broadcastChange('TITLE_CHANGE', {
        title: editedTitle.value,
        version: currentVersion.value,
      })
    }
  }, 500) // 500ms 디바운스
}

// 실시간 설명 변경 감지
const onDescriptionChange = () => {
  clearTimeout(descriptionChangeTimer)
  descriptionChangeTimer = setTimeout(() => {
    if (collaborativeEditor.value) {
      collaborativeEditor.value.broadcastChange('DESCRIPTION_CHANGE', {
        description: editedDescription.value,
        version: currentVersion.value,
      })
    }
  }, 500) // 500ms 디바운스
}

// 원격 변경사항 처리
const handleRemoteChange = (event) => {
  switch (event.type) {
    case 'TITLE_CHANGE':
      if (!isEditingTitle.value) {
        title.value = event.data.title
      }
      break
    case 'DESCRIPTION_CHANGE':
      if (!isEditingDescription.value) {
        description.value = event.data.description
      }
      break
    case 'ITINERARY_CHANGE':
      // 현재 편집 중이 아닌 경우에만 업데이트
      if (event.data.itinerary) {
        itinerary.value = event.data.itinerary
      }
      break
  }

  // 버전 업데이트
  if (event.version > currentVersion.value) {
    currentVersion.value = event.version
  }
}

// 충돌 감지 처리
const handleConflictDetected = (conflictData) => {
  console.log('Conflict detected:', conflictData)
  // CollaborativeEditor에서 UI로 처리됨
}

// 강제 업데이트 (내 변경사항 유지)
const handleForceUpdate = async (data) => {
  try {
    await updatePlanWithVersion(data, true)
  } catch (error) {
    console.error('Force update failed:', error)
  }
}

// 원격 변경사항 수락
const handleAcceptRemoteChanges = (data) => {
  title.value = data.title || title.value
  description.value = data.description || description.value
  if (data.itinerary) {
    itinerary.value = data.itinerary
  }
  currentVersion.value = data.version || currentVersion.value
}

// 일정 항목 추가 시 실시간 동기화
const addToItinerary = (item) => {
  itinerary.value[activeDay.value].items.push(item)

  if (collaborativeEditor.value) {
    collaborativeEditor.value.broadcastChange('ITINERARY_CHANGE', {
      type: 'ADD_ITEM',
      dayIndex: activeDay.value,
      item: item,
      itinerary: itinerary.value,
      version: currentVersion.value,
    })
  }
}

// 일정 항목 제거 시 실시간 동기화
const removeFromItinerary = ({ dayIndex, itemIndex }) => {
  itinerary.value[dayIndex].items.splice(itemIndex, 1)

  if (collaborativeEditor.value) {
    collaborativeEditor.value.broadcastChange('ITINERARY_CHANGE', {
      type: 'REMOVE_ITEM',
      dayIndex,
      itemIndex,
      itinerary: itinerary.value,
      version: currentVersion.value,
    })
  }
}

// 일정 메모 업데이트 시 실시간 동기화
const updateItineraryMemo = ({ dayIndex, itemIndex, memo }) => {
  itinerary.value[dayIndex].items[itemIndex].memo = memo

  if (collaborativeEditor.value) {
    collaborativeEditor.value.broadcastChange('ITINERARY_CHANGE', {
      type: 'UPDATE_MEMO',
      dayIndex,
      itemIndex,
      memo,
      itinerary: itinerary.value,
      version: currentVersion.value,
    })
  }
}

// 일정 순서 변경 시 실시간 동기화
const reorderItineraryItems = ({ dayIndex, fromIndex, toIndex }) => {
  const day = itinerary.value[dayIndex]
  const item = day.items[fromIndex]
  day.items.splice(fromIndex, 1)
  day.items.splice(toIndex, 0, item)

  if (collaborativeEditor.value) {
    collaborativeEditor.value.broadcastChange('ITINERARY_CHANGE', {
      type: 'REORDER_ITEMS',
      dayIndex,
      fromIndex,
      toIndex,
      itinerary: itinerary.value,
      version: currentVersion.value,
    })
  }
}

// 버전 체크와 함께 계획 업데이트
const updatePlanWithVersion = async (data = null, forceUpdate = false) => {
  try {
    isSaving.value = true

    const updateData = data || {
      destination: destination.value,
      duration: itinerary.value.length,
      members: mockMembers.length,
      transport: 'car',
      userId: user.value.id,
      title: title.value,
      description: description.value,
      image: image.value.img || '',
      chatroomId: chatroomId.value,
      version: currentVersion.value,
    }

    // 버전 충돌 체크 (강제 업데이트가 아닌 경우)
    if (!forceUpdate) {
      const latestPlan = await api.get(`/api/v1/plan/${planId.value}`)
      if (latestPlan.data.version > currentVersion.value) {
        // 충돌 감지
        if (collaborativeEditor.value) {
          collaborativeEditor.value.broadcastChange('CONFLICT_DETECTED', {
            conflictType: 'VERSION_CONFLICT',
            currentValue: latestPlan.data,
            incomingValue: updateData,
            currentVersion: latestPlan.data.version,
            incomingVersion: currentVersion.value,
            lastModifiedBy: latestPlan.data.lastEditor,
            lastModifiedAt: latestPlan.data.updatedAt,
          })
        }
        return
      }
    }

    // 기본 계획 정보 업데이트
    await api.put(`/api/v1/plan/${planId.value}`, updateData)

    // 기존 일정 삭제 후 새로 추가
    await api.delete(`/api/v1/plan/itinerary/${planId.value}`)

    // 새로운 일정 추가
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

    // 태그 업데이트
    tags.value.id.forEach(async (tagId) => {
      await api.post(`/api/v1/tag/tripplan/${planId.value}/${tagId}`)
    })

    // 버전 증가
    currentVersion.value++

    alert('여행 계획이 성공적으로 수정되었습니다.')
    router.push(`/tripdetail/${planId.value}`)
  } catch (error) {
    console.error('Error updating plan:', error)
    alert('일정 수정 중 오류가 발생했습니다.')
  } finally {
    isSaving.value = false
  }
}

// 기존 함수들 유지...
const fetchPlan = async (id) => {
  try {
    const response = await api.get(`/api/v1/plan/${id}`)
    const plan = response.data

    title.value = plan?.title || `${plan?.duration}박 ${plan?.duration + 1}일 여행 계획`
    description.value = plan?.description || ''
    destination.value = parseInt(plan?.destination) || null
    currentVersion.value = plan?.version || 1

    const itineraryRes = await api.get(`/api/v1/plan/itinerary/${id}`)
    const rawItinerary = itineraryRes.data

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

    const groupedList = Object.values(grouped).sort((a, b) => a.dayNumber - b.dayNumber)
    itinerary.value = groupedList
    aiPlanData.value = plan.aiSuggestions || null
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
    await updatePlanWithVersion()
  } else {
    console.log('Create plan logic needed')
  }
}

const handleSave = async () => {
  if (planId.value) {
    openImageModal()
  } else {
    openImageModal()
  }
}

onMounted(() => {
  if (planId.value) {
    fetchPlan(planId.value)
  }
})
</script>
