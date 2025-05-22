<template>
  <div class="min-h-screen bg-gray-50">
    <div class="py-12 px-6 max-w-5xl mx-auto">
      <h1 class="text-3xl font-bold mb-8">내 프로필</h1>

      <div class="bg-white shadow-sm rounded-lg p-6 mb-8">
        <div class="flex flex-col sm:flex-row items-center gap-6 mb-6">
          <div class="w-24 h-24 rounded-full bg-blue-100 flex items-center justify-center">
            <span class="text-2xl font-bold text-blue-600">여행</span>
          </div>

          <div class="text-center sm:text-left">
            <h2 class="text-2xl font-bold">{{ user.name }}</h2>
            <p class="text-gray-500">{{ user.userEmail }}</p>
          </div>

          <div class="ml-auto">
            <button
              class="px-4 py-2 border border-gray-300 rounded-md text-sm font-medium text-gray-700 hover:bg-gray-50"
            >
              프로필 수정
            </button>
          </div>
        </div>

        <div class="border-t pt-4">
          <p class="text-gray-700 mb-4">
            {{ user.intro }}
          </p>

          <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
              <h3 class="text-sm font-medium text-gray-500 mb-2">나의 성격 태그</h3>
              <div class="flex flex-wrap gap-2">
                <span
                  v-for="(tag, index) in personalityTags"
                  :key="index"
                  class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-purple-50 text-purple-800"
                >
                  {{ tag }}
                </span>
              </div>
            </div>

            <div>
              <h3 class="text-sm font-medium text-gray-500 mb-2">선호하는 여행 태그</h3>
              <div class="flex flex-wrap gap-2">
                <span
                  v-for="(tag, index) in travelTags"
                  :key="index"
                  class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-green-50 text-green-800"
                >
                  {{ tag }}
                </span>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="bg-white shadow-sm rounded-lg overflow-hidden">
        <div class="border-b border-gray-200">
          <nav class="flex -mb-px">
            <button
              v-for="tab in tabs"
              :key="tab.id"
              @click="activeTab = tab.id"
              class="px-6 py-4 text-sm font-medium"
              :class="
                activeTab === tab.id
                  ? 'border-b-2 border-blue-500 text-blue-600'
                  : 'text-gray-500 hover:text-gray-700 hover:border-gray-300'
              "
            >
              {{ tab.name }}
            </button>
          </nav>
        </div>

        <!-- 회원정보 수정 탭 -->
        <div v-show="activeTab === 'edit'" class="p-6">
          <h2 class="text-lg font-medium mb-6">회원정보 수정</h2>

          <div class="space-y-6">
            <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
              <div class="space-y-2">
                <label for="username" class="block text-sm font-medium text-gray-700">아이디</label>
                <input
                  id="username"
                  v-model="user.userId"
                  readonly
                  class="bg-gray-50 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm text-gray-700"
                />
              </div>

              <div class="space-y-2">
                <label for="email" class="block text-sm font-medium text-gray-700">이메일</label>
                <input
                  id="email"
                  type="email"
                  v-model="user.userEmail"
                  class="block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm text-gray-700"
                />
              </div>

              <div class="space-y-2">
                <label for="name" class="block text-sm font-medium text-gray-700">이름</label>
                <input
                  id="name"
                  v-model="user.name"
                  class="block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm text-gray-700"
                />
              </div>

              <div class="space-y-2">
                <label for="address" class="block text-sm font-medium text-gray-700">지역</label>
                <select
                  id="address"
                  v-model="user.address"
                  class="block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm text-gray-700"
                >
                  <option value="seoul">서울</option>
                  <option value="busan">부산</option>
                  <option value="incheon">인천</option>
                  <option value="daegu">대구</option>
                  <option value="other">기타</option>
                </select>
              </div>

              <div class="space-y-2">
                <label for="age" class="block text-sm font-medium text-gray-700">나이</label>
                <select
                  id="age"
                  v-model="user.age"
                  class="block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm text-gray-700"
                >
                  <option value="20s">20대</option>
                  <option value="30s">30대</option>
                  <option value="40s">40대</option>
                  <option value="50s">50대 이상</option>
                  <option value="other">기타</option>
                </select>
              </div>

              <div class="space-y-2">
                <label for="gender" class="block text-sm font-medium text-gray-700">성별</label>
                <select
                  id="gender"
                  v-model="user.gender"
                  class="block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm text-gray-700"
                >
                  <option value="M">남성</option>
                  <option value="F">여성</option>
                  <option value="O">기타</option>
                </select>
              </div>
            </div>

            <div class="space-y-2">
              <label for="intro" class="block text-sm font-medium text-gray-700">자기소개</label>
              <textarea
                id="intro"
                v-model="user.intro"
                rows="4"
                class="resize-none block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm text-gray-700"
              ></textarea>
            </div>

            <!-- 성격 태그 리스트 -->
            <div class="space-y-2">
              <label class="block text-sm font-medium text-gray-700">성격 태그</label>

              <!-- 사용자 성격 태그 표시 -->
              <div class="flex flex-wrap gap-2 mb-2">
                <span
                  v-for="tagName in personalityTags"
                  :key="tagName"
                  class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-purple-50 text-purple-800"
                >
                  {{ tagName }}
                </span>
              </div>

              <!-- 수정 버튼 -->
              <button
                @click="showModal = true"
                class="px-3 py-1 bg-gray-200 text-sm rounded hover:bg-gray-300"
              >
                성격 태그 수정하기
              </button>
            </div>

            <!-- 모달 -->
            <div
              v-if="showModal"
              class="fixed inset-0 bg-black bg-opacity-30 flex items-center justify-center z-50"
            >
              <div class="bg-white rounded-lg shadow-lg w-full max-w-md p-6">
                <h3 class="text-lg font-semibold mb-4">성격 태그 선택</h3>

                <!-- 선택한 태그들 -->
                <div class="mb-4">
                  <h4 class="text-sm font-medium mb-2">선택한 태그</h4>
                  <div class="flex flex-wrap gap-2">
                    <span
                      v-for="tag in allTags.filter((t) => selectedTagIds.includes(t.id))"
                      :key="tag.id"
                      class="px-2 py-1 rounded-full bg-blue-100 text-blue-800 text-xs cursor-pointer"
                      @click="toggleTag(tag.id)"
                    >
                      {{ tag.name }}
                    </span>
                  </div>
                </div>

                <!-- 선택 안 한 태그들 -->
                <div class="mb-4">
                  <h4 class="text-sm font-medium mb-2">선택 안 한 태그</h4>
                  <div class="flex flex-wrap gap-2">
                    <span
                      v-for="tag in allTags.filter((t) => !selectedTagIds.includes(t.id))"
                      :key="tag.id"
                      class="px-2 py-1 rounded-full bg-gray-100 text-gray-800 text-xs cursor-pointer hover:bg-gray-200"
                      @click="toggleTag(tag.id)"
                    >
                      {{ tag.name }}
                    </span>
                  </div>
                </div>

                <div class="flex justify-end gap-2 mt-4">
                  <button
                    @click="showModal = false"
                    class="px-4 py-2 bg-gray-300 rounded hover:bg-gray-400"
                  >
                    취소
                  </button>
                  <button
                    @click="saveTags"
                    class="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700"
                  >
                    저장
                  </button>
                </div>
              </div>
            </div>

            <div class="space-y-2">
              <label class="block text-sm font-medium text-gray-700">선호 여행 태그</label>
              <div class="flex flex-wrap gap-2 mb-2">
                <span
                  v-for="(tag, index) in travelTags"
                  :key="index"
                  class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-green-50 text-green-800"
                >
                  {{ tag }}
                  <button
                    @click="removeTag('travel', tag)"
                    class="ml-1 h-4 w-4 rounded-full inline-flex items-center justify-center hover:bg-red-100"
                  >
                    <svg
                      xmlns="http://www.w3.org/2000/svg"
                      class="h-3 w-3"
                      viewBox="0 0 20 20"
                      fill="currentColor"
                    >
                      <path
                        fill-rule="evenodd"
                        d="M4.293 4.293a1 1 0 011.414 0L10 8.586l4.293-4.293a1 1 0 111.414 1.414L11.414 10l4.293 4.293a1 1 0 01-1.414 1.414L10 11.414l-4.293 4.293a1 1 0 01-1.414-1.414L8.586 10 4.293 5.707a1 1 0 010-1.414z"
                        clip-rule="evenodd"
                      />
                    </svg>
                  </button>
                </span>
              </div>
              <div class="flex gap-2">
                <input
                  @keydown.enter.prevent="addTag('travel')"
                  placeholder="새 태그 추가"
                  class="block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm text-gray-700"
                />
                <button
                  @click="addTag('travel')"
                  class="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700"
                >
                  추가
                </button>
              </div>
            </div>

            <div class="flex items-center space-x-2">
              <input
                type="checkbox"
                id="invite-setting"
                v-model="allowInvites"
                class="h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300 rounded"
              />
              <label for="invite-setting" class="text-sm text-gray-700">동행 초대 받기 허용</label>
            </div>

            <div class="flex justify-between">
              <button
                class="px-4 py-2 border border-red-300 text-red-600 rounded-md hover:bg-red-50"
              >
                회원 탈퇴
              </button>

              <div class="space-x-4">
                <button
                  class="px-4 py-2 border border-gray-300 rounded-md text-gray-700 hover:bg-gray-50"
                >
                  취소
                </button>
                <button
                  @click="saveProfile"
                  class="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700"
                >
                  저장
                </button>
              </div>
            </div>
          </div>
        </div>

        <!-- 내 여행 계획 탭 -->
        <div v-show="activeTab === 'trips'" class="p-6">
          <h2 class="text-lg font-medium mb-6">나의 여행 계획</h2>

          <div v-if="trips.length === 0" class="text-center py-12">
            <p class="text-gray-500 mb-4">아직 계획된 여행이 없습니다.</p>
            <button class="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700">
              여행 계획 시작하기
            </button>
          </div>

          <div v-else class="grid grid-cols-1 md:grid-cols-2 gap-6">
            <!-- 여행 계획 목록이 있을 경우 표시될 내용 -->
          </div>
        </div>

        <!-- 동행 초대 탭 -->
        <div v-show="activeTab === 'invites'" class="p-6">
          <h2 class="text-lg font-medium mb-6">동행 초대 목록</h2>

          <div v-if="invites.length === 0" class="text-center py-12">
            <p class="text-gray-500 mb-4">아직 받은 초대가 없습니다.</p>
            <button
              class="px-4 py-2 border border-gray-300 rounded-md text-gray-700 hover:bg-gray-50"
            >
              동행자 찾기
            </button>
          </div>

          <div v-else class="grid grid-cols-1 md:grid-cols-2 gap-6">
            <!-- 초대 목록이 있을 경우 표시될 내용 -->
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useUserStore } from '@/store/user'
import { userAi } from '@/axios'
const userStore = useUserStore()
const user = computed(() => userStore.loginUser)

