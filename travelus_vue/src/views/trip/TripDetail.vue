<template>
  <div class="min-h-screen">

    <div class="relative">
      <div class="h-80 w-full overflow-hidden">
        <img :src="tripDetails.image" :alt="tripDetails.title" class="w-full h-full object-cover" />
        <div class="absolute inset-0 bg-black/30"></div>
      </div>

      <div class="absolute bottom-0 left-0 right-0 p-6 text-white">
        <div class="max-w-7xl mx-auto">
          <div class="flex items-center gap-2 mb-2">
            <span class="px-2 py-1 bg-white/20 backdrop-blur-sm rounded-full text-sm">{{ tripDetails.location }}</span>
            <span class="px-2 py-1 bg-white/20 backdrop-blur-sm rounded-full text-sm">{{ tripDetails.period }}</span>
          </div>
          <h1 class="text-3xl md:text-4xl font-bold mb-2">{{ tripDetails.title }}</h1>
          <div class="flex items-center gap-3">
            <div class="flex items-center gap-2">
              <div class="w-8 h-8 rounded-full bg-white/30 backdrop-blur-sm flex items-center justify-center">
                <span>{{ tripDetails.author.charAt(0) }}</span>
              </div>
              <span>{{ tripDetails.author }}</span>
            </div>
            <div class="flex items-center gap-3">
              <span>♥ {{ tripDetails.likes }}</span>
              <span>💬 {{ tripDetails.comments }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="py-8 px-6 max-w-7xl mx-auto">
      <div class="grid grid-cols-1 lg:grid-cols-3 gap-8">
        <div class="lg:col-span-2">
          <div class="flex flex-col md:flex-row justify-between items-start md:items-center mb-6">
            <h2 class="text-2xl font-bold mb-2 md:mb-0">여행 일정</h2>

            <div class="flex gap-3">
              <button class="px-4 py-2 border rounded-md hover:bg-gray-50">♥ 좋아요</button>
              <button class="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700">내 플래너에 가져오기</button>
            </div>
          </div>

          <div class="mb-8">
            <p class="text-gray-700">{{ tripDetails.description }}</p>
          </div>

          <div class="border-b mb-6">
            <div class="flex">
              <button 
                v-for="(day, index) in tripDetails.itinerary" 
                :key="index"
                :class="[
                  'px-4 py-2 text-center',
                  activeDay === index ? 'border-b-2 border-blue-600 text-blue-600' : 'text-gray-500'
                ]"
                @click="activeDay = index"
              >
                Day {{ index + 1 }}
              </button>
            </div>
          </div>

          <div v-for="(day, dayIndex) in tripDetails.itinerary" :key="dayIndex" v-show="activeDay === dayIndex">
            <div class="bg-white rounded-lg border shadow-sm overflow-hidden">
              <div class="p-4 border-b bg-gray-50">
                <h3 class="text-lg font-medium">Day {{ dayIndex + 1 }}: {{ day.title }}</h3>
              </div>
              <div class="p-4">
                <div class="space-y-6">
                  <div v-for="(item, itemIndex) in day.items" :key="itemIndex" class="flex gap-4">
                    <div class="w-16 text-sm font-medium text-gray-500">{{ item.time }}</div>
                    <div class="relative">
                      <div class="w-3 h-3 rounded-full bg-blue-500 absolute left-0 top-2.5 -translate-x-[7px]"></div>
                      <div class="border-l-2 border-gray-200 pl-4 ml-0.5 pb-6 last:border-l-0 last:pb-0">
                        <h4 class="font-medium">{{ item.title }}</h4>
                        <p class="text-sm text-gray-500 mt-1">{{ item.description }}</p>
                        <span class="mt-2 px-2 py-0.5 bg-blue-50 text-blue-600 text-xs rounded-full inline-block">
                          {{ item.type }}
                        </span>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <div class="mt-8">
            <h3 class="text-xl font-medium mb-4">지도 보기</h3>
            <TripPlannerMap height="400px" />
          </div>

          <div class="mt-8">
            <h3 class="text-xl font-medium mb-4">댓글</h3>
            <div class="bg-white rounded-lg border shadow-sm overflow-hidden">
              <div class="p-6">
                <p class="text-gray-500">이 여행 계획에 대한 의견을 남겨주세요.</p>
                <p class="text-sm text-gray-400 mt-1">로그인 후 댓글을 작성할 수 있습니다.</p>
              </div>
            </div>
          </div>
        </div>

        <div class="lg:col-span-1">
          <div class="bg-white rounded-lg border shadow-sm sticky top-24">
            <div class="p-4 border-b">
              <h3 class="text-lg font-medium">여행 정보</h3>
            </div>
            <div class="p-4 space-y-4">
              <div>
                <h4 class="text-sm text-gray-500 mb-1">작성자 성격</h4>
                <div class="flex flex-wrap gap-2">
                  <span 
                    v-for="(tag, index) in tripDetails.personalityTags" 
                    :key="index"
                    class="px-2 py-1 bg-purple-50 text-purple-600 text-xs rounded-full"
                  >
                    {{ tag }}
                  </span>
                </div>
              </div>

              <div>
                <h4 class="text-sm text-gray-500 mb-1">여행 스타일</h4>
                <div class="flex flex-wrap gap-2">
                  <span 
                    v-for="(tag, index) in tripDetails.travelTags" 
                    :key="index"
                    class="px-2 py-1 bg-blue-50 text-blue-600 text-xs rounded-full"
                  >
                    {{ tag }}
                  </span>
                </div>
              </div>

              <div class="pt-4 border-t">
                <button class="w-full px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 flex items-center justify-center">
                  내 플래너에 가져오기
                  <arrow-right-icon class="ml-2 h-4 w-4" />
                </button>
              </div>

              <div class="pt-4 border-t">
                <h4 class="text-sm text-gray-500 mb-2">이 작성자의 다른 여행 계획</h4>
                <ul class="space-y-3">
                  <li>
                    <a href="#" class="text-sm text-blue-600 hover:underline">
                      강릉 2박 3일 여름 여행
                    </a>
                  </li>
                  <li>
                    <a href="#" class="text-sm text-blue-600 hover:underline">
                      부산 맛집 투어 주말 여행
                    </a>
                  </li>
                  <li>
                    <a href="#" class="text-sm text-blue-600 hover:underline">
                      서울 근교 당일치기 코스
                    </a>
                  </li>
                </ul>
              </div>

              <div class="pt-4 border-t">
                <h4 class="text-sm text-gray-500 mb-2">비슷한 여행 계획</h4>
                <ul class="space-y-3">
                  <li>
                    <a href="#" class="text-sm text-blue-600 hover:underline">
                      제주 서부권 3박 4일 가족 여행
                    </a>
                  </li>
                  <li>
                    <a href="#" class="text-sm text-blue-600 hover:underline">
                      제주도 신혼여행 코스 5박 6일
                    </a>
                  </li>
                  <li>
                    <a href="#" class="text-sm text-blue-600 hover:underline">
                      제주 맛집만 모은 먹방 여행
                    </a>
                  </li>
                </ul>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRoute } from 'vue-router'
