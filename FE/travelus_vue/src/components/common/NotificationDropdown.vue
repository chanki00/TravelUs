<template>
  <div class="relative">
    <!-- 알림 버튼 -->
    <button
      @click="toggleDropdown"
      class="relative p-2 text-gray-600 hover:text-gray-800 focus:outline-none"
    >
      <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
        <path
          stroke-linecap="round"
          stroke-linejoin="round"
          stroke-width="2"
          d="M15 17h5l-1.405-1.405A2.032 2.032 0 0118 14.158V11a6.002 6.002 0 00-4-5.659V5a2 2 0 10-4 0v.341C7.67 6.165 6 8.388 6 11v3.159c0 .538-.214 1.055-.595 1.436L4 17h5m6 0v1a3 3 0 11-6 0v-1m6 0H9"
        />
      </svg>
      <!-- 알림 개수 배지 -->
      <span
        v-if="totalNotifications > 0"
        class="absolute -top-1 -right-1 bg-red-500 text-white text-xs rounded-full h-5 w-5 flex items-center justify-center"
      >
        {{ totalNotifications > 99 ? '99+' : totalNotifications }}
      </span>
    </button>

    <!-- 드롭다운 메뉴 -->
    <div
      v-if="showDropdown"
      class="absolute right-0 mt-2 w-80 bg-white rounded-lg shadow-lg border border-gray-200 z-50"
    >
      <div class="p-4 border-b border-gray-200">
        <h3 class="text-lg font-semibold text-gray-800">알림</h3>
      </div>

      <div class="max-h-96 overflow-y-auto">
        <!-- 받은 초대 -->
        <div v-if="notifications.receivedInvites?.length > 0" class="p-2">
          <h4 class="text-sm font-medium text-gray-600 mb-2">받은 초대</h4>
          <div
            v-for="invite in notifications.receivedInvites"
            :key="`invite-${invite.id}`"
            class="p-3 mb-2 bg-blue-50 rounded-lg border border-blue-200"
          >
            <div class="flex justify-between items-start">
              <div class="flex-1">
                <p class="text-sm font-medium text-gray-800">
                  {{ invite.name }}님이 여행에 초대했습니다
                </p>
                <p class="text-xs text-gray-600 mt-1">{{ invite.tripPlanTitle }}</p>
                <p class="text-xs text-gray-500 mt-1">
                  {{ formatDate(invite.createdAt) }}
                </p>
              </div>
            </div>
            <div class="flex gap-2 mt-2">
              <button
                @click="respondToInvite(invite.id, 'ACCEPTED') "
                class="px-3 py-1 bg-blue-500 text-white text-xs rounded hover:bg-blue-600"
              >
                수락
              </button>
              <button
                @click="respondToInvite(invite.id, 'REJECTED')"
                class="px-3 py-1 bg-gray-300 text-gray-700 text-xs rounded hover:bg-gray-400"
              >
                거절
              </button>
            </div>
          </div>
        </div>

        <!-- 받은 참여 요청 -->
        <div v-if="notifications.receivedRequests?.length > 0" class="p-2">
          <h4 class="text-sm font-medium text-gray-600 mb-2">받은 참여 요청</h4>
          <div
            v-for="request in notifications.receivedRequests"
            :key="`request-${request.id}`"
            class="p-3 mb-2 bg-green-50 rounded-lg border border-green-200"
          >
            <div class="flex justify-between items-start">
              <div class="flex-1">
                <p class="text-sm font-medium text-gray-800">
                  {{ request.name }}님이 참여를 요청했습니다
                </p>
                <p class="text-xs text-gray-600 mt-1">{{ request.tripPlanTitle }}</p>
                <p class="text-xs text-gray-500 mt-1">
                  {{ formatDate(request.createdAt) }}
                </p>
              </div>
            </div>
            <div class="flex gap-2 mt-2">
              <button
                @click="respondToJoin(request.id, 'ACCEPTED')"
                class="px-3 py-1 bg-green-500 text-white text-xs rounded hover:bg-green-600"
              >
                수락
              </button>
              <button
                @click="respondToJoin(request.id, 'REJECTED')"
                class="px-3 py-1 bg-gray-300 text-gray-700 text-xs rounded hover:bg-gray-400"
              >
                거절
              </button>
            </div>
          </div>
        </div>

        <!-- 보낸 요청 상태 -->
        <div v-if="notifications.sentRequests?.length > 0" class="p-2">
          <h4 class="text-sm font-medium text-gray-600 mb-2">보낸 요청</h4>
          <div
            v-for="sent in notifications.sentRequests"
            :key="`sent-${sent.id}`"
            class="p-3 mb-2 bg-yellow-50 rounded-lg border border-yellow-200"
          >
            <div class="flex justify-between items-start">
              <div class="flex-1">
                <p class="text-sm font-medium text-gray-800">
                  {{ sent.tripPlanTitle }}
                </p>
                <p class="text-xs text-gray-600 mt-1">
                  상태:
                  <span :class="getStatusClass(sent.status)">
                    {{ getStatusText(sent.status) }}
                  </span>
                </p>
                <p class="text-xs text-gray-500 mt-1">
                  {{ formatDate(sent.createdAt) }}
                </p>
              </div>
            </div>
          </div>
        </div>

        <!-- 알림이 없을 때 -->
        <div v-if="totalNotifications === 0" class="p-4 text-center text-gray-500">
          새로운 알림이 없습니다
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { userAi } from '@/axios'
import { useUserStore } from '@/store/user'

