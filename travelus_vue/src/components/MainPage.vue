<template>
  <div>
    <section class="text-center py-16 px-4">
      <h1 class="text-3xl sm:text-4xl md:text-5xl font-bold leading-tight">
        <span class="text-blue-600">AI 기반 여행 플래너</span><br />
        <span class="text-black">& 동행 매칭 플랫폼</span>
      </h1>
      <p class="mt-4 text-gray-500 text-sm sm:text-base">
        Travelus와 함께 AI로 여행 계획을 세우고, 나와 맞는 여행 동행자를 찾아 특별한 여행을
        떠나보세요.
      </p>

      <div class="mt-6 flex flex-col sm:flex-row justify-center gap-4">
        <button class="btn btn-primary w-full sm:w-auto">AI 여행 시작하기</button>
        <button class="btn btn-outline w-full sm:w-auto">동행자 찾기</button>
      </div>
    </section>

    <section class="bg-base-100 py-12 px-6">
      <h2 class="text-2xl font-semibold mb-4">인기 여행 일정</h2>
      <p class="text-sm text-gray-500 mb-6">다른 여행자들의 인기 일정을 살펴보세요</p>
      <div class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6">
        <div class="card bg-base-100 shadow-md image-full">
          <div class="card-body">
            <h2 class="card-title">제주도 4박 5일</h2>
            <p>힐링 여행 코스</p>
          </div>
        </div>

        <div class="card bg-base-100 shadow-md image-full">
          <div class="card-body">
            <h2 class="card-title">서울 근교 당일치기</h2>
            <p>혼자 가도 좋아요</p>
          </div>
        </div>

        <div class="card bg-base-100 shadow-md image-full">
          <div class="card-body">
            <h2 class="card-title">부산 해운대 2박 3일</h2>
            <p>야경 감상 코스</p>
          </div>
        </div>

        <div class="card bg-base-100 shadow-md image-full">
          <div class="card-body">
            <h2 class="card-title">강원도 서핑 여행</h2>
            <p>여름 추천 일정</p>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup></script>

<style lang="scss" scoped></style>
