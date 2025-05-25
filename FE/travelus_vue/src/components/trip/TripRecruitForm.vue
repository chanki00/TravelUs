<template>
  <div>
    <!-- 여행 계획 선택 섹션 -->
    <div v-if="!showForm" class="bg-white rounded-lg border shadow-sm overflow-hidden mb-6">
      <div class="p-4 border-b flex justify-between items-center">
        <h3 class="text-lg font-medium">내 여행 계획 목록</h3>
        <button
          class="px-3 py-1.5 text-sm border rounded-md hover:bg-gray-50"
          @click="goToCreatePlan"
        >
          새 모집글 작성하기
        </button>
      </div>
      <div class="p-6">
        <div v-if="isLoading" class="text-center py-8">
          <div
            class="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-500 mx-auto mb-4"
          ></div>
          <p class="text-gray-500">여행 계획을 불러오는 중...</p>
        </div>

        <div v-else-if="myTravelPlans.length === 0" class="text-center py-8 text-gray-500">
          <calendar-icon class="h-12 w-12 mx-auto mb-3 text-gray-400" />
          <p>저장된 여행 계획이 없습니다.</p>
          <button
            class="mt-4 px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700"
            @click="goToCreatePlan"
          >
            새 모집글 작성하기
          </button>
        </div>

        <div v-else class="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div
            v-for="plan in myTravelPlans"
            :key="plan.id"
            class="border rounded-lg overflow-hidden cursor-pointer transition-all duration-200"
            :class="
              selectedPlan?.id === plan.id ? 'ring-2 ring-blue-500 shadow-md' : 'hover:shadow-md'
            "
            @click="selectPlan(plan)"
          >
            <div class="h-40 overflow-hidden relative">
              <img
                :src="
                  plan.image ||
                  'https://images.unsplash.com/photo-1476514525535-07fb3b4ae5f1?q=80&w=1000&auto=format&fit=crop'
                "
                :alt="plan.title"
                class="w-full h-full object-cover"
              />
              <div class="absolute top-3 left-3">
                <span
                  class="px-2 py-1 bg-white/70 backdrop-blur-sm text-gray-800 text-xs rounded-full"
                >
                  {{ getSidoName(Number(plan.destination)) }}
                </span>
              </div>

              <!-- Selection indicator -->
              <div v-if="selectedPlan?.id === plan.id" class="absolute top-3 right-3">
                <div class="w-6 h-6 bg-blue-600 rounded-full flex items-center justify-center">
                  <svg
                    xmlns="http://www.w3.org/2000/svg"
                    class="h-4 w-4 text-white"
                    viewBox="0 0 20 20"
                    fill="currentColor"
                  >
                    <path
                      fill-rule="evenodd"
                      d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z"
                      clip-rule="evenodd"
                    />
                  </svg>
                </div>
              </div>
            </div>

            <div class="p-3">
              <h3 class="font-medium text-base mb-1">{{ plan.title }}</h3>

              <div class="flex flex-wrap gap-1 mb-2">
                <span
                  v-for="(tag, index) in plan.tags"
                  :key="index"
                  class="px-2 py-0.5 bg-blue-50 text-blue-600 text-xs rounded-full"
                >
                  {{ tag }}
                </span>
              </div>

              <div class="flex items-center justify-between text-xs text-gray-500">
                <span>{{ plan.created ? formatDate(plan.created) : '날짜 정보 없음' }}</span>
                <span>{{ plan.duration }}일 일정</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 모집글 작성 폼 -->
    <div
      v-if="selectedPlan || showForm"
      class="bg-white rounded-lg border shadow-sm overflow-hidden"
    >
      <div class="p-4 border-b flex justify-between items-center">
        <h3 class="text-lg font-medium">여행 인원 모집하기</h3>
        <div v-if="selectedPlan" class="flex items-center">
          <span class="text-sm text-gray-500 mr-2">선택된 계획: {{ selectedPlan.title }}</span>
          <button class="text-sm text-blue-600 hover:text-blue-800" @click="resetSelection">
            변경
          </button>
        </div>
      </div>
      <div class="p-6 space-y-4">
        <div class="space-y-2">
          <label for="title" class="block text-sm font-medium text-gray-700">모집글 제목</label>
          <input
            id="title"
            v-model="recruitForm.title"
            placeholder="모집글 제목을 입력하세요"
            class="w-full px-3 py-2 border rounded-md"
          />
        </div>

        <div class="space-y-2">
          <label for="location" class="block text-sm font-medium text-gray-700">여행지</label>
          <select
            id="location"
            v-model="recruitForm.location"
            class="w-full px-3 py-2 border rounded-md"
            :disabled="!!selectedPlan"
          >
            <option disabled value="">시/도를 선택하세요</option>
            <option v-for="sido in sidos" :key="sido.sidoCode" :value="sido.sidoCode">
              {{ sido.sidoName }}
            </option>
          </select>
        </div>

        <div class="space-y-2">
          <label class="block text-sm font-medium text-gray-700">여행 기간</label>
          <div class="flex gap-2 items-center">
            <div class="relative flex-1">
              <input
                type="date"
                v-model="recruitForm.startDate"
                class="w-full px-3 py-2 border rounded-md"
                @change="updateEndDate"
              />
            </div>
            <span class="mx-2">~</span>
            <div class="relative flex-1">
              <input
                type="date"
                v-model="recruitForm.endDate"
                class="w-full px-3 py-2 border rounded-md bg-gray-100"
                readonly
              />
            </div>
          </div>
          <div class="text-xs text-gray-500 mt-1">
            * 시작일을 선택하면 {{ selectedPlan ? selectedPlan.duration : '여행' }}일 일정에 맞게
            종료일이 자동으로 계산됩니다.
          </div>
        </div>

        <div class="space-y-2">
          <label for="maxMembers" class="block text-sm font-medium text-gray-700">모집 인원</label>
          <input
            id="maxMembers"
            type="number"
            min="1"
            max="10"
            v-model="recruitForm.maxMembers"
            placeholder="최대 인원수"
            class="w-full px-3 py-2 border rounded-md"
          />
        </div>

        <div class="space-y-2">
          <label class="block text-sm font-medium text-gray-700"
            >선호하는 성격 태그 (최대 5개)</label
          >
          <div class="flex flex-wrap gap-2">
            <span
              v-for="tag in personalityTags"
              :key="tag"
              :class="[
                'px-2 py-1 text-xs rounded-full cursor-pointer',
                recruitForm.personalityTags.includes(tag)
                  ? 'bg-green-600 text-white'
                  : 'bg-gray-100 text-gray-700 hover:bg-gray-200',
              ]"
              @click="togglePersonalityTag(tag)"
            >
              <check-icon
                v-if="recruitForm.personalityTags.includes(tag)"
                class="inline-block h-3 w-3 mr-1"
              />
              {{ tag }}
            </span>
          </div>
        </div>

        <div class="space-y-2">
          <label for="description" class="block text-sm font-medium text-gray-700"
            >여행 소개 및 동행자 요구사항</label
          >
          <textarea
            id="description"
            v-model="recruitForm.content"
            placeholder="여행 계획과 함께하고 싶은 동행자에 대한 설명을 작성해주세요"
            rows="5"
            class="w-full px-3 py-2 border rounded-md"
          ></textarea>
        </div>

        <div v-if="!selectedPlan" class="space-y-2">
          <label for="plan" class="block text-sm font-medium text-gray-700">여행 계획</label>
          <div class="p-4 border rounded-md text-sm text-gray-500">
            <p class="mb-2">저장된 여행 계획을 추가하거나 새롭게 작성할 수 있습니다</p>
            <div class="flex gap-2">
              <button
                class="px-3 py-1.5 text-sm border rounded-md hover:bg-gray-50"
                @click="resetSelection"
              >
                저장된 계획 불러오기
              </button>
              <button
                class="px-3 py-1.5 text-sm border rounded-md hover:bg-gray-50"
                @click="goToCreatePlan"
              >
                새 계획 만들기
              </button>
            </div>
          </div>
        </div>
      </div>
      <div class="p-4 border-t">
        <button
          class="w-full px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700"
          @click="submitForm"
          :disabled="isSubmitting"
        >
          {{ isSubmitting ? '등록 중...' : '모집 등록하기' }}
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/store/user'
import api from '@/api'
import { Check as CheckIcon, MapPin as MapPinIcon, Calendar as CalendarIcon } from 'lucide-vue-next'
import { userAi } from '@/axios'

