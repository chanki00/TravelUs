<template>
  <div class="min-h-screen bg-gradient-to-b from-blue-50 to-white">
    <div class="py-8 px-6 max-w-7xl mx-auto">
      <h1 class="text-3xl font-bold mb-2">여행 플래너</h1>
      <p class="text-gray-600 mb-8">나만의 맞춤형 여행 계획을 직접 만들어보세요</p>

      <div class="max-w-md mx-auto">
        <div class="bg-white rounded-lg border shadow-sm overflow-hidden">
          <div class="p-6">
            <h2 class="text-xl font-medium mb-4">여행 정보</h2>

            <div class="space-y-4">
              <div>
                <label for="destination" class="block text-sm font-medium text-gray-700 mb-1"
                  >여행지</label
                >
                <select
                  id="destination"
                  v-model="destination"
                  class="w-full px-3 py-2 border rounded-md"
                >
                  <option disabled value="">시/도를 선택하세요</option>
                  <option v-for="sido in sidos" :key="sido.sidoCode" :value="sido.sidoCode">
                    {{ sido.sidoName }}
                  </option>
                </select>
              </div>

              <div>
                <label for="duration" class="block text-sm font-medium text-gray-700 mb-1"
                  >여행 일수</label
                >
                <select
                  id="duration"
                  v-model="selectedDays"
                  class="w-full px-3 py-2 border rounded-md"
                >
                  <option v-for="day in days" :key="day" :value="day">{{ day }}일</option>
                </select>
              </div>

              <div>
                <label for="travelers" class="block text-sm font-medium text-gray-700 mb-1"
                  >여행 인원</label
                >
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
                <label for="transportation" class="block text-sm font-medium text-gray-700 mb-1"
                  >주요 이동 수단</label
                >
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

              <!-- Changed to navigate to TripPlan with query parameters -->
              <button
                class="w-full mt-4 py-2 px-4 bg-blue-600 hover:bg-blue-700 text-white rounded-md flex items-center justify-center"
                @click="navigateToTripPlan"
              >
                다음 단계로 <arrow-right-icon class="ml-2 h-4 w-4" />
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import api from '@/api'
import { userAi } from '@/axios'

const router = useRouter()
const destination = ref('')
const selectedDays = ref(3)
const travelers = ref(1)
const transportation = ref('car')
const days = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]

const sidos = ref([])

onMounted(async () => {
  try {
    const response = await api.get('/api/v1/sidos')
    sidos.value = response.data
    console.log(sidos.value)
  } catch (error) {
    console.error('시도 목록 조회 실패:', error)
  }
})

// New function to navigate to TripPlan with query parameters
const navigateToTripPlan = async () => {
  // Validate inputs if needed
  if (!destination.value) {
    alert('여행지를 선택해주세요')
    return
  }

  const res = await userAi.post('/api/v1/chat') // 🔹 채팅방 생성
  const chatroomId = res.data // 생성된 채팅방 ID (백엔드에서 int 또는 DTO 형태로 반환되었을 것)
  // Navigate to TripPlan with query parameters
  router.push({
    path: '/tripplan',
    query: {
      destination: destination.value,
      duration: selectedDays.value,
      members: travelers.value,
      transport: transportation.value,
      chatroomId: chatroomId, // 🔹 함께 전달
    },
  })
}
</script>

<style scoped>
/* 추가 스타일이 필요한 경우 여기에 작성 */
</style>
