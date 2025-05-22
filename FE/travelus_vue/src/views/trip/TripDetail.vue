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
              <span class="px-2 py-1 bg-white/20 backdrop-blur-sm rounded-full text-sm">{{ tripDetails.duration -1}}박 {{ tripDetails.duration }}일</span>
            </div>
            <h1 class="text-3xl md:text-4xl font-bold mb-2">{{ tripDetails.title }}</h1>
            <div class="flex items-center gap-3">
              <div class="flex items-center gap-2">
                <div class="w-8 h-8 rounded-full bg-white/30 backdrop-blur-sm flex items-center justify-center">
                  <span>{{ tripDetails.userId.charAt(0) }}</span>
                </div>
                <span>{{ tripDetails.userId }}</span>
              </div>
              <div class="flex items-center gap-3">
                <span>♥ {{ tripDetails.likes }}</span>
                <span>💬 {{ tripDetails.shares }}</span>
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
                  <div class="w-2/5 p-4 h-full overflow-hidden">
          <TripPlannerMap :locations="[]" :itinerary="itinerary" :active-day="activeDay"/>
        </div>          
                
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
                  <h4 class="text-sm text-gray-500 mb-1">작성자 성격(미구현)</h4>
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
                        미구현 상태
                      </a>
                    </li>
                    <li>
                      <a href="#" class="text-sm text-blue-600 hover:underline">
                        미구현 상태
                      </a>
                    </li>
                    <li>
                      <a href="#" class="text-sm text-blue-600 hover:underline">
                        미구현 상태
                      </a>
                    </li>
                  </ul>
                </div>

                <div class="pt-4 border-t">
                  <h4 class="text-sm text-gray-500 mb-2">비슷한 여행 계획</h4>
                  <ul class="space-y-3">
                    <li>
                      <a href="#" class="text-sm text-blue-600 hover:underline">
                        미구현 상태
                      </a>
                    </li>
                    <li>
                      <a href="#" class="text-sm text-blue-600 hover:underline">
                        미구현 상태
                      </a>
                    </li>
                    <li>
                      <a href="#" class="text-sm text-blue-600 hover:underline">
                        미구현 상태 
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
  import { onMounted, ref, computed } from 'vue'
  import { useRoute } from 'vue-router'
  import { ArrowRight as ArrowRightIcon } from 'lucide-vue-next'
  import { useUserStore } from '@/store/user'
  import TripPlannerMap from '@/components/trip/TripPlannerMap.vue'
  import api from '@/api'

  const userStore = useUserStore()
  const user = computed(() => userStore.loginUser)
  const route = useRoute()
  const activeDay = ref(0)
  // 실제 구현에서는 ID를 기반으로 데이터를 가져오는 로직 추가
  const tripDetails = ref({
    id: route.params.id || "id",
    title: "제목",
    location: "지역",
    duration: 0,
    userId: "유저ID",
    image:
      "https://images.unsplash.com/photo-1601621915196-2ad9b06857b3?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1200&q=80",
    personalityTags: [],
    travelTags: [],
    likes: 0,
    shares: 0,
    description: "여행 계획 설명",
    itinerary: [
    ],
  })

  // 일정 초기화
  const itinerary = ref([])
  console.log(itinerary.value)
  onMounted(async () => {
      const response = await api.get(`/api/v1/plan/${route.params.id}`);
      tripDetails.value.title = response.data.title;
      tripDetails.value.duration = response.data.duration;
      tripDetails.value.userId = user.value.name+ '님';
      tripDetails.value.description = response.data.description
      tripDetails.value.likes = response.data.likes
      tripDetails.value.shares = response.data.shares
      tripDetails.value.location = response.data.destination
      tripDetails.value.image = response.data.image
      
      for (let i = 0; i < response.data.duration; i++) {
      itinerary.value.push({day: i, items: []})
    }
      const tagRes = await api.get(`/api/v1/tag/plan/${route.params.id}`)
      tripDetails.value.travelTags = tagRes.data
      const itineraryRes = await api.get(`/api/v1/plan/itinerary/${route.params.id}`)
      const typeMap = {
        12: "관광",
        39: "식당",
        32: "숙박",
        // 필요한 다른 매핑 추가
      };

      // 날짜별로 그룹핑
      const grouped = itineraryRes.data.reduce((acc, cur) => {
        const { dayNumber } = cur;
        if (!acc[dayNumber]) acc[dayNumber] = [];

        acc[dayNumber].push({
          time: "", // 현재 시간 정보가 없으므로 빈 문자열
          title: cur.title,
          description: cur.memo || "",
          type: typeMap[cur.contentTypeId] || "기타", // fallback
        });

        return acc;
      }, {});

      // tripDetails.itinerary 구성
      tripDetails.value.itinerary = Object.entries(grouped).map(([day, items]) => ({
        day: Number(day),
        title: "", // 서버에서 안 준다면 비워둡니다 (또는 직접 작성)
        items,
      }));
      itineraryRes.data.forEach((res)=> {
         itinerary.value[res.dayNumber-1].items.push({
            day : res.dayNumber,
            items : {
              id : res.contentId,
              image : res.image,
              memo : res.memo,
              placeData :{
                addr: res.addr,
                areaCode: res.areaCode,
                contentId: res.contentId,
                contentTypeId: res.contentTypeId,
                homepage: res.homepage,
                image: res.image,
                latitude: res.latitude,
                longitude: res.longitude,
                mapLevel: res.mapLevel,
                no: res.no,
                  overview: res.overview,
                  sigunguCode: res.sigunguCode,
                  tel: res.tel,
                  title: res.title
                },
              time : '',
              title : res.title,
              type : res.contentTypeId


        }
      });
      })
     
      })
  </script>

  <style scoped>
  /* 추가 스타일이 필요한 경우 여기에 작성 */
  </style>