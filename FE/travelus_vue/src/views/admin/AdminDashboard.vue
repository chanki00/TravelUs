<template>
  <div class="min-h-screen bg-gray-50">
    <div class="py-8 px-6 max-w-7xl mx-auto">
      <div class="flex flex-col md:flex-row justify-between items-start md:items-center mb-8">
        <div>
          <h1 class="text-3xl font-bold mb-2">관리자 대시보드</h1>
          <p class="text-gray-500">Travelus 사이트 관리를 위한 대시보드입니다</p>
        </div>
        <div class="mt-4 md:mt-0">
          <input
            v-model="searchTerm"
            placeholder="검색..."
            class="max-w-xs px-3 py-2 border rounded-md"
          />
        </div>
      </div>

      <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6 mb-8">
        <div v-for="(stat, index) in stats" :key="index" class="card bg-base-100 shadow-sm">
          <div class="card-body p-6 flex justify-between items-center">
            <div>
              <p class="text-sm text-gray-500">{{ stat.title }}</p>
              <h3 class="text-2xl font-bold mt-2">{{ stat.value }}</h3>
            </div>
            <div
              :class="[
                'text-sm font-semibold',
                stat.change.startsWith('+') ? 'text-green-600' : 'text-red-600'
              ]"
            >
              {{ stat.change }}
            </div>
          </div>
        </div>
      </div>

      <div class="tabs mb-8">
        <a
          v-for="tab in ['users', 'trips']"
          :key="tab"
          :class="['tab tab-bordered', activeTab === tab ? 'tab-active' : '']"
          @click="activeTab = tab"
        >
          {{ tabLabels[tab] }}
        </a>
      </div>

      <!-- 사용자 목록 -->
      <div v-if="activeTab === 'users'" class="card bg-base-100 shadow-sm">
        <div class="card-header p-6">
          <h2 class="card-title">사용자 목록</h2>
          <p class="text-gray-500">전체 {{ users.length }}명의 사용자가 등록되어 있습니다</p>
        </div>
        <div class="card-body p-0">
          <div class="overflow-x-auto">
            <table class="table w-full">
              <thead>
                <tr>
                  <th>글번호</th>
                  <th>아이디</th>
                  <th>이름</th>
                  <th>이메일</th>
                  <th>성별</th>
                  <th>가입일</th>
                  <th>관리</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="user in filteredUsers" :key="user.id">
                  <td class="font-medium">{{ user.id }}</td>
                  <td>{{ user.userId }}</td>
                  <td>{{ user.name }}</td>
                  <td>{{ user.userEmail }}</td>
                  <td>{{ user.gender }}</td>
                  <td>{{ user.createdAt }}</td>
                  <td>
                    <button
                      @click="deleteUser(user.id)"
                      class="btn btn-ghost btn-sm h-8 text-red-600"
                    >
                      삭제
                    </button>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>

      <!-- 여행 계획 목록 -->
      <div v-if="activeTab === 'trips'" class="card bg-base-100 shadow-sm">
        <div class="card-header p-6">
          <h2 class="card-title">여행 계획 목록</h2>
          <p class="text-gray-500">전체 {{ trips.length }}개의 여행 계획이 등록되어 있습니다</p>
        </div>
        <div class="card-body p-0">
          <div class="overflow-x-auto">
            <table class="table w-full">
              <thead>
                <tr>
                  <th>글번호</th>
                  <th>제목</th>
                  <th>작성자</th>
                  <th>지역</th>
                  <th>작성일</th>
                  <th>좋아요수</th>
                  <th>관리</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="trip in filteredTrips" :key="trip.id">
                  <td class="font-medium">{{ trip.id }}</td>
                  <td>{{ trip.title }}</td>
                  <td>{{ trip.userId }}</td>
                  <td>{{ getSidoName(Number(trip.destination)) }}</td>
                  <td>{{ trip.createdAt }}</td>
                  <td>{{ trip.likes }}</td>
                  <td>
                    <div class="flex gap-2">
                      <button
                        class="btn btn-ghost btn-sm h-8 text-red-600"
                        @click="deleteTrip(trip.id)"
                      >
                        삭제
                      </button>
                    </div>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>

    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import api from '@/api'

const searchTerm = ref('')
const activeTab = ref('users')

const users = ref([])
const sidos = ref([])
const trips = ref([])

const stats = ref([
  { title: '총 사용자', value: '0', change: '+0%' },
  { title: '여행 계획 수', value: '0', change: '+0%' },
  { title: '동행 매칭 수', value: '572', change: '+8.3%' },
])

const tabLabels = {
  users: '사용자 관리',
  trips: '여행 계획 관리',
}

const fetchTrips = async () => {
  try {
    const res = await api.get('/api/v1/plan')
    const tripList = res.data

    tripList.forEach((trip) => {
      const user = users.value.find((u) => u.id === trip.userId)
      trip.userId = user ? user.userId : '알 수 없음'
    })

    trips.value = tripList

    const tripStat = stats.value.find((stat) => stat.title === '여행 계획 수')
    if (tripStat) {
      tripStat.value = tripList.length.toString()
    }
  } catch (error) {
    console.error('여행 계획 목록 불러오기 실패:', error)
  }
}

const fetchUsers = async () => {
  try {
    const res = await api.get('/api/v1/user')
    users.value = res.data

    const userStat = stats.value.find((stat) => stat.title === '총 사용자')
    if (userStat) {
      userStat.value = users.value.length.toString()
    }
  } catch (error) {
    console.error('사용자 목록 불러오기 실패:', error)
  }
}

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

// 필터 함수
const filterData = (data, term) => {
  if (!term) return data
  return data.filter((item) =>
    Object.values(item).some((value) =>
      value.toString().toLowerCase().includes(term.toLowerCase()),
    ),
  )
}

const filteredUsers = computed(() => filterData(users.value, searchTerm.value))
const filteredTrips = computed(() => filterData(trips.value, searchTerm.value))

const deleteUser = async (id) => {
  if (!confirm('정말 이 사용자를 삭제하시겠습니까?')) return
  try {
    await api.delete(`/api/v1/user/${id}`)
    await fetchUsers()
  } catch (error) {
    console.error(`사용자 ${id} 삭제 실패:`, error)
  }
}

const deleteTrip = async (planId) => {
  if (!confirm('정말 삭제하시겠습니까?')) return

  try {
    await api.delete(`/api/v1/plan/${planId}`)
    trips.value = trips.value.filter((trip) => trip.id !== planId)

    const tripStat = stats.value.find((stat) => stat.title === '여행 계획 수')
    if (tripStat) {
      tripStat.value = trips.value.length.toString()
    }
  } catch (error) {
    console.error('삭제 실패:', error)
  }
}

onMounted(() => {
  fetchUsers()
  fetchTrips()
  fetchSidos()
})
</script>

<style scoped>
/* 필요 시 추가 스타일 작성 가능 */
</style>
