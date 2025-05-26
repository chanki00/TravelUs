<template>
  <div class="min-h-screen flex flex-col">
    <div class="flex-1 flex items-center justify-center py-12 px-6">
      <div class="w-full max-w-md">
        <div class="text-center mb-8">
          <h1 class="text-3xl font-bold">로그인</h1>
          <p class="text-gray-600 mt-2">Travelus 계정으로 로그인하세요</p>
        </div>

        <div class="bg-white rounded-xl shadow-sm border p-6">
          <form @submit.prevent="login" method="post">
            <div class="mb-4">
              <label for="id" class="block text-sm font-medium mb-2">아이디</label>
              <input
                id="id"
                v-model="id"
                type="text"
                class="w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500"
                placeholder="아이디를 입력하세요"
                required
              />
            </div>

            <div class="mb-6">
              <div class="flex items-center justify-between mb-2">
                <label for="password" class="block text-sm font-medium">비밀번호</label>
                <router-link to="/forgot-password" class="text-sm text-blue-600 hover:underline">
                  비밀번호 찾기
                </router-link>
              </div>
              <input
                id="password"
                v-model="password"
                type="password"
                class="w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500"
                placeholder="비밀번호를 입력하세요"
                required
              />
            </div>

            <div class="flex items-center mb-6">
              <input
                id="remember"
                v-model="rememberMe"
                type="checkbox"
                class="rounded border-gray-300 text-blue-600 shadow-sm focus:border-blue-500 focus:ring-blue-500"
              />
              <label for="remember" class="ml-2 block text-sm text-gray-600">
                로그인 상태 유지
              </label>
            </div>

            <button
              type="submit"
              class="w-full bg-blue-600 text-white rounded-md py-2 hover:bg-blue-700 transition-colors"
            >
              <span>로그인</span>
            </button>
          </form>

          <div class="mt-6">
            <div class="relative">
              <div class="absolute inset-0 flex items-center">
                <div class="w-full border-t border-gray-300"></div>
              </div>
              <div class="relative flex justify-center text-sm">
                <span class="px-2 bg-white text-gray-500">또는</span>
              </div>
            </div>

            <div class="mt-6">
              <button
                @click="loginWithNaver"
                class="w-full flex justify-center items-center py-2 px-4 border border-gray-300 rounded-md shadow-sm bg-white text-sm font-medium text-gray-700 hover:bg-gray-50"
              >
                <svg
                  class="h-5 w-5 mr-2"
                  viewBox="0 0 24 24"
                  fill="currentColor"
                  xmlns="http://www.w3.org/2000/svg"
                >
                  <rect width="24" height="24" fill="#03C75A" />
                  <path d="M9.5 7H7v10h2.5v-5.5L14.5 17H17V7h-2.5v5.5L9.5 7z" fill="white" />
                </svg>
                Naver
              </button>
            </div>
          </div>
        </div>

        <div class="text-center mt-6">
          <p class="text-gray-600">
            계정이 없으신가요?
            <router-link to="/regist" class="text-blue-600 hover:underline">회원가입</router-link>
          </p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { userAi } from '@/axios'
import { useUserStore } from '@/store/user'

const userStore = useUserStore()

const router = useRouter()
const id = ref('')
const password = ref('')
const rememberMe = ref(false)
const errorMessage = ref('')

const login = async () => {
  errorMessage.value = ''
  console.log('로그인 버튼 클릭')

  try {
    await userStore.login(id.value, password.value)

    // 로그인 성공 시 이동
    router.push('/') // 원하는 경로로 이동
  } catch (e) {
    errorMessage.value = '아이디 또는 비밀번호가 올바르지 않습니다.'
  }
}

const loginWithNaver = () => {
  window.location.href = 'http://localhost:8080/oauth2/authorization/naver'
}
</script>

<style scoped>
/* 필요한 스타일 추가 */
</style>
