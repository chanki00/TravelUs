<template>
  <div class="min-h-screen bg-gradient-to-b from-blue-50 to-white">
    <div class="py-12 px-6 max-w-4xl mx-auto">
      <h1 class="text-3xl font-bold text-center mb-2">AI 여행 플래너</h1>
      <p class="text-gray-600 text-center mb-12">
        아래 질문에 답하고 AI가 맞춤형 여행 계획을 생성해드립니다
      </p>

      <!-- Loading indicator -->
      <div v-if="isLoading" class="flex flex-col items-center justify-center py-12">
        <div class="animate-spin rounded-full h-16 w-16 border-b-2 border-blue-600 mb-4"></div>
        <p class="text-gray-600">AI가 여행 계획을 생성하고 있습니다...</p>
        <p class="text-gray-500 text-sm mt-2">잠시만 기다려주세요</p>
      </div>

      <div v-else>
        <div class="mb-10">
          <div class="flex justify-between items-center mb-4">
            <div
              v-for="(_, index) in questions"
              :key="index"
              :class="[
                'w-10 h-10 rounded-full flex items-center justify-center',
                index < currentQuestion
                  ? 'bg-blue-600 text-white'
                  : index === currentQuestion
                    ? 'bg-blue-100 border-2 border-blue-600 text-blue-600'
                    : 'bg-gray-100 text-gray-400',
              ]"
            >
              {{ index + 1 }}
            </div>
          </div>
          <div class="h-2 bg-gray-100 rounded-full">
            <div
              class="h-2 bg-blue-600 rounded-full transition-all duration-300"
              :style="{ width: `${((currentQuestion + 1) / questions.length) * 100}%` }"
            ></div>
          </div>
        </div>

        <QuestionCard
          :question="currentQuestionData.question"
          :options="currentQuestionData.options"
          :allow-multiple="currentQuestionData.allowMultiple"
          @select="handleSelectAnswer(currentQuestionData.id, $event)"
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import QuestionCard from '@/components/ui/QuestionCard.vue'
import api from '@/api'
import { userAi } from '@/axios'

const router = useRouter()
const questions = [
  {
    id: 'destination',
    question: '어디로 여행을 계획하고 계신가요?',
    options: [
      { id: 'seoul', label: '서울' },
      { id: 'busan', label: '부산' },
      { id: 'daegu', label: '대구' },
      { id: 'incheon', label: '인천' },
      { id: 'gwangju', label: '광주' },
      { id: 'daejeon', label: '대전' },
      { id: 'ulsan', label: '울산' },
      { id: 'sejong', label: '세종' },
      { id: 'gyeonggi', label: '경기도' },
      { id: 'gangwon', label: '강원도' },
      { id: 'chungbuk', label: '충청북도' },
      { id: 'chungnam', label: '충청남도' },
      { id: 'jeonbuk', label: '전라북도' },
      { id: 'jeonnam', label: '전라남도' },
      { id: 'gyeongbuk', label: '경상북도' },
      { id: 'gyeongnam', label: '경상남도' },
      { id: 'jeju', label: '제주도' },
    ],
  },
  {
    id: 'duration',
    question: '여행 기간은 얼마나 되나요?',
    options: [
      { id: '2', label: '1박 2일' },
      { id: '3', label: '2박 3일' },
      { id: '4', label: '3박 4일' },
      { id: '5', label: '4박 5일' },
      { id: '6', label: '5박 6일' },
      { id: '7', label: '일주일 이상' },
    ],
  },
  {
    id: 'style',
    question: '어떤 스타일의 여행을 원하시나요?',
    options: [
      { id: 'relaxing', label: '휴양/힐링' },
      { id: 'cultural', label: '문화/역사' },
      { id: 'food', label: '맛집 탐방' },
      { id: 'nature', label: '자연/풍경' },
      { id: 'activity', label: '액티비티' },
      { id: 'shopping', label: '쇼핑' },
    ],
    allowMultiple: true,
  },
  {
    id: 'companions',
    question: '누구와 함께 여행하시나요?',
    options: [
      { id: 'alone', label: '혼자' },
      { id: 'friend', label: '친구' },
      { id: 'couple', label: '연인' },
      { id: 'family', label: '가족' },
      { id: 'parent', label: '부모님' },
      { id: 'children', label: '아이들' },
    ],
  },
  {
    id: 'budget',
    question: '예산은 어느 정도로 생각하시나요?',
    options: [
      { id: 'budget', label: '알뜰하게' },
      { id: 'moderate', label: '적당히' },
      { id: 'luxury', label: '럭셔리하게' },
    ],
  },
]

const currentQuestion = ref(0)
const answers = ref({})
const isLoading = ref(false)
const currentQuestionData = computed(() => questions[currentQuestion.value])

// 선택한 답변 처리
const handleSelectAnswer = (questionId, selectedOptions) => {
  // Update answers
  answers.value = {
    ...answers.value,
    [questionId]: selectedOptions,
  }

  // Move to next question or finish
  if (currentQuestion.value < questions.length - 1) {
    currentQuestion.value++
  } else {
    // Generate travel plan with AI
    generateTravelPlan()
  }
}

