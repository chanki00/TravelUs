<template>
  <div class="h-full flex flex-col">
    <!-- Tab navigation - 고정 영역 -->
    <div class="flex border-b">
      <button
        v-for="(label, tab) in tabLabels"
        :key="tab"
        :class="[
          'flex-1 py-2 px-4 text-center',
          activeTab === tab ? 'border-b-2 border-blue-600 text-blue-600' : 'text-gray-500',
        ]"
        @click="activeTab = tab"
      >
        {{ label }}
      </button>
    </div>

    <!-- Search input - 고정 영역 -->
    <div class="mt-4">
      <div class="flex items-center gap-2 mb-4">
        <div class="flex-1 relative">
          <input
            type="text"
            placeholder="장소 검색하기"
            class="w-full px-3 py-2 pl-10 border rounded-md"
          />
          <span class="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400">
            <svg
              xmlns="http://www.w3.org/2000/svg"
              width="16"
              height="16"
              viewBox="0 0 24 24"
              fill="none"
              stroke="currentColor"
              stroke-width="2"
              stroke-linecap="round"
              stroke-linejoin="round"
              class="lucide lucide-search"
            >
              <circle cx="11" cy="11" r="8"></circle>
              <path d="m21 21-4.3-4.3"></path>
            </svg>
          </span>
        </div>
        <button
          class="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 transition-colors"
        >
          검색
        </button>
      </div>
    </div>

    <!-- Loading indicator for initial load -->
    <div v-if="isLoading" class="flex justify-center items-center py-8">
      <div class="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-600"></div>
    </div>

    <!-- Results area - 스크롤 영역 -->
    <div ref="scrollContainer" class="mt-2 flex-1 overflow-auto custom-scrollbar">
      <div v-if="!isLoading && visiblePlaces.length === 0" class="text-center py-8 text-gray-500">
        검색 결과가 없습니다.
      </div>
      <div v-else-if="!isLoading" class="space-y-3 pr-2">
        <div
          v-for="place in visiblePlaces"
          :key="place.contentId"
          class="border rounded-md p-3 flex items-center gap-3 cursor-pointer hover:border-blue-200 transition-colors"
        >
          <div class="flex-1 flex items-center gap-3" @click="showPlaceDetail(place)">
            <img :src="place.image" :alt="place.name" class="w-16 h-16 object-cover rounded-md" />
            <div>
              <h4 class="font-medium">{{ place.title }}</h4>
              <div class="flex items-center gap-1 text-sm text-gray-500">
                <span>{{ place.category }}</span>
                <span>•</span>
                <span>★ {{ place.rating }}</span>
              </div>
            </div>
          </div>
          <button
            @click="addToItinerary(place)"
            class="p-2 text-blue-600 hover:bg-blue-50 rounded-full transition-colors"
            title="일정에 추가"
          >
            <plus-icon class="h-5 w-5" />
          </button>
        </div>

        <!-- 스크롤 감지를 위한 요소 -->
        <div ref="loadMoreTrigger" class="h-20 flex justify-center items-center my-2">
          <div
            v-if="isLoadingMore"
            class="animate-spin rounded-full h-6 w-6 border-b-2 border-blue-600"
          ></div>
          <div v-else-if="visibleCount < getTotalCount" class="text-sm text-gray-400">
            스크롤하여 더 보기
          </div>
          <div v-else class="text-sm text-gray-400">모든 항목을 불러왔습니다</div>
        </div>
      </div>
    </div>

    <!-- Place Detail Modal -->
    <PlaceDetailModal
      v-if="selectedPlace"
      :place="selectedPlace"
      @close="selectedPlace = null"
      @add-to-itinerary="addToItinerary(selectedPlace)"
    />
  </div>
</template>

<script setup>
import { ref, onMounted, watch, computed, onBeforeUnmount, nextTick } from 'vue'
import api from '@/api'
import { Plus as PlusIcon } from 'lucide-vue-next'
import PlaceDetailModal from './PlaceDetailModal.vue'
import { defineProps, defineEmits } from 'vue'

const props = defineProps({
  destination: {
    type: Number,
  },
})

const activeTab = ref('attractions')
const isLoading = ref(false)
const isLoadingMore = ref(false)
const selectedPlace = ref(null)
const emit = defineEmits(['add-to-itinerary'])
const scrollContainer = ref(null)
const loadMoreTrigger = ref(null)

const tabLabels = {
  attractions: '명소',
  restaurants: '식당',
  accommodations: '숙소',
}

const rawPlaces = ref([])
const filteredPlaces = ref({
  attractions: [],
  restaurants: [],
  accommodations: [],
})

// 무한 스크롤 관련
const visibleCount = ref(10)
const pageSize = 10

const visiblePlaces = computed(() => {
  const list = filteredPlaces.value[activeTab.value] || []
  return list.slice(0, visibleCount.value)
})

