
<template>
  <div class="min-h-screen bg-gray-50">
    
    <div class="container py-12 px-6">
      <h1 class="text-3xl font-bold mb-2">동행 찾기</h1>
      <p class="text-gray-600 mb-8">나와 맞는 여행 동행자를 찾아 특별한 여행을 떠나보세요</p>
      
      <div class="flex flex-col md:flex-row gap-6">
        <div class="w-full md:w-64 bg-white rounded-xl shadow-sm border p-4">
          <h3 class="font-semibold mb-4">필터</h3>
          
          <div class="mb-4">
            <label class="block text-sm font-medium mb-2">여행지</label>
            <select v-model="filters.destination" class="w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500">
              <option value="">전체</option>
              <option value="jeju">제주도</option>
              <option value="busan">부산</option>
              <option value="seoul">서울</option>
              <option value="gangwon">강원도</option>
              <option value="japan">일본</option>
              <option value="europe">유럽</option>
            </select>
          </div>
          
          <div class="mb-4">
            <label class="block text-sm font-medium mb-2">여행 기간</label>
            <div class="flex gap-2">
              <input v-model="filters.startDate" type="date" class="w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500" />
            </div>
          </div>
          
          <div class="mb-4">
            <label class="block text-sm font-medium mb-2">여행 테마</label>
            <div class="space-y-2">
              <label v-for="(theme, index) in travelThemes" :key="index" class="flex items-center">
                <input 
                  type="checkbox" 
                  :value="theme" 
                  v-model="filters.themes"
                  class="rounded border-gray-300 text-blue-600 shadow-sm focus:border-blue-500 focus:ring-blue-500" 
                />
                <span class="ml-2 text-sm">{{ theme }}</span>
              </label>
            </div>
          </div>
          
          <div class="mb-4">
            <label class="block text-sm font-medium mb-2">연령대</label>
            <div class="space-y-2">
              <label v-for="(age, index) in ageGroups" :key="index" class="flex items-center">
                <input 
                  type="checkbox" 
                  :value="age" 
                  v-model="filters.ageGroups"
                  class="rounded border-gray-300 text-blue-600 shadow-sm focus:border-blue-500 focus:ring-blue-500" 
                />
                <span class="ml-2 text-sm">{{ age }}</span>
              </label>
            </div>
          </div>
          
          <button 
            @click="applyFilters"
            class="w-full bg-blue-600 text-white rounded-md py-2 hover:bg-blue-700 transition-colors"
          >
            필터 적용
          </button>
        </div>
        
        <div class="flex-1">
          <div class="flex justify-between items-center mb-6">
            <div class="flex items-center">
              <span class="text-sm text-gray-500">총 {{ companions.length }}개의 동행</span>
            </div>
            
            <div class="flex items-center gap-2">
              <select v-model="sortBy" class="rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500 text-sm">
                <option value="recent">최신순</option>
                <option value="popular">인기순</option>
                <option value="closing">마감임박순</option>
              </select>
              
              <router-link to="/companion/create">
                <button class="inline-flex items-center justify-center text-sm font-medium transition-colors focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:opacity-50 disabled:pointer-events-none ring-offset-background bg-blue-600 text-white hover:bg-blue-700 h-9 rounded-md px-3">
                  동행 만들기
                </button>
              </router-link>
            </div>
          </div>
          
          <div v-if="isLoading" class="flex justify-center items-center py-12">
            <div class="text-center">
              <svg class="animate-spin h-8 w-8 text-blue-500 mx-auto mb-4" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
                <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
              </svg>
              <p class="text-gray-600">동행을 불러오는 중...</p>
            </div>
          </div>
          
          <div v-else-if="companions.length === 0" class="text-center py-12 bg-white rounded-xl shadow-sm border">
            <svg xmlns="http://www.w3.org/2000/svg" width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-search-x mx-auto mb-4 text-gray-400">
              <path d="m13.5 8.5-5 5"></path>
              <path d="m8.5 8.5 5 5"></path>
              <circle cx="11" cy="11" r="8"></circle>
              <path d="m21 21-4.3-4.3"></path>
            </svg>
            <p class="text-gray-600 mb-2">조건에 맞는 동행이 없습니다</p>
            <p class="text-sm text-gray-500 mb-4">다른 조건으로 검색하거나 직접 동행을 만들어보세요</p>
            <router-link to="/companion/create">
              <button class="inline-flex items-center justify-center text-sm font-medium transition-colors focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:opacity-50 disabled:pointer-events-none ring-offset-background bg-blue-600 text-white hover:bg-blue-700 h-9 rounded-md px-3">
                동행 만들기
              </button>
            </router-link>
          </div>
          
          <div v-else class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            <CompanionCard 
              v-for="(companion, index) in companions" 
              :key="index"
              :companion="companion"
            />
          </div>
          
          <div v-if="companions.length > 0" class="flex justify-center mt-8">
            <nav class="flex items-center gap-1">
              <button 
                @click="prevPage" 
                :disabled="currentPage === 1"
                class="inline-flex items-center justify-center rounded-md text-sm font-medium h-9 w-9 border border-input bg-background hover:bg-accent hover:text-accent-foreground disabled:opacity-50"
              >
                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-chevron-left">
                  <path d="m15 18-6-6 6-6"></path>
                </svg>
              </button>
              
              <button 
                v-for="page in totalPages" 
                :key="page"
                @click="goToPage(page)"
                :class="[
                  'inline-flex items-center justify-center rounded-md text-sm font-medium h-9 w-9 border border-input',
                  currentPage === page 
                    ? 'bg-primary text-primary-foreground hover:bg-primary/90' 
                    : 'bg-background hover:bg-accent hover:text-accent-foreground'
                ]"
              >
                {{ page }}
              </button>
              
              <button 
                @click="nextPage" 
                :disabled="currentPage === totalPages"
                class="inline-flex items-center justify-center rounded-md text-sm font-medium h-9 w-9 border border-input bg-background hover:bg-accent hover:text-accent-foreground disabled:opacity-50"
              >
                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-chevron-right">
                  <path d="m9 18 6-6-6-6"></path>
                </svg>
              </button>
            </nav>
          </div>
        </div>
      </div>
    </div>
    
  </div>
</template>

<script setup>

</script>
