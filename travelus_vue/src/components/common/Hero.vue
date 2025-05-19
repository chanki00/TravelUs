<template>
  <div class="hero-gradient py-16 md:py-24 px-6 md:px-12 flex flex-col items-center text-center animate-fade-in">
    <h1 class="text-4xl md:text-5xl lg:text-6xl font-bold mb-6">
      <span class="text-gradient">AI 기반 여행 플래너</span>
      <br />& 동행 매칭 플랫폼
    </h1>

    <p class="text-gray-600 max-w-2xl text-lg md:text-xl mb-10">
      Travelus와 함께 AI로 쉽게 여행 계획을 세우고, 나와 맞는 여행 동행자를 찾아 특별한 여행을 떠나보세요.
    </p>

    <div class="flex gap-4 flex-col sm:flex-row mb-12">
      <router-link to="/ai-planner">
        <button class="btn-primary">
          AI 여행 시작하기
        </button>
      </router-link>
      <router-link to="/companion">
        <button class="btn-secondary">
          동행자 찾기
        </button>
      </router-link>
    </div>

    <div class="flex justify-center relative w-full max-w-4xl">
      <div class="absolute -top-12 -left-8 w-24 h-24 bg-travel-blue rounded-full opacity-20 blur-2xl"></div>
      <div class="absolute -bottom-12 -right-8 w-32 h-32 bg-travel-peach rounded-full opacity-30 blur-2xl"></div>
      <img
        src="https://images.unsplash.com/photo-1469854523086-cc02fe5d8800?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1500&q=80"
        alt="Travel Adventure"
        class="rounded-2xl shadow-xl w-full h-64 md:h-80 object-cover"
      />
    </div>
  </div>
</template>

<style scoped>
/* 필요한 스타일 추가 */
</style>