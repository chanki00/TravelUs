<template>
  <div class="min-h-screen bg-gray-50">
    <navbar />
    
    <div class="container py-12 px-6">
      <div class="grid grid-cols-1 md:grid-cols-12 gap-6">
        <!-- 사이드바 -->
        <div class="md:col-span-3">
          <div class="bg-white rounded-xl shadow-sm border overflow-hidden">
            <div class="p-6 text-center">
              <div class="relative mx-auto w-24 h-24 mb-4">
                <img 
                  :src="user.avatar || 'https://randomuser.me/api/portraits/men/32.jpg'" 
                  alt="Profile"
                  class="rounded-full w-full h-full object-cover"
                />
                <button class="absolute bottom-0 right-0 bg-blue-600 text-white rounded-full p-1 shadow-md">
                  <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-pencil">
                    <path d="M17 3a2.85 2.83 0 1 1 4 4L7.5 20.5 2 22l1.5-5.5Z"></path>
                    <path d="m15 5 4 4"></path>
                  </svg>
                </button>
              </div>
              <h2 class="text-xl font-semibold">{{ user.name }}</h2>
              <p class="text-gray-500 text-sm">{{ user.email }}</p>
            </div>
            
            <div class="border-t">
              <nav class="flex flex-col">
                <a 
                  v-for="(item, index) in menuItems" 
                  :key="index"
                  href="#"
                  :class="[
                    'flex items-center px-6 py-3 text-sm',
                    activeTab === item.id 
                      ? 'bg-blue-50 text-blue-600 border-l-2 border-blue-600' 
                      : 'text-gray-700 hover:bg-gray-50'
                  ]"
                  @click.prevent="activeTab = item.id"
                >
                  <component :is="item.icon" class="w-5 h-5 mr-3" />
                  {{ item.label }}
                </a>
              </nav>
            </div>
          </div>
        </div>
        
        <!-- 메인 콘텐츠 -->
        <div class="md:col-span-9">
          <!-- 프로필 정보 -->
          <div v-if="activeTab === 'profile'" class="bg-white rounded-xl shadow-sm border p-6">
            <h3 class="text-xl font-semibold mb-6">프로필 정보</h3>
            
            <form @submit.prevent="updateProfile">
              <div class="grid grid-cols-1 md:grid-cols-2 gap-6 mb-6">
                <div>
                  <label class="block text-sm font-medium mb-2">이름</label>
                  <input 
                    v-model="user.name"
                    type="text" 
                    class="w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500"
                  />
                </div>
                
                <div>
                  <label class="block text-sm font-medium mb-2">이메일</label>
                  <input 
                    v-model="user.email"
                    type="email" 
                    class="w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500"
                    disabled
                  />
                </div>
                
                <div>
                  <label class="block text-sm font-medium mb-2">전화번호</label>
                  <input 
                    v-model="user.phone"
                    type="tel" 
                    class="w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500"
                  />
                </div>
                
                <div>
                  <label class="block text-sm font-medium mb-2">생년월일</label>
                  <input 
                    v-model="user.birthdate"
                    type="date" 
                    class="w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500"
                  />
                </div>
              </div>
              
              <div class="mb-6">
                <label class="block text-sm font-medium mb-2">자기소개</label>
                <textarea 
                  v-model="user.bio"
                  rows="4" 
                  class="w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500"
                  placeholder="자신에 대해 간단히 소개해주세요"
                ></textarea>
              </div>
              
              <div class="mb-6">
                <label class="block text-sm font-medium mb-2">관심 여행 테마</label>
                <div class="flex flex-wrap gap-2">
                  <label 
                    v-for="(theme, index) in travelThemes" 
                    :key="index"
                    class="inline-flex items-center px-3 py-1 rounded-full border"
                    :class="user.interests.includes(theme) ? 'bg-blue-50 border-blue-300 text-blue-700' : 'border-gray-300 text-gray-700'"
                  >
                    <input 
                      type="checkbox" 
                      :value="theme" 
                      v-model="user.interests"
                      class="sr-only"
                    />
                    {{ theme }}
                  </label>
                </div>
              </div>
              
              <div class="flex justify-end">
                <button 
                  type="submit"
                  class="inline-flex items-center justify-center text-sm font-medium transition-colors focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:opacity-50 disabled:pointer-events-none ring-offset-background bg-blue-600 text-white hover:bg-blue-700 h-10 py-2 px-4 rounded-md"
                >
                  저장하기
                </button>
              </div>
            </form>
          </div>
          
          <!-- 내 여행 -->
          <div v-if="activeTab === 'trips'" class="bg-white rounded-xl shadow-sm border p-6">
            <h3 class="text-xl font-semibold mb-6">내 여행</h3>
            
            <div class="mb-6">
              <div class="flex items-center justify-between mb-4">
                <h4 class="font-medium">예정된 여행</h4>
                <router-link to="/ai-planner" class="text-sm text-blue-600 hover:underline">
                  새 여행 계획하기
                </router-link>
              </div>
              
              <div v-if="upcomingTrips.length === 0" class="text-center py-8 border rounded-md bg-gray-50">
                <p class="text-gray-500">예정된 여행이 없습니다</p>
                <router-link to="/ai-planner">
                  <button class="mt-4 inline-flex items-center justify-center text-sm font-medium transition-colors focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:opacity-50 disabled:pointer-events-none ring-offset-background bg-blue-600 text-white hover:bg-blue-700 h-9 py-2 px-4 rounded-md">
                    AI 여행 시작하기
                  </button>
                </router-link>
              </div>
              
              <div v-else class="space-y-4">
                <div 
                  v-for="(trip, index) in upcomingTrips" 
                  :key="index"
                  class="border rounded-md p-4 hover:bg-gray-50"
                >
                  <div class="flex justify-between items-start">
                    <div>
                      <h5 class="font-medium">{{ trip.destination }}</h5>
                      <p class="text-sm text-gray-500">{{ trip.date }}</p>
                    </div>
                    <div class="flex gap-2">
                      <button class="text-gray-500 hover:text-gray-700">
                        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-pencil">
                          <path d="M17 3a2.85 2.83 0 1 1 4 4L7.5 20.5 2 22l1.5-5.5Z"></path>
                          <path d="m15 5 4 4"></path>
                        </svg>
                      </button>
                      <button class="text-gray-500 hover:text-red-600">
                        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-trash-2">
                          <path d="M3 6h18"></path>
                          <path d="M19 6v14c0 1-1 2-2 2H7c-1 0-2-1-2-2V6"></path>
                          <path d="M8 6V4c0-1 1-2 2-2h4c1 0 2 1 2 2v2"></path>
                          <line x1="10" x2="10" y1="11" y2="17"></line>
                          <line x1="14" x2="14" y1="11" y2="17"></line>
                        </svg>
                      </button>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            
            <div>
              <h4 class="font-medium mb-4">지난 여행</h4>
              
              <div v-if="pastTrips.length === 0" class="text-center py-8 border rounded-md bg-gray-50">
                <p class="text-gray-500">지난 여행이 없습니다</p>
              </div>
              
              <div v-else class="space-y-4">
                <div 
                  v-for="(trip, index) in pastTrips" 
                  :key="index"
                  class="border rounded-md p-4 hover:bg-gray-50"
                >
                  <div class="flex justify-between items-start">
                    <div>
                      <h5 class="font-medium">{{ trip.destination }}</h5>
                      <p class="text-sm text-gray-500">{{ trip.date }}</p>
                    </div>
                    <button class="text-gray-500 hover:text-gray-700">
                      <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-eye">
                        <path d="M2 12s3-7 10-7 10 7 10 7-3 7-10 7-10-7-10-7Z"></path>
                        <circle cx="12" cy="12" r="3"></circle>
                      </svg>
                    </button>
                  </div>
                </div>
              </div>
            </div>
          </div>
          
          <!-- 내 동행 -->
          <div v-if="activeTab === 'companions'" class="bg-white rounded-xl shadow-sm border p-6">
            <h3 class="text-xl font-semibold mb-6">내 동행</h3>
            
            <div class="mb-6">
              <div class="flex items-center justify-between mb-4">
                <h4 class="font-medium">내가 만든 동행</h4>
                <router-link to="/companion/create" class="text-sm text-blue-600 hover:underline">
                  새 동행 만들기
                </router-link>
              </div>
              
              <div v-if="myCompanions.length === 0" class="text-center py-8 border rounded-md bg-gray-50">
                <p class="text-gray-500">만든 동행이 없습니다</p>
                <router-link to="/companion/create">
                  <button class="mt-4 inline-flex items-center justify-center text-sm font-medium transition-colors focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:opacity-50 disabled:pointer-events-none ring-offset-background bg-blue-600 text-white hover:bg-blue-700 h-9 py-2 px-4 rounded-md">
                    동행 만들기
                  </button>
                </router-link>
              </div>
              
              <div v-else class="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div 
                  v-for="(companion, index) in myCompanions" 
                  :key="index"
                  class="border rounded-md overflow-hidden"
                >
                  <div class="h-32 bg-gray-200 relative">
                    <img 
                      :src="companion.coverImage" 
                      :alt="companion.title"
                      class="w-full h-full object-cover"
                    />
                    <div class="absolute bottom-0 left-0 right-0 bg-gradient-to-t from-black/70 to-transparent p-3">
                      <h5 class="text-white font-medium">{{ companion.title }}</h5>
                      <p class="text-white/80 text-sm">{{ companion.destination }}</p>
                    </div>
                  </div>
                  <div class="p-3 flex justify-between items-center">
                    <div class="text-sm text-gray-500">
                      {{ companion.currentMembers }}/{{ companion.maxMembers }}명 참여 중
                    </div>
                    <div class="flex gap-2">
                      <button class="text-gray-500 hover:text-gray-700">
                        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-pencil">
                          <path d="M17 3a2.85 2.83 0 1 1 4 4L7.5 20.5 2 22l1.5-5.5Z"></path>
                          <path d="m15 5 4 4"></path>
                        </svg>
                      </button>
                      <button class="text-gray-500 hover:text-red-600">
                        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-trash-2">
                          <path d="M3 6h18"></path>
                          <path d="M19 6v14c0 1-1 2-2 2H7c-1 0-2-1-2-2V6"></path>
                          <path d="M8 6V4c0-1 1-2 2-2h4c1 0 2 1 2 2v2"></path>
                          <line x1="10" x2="10" y1="11" y2="17"></line>
                          <line x1="14" x2="14" y1="11" y2="17"></line>
                        </svg>
                      </button>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            
            <div>
              <h4 class="font-medium mb-4">참여 중인 동행</h4>
              
              <div v-if="joinedCompanions.length === 0" class="text-center py-8 border rounded-md bg-gray-50">
                <p class="text-gray-500">참여 중인 동행이 없습니다</p>
                <router-link to="/companion">
                  <button class="mt-4 inline-flex items-center justify-center text-sm font-medium transition-colors focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:opacity-50 disabled:pointer-events-none ring-offset-background border border-input hover:bg-accent hover:text-accent-foreground h-9 py-2 px-4 rounded-md">
                    동행 찾아보기
                  </button>
                </router-link>
              </div>
              
              <div v-else class="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div 
                  v-for="(companion, index) in joinedCompanions" 
                  :key="index"
                  class="border rounded-md overflow-hidden"
                >
                  <div class="h-32 bg-gray-200 relative">
                    <img 
                      :src="companion.coverImage" 
                      :alt="companion.title"
                      class="w-full h-full object-cover"
                    />
                    <div class="absolute bottom-0 left-0 right-0 bg-gradient-to-t from-black/70 to-transparent p-3">
                      <h5 class="text-white font-medium">{{ companion.title }}</h5>
                      <p class="text-white/80 text-sm">{{ companion.destination }}</p>
                    </div>
                  </div>
                  <div class="p-3 flex justify-between items-center">
                    <div class="text-sm text-gray-500">
                      {{ companion.currentMembers }}/{{ companion.maxMembers }}명 참여 중
                    </div>
                    <button class="text-gray-500 hover:text-gray-700">
                      <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-eye">
                        <path d="M2 12s3-7 10-7 10 7 10 7-3 7-10 7-10-7-10-7Z"></path>
                        <circle cx="12" cy="12" r="3"></circle>
                      </svg>
                    </button>
                  </div>
                </div>
              </div>
            </div>
          </div>
          
          <!-- 설정 -->
          <div v-if="activeTab === 'settings'" class="bg-white rounded-xl shadow-sm border p-6">
            <h3 class="text-xl font-semibold mb-6">계정 설정</h3>
            
            <div class="space-y-6">
              <div>
                <h4 class="font-medium mb-4">비밀번호 변경</h4>
                <form @submit.prevent="changePassword" class="space-y-4">
                  <div>
                    <label class="block text-sm font-medium mb-2">현재 비밀번호</label>
                    <input 
                      v-model="currentPassword"
                      type="password" 
                      class="w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500"
                    />
                  </div>
                  
                  <div>
                    <label class="block text-sm font-medium mb-2">새 비밀번호</label>
                    <input 
                      v-model="newPassword"
                      type="password" 
                      class="w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500"
                    />
                  </div>
                  
                  <div>
                    <label class="block text-sm font-medium mb-2">새 비밀번호 확인</label>
                    <input 
                      v-model="confirmPassword"
                      type="password" 
                      class="w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500"
                    />
                  </div>
                  
                  <div>
                    <button 
                      type="submit"
                      class="inline-flex items-center justify-center text-sm font-medium transition-colors focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:opacity-50 disabled:pointer-events-none ring-offset-background bg-blue-600 text-white hover:bg-blue-700 h-10 py-2 px-4 rounded-md"
                    >
                      비밀번호 변경
                    </button>
                  </div>
                </form>
              </div>
              
              <div class="pt-6 border-t">
                <h4 class="font-medium mb-4">알림 설정</h4>
                <div class="space-y-3">
                  <label class="flex items-center justify-between">
                    <span class="text-sm">이메일 알림</span>
                    <input 
                      v-model="notifications.email"
                      type="checkbox" 
                      class="toggle"
                    />
                  </label>
                  
                  <label class="flex items-center justify-between">
                    <span class="text-sm">푸시 알림</span>
                    <input 
                      v-model="notifications.push"
                      type="checkbox" 
                      class="toggle"
                    />
                  </label>
                  
                  <label class="flex items-center justify-between">
                    <span class="text-sm">마케팅 정보 수신</span>
                    <input 
                      v-model="notifications.marketing"
                      type="checkbox" 
                      class="toggle"
                    />
                  </label>
                </div>
                
                <div class="mt-4">
                  <button 
                    @click="saveNotificationSettings"
                    class="inline-flex items-center justify-center text-sm font-medium transition-colors focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:opacity-50 disabled:pointer-events-none ring-offset-background bg-blue-600 text-white hover:bg-blue-700 h-10 py-2 px-4 rounded-md"
                  >
                    설정 저장
                  </button>
                </div>
              </div>
              
              <div class="pt-6 border-t">
                <h4 class="font-medium mb-4 text-red-600">계정 삭제</h4>
                <p class="text-sm text-gray-500 mb-4">
                  계정을 삭제하면 모든 데이터가 영구적으로 삭제되며 복구할 수 없습니다.
                </p>
                <button 
                  @click="confirmDeleteAccount"
                  class="inline-flex items-center justify-center text-sm font-medium transition-colors focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:opacity-50 disabled:pointer-events-none ring-offset-background bg-red-600 text-white hover:bg-red-700 h-10 py-2 px-4 rounded-md"
                >
                  계정 삭제
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    
    <footer-component />
  </div>
