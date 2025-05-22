<template>
  <div class="min-h-screen flex flex-col">
    <div class="flex-1 flex items-center justify-center py-12 px-6">
      <div class="w-full max-w-md">
        <div class="text-center mb-8">
          <h1 class="text-3xl font-bold">회원가입</h1>
          <p class="text-gray-600 mt-2">Travelus 계정을 만들고 여행을 시작하세요</p>
        </div>

        <div class="bg-white rounded-xl shadow-sm border p-6">
          <form @submit.prevent="regist" method="post">
            <div class="mb-4">
              <label for="id" class="block text-sm font-medium mb-2">아이디</label>
              <input 
                id="id"
                v-model="id"
                type="text" 
                class="w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500"
                placeholder="아이디를 입력하세요"
              />
              <p v-if="submitted && !id" class="text-red-500 text-sm mt-1">아이디를 입력해주세요</p>
            </div>

            <div class="mb-4">
              <label for="name" class="block text-sm font-medium mb-2">이름</label>
              <input 
                id="name"
                v-model="name"
                type="text" 
                class="w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500"
                placeholder="이름을 입력하세요"
              />
              <p v-if="submitted && !name" class="text-red-500 text-sm mt-1">이름을 입력해주세요</p>
            </div>

            <div class="mb-4">
              <label for="email" class="block text-sm font-medium mb-2">이메일</label>
              <input 
                id="email"
                v-model="email"
                type="email" 
                class="w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500"
                placeholder="이메일 주소를 입력하세요"
              />
              <p v-if="submitted && !email" class="text-red-500 text-sm mt-1">이메일을 입력해주세요</p>
              <p v-else-if="submitted && !isValidEmail(email)" class="text-red-500 text-sm mt-1">유효한 이메일 형식이 아닙니다</p>
            </div>

            <div class="mb-4">
              <label for="password" class="block text-sm font-medium mb-2">비밀번호</label>
              <input 
                id="password"
                v-model="password"
                type="password" 
                class="w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500"
                placeholder="비밀번호를 입력하세요"
              />
              <p v-if="submitted && !password" class="text-red-500 text-sm mt-1">비밀번호를 입력해주세요</p>
            </div>

            <div class="mb-6">
              <label for="passwordConfirm" class="block text-sm font-medium mb-2">비밀번호 확인</label>
              <input 
                id="passwordConfirm"
                v-model="passwordConfirm"
                type="password" 
                class="w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500"
                placeholder="비밀번호를 다시 입력하세요"
              />
              <p v-if="submitted && password !== passwordConfirm" class="text-red-500 text-sm mt-1">비밀번호가 일치하지 않습니다</p>
            </div>

            <div class="flex items-center mb-6">
              <input 
                id="terms"
                v-model="agreeTerms"
                type="checkbox" 
                class="rounded border-gray-300 text-blue-600 shadow-sm focus:border-blue-500 focus:ring-blue-500"
              />
              <label for="terms" class="ml-2 block text-sm text-gray-600">
                <span>이용약관 및 </span>
                <router-link to="/privacy" class="text-blue-600 hover:underline">개인정보처리방침</router-link>
                <span>에 동의합니다</span>
              </label>
            </div>
            <p v-if="submitted && !agreeTerms" class="text-red-500 text-sm mb-4">약관에 동의해주세요</p>

            <button 
              type="submit"
              class="w-full bg-blue-600 text-white rounded-md py-2 hover:bg-blue-700 transition-colors"
            >
              <span>회원가입</span>
            </button>
          </form>
        </div>

        <div class="text-center mt-6">
          <p class="text-gray-600">
            이미 계정이 있으신가요?
            <router-link to="/login" class="text-blue-600 hover:underline">로그인</router-link>
          </p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { userAi } from '@/axios'
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const id = ref('')
const name = ref('')
const email = ref('')
const password = ref('')
const passwordConfirm = ref('')
const agreeTerms = ref(false)
const submitted = ref(false)

const isValidEmail = (email) => {
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
  return emailRegex.test(email)
}

const regist = async () => {
  submitted.value = true

  if (!id.value || !name.value || !email.value || !isValidEmail(email.value) ||
      !password.value || password.value !== passwordConfirm.value || !agreeTerms.value) {
    return
  }

  try {
    const response = await userAi.post('/api/v1/user', {
      userId: id.value,
      userPw: password.value,
      name: name.value,
      userEmail: email.value,
      role: 'USER'
    })

    alert(response.data.message)
    router.push('/login')
  } catch (error) {
    console.error(error)
    alert(error.response?.data?.message || '회원가입에 실패했습니다.')
  }
}
</script>
