<template>
  <div class="min-h-screen  bg-gradient-to-b from-blue-50 to-white">

    <div class="py-8 px-6 max-w-7xl mx-auto">
      <h1 class="text-3xl font-bold mb-2">여행 플래너</h1>
      <p class="text-gray-600 mb-8">나만의 맞춤형 여행 계획을 직접 만들어보세요</p>

      <div class="max-w-md mx-auto">
        <div class="bg-white rounded-lg border shadow-sm overflow-hidden">
          <div class="p-6">
            <h2 class="text-xl font-medium mb-4">여행 정보</h2>

            <div class="space-y-4">
              <div>
                <label for="destination" class="block text-sm font-medium text-gray-700 mb-1">여행지</label>
                <input 
                  id="destination" 
                  v-model="destination"
                  placeholder="여행지를 입력하세요" 
                  class="w-full px-3 py-2 border rounded-md"
                />
              </div>

              <!-- <div>
                <label for="startDate" class="block text-sm font-medium text-gray-700 mb-1">출발일</label>
                <input 
                  id="startDate" 
                  type="date" 
                  v-model="startDate"
                  class="w-full px-3 py-2 border rounded-md"
                />
              </div> -->

              <div>
                <label for="duration" class="block text-sm font-medium text-gray-700 mb-1">여행 일수</label>
                <select 
                  id="duration" 
                  v-model="selectedDays"
                  class="w-full px-3 py-2 border rounded-md"
                >
                  <option v-for="day in days" :key="day" :value="day">{{ day }}일</option>
                </select>
              </div>

              <div>
                <label for="travelers" class="block text-sm font-medium text-gray-700 mb-1">여행 인원</label>
                <input 
                  id="travelers" 
                  type="number" 
                  min="1" 
                  v-model="travelers"
                  placeholder="인원수를 입력하세요" 
                  class="w-full px-3 py-2 border rounded-md"
                />
              </div>

              <div>
                <label for="transportation" class="block text-sm font-medium text-gray-700 mb-1">주요 이동 수단</label>
                <select 
                  id="transportation" 
                  v-model="transportation"
                  class="w-full px-3 py-2 border rounded-md"
                >
                  <option value="car">자동차/렌트카</option>
                  <option value="public">대중교통</option>
                  <option value="walk">도보/자전거</option>
                  <option value="tour">투어버스</option>
                </select>
              </div>
              <RouterLink to="/planresult">
                 <button 
                class="w-full mt-4 py-2 px-4 bg-blue-600 hover:bg-blue-700 text-white rounded-md flex items-center justify-center"
                @click="createPlan"
              >
                여행 계획 생성 <arrow-right-icon class="ml-2 h-4 w-4" />
              </button>
              </RouterLink>
             
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
  import { RouterLink} from "vue-router"

const destination = ref('')
const startDate = ref('')
const selectedDays = ref(3)
const travelers = ref(1)
const transportation = ref('car')
const days = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]

const createPlan = () => {
  // 여행 계획 생성 로직 추가
}
</script>

<style scoped>
/* 추가 스타일이 필요한 경우 여기에 작성 */
</style>