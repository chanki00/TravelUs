<template>
  <div v-if="isOpen" class="fixed inset-0 z-50 flex items-center justify-center">
    <!-- Backdrop with click-outside to close -->
    <div class="absolute inset-0 bg-black/50" @click="closeModal"></div>
    
    <!-- Modal content -->
    <div class="relative bg-white rounded-lg shadow-xl w-full max-w-4xl max-h-[90vh] overflow-hidden flex flex-col">
      <!-- Header -->
      <div class="p-4 border-b flex justify-between items-center">
        <h2 class="text-xl font-bold text-gray-800">대표 이미지 선택</h2>

        <button @click="closeModal" class="text-gray-500 hover:text-gray-700">
          <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
          </svg>
        </button>
      </div>
      
      <!-- Body -->
      <div class="p-6 overflow-y-auto flex-grow">

        <div class="grid grid-cols-1 md:grid-cols-2 gap-4">

          <div 
            v-for="image in images" 
            :key="image.id"
            class="border rounded-lg overflow-hidden cursor-pointer transition-all duration-200"
            :class="selectedTrip?.id === image.id ? 'ring-2 ring-blue-500 shadow-md' : 'hover:shadow-md'"
            @click="selectTrip(image)"
          >
            <div class="h-40 overflow-hidden relative">
              <img
                :src="image.img"
                :alt="image.title"
                class="w-full h-full object-cover"
              />
              
              <!-- Selection indicator -->
              <div v-if="selectedTrip?.id === image.id" class="absolute top-3 right-3">
                <div class="w-6 h-6 bg-blue-600 rounded-full flex items-center justify-center">
                  <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 text-white" viewBox="0 0 20 20" fill="currentColor">
                    <path fill-rule="evenodd" d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z" clip-rule="evenodd" />
                  </svg>
                </div>
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
          다음
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, defineProps, defineEmits, onMounted } from 'vue'
import api from '@/api'

const props = defineProps({
  isOpen: Boolean
})
const emit = defineEmits(['close', 'save'])

const selectedTrip = ref(null)
const images = ref([
  { id: 1, title: '서울 야경', img: 'https://images.unsplash.com/photo-1549887534-4a23fabc99c5' },
  { id: 2, title: '한강 풍경', img: 'https://images.unsplash.com/photo-1507525428034-b723cf961d3e' },
])

const selectTrip = (image) => {
  selectedTrip.value = image
}

const closeModal = () => {
  emit('close')
}

const applySelectedTrip = () => {
  if (selectedTrip.value) {
    emit('save', selectedTrip.value)
  }
}

</script>
