<template>
  <div class="min-h-screen bg-gradient-to-b from-blue-50 to-white">
    <div class="py-12 px-6 max-w-6xl mx-auto">
      <h1 class="text-3xl font-bold text-center mb-2">나와 잘 맞는 동행자 찾기</h1>
      <p class="text-gray-600 text-center mb-12">
        비슷한 여행 취향을 가진 동행자를 찾아 함께 특별한 여행을 떠나보세요
      </p>

      <!-- 필터 섹션 -->
      <div class="bg-white rounded-lg shadow-sm p-6 mb-8">
        <div class="flex flex-wrap gap-4 mb-6">
          <!-- 매칭 기준 선택 -->
          <div class="flex flex-wrap gap-2">
            <button
              :class="[
                'px-4 py-2 rounded-md transition-colors',
                activeFilter === 'recommended'
                  ? 'bg-blue-500 text-white'
                  : 'bg-white border border-gray-300 text-gray-700 hover:bg-gray-50',
              ]"
              @click="activeFilter = 'recommended'"
            >
              종합 매칭
            </button>
            <button
              :class="[
                'px-4 py-2 rounded-md transition-colors',
                activeFilter === 'personal'
                  ? 'bg-purple-500 text-white'
                  : 'bg-white border border-gray-300 text-gray-700 hover:bg-gray-50',
              ]"
              @click="activeFilter = 'personal'"
            >
              성격 중심
            </button>
            <button
              :class="[
                'px-4 py-2 rounded-md transition-colors',
                activeFilter === 'preference'
                  ? 'bg-green-500 text-white'
                  : 'bg-white border border-gray-300 text-gray-700 hover:bg-gray-50',
              ]"
              @click="activeFilter = 'preference'"
            >
              여행 스타일 중심
            </button>
          </div>

          <!-- 정렬 옵션 -->
          <div class="flex items-center gap-2 ml-auto">
            <label class="text-sm text-gray-600">정렬:</label>
            <select
              v-model="sortOption"
              class="px-3 py-2 border border-gray-300 rounded-md text-sm"
            >
              <option value="match">매칭 점수순</option>
              <option value="age">나이순</option>
              <option value="recent">최근 가입순</option>
            </select>
          </div>
        </div>

        <!-- 상세 필터 -->
        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
          <!-- 성별 필터 -->
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">성별</label>
            <select
              v-model="genderFilter"
              class="w-full px-3 py-2 border border-gray-300 rounded-md"
            >
              <option value="all">전체</option>
              <option value="M">남성</option>
              <option value="F">여성</option>
              <option value="O">기타</option>
            </select>
          </div>

          <!-- 나이 필터 -->
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">나이</label>
            <select v-model="ageFilter" class="w-full px-3 py-2 border border-gray-300 rounded-md">
              <option value="all">전체</option>
              <option value="20s">20대</option>
              <option value="30s">30대</option>
              <option value="40s">40대</option>
              <option value="50s">50대 이상</option>
            </select>
          </div>

          <!-- 성격 태그 필터 -->
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">성격</label>
            <button
              @click="showPersonalityFilter = !showPersonalityFilter"
              class="w-full px-3 py-2 border border-gray-300 rounded-md flex items-center justify-between hover:bg-gray-50"
            >
              <span class="text-sm">
                {{
                  selectedPersonalityTags.length > 0
                    ? `${selectedPersonalityTags.length}개 선택`
                    : '성격 선택'
                }}
              </span>
              <chevron-down-icon class="h-4 w-4" />
            </button>
          </div>

          <!-- 여행 스타일 필터 -->
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">여행 스타일</label>
            <button
              @click="showPreferenceFilter = !showPreferenceFilter"
              class="w-full px-3 py-2 border border-gray-300 rounded-md flex items-center justify-between hover:bg-gray-50"
            >
              <span class="text-sm">
                {{
                  selectedPreferenceTags.length > 0
                    ? `${selectedPreferenceTags.length}개 선택`
                    : '스타일 선택'
                }}
              </span>
              <chevron-down-icon class="h-4 w-4" />
            </button>
          </div>
        </div>

        <!-- 성격 태그 선택 드롭다운 -->
        <div
          v-if="showPersonalityFilter"
          class="mt-4 p-4 border border-gray-200 rounded-lg bg-gray-50"
        >
          <div class="flex flex-wrap gap-2">
            <button
              v-for="tag in personalityTags"
              :key="tag"
              @click="togglePersonalityTag(tag)"
              :class="[
                'px-3 py-1 rounded-full text-sm transition-colors',
                selectedPersonalityTags.includes(tag)
                  ? 'bg-purple-500 text-white'
                  : 'bg-white border border-purple-200 text-purple-700 hover:bg-purple-50',
              ]"
            >
              {{ tag }}
            </button>
          </div>
        </div>

        <!-- 여행 스타일 태그 선택 드롭다운 -->
        <div
          v-if="showPreferenceFilter"
          class="mt-4 p-4 border border-gray-200 rounded-lg bg-gray-50"
        >
          <div class="flex flex-wrap gap-2">
            <button
              v-for="tag in preferenceTags"
              :key="tag"
              @click="togglePreferenceTag(tag)"
              :class="[
                'px-3 py-1 rounded-full text-sm transition-colors',
                selectedPreferenceTags.includes(tag)
                  ? 'bg-green-500 text-white'
                  : 'bg-white border border-green-200 text-green-700 hover:bg-green-50',
              ]"
            >
              {{ tag }}
            </button>
          </div>
        </div>

        <!-- 필터 초기화 버튼 -->
        <div class="mt-4 flex justify-end">
          <button
            @click="resetFilters"
            class="px-4 py-2 text-sm text-gray-600 hover:text-gray-800 border border-gray-300 rounded-md hover:bg-gray-50"
          >
            필터 초기화
          </button>
        </div>
      </div>

      <!-- 검색 결과 -->
      <div class="mb-6">
        <p class="text-gray-600">총 {{ filteredCompanions.length }}명의 동행자를 찾았습니다</p>
      </div>

      <!-- 동행자 목록 -->
      <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        <div
          v-for="companion in paginatedCompanions"
          :key="companion.id"
          class="bg-white rounded-lg overflow-hidden shadow hover:shadow-lg transition-shadow"
        >
          <div class="p-6">
            <div class="flex items-center gap-4 mb-4">
              <img
                :src="companion.profile"
                :alt="companion.name"
                class="w-16 h-16 rounded-full object-cover border-2 border-white shadow"
              />
              <div>
                <h3 class="font-medium text-lg">{{ companion.name }}</h3>
                <div class="flex items-center gap-1 text-sm text-gray-500">
                  <span>{{ companion.age }}</span>
                  <span>•</span>
                  <span>{{ convertGender(companion.gender) }}</span>
                  <span>•</span>
                  <span>{{ companion.address }}</span>
                </div>
              </div>
            </div>

            <div class="mb-4">
              <div class="text-sm text-gray-500 mb-1">성격</div>
              <div class="flex flex-wrap gap-2">
                <span
                  v-for="(tag, i) in companion.personality"
                  :key="i"
                  class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs bg-purple-50 text-purple-800"
                >
                  {{ tag }}
                </span>
              </div>
            </div>

            <div class="mb-4">
              <div class="text-sm text-gray-500 mb-1">선호 여행 스타일</div>
              <div class="flex flex-wrap gap-2">
                <span
                  v-for="(tag, i) in companion.preference"
                  :key="i"
                  class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs bg-green-50 text-green-800"
                >
                  {{ tag }}
                </span>
              </div>
            </div>

            <div class="flex justify-between items-center mt-6">
              <div>
                <div class="text-xs text-gray-500">매칭 점수</div>
                <div class="font-bold text-lg" :class="getMatchScoreColor(companion.match)">
                  {{ companion.match }}%
                </div>
              </div>
              <div class="flex gap-2">
                <button
                  class="px-4 py-2 text-sm bg-blue-500 text-white rounded-md hover:bg-blue-600 transition-colors"
                  @click="openInviteModal"
                >
                  초대하기
                </button>
                <InvitePlanModal
                  :show="showInviteModal"
                  :plans="myPlans"
                  :invitee-id="companion"
                  @select="inviteToPlan"
                  @close="showInviteModal = false"
                />
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 무한 스크롤 로딩 -->
      <div v-if="isLoadingMore" class="text-center py-8">
        <div class="inline-flex items-center gap-2 text-gray-600">
          <div class="animate-spin rounded-full h-4 w-4 border-b-2 border-blue-500"></div>
          <span>더 많은 동행자를 찾는 중...</span>
        </div>
      </div>

      <!-- 모든 데이터 로드 완료 -->
      <div v-else-if="hasReachedEnd && filteredCompanions.length > 0" class="text-center py-8">
        <p class="text-gray-500">모든 동행자를 확인했습니다</p>
      </div>

      <!-- 검색 결과 없음 -->
      <div v-if="filteredCompanions.length === 0" class="text-center py-12">
        <div class="text-gray-400 mb-4">
          <users-icon class="h-16 w-16 mx-auto" />
        </div>
        <h3 class="text-lg font-medium text-gray-900 mb-2">검색 결과가 없습니다</h3>
        <p class="text-gray-500">필터 조건을 조정해보세요</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import InvitePlanModal from '@/components/trip/InvitePlanModel.vue'
