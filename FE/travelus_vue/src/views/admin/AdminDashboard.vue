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

      <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
        <div v-for="(stat, index) in stats" :key="index" class="card bg-base-100 shadow-sm">
          <div class="card-body p-6">
            <div class="flex justify-between items-start">
              <div>
                <p class="text-sm text-gray-500">{{ stat.title }}</p>
                <h3 class="text-2xl font-bold mt-2">{{ stat.value }}</h3>
              </div>
              <div :class="`text-sm ${stat.change.startsWith('+') ? 'text-green-600' : 'text-red-600'}`">
                {{ stat.change }}
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="tabs mb-8">
        <a 
          v-for="tab in ['users', 'trips', 'inquiries']" 
          :key="tab"
          :class="['tab tab-bordered', activeTab === tab ? 'tab-active' : '']"
          @click="activeTab = tab"
        >
          {{ tabLabels[tab] }}
        </a>
      </div>

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
                  <td>{{ user.username }}</td>
                  <td>{{ user.name }}</td>
                  <td>{{ user.email }}</td>
                  <td>{{ user.gender }}</td>
                  <td>{{ user.joined }}</td>
                  <td>
                    <button class="btn btn-ghost btn-sm h-8 text-red-600">삭제</button>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>

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
                  <th>조회수</th>
                  <th>관리</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="trip in filteredTrips" :key="trip.id">
                  <td class="font-medium">{{ trip.id }}</td>
                  <td>{{ trip.title }}</td>
                  <td>{{ trip.author }}</td>
                  <td>{{ trip.location }}</td>
                  <td>{{ trip.created }}</td>
                  <td>{{ trip.views }}</td>
                  <td>
                    <div class="flex gap-2">
                      <button class="btn btn-outline btn-sm h-8">보기</button>
                      <button class="btn btn-ghost btn-sm h-8 text-red-600">삭제</button>
                    </div>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>

      <div v-if="activeTab === 'inquiries'" class="card bg-base-100 shadow-sm">
        <div class="card-header p-6">
          <h2 class="card-title">문의 내역</h2>
          <p class="text-gray-500">전체 {{ inquiries.length }}건의 문의가 등록되어 있습니다</p>
        </div>
        <div class="card-body p-0">
          <div class="overflow-x-auto">
            <table class="table w-full">
              <thead>
                <tr>
                  <th>글번호</th>
                  <th>제목</th>
                  <th>작성자</th>
                  <th>상태</th>
                  <th>작성일</th>
                  <th>관리</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="inquiry in filteredInquiries" :key="inquiry.id">
                  <td class="font-medium">{{ inquiry.id }}</td>
                  <td>{{ inquiry.title }}</td>
                  <td>{{ inquiry.author }}</td>
                  <td>
                    <span
                      :class="`px-2 py-1 rounded-full text-xs ${
                        inquiry.status === '대기중'
                          ? 'bg-yellow-100 text-yellow-800'
                          : inquiry.status === '처리중'
                            ? 'bg-blue-100 text-blue-800'
                            : 'bg-green-100 text-green-800'
                      }`"
                    >
                      {{ inquiry.status }}
                    </span>
                  </td>
                  <td>{{ inquiry.date }}</td>
                  <td>
                    <button class="btn btn-outline btn-sm h-8">답변</button>
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
import { ref, computed } from 'vue';

const searchTerm = ref('');
const activeTab = ref('users');

const tabLabels = {
  users: '사용자 관리',
  trips: '여행 계획 관리',
  inquiries: '문의 관리'
};

// Mock data
const stats = [
  { title: "총 사용자", value: "1,243", change: "+12.5%" },
  { title: "여행 계획 수", value: "3,876", change: "+23.1%" },
  { title: "동행 매칭 수", value: "572", change: "+8.3%" },
  { title: "미답변 문의", value: "14", change: "-2" },
];

const users = [
  {
    id: "1",
    username: "traveler123",
    name: "김여행",
    email: "traveler123@example.com",
    gender: "남성",
    joined: "2023-09-15",
  },
  {
    id: "2",
    username: "adventurer",
    name: "이모험",
    email: "adventure@example.com",
    gender: "여성",
    joined: "2023-09-20",
  },
  {
    id: "3",
    username: "explorer",
    name: "박탐험",
    email: "explorer@example.com",
    gender: "남성",
    joined: "2023-09-22",
  },
  {
    id: "4",
    username: "wanderlust",
    name: "최방랑",
    email: "wanderlust@example.com",
    gender: "여성",
    joined: "2023-09-25",
  },
  {
    id: "5",
    username: "globetrotter",
    name: "정세계",
    email: "globetrotter@example.com",
    gender: "남성",
    joined: "2023-09-28",
  },
];

const inquiries = [
  { id: "1", title: "여행 계획 공유 문제", author: "traveler123", status: "대기중", date: "2023-10-08" },
  { id: "2", title: "회원 정보 수정 안됨", author: "adventurer", status: "처리중", date: "2023-10-07" },
  { id: "3", title: "매칭 시스템 문의", author: "explorer", status: "완료", date: "2023-10-05" },
  { id: "4", title: "계정 삭제 요청", author: "wanderlust", status: "대기중", date: "2023-10-04" },
  { id: "5", title: "비밀번호 재설정 오류", author: "globetrotter", status: "완료", date: "2023-10-01" },
];

const trips = [
  {
    id: "1",
    title: "제주도 4박 5일 힐링 여행",
    author: "traveler123",
    location: "제주도",
    created: "2023-09-15",
    views: 324,
  },
  {
    id: "2",
    title: "서울 근교 당일치기",
    author: "adventurer",
    location: "경기도",
    created: "2023-10-02",
    views: 156,
  },
  {
    id: "3",
    title: "부산 해운대 2박 3일 여행",
    author: "explorer",
    location: "부산",
    created: "2023-08-22",
    views: 482,
  },
  {
    id: "4",
    title: "강원도 양양 서핑 여행",
    author: "wanderlust",
    location: "강원도",
    created: "2023-09-28",
    views: 209,
  },
  {
    id: "5",
    title: "전주 한옥마을 1박 2일",
    author: "globetrotter",
    location: "전주",
    created: "2023-08-14",
    views: 347,
  },
];

// Filter functions
const filterData = (data, term) => {
  if (!term) return data;
  return data.filter((item) =>
    Object.values(item).some((value) => 
      value.toString().toLowerCase().includes(term.toLowerCase())
    )
  );
};

const filteredUsers = computed(() => filterData(users, searchTerm.value));
const filteredTrips = computed(() => filterData(trips, searchTerm.value));
const filteredInquiries = computed(() => filterData(inquiries, searchTerm.value));
</script>

<style scoped>
/* 추가적인 스타일이 필요하면 여기에 작성하세요 */
</style>