<template>
  <div class="w-full max-w-xl mx-auto bg-white rounded-lg border shadow-sm">
    <div class="p-6">
      <h3 class="text-xl font-medium text-center mb-4">{{ question }}</h3>
      
      <div class="flex flex-wrap gap-3 justify-center mb-6">
        <button
          v-for="option in options"
          :key="option.id"
          :class="[
            'px-4 py-2 rounded-md transition-colors',
            isSelected(option.id) 
              ? 'bg-blue-600 text-white' 
              : 'border hover:bg-blue-50 hover:text-blue-600'
          ]"
          @click="handleSelectOption(option.id)"
        >
          {{ option.label }}
        </button>
      </div>
      
      <button 
        class="w-full py-2 px-4 bg-blue-600 hover:bg-blue-700 text-white rounded-md disabled:opacity-50 disabled:cursor-not-allowed"
        :disabled="selectedOptions.length === 0"
        @click="handleSubmit"
      >
        다음
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const props = defineProps({
  question: {
    type: String,
    required: true
  },
  options: {
    type: Array,
    required: true
  },
  allowMultiple: {
    type: Boolean,
    default: false
  }
})

const emit = defineEmits(['select'])

const selectedOptions = ref([])

const isSelected = (optionId) => {
  return selectedOptions.value.includes(optionId)
}

const handleSelectOption = (optionId) => {
  if (props.allowMultiple) {
    // Toggle selection for multi-select
    if (selectedOptions.value.includes(optionId)) {
      selectedOptions.value = selectedOptions.value.filter(id => id !== optionId)
    } else {
      selectedOptions.value.push(optionId)
    }
  } else {
    // Single selection
    selectedOptions.value = [optionId]
  }
}

const handleSubmit = () => {
  if (selectedOptions.value.length > 0) {
    emit('select', selectedOptions.value)
  }
}
</script>

<style scoped>
/* 추가 스타일이 필요한 경우 여기에 작성 */
</style>