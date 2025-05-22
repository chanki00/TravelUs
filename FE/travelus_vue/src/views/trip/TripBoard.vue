<template>
  <div class="min-h-screen bg-gradient-to-b from-blue-50 to-white">
    <div class="py-12 px-6 max-w-7xl mx-auto">
      <div class="flex flex-col md:flex-row justify-between items-start md:items-center mb-8">
        <div>
          <h1 class="text-3xl font-bold mb-2">여행 계획 공유 게시판</h1>
          <p class="text-gray-600">다른 여행자들의 일정을 확인하고 참고해 보세요</p>
        </div>

        <button 
          class="mt-4 md:mt-0 px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700"
          @click="openUploadModal"
        >
          내 여행 계획 업로드
        </button>
      </div>

      <div class="mb-8">
        <div class="flex flex-col sm:flex-row justify-between gap-4">
          <div class="border-b w-full sm:w-auto">
            <div class="flex">
              <button 
                v-for="tab in tabs" 
                :key="tab.value"
                :class="[
                  'px-4 py-2',
                  activeFilter === tab.value ? 'border-b-2 border-blue-600 text-blue-600' : 'text-gray-500'
                ]"
                @click="activeFilter = tab.value"
              >
                {{ tab.label }}
              </button>
            </div>
          </div>

          <div class="flex">
            <input
              v-model="searchTerm"
              placeholder="여행 계획 검색하기"
              class="px-3 py-2 border rounded-l-md focus:outline-none focus:ring-1 focus:ring-blue-500"
            />
            <button class="px-4 py-2 bg-blue-600 text-white rounded-r-md hover:bg-blue-700">
              검색
            </button>
          </div>
        </div>
      </div>

      <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        <router-link 
          v-for="trip in trips" 
          :key="trip.id"
          :to="`/tripdetail/${trip.id}`"
          class="block"
        >
          <div class="bg-white rounded-lg border shadow-sm overflow-hidden hover:shadow-lg transition-shadow h-full">
            <div class="h-48 overflow-hidden relative">
              <img
                :src="trip.image"
                :alt="trip.title"
                class="w-full h-full object-cover hover:scale-105 transition-transform duration-300"
              />
              <div class="absolute top-3 left-3">
                <span class="px-2 py-1 bg-white/70 backdrop-blur-sm text-gray-800 text-xs rounded-full">
                  {{ trip.location }}
                </span>
              </div>
            </div>
            <div class="p-4">
              <h3 class="font-medium text-lg mb-2">{{ trip.title }}</h3>

              <div class="flex flex-wrap gap-1 mb-3">
                <span 
                  v-for="(tag, index) in trip.tags" 
                  :key="index"
                  class="px-2 py-1 bg-blue-50 text-blue-600 text-xs rounded-full"
                >
                  {{ tag }}
                </span>
              </div>

              <div class="flex items-center justify-between">
                <div class="flex items-center gap-2">
                  <div class="w-6 h-6 rounded-full bg-gray-200 flex items-center justify-center">
                    <span class="text-xs">{{ trip.user }}</span>
                  </div>
                  <span class="text-xs text-gray-600">{{ trip.author }}</span>
                </div>

                <div class="flex items-center gap-3 text-xs text-gray-500">
                  <span>♥ {{ trip.likes }}</span>
                  <span>💬 {{ trip.shares }}</span>
                </div>
              </div>
            </div>
          </div>
        </router-link>
      </div>

      <div class="mt-8 flex justify-center">
        <button class="px-4 py-2 border rounded-md hover:bg-gray-50">
          더 보기
        </button>
      </div>
    </div>

    <!-- 여행 계획 업로드 모달 -->
    <TripUploadModal 
      :is-open="isUploadModalOpen" 
      @close="closeUploadModal"
      @apply="handleApplyTrip"
    />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import TripUploadModal from '@/components/trip/TripUploadModal.vue'
import api from '@/api'


const activeFilter = ref('all')
const searchTerm = ref('')
const isUploadModalOpen = ref(false)

const tabs = [
  { value: 'all', label: '전체' },
  { value: 'popular', label: '인기순' },
  { value: 'recent', label: '최신순' },
  { value: 'recommended', label: '추천순' }
]

const trips = ref([])

const fetchTrips = async () => {
  const response = await api.get(`/api/v1/plan/share`)
  const trip = response.data

  const enrichedTrips = await Promise.all(
    trip.map(async (tripPlan) => {
      const tagRes = await api.get(`/api/v1/tag/plan/${tripPlan.id}`)
      return {
        ...tripPlan,
        tags: tagRes.data
      }
    })
  )

  trips.value = enrichedTrips
}



onMounted(async () => {
    fetchTrips()
})

// 모달 열기
const openUploadModal = () => {
  isUploadModalOpen.value = true
}

// 모달 닫기
const closeUploadModal = () => {
  isUploadModalOpen.value = false
}

// 선택한 여행 계획 적용하기
const handleApplyTrip = async (selectedTrip) => {
  console.log('선택한 여행 계획:', selectedTrip)
  
  // 여기서 선택한 여행 계획을 게시판에 업로드하는 로직을 구현
  // 예: API 호출 또는 상태 업데이트
  const response = await api.patch(`/api/v1/plan/updateShare/${selectedTrip.id}`)
  // 예시: 선택한 여행 계획을 게시판 목록에 추가
  // const newTrip = {
  //   id: `new-${Date.now()}`,
  //   title: selectedTrip.title,
  //   location: selectedTrip.location,
  //   author: "현재사용자", // 실제로는 로그인한 사용자 정보를 사용
  //   image: selectedTrip.image,
  //   tags: selectedTrip.tags,
  //   likes: 0,
  //   comments: 0,
  //   created: new Date().toISOString().split('T')[0]
  // }
  
  // // 새 여행 계획을 목록 맨 앞에 추가
  // trips.value.unshift(newTrip)

   await fetchTrips()
  // 성공 메시지 표시 (실제 구현에서는 토스트 메시지 등으로 구현)
  alert('여행 계획이 성공적으로 업로드되었습니다!')
  

}
</script>