const saveProfile = async () => {
  console.log(showModal.value)
  try {
    await userAi.patch(`/api/v1/user/info/${user.value.id}`, user.value)
    alert('프로필이 저장되었습니다.')
  } catch (error) {
    console.error('프로필 저장 실패:', error)
    alert('프로필 저장에 실패했습니다.')
  }
}

// ------------
const selectedTagIds = ref([])
const personalityTags = ref([])

const toggleTag = (id) => {
  if (selectedTagIds.value.includes(id)) {
    selectedTagIds.value = selectedTagIds.value.filter((tagId) => tagId !== id)
  } else {
    selectedTagIds.value.push(id)
  }
}

const saveTags = async () => {
  try {
    await userAi.patch(`/api/v1/tag/user`, {
      userId: user.value.id,
      tagIds: selectedTagIds.value,
    })

    // 저장 후 태그 다시 가져오기
    const selectedNames = await userAi.get(`/api/v1/tag/user/${user.value.id}`)
    personalityTags.value = selectedNames.data

    showModal.value = false
    alert('성격 태그가 저장되었습니다.')
  } catch (e) {
    alert('저장 실패')
  }
}

const allowInvites = ref(false)
const showModal = ref(false)
const allTags = ref([])
const trips = ref([])
const invites = ref([])

onMounted(async () => {
  if (user.value.age == null) user.value.age = 'other'
  if (user.value.gender == null) user.value.gender = 'O'
  if (user.value.address == null) user.value.address = 'other'

  try {
    getUserTag()
  } catch (e) {
    console.error('태그 불러오기 실패', e)
  }
})

const getUserTag = async () => {
  const all = await userAi.get('/api/v1/tag/user', { params: { type: '성격' } })
  allTags.value = all.data

  const selectedNames = await userAi.get(`/api/v1/tag/user/${user.value.id}`)
  personalityTags.value = selectedNames.data
  const tagNames = selectedNames.data

  // 태그 이름 기준으로 해당 ID 추출
  selectedTagIds.value = allTags.value
    .filter((tag) => tagNames.includes(tag.name))
    .map((tag) => tag.id)
}

const activeTab = ref('edit')
const tabs = [
  { id: 'edit', name: '회원정보 수정' },
  { id: 'trips', name: '내 여행 계획' },
  { id: 'invites', name: '동행 초대' },
]
</script>
<style scoped></style>
