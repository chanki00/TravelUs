<template>
  <div class="min-h-screen">
    <div class="relative">
      <div class="h-80 w-full overflow-hidden">
        <img :src="tripDetails.image" :alt="tripDetails.title" class="w-full h-full object-cover" />
        <div class="absolute inset-0 bg-black/30"></div>
      </div>

      <div class="absolute bottom-0 left-0 right-0 p-6 text-white">
        <div class="max-w-7xl mx-auto">
          <div class="flex items-center gap-2 mb-2">
            <span class="px-2 py-1 bg-white/20 backdrop-blur-sm rounded-full text-sm">{{
              tripDetails.location
            }}</span>
            <!-- 수정: duration + 1일로 표시 -->
            <span class="px-2 py-1 bg-white/20 backdrop-blur-sm rounded-full text-sm"
              >{{ tripDetails.duration }}박 {{ tripDetails.duration + 1 }}일</span
            >
          </div>
          <h1 class="text-3xl md:text-4xl font-bold mb-2">{{ tripDetails.title }}</h1>
          <div class="flex items-center gap-3">
            <div class="flex items-center gap-2">
              <div
                class="w-8 h-8 rounded-full bg-white/30 backdrop-blur-sm flex items-center justify-center"
              >
                <span>{{ authorInfo.name ? authorInfo.name.charAt(0) : 'U' }}</span>
              </div>
              <span>{{ authorInfo.name || 'Unknown' }} ({{ authorInfo.userId || 'unknown' }})</span>
            </div>
            <div class="flex items-center gap-3">
              <span>♥ {{ tripDetails.likes }}</span>
              <span>📤 {{ tripDetails.shares }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="py-8 px-6 max-w-7xl mx-auto">
      <div class="grid grid-cols-1 lg:grid-cols-3 gap-8">
        <div class="lg:col-span-2">
          <div class="flex flex-col md:flex-row justify-between items-start md:items-center mb-6">
            <h2 class="text-2xl font-bold mb-2 md:mb-0">여행 일정</h2>

            <div class="flex gap-3">
              <!-- 수정: 좋아요 버튼 추가 -->
              <button
                @click="toggleLike"
                :class="[
                  'px-4 py-2 border rounded-md transition-colors flex items-center gap-2',
                  isLiked
                    ? 'bg-red-50 border-red-300 text-red-600'
                    : 'hover:bg-gray-50 border-gray-300',
                ]"
                :disabled="isLikeLoading"
              >
                <heart-icon :class="['h-4 w-4', isLiked ? 'fill-current text-red-500' : '']" />
                {{ isLiked ? '좋아요 취소' : '좋아요' }} ({{ likeCount }})
              </button>
            </div>
          </div>

          <div class="mb-8">
            <p class="text-gray-700">{{ tripDetails.description }}</p>
          </div>

          <div class="border-b mb-6">
            <div class="flex">
              <button
                v-for="(day, index) in tripDetails.itinerary"
                :key="index"
                :class="[
                  'px-4 py-2 text-center',
                  activeDay === index
                    ? 'border-b-2 border-blue-600 text-blue-600'
                    : 'text-gray-500',
                ]"
                @click="activeDay = index"
              >
                Day {{ index + 1 }}
              </button>
            </div>
          </div>

          <div
            v-for="(day, dayIndex) in tripDetails.itinerary"
            :key="dayIndex"
            v-show="activeDay === dayIndex"
          >
            <div class="bg-white rounded-lg border shadow-sm overflow-hidden">
              <div class="p-4 border-b bg-gray-50">
                <h3 class="text-lg font-medium">Day {{ dayIndex + 1 }}:</h3>
              </div>
              <div class="p-4">
                <div class="space-y-6">
                  <div v-for="(item, itemIndex) in day.items" :key="itemIndex" class="flex gap-4">
                    <div class="w-16 text-sm font-medium text-gray-500">{{ item.time }}</div>
                    <div class="relative">
                      <div
                        class="w-3 h-3 rounded-full bg-blue-500 absolute left-0 top-2.5 -translate-x-[7px]"
                      ></div>
                      <div
                        class="border-l-2 border-gray-200 pl-4 ml-0.5 pb-6 last:border-l-0 last:pb-0"
                      >
                        <h4 class="font-medium">{{ item.title }}</h4>
                        <p class="text-sm text-gray-500 mt-1">{{ item.description }}</p>
                        <span
                          class="mt-2 px-2 py-0.5 bg-blue-50 text-blue-600 text-xs rounded-full inline-block"
                        >
                          {{ item.type }}
                        </span>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <div class="mt-8">
            <h3 class="text-xl font-medium mb-4">지도 보기</h3>
            <div class="h-[400px] border rounded-lg">
              <!-- 높이 지정 -->
              <TripPlannerMap
                v-if="processedItinerary.length > 0"
                :locations="[]"
                :itinerary="processedItinerary"
                :active-day="activeDay"
              />
            </div>
          </div>
        </div>

        <div class="lg:col-span-1">
          <div class="bg-white rounded-lg border shadow-sm sticky top-24">
            <div class="p-4 border-b">
              <h3 class="text-lg font-medium">여행 정보</h3>
            </div>
            <div class="p-4 space-y-4">
              <div>
                <h4 class="text-sm text-gray-500 mb-1">작성자 성격</h4>
                <div class="flex flex-wrap gap-2">
                  <span
                    v-for="(tag, index) in authorPersonalityTags"
                    :key="index"
                    class="px-2 py-1 bg-purple-50 text-purple-600 text-xs rounded-full"
                  >
                    {{ tag }}
                  </span>
                  <span v-if="authorPersonalityTags.length === 0" class="text-xs text-gray-400">
                    성격 태그 없음
                  </span>
                </div>
              </div>

              <div>
                <h4 class="text-sm text-gray-500 mb-1">여행 스타일</h4>
                <div class="flex flex-wrap gap-2">
                  <span
                    v-for="(tag, index) in tripDetails.travelTags"
                    :key="index"
                    class="px-2 py-1 bg-blue-50 text-blue-600 text-xs rounded-full"
                  >
                    {{ tag }}
                  </span>
                </div>
              </div>

              <div class="pt-4 border-t">
                <button
                  class="w-full px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 flex items-center justify-center"
                  @click="copyToMyPlanner"
                  :disabled="isCopyLoading"
                >
                  {{ isCopyLoading ? '처리 중...' : '내 플래너에 가져오기' }}
                  <arrow-right-icon class="ml-2 h-4 w-4" />
                </button>
              </div>

              <div class="pt-4 border-t">
                <h4 class="text-sm text-gray-500 mb-2">이 작성자의 다른 여행 계획</h4>
                <ul class="space-y-3">
                  <li v-for="trip in authorOtherTrips" :key="trip.id">
                    <router-link
                      :to="`/tripdetail/${trip.id}`"
                      class="text-sm text-blue-600 hover:underline block"
                    >
                      {{ trip.title }}
                    </router-link>
                  </li>
                  <li v-if="authorOtherTrips.length === 0">
                    <span class="text-sm text-gray-400">다른 여행 계획이 없습니다.</span>
                  </li>
                </ul>
              </div>

              <div class="pt-4 border-t">
                <h4 class="text-sm text-gray-500 mb-2">비슷한 여행 계획</h4>
                <ul class="space-y-3">
                  <li>
                    <a href="#" class="text-sm text-blue-600 hover:underline"> 미구현 상태 </a>
                  </li>
                  <li>
                    <a href="#" class="text-sm text-blue-600 hover:underline"> 미구현 상태 </a>
                  </li>
                  <li>
                    <a href="#" class="text-sm text-blue-600 hover:underline"> 미구현 상태 </a>
                  </li>
                </ul>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref, computed, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ArrowRight as ArrowRightIcon, Heart as HeartIcon } from 'lucide-vue-next'
