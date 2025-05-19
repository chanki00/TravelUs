<template>
  <div class="min-h-screen">

    <div class="py-8 px-6 max-w-7xl mx-auto">
      <div class="flex flex-col md:flex-row justify-between items-start mb-6">
        <div>
          <h1 class="text-3xl font-bold mb-2">
            {{ destination }} {{ duration }} 여행 계획
          </h1>
          <p class="text-gray-500">여행 계획을 확인하고 참여자들과 의견을 나눠보세요</p>
        </div>

        <div class="flex gap-3 mt-4 md:mt-0">
          <trip-members :members="mockMembers" />
          <button class="inline-flex items-center justify-center border bg-background hover:bg-accent hover:text-accent-foreground h-10 px-4 py-2 rounded-md text-sm font-medium">일정 저장</button>
          <button class="inline-flex items-center justify-center border bg-background hover:bg-accent hover:text-accent-foreground h-10 px-4 py-2 rounded-md text-sm font-medium">공유하기</button>
          <button class="inline-flex items-center justify-center bg-primary text-primary-foreground hover:bg-primary/90 h-10 px-4 py-2 rounded-md text-sm font-medium">일정 수정</button>
        </div>
      </div>

      <div class="grid grid-cols-1 lg:grid-cols-12 gap-6">
        <!-- 장소 검색 패널 -->
        <div class="lg:col-span-3 order-2 lg:order-1">
          <!-- 탭 컴포넌트 및 장소 검색 UI -->
          <!-- 일정 목록 -->
        </div>

        <!-- 지도 -->
        <div class="lg:col-span-6 order-1 lg:order-2">
          <trip-planner-map height="800px" />
        </div>

        <!-- 채팅 영역 -->
        <div class="lg:col-span-3 order-3 lg:order-3 relative">
          <!-- 채팅 UI -->
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
</script>

<style scoped>
/* 필요한 스타일 추가 */
</style>