<template>
  <div v-if="isOpen" class="fixed inset-0 bg-black bg-opacity-50 z-50 flex items-center justify-center">
    <div class="bg-white rounded-lg shadow-xl w-full max-w-md mx-4 overflow-hidden">
      <!-- Modal Header -->
      <div class="px-6 py-4 border-b">
        <h3 class="text-xl font-semibold text-gray-900">여행 태그 선택</h3>
        <p class="text-sm text-gray-500 mt-1">여행 계획에 맞는 태그를 선택해주세요</p>
      </div>
      
      <!-- Modal Body -->
      <div class="px-6 py-4">
        <div class="space-y-4">
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">여행 스타일</label>
            <div class="flex flex-wrap gap-2">
              <button
                v-for="tag in tags"
                :key="tag.id"
                @click="toggleTag(tag.id, 'id')"
                :class="[
                  'px-3 py-1.5 rounded-full text-sm font-medium transition-colors',
                  selectedTags.id.includes(tag.id)
                    ? 'bg-blue-100 text-blue-800 border border-blue-300'
                    : 'bg-gray-100 text-gray-800 border border-gray-200 hover:bg-gray-200'
                ]"
              >
                {{ tag.name }}
              </button>
            </div>
          </div>
          
          
        </div>
      </div>
      
      <!-- Modal Footer -->
      <div class="px-6 py-4 border-t bg-gray-50 flex justify-end space-x-3">
        <button 
          @click="close" 
          class="px-4 py-2 border border-gray-300 rounded-md text-gray-700 bg-white hover:bg-gray-50 transition-colors"
        >
          닫기
        </button>
        <button 
          @click="save" 
          class="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 transition-colors"
        >
          저장하기
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, defineProps, defineEmits, watch, onMounted } from 'vue'
import api from '@/api'

const props = defineProps({
  isOpen: {
    type: Boolean,
    default: false
  },
  initialTags: {
    type: Object,
    default: () => ({
      id : []
    })
  }
})

const emit = defineEmits(['close', 'save'])

// Sample tag data (you can replace this with actual data from your API)
const tags = ref([])

onMounted(async () => {
    const response = await api.get("/api/v1/tag/trip");
    tags.value = response.data
})
// Selected tags state
const selectedTags = ref({
  id: [],
})

// Watch for changes in initialTags prop
watch(() => props.initialTags, (newTags) => {
  if (newTags) {
    selectedTags.value = { ...newTags }
  }
}, { immediate: true })

// Toggle tag selection
const toggleTag = (tagId, category) => {
  if (selectedTags.value[category].includes(tagId)) {
    selectedTags.value[category] = selectedTags.value[category].filter(id => id !== tagId)
  } else {
    selectedTags.value[category] = [...selectedTags.value[category], tagId]
  }
}

// Close modal
const close = () => {
  emit('close')
}

// Save selected tags and close modal
const save = () => {
  emit('save', selectedTags.value)
}
</script>