import { useUserStore } from '@/store/user'
import TripPlannerMap from '@/components/trip/TripPlannerMap.vue'
import api from '@/api'

const userStore = useUserStore()
const user = computed(() => userStore.loginUser)
const route = useRoute()
const router = useRouter()
const activeDay = ref(0)

// 좋아요 관련 상태
const isLiked = ref(false)
const likeCount = ref(0)
const isLikeLoading = ref(false)
const isCopyLoading = ref(false)

// 작성자 정보
const authorInfo = ref({
  name: '',
  userId: '',
})
const authorPersonalityTags = ref([])
const authorOtherTrips = ref([])

const tripDetails = ref({
  id: route.params.id || 'id',
  title: '제목',
  location: '지역',
  duration: 0,
  userId: '유저ID',
  image:
    'https://images.unsplash.com/photo-1601621915196-2ad9b06857b3?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1200&q=80',
  personalityTags: [],
  travelTags: [],
  likes: 0,
  shares: 0,
  description: '여행 계획 설명',
  itinerary: [],
})

// 원본 일정 데이터
const itinerary = ref([])

// 지도 컴포넌트에 전달할 처리된 일정 데이터
const processedItinerary = ref([])

// Kakao Maps API 로드 여부를 나타내는 ref
const isKakaoMapsLoaded = ref(false)

