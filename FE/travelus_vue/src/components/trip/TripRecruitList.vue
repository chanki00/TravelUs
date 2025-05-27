<template>
  <div>
    <!-- 검색 필터 섹션 -->
    <div class="mb-6 bg-white p-4 rounded-lg shadow-sm border">
      <div class="flex flex-col sm:flex-row gap-4">
        <!-- 지역 필터 -->
        <div class="relative">
          <button
            @click="showRegionFilter = !showRegionFilter"
            class="flex items-center justify-between w-full sm:w-48 px-3 py-2 border rounded-md hover:bg-gray-50 focus:outline-none focus:ring-1 focus:ring-blue-500 h-10"
          >
            <span class="text-sm">
              {{ selectedRegion ? getSelectedRegionName() : '지역 선택' }}
            </span>
            <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path
                stroke-linecap="round"
                stroke-linejoin="round"
                stroke-width="2"
                d="M19 9l-7 7-7-7"
              ></path>
            </svg>
          </button>

          <!-- 지역 드롭다운 -->
          <div
            v-if="showRegionFilter"
            class="absolute top-full left-0 mt-1 w-full sm:w-48 bg-white border rounded-md shadow-lg z-10 max-h-60 overflow-y-auto"
          >
            <button
              @click="selectRegion('')"
              class="w-full px-3 py-2 text-left hover:bg-gray-50 text-sm"
            >
              모든 지역
            </button>
            <button
              v-for="sido in sidos"
              :key="sido.sidoCode"
              @click="selectRegion(sido.sidoCode)"
              :class="[
                'w-full px-3 py-2 text-left hover:bg-gray-50 text-sm',
                selectedRegion === sido.sidoCode ? 'bg-blue-50 text-blue-600' : '',
              ]"
            >
              {{ sido.sidoName }}
            </button>
          </div>
        </div>

        <!-- 성격 태그 필터 -->
        <div class="relative">
          <button
            @click="showPersonalityTagFilter = !showPersonalityTagFilter"
            class="flex items-center justify-between w-full sm:w-48 px-3 py-2 border rounded-md hover:bg-gray-50 focus:outline-none focus:ring-1 focus:ring-blue-500 h-10"
          >
            <span class="text-sm">
              {{
                tempSelectedPersonalityTags.length > 0
                  ? `성격 ${tempSelectedPersonalityTags.length}개 선택`
                  : '성격 태그'
              }}
            </span>
            <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path
                stroke-linecap="round"
                stroke-linejoin="round"
                stroke-width="2"
                d="M19 9l-7 7-7-7"
              ></path>
            </svg>
          </button>

          <!-- 성격 태그 드롭다운 -->
          <div
            v-if="showPersonalityTagFilter"
            class="absolute top-full left-0 mt-1 w-full sm:w-64 bg-white border rounded-md shadow-lg z-10"
          >
            <div class="p-3">
              <div class="grid grid-cols-2 gap-2 mb-3 max-h-48 overflow-y-auto">
                <button
                  v-for="tag in allPersonalityTags"
                  :key="tag"
                  @click="toggleTempPersonalityTag(tag)"
                  :class="[
                    'px-3 py-1 text-sm rounded-full transition-colors',
                    tempSelectedPersonalityTags.includes(tag)
                      ? 'bg-green-600 text-white'
                      : 'bg-gray-100 text-gray-700 hover:bg-gray-200',
                  ]"
                >
                  {{ tag }}
                </button>
              </div>
              <div class="flex gap-2">
                <button
                  @click="applyPersonalityTagFilter"
                  class="flex-1 px-3 py-2 bg-green-600 text-white text-sm rounded-md hover:bg-green-700"
                >
                  적용하기
                </button>
                <button
                  @click="cancelPersonalityTagFilter"
                  class="px-3 py-2 bg-gray-200 text-gray-700 text-sm rounded-md hover:bg-gray-300"
                >
                  취소
                </button>
              </div>
            </div>
          </div>
        </div>

        <!-- 여행 태그 필터 -->
        <div class="relative">
          <button
            @click="showTravelTagFilter = !showTravelTagFilter"
            class="flex items-center justify-between w-full sm:w-48 px-3 py-2 border rounded-md hover:bg-gray-50 focus:outline-none focus:ring-1 focus:ring-blue-500 h-10"
          >
            <span class="text-sm">
              {{
                tempSelectedTravelTags.length > 0
                  ? `여행 ${tempSelectedTravelTags.length}개 선택`
                  : '여행 태그'
              }}
            </span>
            <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path
                stroke-linecap="round"
                stroke-linejoin="round"
                stroke-width="2"
                d="M19 9l-7 7-7-7"
              ></path>
            </svg>
          </button>

          <!-- 여행 태그 드롭다운 -->
          <div
            v-if="showTravelTagFilter"
            class="absolute top-full left-0 mt-1 w-full sm:w-64 bg-white border rounded-md shadow-lg z-10"
          >
            <div class="p-3">
              <div class="grid grid-cols-2 gap-2 mb-3 max-h-48 overflow-y-auto">
                <button
                  v-for="tag in allTravelTags"
                  :key="tag"
                  @click="toggleTempTravelTag(tag)"
                  :class="[
                    'px-3 py-1 text-sm rounded-full transition-colors',
                    tempSelectedTravelTags.includes(tag)
                      ? 'bg-blue-600 text-white'
                      : 'bg-gray-100 text-gray-700 hover:bg-gray-200',
                  ]"
                >
                  {{ tag }}
                </button>
              </div>
              <div class="flex gap-2">
                <button
                  @click="applyTravelTagFilter"
                  class="flex-1 px-3 py-2 bg-blue-600 text-white text-sm rounded-md hover:bg-blue-700"
                >
                  적용하기
                </button>
                <button
                  @click="cancelTravelTagFilter"
                  class="px-3 py-2 bg-gray-200 text-gray-700 text-sm rounded-md hover:bg-gray-300"
                >
                  취소
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 선택된 필터 표시 -->
      <div v-if="hasActiveFilters" class="mt-4 flex flex-wrap gap-2 items-center">
        <div v-if="selectedRegion" class="flex items-center bg-blue-50 px-3 py-1 rounded-full">
          <span class="text-sm text-blue-700">{{ getSelectedRegionName() }}</span>
          <button @click="clearRegionFilter" class="ml-2 text-blue-700 hover:text-blue-900">
            ✕
          </button>
        </div>
        <div
          v-for="tag in selectedPersonalityTags"
          :key="'personality-' + tag"
          class="flex items-center bg-green-50 px-3 py-1 rounded-full"
        >
          <span class="text-sm text-green-700">성격: {{ tag }}</span>
          <button
            @click="removePersonalityTag(tag)"
            class="ml-2 text-green-700 hover:text-green-900"
          >
            ✕
          </button>
        </div>
        <div
          v-for="tag in selectedTravelTags"
          :key="'travel-' + tag"
          class="flex items-center bg-blue-50 px-3 py-1 rounded-full"
        >
          <span class="text-sm text-blue-700">여행: {{ tag }}</span>
          <button @click="removeTravelTag(tag)" class="ml-2 text-blue-700 hover:text-blue-900">
            ✕
          </button>
        </div>
        <button @click="clearAllFilters" class="text-sm text-blue-600 hover:text-blue-800 ml-auto">
          필터 초기화
        </button>
      </div>
    </div>

    <!-- 검색 결과 메시지 -->
    <div v-if="hasActiveFilters" class="mb-4 text-gray-600">
      {{ filteredPosts.length }}개의 검색 결과가 있습니다.
    </div>

    <div v-if="isLoading && displayedPosts.length === 0" class="text-center py-8">
      <div
        class="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-500 mx-auto mb-4"
      ></div>
      <p class="text-gray-500">모집글을 불러오는 중...</p>
    </div>

    <div
      v-else-if="filteredPosts.length === 0 && !isLoading"
      class="text-center py-8 bg-white rounded-lg border shadow-sm"
    >
      <users-icon class="h-12 w-12 mx-auto mb-3 text-gray-400" />
      <p class="text-gray-500 mb-4">
        {{ hasActiveFilters ? '검색 조건에 맞는 모집글이 없습니다.' : '등록된 모집글이 없습니다.' }}
      </p>
      <router-link
        to="/recruit/create"
        class="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 inline-block"
      >
        모집글 작성하기
      </router-link>
    </div>

    <div v-else class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
      <div
        v-for="post in displayedPosts"
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
          <!-- 성격 태그 표시 (상단) -->
          <div
            v-if="post.personalityTags && post.personalityTags.length > 0"
            class="flex flex-wrap gap-1 mb-2"
          >
            <span
              v-for="tag in post.personalityTags"
              :key="'personality-' + tag"
              class="px-2 py-0.5 bg-green-100 text-green-700 text-xs rounded-full"
            >
              {{ tag }}
            </span>
          </div>

          <!-- 여행 태그 표시 (하단) -->
          <div class="flex flex-wrap gap-1 mb-3">
            <span
              v-for="tag in post.tags || []"
              :key="'travel-' + tag"
              class="px-2 py-0.5 bg-blue-100 text-blue-700 text-xs rounded-full"
            >
              {{ tag }}
            </span>
          </div>

          <div class="flex items-center justify-between text-sm mb-3">
            <div class="text-gray-500">{{ formatDateRange(post.startDate, post.endDate) }}</div>
            <div class="flex items-center">
              <users-icon class="h-3 w-3 mr-1" />
              <span> {{ post.chatroon_user }}/{{ post.currentMembers }}명 </span>
            </div>
          </div>
          <p class="text-sm text-gray-500 line-clamp-2">{{ post.content }}</p>
        </div>
        <div class="p-4 border-t flex justify-between">
          <div class="text-sm">호스트: {{ post.authorName }}</div>
          <div class="text-xs text-gray-500">{{ formatDate(post.createdAt) }}</div>
        </div>
      </div>
    </div>

    <!-- 무한 스크롤 로딩 -->
    <div v-if="isLoadingMore" class="mt-8 text-center py-4">
      <div class="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-500 mx-auto mb-2"></div>
      <p class="text-gray-500 text-sm">더 많은 모집글을 불러오는 중...</p>
    </div>

    <!-- 모든 데이터 로드 완료 -->
    <div v-if="hasReachedEnd && displayedPosts.length > 0" class="mt-8 text-center py-4">
      <p class="text-gray-500 text-sm">모든 모집글을 확인했습니다.</p>
    </div>

    <!-- 여행 모집 상세 모달 -->
    <trip-detail-modal
      v-if="showTripDetails"
      :show="showTripDetails"
      :post="selectedPost"
      @close="closeTripDetails"
      @deleted="fetchRecruitPosts"
    />
  </div>
