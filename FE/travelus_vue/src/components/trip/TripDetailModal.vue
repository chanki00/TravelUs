<template>
  <div v-if="show" class="fixed inset-0 z-50 flex items-center justify-center">
    <!-- Backdrop -->
    <div class="absolute inset-0 bg-black/50" @click="closeModal"></div>

    <!-- Content -->
    <div
      class="relative bg-white rounded-lg shadow-lg w-full max-w-3xl max-h-[90vh] overflow-hidden flex flex-col"
    >
      <div class="p-4 border-b">
        <h2 class="text-xl font-medium">{{ post.title }}</h2>
        <div class="flex items-center gap-2 text-sm text-gray-500">
          <map-pin-icon class="h-4 w-4" />
          {{ post.plan ? getSidoName(Number(post.plan.destination)) : '지역 정보 없음' }}
          <span class="mx-2">•</span>
          <clock-icon class="h-4 w-4" /> {{ formatDateRange(post.startDate, post.endDate) }}
        </div>
      </div>

      <div class="p-6 overflow-y-auto">
        <div class="aspect-video rounded-md overflow-hidden mb-4">
          <img
            :src="
              post.plan && post.plan.image
                ? post.plan.image
                : 'https://images.unsplash.com/photo-1476514525535-07fb3b4ae5f1?q=80&w=1000&auto=format&fit=crop'
            "
            :alt="post.title"
            class="w-full h-full object-cover"
          />
        </div>

        <div class="mb-4">
          <div class="font-medium mb-2">여행 태그</div>
          <div class="flex flex-wrap gap-1">
            <span
              v-for="tag in post.tags || []"
              :key="tag"
              class="px-2 py-0.5 bg-gray-100 text-gray-700 text-xs rounded-full"
            >
              {{ tag }}
            </span>
          </div>
        </div>

        <div class="mb-4">
          <div class="font-medium mb-2">여행 소개</div>
          <p class="text-gray-600">{{ post.content }}</p>
        </div>

        <div v-if="post.plan && post.plan.description" class="mb-4">
          <div class="font-medium mb-2">여행 계획 설명</div>
          <p class="text-gray-600">{{ post.plan.description }}</p>
        </div>

        <!-- 여행 일정 탭 -->
        <div v-if="itinerary.length > 0" class="mb-4">
          <div class="font-medium mb-2">여행 일정</div>

          <!-- 일차 탭 -->
          <div class="border-b mb-4">
            <div class="flex overflow-x-auto">
              <button
                v-for="(day, index) in itinerary"
                :key="index"
                :class="[
                  'px-4 py-2 text-center whitespace-nowrap',
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

          <!-- 일차별 일정 -->
          <div v-for="(day, dayIndex) in itinerary" :key="dayIndex" v-show="activeDay === dayIndex">
            <div class="bg-white rounded-lg border shadow-sm overflow-hidden">
              <div class="p-3 border-b bg-gray-50">
                <h3 class="font-medium">Day {{ dayIndex + 1 }}</h3>
              </div>
              <div class="p-4">
                <div class="space-y-6">
                  <div v-for="(item, itemIndex) in day.items" :key="itemIndex" class="flex gap-4">
                    <div class="w-16 text-sm font-medium text-gray-500">{{ item.time || '' }}</div>
                    <div class="relative">
                      <div
                        class="w-3 h-3 rounded-full bg-blue-500 absolute left-0 top-2.5 -translate-x-[7px]"
                      ></div>
                      <div
                        class="border-l-2 border-gray-200 pl-4 ml-0.5 pb-6 last:border-l-0 last:pb-0"
                      >
                        <h4 class="font-medium">{{ item.title }}</h4>
                        <p class="text-sm text-gray-500 mt-1">{{ item.description || '' }}</p>
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
        </div>

        <div class="mb-4">
          <div class="font-medium mb-2">참여 현황</div>
          <div class="flex items-center gap-1">
            <users-icon class="h-4 w-4" />
            <span>
              {{ post.currentMembers }}/{{ post.max_members }}명
              {{
                post.currentMembers < post.max_members
                  ? ` (${post.max_members - post.currentMembers}명 더 모집 중)`
                  : ' (모집 완료)'
              }}
            </span>
          </div>
        </div>

        <div class="mb-4">
          <div class="font-medium mb-2">여행 정보</div>
          <div class="grid grid-cols-2 gap-4">
            <div class="p-3 bg-gray-50 rounded-md">
              <div class="text-sm text-gray-500 mb-1">여행 기간</div>
              <div>{{ getDurationDays() }}일</div>
            </div>
            <div class="p-3 bg-gray-50 rounded-md">
              <div class="text-sm text-gray-500 mb-1">이동 수단</div>
              <div>{{ getTransportName() }}</div>
            </div>
          </div>
        </div>

        <div>
          <div class="font-medium mb-2">호스트 정보</div>
          <div class="flex items-center gap-2">
            <div class="w-8 h-8 rounded-full bg-blue-100 flex items-center justify-center">
              {{ post.userId ? post.userId.toString().slice(0, 1) : '?' }}
            </div>
            <span>호스트 ID: {{ post.userId }}</span>
          </div>
        </div>
      </div>

      <div class="p-4 border-t flex justify-end gap-2 mt-auto">
        <button class="px-4 py-2 border rounded-md hover:bg-gray-50" @click="closeModal">
          닫기
        </button>
        <button
          class="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700"
          :disabled="post.currentMembers >= post.max_members"
          @click="joinTrip"
        >
          {{ post.currentMembers >= post.max_members ? '모집 완료' : '참여하기' }}
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, defineProps, defineEmits, onMounted, computed, watch } from 'vue'
import api from '@/api'
import { MapPin as MapPinIcon, Users as UsersIcon, Clock as ClockIcon } from 'lucide-vue-next'

