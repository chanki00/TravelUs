<template>
  <header class="bg-base-100 shadow-sm">
    <div class="flex items-center justify-between h-16 px-10">
      <!-- <a class="text-xl font-bold text-blue-600">Travelus</a> -->
      <RouterLink to="/" class="text-xl font-bold text-blue-600">Travelus</RouterLink>

      <nav class="hidden md:flex gap-4">
        <RouterLink to="/plan" class="text-m hover:underline">여행 플래너</RouterLink>
        <RouterLink to="/recruit" class="text-m hover:underline">여행 인원 모집</RouterLink>
        <RouterLink to="/match" class="text-m hover:underline">동행자 찾기</RouterLink>
        <RouterLink to="/share" class="text-m hover:underline">여행 공유 게시판</RouterLink>


        <!-- <a class="text-sm hover:underline">여행 플래너</a>
        <a class="text-sm hover:underline">여행 인원 모집</a>
        <a class="text-sm hover:underline">동행자 찾기</a>
        <a class="text-sm hover:underline">여행 공유 게시판</a> -->
      </nav>

      <div class="flex gap-2">
        <RouterLink to="/login" class="btn btn-m btn-ghost">로그인</RouterLink>
        <RouterLink to="/regist" class="btn btn-m btn-primary">회원가입</RouterLink>

        <!-- <button class="btn btn-m btn-ghost">로그인</button>
        <button class="btn btn-m btn-primary">회원가입</button> -->
      </div>
    </div>
  </header>
</template>

<script setup>
  import {RouterView, RouterLink} from "vue-router"
</script>

<style scoped></style>
