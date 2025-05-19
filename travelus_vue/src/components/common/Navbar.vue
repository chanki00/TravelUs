<template>
  <nav class="flex justify-between items-center py-4 px-6 md:px-12 bg-white/80 backdrop-blur-md sticky top-0 z-50 shadow-sm">
    <router-link to="/" class="flex items-center gap-2">
      <div class="w-8 h-8 rounded-full bg-gradient-to-r from-blue-500 to-blue-600 flex items-center justify-center">
        <span class="text-white font-bold text-sm">T</span>
      </div>
      <span class="font-bold text-xl text-blue-600">Travelus</span>
    </router-link>

    <div class="hidden md:flex items-center gap-8">
      <router-link to="/planner" class="text-gray-600 hover:text-blue-600 transition-colors">
        여행 플래너
      </router-link>
      <router-link to="/trip-recruitment" class="text-gray-600 hover:text-blue-600 transition-colors">
        여행 인원 모집
      </router-link>
      <router-link to="/companion" class="text-gray-600 hover:text-blue-600 transition-colors">
        동행자 찾기
      </router-link>
      <router-link to="/trip-board" class="text-gray-600 hover:text-blue-600 transition-colors">
        여행 공유 게시판
      </router-link>
    </div>

    <div class="flex items-center gap-4">
        <router-link to="/login">
    <button class="hidden md:inline-flex bg-transparent hover:bg-accent hover:text-accent-foreground px-4 py-2 rounded-md text-sm font-medium">
        로그인
        </button>
    </router-link>
              <router-link to="/regist"><button class="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded-md text-sm font-medium">
        회원가입
      </button></router-link>

      
    </div>
  </nav>
</template>

<style scoped>
/* 필요한 스타일 추가 */
</style>