</template>

<script setup>
import { ref, onMounted, computed, watch, onUnmounted } from 'vue'
import api from '@/api'
import { MapPin as MapPinIcon, Users as UsersIcon } from 'lucide-vue-next'
import TripDetailModal from './TripDetailModal.vue'

// 상태 관리
const recruitPosts = ref([])
const isLoading = ref(false)
const isLoadingMore = ref(false)
const sidos = ref([])
const showTripDetails = ref(false)
const selectedPost = ref(null)

// 무한 스크롤 관련 상태
const itemsPerPage = 9
const currentPage = ref(1)
const hasReachedEnd = ref(false)

// 필터 상태
const selectedRegion = ref('')
const selectedPersonalityTags = ref([])
const selectedTravelTags = ref([])
const tempSelectedPersonalityTags = ref([]) // 임시 선택된 성격 태그들
const tempSelectedTravelTags = ref([]) // 임시 선택된 여행 태그들
const showRegionFilter = ref(false)
const showPersonalityTagFilter = ref(false)
const showTravelTagFilter = ref(false)
const allPersonalityTags = ref([])
const allTravelTags = ref([])

// 필터가 활성화되었는지 확인
const hasActiveFilters = computed(() => {
  return (
    selectedRegion.value ||
    selectedPersonalityTags.value.length > 0 ||
    selectedTravelTags.value.length > 0
  )
})

