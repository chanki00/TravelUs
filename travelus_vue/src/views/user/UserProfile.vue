<template>
  <div class="min-h-screen bg-gray-50">

    <div class="py-12 px-6 max-w-5xl mx-auto">
      <h1 class="text-3xl font-bold mb-8">내 프로필</h1>

      <div class="bg-white shadow-sm rounded-lg p-6 mb-8">
        <div class="flex flex-col sm:flex-row items-center gap-6 mb-6">
          <div class="w-24 h-24 rounded-full bg-blue-100 flex items-center justify-center">
            <span class="text-2xl font-bold text-blue-600">여행</span>
          </div>

          <div class="text-center sm:text-left">
            <h2 class="text-2xl font-bold">{{ username }}</h2>
            <p class="text-gray-500">{{ email }}</p>
          </div>

          <div class="ml-auto">
            <button class="px-4 py-2 border border-gray-300 rounded-md text-sm font-medium text-gray-700 hover:bg-gray-50">
              프로필 수정
            </button>
          </div>
        </div>

        <div class="border-t pt-4">
          <p class="text-gray-700 mb-4">
            {{ bio }}
          </p>

          <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
              <h3 class="text-sm font-medium text-gray-500 mb-2">나의 성격 태그</h3>
              <div class="flex flex-wrap gap-2">
                <span 
                  v-for="(tag, index) in personalityTags" 
                  :key="index" 
                  class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-purple-50 text-purple-800"
                >
                  {{ tag }}
                </span>
              </div>
            </div>

            <div>
              <h3 class="text-sm font-medium text-gray-500 mb-2">선호하는 여행 태그</h3>
              <div class="flex flex-wrap gap-2">
                <span 
                  v-for="(tag, index) in travelTags" 
                  :key="index" 
                  class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-green-50 text-green-800"
                >
                  {{ tag }}
                </span>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="bg-white shadow-sm rounded-lg overflow-hidden">
        <div class="border-b border-gray-200">
          <nav class="flex -mb-px">
            <button 
              v-for="tab in tabs" 
              :key="tab.id"
              @click="activeTab = tab.id"
              class="px-6 py-4 text-sm font-medium"
              :class="activeTab === tab.id ? 'border-b-2 border-blue-500 text-blue-600' : 'text-gray-500 hover:text-gray-700 hover:border-gray-300'"
            >
              {{ tab.name }}
            </button>
          </nav>
        </div>

        <!-- 회원정보 수정 탭 -->
        <div v-if="activeTab === 'edit'" class="p-6">
          <h2 class="text-lg font-medium mb-6">회원정보 수정</h2>
          
          <div class="space-y-6">
            <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
              <div class="space-y-2">
                <label for="username" class="block text-sm font-medium text-gray-700">아이디</label>
                <input 
                  id="username" 
                  v-model="username" 
                  readonly 
                  class="bg-gray-50 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm text-gray-700"
                />
              </div>

              <div class="space-y-2">
                <label for="email" class="block text-sm font-medium text-gray-700">이메일</label>
                <input 
                  id="email" 
                  type="email" 
                  v-model="email" 
                  class="block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm text-gray-700"
                />
              </div>

              <div class="space-y-2">
                <label for="name" class="block text-sm font-medium text-gray-700">이름</label>
                <input 
                  id="name" 
                  v-model="name" 
                  class="block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm text-gray-700"
                />
              </div>

              <div class="space-y-2">
                <label for="region" class="block text-sm font-medium text-gray-700">지역</label>
                <select 
                  id="region" 
                  v-model="region" 
                  class="block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm text-gray-700"
                >
                  <option value="seoul">서울</option>
                  <option value="busan">부산</option>
                  <option value="incheon">인천</option>
                  <option value="daegu">대구</option>
                  <option value="other">기타</option>
                </select>
              </div>

              <div class="space-y-2">
                <label for="age" class="block text-sm font-medium text-gray-700">나이</label>
                <select 
                  id="age" 
                  v-model="age" 
                  class="block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm text-gray-700"
                >
                  <option value="20s">20대</option>
                  <option value="30s">30대</option>
                  <option value="40s">40대</option>
                  <option value="50s">50대 이상</option>
                </select>
              </div>

              <div class="space-y-2">
                <label for="gender" class="block text-sm font-medium text-gray-700">성별</label>
                <select 
                  id="gender" 
                  v-model="gender" 
                  class="block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm text-gray-700"
                >
                  <option value="male">남성</option>
                  <option value="female">여성</option>
                  <option value="other">기타</option>
                </select>
              </div>
            </div>

            <div class="space-y-2">
              <label for="bio" class="block text-sm font-medium text-gray-700">자기소개</label>
              <textarea 
                id="bio" 
                v-model="bio" 
                rows="4" 
                class="resize-none block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm text-gray-700"
              ></textarea>
            </div>

            <div class="space-y-2">
              <label class="block text-sm font-medium text-gray-700">성격 태그</label>
              <div class="flex flex-wrap gap-2 mb-2">
                <span 
                  v-for="(tag, index) in personalityTags" 
                  :key="index" 
                  class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-purple-50 text-purple-800"
                >
                  {{ tag }}
                  <button 
                    @click="removeTag('personality', tag)" 
                    class="ml-1 h-4 w-4 rounded-full inline-flex items-center justify-center hover:bg-red-100"
                  >
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-3 w-3" viewBox="0 0 20 20" fill="currentColor">
                      <path fill-rule="evenodd" d="M4.293 4.293a1 1 0 011.414 0L10 8.586l4.293-4.293a1 1 0 111.414 1.414L11.414 10l4.293 4.293a1 1 0 01-1.414 1.414L10 11.414l-4.293 4.293a1 1 0 01-1.414-1.414L8.586 10 4.293 5.707a1 1 0 010-1.414z" clip-rule="evenodd" />
                    </svg>
                  </button>
                </span>
              </div>
              <div class="flex gap-2">
                <input 
                  v-model="newTag" 
                  @keydown.enter.prevent="addTag('personality')" 
                  placeholder="새 태그 추가" 
                  class="block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm text-gray-700"
                />
                <button 
                  @click="addTag('personality')" 
                  class="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700"
                >
                  추가
                </button>
              </div>
            </div>

            <div class="space-y-2">
              <label class="block text-sm font-medium text-gray-700">선호 여행 태그</label>
              <div class="flex flex-wrap gap-2 mb-2">
                <span 
                  v-for="(tag, index) in travelTags" 
                  :key="index" 
                  class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-green-50 text-green-800"
                >
                  {{ tag }}
                  <button 
                    @click="removeTag('travel', tag)" 
                    class="ml-1 h-4 w-4 rounded-full inline-flex items-center justify-center hover:bg-red-100"
                  >
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-3 w-3" viewBox="0 0 20 20" fill="currentColor">
                      <path fill-rule="evenodd" d="M4.293 4.293a1 1 0 011.414 0L10 8.586l4.293-4.293a1 1 0 111.414 1.414L11.414 10l4.293 4.293a1 1 0 01-1.414 1.414L10 11.414l-4.293 4.293a1 1 0 01-1.414-1.414L8.586 10 4.293 5.707a1 1 0 010-1.414z" clip-rule="evenodd" />
                    </svg>
                  </button>
                </span>
              </div>
              <div class="flex gap-2">
                <input 
                  v-model="newTag" 
                  @keydown.enter.prevent="addTag('travel')" 
                  placeholder="새 태그 추가" 
                  class="block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm text-gray-700"
                />
                <button 
                  @click="addTag('travel')" 
                  class="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700"
                >
                  추가
                </button>
              </div>
            </div>

            <div class="flex items-center space-x-2">
              <input 
                type="checkbox" 
                id="invite-setting" 
                v-model="allowInvites" 
                class="h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300 rounded"
              />
              <label for="invite-setting" class="text-sm text-gray-700">동행 초대 받기 허용</label>
            </div>

            <div class="flex justify-between">
              <button class="px-4 py-2 border border-red-300 text-red-600 rounded-md hover:bg-red-50">
                회원 탈퇴
              </button>

              <div class="space-x-4">
                <button class="px-4 py-2 border border-gray-300 rounded-md text-gray-700 hover:bg-gray-50">
                  취소
                </button>
                <button class="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700">
                  저장
                </button>
              </div>
            </div>
          </div>
        </div>

        <!-- 내 여행 계획 탭 -->
        <div v-if="activeTab === 'trips'" class="p-6">
          <h2 class="text-lg font-medium mb-6">나의 여행 계획</h2>
          
          <div v-if="trips.length === 0" class="text-center py-12">
            <p class="text-gray-500 mb-4">아직 계획된 여행이 없습니다.</p>
            <button class="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700">
              여행 계획 시작하기
            </button>
          </div>
          
          <div v-else class="grid grid-cols-1 md:grid-cols-2 gap-6">
            <!-- 여행 계획 목록이 있을 경우 표시될 내용 -->
          </div>
        </div>

        <!-- 동행 초대 탭 -->
        <div v-if="activeTab === 'invites'" class="p-6">
          <h2 class="text-lg font-medium mb-6">동행 초대 목록</h2>
          
          <div v-if="invites.length === 0" class="text-center py-12">
            <p class="text-gray-500 mb-4">아직 받은 초대가 없습니다.</p>
            <button class="px-4 py-2 border border-gray-300 rounded-md text-gray-700 hover:bg-gray-50">
              동행자 찾기
            </button>
          </div>
          
          <div v-else class="grid grid-cols-1 md:grid-cols-2 gap-6">
            <!-- 초대 목록이 있을 경우 표시될 내용 -->
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>