// 좋아요 토글 함수
const toggleLike = async () => {
  if (!user.value || !user.value.id) {
    alert('로그인이 필요합니다.')
    return
  }

  isLikeLoading.value = true
  try {
    const response = await api.post(
      `/api/v1/likes/toggle/${route.params.id}?userId=${user.value.id}`,
    )

    if (response.data.status === 'SUCCESS') {
      isLiked.value = response.data.isLiked
      likeCount.value = response.data.likeCount
      tripDetails.value.likes = response.data.likeCount
    }
  } catch (error) {
    console.error('좋아요 처리 실패:', error)
    alert('좋아요 처리에 실패했습니다.')
  } finally {
    isLikeLoading.value = false
  }
}

// 좋아요 상태 확인
const checkLikeStatus = async () => {
  if (!user.value || !user.value.id) return

  try {
    const response = await api.get(`/api/v1/likes/check/${route.params.id}?userId=${user.value.id}`)
    isLiked.value = response.data.isLiked
    likeCount.value = response.data.likeCount
  } catch (error) {
    console.error('좋아요 상태 확인 실패:', error)
  }
}

// 일정 데이터 처리 함수
const processItineraryData = () => {
  processedItinerary.value = itinerary.value.map((day) => {
    return {
      day: day.day,
      title: `Day ${day.day + 1}`,
      items: day.items.map((item) => {
        // items가 객체인 경우 (현재 구조)
        if (item.items && typeof item.items === 'object') {
          return {
            title: item.items.title,
            time: item.items.time || '',
            description: item.items.memo || '',
            type: item.items.type,
            placeData: item.items.placeData,
          }
        }
        // items가 이미 올바른 형식인 경우
        return item
      }),
    }
  })
}

// Kakao Maps API 로드 확인
const checkKakaoMapsLoaded = () => {
  if (!window.kakao || !window.kakao.maps) {
    console.warn("Kakao Maps API is not loaded. Make sure it's included in your index.html")
    isKakaoMapsLoaded.value = false
    return false
  }
  isKakaoMapsLoaded.value = true
  return true
}

// 작성자 정보 가져오기 함수 수정:
const fetchAuthorInfo = async (authorId) => {
  try {
    // 작성자 기본 정보 가져오기
    const userResponse = await api.get(`/api/v1/plan/user-info/${authorId}`)
    if (userResponse.data) {
      authorInfo.value = {
        name: userResponse.data.name,
        userId: userResponse.data.userId,
      }
    }

    // 작성자 성격 태그 가져오기
    const personalityResponse = await api.get(`/api/v1/tag/user/personal/${authorId}`)
    authorPersonalityTags.value = personalityResponse.data || []

    // 작성자의 다른 여행 계획 가져오기 (현재 계획 제외, 최대 3개)
    const tripsResponse = await api.get(`/api/v1/plan/user/${authorId}`)
    const allTrips = tripsResponse.data || []
    authorOtherTrips.value = allTrips
      .filter((trip) => trip.id !== parseInt(route.params.id))
      .slice(0, 3)
  } catch (error) {
    console.error('작성자 정보 가져오기 실패:', error)
  }
}

// 내 플래너에 가져오기 함수 수정:
const copyToMyPlanner = async () => {
  if (!user.value || !user.value.id) {
    alert('로그인이 필요합니다.')
    return
  }

  isCopyLoading.value = true

  try {
    // shares 수 증가
    const shareResponse = await api.patch(`/api/v1/plan/updateShare/${route.params.id}`)
    if (shareResponse.data && shareResponse.data.shares !== undefined) {
      tripDetails.value.shares = shareResponse.data.shares
    } else {
      // 응답에 shares 정보가 없으면 로컬에서 증가
      tripDetails.value.shares += 1
    }

    // 일정 데이터를 올바른 형식으로 변환
    const convertedItinerary = itinerary.value.map((day) => ({
      day: day.day,
      items: day.items.map((item) => ({
        title: item.title,
        type: item.type || '관광',
        memo: item.description || item.memo || '',
        time: item.time || '',
        placeData: {
          no: item.placeData?.no || item.placeData?.attraction_id || item.placeData?.content_id,
          latitude: item.placeData?.latitude || 0,
          longitude: item.placeData?.longitude || 0,
          contentTypeId: item.placeData?.contentTypeId || 12,
          title: item.title,
          addr: item.placeData?.addr || '',
          image: item.placeData?.image || '/placeholder.svg?height=150&width=150',
        },
      })),
    }))

    // 현재 여행 계획 데이터를 쿼리 파라미터로 전달
    const planData = {
      destination: tripDetails.value.location,
      duration: tripDetails.value.duration,
      members: 1, // 기본값
      transport: 'car', // 기본값
      title: `${tripDetails.value.title} (복사본)`,
      description: tripDetails.value.description,
      itinerary: JSON.stringify(convertedItinerary),
      tags: JSON.stringify(tripDetails.value.travelTags),
    }

    const queryString = new URLSearchParams(planData).toString()
    router.push(`/tripplan?${queryString}`)
  } catch (error) {
    console.error('공유 수 업데이트 실패:', error)
    alert('공유 처리에 실패했습니다.')
  } finally {
    isCopyLoading.value = false
  }
}

