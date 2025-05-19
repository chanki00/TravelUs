<template>
  <div class="min-h-screen bg-gradient-to-b from-blue-50 to-white">
    <navbar />

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

      <question-card
        :question="currentQuestionData.question"
        :options="currentQuestionData.options"
        :allow-multiple="currentQuestionData.allowMultiple"
        @select="handleSelectAnswer(currentQuestionData.id, $event)"
      />
    </div>
  </div>
</template>

<script setup>
// 여기에 script 로직이 추가될 예정
import { ref, computed } from 'vue';
import { useRouter } from 'vue-router';
import Navbar from '@/components/common/Navbar.vue';
import QuestionCard from '@/components/ui/QuestionCard.vue';

const router = useRouter();
const currentQuestion = ref(0);
const answers = ref({});

const questions = []; // 질문 데이터

const currentQuestionData = computed(() => questions[currentQuestion.value]);

function handleSelectAnswer(questionId, selectedOptions) {}
</script>

<style scoped>
/* 필요한 스타일 추가 */
</style>