import { ArrowRight as ArrowRightIcon } from 'lucide-vue-next'
import TripPlannerMap from '@/components/trip/TripPlannerMap.vue'

const route = useRoute()
const activeDay = ref(0)

// 실제 구현에서는 ID를 기반으로 데이터를 가져오는 로직 추가
const tripDetails = {
  id: route.params.id || "1",
  title: "제주도 4박 5일 힐링 여행",
  location: "제주도",
  period: "4박 5일",
  author: "여행자123",
  image:
    "https://images.unsplash.com/photo-1601621915196-2ad9b06857b3?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1200&q=80",
  personalityTags: ["친절함", "차분함", "계획적"],
  travelTags: ["힐링", "자연", "맛집"],
  likes: 153,
  comments: 24,
  description: "제주도의 아름다운 자연을 즐기면서 힐링하는 여행. 유명 관광지부터 숨은 맛집까지 모두 담았습니다.",
  itinerary: [
    {
      day: 1,
      title: "제주 도착 및 동부 탐험",
      items: [
        { time: "09:00", title: "제주공항 도착", description: "렌터카 수령", type: "이동" },
        { time: "11:00", title: "성산일출봉", description: "유네스코 세계자연유산", type: "관광" },
        { time: "13:00", title: "섭지코지", description: "드라마 촬영지로 유명한 해안가", type: "관광" },
        { time: "15:00", title: "우도 탐방", description: "페리로 이동하여 우도 탐방", type: "관광" },
        { time: "18:00", title: "흑돼지 맛집", description: "제주 흑돼지 전문점", type: "식사" },
        { time: "20:00", title: "호텔 체크인", description: "서귀포 호텔", type: "숙박" },
      ],
    },
    {
      day: 2,
      title: "서부 자연 체험",
      items: [
        { time: "09:00", title: "아침 식사", description: "호텔 조식", type: "식사" },
        { time: "10:30", title: "올레길 걷기", description: "7코스 서귀포 올레길", type: "액티비티" },
        { time: "13:00", title: "점심 식사", description: "해산물 요리", type: "식사" },
        { time: "15:00", title: "한라산 둘레길", description: "쉬운 코스로 산책", type: "액티비티" },
        { time: "18:00", title: "흑돼지 BBQ", description: "로컬 맛집", type: "식사" },
      ],
    },
    {
      day: 3,
      title: "중문 관광단지",
      items: [
        { time: "09:00", title: "아침 식사", description: "호텔 조식", type: "식사" },
        { time: "10:30", title: "주상절리", description: "천연기념물 해안 절벽", type: "관광" },
        { time: "12:30", title: "점심 식사", description: "전복죽", type: "식사" },
        { time: "14:00", title: "카페 투어", description: "뷰 좋은 카페 방문", type: "휴식" },
        { time: "16:00", title: "테디베어 박물관", description: "중문관광단지 내 위치", type: "관광" },
        { time: "18:30", title: "저녁 식사", description: "해물 뚝배기", type: "식사" },
      ],
    },
    {
      day: 4,
      title: "북부 탐험",
      items: [
        { time: "08:30", title: "아침 식사", description: "호텔 조식", type: "식사" },
        { time: "10:00", title: "만장굴", description: "세계자연유산 용암동굴", type: "관광" },
        { time: "12:30", title: "점심 식사", description: "고등어회", type: "식사" },
        { time: "14:00", title: "에코랜드", description: "기차 테마파크", type: "관광" },
        { time: "16:30", title: "제주민속촌", description: "전통 제주 마을 체험", type: "관광" },
        { time: "19:00", title: "저녁 식사", description: "제주 흑돼지", type: "식사" },
      ],
    },
    {
      day: 5,
      title: "마무리 및 귀가",
      items: [
        { time: "09:00", title: "아침 식사", description: "호텔 조식 후 체크아웃", type: "식사" },
        { time: "10:30", title: "이호테우 해변", description: "하얀 등대가 있는 해변", type: "관광" },
        { time: "12:00", title: "제주공항 근처 점심", description: "해물탕", type: "식사" },
        { time: "14:00", title: "제주공항 도착", description: "렌터카 반납", type: "이동" },
        { time: "16:00", title: "제주공항 출발", description: "귀가", type: "이동" },
      ],
    },
  ],
}
</script>

<style scoped>
/* 추가 스타일이 필요한 경우 여기에 작성 */
</style>