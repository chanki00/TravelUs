<template>
  <div class="min-h-screen bg-gray-50 py-10 px-6 max-w-7xl mx-auto font-bmjua">
    <!-- Header -->
    <div class="flex justify-between items-center mb-6">
      <div>
        <h1 class="text-2xl font-bold">여행 계획 공유 게시판</h1>
        <p class="text-sm text-gray-500">다른 여행자들의 일정을 확인하고 참고해 보세요</p>
      </div>
      <button class="btn btn-neutral">내 여행 계획 업로드</button>
    </div>

    <!-- Filter & Search -->
    <div class="flex flex-wrap justify-between items-center mb-6 gap-2">
      <div class="tabs tabs-boxed">
        <a class="tab tab-active">전체</a>
        <a class="tab">인기</a>
        <a class="tab">최신순</a>
        <a class="tab">추천순</a>
      </div>
      <div class="flex gap-2">
        <input type="text" placeholder="여행 계획 검색하기" class="input input-bordered" />
        <button class="btn btn-outline">검색</button>
      </div>
    </div>

    <!-- Grid Cards -->
    <div class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-6">
      <div
        v-for="plan in travelPlans"
        :key="plan.id"
        class="card bg-white shadow-sm border rounded-xl overflow-hidden"
      >
        <figure v-if="plan.image">
          <img :src="plan.image" alt="" class="w-full h-48 object-cover" />
        </figure>
        <div class="card-body p-4">
          <div class="badge badge-outline mb-1">{{ plan.region }}</div>
          <h2 class="font-semibold text-lg mb-1">{{ plan.title }}</h2>

          <div class="flex flex-wrap gap-1 text-xs mb-2">
            <span v-for="tag in plan.tags" :key="tag" class="badge badge-sm badge-ghost">{{
              tag
            }}</span>
          </div>

          <div class="flex justify-between text-sm text-gray-500">
            <span>👤 {{ plan.author }}</span>
            <div class="flex items-center gap-2">
              <span>❤️ {{ plan.likes }}</span>
              <span> {{ plan.views }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 더 보기 버튼 -->
    <div class="mt-10 flex justify-center">
      <button class="btn">더 보기</button>
    </div>
  </div>
</template>

<script setup>
const travelPlans = [
  {
    id: 1,
    region: '제주도',
    title: '제주도 4박 5일 힐링 여행',
    tags: ['힐링', '자연', '맛집'],
    author: '여행자123',
    likes: 153,
    views: 24,
    image: '',
  },
  {
    id: 2,
    region: '서울',
    title: '서울 근교 당일치기',
    tags: ['당일치기', '가족', '드라이브'],
    author: '주말탐방가',
    likes: 87,
    views: 12,
    image: '',
  },
  {
    id: 3,
    region: '부산',
    title: '부산 해운대 2박 3일 여행',
    tags: ['해변', '맛집', '도시'],
    author: '바다사랑',
    likes: 210,
    views: 35,
    image: '',
  },
  {
    id: 4,
    region: '강원도',
    title: '강원도 양양 서핑 여행',
    tags: ['서핑', '액티비티', '바다'],
    author: '서핑boy',
    likes: 165,
    views: 19,
    image: '',
  },
  {
    id: 5,
    region: '전주',
    title: '전주 한옥마을 1박 2일',
    tags: ['역사', '한옥', '먹방'],
    author: '역사탐험가',
    likes: 132,
    views: 27,
    image: '',
  },
  {
    id: 6,
    region: '경주',
    title: '경주 역사 기행 3박 4일',
    tags: ['역사', '문화', '사찰'],
    author: '역사연구자',
    likes: 94,
    views: 15,
    image: '',
  },
]
</script>

<style scoped></style>
