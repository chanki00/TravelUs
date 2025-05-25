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
            <router-link
              to="/plan"
              class="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700"
            >
              여행 계획 시작하기
            </router-link>
          </div>

          <div v-else class="grid grid-cols-1 md:grid-cols-2 gap-6">
            <div
              v-for="trip in trips"
              :key="trip.id"
              class="border rounded-lg overflow-hidden hover:shadow-md transition-shadow relative group"
            >
              <!-- 삭제 버튼 -->
              <button
                @click.stop="confirmDelete(trip)"
                class="absolute top-2 right-2 z-10 bg-red-500 text-white rounded-full w-8 h-8 flex items-center justify-center opacity-0 group-hover:opacity-100 transition-opacity hover:bg-red-600"
                title="여행계획 삭제"
              >
                ✕
              </button>

              <div class="cursor-pointer" @click="$router.push(`/tripdetail/${trip.id}`)">
                <div class="h-40 overflow-hidden relative">
                  <img
                    :src="
                      trip.image ||
                      'https://images.unsplash.com/photo-1476514525535-07fb3b4ae5f1?q=80&w=1000&auto=format&fit=crop'
                    "
                    :alt="trip.title"
                    class="w-full h-full object-cover"
                  />
                  <div class="absolute top-3 left-3">
                    <span
                      class="px-2 py-1 bg-white/70 backdrop-blur-sm text-gray-800 text-xs rounded-full"
                    >
                      {{ getSidoName(Number(trip.destination)) }}
                    </span>
                  </div>
                </div>

                <div class="p-3">
                  <h3 class="font-medium text-base mb-1">{{ trip.title }}</h3>

                  <div class="flex items-center justify-between text-xs text-gray-500 mb-2">
                    <span>{{ trip.duration - 1 }}박 {{ trip.duration }}일</span>
                    <span>{{ formatDate(trip.createdAt) }}</span>
                  </div>

                  <div class="flex items-center justify-between text-xs text-gray-500">
                    <span class="flex items-center gap-1">
                      <span>♥ {{ trip.likes }}</span>
                      <span>💬 {{ trip.shares }}</span>
                    </span>
                    <span :class="trip.isShared ? 'text-green-600' : 'text-gray-400'">
                      {{ trip.isShared ? '공유됨' : '비공개' }}
                    </span>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- 삭제 확인 모달 -->
        <div
          v-if="showDeleteModal"
          class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50"
        >
          <div class="bg-white rounded-lg shadow-lg w-full max-w-md p-6">
            <h3 class="text-lg font-semibold mb-4">여행계획 삭제</h3>
            <p class="text-gray-600 mb-6">
              "{{ tripToDelete?.title }}" 여행계획을 정말 삭제하시겠습니까?<br />
              <span class="text-red-500 text-sm">삭제된 계획은 복구할 수 없습니다.</span>
            </p>

            <div class="flex justify-end gap-3">
              <button
                @click="showDeleteModal = false"
                class="px-4 py-2 bg-gray-300 text-gray-700 rounded hover:bg-gray-400"
              >
                취소
              </button>
              <button
                @click="deleteTrip"
                class="px-4 py-2 bg-red-500 text-white rounded hover:bg-red-600"
              >
                삭제
              </button>
            </div>
          </div>
        </div>

        <!-- 동행 초대 탭 -->
        <div v-show="activeTab === 'invites'" class="p-6">
          <h2 class="text-lg font-medium mb-6">동행 요청 목록</h2>

          <div v-if="invites.length === 0" class="text-center py-12">
            <p class="text-gray-500 mb-4">아직 받은 초대가 없습니다.</p>
            <button
              class="px-4 py-2 border border-gray-300 rounded-md text-gray-700 hover:bg-gray-50"
            >
              동행자 찾기
            </button>
          </div>

          <div v-else class="grid grid-cols-1 md:grid-cols-2 gap-6">
            <div
              v-for="invite in invites"
              :key="invite.id"
              class="border rounded-lg p-4 shadow-sm flex flex-col gap-2"
            >
              <div class="flex justify-between items-center">
                <div>
                  <p class="text-sm text-gray-600">
                    <strong>
                      <template v-if="invite.type === 'INVITE'">
                        {{ invite.name }} 님이
                        <span class="text-blue-600 font-semibold">초대했습니다.</span>
                      </template>
                      <template v-else-if="invite.type === 'REQUEST'">
                        {{ invite.name }} 님이
                        <span class="text-green-600 font-semibold">참여를 요청했습니다.</span>
                      </template>
                    </strong>
                  </p>

                  <p class="text-xs text-gray-400">
                    {{ formatDate(invite.createdAt) }}
                  </p>
                </div>

                <span
                  v-if="invite.status === 'ACCEPTED'"
                  class="text-green-600 text-sm font-semibold"
                >
                  수락됨
                </span>
                <span
                  v-else-if="invite.status === 'DECLINED'"
                  class="text-red-500 text-sm font-semibold"
                >
                  거절됨
                </span>
              </div>

              <div v-if="invite.status === 'PENDING'" class="flex gap-2 mt-2">
                <button
                  class="flex-1 bg-blue-600 text-white text-sm py-1 rounded hover:bg-blue-700"
                  @click="respondToInvite(invite.id, 'ACCEPTED')"
                >
                  수락
                </button>
                <button
                  class="flex-1 bg-gray-200 text-gray-800 text-sm py-1 rounded hover:bg-gray-300"
                  @click="respondToInvite(invite.id, 'DECLINED')"
                >
                  거절
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
import { ref, computed, onMounted } from 'vue'
import { useUserStore } from '@/store/user'
import { userAi } from '@/axios'
import { useRouter } from 'vue-router'
import api from '@/api'
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

