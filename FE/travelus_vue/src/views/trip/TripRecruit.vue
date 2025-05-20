<template>
  <div class="min-h-screen bg-gradient-to-b from-blue-50 to-white">

    <div class="py-8 px-6 max-w-7xl mx-auto">
      <h1 class="text-3xl font-bold mb-2">여행 인원 모집</h1>
      <p class="text-gray-500 mb-8">함께 여행할 동행자를 모집하거나 참여해보세요</p>

      <div class="mb-6">
        <div class="border-b mb-6">
          <div class="flex">
            <button 
              v-for="tab in tabs" 
              :key="tab.value"
              :class="[
                'px-4 py-2',
                activeTab === tab.value ? 'border-b-2 border-blue-600 text-blue-600' : 'text-gray-500'
              ]"
              @click="activeTab = tab.value"
            >
              {{ tab.label }}
            </button>
          </div>
        </div>

        <!-- 모집글 보기 탭 -->
        <div v-if="activeTab === 'browse'">
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
                            : 'bg-gray-100 text-gray-700 hover:bg-gray-200'
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
              <button class="px-4 py-2 border rounded-md hover:bg-gray-50">검색</button>
            </div>

            <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
              <div 
                v-for="trip in mockTrips" 
                :key="trip.id"
                class="bg-white rounded-lg border shadow-sm overflow-hidden cursor-pointer hover:shadow-md transition-shadow"
                @click="openTripDetails(trip)"
              >
                <div class="h-48 overflow-hidden">
                  <img :src="trip.imageUrl" :alt="trip.title" class="w-full h-full object-cover object-center" />
                </div>
                <div class="p-4 border-b">
                  <div class="flex justify-between items-start">
                    <h3 class="text-lg font-medium">{{ trip.title }}</h3>
                  </div>
                  <div class="flex items-center text-sm text-gray-500">
                    <map-pin-icon class="h-3 w-3 mr-1" /> {{ trip.location }}
                  </div>
                </div>
                <div class="p-4">
                  <div class="flex flex-wrap gap-1 mb-3">
                    <span 
                      v-for="tag in trip.tags" 
                      :key="tag"
                      class="px-2 py-0.5 bg-gray-100 text-gray-700 text-xs rounded-full"
                    >
                      {{ tag }}
                    </span>
                  </div>
                  <div class="flex items-center justify-between text-sm mb-3">
                    <div class="text-gray-500">{{ trip.dates }}</div>
                    <div class="flex items-center">
                      <users-icon class="h-3 w-3 mr-1" />
                      <span>
                        {{ trip.memberCount }}/{{ trip.maxMembers }}명
                      </span>
                    </div>
                  </div>
                  <p class="text-sm text-gray-500 line-clamp-2">{{ trip.description }}</p>
                </div>
                <div class="p-4 border-t flex justify-between">
                  <div class="text-sm">호스트: {{ trip.host }}</div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- 모집글 작성 탭 -->
        <div v-if="activeTab === 'create'">
          <div class="bg-white rounded-lg border shadow-sm overflow-hidden">
            <div class="p-4 border-b">
              <h3 class="text-lg font-medium">여행 인원 모집하기</h3>
            </div>
            <div class="p-6 space-y-4">
              <div class="space-y-2">
                <label for="title" class="block text-sm font-medium text-gray-700">제목</label>
                <input 
                  id="title" 
                  v-model="recruitForm.title"
                  placeholder="모집글 제목을 입력하세요" 
                  class="w-full px-3 py-2 border rounded-md"
                />
              </div>

              <div class="space-y-2">
                <label for="location" class="block text-sm font-medium text-gray-700">여행지</label>
                <input 
                  id="location" 
                  v-model="recruitForm.location"
                  placeholder="국내 여행지를 입력하세요" 
                  class="w-full px-3 py-2 border rounded-md"
                />
              </div>

              <div class="space-y-2">
                <label class="block text-sm font-medium text-gray-700">여행 기간</label>
                <div class="flex gap-2">
                  <div class="relative flex-1">
                    <input 
                      type="date" 
                      v-model="recruitForm.startDate"
                      class="w-full px-3 py-2 border rounded-md"
                    />
                  </div>
                  <div class="relative flex-1">
                    <input 
                      type="date" 
                      v-model="recruitForm.endDate"
                      class="w-full px-3 py-2 border rounded-md"
                    />
                  </div>
                </div>
              </div>

              <div class="space-y-2">
                <label for="maxMembers" class="block text-sm font-medium text-gray-700">모집 인원</label>
                <input 
                  id="maxMembers" 
                  type="number" 
                  min="1" 
                  max="10" 
                  v-model="recruitForm.maxMembers"
                  placeholder="최대 인원수" 
                  class="w-full px-3 py-2 border rounded-md"
                />
              </div>

              <div class="space-y-2">
                <label class="block text-sm font-medium text-gray-700">여행 태그 선택 (최대 5개)</label>
                <div class="flex flex-wrap gap-2">
                  <span 
                    v-for="tag in availableTags" 
                    :key="tag"
                    :class="[
                      'px-2 py-1 text-xs rounded-full cursor-pointer',
                      recruitForm.tags.includes(tag) 
                        ? 'bg-blue-600 text-white' 
                        : 'bg-gray-100 text-gray-700 hover:bg-gray-200'
                    ]"
                    @click="toggleRecruitTag(tag)"
                  >
                    <check-icon v-if="recruitForm.tags.includes(tag)" class="inline-block h-3 w-3 mr-1" />
                    {{ tag }}
                  </span>
                </div>
              </div>

              <div class="space-y-2">
                <label for="description" class="block text-sm font-medium text-gray-700">여행 소개 및 동행자 요구사항</label>
                <textarea
                  id="description"
                  v-model="recruitForm.description"
                  placeholder="여행 계획과 함께하고 싶은 동행자에 대한 설명을 작성해주세요"
                  rows="5"
                  class="w-full px-3 py-2 border rounded-md"
                ></textarea>
              </div>

              <div class="space-y-2">
                <label for="plan" class="block text-sm font-medium text-gray-700">여행 계획</label>
                <div class="p-4 border rounded-md text-sm text-gray-500">
                  <p class="mb-2">저장된 여행 계획을 추가하거나 새롭게 작성할 수 있습니다</p>
                  <div class="flex gap-2">
                    <button class="px-3 py-1.5 text-sm border rounded-md hover:bg-gray-50">
                      저장된 계획 불러오기
                    </button>
                    <button class="px-3 py-1.5 text-sm border rounded-md hover:bg-gray-50">
                      새 계획 만들기
                    </button>
                  </div>
                </div>
              </div>
            </div>
            <div class="p-4 border-t">
              <button class="w-full px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700">
                모집 등록하기
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 여행 모집 상세 모달 -->
    <div v-if="showTripDetails" class="fixed inset-0 z-50 flex items-center justify-center">
      <!-- Backdrop -->
      <div class="absolute inset-0 bg-black/50" @click="showTripDetails = false"></div>
      
      <!-- Content -->
      <div class="relative bg-white rounded-lg shadow-lg w-full max-w-3xl overflow-hidden">
        <div class="p-4 border-b">
          <h2 class="text-xl font-medium">{{ selectedTrip.title }}</h2>
          <div class="flex items-center gap-2 text-sm text-gray-500">
            <map-pin-icon class="h-4 w-4" /> {{ selectedTrip.location }}
            <span class="mx-2">•</span>
            <clock-icon class="h-4 w-4" /> {{ selectedTrip.dates }}
          </div>
        </div>
        
        <div class="p-6">
          <div class="aspect-video rounded-md overflow-hidden mb-4">
            <img :src="selectedTrip.imageUrl" :alt="selectedTrip.title" class="w-full h-full object-cover" />
          </div>

          <div class="mb-4">
            <div class="font-medium mb-2">여행 태그</div>
            <div class="flex flex-wrap gap-1">
              <span 
                v-for="tag in selectedTrip.tags" 
                :key="tag"
                class="px-2 py-0.5 bg-gray-100 text-gray-700 text-xs rounded-full"
              >
                {{ tag }}
              </span>
            </div>
          </div>

          <div class="mb-4">
            <div class="font-medium mb-2">여행 소개</div>
            <p class="text-gray-600">{{ selectedTrip.description }}</p>
          </div>

          <div class="mb-4">
            <div class="font-medium mb-2">여행 일정</div>
            <div class="space-y-3">
              <div 
                v-for="day in selectedTrip.schedule" 
                :key="day.day"
                class="p-3 border rounded-md"
              >
                <div class="font-medium mb-1">{{ day.day }}일차</div>
                <ul class="list-disc pl-5 text-sm text-gray-600">
                  <li v-for="(activity, i) in day.activities" :key="i">{{ activity }}</li>
                </ul>
              </div>
            </div>
          </div>

          <div class="mb-4">
            <div class="font-medium mb-2">참여 현황</div>
            <div class="flex items-center gap-1">
              <users-icon class="h-4 w-4" />
              <span>
                {{ selectedTrip.memberCount }}/{{ selectedTrip.maxMembers }}명
                {{ selectedTrip.memberCount < selectedTrip.maxMembers 
                  ? ` (${selectedTrip.maxMembers - selectedTrip.memberCount}명 더 모집 중)` 
                  : " (모집 완료)" }}
              </span>
            </div>
          </div>

          <div>
            <div class="font-medium mb-2">호스트 정보</div>
            <div class="flex items-center gap-2">
              <div class="w-8 h-8 rounded-full bg-blue-100 flex items-center justify-center">
                {{ selectedTrip.host.slice(0, 1) }}
              </div>
              <span>{{ selectedTrip.host }}</span>
            </div>
          </div>
        </div>
        
        <div class="p-4 border-t flex justify-end gap-2">
          <button 
            class="px-4 py-2 border rounded-md hover:bg-gray-50"
            @click="showTripDetails = false"
          >
            닫기
          </button>
          <button 
            class="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700"
            :disabled="selectedTrip.memberCount >= selectedTrip.maxMembers"
          >
            {{ selectedTrip.memberCount >= selectedTrip.maxMembers ? "모집 완료" : "참여하기" }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { 
  Tag as TagIcon, 
  MapPin as MapPinIcon, 
  Users as UsersIcon, 
  Clock as ClockIcon,
  Check as CheckIcon
} from 'lucide-vue-next'

// 상태 관리
const activeTab = ref('browse')
const searchTerm = ref('')
const showTagFilter = ref(false)
const selectedTags = ref([])
const showTripDetails = ref(false)
const selectedTrip = ref(null)

// 모집글 작성 폼
const recruitForm = ref({
  title: '',
  location: '',
  startDate: '',
  endDate: '',
  maxMembers: 4,
  tags: [],
  description: ''
})

// 탭 정의
const tabs = [
  { value: 'browse', label: '모집글 보기' },
  { value: 'create', label: '모집글 작성' }
]

// 사용 가능한 태그
const availableTags = [
  "식도락",
  "자연",
  "문화",
  "쇼핑",
  "힐링",
  "액티비티",
  "사진",
  "도시",
  "가족",
  "커플",
  "친구",
  "혼자",
  "저예산",
  "럭셔리",
]

// 목업 데이터
const mockTrips = [
  {
    id: 1,
    title: "제주도 3박 4일 힐링 여행",
    host: "여행좋아",
    dates: "2025-06-15 ~ 2025-06-18",
    location: "제주도",
    tags: ["힐링", "자연", "맛집"],
    memberCount: 2,
    maxMembers: 4,
    description:
      "제주도 동쪽을 중심으로 맛집과 카페를 둘러볼 예정입니다. 렌트카로 이동하며 사진 촬영을 좋아하시는 분들과 함께하고 싶어요.",
    imageUrl: "https://images.unsplash.com/photo-1575482288877-5cd70c6e3704?q=80&w=1000&auto=format&fit=crop",
    schedule: [
      { day: 1, activities: ["우도 여행", "섭지코지 일출", "카페 투어"] },
      { day: 2, activities: ["올레길 걷기", "만장굴 탐험", "흑돼지 맛집"] },
      { day: 3, activities: ["한라산 등반", "해변 산책", "해산물 저녁"] },
    ],
  },
  {
    id: 2,
    title: "부산 2박 3일 맛집 탐방",
    host: "푸드러버",
    dates: "2025-05-22 ~ 2025-05-24",
    location: "부산",
    tags: ["식도락", "도시", "해변"],
    memberCount: 1,
    maxMembers: 3,
    description: "부산 해운대와 광안리 주변 맛집을 탐방할 예정입니다. 부산 여행 경험이 있으시면 더 좋습니다.",
    imageUrl: "https://images.unsplash.com/photo-1617243689822-a2cbe7eae4ed?q=80&w=1000&auto=format&fit=crop",
    schedule: [
      { day: 1, activities: ["해운대 해변", "광안리 맛집", "야경 감상"] },
      { day: 2, activities: ["자갈치 시장", "남포동 쇼핑", "서면 맛집"] },
    ],
  },
  {
    id: 3,
    title: "경주 역사 탐방 주말여행",
    host: "역사탐험가",
    dates: "2025-06-08 ~ 2025-06-09",
    location: "경주",
    tags: ["문화", "역사", "사진"],
    memberCount: 3,
    maxMembers: 5,
    description: "경주 역사 유적지를 둘러보는 여행입니다. 역사에 관심 있는 분들과 함께하고 싶어요.",
    imageUrl: "https://images.unsplash.com/photo-1578637147340-32aad0584920?q=80&w=1000&auto=format&fit=crop",
    schedule: [
      { day: 1, activities: ["불국사", "석굴암", "첨성대", "안압지 야경"] },
      { day: 2, activities: ["대릉원", "국립경주박물관", "양동마을"] },
    ],
  },
]

// 태그 토글 함수
const toggleTag = (tag) => {
  if (selectedTags.value.includes(tag)) {
    selectedTags.value = selectedTags.value.filter(t => t !== tag)
  } else {
    selectedTags.value.push(tag)
  }
}

// 모집글 작성 태그 토글 함수
const toggleRecruitTag = (tag) => {
  if (recruitForm.value.tags.includes(tag)) {
    recruitForm.value.tags = recruitForm.value.tags.filter(t => t !== tag)
  } else {
    if (recruitForm.value.tags.length < 5) {
      recruitForm.value.tags.push(tag)
    }
  }
}

// 여행 상세 정보 열기
const openTripDetails = (trip) => {
  selectedTrip.value = trip
  showTripDetails.value = true
}
</script>

<style scoped>
/* 추가 스타일이 필요한 경우 여기에 작성 */
</style>