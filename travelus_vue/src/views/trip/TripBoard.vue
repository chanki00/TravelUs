<template>
  <div class="min-h-screen bg-gradient-to-b from-blue-50 to-white">

    <div class="py-12 px-6 max-w-7xl mx-auto">
      <div class="flex flex-col md:flex-row justify-between items-start md:items-center mb-8">
        <div>
          <h1 class="text-3xl font-bold mb-2">여행 계획 공유 게시판</h1>
          <p class="text-gray-600">다른 여행자들의 일정을 확인하고 참고해 보세요</p>
        </div>

        <button class="mt-4 md:mt-0 px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700">
          내 여행 계획 업로드
        </button>
      </div>

      <div class="mb-8">
        <div class="flex flex-col sm:flex-row justify-between gap-4">
          <div class="border-b w-full sm:w-auto">
            <div class="flex">
              <button 
                v-for="tab in tabs" 
                :key="tab.value"
                :class="[
                  'px-4 py-2',
                  activeFilter === tab.value ? 'border-b-2 border-blue-600 text-blue-600' : 'text-gray-500'
                ]"
                @click="activeFilter = tab.value"
              >
                {{ tab.label }}
              </button>
            </div>
          </div>

          <div class="flex">
            <input
              v-model="searchTerm"
              placeholder="여행 계획 검색하기"
              class="px-3 py-2 border rounded-l-md focus:outline-none focus:ring-1 focus:ring-blue-500"
            />
            <button class="px-4 py-2 bg-blue-600 text-white rounded-r-md hover:bg-blue-700">
              검색
            </button>
          </div>
        </div>
      </div>

      <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        <router-link 
          v-for="trip in trips" 
          :key="trip.id"
          :to="`/tripdetail/${trip.id}`"
          class="block"
        >
          <div class="bg-white rounded-lg border shadow-sm overflow-hidden hover:shadow-lg transition-shadow h-full">
            <div class="h-48 overflow-hidden relative">
              <img
                :src="trip.image"
                :alt="trip.title"
                class="w-full h-full object-cover hover:scale-105 transition-transform duration-300"
              />
              <div class="absolute top-3 left-3">
                <span class="px-2 py-1 bg-white/70 backdrop-blur-sm text-gray-800 text-xs rounded-full">
                  {{ trip.location }}
                </span>
              </div>
            </div>
            <div class="p-4">
              <h3 class="font-medium text-lg mb-2">{{ trip.title }}</h3>

              <div class="flex flex-wrap gap-1 mb-3">
                <span 
                  v-for="(tag, index) in trip.tags" 
                  :key="index"
                  class="px-2 py-1 bg-blue-50 text-blue-600 text-xs rounded-full"
                >
                  {{ tag }}
                </span>
              </div>

              <div class="flex items-center justify-between">
                <div class="flex items-center gap-2">
                  <div class="w-6 h-6 rounded-full bg-gray-200 flex items-center justify-center">
                    <span class="text-xs">{{ trip.author.charAt(0) }}</span>
                  </div>
                  <span class="text-xs text-gray-600">{{ trip.author }}</span>
                </div>

                <div class="flex items-center gap-3 text-xs text-gray-500">
                  <span>♥ {{ trip.likes }}</span>
                  <span>💬 {{ trip.comments }}</span>
                </div>
              </div>
            </div>
          </div>
        </router-link>
      </div>

      <div class="mt-8 flex justify-center">
        <button class="px-4 py-2 border rounded-md hover:bg-gray-50">
          더 보기
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const activeFilter = ref('all')
const searchTerm = ref('')

const tabs = [
  { value: 'all', label: '전체' },
  { value: 'popular', label: '인기순' },
  { value: 'recent', label: '최신순' },
  { value: 'recommended', label: '추천순' }
]

const trips = [
  {
    id: "1",
    title: "제주도 4박 5일 힐링 여행",
    location: "제주도",
    author: "여행자123",
    image:
      "https://images.unsplash.com/photo-1601621915196-2ad9b06857b3?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=800&q=80",
    tags: ["힐링", "자연", "맛집"],
    likes: 153,
    comments: 24,
    created: "2023-09-15",
  },
  {
    id: "2",
    title: "서울 근교 당일치기",
    location: "경기도",
    author: "주말탐험가",
    image:
      "https://images.unsplash.com/photo-1597559833761-e6b943d4e80d?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=800&q=80",
    tags: ["당일치기", "가족", "드라이브"],
    likes: 87,
    comments: 12,
    created: "2023-10-02",
  },
  {
    id: "3",
    title: "부산 해운대 2박 3일 여행",
    location: "부산",
    author: "바다사랑",
    image:
      "https://images.unsplash.com/photo-1578037571214-25e2624a6cb6?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=800&q=80",
    tags: ["해변", "맛집", "도시"],
    likes: 210,
    comments: 35,
    created: "2023-08-22",
  },
  {
    id: "4",
    title: "강원도 양양 서핑 여행",
    location: "강원도",
    author: "서퍼boy",
    image:
      "https://images.unsplash.com/photo-1502680390469-be75c86b636f?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=800&q=80",
    tags: ["서핑", "액티비티", "바다"],
    likes: 165,
    comments: 19,
    created: "2023-09-28",
  },
  {
    id: "5",
    title: "전주 한옥마을 1박 2일",
    location: "전주",
    author: "역사탐험가",
    image:
      "https://images.unsplash.com/photo-1615884241431-d970dd79f60a?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=800&q=80",
    tags: ["역사", "문화", "한식"],
    likes: 132,
    comments: 27,
    created: "2023-08-14",
  },
  {
    id: "6",
    title: "경주 역사 기행 3박 4일",
    location: "경주",
    author: "역사여행자",
    image:
      "https://images.unsplash.com/photo-1635830625698-3b9bd74671ca?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=800&q=80",
    tags: ["역사", "문화", "사찰"],
    likes: 94,
    comments: 15,
    created: "2023-10-05",
  },
]
</script>

<style scoped>
/* 추가 스타일이 필요한 경우 여기에 작성 */
</style>