const router = useRouter()
const userStore = useUserStore()
const user = computed(() => userStore.loginUser)

// 상태 관리
const selectedPlan = ref(null)
const showForm = ref(false)
const isLoading = ref(false)
const isSubmitting = ref(false)
const myTravelPlans = ref([])
const sidos = ref([])

// 모집글 작성 폼
const recruitForm = ref({
  title: '',
  location: '',
  startDate: '',
  endDate: '',
  maxMembers: 4,
  personalityTags: [], // 성격 태그 추가
  content: '',
})

// 성격 태그 목록
const personalityTags = [
  '활발한',
  '조용한',
  '계획적인',
  '즉흥적인',
  '사교적인',
  '내향적인',
  '모험적인',
  '안전한',
  '유머러스한',
  '진지한',
  '친화적인',
  '독립적인',
]

// 시도 목록 가져오기
const fetchSidos = async () => {
  try {
    const response = await api.get('/api/v1/sidos')
    sidos.value = response.data
  } catch (error) {
    console.error('시도 목록 조회 실패:', error)
  }
}

// 시도 코드로 시도 이름 가져오기
const getSidoName = (sidoCode) => {
  if (!sidoCode || !sidos.value.length) return '지역 정보 없음'

  const sido = sidos.value.find((sido) => sido.sidoCode === sidoCode)
  return sido ? sido.sidoName : '지역 정보 없음'
}

