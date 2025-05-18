<template>
  <div class="min-h-screen flex items-center justify-center bg-white">
    <div class="border rounded-lg p-8 w-full max-w-md shadow">
      <h2 class="text-2xl font-bold mb-6">로그인</h2>

      <form @submit.prevent="login">
        <label class="block font-semibold mb-1">아이디</label>
        <input
          type="text"
          v-model="userId"
          class="input input-bordered w-full mb-4"
          placeholder="아이디 입력"
        />

        <label class="block font-semibold mb-1">비밀번호</label>
        <input
          type="password"
          v-model="password"
          class="input input-bordered w-full mb-2"
          placeholder="비밀번호 입력"
        />

        <div class="flex justify-between items-center text-sm mb-6">
          <label class="flex items-center gap-2">
            <input type="checkbox" class="checkbox checkbox-sm" v-model="remember" />
            id 기억하기
          </label>
          <a href="#" class="text-black hover:underline">비밀번호 찾기</a>
        </div>

        <div class="flex justify-between gap-4">
          <button class="btn bg-cyan-500 text-white hover:bg-cyan-600 w-1/2">로그인</button>
          <button class="btn bg-red-500 text-white hover:bg-red-600 w-1/2">회원가입</button>
        </div>
      </form>
    </div>
  </div>
</template>
<script setup></script>

<style scoped></style>
