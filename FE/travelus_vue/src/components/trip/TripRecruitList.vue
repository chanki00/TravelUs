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
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7"></path>
            </svg>
          </button>
          
          <!-- 지역 드롭다운 -->
          <div v-if="showRegionFilter" class="absolute top-full left-0 mt-1 w-full sm:w-48 bg-white border rounded-md shadow-lg z-10 max-h-60 overflow-y-auto">
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
                selectedRegion === sido.sidoCode ? 'bg-blue-50 text-blue-600' : ''
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
              {{ tempSelectedTags.length > 0 ? `태그 ${tempSelectedTags.length}개 선택` : '태그 선택' }}
            </span>
            <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7"></path>
            </svg>
          </button>
          
          <!-- 태그 드롭다운 -->
          <div v-if="showTagFilter" class="absolute top-full left-0 mt-1 w-full sm:w-64 bg-white border rounded-md shadow-lg z-10">
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
                      : 'bg-gray-100 text-gray-700 hover:bg-gray-200'
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
          v-for="tag in selectedTags" 
          :key="tag" 
          class="flex items-center bg-blue-50 px-3 py-1 rounded-full"
        >
          <span class="text-sm text-blue-700">{{ tag }}</span>
          <button @click="removeTag(tag)" class="ml-2 text-blue-700 hover:text-blue-900">
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

    <!-- 검색 결과 메시지 -->
    <div v-if="hasActiveFilters" class="mb-4 text-gray-600">
      {{ filteredPosts.length }}개의 검색 결과가 있습니다.
    </div>

    <div v-if="isLoading" class="text-center py-8">
      <div
        class="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-500 mx-auto mb-4"
      ></div>
      <p class="text-gray-500">모집글을 불러오는 중...</p>
    </div>

    <div
      v-else-if="filteredPosts.length === 0"
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
        v-for="post in filteredPosts"
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
import { ref, onMounted, computed } from 'vue'
import api from '@/api'
import { MapPin as MapPinIcon, Users as UsersIcon } from 'lucide-vue-next'
import TripDetailModal from './TripDetailModal.vue'

// 상태 관리
const recruitPosts = ref([])
const isLoading = ref(false)
const sidos = ref([])
const showTripDetails = ref(false)
const selectedPost = ref(null)

// 필터 상태
const selectedRegion = ref('')
const selectedTags = ref([])
const tempSelectedTags = ref([]) // 임시 선택된 태그들 (적용하기 전)
const showRegionFilter = ref(false)
const showTagFilter = ref(false)
const allTags = ref([])

// 필터가 활성화되었는지 확인
const hasActiveFilters = computed(() => {
  return selectedRegion.value || selectedTags.value.length > 0
})

// 필터링된 모집글 목록
const filteredPosts = computed(() => {
  let result = [...recruitPosts.value]
  
  // 지역 필터링
  if (selectedRegion.value) {
    result = result.filter(post => {
      return post.plan && Number(post.plan.destination) === Number(selectedRegion.value)
    })
  }
  
  // 태그 필터링
  if (selectedTags.value.length > 0) {
    result = result.filter(post => {
      if (!post.tags || post.tags.length === 0) return false
      return selectedTags.value.some(tag => post.tags.includes(tag))
    })
  }
  
  return result
})

// 지역 선택
const selectRegion = (regionCode) => {
  selectedRegion.value = regionCode
  showRegionFilter.value = false
}

// 지역 필터 초기화
const clearRegionFilter = () => {
  selectedRegion.value = ''
}

// 임시 태그 토글
const toggleTempTag = (tag) => {
  if (tempSelectedTags.value.includes(tag)) {
    tempSelectedTags.value = tempSelectedTags.value.filter(t => t !== tag)
  } else {
    tempSelectedTags.value.push(tag)
  }
}

// 태그 필터 적용
const applyTagFilter = () => {
  selectedTags.value = [...tempSelectedTags.value]
  showTagFilter.value = false
}

// 태그 필터 취소
const cancelTagFilter = () => {
  tempSelectedTags.value = [...selectedTags.value]
  showTagFilter.value = false
}

// 태그 제거
const removeTag = (tag) => {
  selectedTags.value = selectedTags.value.filter(t => t !== tag)
  tempSelectedTags.value = tempSelectedTags.value.filter(t => t !== tag)
}

// 모든 필터 초기화
const clearAllFilters = () => {
  selectedRegion.value = ''
  selectedTags.value = []
  tempSelectedTags.value = []
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
    // 모든 모집글에서 사용된 태그를 추출하여 중복 제거
    const uniqueTags = new Set()
    
    recruitPosts.value.forEach(post => {
      if (post.tags && Array.isArray(post.tags)) {
        post.tags.forEach(tag => uniqueTags.add(tag))
      }
    })
    
    allTags.value = Array.from(uniqueTags)
  } catch (error) {
    console.error('태그 목록 조회 실패:', error)
    // 기본 태그 제공
    allTags.value = ['가족여행', '혼자여행', '커플여행', '맛집투어', '자연경관', '역사탐방', '쇼핑', '축제']
  }
}

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
    
    // 모든 태그 가져오기
    fetchAllTags()
    
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

// 컴포넌트 마운트 시 모집글과 시도 목록 가져오기
onMounted(() => {
  fetchRecruitPosts()
  fetchSidos()
  document.addEventListener('click', handleClickOutside)
})
</script>