// AI를 통한 여행 계획 생성
const generateTravelPlan = async () => {
  try {
    isLoading.value = true

    // 선택된 옵션 값 추출
    const destination = getOptionLabel('destination')
    const duration = getDurationValue(getOptionLabel('duration'))
    const style = getOptionLabels('style')
    const companions = getOptionLabel('companions')
    const budget = getOptionLabel('budget')

    // Spring 백엔드 API 요청
    const response = await api.post('/ai/trip-plan', {
      destination,
      duration,
      style,
      companions,
      budget,
    })

    if (!response.data || response.data.status === 'ERROR') {
      throw new Error(response.data?.message || 'AI 여행 계획 생성에 실패했습니다')
    }

    // 응답 데이터 처리
    let planData
    let attractionsMap = {}

    if (response.data.plan) {
      // 이미 JSON으로 파싱된 경우
      planData = response.data.plan

      // 관광지 정보 맵이 있으면 저장
      if (response.data.attractions) {
        attractionsMap = response.data.attractions
      }
    } else if (response.data.rawResponse) {
      // 원본 텍스트 응답인 경우 - 프론트엔드에서 파싱 시도
      try {
        const resultStr = response.data.message.toString()
        const jsonStart = resultStr.indexOf('{')
        const jsonEnd = resultStr.lastIndexOf('}') + 1

        if (jsonStart >= 0 && jsonEnd > jsonStart) {
          const jsonStr = resultStr.substring(jsonStart, jsonEnd)
          planData = JSON.parse(jsonStr)
        } else {
          throw new Error('JSON 형식을 찾을 수 없습니다')
        }
      } catch (e) {
        console.error('JSON 파싱 오류:', e)
        planData = {
          title: `${destination} ${duration}박 ${parseInt(duration) + 1}일 여행 계획`,
          description: '여행 계획에 대한 간략한 소개말을 작성해보세요.',
          itinerary: [],
        }
      }
    } else {
      // 기본 응답 처리
      planData = {
        title: `${destination} ${duration}박 ${parseInt(duration) + 1}일 여행 계획`,
        description: response.data.message || '여행 계획에 대한 간략한 소개말을 작성해보세요.',
        itinerary: [],
      }
    }

    // 여행 계획 데이터를 TripPlan.vue로 전달
    const res = await userAi.post('/api/v1/chat') // 🔹 채팅방 생성
    const chatroomId = res.data
    router.push({
      path: '/tripplan',
      query: {
        // 기본 정보
        destination: getDestinationCode(destination),
        duration: duration,
        members: getCompanionsCount(companions),
        transport: 'car',
        // AI 생성 데이터
        aiPlan: JSON.stringify(planData),
        // 관광지 정보 맵 (문자열로 변환)
        attractions: JSON.stringify(attractionsMap),
        chatroomId,
      },
    })
  } catch (error) {
    console.error('여행 계획 생성 오류:', error)
    alert('여행 계획 생성 중 오류가 발생했습니다. 다시 시도해주세요.')
  } finally {
    isLoading.value = false
  }
}

// 선택된 옵션의 라벨 가져오기
const getOptionLabel = (questionId) => {
  const selectedId = answers.value[questionId]?.[0]
  if (!selectedId) return ''

  const question = questions.find((q) => q.id === questionId)
  const option = question.options.find((opt) => opt.id === selectedId)
  return option ? option.label : ''
}

// 다중 선택 옵션의 라벨 배열 가져오기
const getOptionLabels = (questionId) => {
  const selectedIds = answers.value[questionId] || []
  if (selectedIds.length === 0) return []

  const question = questions.find((q) => q.id === questionId)
  return selectedIds.map((id) => {
    const option = question.options.find((opt) => opt.id === id)
    return option ? option.label : ''
  })
}

// 여행지 이름을 코드로 변환
const getDestinationCode = (destination) => {
  const destinationMap = {
    서울: '1',
    제주도: '39',
    부산: '6',
    강원도: '32',
    경상북도: '35',
    전라북도: '37',
  }
  return destinationMap[destination] || '1'
}

// 기간 텍스트를 숫자로 변환
const getDurationValue = (duration) => {
  if (duration.includes('1박')) return 2
  if (duration.includes('2박')) return 3
  if (duration.includes('3박')) return 4
  if (duration.includes('4박')) return 5
  if (duration.includes('5박')) return 6
  if (duration.includes('일주일')) return 7
  return 3 // 기본값
}

// 동행자 유형에 따른 인원수 추정
const getCompanionsCount = (companions) => {
  const companionsMap = {
    혼자: 1,
    친구: 2,
    연인: 2,
    가족: 4,
    부모님: 2,
    아이들: 3,
  }
  return companionsMap[companions] || 2
}
</script>

<style scoped>
/* 추가 스타일이 필요한 경우 여기에 작성 */
</style>