import { ref, onMounted, computed, watch, onUnmounted } from 'vue'
import { useUserStore } from '@/store/user'
import { ChevronDown as ChevronDownIcon, Users as UsersIcon } from 'lucide-vue-next'
import api from '@/api'

const userStore = useUserStore()

// 필터 상태
const genderFilter = ref('all')
const ageFilter = ref('all')
const activeFilter = ref('recommended')
const sortOption = ref('match')

// 태그 필터
const showPersonalityFilter = ref(false)
const showPreferenceFilter = ref(false)
const selectedPersonalityTags = ref([])
const selectedPreferenceTags = ref([])

// 무한 스크롤
const currentPage = ref(1)
const itemsPerPage = 9
const isLoadingMore = ref(false)
const hasReachedEnd = ref(false)

// 사용자 데이터
const currUser = ref(null)

// 태그 목록
const personalityTags = [
  '활발한',
  '조용한',
  '계획적인',
  '즉흥적인',
  '사교적인',
  '내향적인',
  '모험적인',
  '안전한',
  '유머러스한',
  '진지한',
  '친화적인',
  '독립적인',
]

const preferenceTags = [
  '자연',
  '도시',
  '문화',
  '음식',
  '쇼핑',
  '액티비티',
  '휴양',
  '사진',
  '역사',
  '예술',
  '스포츠',
  '축제',
  '야경',
  '카페',
  '맛집',
  '힐링',
]

