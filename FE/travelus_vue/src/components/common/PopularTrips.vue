<template>
  <section class="py-16 px-6 md:px-12">
    <div class="max-w-7xl mx-auto">
      <h2 class="text-2xl md:text-3xl font-bold mb-2">인기 여행 일정</h2>
      <p class="text-gray-500 mb-8">좋아요가 많은 인기 여행 일정을 둘러보세요</p>

      <div v-if="isLoading" class="text-center py-8">
        <div
          class="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-500 mx-auto mb-4"
        ></div>
        <p class="text-gray-500">인기 여행 일정을 불러오는 중...</p>
      </div>

      <div v-else-if="popularTrips.length === 0" class="text-center py-8">
        <p class="text-gray-500">아직 공유된 여행 계획이 없습니다.</p>
      </div>

      <div v-else class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
        <div
          v-for="trip in popularTrips"
          :key="trip.id"
          class="animate-fade-in cursor-pointer"
          :style="{ animationDelay: `${parseInt(trip.id) * 150}ms` }"
          @click="goToTripDetail(trip.id)"
        >
          <div
            class="bg-white rounded-lg border shadow-sm overflow-hidden hover:shadow-lg transition-shadow h-full"
          >
            <div class="h-48 overflow-hidden relative">
              <img
                :src="
                  trip.image ||
                  'https://images.unsplash.com/photo-1476514525535-07fb3b4ae5f1?q=80&w=1000&auto=format&fit=crop'
                "
                :alt="trip.title"
                class="w-full h-full object-cover hover:scale-105 transition-transform duration-300"
              />
              <div class="absolute top-3 left-3">
                <span
                  class="px-2 py-1 bg-white/70 backdrop-blur-sm text-gray-800 text-xs rounded-full"
                >
                  {{ getSidoName(Number(trip.destination)) }}
                </span>
              </div>
              <div class="absolute top-3 right-3">
                <span
                  class="px-2 py-1 bg-red-500 text-white text-xs rounded-full flex items-center gap-1"
                >
                  <heart-icon class="h-3 w-3 fill-current" />
                  {{ trip.likes }}
                </span>
              </div>
            </div>
            <div class="p-4">
              <h3 class="font-medium text-lg mb-2">{{ trip.title }}</h3>

              <!-- 수정: duration + 1일로 표시 -->
              <p class="text-sm text-gray-500 mb-3">
                {{ trip.duration }}박 {{ trip.duration + 1 }}일
              </p>

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
                    <span class="text-xs">{{
                      trip.authorName ? trip.authorName.charAt(0) : 'U'
                    }}</span>
                  </div>
                  <span class="text-xs text-gray-600">{{ trip.authorName || 'Unknown' }}</span>
                </div>

                <div class="flex items-center gap-3 text-xs text-gray-500">
                  <span class="flex items-center gap-1">
                    🧡
                    {{ trip.likes }}
                  </span>
                  <span class="flex items-center gap-1">
                    <share-icon class="h-3 w-3" />
                    {{ trip.shares }}
                  </span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div v-if="popularTrips.length > 0" class="text-center mt-8">
        <router-link
          to="/share"
          class="inline-flex items-center px-6 py-3 bg-blue-600 text-white rounded-md hover:bg-blue-700 transition-colors"
        >
          더 많은 여행 계획 보기
          <arrow-right-icon class="ml-2 h-4 w-4" />
        </router-link>
      </div>
    </div>
  </section>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import {
  Heart as HeartIcon,
  Share as ShareIcon,
  ArrowRight as ArrowRightIcon,
} from 'lucide-vue-next'
import api from '@/api'

const router = useRouter()
const popularTrips = ref([])
const isLoading = ref(false)
const sidos = ref([])

// 인기 여행 계획 가져오기
const fetchPopularTrips = async () => {
  try {
    isLoading.value = true

    // 인기 여행 계획 조회 (좋아요 순) - 4개로 변경
    const response = await api.get('/api/v1/likes/popular?limit=4')
    let trips = response.data

    // 각 여행 계획에 태그 정보와 작성자 정보 추가
    trips = await Promise.all(
      trips.map(async (trip) => {
        try {
          // 태그 정보 가져오기
          const tagRes = await api.get(`/api/v1/tag/plan/${trip.id}`)

          // 작성자 정보 가져오기
          const authorRes = await api.get(`/api/v1/plan/user-info/${trip.userId}`)

          return {
            ...trip,
            tags: tagRes.data || [],
            authorName: authorRes.data?.name || 'Unknown',
            authorUserId: authorRes.data?.userId || 'unknown',
          }
        } catch (error) {
          console.error('추가 정보 가져오기 실패:', error)
          return {
            ...trip,
            tags: [],
            authorName: 'Unknown',
            authorUserId: 'unknown',
          }
        }
      }),
    )

    popularTrips.value = trips
  } catch (error) {
    console.error('인기 여행 계획 조회 실패:', error)
    popularTrips.value = []
  } finally {
    isLoading.value = false
  }
}

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

// 여행 상세 페이지로 이동
const goToTripDetail = (tripId) => {
  router.push(`/tripdetail/${tripId}`)
}

onMounted(() => {
  fetchPopularTrips()
  fetchSidos()
})
</script>

<style scoped>
@keyframes fade-in {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.animate-fade-in {
  animation: fade-in 0.6s ease-out forwards;
  opacity: 0;
}
</style>
