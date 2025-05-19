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
            추천순
          </button>
          <button 
            :class="[
              'px-4 py-2 rounded-md',
              activeFilter === 'latest' 
                ? 'bg-primary text-white' 
                : 'bg-white border border-gray-300 text-gray-700'
            ]"
            @click="activeFilter = 'latest'"
          >
            최신순
          </button>
          <button 
            :class="[
              'px-4 py-2 rounded-md',
              activeFilter === 'location' 
                ? 'bg-primary text-white' 
                : 'bg-white border border-gray-300 text-gray-700'
            ]"
            @click="activeFilter = 'location'"
          >
            지역순
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
            <option value="40s">40대 이상</option>
          </select>

          <select class="w-[120px] px-3 py-2 border border-gray-300 rounded-md">
            <option value="all">모두</option>
            <option value="relax">휴양/힐링</option>
            <option value="active">액티비티</option>
            <option value="food">맛집 탐방</option>
            <option value="culture">문화/역사</option>
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
                  <span>{{ companion.gender }}</span>
                  <span>•</span>
                  <span>{{ companion.location }}</span>
                </div>
              </div>
            </div>

            <div class="mb-4">
              <div class="text-sm text-gray-500 mb-1">성격</div>
              <div class="flex flex-wrap gap-2">
                <span 
                  v-for="(item, index) in companion.personality" 
                  :key="index"
                  class="px-2 py-1 text-xs rounded-full bg-purple-50 border border-purple-100"
                >
                  {{ item }}
                </span>
              </div>
            </div>

            <div class="mb-4">
              <div class="text-sm text-gray-500 mb-1">선호 여행 스타일</div>
              <div class="flex flex-wrap gap-2">
                <span 
                  v-for="(item, index) in companion.preference" 
                  :key="index"
                  class="px-2 py-1 text-xs rounded-full bg-green-50 border border-green-100"
                >
                  {{ item }}
                </span>
              </div>
            </div>

            <div class="flex justify-between items-center mt-6">
              <div>
                <div class="text-xs text-gray-500">매칭 점수</div>
                <div class="font-bold text-purple-600">{{ companion.match }}%</div>
              </div>
              <div class="flex gap-2">
                <button class="px-3 py-1 text-sm border border-gray-300 rounded-md">
                  프로필
                </button>
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
import { ref } from 'vue';

const activeFilter = ref('recommended');

const companions = [
  {
    id: "1",
    name: "여행자123",
    age: "20대",
    gender: "남성",
    location: "서울",
    profile:
      "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=100&q=80",
    personality: ["친절함", "조용함", "계획적"],
    preference: ["자연", "사진", "맛집"],
    match: 92,
  },
  {
    id: "2",
    name: "여행좋아",
    age: "30대",
    gender: "여성",
    location: "부산",
    profile:
      "https://images.unsplash.com/photo-1494790108377-be9c29b29330?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=100&q=80",
    personality: ["활발함", "사교적", "즉흥적"],
    preference: ["음식", "문화", "쇼핑"],
    match: 85,
  },
  {
    id: "3",
    name: "산책러버",
    age: "20대",
    gender: "여성",
    location: "인천",
    profile:
      "https://images.unsplash.com/photo-1438761681033-6461ffad8d80?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=100&q=80",
    personality: ["차분함", "예술적", "꼼꼼함"],
    preference: ["하이킹", "풍경", "카페"],
    match: 78,
  },
  {
    id: "4",
    name: "바다사랑",
    age: "30대",
    gender: "남성",
    location: "제주",
    profile:
      "https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=100&q=80",
    personality: ["유머러스", "모험적", "여유로움"],
    preference: ["해변", "수영", "서핑"],
    match: 64,
  },
  {
    id: "5",
    name: "도시탐험가",
    age: "40대",
    gender: "여성",
    location: "대구",
    profile:
      "https://images.unsplash.com/photo-1573496359142-b8d87734a5a2?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=100&q=80",
    personality: ["지적", "열정적", "호기심"],
    preference: ["역사", "박물관", "도시"],
    match: 58,
  },
];
</script>

<style scoped>
.bg-primary {
  @apply bg-purple-600;
}
</style>