// 필터링된 모집글 목록
const filteredPosts = computed(() => {
  let result = [...recruitPosts.value]

  // 지역 필터링
  if (selectedRegion.value) {
    result = result.filter((post) => {
      return post.plan && Number(post.plan.destination) === Number(selectedRegion.value)
    })
  }

  // 성격 태그 필터링
  if (selectedPersonalityTags.value.length > 0) {
    result = result.filter((post) => {
      if (!post.personalityTags || post.personalityTags.length === 0) return false
      return selectedPersonalityTags.value.some((tag) => post.personalityTags.includes(tag))
    })
  }

  // 여행 태그 필터링
  if (selectedTravelTags.value.length > 0) {
    result = result.filter((post) => {
      if (!post.tags || post.tags.length === 0) return false
      return selectedTravelTags.value.some((tag) => post.tags.includes(tag))
    })
  }

  return result
})

// 현재 표시할 모집글들
const displayedPosts = computed(() => {
  return filteredPosts.value.slice(0, currentPage.value * itemsPerPage)
})

// 무한 스크롤 핸들러
const handleScroll = () => {
  if (isLoadingMore.value || hasReachedEnd.value) return

  const scrollTop = window.pageYOffset || document.documentElement.scrollTop
  const windowHeight = window.innerHeight
  const documentHeight = document.documentElement.scrollHeight

  // 스크롤이 하단에서 200px 이내에 도달했을 때
  if (scrollTop + windowHeight >= documentHeight - 200) {
    loadMoreItems()
  }
}

