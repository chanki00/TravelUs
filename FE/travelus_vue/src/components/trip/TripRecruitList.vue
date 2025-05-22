<template>
  <div>
    <div class="mb-6">
      <div class="flex gap-2 mb-4 flex-wrap">
        <input
          v-model="searchTerm"
          placeholder="여행지나 제목으로 검색"
          class="max-w-xs px-3 py-2 border rounded-md"
        />
        <div class="relative">
          <button
            @click="showTagFilter = !showTagFilter"
            class="px-4 py-2 border border-dashed rounded-md flex items-center gap-2"
          >
            <tag-icon class="h-4 w-4" />
            태그 필터
          </button>
          <div
            v-if="showTagFilter"
            class="absolute top-full left-0 mt-2 w-80 bg-white rounded-md border shadow-lg p-4 z-10"
          >
            <div class="space-y-4">
              <div class="flex flex-wrap gap-2">
                <span
                  v-for="tag in availableTags"
                  :key="tag"
                  :class="[
                    'px-2 py-1 text-xs rounded-full cursor-pointer',
                    selectedTags.includes(tag)
                      ? 'bg-blue-600 text-white'
                      : 'bg-gray-100 text-gray-700 hover:bg-gray-200',
                  ]"
                  @click="toggleTag(tag)"
                >
                  {{ tag }}
                </span>
              </div>
              <div class="flex justify-between">
                <button
                  class="px-3 py-1.5 text-sm border rounded-md hover:bg-gray-50"
                  @click="selectedTags = []"
                >
                  초기화
                </button>
                <button
                  class="px-3 py-1.5 text-sm bg-blue-600 text-white rounded-md hover:bg-blue-700"
                  @click="showTagFilter = false"
                >
                  적용하기
                </button>
              </div>
            </div>
          </div>
        </div>
        <button class="px-4 py-2 border rounded-md hover:bg-gray-50" @click="searchRecruits">
          검색
        </button>
      </div>

      <div v-if="isLoading" class="text-center py-8">
        <div
          class="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-500 mx-auto mb-4"
        ></div>
        <p class="text-gray-500">모집글을 불러오는 중...</p>
      </div>

      <div
        v-else-if="recruitPosts.length === 0"
        class="text-center py-8 bg-white rounded-lg border shadow-sm"
      >
        <users-icon class="h-12 w-12 mx-auto mb-3 text-gray-400" />
        <p class="text-gray-500 mb-4">등록된 모집글이 없습니다.</p>
        <router-link
          to="/recruit/create"
          class="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 inline-block"
        >
          모집글 작성하기
        </router-link>
      </div>

      <div v-else class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        <div
          v-for="post in recruitPosts"
          :key="post.id"
          class="bg-white rounded-lg border shadow-sm overflow-hidden cursor-pointer hover:shadow-md transition-shadow"
          @click="openTripDetails(post)"
        >
          <div class="h-48 overflow-hidden">
            <img
              :src="
                post.plan && post.plan.image
                  ? post.plan.image
                  : 'https://images.unsplash.com/photo-1476514525535-07fb3b4ae5f1?q=80&w=1000&auto=format&fit=crop'
              "
              :alt="post.title"
              class="w-full h-full object-cover object-center"
            />
          </div>
          <div class="p-4 border-b">
            <div class="flex justify-between items-start">
              <h3 class="text-lg font-medium">{{ post.title }}</h3>
            </div>
            <div class="flex items-center text-sm text-gray-500">
              <map-pin-icon class="h-3 w-3 mr-1" />
              {{ post.plan ? getSidoName(Number(post.plan.destination)) : '지역 정보 없음' }}
            </div>
          </div>
          <div class="p-4">
            <div class="flex flex-wrap gap-1 mb-3">
              <span
                v-for="tag in post.tags || []"
                :key="tag"
                class="px-2 py-0.5 bg-gray-100 text-gray-700 text-xs rounded-full"
              >
                {{ tag }}
              </span>
            </div>
            <div class="flex items-center justify-between text-sm mb-3">
              <div class="text-gray-500">{{ formatDateRange(post.startDate, post.endDate) }}</div>
              <div class="flex items-center">
                <users-icon class="h-3 w-3 mr-1" />
                <span> {{ post.currentMembers }}/{{ post.max_members }}명 </span>
              </div>
            </div>
            <p class="text-sm text-gray-500 line-clamp-2">{{ post.content }}</p>
          </div>
          <div class="p-4 border-t flex justify-between">
            <div class="text-sm">호스트: {{ post.userId }}</div>
            <div class="text-xs text-gray-500">{{ formatDate(post.createdAt) }}</div>
          </div>
        </div>
      </div>
    </div>

    <!-- 여행 모집 상세 모달 -->
    <trip-detail-modal
      v-if="showTripDetails"
      :show="showTripDetails"
      :post="selectedPost"
      @close="closeTripDetails"
    />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import api from '@/api'