// 필터링된 동행자 목록
const filteredCompanions = computed(() => {
  if (!currUser.value) {
    console.log('No current user found')
    return []
  }

  let filtered = userStore.userList
    .filter((user) => user.id !== currUser.value.id)
    .map((user) => {
      const matchScore = calculateSimilarity(currUser.value, user, activeFilter.value)
      return {
        ...user,
        match: matchScore,
      }
    })

  // 성별 필터
  if (genderFilter.value !== 'all') {
    filtered = filtered.filter((user) => user.gender === genderFilter.value)
  }

  // 나이 필터
  if (ageFilter.value !== 'all') {
    filtered = filtered.filter((user) => {
      const age = parseInt(user.age)
      switch (ageFilter.value) {
        case '20s':
          return age >= 20 && age < 30
        case '30s':
          return age >= 30 && age < 40
        case '40s':
          return age >= 40 && age < 50
        case '50s':
          return age >= 50
        default:
          return true
      }
    })
  }

  // 성격 태그 필터
  if (selectedPersonalityTags.value.length > 0) {
    filtered = filtered.filter((user) =>
      selectedPersonalityTags.value.some(
        (tag) => user.personality && user.personality.includes(tag),
      ),
    )
  }

  // 여행 스타일 태그 필터
  if (selectedPreferenceTags.value.length > 0) {
    filtered = filtered.filter((user) =>
      selectedPreferenceTags.value.some((tag) => user.preference && user.preference.includes(tag)),
    )
  }

  // 정렬
  switch (sortOption.value) {
    case 'match':
      filtered.sort((a, b) => b.match - a.match)
      break
    case 'age':
      filtered.sort((a, b) => parseInt(a.age) - parseInt(b.age))
      break
    case 'recent':
      filtered.sort((a, b) => new Date(b.createdAt || 0) - new Date(a.createdAt || 0))
      break
  }

  return filtered
})

// 페이지네이션된 동행자 목록
const paginatedCompanions = computed(() => {
  const endIndex = currentPage.value * itemsPerPage
  return filteredCompanions.value.slice(0, endIndex)
})

// 필터 변경 감지
watch(
  [
    genderFilter,
    ageFilter,
    activeFilter,
    sortOption,
    selectedPersonalityTags,
    selectedPreferenceTags,
  ],
  () => {
    resetPagination()
  },
  { deep: true },
)

const convertGender = (code) => {
  switch (code) {
    case 'M':
      return '남성'
    case 'F':
      return '여성'
    case 'O':
      return '기타'
    default:
      return '알 수 없음'
  }
}

const getMatchScoreColor = (score) => {
  if (score >= 80) return 'text-green-600'
  if (score >= 60) return 'text-blue-600'
  if (score >= 40) return 'text-yellow-600'
  return 'text-gray-600'
}

const togglePersonalityTag = (tag) => {
  const index = selectedPersonalityTags.value.indexOf(tag)
  if (index > -1) {
    selectedPersonalityTags.value.splice(index, 1)
  } else {
    selectedPersonalityTags.value.push(tag)
  }
}

