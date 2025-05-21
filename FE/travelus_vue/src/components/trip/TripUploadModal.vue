<template>
  <div v-if="isOpen" class="fixed inset-0 z-50 flex items-center justify-center">
    <!-- Backdrop with click-outside to close -->
    <div class="absolute inset-0 bg-black/50" @click="closeModal"></div>
    
    <!-- Modal content -->
    <div class="relative bg-white rounded-lg shadow-xl w-full max-w-4xl max-h-[90vh] overflow-hidden flex flex-col">
      <!-- Header -->
      <div class="p-4 border-b flex justify-between items-center">
        <h2 class="text-xl font-bold text-gray-800">내 여행 계획 선택</h2>
        <button @click="closeModal" class="text-gray-500 hover:text-gray-700">
          <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
          </svg>
        </button>
      </div>
      
      <!-- Body -->
      <div class="p-6 overflow-y-auto flex-grow">
        <div v-if="myTrips.length === 0" class="text-center py-12">
          <p class="text-gray-500">저장된 여행 계획이 없습니다.</p>
          <button class="mt-4 px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700">
            새 여행 계획 만들기
          </button>
        </div>
        
        <div v-else class="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div 
            v-for="trip in myTrips" 
            :key="trip.id"
            class="border rounded-lg overflow-hidden cursor-pointer transition-all duration-200"
            :class="selectedTrip?.id === trip.id ? 'ring-2 ring-blue-500 shadow-md' : 'hover:shadow-md'"
            @click="selectTrip(trip)"
          >
            <div class="h-40 overflow-hidden relative">
              <img
                :src="trip.image"
                :alt="trip.title"
                class="w-full h-full object-cover"
              />
              <div class="absolute top-3 left-3">
                <span class="px-2 py-1 bg-white/70 backdrop-blur-sm text-gray-800 text-xs rounded-full">
                  {{ trip.location }}
                </span>
              </div>
              
              <!-- Selection indicator -->
              <div v-if="selectedTrip?.id === trip.id" class="absolute top-3 right-3">
                <div class="w-6 h-6 bg-blue-600 rounded-full flex items-center justify-center">
                  <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 text-white" viewBox="0 0 20 20" fill="currentColor">
                    <path fill-rule="evenodd" d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z" clip-rule="evenodd" />
                  </svg>
                </div>
              </div>
            </div>
            
            <div class="p-3">
              <h3 class="font-medium text-base mb-1">{{ trip.title }}</h3>
              
              <div class="flex flex-wrap gap-1 mb-2">
                <span 
                  v-for="(tag, index) in trip.tags" 
                  :key="index"
                  class="px-2 py-0.5 bg-blue-50 text-blue-600 text-xs rounded-full"
                >
                  {{ tag }}
                </span>
              </div>
              
              <div class="flex items-center justify-between text-xs text-gray-500">
                <span>{{ formatDate(trip.created) }}</span>
                <span>{{ trip.days }}일 일정</span>
              </div>
            </div>
          </div>
        </div>
      </div>
      
      <!-- Footer -->
      <div class="p-4 border-t flex justify-end gap-3">
        <button 
          @click="closeModal" 
          class="px-4 py-2 border rounded-md hover:bg-gray-50"
        >
          취소
        </button>
        <button 
          @click="applySelectedTrip" 
          class="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700"
          :disabled="!selectedTrip"
          :class="!selectedTrip ? 'opacity-50 cursor-not-allowed' : ''"
        >
          적용하기
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, defineProps, defineEmits, onMounted} from 'vue'
import api from '@/api'

const props = defineProps({
  isOpen: {
    type: Boolean,
    default: false
  }
})

const emit = defineEmits(['close', 'apply'])

const selectedTrip = ref(null)

// 내 여행 계획 데이터 (실제로는 API에서 가져올 수 있음)
const myTrips = ref([])

// 여행 계획 선택
const selectTrip = (trip) => {
  selectedTrip.value = trip

}

// 선택한 여행 계획 적용
const applySelectedTrip = () => {
  if (selectedTrip.value) {
    emit('apply', selectedTrip.value)
    closeModal()
  }
}

// 모달 닫기
const closeModal = () => {
  selectedTrip.value = null
  emit('close')
  getPlans()
}

// 날짜 포맷팅
const formatDate = (dateString) => {
  const date = new Date(dateString)
  return `${date.getFullYear()}.${String(date.getMonth() + 1).padStart(2, '0')}.${String(date.getDate()).padStart(2, '0')}`
}

onMounted(async () => {
    getPlans()
})

const getPlans = async () => {
 // const response = await api.get(`/api/v1/plan/user/${userId}`)
    const response = await api.get(`/api/v1/plan/user/1`)
    myTrips.value = response.data
}

</script>