// 더 많은 아이템 로드
const loadMoreItems = () => {
  if (isLoadingMore.value || hasReachedEnd.value) return

  const totalItems = filteredPosts.value.length
  const currentDisplayed = currentPage.value * itemsPerPage

  if (currentDisplayed >= totalItems) {
    hasReachedEnd.value = true
    return
  }

  isLoadingMore.value = true

  // 실제 API 호출 시뮬레이션 (현재는 클라이언트 사이드 페이지네이션)
  setTimeout(() => {
    currentPage.value += 1
    isLoadingMore.value = false

    // 모든 데이터를 로드했는지 확인
    if (currentPage.value * itemsPerPage >= totalItems) {
      hasReachedEnd.value = true
    }
  }, 500)
}

// 필터 변경 시 페이지 초기화
const resetPagination = () => {
  currentPage.value = 1
  hasReachedEnd.value = false
  isLoadingMore.value = false
}

// 지역 선택
const selectRegion = (regionCode) => {
  selectedRegion.value = regionCode
  showRegionFilter.value = false
  resetPagination()
}

// 지역 필터 초기화
const clearRegionFilter = () => {
  selectedRegion.value = ''
  resetPagination()
}

// 임시 성격 태그 토글
const toggleTempPersonalityTag = (tag) => {
  if (tempSelectedPersonalityTags.value.includes(tag)) {
    tempSelectedPersonalityTags.value = tempSelectedPersonalityTags.value.filter((t) => t !== tag)
  } else {
    tempSelectedPersonalityTags.value.push(tag)
  }
}

// 임시 여행 태그 토글
const toggleTempTravelTag = (tag) => {
  if (tempSelectedTravelTags.value.includes(tag)) {
    tempSelectedTravelTags.value = tempSelectedTravelTags.value.filter((t) => t !== tag)
  } else {
    tempSelectedTravelTags.value.push(tag)
  }
}

// 성격 태그 필터 적용
const applyPersonalityTagFilter = () => {
  selectedPersonalityTags.value = [...tempSelectedPersonalityTags.value]
  showPersonalityTagFilter.value = false
  resetPagination()
}

// 성격 태그 필터 취소
const cancelPersonalityTagFilter = () => {
  tempSelectedPersonalityTags.value = [...selectedPersonalityTags.value]
  showPersonalityTagFilter.value = false
}

// 여행 태그 필터 적용
const applyTravelTagFilter = () => {
  selectedTravelTags.value = [...tempSelectedTravelTags.value]
  showTravelTagFilter.value = false
  resetPagination()
}

// 여행 태그 필터 취소
const cancelTravelTagFilter = () => {
  tempSelectedTravelTags.value = [...selectedTravelTags.value]
  showTravelTagFilter.value = false
}

// 성격 태그 제거
const removePersonalityTag = (tag) => {
  selectedPersonalityTags.value = selectedPersonalityTags.value.filter((t) => t !== tag)
  tempSelectedPersonalityTags.value = tempSelectedPersonalityTags.value.filter((t) => t !== tag)
  resetPagination()
}