const props = defineProps({
  show: {
    type: Boolean,
    default: false,
  },
  post: {
    type: Object,
    default: () => ({}),
  },
})

const emit = defineEmits(['close'])
const sidos = ref([])
const itinerary = ref([])
const activeDay = ref(0)

// 모달 닫기
const closeModal = () => {
  emit('close')
}

// 여행 참여하기
const joinTrip = async () => {
  try {
    // 여기에 참여 API 호출 로직 구현
    alert('참여 신청이 완료되었습니다.')
    closeModal()
  } catch (error) {
    console.error('참여 신청 실패:', error)
    alert('참여 신청에 실패했습니다. 다시 시도해주세요.')
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

// 여행 일정 가져오기
const fetchItinerary = async () => {
  if (!props.post || !props.post.planId) return

  try {
    // 일정 데이터 가져오기
    const itineraryRes = await api.get(`/api/v1/plan/itinerary/${props.post.planId}`)

    // 컨텐츠 타입 매핑
    const typeMap = {
      12: '관광',
      39: '식당',
      32: '숙박',
      12: '관광',
      39: '식당',
      32: '숙박',
    }

    // 날짜별로 그룹핑
    const grouped = itineraryRes.data.reduce((acc, cur) => {
      const dayNumber = cur.dayNumber - 1 // 0부터 시작하도록 조정
      if (!acc[dayNumber]) acc[dayNumber] = []

      acc[dayNumber].push({
        time: '',
        title: cur.title,
        description: cur.memo || '',
        type: typeMap[cur.contentTypeId] || '기타',
        placeData: {
          latitude: cur.latitude,
          longitude: cur.longitude,
          contentTypeId: cur.contentTypeId,
          title: cur.title,
          addr: cur.addr,
          image: cur.image,
        },
      })

      return acc
    }, {})

    // 일정 데이터 구성
    const days = props.post.plan ? props.post.plan.duration : 0
    itinerary.value = []

    // 모든 날짜에 대해 빈 배열 초기화
    for (let i = 0; i < days; i++) {
      itinerary.value.push({
        day: i,
        title: `Day ${i + 1}`,
        items: grouped[i] || [],
      })
    }

    // 일정이 없는 경우 임시 데이터 생성
    if (itinerary.value.length === 0) {
      const mockDays = props.post.plan ? props.post.plan.duration : 0
      for (let i = 0; i < mockDays; i++) {
        itinerary.value.push({
          day: i,
          title: `Day ${i + 1}`,
          items: [
            {
              time: '',
              title: `${i + 1}일차 여행 일정`,
              description: '상세 일정이 등록되지 않았습니다.',
              type: '기타',
            },
          ],
        })
      }
    }
  } catch (error) {
    console.error('여행 일정 가져오기 실패:', error)

    // API 호출 실패 시 임시 데이터 생성
    const days = props.post.plan ? props.post.plan.duration : 0
    itinerary.value = []

    for (let i = 0; i < days; i++) {
      itinerary.value.push({
        day: i,
        title: `Day ${i + 1}`,
        items: [
          {
            time: '',
            title: `${i + 1}일차 여행 일정`,
            description: '상세 일정을 불러올 수 없습니다.',
            type: '기타',
          },
        ],
      })
    }
  }
}

// 시도 코드로 시도 이름 가져오기
const getSidoName = (sidoCode) => {
  if (!sidoCode || !sidos.value.length) return '지역 정보 없음'

  const sido = sidos.value.find((sido) => sido.sidoCode === sidoCode)
  return sido ? sido.sidoName : '지역 정보 없음'
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

// 여행 기간 일수 계산
const getDurationDays = () => {
  if (!props.post.startDate || !props.post.endDate) {
    return props.post.plan ? props.post.plan.duration : '정보 없음'
  }

  try {
    const start = new Date(props.post.startDate)
    const end = new Date(props.post.endDate)

    if (isNaN(start.getTime()) || isNaN(end.getTime())) return '정보 없음'

    // 날짜 차이 계산 (밀리초 -> 일)
    const diffTime = Math.abs(end - start)
    const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24)) + 1 // 당일 포함

    return diffDays
  } catch (error) {
    console.error('여행 기간 계산 오류:', error)
    return '정보 없음'
  }
}

// 이동 수단 이름 가져오기
const getTransportName = () => {
  if (!props.post.plan || !props.post.plan.transport) return '정보 없음'

  const transportMap = {
    car: '자동차/렌트카',
    public: '대중교통',
    walk: '도보/자전거',
    tour: '투어버스',
  }

  return transportMap[props.post.plan.transport] || props.post.plan.transport
}

// post 변경 시 일정 다시 가져오기
watch(
  () => props.post,
  () => {
    if (props.show && props.post) {
      fetchItinerary()
      activeDay.value = 0 // 일정 변경 시 첫 번째 날짜로 초기화
    }
  },
  { deep: true },
)

// 컴포넌트 마운트 시 시도 목록 가져오기
onMounted(() => {
  fetchSidos()
  if (props.show && props.post) {
    fetchItinerary()
  }
})
</script>
