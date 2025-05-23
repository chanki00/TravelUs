<template>
  <div class="min-h-screen bg-gradient-to-b from-blue-50 to-white">

    <div class="py-12 px-6 max-w-4xl mx-auto">
      <h1 class="text-3xl font-bold text-center mb-2">AI 여행 플래너</h1>
      <p class="text-gray-600 text-center mb-12">아래 질문에 답하고 AI가 맞춤형 여행 계획을 생성해드립니다</p>

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
                  : 'bg-gray-100 text-gray-400'
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
</template>

<script setup>
import { ref, computed } from 'vue'
import QuestionCard from '@/components/ui/QuestionCard.vue'

const currentQuestion = ref(0)
const answers = ref({})

const questions = [
  {
    id: "destination",
    question: "어디로 여행을 계획하고 계신가요?",
    options: [
      { id: "seoul", label: "서울" },
      { id: "jeju", label: "제주도" },
      { id: "busan", label: "부산" },
      { id: "gangwon", label: "강원도" },
      { id: "gyeongju", label: "경주" },
      { id: "jeonju", label: "전주" },
    ],
  },
  {
    id: "duration",
    question: "여행 기간은 얼마나 되나요?",
    options: [
      { id: "day-trip", label: "당일치기" },
      { id: "1-night", label: "1박 2일" },
      { id: "2-nights", label: "2박 3일" },
      { id: "3-nights", label: "3박 4일" },
      { id: "4-nights", label: "4박 5일" },
      { id: "week", label: "일주일 이상" },
    ],
  },
  {
    id: "style",
    question: "어떤 스타일의 여행을 원하시나요?",
    options: [
      { id: "relaxing", label: "휴양/힐링" },
      { id: "cultural", label: "문화/역사" },
      { id: "food", label: "맛집 탐방" },
      { id: "nature", label: "자연/풍경" },
      { id: "activity", label: "액티비티" },
      { id: "shopping", label: "쇼핑" },
    ],
    allowMultiple: true,
  },
  {
    id: "companions",
    question: "누구와 함께 여행하시나요?",
    options: [
      { id: "alone", label: "혼자" },
      { id: "friend", label: "친구" },
      { id: "couple", label: "연인" },
      { id: "family", label: "가족" },
      { id: "parent", label: "부모님" },
      { id: "children", label: "아이들" },
    ],
  },
  {
    id: "budget",
    question: "예산은 어느 정도로 생각하시나요?",
    options: [
      { id: "budget", label: "알뜰하게" },
      { id: "moderate", label: "적당히" },
      { id: "luxury", label: "럭셔리하게" },
    ],
  },
]

const currentQuestionData = computed(() => questions[currentQuestion.value])

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
    // Navigate to results page when all questions are answered
    navigateToResults()
  }
}

const navigateToResults = () => {
  // 결과 페이지로 이동하는 로직 추가
}
</script>

<style scoped>
/* 추가 스타일이 필요한 경우 여기에 작성 */
</style>