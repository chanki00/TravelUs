<template>
  <div class="min-h-screen bg-gradient-to-b from-blue-50 to-white">
    <div class="py-8 px-6 max-w-7xl mx-auto">
      <h1 class="text-3xl font-bold mb-2">여행 계획 공유 게시판</h1>
      <p class="text-gray-500 mb-8">다른 여행자들의 일정을 확인하고 참고해 보세요</p>

      <div class="mb-6">
        <div class="border-b mb-6">
          <div class="flex">
            <button
              v-for="tab in tabs"
              :key="tab.value"
              :class="[
                'px-4 py-2',
                activeFilter === tab.value
                  ? 'border-b-2 border-blue-600 text-blue-600'
                  : 'text-gray-500',
              ]"
              @click="changeTab(tab.value)"
            >
              {{ tab.label }}
            </button>
          </div>
        </div>

        <!-- 검색 필터와 업로드 버튼 -->
        <div class="flex flex-col lg:flex-row items-stretch gap-4 mb-6">
          <!-- 필터 섹션 -->
          <div class="flex-1 bg-white p-4 rounded-lg shadow-sm border">
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

              <!-- 태그 필터 -->
              <div class="relative">
                <button
                  @click="showTagFilter = !showTagFilter"
                  class="flex items-center justify-between w-full sm:w-48 px-3 py-2 border rounded-md hover:bg-gray-50 focus:outline-none focus:ring-1 focus:ring-blue-500 h-10"
                >
                  <span class="text-sm">
                    {{
                      tempSelectedTags.length > 0
                        ? `태그 ${tempSelectedTags.length}개 선택`
                        : '태그 선택'
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

                <!-- 태그 드롭다운 -->
                <div
                  v-if="showTagFilter"
                  class="absolute top-full left-0 mt-1 w-full sm:w-64 bg-white border rounded-md shadow-lg z-10"
                >
                  <div class="p-3">
                    <div class="grid grid-cols-2 gap-2 mb-3 max-h-48 overflow-y-auto">
                      <button
                        v-for="tag in allTags"
                        :key="tag"
                        @click="toggleTempTag(tag)"
                        :class="[
                          'px-3 py-1 text-sm rounded-full transition-colors',
                          tempSelectedTags.includes(tag)
                            ? 'bg-blue-600 text-white'
                            : 'bg-gray-100 text-gray-700 hover:bg-gray-200',
                        ]"
                      >
                        {{ tag }}
                      </button>
                    </div>
                    <div class="flex gap-2">
                      <button
                        @click="applyTagFilter"
                        class="flex-1 px-3 py-2 bg-blue-600 text-white text-sm rounded-md hover:bg-blue-700"
                      >
                        적용하기
                      </button>
                      <button
                        @click="cancelTagFilter"
                        class="px-3 py-2 bg-gray-200 text-gray-700 text-sm rounded-md hover:bg-gray-300"
                      >
                        취소
                      </button>
                    </div>
                  </div>
                </div>
              </div>

              <!-- 검색어 입력 -->
              <div class="flex-1">
                <div class="flex h-full">
                  <input
                    v-model="searchTerm"
                    placeholder="여행 계획 검색하기"
                    class="px-3 py-2 border rounded-l-md focus:outline-none focus:ring-1 focus:ring-blue-500 flex-grow h-10"
                    @keyup.enter="searchTrips"
                  />
                  <button
                    class="px-4 py-2 bg-blue-600 text-white rounded-r-md hover:bg-blue-700 h-10"
                    @click="searchTrips"
                  >
                    검색
                  </button>
                </div>
              </div>
            </div>

            <!-- 선택된 필터 표시 -->
            <div v-if="hasActiveFilters" class="mt-4 flex flex-wrap gap-2 items-center">
              <div
                v-if="selectedRegion"
                class="flex items-center bg-blue-50 px-3 py-1 rounded-full"
              >
                <span class="text-sm text-blue-700">{{ getSelectedRegionName() }}</span>
                <button @click="clearRegionFilter" class="ml-2 text-blue-700 hover:text-blue-900">
                  ✕
                </button>
              </div>
              <div
                v-for="tag in selectedTags"
                :key="tag"
                class="flex items-center bg-blue-50 px-3 py-1 rounded-full"
              >
                <span class="text-sm text-blue-700">{{ tag }}</span>
                <button @click="removeTag(tag)" class="ml-2 text-blue-700 hover:text-blue-900">
                  ✕
                </button>
              </div>
              <div v-if="searchTerm" class="flex items-center bg-blue-50 px-3 py-1 rounded-full">
                <span class="text-sm text-blue-700">"{{ searchTerm }}"</span>
                <button @click="clearSearchTerm" class="ml-2 text-blue-700 hover:text-blue-900">
                  ✕
                </button>
              </div>
              <button
                @click="clearAllFilters"
                class="text-sm text-blue-600 hover:text-blue-800 ml-auto"
              >
                필터 초기화
              </button>
            </div>
          </div>

          <!-- 업로드 버튼 -->
          <div class="flex items-center h-full">
            <button
              class="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 whitespace-nowrap h-10"
              @click="openUploadModal"
            >
              내 여행 계획 업로드
            </button>
          </div>
        </div>

        <!-- 검색 결과 메시지 -->
        <div v-if="hasActiveFilters" class="mb-4 text-gray-600">
          {{ filteredTrips.length }}개의 검색 결과가 있습니다.
        </div>

        <!-- 로딩 상태 -->
        <div v-if="isLoading && displayedTrips.length === 0" class="text-center py-8">
          <div
            class="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-500 mx-auto mb-4"
          ></div>
          <p class="text-gray-500">여행 계획을 불러오는 중...</p>
        </div>

        <!-- 검색 결과 없음 메시지 -->
        <div v-else-if="filteredTrips.length === 0 && !isLoading" class="mt-8 text-center py-12">
          <div class="text-gray-400 text-lg mb-2">
            {{ hasActiveFilters ? '검색 결과가 없습니다' : '등록된 여행 계획이 없습니다' }}
          </div>
          <p class="text-gray-500">
            {{
              hasActiveFilters
                ? '다른 검색어나 필터를 사용해 보세요'
                : '첫 번째 여행 계획을 업로드해 보세요'
            }}
          </p>
        </div>

        <!-- 여행 계획 카드 그리드 -->
        <div v-else class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          <router-link
            v-for="trip in displayedTrips"
            :key="trip.id"
            :to="`/tripdetail/${trip.id}`"
            class="block"
          >
            <div
              class="bg-white rounded-lg border shadow-sm overflow-hidden hover:shadow-lg transition-shadow h-full"
            >
              <div class="h-48 overflow-hidden relative">
                <img
                  :src="trip.image"
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
                      <span class="text-xs">{{ user.image }}</span>
                    </div>
                    <span class="text-xs text-gray-600">{{ user.name }}</span>
                  </div>

                  <div class="flex items-center gap-3 text-xs text-gray-500">
                    <span>❤️{{ trip.likes }}</span>
                    <span>💬 {{ trip.shares }}</span>
                  </div>
                </div>
              </div>
            </div>
          </router-link>
        </div>

        <!-- 무한 스크롤 로딩 -->
        <div v-if="isLoadingMore" class="mt-8 text-center py-4">
          <div
            class="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-500 mx-auto mb-2"
          ></div>
          <p class="text-gray-500 text-sm">더 많은 여행 계획을 불러오는 중...</p>
        </div>

        <!-- 모든 데이터 로드 완료 -->
        <div v-if="hasReachedEnd && displayedTrips.length > 0" class="mt-8 text-center py-4">
          <p class="text-gray-500 text-sm">모든 여행 계획을 확인했습니다.</p>
        </div>
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
import { ref, onMounted, computed, watch, onUnmounted } from 'vue'
import { useUserStore } from '@/store/user'
import TripUploadModal from '@/components/trip/TripUploadModal.vue'
import api from '@/api'

const userStore = useUserStore()
const user = computed(() => userStore.loginUser)

const activeFilter = ref('all')
const searchTerm = ref('')
const selectedRegion = ref('')
const selectedTags = ref([])
const tempSelectedTags = ref([]) // 임시 선택된 태그들 (적용하기 전)
const showRegionFilter = ref(false)
const showTagFilter = ref(false)
const isUploadModalOpen = ref(false)
const isLoading = ref(false)
const isLoadingMore = ref(false)

// 무한 스크롤 관련 상태
const itemsPerPage = 9
const currentPage = ref(1)
const hasReachedEnd = ref(false)

const tabs = [
  { value: 'all', label: '전체' },
  { value: 'popular', label: '인기순' },
  { value: 'recent', label: '최신순' },
  { value: 'recommended', label: '추천순' },
]

const trips = ref([])
const sidos = ref([])
const allTags = ref([])

// 필터가 활성화되었는지 확인
const hasActiveFilters = computed(() => {
  return selectedRegion.value || selectedTags.value.length > 0 || searchTerm.value
})

// 필터링된 여행 계획 목록
const filteredTrips = computed(() => {
  let result = [...trips.value]

  // 지역 필터링
  if (selectedRegion.value) {
    result = result.filter((trip) => Number(trip.destination) === Number(selectedRegion.value))
  }

  // 태그 필터링
  if (selectedTags.value.length > 0) {
    result = result.filter((trip) => {
      return selectedTags.value.some((tag) => trip.tags.includes(tag))
    })
  }

  // 검색어 필터링
  if (searchTerm.value) {
    const term = searchTerm.value.toLowerCase()
    result = result.filter(
      (trip) =>
        trip.title.toLowerCase().includes(term) ||
        trip.tags.some((tag) => tag.toLowerCase().includes(term)),
    )
  }

  // 정렬 적용
  if (activeFilter.value === 'popular') {
    result.sort((a, b) => b.likes - a.likes)
  } else if (activeFilter.value === 'recent') {
    result.sort((a, b) => new Date(b.createdAt || 0) - new Date(a.createdAt || 0))
  } else if (activeFilter.value === 'recommended') {
    result.sort((a, b) => b.likes + b.shares - (a.likes + a.shares))
  }

  return result
})

// 현재 표시할 여행 계획들
const displayedTrips = computed(() => {
  return filteredTrips.value.slice(0, currentPage.value * itemsPerPage)
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

  const totalItems = filteredTrips.value.length
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

// 탭 변경
const changeTab = (tabValue) => {
  activeFilter.value = tabValue
  resetPagination()
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

// 임시 태그 토글
const toggleTempTag = (tag) => {
  if (tempSelectedTags.value.includes(tag)) {
    tempSelectedTags.value = tempSelectedTags.value.filter((t) => t !== tag)
  } else {
    tempSelectedTags.value.push(tag)
  }
}

// 태그 필터 적용
const applyTagFilter = () => {
  selectedTags.value = [...tempSelectedTags.value]
  showTagFilter.value = false
  resetPagination()
}

// 태그 필터 취소
const cancelTagFilter = () => {
  tempSelectedTags.value = [...selectedTags.value]
  showTagFilter.value = false
}

// 태그 제거
const removeTag = (tag) => {
  selectedTags.value = selectedTags.value.filter((t) => t !== tag)
  tempSelectedTags.value = tempSelectedTags.value.filter((t) => t !== tag)
  resetPagination()
}

// 검색어 초기화
const clearSearchTerm = () => {
  searchTerm.value = ''
  resetPagination()
}

// 모든 필터 초기화
const clearAllFilters = () => {
  selectedRegion.value = ''
  selectedTags.value = []
  tempSelectedTags.value = []
  searchTerm.value = ''
  resetPagination()
}

// 검색 실행
const searchTrips = () => {
  resetPagination()
}

// 외부 클릭 시 드롭다운 닫기
const handleClickOutside = (event) => {
  if (!event.target.closest('.relative')) {
    showRegionFilter.value = false
    showTagFilter.value = false
  }
}

// 모든 태그 가져오기
const fetchAllTags = async () => {
  try {
    const uniqueTags = new Set()

    trips.value.forEach((trip) => {
      if (trip.tags && Array.isArray(trip.tags)) {
        trip.tags.forEach((tag) => uniqueTags.add(tag))
      }
    })

    allTags.value = Array.from(uniqueTags)
  } catch (error) {
    console.error('태그 목록 조회 실패:', error)
    allTags.value = [
      '가족여행',
      '혼자여행',
      '커플여행',
      '맛집투어',
      '자연경관',
      '역사탐방',
      '쇼핑',
      '축제',
    ]
  }
}

// fetchTrips 함수
const fetchTrips = async () => {
  try {
    isLoading.value = true
    const response = await api.get(`/api/v1/plan/share`)
    const trip = response.data

    const enrichedTrips = await Promise.all(
      trip.map(async (tripPlan) => {
        const tagRes = await api.get(`/api/v1/tag/plan/${tripPlan.id}`)
        return {
          ...tripPlan,
          tags: tagRes.data,
          createdAt: tripPlan.createdAt || new Date().toISOString(),
        }
      }),
    )

    trips.value = enrichedTrips
    fetchAllTags()
  } catch (error) {
    console.error('여행 계획 조회 실패:', error)
  } finally {
    isLoading.value = false
  }
}

const fetchSidos = async () => {
  try {
    const response = await api.get('/api/v1/sidos')
    sidos.value = response.data
  } catch (error) {
    console.error('시도 목록 조회 실패:', error)
  }
}

const getSidoName = (sidoCode) => {
  if (!sidoCode || !sidos.value.length) return '지역 정보 없음'

  const sido = sidos.value.find((sido) => sido.sidoCode === sidoCode)
  return sido ? sido.sidoName : '지역 정보 없음'
}

const getSelectedRegionName = () => {
  if (!selectedRegion.value) return ''
  return getSidoName(Number(selectedRegion.value))
}

const openUploadModal = () => {
  isUploadModalOpen.value = true
}

const closeUploadModal = () => {
  isUploadModalOpen.value = false
}

const handleApplyTrip = async (selectedTrip) => {
  console.log('선택한 여행 계획:', selectedTrip)

  const response = await api.patch(`/api/v1/plan/updateShare/${selectedTrip.id}`)

  await fetchTrips()
  alert('여행 계획이 성공적으로 업로드되었습니다!')
}

// 필터 변경 감지하여 페이지네이션 리셋
watch([selectedRegion, selectedTags, searchTerm, activeFilter], () => {
  resetPagination()
})

onMounted(async () => {
  fetchTrips()
  fetchSidos()
  document.addEventListener('click', handleClickOutside)
  window.addEventListener('scroll', handleScroll)
})

onUnmounted(() => {
  document.removeEventListener('click', handleClickOutside)
  window.removeEventListener('scroll', handleScroll)
})
</script>
