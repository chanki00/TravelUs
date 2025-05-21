<template>
  <div class="min-h-screen bg-gray-50 py-10 px-6 max-w-7xl mx-auto font-bmjua">
    <div class="flex justify-between items-center mb-6">
      <div>
        <h1 class="text-2xl font-bold">여행 계획 공유 게시판</h1>
        <p class="text-sm text-gray-500">다른 여행자들의 일정을 확인하고 참고해 보세요</p>
      </div>
      <button @click="openModal" class="btn btn-neutral">내 여행 계획 업로드</button>
    </div>

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

    <div class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-6">
      <div
        v-for="plan in tripPlans"
        :key="plan.id"
        class="card bg-white shadow-sm border rounded-xl overflow-hidden"
      >
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
              <span>👁️ {{ plan.views }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="mt-10 flex justify-center">
      <button class="btn">더 보기</button>
    </div>
  </div>

   <div v-if="isModalOpen" class="fixed inset-0 bg-black bg-opacity-50 z-40 flex items-center justify-center p-4" @click="closeModal">
      <!-- 모달 컨테이너 -->
      <div class="bg-white rounded-xl shadow-xl w-full max-w-4xl max-h-[90vh] overflow-hidden flex flex-col" @click.stop>
        <!-- 모달 헤더 -->
        <div class="p-4 border-b flex justify-between items-center bg-gray-50">
          <h3 class="text-xl font-bold">내 여행 계획 선택</h3>
          <button @click="closeModal" class="btn btn-sm btn-circle btn-ghost">✕</button>
        </div>

        <!-- 모달 내용 -->
        <div class="p-4 overflow-y-auto flex-1">
          <!-- 검색 및 필터 -->
          <div class="flex gap-2 mb-4">
            <input type="text" v-model="searchQuery" placeholder="여행 계획 검색" class="input input-bordered flex-1" />
            <select v-model="sortBy" class="select select-bordered">
              <option value="recent">최신순</option>
              <option value="name">이름순</option>
              <option value="duration">기간순</option>
            </select>
          </div>

          <!-- 여행 계획 목록 -->
          <div v-if="filteredPlans.length > 0" class="grid grid-cols-1 sm:grid-cols-2 gap-4">
            <div 
              v-for="plan in filteredPlans" 
              :key="plan.id"
              :class="['card bg-white border rounded-lg overflow-hidden cursor-pointer transition-all hover:shadow-md', 
                selectedPlan?.id === plan.id ? 'border-2 border-blue-500 shadow-md' : '']"
              @click="selectPlan(plan)"
            >
              <div class="card-body p-4">
                <div class="flex justify-between items-start">
                  <div>
                    <div class="badge badge-outline mb-1">{{ plan.destination }}</div>
                    <h3 class="font-semibold text-lg">{{ plan.title }}</h3>
                    <p class="text-sm text-gray-500">{{ plan.duration }}박 {{ plan.duration + 1 }}일</p>
                  </div>
                  <div class="flex flex-col items-end">
                    <span class="text-xs text-gray-500">{{ formatDate(plan.createdAt) }}</span>
                    <div class="mt-2">
                      <span v-if="selectedPlan?.id === plan.id" class="badge badge-primary">선택됨</span>
                    </div>
                  </div>
                </div>
                
                <div class="flex flex-wrap gap-1 mt-2">
                  <span v-for="tag in plan.tags" :key="tag" class="badge badge-sm badge-ghost">{{ tag }}</span>
                </div>
                
                <div class="mt-2 flex items-center text-sm text-gray-500">
                  <span class="flex items-center">
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 mr-1" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
                    </svg>
                    {{ formatDate(plan.updatedAt) }} 수정
                  </span>
                </div>
              </div>
            </div>
          </div>
          
          <!-- 여행 계획이 없을 때 -->
          <div v-else class="flex flex-col items-center justify-center py-10">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-16 w-16 text-gray-300 mb-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 20l-5.447-2.724A1 1 0 013 16.382V5.618a1 1 0 011.447-.894L9 7m0 13l6-3m-6 3V7m6 10l4.553 2.276A1 1 0 0021 18.382V7.618a1 1 0 00-.553-.894L15 4m0 13V4m0 0L9 7" />
            </svg>
            <p class="text-gray-500 text-lg">저장된 여행 계획이 없습니다</p>
            <button class="btn btn-outline mt-4">새 여행 계획 만들기</button>
          </div>
        </div>

        <!-- 모달 푸터 -->
        <div class="p-4 border-t flex justify-between items-center bg-gray-50">
          <span class="text-sm text-gray-500">
            {{ filteredPlans.length }}개의 여행 계획
          </span>
          <div class="flex gap-2">
            <button @click="closeModal" class="btn btn-outline">취소</button>
            <button 
              @click="applySelection" 
              class="btn btn-primary" 
              :disabled="!selectedPlan"
            >
              적용하기
            </button>
          </div>
        </div>
      </div>
    </div>
</template>

<script setup>
const tripPlans = [
  {
    id: 1,
    region: '제주도',
    title: '제주도 4박 5일 힐링 여행',
    tags: ['#힐링', '자연', '맛집'],
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
