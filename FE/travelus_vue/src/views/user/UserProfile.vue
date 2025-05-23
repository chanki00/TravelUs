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
            <p class="text-gray-500">{{ editUser.userEmail }}</p>
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
                  v-for="(tag, index) in userTags"
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
                  v-for="(tag, index) in tripTags"
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
                  v-model.lazy="editUser.userId"
                  readonly
                  class="bg-gray-50 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm text-gray-700"
                />
              </div>

              <div class="space-y-2">
                <label for="email" class="block text-sm font-medium text-gray-700">이메일</label>
                <input
                  id="email"
                  type="email"
                  v-model="editUser.userEmail"
                  class="block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm text-gray-700"
                />
              </div>

              <div class="space-y-2">
                <label for="name" class="block text-sm font-medium text-gray-700">이름</label>
                <input
                  id="name"
                  v-model="editUser.name"
                  class="block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm text-gray-700"
                />
              </div>

              <div class="space-y-2">
                <label for="address" class="block text-sm font-medium text-gray-700">지역</label>
                <select
                  id="address"
                  v-model="editUser.address"
                  class="block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm text-gray-700"
                >
                  <option value="서울">서울</option>
                  <option value="부산">부산</option>
                  <option value="인천">인천</option>
                  <option value="대구">대구</option>
                  <option value="기타">기타</option>
                </select>
              </div>

              <div class="space-y-2">
                <label for="age" class="block text-sm font-medium text-gray-700">나이</label>
                <select
                  id="age"
                  v-model="editUser.age"
                  class="block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm text-gray-700"
                >
                  <option value="20대">20대</option>
                  <option value="30대">30대</option>
                  <option value="40대">40대</option>
                  <option value="50대 이상">50대 이상</option>
                  <option value="기타">기타</option>
                </select>
              </div>

              <div class="space-y-2">
                <label for="gender" class="block text-sm font-medium text-gray-700">성별</label>
                <select
                  id="gender"
                  v-model="editUser.gender"
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
                v-model.lazy="editUser.intro"
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
                  v-for="tagName in userTags"
                  :key="tagName"
                  class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-purple-50 text-purple-800"
                >
                  {{ tagName }}
                </span>
              </div>

              <!-- 수정 버튼 -->
              <button
                @click="showUserModal = true"
                class="px-3 py-1 bg-gray-200 text-sm rounded hover:bg-gray-300"
              >
                성격 태그 수정하기
              </button>
            </div>

            <!-- 모달 -->
            <div
              v-if="showUserModal"
              class="fixed inset-0 bg-black bg-opacity-30 flex items-center justify-center z-50"
            >
              <div class="bg-white rounded-lg shadow-lg w-full max-w-md p-6">
                <h3 class="text-lg font-semibold mb-4">성격 태그 선택</h3>

                <!-- 선택한 태그들 -->
                <div class="mb-4">
                  <h4 class="text-sm font-medium mb-2">선택한 태그</h4>
                  <div class="flex flex-wrap gap-2">
                    <span
                      v-for="tag in allUserTags.filter((t) => selectedUserTagIds.includes(t.id))"
                      :key="tag.id"
                      class="px-2 py-1 rounded-full bg-blue-100 text-blue-800 text-xs cursor-pointer"
                      @click="toggleTag('personal', tag.id)"
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
                      v-for="tag in allUserTags.filter((t) => !selectedUserTagIds.includes(t.id))"
                      :key="tag.id"
                      class="px-2 py-1 rounded-full bg-gray-100 text-gray-800 text-xs cursor-pointer hover:bg-gray-200"
                      @click="toggleTag('personal', tag.id)"
                    >
                      {{ tag.name }}
                    </span>
                  </div>
                </div>

                <div class="flex justify-end gap-2 mt-4">
                  <button
                    @click="showUserModal = false"
                    class="px-4 py-2 bg-gray-300 rounded hover:bg-gray-400"
                  >
                    취소
                  </button>
                  <button
                    @click="saveTags('personal')"
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
                  v-for="tagName in tripTags"
                  :key="tagName"
                  class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-green-50 text-green-800"
                >
                  {{ tagName }}
                </span>
              </div>

              <!-- 수정 버튼 -->
              <button
                @click="showTripModal = true"
                class="px-3 py-1 bg-gray-200 text-sm rounded hover:bg-gray-300"
              >
                여행 태그 수정하기
              </button>
            </div>

            <div
              v-if="showTripModal"
              class="fixed inset-0 bg-black bg-opacity-30 flex items-center justify-center z-50"
            >
              <div class="bg-white rounded-lg shadow-lg w-full max-w-md p-6">
                <h3 class="text-lg font-semibold mb-4">성격 태그 선택</h3>

                <!-- 선택한 태그들 -->
                <div class="mb-4">
                  <h4 class="text-sm font-medium mb-2">선택한 태그</h4>
                  <div class="flex flex-wrap gap-2">
                    <span
                      v-for="tag in allTripTags.filter((t) => selectedTripTagIds.includes(t.id))"
                      :key="tag.id"
                      class="px-2 py-1 rounded-full bg-blue-100 text-blue-800 text-xs cursor-pointer"
                      @click="toggleTag('trip', tag.id)"
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
                      v-for="tag in allTripTags.filter((t) => !selectedTripTagIds.includes(t.id))"
                      :key="tag.id"
                      class="px-2 py-1 rounded-full bg-gray-100 text-gray-800 text-xs cursor-pointer hover:bg-gray-200"
                      @click="toggleTag('trip', tag.id)"
                    >
                      {{ tag.name }}
                    </span>
                  </div>
                </div>

                <div class="flex justify-end gap-2 mt-4">
                  <button
                    @click="showTripModal = false"
                    class="px-4 py-2 bg-gray-300 rounded hover:bg-gray-400"
                  >
                    취소
                  </button>
                  <button
                    @click="saveTags('trip')"
                    class="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700"
                  >
                    저장
                  </button>
                </div>
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
                @click="deleteUser"
                class="px-4 py-2 border border-red-300 text-red-600 rounded-md hover:bg-red-50"
              >
                회원 탈퇴
              </button>

              <div class="space-x-4">
                <!-- <button
                  class="px-4 py-2 border border-gray-300 rounded-md text-gray-700 hover:bg-gray-50"
                >
                  취소
                </button> -->
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
import {useRouter} from "vue-router"
const router = useRouter()
const userStore = useUserStore()
const user = computed(() => userStore.loginUser)
const editUser = ref({})