const togglePreferenceTag = (tag) => {
  const index = selectedPreferenceTags.value.indexOf(tag)
  if (index > -1) {
    selectedPreferenceTags.value.splice(index, 1)
  } else {
    selectedPreferenceTags.value.push(tag)
  }
}

const resetFilters = () => {
  genderFilter.value = 'all'
  ageFilter.value = 'all'
  selectedPersonalityTags.value = []
  selectedPreferenceTags.value = []
  showPersonalityFilter.value = false
  showPreferenceFilter.value = false
}

const resetPagination = () => {
  currentPage.value = 1
  hasReachedEnd.value = false
}

const loadMoreItems = () => {
  if (isLoadingMore.value || hasReachedEnd.value) return

  const totalItems = filteredCompanions.value.length
  const currentItems = currentPage.value * itemsPerPage

  if (currentItems >= totalItems) {
    hasReachedEnd.value = true
    return
  }

  isLoadingMore.value = true

  setTimeout(() => {
    currentPage.value++
    isLoadingMore.value = false

    if (currentPage.value * itemsPerPage >= totalItems) {
      hasReachedEnd.value = true
    }
  }, 500)
}

const handleScroll = () => {
  const scrollTop = window.pageYOffset || document.documentElement.scrollTop
  const windowHeight = window.innerHeight
  const documentHeight = document.documentElement.scrollHeight

  if (scrollTop + windowHeight >= documentHeight - 200) {
    loadMoreItems()
  }
}

onMounted(async () => {
  await userStore.getUserList()
  currUser.value = userStore.userList.find((user) => user.id === userStore.loginUser.id)

  window.addEventListener('scroll', handleScroll)
})

onUnmounted(() => {
  window.removeEventListener('scroll', handleScroll)
})

const getCommonCount = (arr1, arr2) => {
  if (!Array.isArray(arr1) || !Array.isArray(arr2)) return 0

  const set1 = new Set(arr1)
  let count = 0

  for (const item of arr2) {
    if (set1.has(item)) {
      count++
    }
  }

  return count
}

const calculateSimilarity = (user, target, type) => {
  // 사용자 태그 데이터 안전하게 가져오기
  const userPersonality = user.personality || []
  const userPreference = user.preference || []
  const targetPersonality = target.personality || []
  const targetPreference = target.preference || []

  // 공통 태그 개수 계산
  const commonPersonality = getCommonCount(userPersonality, targetPersonality)
  const commonPreference = getCommonCount(userPreference, targetPreference)

  // 전체 태그 개수 (중복 제거)
  const allPersonalityTags = [...new Set([...userPersonality, ...targetPersonality])]
  const allPreferenceTags = [...new Set([...userPreference, ...targetPreference])]

  // 유사도 계산 (자카드 유사도 사용)
  const personalityScore =
    allPersonalityTags.length > 0
      ? (commonPersonality * 2) / (userPersonality.length + targetPersonality.length)
      : 0
  const preferenceScore =
    allPreferenceTags.length > 0
      ? (commonPreference * 2) / (userPreference.length + targetPreference.length)
      : 0

  // 매칭 타입에 따른 가중치 적용
  let weightPersonality = 0.5
  let weightPreference = 0.5

  if (type === 'personal') {
    weightPersonality = 0.7
    weightPreference = 0.3
  } else if (type === 'preference') {
    weightPersonality = 0.3
    weightPreference = 0.7
  }

  const finalScore =
    (personalityScore * weightPersonality + preferenceScore * weightPreference) * 100
  const result = Math.round(Math.max(0, Math.min(100, finalScore)))

  return result
}

// ---------------
const showInviteModal = ref(false)
const myPlans = ref([])
const openInviteModal = async () => {
  showInviteModal.value = true
  const res = await api.get(`/api/v1/plan/user/${userStore.loginUser.id}`)
  myPlans.value = res.data
}
const inviteToPlan = async (currPlan) => {
  try {
    console.log('계획', currPlan.plan.chatroomId)
    console.log('로그인', userStore.loginUser.id)
    console.log('상대', currPlan.inviteeId.id)

    await api.post('/api/v1/chat/invite', {
      chatroomId: currPlan.plan.chatroomId, // 여행 계획에 연결된 chatroomId
      inviterId: userStore.loginUser.id,
      inviteeId: currPlan.inviteeId.id,
    })
    alert('초대를 보냈습니다!')
    showInviteModal.value = false
  } catch (err) {
    console.error(err)
    alert('초대에 실패했습니다.')
  }
}
</script>

<style scoped></style>