</template>

<script setup>
// 여기에 script 로직이 추가될 예정
import { ref } from 'vue';
import Navbar from '@/components/common/Navbar.vue';
import FooterComponent from '@/components/common/Footer.vue';

const activeTab = ref('profile');

// 메뉴 아이템
const menuItems = [
  { id: 'profile', label: '프로필 정보', icon: 'UserIcon' },
  { id: 'trips', label: '내 여행', icon: 'MapIcon' },
  { id: 'companions', label: '내 동행', icon: 'UsersIcon' },
  { id: 'settings', label: '설정', icon: 'SettingsIcon' },
];

// 사용자 정보
const user = ref({
  name: '김여행',
  email: 'travel@example.com',
  phone: '010-1234-5678',
  birthdate: '1990-01-01',
  bio: '여행을 좋아하는 직장인입니다. 주로 자연과 문화 체험을 즐깁니다.',
  avatar: 'https://randomuser.me/api/portraits/men/32.jpg',
  interests: ['자연/힐링', '문화/역사', '맛집 탐방']
});

// 여행 테마
const travelThemes = [
  '자연/힐링', '문화/역사', '맛집 탐방', '쇼핑', '액티비티', '축제/이벤트', '사진촬영', '도시여행'
];

// 여행 목록
const upcomingTrips = [
  { destination: '제주도 3박 4일', date: '2023.12.15 - 2023.12.18' },
  { destination: '부산 주말 여행', date: '2024.01.20 - 2024.01.21' }
];

