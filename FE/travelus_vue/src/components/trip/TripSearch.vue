<template>
  <div class="h-full flex flex-col">
    <!-- Tab navigation -->
    <div class="flex border-b">
      <button 
        v-for="(label, tab) in tabLabels" 
        :key="tab"
        :class="[
          'flex-1 py-2 px-4 text-center',
          activeTab === tab ? 'border-b-2 border-blue-600 text-blue-600' : 'text-gray-500'
        ]"
        @click="activeTab = tab"
      >
        {{ label }}
      </button>
    </div>

    <!-- Search area -->
    <div class="mt-4 flex-1 flex flex-col">
      <div class="flex items-center gap-2 mb-4">
        <div class="flex-1 relative">
          <input 
            type="text" 
            placeholder="장소 검색하기" 
            class="w-full px-3 py-2 pl-10 border rounded-md"
          />
          <span class="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400">
            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-search">
              <circle cx="11" cy="11" r="8"></circle>
              <path d="m21 21-4.3-4.3"></path>
            </svg>
          </span>
        </div>
        <button class="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 transition-colors">
          검색
        </button>
      </div>

      <!-- Results area - scrollable -->
      <div class="flex-1 overflow-y-auto pr-2 custom-scrollbar space-y-3">
        <div 
          v-if="activeTab === 'attractions'"
          v-for="place in mockPlaces.attractions" 
          :key="place.id"
          class="border rounded-md p-3 flex items-center gap-3 cursor-pointer hover:border-blue-200 transition-colors"
        >
          <div class="flex-1 flex items-center gap-3" @click="showPlaceDetail(place)">
            <img :src="place.image" :alt="place.name" class="w-16 h-16 object-cover rounded-md" />
            <div>
              <h4 class="font-medium">{{ place.name }}</h4>
              <div class="flex items-center gap-1 text-sm text-gray-500">
                <span>{{ place.category }}</span>
                <span>•</span>
                <span>★ {{ place.rating }}</span>
              </div>
            </div>
          </div>
          <button 
            @click="addToItinerary(place)" 
            class="p-2 text-blue-600 hover:bg-blue-50 rounded-full transition-colors"
            title="일정에 추가"
          >
            <plus-icon class="h-5 w-5" />
          </button>
        </div>

        <div 
          v-if="activeTab === 'restaurants'"
          v-for="place in mockPlaces.restaurants" 
          :key="place.id"
          class="border rounded-md p-3 flex items-center gap-3 cursor-pointer hover:border-blue-200 transition-colors"
        >
          <div class="flex-1 flex items-center gap-3" @click="showPlaceDetail(place)">
            <img :src="place.image" :alt="place.name" class="w-16 h-16 object-cover rounded-md" />
            <div>
              <h4 class="font-medium">{{ place.name }}</h4>
              <div class="flex items-center gap-1 text-sm text-gray-500">
                <span>{{ place.category }}</span>
                <span>•</span>
                <span>★ {{ place.rating }}</span>
              </div>
            </div>
          </div>
          <button 
            @click="addToItinerary(place)" 
            class="p-2 text-blue-600 hover:bg-blue-50 rounded-full transition-colors"
            title="일정에 추가"
          >
            <plus-icon class="h-5 w-5" />
          </button>
        </div>
        <div 
          v-if="activeTab === 'accommodations'"
          v-for="place in mockPlaces.accommodations" 
          :key="place.id"
          class="border rounded-md p-3 flex items-center gap-3 cursor-pointer hover:border-blue-200 transition-colors"
        >
          <div class="flex-1 flex items-center gap-3" @click="showPlaceDetail(place)">
            <img :src="place.image" :alt="place.name" class="w-16 h-16 object-cover rounded-md" />
            <div>
              <h4 class="font-medium">{{ place.name }}</h4>
              <div class="flex items-center gap-1 text-sm text-gray-500">
                <span>{{ place.category }}</span>
                <span>•</span>
                <span>★ {{ place.rating }}</span>
              </div>
            </div>
          </div>
          <button 
            @click="addToItinerary(place)" 
            class="p-2 text-blue-600 hover:bg-blue-50 rounded-full transition-colors"
            title="일정에 추가"
          >
            <plus-icon class="h-5 w-5" />
          </button>
        </div>
      </div>
    </div>

    <!-- Place Detail Modal -->
    <PlaceDetailModal 
      v-if="selectedPlace" 
      :place="selectedPlace" 
      @close="selectedPlace = null"
      @add-to-itinerary="addToItinerary(selectedPlace)"
    />
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { Plus as PlusIcon } from 'lucide-vue-next'
import PlaceDetailModal from './PlaceDetailModal.vue'

const activeTab = ref('attractions')
const selectedPlace = ref(null)

const emit = defineEmits(['add-to-itinerary'])

// 탭 레이블
const tabLabels = {
  attractions: '명소',
  restaurants: '식당',
  accommodations: '숙소'
}

