<template>
  <div class="min-h-screen bg-gradient-to-b from-blue-50 to-white">

    <div class="py-12 px-6 max-w-6xl mx-auto">
      <h1 class="text-3xl font-bold text-center mb-2">나와 잘 맞는 동행자 찾기</h1>
      <p class="text-gray-600 text-center mb-12">
        비슷한 여행 취향을 가진 동행자를 찾아 함께 특별한 여행을 떠나보세요
      </p>

      <div class="flex flex-wrap gap-4 mb-8 justify-between">
        <div class="flex flex-wrap gap-2">
          <button 
            :class="[
              'px-4 py-2 rounded-md',
              activeFilter === 'recommended' 
                ? 'bg-primary text-white' 
                : 'bg-white border border-gray-300 text-gray-700'
            ]"
            @click="activeFilter = 'recommended'"
          >
            종합
          </button>
          <button 
            :class="[
              'px-4 py-2 rounded-md',
              activeFilter === 'personal' 
                ? 'bg-primary text-white' 
                : 'bg-white border border-gray-300 text-gray-700'
            ]"
            @click="activeFilter = 'personal'"
          >
            성격
          </button>
          <button 
              @click="showTagFilter = !showTagFilter"
              class="px-4 py-2 border border-dashed rounded-md flex items-center gap-2"
            >
              <tag-icon class="h-4 w-4" />
              태그 필터
          </button>
        </div>

        <div class="flex flex-wrap gap-2">
          <select class="w-[120px] px-3 py-2 border border-gray-300 rounded-md">
            <option value="all">모두</option>
            <option value="male">남성</option>
            <option value="female">여성</option>
          </select>

          <select class="w-[120px] px-3 py-2 border border-gray-300 rounded-md">
            <option value="all">모두</option>
            <option value="20s">20대</option>
            <option value="30s">30대</option>
            <option value="40s">40대</option>
            <option value="50s">50대 이상</option>
          </select>
        </div>
      </div>

      <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        <div 
          v-for="companion in companions" 
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
                <div class="font-bold text-purple-600">{{ companion.match }}%</div>
              </div>
              <div class="flex gap-2">
                <button class="px-3 py-1 text-sm bg-primary text-white rounded-md">
                  초대하기
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import { useUserStore } from '@/store/user';
import { 
  Tag as TagIcon
} from 'lucide-vue-next'

const userStore = useUserStore()

const genderFilter = ref('all')
const ageFilter = ref('all')
const styleFilter = ref('all')

const currUser = ref(null)
const companions = computed(() => {
  if (!currUser.value) return []
  
  return userStore.userList
    .filter(user => user.id !== currUser.value.id)
    .map(user => ({
      ...user,
      match: calculateSimilarity(currUser.value, user, activeFilter.value)
    }))
    .sort((a, b) => b.match - a.match) // 높은 점수부터 정렬
})

const activeFilter = ref('recommended')

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

onMounted(async () => {
  await userStore.getUserList()
  currUser.value = userStore.userList.find(
    (user) => user.id === userStore.loginUser.id
  )
})

const getCommonCount = (arr1, arr2) => {
  const set1 = new Set(arr1)
  const set2 = new Set(arr2)
  let count = 0
  for (const item of set1) {
    if (set2.has(item)) count++
  }
  return count
}

const calculateSimilarity = (user, target, type) => {
  const commonPersonality = getCommonCount(user.personality, target.personality)
  const commonPreference = getCommonCount(user.preference, target.preference)

  const totalPersonality = new Set([...user.personality, ...target.personality]).size
  const totalPreference = new Set([...user.preference, ...target.preference]).size

  const personalityScore = totalPersonality ? commonPersonality / totalPersonality : 0
  const preferenceScore = totalPreference ? commonPreference / totalPreference : 0

  let weightPersonality = 0.5
  let weightPreference = 0.5

  if (type === 'personal') {
    weightPersonality = 0.7
    weightPreference = 0.3
  } else if (type === 'prefer') {
    weightPersonality = 0.3
    weightPreference = 0.7
  }

  const finalScore = (personalityScore * weightPersonality + preferenceScore * weightPreference) * 100
  return Math.round(finalScore)
}
</script>

<style scoped>
</style>