const saveProfile = async () => {
  try {
    userStore.update(editUser)
    await userAi.patch(`/api/v1/user/info/${user.value.id}`, user.value)
    alert('프로필이 저장되었습니다.')
  } catch (error) {
    console.error('프로필 저장 실패:', error)
    alert('프로필 저장에 실패했습니다.')
  }
}

// ------------
const selectedUserTagIds = ref([])
const userTags = ref([])
const selectedTripTagIds = ref([])
const tripTags = ref([])


const toggleTag = (type, id) => {

  if (type === 'personal') {
    if (selectedUserTagIds.value.includes(id)) {
      selectedUserTagIds.value = selectedUserTagIds.value.filter((tagId) => tagId !== id)
    } else {
      selectedUserTagIds.value.push(id)
    }
  }
  else {
    if (selectedTripTagIds.value.includes(id)) {
      selectedTripTagIds.value = selectedTripTagIds.value.filter((tagId) => tagId !== id)
    } else {
      selectedTripTagIds.value.push(id)
    }
  }
}

const saveTags = async (type) => {
  try {
    let selected;
    if (type === 'personal') {
      selected = selectedUserTagIds.value;
    }
    else {
      selected = selectedTripTagIds.value
    }

    await userAi.patch(`/api/v1/tag/user`, {
      userId: user.value.id,
      type,
      tagIds: selected,
    })

    // 저장 후 태그 다시 가져오기
    const selectedNames = await userAi.get(`/api/v1/tag/user/${type}/${user.value.id}`)

    if (type === "personal") {
      userTags.value = selectedNames.data
    }
    else {
      tripTags.value = selectedNames.data;
    }

    showUserModal.value = false
    showTripModal.value = false
    alert('성격 태그가 저장되었습니다.')
  } catch (e) {
    alert('저장 실패')
  }
}

const showUserModal = ref(false)
const showTripModal = ref(false)
const allUserTags = ref([])
const allTripTags = ref([])

onMounted(async () => {
  editUser.value = {...user.value}
  if (editUser.value.age == null) user.value.age = '기타'
  if (editUser.value.gender == null) editUser.value.gender = 'O'
  if (editUser.value.address == null) editUser.value.address = '기타'

  console.log("유저", editUser)

  try {
    getUserTag()
    getTripTag()
  } catch (e) {
    console.error('태그 불러오기 실패', e)
  }
})

const getUserTag = async () => {
  const all = await userAi.get('/api/v1/tag/user', { params: { type: '성격' } })
  allUserTags.value = all.data

  const selectedNames = await userAi.get(`/api/v1/tag/user/personal/${user.value.id}`)
  userTags.value = selectedNames.data
  const tagNames = selectedNames.data

  // 태그 이름 기준으로 해당 ID 추출
  selectedUserTagIds.value = allUserTags.value
    .filter((tag) => tagNames.includes(tag.name))
    .map((tag) => tag.id)
}

const getTripTag = async () => {
  const all = await userAi.get('/api/v1/tag/trip', {params: {type: '여행'}})
  allTripTags.value = all.data;

  const selectedNames = await userAi.get(`api/v1/tag/user/trip/${user.value.id}`)
  tripTags.value = selectedNames.data;
  const tagNames = selectedNames.data;

  selectedTripTagIds.value = allTripTags.value
    .filter((tag) => tagNames.includes(tag.name))
    .map((tag) => tag.id)
}

// --------------------------
const deleteUser = () => {
  userStore.deleteUser();
  router.push("/")
}

const trips = ref([])
const invites = ref([])
const allowInvites = ref(false)

const activeTab = ref('edit')
const tabs = [
  { id: 'edit', name: '회원정보 수정' },
  { id: 'trips', name: '내 여행 계획' },
  { id: 'invites', name: '동행 초대' },
]
</script>
<style scoped></style>