const userStore = useUserStore()
const showDropdown = ref(false)
const notifications = ref({
  receivedInvites: [],
  receivedRequests: [],
  sentRequests: [],
  totalCount: 0,
})

const totalNotifications = computed(() => {
  return notifications.value.receivedInvites.length + notifications.value.receivedRequests.length
})

const toggleDropdown = () => {
  showDropdown.value = !showDropdown.value
  if (showDropdown.value) {
    fetchNotifications()
  }
}

const fetchNotifications = async () => {
  try {
    const response = await userAi.get(`/api/v1/chat/notifications/${userStore.loginUser.id}`)
    notifications.value = response.data
  } catch (error) {
    console.error('알림 조회 실패:', error)
  }
}

const respondToInvite = async (inviteId, response) => {
  try {
    await userAi.patch(`/api/v1/chat/invite/${inviteId}?response=${response}`)
    await fetchNotifications() // 알림 새로고침

    if (response === 'ACCEPTED') {
      alert('수락했습니다!')
    } else {
      alert('거절했습니다.')
    }
  } catch (error) {
    console.error('응답 처리 실패:', error)
    alert('처리 중 오류가 발생했습니다.')
  }
}


const respondToJoin = async (inviteId, response) => {
  try {
    await userAi.patch(`/api/v1/chat/join/${inviteId}?response=${response}`)
    await fetchNotifications() // 알림 새로고침

    if (response === 'ACCEPTED') {
      alert('수락했습니다!')
    } else {
      alert('거절했습니다.')
    }
  } catch (error) {
    console.error('응답 처리 실패:', error)
    alert('처리 중 오류가 발생했습니다.')
  }
}


const formatDate = (dateString) => {
  const date = new Date(dateString)
  const now = new Date()
  const diff = now - date
  const minutes = Math.floor(diff / 60000)
  const hours = Math.floor(diff / 3600000)
  const days = Math.floor(diff / 86400000)

  if (minutes < 1) return '방금 전'
  if (minutes < 60) return `${minutes}분 전`
  if (hours < 24) return `${hours}시간 전`
  if (days < 7) return `${days}일 전`

  return date.toLocaleDateString()
}

const getStatusText = (status) => {
  switch (status) {
    case 'PENDING':
      return '대기중'
    case 'ACCEPTED':
      return '수락됨'
    case 'REJECTED':
      return '거절됨'
    default:
      return status
  }
}

const getStatusClass = (status) => {
  switch (status) {
    case 'PENDING':
      return 'text-yellow-600'
    case 'ACCEPTED':
      return 'text-green-600'
    case 'REJECTED':
      return 'text-red-600'
    default:
      return 'text-gray-600'
  }
}

// 컴포넌트 마운트 시 알림 조회
onMounted(() => {
  if (userStore.isLoggedIn) {
    fetchNotifications()
  }
})

// 외부 클릭 시 드롭다운 닫기
const handleClickOutside = (event) => {
  if (!event.target.closest('.relative')) {
    showDropdown.value = false
  }
}

onMounted(() => {
  document.addEventListener('click', handleClickOutside)
})

onUnmounted(() => {
  document.removeEventListener('click', handleClickOutside)
})
</script>