const pastTrips = [
  { destination: '강원도 속초', date: '2023.08.12 - 2023.08.14' },
  { destination: '일본 도쿄', date: '2023.05.01 - 2023.05.05' }
];

// 동행 목록
const myCompanions = [
  {
    title: '제주도 1주일 힐링 여행',
    destination: '제주도',
    coverImage: 'https://images.unsplash.com/photo-1601629665203-f9f2b8d3a3b0?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1000&q=80',
    currentMembers: 2,
    maxMembers: 4
  }
];

const joinedCompanions = [
  {
    title: '부산 맛집 투어',
    destination: '부산',
    coverImage: 'https://images.unsplash.com/photo-1540979388789-6cee28a1cdc9?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1000&q=80',
    currentMembers: 3,
    maxMembers: 5
  }
];

// 설정
const currentPassword = ref('');
const newPassword = ref('');
const confirmPassword = ref('');

const notifications = ref({
  email: true,
  push: true,
  marketing: false
});

// 메서드
function updateProfile() {
  // 프로필 업데이트 로직
  alert('프로필이 업데이트되었습니다.');
}

function changePassword() {
  // 비밀번호 변경 로직
  alert('비밀번호가 변경되었습니다.');
  currentPassword.value = '';
  newPassword.value = '';
  confirmPassword.value = '';
}

function saveNotificationSettings() {
  // 알림 설정 저장 로직
  alert('알림 설정이 저장되었습니다.');
}

function confirmDeleteAccount() {
  // 계정 삭제 확인 로직
  const confirmed = confirm('정말로 계정을 삭제하시겠습니까? 이 작업은 되돌릴 수 없습니다.');
  if (confirmed) {
    // 계정 삭제 로직
    alert('계정이 삭제되었습니다.');
  }
}
</script>

<style scoped>
.toggle {
  position: relative;
  display: inline-block;
  width: 40px;
  height: 20px;
  background-color: #ccc;
  border-radius: 20px;
  transition: all 0.3s;
}

.toggle::after {
  content: '';
  position: absolute;
  width: 18px;
  height: 18px;
  border-radius: 18px;
  background-color: white;
  top: 1px;
  left: 1px;
  transition: all 0.3s;
}

input:checked + .toggle {
  background-color: #3b82f6;
}

input:checked + .toggle::after {
  transform: translateX(20px);
}

input[type="checkbox"].toggle {
  display: none;
}
</style>
