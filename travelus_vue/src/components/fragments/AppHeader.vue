<template>
  <header class="bg-base-100 shadow-sm">
    <div class="flex items-center justify-between h-16 px-10">
<RouterLink to="/" class="flex items-center gap-2">
      <div class="w-8 h-8 rounded-full bg-gradient-to-r from-blue-500 to-blue-600 flex items-center justify-center">
        <span class="text-white font-bold text-sm">T</span>
      </div>
      <span class="font-bold text-xl text-blue-600">Travelus</span>
    </RouterLink>
      <nav class="hidden md:flex gap-4">
        <RouterLink to="/plan" class="text-m hover:underline">여행 플래너</RouterLink>
        <RouterLink to="/recruit" class="text-m hover:underline">여행 인원 모집</RouterLink>
        <RouterLink to="/companion" class="text-m hover:underline">동행자 찾기</RouterLink>
        <RouterLink to="/share" class="text-m hover:underline">여행 공유 게시판</RouterLink>

      </nav>

      <div class="flex gap-2">
        <RouterLink to="/login" class="btn btn-m btn-ghost">로그인</RouterLink>
        <RouterLink to="/regist" class="btn btn-m btn-primary">회원가입</RouterLink>

      </div>
    </div>
  </header>
</template>

<script setup>
  import {RouterView, RouterLink} from "vue-router"
</script>

<style scoped></style>