// 여행 계획 페이지로 이동
const goToCreatePlan = () => {
  router.push('/plan')
}

// 여행 계획 가져오기
const fetchTravelPlans = async () => {
  try {
    isLoading.value = true

    // 사용자의 여행 계획 가져오기
    const response = await api.get(`/api/v1/plan/user/${user.value.id}`)
    console.log(response)
    const trips = response.data

    // 각 여행 계획에 태그를 병렬로 가져와서 추가
    const enrichedTrips = await Promise.all(
      trips.map(async (tripPlan) => {
        const tagRes = await api.get(`/api/v1/tag/plan/${tripPlan.id}`)
        return {
          ...tripPlan, // 기존 tripPlan의 모든 속성 유지
          tags: tagRes.data, // 새로 추가된 tags 필드
        }
      }),
    )

    myTravelPlans.value = enrichedTrips
  } catch (error) {
    console.error('여행 계획을 가져오는 중 오류 발생:', error)
  } finally {
    isLoading.value = false
  }
}

// 여행 계획 선택 함수
const selectPlan = (plan) => {
  selectedPlan.value = plan
  showForm.value = true
  // 선택한 계획에서 여행지를 가져옴 (성격 태그는 초기화)
  recruitForm.value = {
    title: '',
    location: plan.destination,
    startDate: '',
    endDate: '',
    maxMembers: 4,
    personalityTags: [], // 성격 태그는 초기화
    content: '',
  }
}

// 시작일 변경 시 종료일 자동 계산
const updateEndDate = () => {
  if (!recruitForm.value.startDate) return

  const startDate = new Date(recruitForm.value.startDate)
  if (isNaN(startDate.getTime())) return

  // 선택된 계획이 있으면 해당 duration을 사용, 없으면 기본값 1 사용
  const duration = selectedPlan.value ? selectedPlan.value.duration - 1 : 0

  // 시작일로부터 duration일 후의 날짜 계산
  const endDate = new Date(startDate)
  endDate.setDate(startDate.getDate() + duration)

  // YYYY-MM-DD 형식으로 변환
  const year = endDate.getFullYear()
  const month = String(endDate.getMonth() + 1).padStart(2, '0')
  const day = String(endDate.getDate()).padStart(2, '0')

  recruitForm.value.endDate = `${year}-${month}-${day}`
}