const mockPlaces = {
  attractions: [
    {
      id: "attr1",
      name: "유명한 명소",
      category: "관광지",
      rating: 4.8,
      image: "https://images.unsplash.com/photo-1533929736458-ca588d08c8be?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=200&q=80",
      description: "이 유명한 명소는 많은 관광객들이 찾는 곳으로, 아름다운 경치와 역사적인 가치를 지니고 있습니다.",
      address: "제주특별자치도 제주시 첨단로 242",
      hours: "09:00 - 18:00 (매일)",
      phone: "064-123-4567",
      website: "https://example.com",
    },
    {
      id: "attr2",
      name: "역사적인 장소",
      category: "역사",
      rating: 4.6,
      image: "https://images.unsplash.com/photo-1526815456940-2c11653292a2?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=200&q=80",
      description: "오랜 역사를 간직한 이 장소는 문화재로 지정되어 있으며, 한국의 전통 건축양식을 볼 수 있습니다.",
      address: "제주특별자치도 서귀포시 중문관광로 224",
      hours: "09:00 - 17:00 (월요일 휴무)",
      phone: "064-234-5678",
      website: "https://example.com",
    },
    {
      id: "attr3",
      name: "아름다운 공원",
      category: "자연",
      rating: 4.5,
      image: "https://images.unsplash.com/photo-1519331379826-f10be5486c6f?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=200&q=80",
      description: "울창한 숲과 다양한 식물들이 있는 이 공원은 휴식과 산책을 즐기기에 완벽한 장소입니다.",
      address: "제주특별자치도 제주시 노형동 2565",
      hours: "항상 개방",
      phone: "064-345-6789",
      website: "https://example.com",
    },
  ],
  restaurants: [
    {
      id: "rest1",
      name: "맛있는 식당 1",
      category: "한식",
      rating: 4.5,
      image: "https://images.unsplash.com/photo-1414235077428-338989a2e8c0?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=200&q=80",
      description: "제주도의 신선한 해산물을 사용한 전통 한식을 맛볼 수 있는 곳입니다.",
      address: "제주특별자치도 제주시 연동 312-1",
      hours: "11:00 - 22:00 (매일)",
      phone: "064-456-7890",
      website: "https://example.com",
      menu: ["해물탕 - 45,000원", "고등어구이 - 18,000원", "전복죽 - 15,000원"]
    },
    {
      id: "rest2",
      name: "좋은 레스토랑",
      category: "양식",
      rating: 4.2,
      image: "https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=200&q=80",
      description: "제주도에서 맛볼 수 있는 최고의 양식 레스토랑으로, 현지 식재료를 활용한 창의적인 요리를 선보입니다.",
      address: "제주특별자치도 서귀포시 색달동 2812-4",
      hours: "17:00 - 23:00 (월요일 휴무)",
      phone: "064-567-8901",
      website: "https://example.com",
      menu: ["스테이크 - 38,000원", "파스타 - 22,000원", "샐러드 - 15,000원"]
    },
    {
      id: "rest3",
      name: "로컬 맛집",
      category: "분식",
      rating: 4.7,
      image: "https://images.unsplash.com/photo-1565299585323-38d6b0865b47?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=200&q=80",
      description: "현지인들이 자주 찾는 분식점으로, 푸짐한 양과 합리적인 가격이 특징입니다.",
      address: "제주특별자치도 제주시 일도2동 991-21",
      hours: "10:00 - 20:00 (일요일 휴무)",
      phone: "064-678-9012",
      website: "https://example.com",
      menu: ["떡볶이 - 5,000원", "김밥 - 3,500원", "라면 - 4,000원"]
    },
  ],
 
  accommodations: [
    {
      id: "acc1",
      name: "좋은 호텔",
      category: "호텔",
      rating: 4.3,
      image: "https://images.unsplash.com/photo-1566073771259-6a8506099945?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=200&q=80",
      description: "제주도의 아름다운 해변 근처에 위치한 고급 호텔입니다. 수영장과 스파 시설을 갖추고 있습니다.",
      address: "제주특별자치도 서귀포시 중문관광로 332-2",
      checkIn: "15:00",
      checkOut: "11:00",
      phone: "064-901-2345",
      website: "https://example.com",
      amenities: ["수영장", "스파", "레스토랑", "무료 와이파이", "주차장"]
    },
    {
      id: "acc2",
      name: "아늑한 펜션",
      category: "펜션",
      rating: 4.5,
      image: "https://images.unsplash.com/photo-1520250497591-112f2f40a3f4?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=200&q=80",
      description: "자연 속에 위치한 아늑한 펜션으로, 바베큐 시설과 정원이 있습니다.",
      address: "제주특별자치도 제주시 애월읍 애월해안로 940",
      checkIn: "14:00",
      checkOut: "12:00",
      phone: "064-012-3456",
      website: "https://example.com",
      amenities: ["바베큐 시설", "정원", "무료 와이파이", "주방", "주차장"]
    },
  ],
}

// 장소 상세 정보 표시
const showPlaceDetail = (place) => {
  selectedPlace.value = place
}

// 일정에 장소 추가
const addToItinerary = (place) => {
  emit('add-to-itinerary', {
    id: place.id,
    title: place.name,
    type: place.category,
    time: new Date().toLocaleTimeString('ko-KR', { hour: '2-digit', minute: '2-digit' }),
    image: place.image,
    placeData: place
  })
}
</script>

<style scoped>
/* Custom scrollbar styling */
.custom-scrollbar::-webkit-scrollbar {
  width: 6px;
}

.custom-scrollbar::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 10px;
}

.custom-scrollbar::-webkit-scrollbar-thumb {
  background: #c5c5c5;
  border-radius: 10px;
}

.custom-scrollbar::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}

/* Transition effects */
.transition-colors {
  transition: all 0.2s ease;
}
</style>