// 여행 계획 데이터 로드 함수
const loadTripData = async (tripId) => {
  try {
    // 기존 데이터 로드 코드
    const response = await api.get(`/api/v1/plan/${tripId}`)
    tripDetails.value.title = response.data.title
    tripDetails.value.duration = response.data.duration
    tripDetails.value.userId = response.data.userId
    tripDetails.value.description = response.data.description
    tripDetails.value.likes = response.data.likes
    tripDetails.value.shares = response.data.shares
    tripDetails.value.location = response.data.destination
    tripDetails.value.image = response.data.image

    // 작성자 정보 가져오기
    await fetchAuthorInfo(response.data.userId)

    // 좋아요 상태 확인
    await checkLikeStatus()

    // 일정 초기화
    itinerary.value = []
    for (let i = 0; i < response.data.duration; i++) {
      itinerary.value.push({ day: i, items: [] })
    }

    const tagRes = await api.get(`/api/v1/tag/plan/${tripId}`)
    tripDetails.value.travelTags = tagRes.data

    const itineraryRes = await api.get(`/api/v1/plan/itinerary/${tripId}`)
    const typeMap = {
      12: '관광',
      39: '식당',
      32: '숙박',
    }

    // 날짜별로 그룹핑 (tripDetails용)
    const grouped = itineraryRes.data.reduce((acc, cur) => {
      const { dayNumber } = cur
      if (!acc[dayNumber]) acc[dayNumber] = []

      acc[dayNumber].push({
        time: '',
        title: cur.title,
        description: cur.memo || '',
        type: typeMap[cur.contentTypeId] || '기타',
      })

      return acc
    }, {})

    // tripDetails.itinerary 구성
    tripDetails.value.itinerary = Object.entries(grouped).map(([day, items]) => ({
      day: Number(day),
      title: `Day ${day}`,
      items,
    }))

    // 지도용 itinerary 데이터 구성
    itineraryRes.data.forEach((res) => {
      const dayIndex = res.dayNumber - 1
      if (dayIndex >= 0 && dayIndex < itinerary.value.length) {
        itinerary.value[dayIndex].items.push({
          title: res.title,
          time: '',
          description: res.memo || '',
          type: typeMap[res.contentTypeId] || '기타',
          placeData: {
            no: res.attractionId, // no 필드 추가
            latitude: res.latitude,
            longitude: res.longitude,
            contentTypeId: res.contentTypeId,
            title: res.title,
            addr: res.addr,
            image: res.image,
          },
        })
      }
    })

    // 지도용 데이터 처리
    processItineraryData()

    // Kakao Maps API 로드 확인
    checkKakaoMapsLoaded()
  } catch (error) {
    console.error('Failed to fetch trip details:', error)
  }
}

// onMounted에서 작성자 정보도 가져오도록 수정:
onMounted(async () => {
  await loadTripData(route.params.id)
})

// route 파라미터 변경 감지
watch(
  () => route.params.id,
  async (newId, oldId) => {
    if (newId && newId !== oldId) {
      // 페이지 데이터 초기화
      tripDetails.value = {
        id: newId,
        title: '제목',
        location: '지역',
        duration: 0,
        userId: '유저ID',
        image:
          'https://images.unsplash.com/photo-1601621915196-2ad9b06857b3?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1200&q=80',
        personalityTags: [],
        travelTags: [],
        likes: 0,
        shares: 0,
        description: '여행 계획 설명',
        itinerary: [],
      }

      authorInfo.value = { name: '', userId: '' }
      authorPersonalityTags.value = []
      authorOtherTrips.value = []
      itinerary.value = []
      processedItinerary.value = []
      activeDay.value = 0

      // 새로운 데이터 로드
      await loadTripData(newId)
    }
  },
  { immediate: false },
)

// itinerary 또는 activeDay가 변경될 때 데이터 재처리
watch(
  [() => itinerary.value, () => activeDay.value],
  () => {
    processItineraryData()
  },
  { deep: true },
)
</script>

<style scoped>
/* 추가 스타일이 필요한 경우 여기에 작성 */
</style>