// 선택 초기화 함수
const resetSelection = () => {
  selectedPlan.value = null
  showForm.value = false

  // 폼 초기화
  recruitForm.value = {
    title: '',
    location: '',
    startDate: '',
    endDate: '',
    maxMembers: 4,
    personalityTags: [],
    content: '',
  }
}

// 새 폼 보여주기
const showNewForm = () => {
  showForm.value = true
}

// 성격 태그 토글 함수
const togglePersonalityTag = (tag) => {
  if (recruitForm.value.personalityTags.includes(tag)) {
    recruitForm.value.personalityTags = recruitForm.value.personalityTags.filter((t) => t !== tag)
  } else {
    if (recruitForm.value.personalityTags.length < 5) {
      recruitForm.value.personalityTags.push(tag)
    }
  }
}

// 폼 제출 함수
const submitForm = async () => {
  if (!validateForm()) return
  console.log(selectedPlan.value)
  try {
    isSubmitting.value = true
    const res = await userAi.post('/api/v1/chat') // 🔹 채팅방 생성
    const newchatroomId = res.data
    console.log(newchatroomId)
    const postData = {
      planId: selectedPlan.value ? selectedPlan.value.id : null,
      userId: user.value.id,
      startDate: recruitForm.value.startDate,
      endDate: recruitForm.value.endDate,
      title: recruitForm.value.title,
      content: recruitForm.value.content,
      currentMembers: recruitForm.value.maxMembers,
      personalityTags: JSON.stringify(recruitForm.value.personalityTags), // JSON 문자열로 변환
      chatroomId: newchatroomId,
    }
    console.log(postData)
    // API 호출하여 모집글 저장
    const response = await api.post('/api/v1/post/recruit', postData)

    console.log('모집글이 성공적으로 등록되었습니다:', response.data)

    // 성공 후 처리 (예: 알림 표시, 페이지 이동 등)
    alert('모집글이 성공적으로 등록되었습니다.')

    // 폼 초기화
    resetSelection()

    // 모집글 목록 페이지로 이동 (필요시 경로 수정)
    router.push('/recruit')
  } catch (error) {
    console.error('모집글 등록 중 오류 발생:', error)
    alert('모집글 등록에 실패했습니다. 다시 시도해주세요.')
  } finally {
    isSubmitting.value = false
  }
}

// 폼 유효성 검사
const validateForm = () => {
  if (!recruitForm.value.title.trim()) {
    alert('모집글 제목을 입력해주세요.')
    return false
  }

  if (!recruitForm.value.location) {
    alert('여행지를 선택해주세요.')
    return false
  }

  if (!recruitForm.value.startDate) {
    alert('여행 시작일을 선택해주세요.')
    return false
  }

  if (!recruitForm.value.content.trim()) {
    alert('여행 소개 및 동행자 요구사항을 입력해주세요.')
    return false
  }

  if (!selectedPlan.value && !confirm('여행 계획을 선택하지 않았습니다. 계속 진행하시겠습니까?')) {
    return false
  }

  return true
}

// 날짜 포맷팅
const formatDate = (dateString) => {
  if (!dateString) return '날짜 정보 없음'

  try {
    const date = new Date(dateString)
    if (isNaN(date.getTime())) return '날짜 형식 오류'

    return `${date.getFullYear()}.${String(date.getMonth() + 1).padStart(2, '0')}.${String(date.getDate()).padStart(2, '0')}`
  } catch (error) {
    console.error('날짜 포맷팅 오류:', error)
    return '날짜 형식 오류'
  }
}

// 컴포넌트 마운트 시 여행 계획과 시도 목록 가져오기
onMounted(() => {
  fetchTravelPlans()
  fetchSidos()
})
</script>
