<template>
  <div class="min-h-screen flex flex-col">
    <Navbar />
    
    <div class="flex-1 flex items-center justify-center py-12 px-6">
      <div class="w-full max-w-md">
        <div class="text-center mb-8">
          <h1 class="text-3xl font-bold">회원가입</h1>
          <p class="text-gray-600 mt-2">Travelus 계정을 만들고 여행을 시작하세요</p>
        </div>
        
        <div class="bg-white rounded-xl shadow-sm border p-6">
          <form @submit.prevent="handleRegister">
            <div class="mb-4">
              <label for="name" class="block text-sm font-medium mb-2">이름</label>
              <input 
                id="name"
                v-model="name"
                type="text" 
                class="w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500"
                placeholder="이름을 입력하세요"
                required
              />
            </div>
            
            <div class="mb-4">
              <label for="email" class="block text-sm font-medium mb-2">이메일</label>
              <input 
                id="email"
                v-model="email"
                type="email" 
                class="w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500"
                placeholder="이메일 주소를 입력하세요"
                required
              />
            </div>
            
            <div class="mb-4">
              <label for="password" class="block text-sm font-medium mb-2">비밀번호</label>
              <input 
                id="password"
                v-model="password"
                type="password" 
                class="w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500"
                placeholder="비밀번호를 입력하세요"
                required
              />
            </div>
            
            <div class="mb-6">
              <label for="passwordConfirm" class="block text-sm font-medium mb-2">비밀번호 확인</label>
              <input 
                id="passwordConfirm"
                v-model="passwordConfirm"
                type="password" 
                class="w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500"
                placeholder="비밀번호를 다시 입력하세요"
                required
              />
            </div>
            
            <div class="flex items-center mb-6">
              <input 
                id="terms"
                v-model="agreeTerms"
                type="checkbox" 
                class="rounded border-gray-300 text-blue-600 shadow-sm focus:border-blue-500 focus:ring-blue-500"
                required
              />
              <label for="terms" class="ml-2 block text-sm text-gray-600">
                <span>이용약관 및 </span>
                <router-link to="/privacy" class="text-blue-600 hover:underline">개인정보처리방침</router-link>
                <span>에 동의합니다</span>
              </label>
            </div>
            
            <button 
              type="submit"
              class="w-full bg-blue-600 text-white rounded-md py-2 hover:bg-blue-700 transition-colors"
              :disabled="isLoading || !isFormValid"
            >
              <span v-if="isLoading">가입 중...</span>
              <span v-else>회원가입</span>
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
            
            <div class="mt-6 grid grid-cols-2 gap-3">
              <button 
                @click="handleSocialRegister('google')"
                class="flex justify-center items-center py-2 px-4 border border-gray-300 rounded-md shadow-sm bg-white text-sm font-medium text-gray-700 hover:bg-gray-50"
              >
                <svg class="h-5 w-5 mr-2" viewBox="0 0 24 24">
                  <g transform="matrix(1, 0, 0, 1, 27.009001, -39.238998)">
                    <path fill="#4285F4" d="M -3.264 51.509 C -3.264 50.719 -3.334 49.969 -3.454 49.239 L -14.754 49.239 L -14.754 53.749 L -8.284 53.749 C -8.574 55.229 -9.424 56.479 -10.684 57.329 L -10.684 60.329 L -6.824 60.329 C -4.564 58.239 -3.264 55.159 -3.264 51.509 Z"></path>
                    <path fill="#34A853" d="M -14.754 63.239 C -11.514 63.239 -8.804 62.159 -6.824 60.329 L -10.684 57.329 C -11.764 58.049 -13.134 58.489 -14.754 58.489 C -17.884 58.489 -20.534 56.379 -21.484 53.529 L -25.464 53.529 L -25.464 56.619 C -23.494 60.539 -19.444 63.239 -14.754 63.239 Z"></path>
                    <path fill="#FBBC05" d="M -21.484 53.529 C -21.734 52.809 -21.864 52.039 -21.864 51.239 C -21.864 50.439 -21.724 49.669 -21.484 48.949 L -21.484 45.859 L -25.464 45.859 C -26.284 47.479 -26.754 49.299 -26.754 51.239 C -26.754 53.179 -26.284 54.999 -25.464 56.619 L -21.484 53.529 Z"></path>
                    <path fill="#EA4335" d="M -14.754 43.989 C -12.984 43.989 -11.404 44.599 -10.154 45.789 L -6.734 42.369 C -8.804 40.429 -11.514 39.239 -14.754 39.239 C -19.444 39.239 -23.494 41.939 -25.464 45.859 L -21.484 48.949 C -20.534 46.099 -17.884 43.989 -14.754 43.989 Z"></path>
                  </g>
                </svg>
                Google
              </button>
              <button 
                @click="handleSocialRegister('facebook')"
                class="flex justify-center items-center py-2 px-4 border border-gray-300 rounded-md shadow-sm bg-white text-sm font-medium text-gray-700 hover:bg-gray-50"
              >
                <svg class="h-5 w-5 mr-2" viewBox="0 0 24 24" fill="currentColor">
                  <path d="M22 12c0-5.523-4.477-10-10-10S2 6.477 2 12c0 4.991 3.657 9.128 8.438 9.878v-6.987h-2.54V12h2.54V9.797c0-2.506 1.492-3.89 3.777-3.89 1.094 0 2.238.195 2.238.195v2.46h-1.26c-1.243 0-1.63.771-1.63 1.562V12h2.773l-.443 2.89h-2.33v6.988C18.343 21.128 22 16.991 22 12z"></path>
                </svg>
                Facebook
              </button>
            </div>
          </div>
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
// 여기에 script 로직이 추가될 예정
// import { ref, computed, watch } from 'vue';
// import { useRouter } from 'vue-router';
// import { useAuthStore } from '@/stores/auth';
// import Navbar from '@/components/common/Navbar.vue';
// import Footer from '@/components/common/Footer.vue';
// 
// const router = useRouter();
// const authStore = useAuthStore();
// 
// const name = ref('');
// const email = ref('');
// const password = ref('');
// const passwordConfirm = ref('');
// const agreeTerms = ref(false);
// const isLoading = ref(false);
// const passwordError = ref('');
// 
// // 비밀번호 유효성 검사
// watch(password, () => {
//   validatePassword();
// });
// 
// watch(passwordConfirm, () => {
//   validatePasswordMatch();
// });
// 
// function validatePassword() {
//   if (password.value.length &lt; 8) {
//     passwordError.value = '비밀번호는 8자 이상이어야 합니다.';
//     return false;
//   }
//   
//   if (!/[A-Z]/.test(password.value) || !/[a-z]/.test(password.value) || !/[0-9]/.test(password.value)) {
//     passwordError.value = '비밀번호는 대문자, 소문자, 숫자를 포함해야 합니다.';
//     return false;
//   }
//   
//   passwordError.value = '';
//   return true;
// }
// 
// function validatePasswordMatch() {
//   if (password.value !== passwordConfirm.value) {
//     passwordError.value = '비밀번호가 일치하지 않습니다.';
//     return false;
//   }
//   
//   if (passwordConfirm.value && !validatePassword()) {
//     return false;
//   }
//   
//   passwordError.value = '';
//   return true;
// }
// 
// const isFormValid = computed(() => {
//   return (
//     name.value.trim() !== '' &&
//     email.value.trim() !== '' &&
//     password.value.trim() !== '' &&
//     passwordConfirm.value.trim() !== '' &&
//     password.value === passwordConfirm.value &&
//     agreeTerms.value &&
//     !passwordError.value
//   );
// });
// 
// async function handleRegister() {
//   if (!isFormValid.value) return;
//   
//   try {
//     isLoading.value = true;
//     const success = await authStore.register(name.value, email.value, password.value);
//     
//     if (success) {
//       router.push('/login');
//     } else {
//       // 회원가입 실패 처리
//       alert('회원가입에 실패했습니다. 다시 시도해주세요.');
//     }
//   } catch (error) {
//     console.error('회원가입 실패:', error);
//     alert('회원가입 중 오류가 발생했습니다. 다시 시도해주세요.');
//   } finally {
//     isLoading.value = false;
//   }
// }
// 
// function handleSocialRegister(provider) {
//   // 소셜 회원가입 처리
//   console.log(`${provider} 회원가입 시도`);
//   // 실제 구현에서는 각 소셜 로그인 API 연동 필요
// }
</script>
