<template>
  <div class="min-h-screen flex items-center justify-center bg-white">
    <div class="border rounded-lg p-8 w-full max-w-lg shadow">
      <h2 class="text-2xl font-bold text-center mb-8">회원가입</h2>

      <form @submit.prevent="register">
        <div class="mb-4">
          <label class="block font-semibold mb-1">이름</label>
          <input
            v-model="form.name"
            type="text"
            placeholder="이름 입력"
            class="input input-bordered w-full"
          />
        </div>

        <div class="mb-4">
          <label class="block font-semibold mb-1">아이디</label>
          <input
            v-model="form.id"
            type="text"
            placeholder="아이디 입력"
            class="input input-bordered w-full"
          />
        </div>

        <div class="mb-4">
          <label class="block font-semibold mb-1">비밀번호</label>
          <input
            v-model="form.password"
            type="password"
            placeholder="비밀번호 입력"
            class="input input-bordered w-full"
          />
        </div>

        <div class="mb-4">
          <label class="block font-semibold mb-1">비밀번호 확인</label>
          <input
            v-model="form.passwordConfirm"
            type="password"
            placeholder="비밀번호 확인"
            class="input input-bordered w-full"
          />
        </div>

        <div class="mb-6">
          <label class="block font-semibold mb-1">이메일</label>
          <div class="flex gap-2">
            <input
              v-model="form.email"
              type="text"
              placeholder="이메일 입력"
              class="input input-bordered w-full"
            />
            <div class="flex items-center px-2">@</div>
            <select v-model="form.emailDomain" class="select select-bordered w-full max-w-xs">
              <option disabled value="">도메인 선택</option>
              <option value="@gmail.com">gmail.com</option>
              <option value="@naver.com">naver.com</option>
              <option value="@DB_PASSWORD_REDACTED.com">DB_PASSWORD_REDACTED.com</option>
            </select>
          </div>
        </div>

        <div class="flex justify-end gap-2">
          <button type="submit" class="btn bg-cyan-500 text-white hover:bg-cyan-600">
            회원가입
          </button>
          <button type="button" class="btn bg-red-500 text-white hover:bg-red-600">취소</button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { reactive } from 'vue'

const form = reactive({
  name: '',
  id: '',
  password: '',
  passwordConfirm: '',
  email: '',
  emailDomain: '',
})
</script>

<style scoped></style>