// 탭 바뀔 때마다 보여줄 개수 초기화
watch(activeTab, () => {
  visibleCount.value = 10
  // 탭 변경 시 스크롤 맨 위로
  if (scrollContainer.value) {
    scrollContainer.value.scrollTop = 0
  }
  // 탭 변경 후 스크롤 감지 재설정
  nextTick(() => {
    setupScrollDetection()
  })
})

// 데이터 로딩
const fetchAttractions = async () => {
  try {
    isLoading.value = true
    const res = await api.get(`/api/v1/attractions/${props.destination}`)
    rawPlaces.value = res.data
    console.log(props.destination)
    filteredPlaces.value.attractions = rawPlaces.value.filter((p) => p.contentTypeId === 12)
    filteredPlaces.value.restaurants = rawPlaces.value.filter((p) => p.contentTypeId === 39)
    filteredPlaces.value.accommodations = rawPlaces.value.filter((p) => p.contentTypeId === 32)
  } catch (err) {
    console.error('명소 데이터를 불러오는 데 실패했습니다:', err)
  } finally {
    isLoading.value = false
    // 데이터 로딩 후 스크롤 감지 설정
    nextTick(() => {
      setupScrollDetection()
    })
  }
}
// destination 변경 시 재요청
// watch(() => props.destination, fetchAttractions, { immediate: true })

// 장소 상세
const showPlaceDetail = (place) => {
  selectedPlace.value = place
}

// 일정 추가
const addToItinerary = (place) => {
  emit('add-to-itinerary', {
    id: place.id ?? place.contentId,
    title: place.name ?? place.title,
    type: place.category ?? '',
    time: new Date().toLocaleTimeString('ko-KR', { hour: '2-digit', minute: '2-digit' }),
    image: place.image ?? '',
    placeData: place,
  })
}

// 스크롤 이벤트 리스너
const handleScroll = () => {
  if (!scrollContainer.value || !loadMoreTrigger.value || isLoadingMore.value) return

  const containerRect = scrollContainer.value.getBoundingClientRect()
  const triggerRect = loadMoreTrigger.value.getBoundingClientRect()

  // 트리거 요소가 컨테이너 내에 보이는지 확인
  if (triggerRect.top <= containerRect.bottom + 100) {
    loadMore()
  }
}

// IntersectionObserver 설정
let observer = null

const setupScrollDetection = () => {
  // 기존 observer 제거
  if (observer) {
    observer.disconnect()
  }

  // 스크롤 이벤트 리스너 설정
  if (scrollContainer.value) {
    scrollContainer.value.addEventListener('scroll', handleScroll)
  }

  // IntersectionObserver 설정
  observer = new IntersectionObserver(
    (entries) => {
      if (entries[0].isIntersecting && !isLoadingMore.value) {
        console.log('스크롤 끝 감지: 추가 데이터 로드 시작')
        loadMore()
      }
    },
    {
      root: scrollContainer.value,
      threshold: 0.1,
      rootMargin: '100px',
    },
  )

  // 로드 트리거 요소 관찰 시작
  if (loadMoreTrigger.value) {
    observer.observe(loadMoreTrigger.value)
    console.log('스크롤 감지 설정 완료')
  }
}

onMounted(() => {
  // 컴포넌트 마운트 후 스크롤 감지 설정
  fetchAttractions()

  nextTick(() => {
    setupScrollDetection()
  })
})

onBeforeUnmount(() => {
  // 컴포넌트 언마운트 시 정리
  if (observer) {
    observer.disconnect()
  }

  if (scrollContainer.value) {
    scrollContainer.value.removeEventListener('scroll', handleScroll)
  }
})

const loadMore = async () => {
  const total = filteredPlaces.value[activeTab.value]?.length || 0
  console.log(`현재 표시 항목: ${visibleCount.value}/${total}`)

  if (visibleCount.value < total && !isLoadingMore.value) {
    console.log('추가 항목 로딩 시작')
    isLoadingMore.value = true

    try {
      // 실제 API 호출이 있다면 여기서 처리
      // 시뮬레이션을 위한 짧은 지연
      await new Promise((resolve) => setTimeout(resolve, 500))

      // 항목 추가
      visibleCount.value += pageSize
      console.log(`항목 추가 완료: ${visibleCount.value}/${total}`)
    } catch (error) {
      console.error('추가 항목 로딩 실패:', error)
    } finally {
      isLoadingMore.value = false
    }
  } else {
    console.log('더 이상 로드할 항목이 없거나 이미 로딩 중입니다')
  }
}

const getTotalCount = computed(() => {
  return filteredPlaces.value[activeTab.value]?.length || 0
})
</script>

<style scoped>
.custom-scrollbar {
  height: 100%;
  overflow-y: auto;
  overflow-x: hidden;
  -webkit-overflow-scrolling: touch; /* iOS 스크롤 개선 */
}
.custom-scrollbar::-webkit-scrollbar {
  width: 6px;
}
.custom-scrollbar::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 10px;
}
.custom-scrollbar::-webkit-scrollbar-thumb {
  background: #c5c5c5;
  border-radius: 10px;
}
.custom-scrollbar::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}
.transition-colors {
  transition: all 0.2s ease;
}
</style>
