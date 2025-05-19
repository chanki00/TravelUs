<template>
  <div class="min-h-screen bg-gradient-to-b from-blue-50 to-white">

    <div class="py-12 px-6 max-w-5xl mx-auto">
      <h1 class="text-3xl font-bold text-center mb-2">여행 계획 방식 선택</h1>
      <p class="text-gray-600 text-center mb-12">원하시는 여행 플래너 방식을 선택해주세요</p>

      <div class="grid grid-cols-1 md:grid-cols-2 gap-8">
        <div class="transition-all hover:shadow-lg border-2 hover:border-blue-200 rounded-lg overflow-hidden">
          <div class="p-6">
            <div class="flex items-center gap-2 text-xl font-semibold mb-2">
              <edit-icon class="h-5 w-5 text-blue-500" />
              직접 계획하기
            </div>
            <p class="text-gray-500 mb-4">지도를 보며 직접 장소를 선택하고 일정을 구성합니다</p>
            
            <div class="h-40 bg-gray-100 rounded-md flex items-center justify-center mb-6">
              <div class="text-center">
                <map-pin-icon class="h-8 w-8 text-gray-400 mx-auto mb-2" />
                <p class="text-gray-500">지도에서 원하는 장소를 선택하고</p>
                <p class="text-gray-500">나만의 여행 일정을 만들어보세요</p>
              </div>
            </div>
            <RouterLink to="/humanplan">
                <button 
              class="w-full py-2 px-4 bg-blue-600 hover:bg-blue-700 text-white rounded-md"
              @click="goToTravelPlanner"
            >
              직접 플래너 시작하기
            </button>

            </RouterLink>
            
          </div>
        </div>

        <div class="transition-all hover:shadow-lg border-2 hover:border-blue-200 rounded-lg overflow-hidden">
          <div class="p-6">
            <div class="flex items-center gap-2 text-xl font-semibold mb-2">
              <users-icon class="h-5 w-5 text-blue-500" />
              AI 추천 받기
            </div>
            <p class="text-gray-500 mb-4">질문에 답하고 AI가 여행 일정을 추천해드립니다</p>
            
            <div class="h-40 bg-gray-100 rounded-md flex items-center justify-center mb-6">
              <div class="text-center">
                <users-icon class="h-8 w-8 text-gray-400 mx-auto mb-2" />
                <p class="text-gray-500">몇 가지 질문에 답하면</p>
                <p class="text-gray-500">AI가 맞춤형 일정을 제안해드립니다</p>
              </div>
            </div>
            <RouterLink to="/aiplan"> <button 
              class="w-full py-2 px-4 bg-blue-600 hover:bg-blue-700 text-white rounded-md"
              @click="goToAiPlanner"
            >
              AI 플래너 시작하기
            </button> </RouterLink>
           
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import {RouterLink} from "vue-router"
import { Edit as EditIcon, MapPin as MapPinIcon, Users as UsersIcon } from 'lucide-vue-next'

const goToTravelPlanner = () => {
  // 라우팅 로직 추가
}

const goToAiPlanner = () => {
  // 라우팅 로직 추가
}
</script>

<style scoped>
/* 추가 스타일이 필요한 경우 여기에 작성 */
</style>