const showDeleteModal = ref(false)
const tripToDelete = ref(null)

const confirmDelete = (trip) => {
  tripToDelete.value = trip
  showDeleteModal.value = true
}

const deleteTrip = async () => {
  if (!tripToDelete.value) return

  try {
    await api.delete(`/api/v1/plan/${tripToDelete.value.id}/user/${user.value.id}`)

    // 목록에서 삭제된 여행계획 제거
    trips.value = trips.value.filter((trip) => trip.id !== tripToDelete.value.id)

    showDeleteModal.value = false
    tripToDelete.value = null
    alert('여행계획이 성공적으로 삭제되었습니다.')
  } catch (error) {
    console.error('여행계획 삭제 실패:', error)
    if (error.response?.status === 403) {
      alert('삭제 권한이 없습니다.')
    } else {
      alert('여행계획 삭제에 실패했습니다.')
    }
  }
}

const toggleTag = (type, id) => {
  if (type === 'personal') {
    if (selectedUserTagIds.value.includes(id)) {
      selectedUserTagIds.value = selectedUserTagIds.value.filter((tagId) => tagId !== id)
    } else {
      selectedUserTagIds.value.push(id)
    }
  } else {
    if (selectedTripTagIds.value.includes(id)) {
      selectedTripTagIds.value = selectedTripTagIds.value.filter((tagId) => tagId !== id)
    } else {
      selectedTripTagIds.value.push(id)
    }
  }
}

const saveTags = async (type) => {
  try {
    let selected
    if (type === 'personal') {
      selected = selectedUserTagIds.value
    } else {
      selected = selectedTripTagIds.value
    }

    await userAi.patch(`/api/v1/tag/user`, {
      userId: user.value.id,
      type,
      tagIds: selected,
    })

    // 저장 후 태그 다시 가져오기
    const selectedNames = await userAi.get(`/api/v1/tag/user/${type}/${user.value.id}`)

    if (type === 'personal') {
      userTags.value = selectedNames.data
    } else {
      tripTags.value = selectedNames.data
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

// 날짜 포맷팅 함수 추가
const formatDate = (dateString) => {
  if (!dateString) return ''
  const date = new Date(dateString)
  return `${date.getFullYear()}.${String(date.getMonth() + 1).padStart(2, '0')}.${String(date.getDate()).padStart(2, '0')}`
}

const fetchMyTrips = async () => {
  try {
    const response = await api.get(`/api/v1/chat/chatroom/${user.value.id}`)
    // null인 여행계획은 제외
    trips.value = (response.data || []).filter((plan) => plan !== null)
  } catch (error) {
    console.error('내 여행 계획 조회 실패:', error)
    trips.value = []
  }
}

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
  const all = await userAi.get('/api/v1/tag/trip', { params: { type: '여행' } })
  allTripTags.value = all.data

  const selectedNames = await userAi.get(`api/v1/tag/user/trip/${user.value.id}`)
  tripTags.value = selectedNames.data
  const tagNames = selectedNames.data

  selectedTripTagIds.value = allTripTags.value
    .filter((tag) => tagNames.includes(tag.name))
    .map((tag) => tag.id)
}

const deleteUser = () => {
  userStore.deleteUser()
  router.push('/')
}

const trips = ref([])
const allowInvites = ref(false)

const activeTab = ref('edit')
const tabs = [
  { id: 'edit', name: '회원정보 수정' },
  { id: 'trips', name: '내 여행 계획' },
  { id: 'invites', name: '동행 요청' },
]

onMounted(async () => {
  editUser.value = { ...user.value }
  if (editUser.value.age == null) editUser.value.age = '기타'
  if (editUser.value.gender == null) editUser.value.gender = 'O'
  if (editUser.value.address == null) editUser.value.address = '기타'

  console.log('유저', editUser)

  await getUserTag()
  await getTripTag()
  await fetchMyTrips() // 내 여행 계획 가져오기 추가
  await fetchSidos()
  await fetchInvites()
  await fetchRequests()
})

const sidos = ref([])
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

// -------------------------
const invites = ref([])

const fetchInvites = async () => {
  try {
    const res = await api.get(`/api/v1/chat/invite/${userStore.loginUser.id}`)
    invites.value = res.data
    console.log('초대', invites.value)
  } catch (err) {
    console.error('초대 목록 불러오기 실패', err)
  }
}

const respondToInvite = async (inviteId, response) => {
  try {
    console.log('초대임', inviteId)
    await api.patch(`/api/v1/chat/invite/${inviteId}`, null, {
      params: { response }, // ✅ GET param으로 response 전송
    })

    // 프론트 상태 업데이트
    invites.value = invites.value.map((inv) =>
      inv.id === inviteId ? { ...inv, status: response } : inv,
    )
  } catch (err) {
    console.error('초대 응답 실패', err)
    alert('초대 응답에 실패했습니다.')
  }
}

const requests = ref([])

const fetchRequests = async () => {
  try {
    const res = await api.get(`/api/v1/chat/request/${userStore.loginUser.id}`)
    requests.value = res.data
  } catch (err) {
    console.error('참가 요청 목록 불러오기 실패', err)
  }
}
</script>
<style scoped></style>