export default {
  name: 'UserProfile',
  
  data() {
    return {
      // 사용자 정보
      username: 'traveler123',
      email: 'traveler123@example.com',
      name: '김여행',
      region: 'seoul',
      age: '30s',
      gender: 'male',
      bio: '안녕하세요! 저는 여행을 좋아하는 30대 직장인입니다. 주로 자연과 맛집을 찾아다니는 여행을 즐기고 있어요. 사진 찍는 것도 좋아해서 예쁜 풍경을 담아두는 것을 좋아합니다.',
      
      // 태그 관련
      personalityTags: ['친절함', '조용함', '계획적', '꼼꼼함', '열정적'],
      travelTags: ['자연', '맛집', '사진', '휴식'],
      newTag: '',
      
      // 설정
      allowInvites: true,
      
      // 탭 관련
      activeTab: 'edit',
      tabs: [
        { id: 'edit', name: '회원정보 수정' },
        { id: 'trips', name: '내 여행 계획' },
        { id: 'invites', name: '동행 초대' }
      ],
      
      // 여행 및 초대 목록
      trips: [],
      invites: []
    };
  },
  methods: {
    addTag(type) {
      if (!this.newTag.trim()) return;
      
      if (type === 'personality') {
        this.personalityTags.push(this.newTag);
      } else {
        this.travelTags.push(this.newTag);
      }
      
      this.newTag = '';
    },
    removeTag(type, tag) {
      if (type === 'personality') {
        this.personalityTags = this.personalityTags.filter(t => t !== tag);
      } else {
        this.travelTags = this.travelTags.filter(t => t !== tag);
      }
    }
  }
};
</script>

<style scoped>
/* 추가적인 스타일이 필요한 경우 여기에 작성 */
</style>