// 여행 태그 제거
const removeTravelTag = (tag) => {
  selectedTravelTags.value = selectedTravelTags.value.filter((t) => t !== tag)
  tempSelectedTravelTags.value = tempSelectedTravelTags.value.filter((t) => t !== tag)
  resetPagination()
}

// 모든 필터 초기화
const clearAllFilters = () => {
  selectedRegion.value = ''
  selectedPersonalityTags.value = []
  selectedTravelTags.value = []
  tempSelectedPersonalityTags.value = []
  tempSelectedTravelTags.value = []
  resetPagination()
}

// 외부 클릭 시 드롭다운 닫기
const handleClickOutside = (event) => {
  if (!event.target.closest('.relative')) {
    showRegionFilter.value = false
    showPersonalityTagFilter.value = false
    showTravelTagFilter.value = false
  }
}

// 모든 태그 가져오기
const fetchAllTags = async () => {
  try {
    const uniquePersonalityTags = new Set()
    const uniqueTravelTags = new Set()

    recruitPosts.value.forEach((post) => {
      // 성격 태그 추출
      if (post.personalityTags && Array.isArray(post.personalityTags)) {
        post.personalityTags.forEach((tag) => uniquePersonalityTags.add(tag))
      }

      // 여행 태그 추출
      if (post.tags && Array.isArray(post.tags)) {
        post.tags.forEach((tag) => uniqueTravelTags.add(tag))
      }
    })

    allPersonalityTags.value = Array.from(uniquePersonalityTags)
    allTravelTags.value = Array.from(uniqueTravelTags)
  } catch (error) {
    console.error('태그 목록 조회 실패:', error)
    allPersonalityTags.value = ['활발한', '조용한', '계획적인', '즉흥적인', '사교적인', '내향적인']
    allTravelTags.value = ['가족여행', '혼자여행', '커플여행', '맛집투어', '자연경관', '역사탐방']
  }
}

// 모집글 가져오기
const fetchRecruitPosts = async () => {
  try {
    isLoading.value = true

    const response = await api.get('/api/v1/post/recruit')
    let posts = response.data

    posts = await Promise.all(
      posts.map(async (post) => {
        if (!post) return null
        try {
          // 성격 태그 파싱
          let personalityTags = []
          if (post.personalityTags) {
            try {
              personalityTags = JSON.parse(post.personalityTags)
            } catch (e) {
              console.error('성격 태그 파싱 오류:', e)
              personalityTags = []
            }
          }
          const authorRes = await api.get(`/api/v1/plan/user-info/${post.userId}`)

          const chatroonUserRes = await api.get(`/api/v1/chat/usercount/${post.chatroomId}`)
          // 여행 계획이 있는 경우에만 조회
          if (post.planId) {
            const planRes = await api.get(`/api/v1/plan/${post.planId}`)
            const tagRes = await api.get(`/api/v1/tag/plan/${post.planId}`)
            return {
              ...post,
              plan: planRes.data,
              tags: tagRes.data,
              personalityTags: personalityTags,
              authorName: authorRes.data?.name || 'Unknown',
              authorUserId: authorRes.data?.userId || 'unknown',
              chatroon_user : chatroonUserRes.data || 0,
            }
          }

          return {
            ...post,
            personalityTags: personalityTags,
            authorName: 'Unknown',
            authorUserId: 'unknown',
          }
        } catch (error) {
          console.error('모집글 추가 정보 가져오기 실패:', error)
          return post
        }
      }),
    )

    recruitPosts.value = posts
    fetchAllTags()
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

const getSelectedRegionName = () => {
  if (!selectedRegion.value) return ''
  return getSidoName(Number(selectedRegion.value))
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

// 필터 변경 감지하여 페이지네이션 리셋
watch([selectedRegion, selectedPersonalityTags, selectedTravelTags], () => {
  resetPagination()
})

// 컴포넌트 마운트 시 모집글과 시도 목록 가져오기
onMounted(() => {
  fetchRecruitPosts()
  fetchSidos()
  document.addEventListener('click', handleClickOutside)
  window.addEventListener('scroll', handleScroll)
})

onUnmounted(() => {
  document.removeEventListener('click', handleClickOutside)
  window.removeEventListener('scroll', handleScroll)
})
</script>