import { Tag as TagIcon, MapPin as MapPinIcon, Users as UsersIcon } from 'lucide-vue-next'
import TripDetailModal from './TripDetailModal.vue'

// 상태 관리
const searchTerm = ref('')
const selectedTags = ref([])
const recruitPosts = ref([])
const isLoading = ref(false)
const sidos = ref([])
const showTagFilter = ref(false)
const showTripDetails = ref(false)
const selectedPost = ref(null)

// 사용 가능한 태그
const availableTags = [
  '식도락',
  '자연',
  '문화',
  '쇼핑',
  '힐링',
  '액티비티',
  '사진',
  '도시',
  '가족',
  '커플',
  '친구',
  '혼자',
  '저예산',
  '럭셔리',
]

// 모집글 가져오기
const fetchRecruitPosts = async () => {
  try {
    isLoading.value = true

    // 모든 모집글 가져오기
    const response = await api.get('/api/v1/post/recruit')
    let posts = response.data

    // 각 모집글에 대해 여행 계획 데이터와 태그 정보 가져오기
    posts = await Promise.all(
      posts.map(async (post) => {
        try {
          // 여행 계획이 있는 경우에만 조회
          if (post.planId) {
            // 여행 계획 정보 가져오기
            const planRes = await api.get(`/api/v1/plan/${post.planId}`)

            // 태그 정보 가져오기
            const tagRes = await api.get(`/api/v1/tag/plan/${post.planId}`)
            return {
              ...post,
              plan: planRes.data,
              tags: tagRes.data,
            }
          }

          return post
        } catch (error) {
          console.error('모집글 추가 정보 가져오기 실패:', error)
          return post
        }
      }),
    )

    recruitPosts.value = posts
    console.log(recruitPosts.value)
  } catch (error) {
    console.error('모집글 목록 가져오기 실패:', error)
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

// 태그 토글 함수
const toggleTag = (tag) => {
  if (selectedTags.value.includes(tag)) {
    selectedTags.value = selectedTags.value.filter((t) => t !== tag)
  } else {
    selectedTags.value.push(tag)
  }
}

const searchRecruits = async () => {
  try {
    isLoading.value = true

    // 검색어와 태그로 필터링된 모집글 가져오기
    let url = '/api/v1/post/recruit'

    // 검색어가 있는 경우 쿼리 파라미터 추가
    if (searchTerm.value) {
      url += `?search=${encodeURIComponent(searchTerm.value)}`
    }

    const response = await api.get(url)
    let posts = response.data

    // 각 모집글에 대해 여행 계획 데이터와 태그 정보 가져오기
    posts = await Promise.all(
      posts.map(async (post) => {
        try {
          // 여행 계획이 있는 경우에만 조회
          if (post.planId) {
            // 여행 계획 정보 가져오기
            const planRes = await api.get(`/api/v1/plan/${post.planId}`)

            // 태그 정보 가져오기
            const tagRes = await api.get(`/api/v1/tag/plan/${post.planId}`)

            return {
              ...post,
              plan: planRes.data,
              tags: tagRes.data,
            }
          }

          return post
        } catch (error) {
          console.error('모집글 추가 정보 가져오기 실패:', error)
          return post
        }
      }),
    )

    // 태그 필터링 (클라이언트 측에서 수행)
    if (selectedTags.value.length > 0) {
      posts = posts.filter((post) => {
        if (!post.tags || post.tags.length === 0) return false
        return selectedTags.value.some((tag) => post.tags.includes(tag))
      })
    }

    recruitPosts.value = posts
  } catch (error) {
    console.error('모집글 검색 실패:', error)
  } finally {
    isLoading.value = false
  }
}

// 여행 상세 정보 열기
const openTripDetails = (post) => {
  selectedPost.value = post
  showTripDetails.value = true
}

// 여행 상세 정보 닫기
const closeTripDetails = () => {
  showTripDetails.value = false
  selectedPost.value = null
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

// 날짜 범위 포맷팅
const formatDateRange = (startDate, endDate) => {
  if (!startDate || !endDate) return '기간 정보 없음'

  try {
    const start = new Date(startDate)
    const end = new Date(endDate)

    if (isNaN(start.getTime()) || isNaN(end.getTime())) return '날짜 형식 오류'

    const startStr = `${start.getFullYear()}.${String(start.getMonth() + 1).padStart(2, '0')}.${String(start.getDate()).padStart(2, '0')}`
    const endStr = `${end.getFullYear()}.${String(end.getMonth() + 1).padStart(2, '0')}.${String(end.getDate()).padStart(2, '0')}`

    return `${startStr} ~ ${endStr}`
  } catch (error) {
    console.error('날짜 범위 포맷팅 오류:', error)
    return '기간 정보 없음'
  }
}

// 컴포넌트 마운트 시 모집글과 시도 목록 가져오기
onMounted(() => {
  fetchRecruitPosts()
  fetchSidos()
})
</script>
