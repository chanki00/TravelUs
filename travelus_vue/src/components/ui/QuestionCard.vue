<template>
  <div class="w-full max-w-xl mx-auto rounded-lg border bg-card text-card-foreground shadow-sm">
    <div class="flex flex-col space-y-1.5 p-6">
      <h3 class="text-xl font-medium text-center">{{ question }}</h3>
    </div>
    <div class="p-6 pt-0">
      <div class="flex flex-wrap gap-3 justify-center mb-6">
        <button
          v-for="option in options"
          :key="option.id"
          :class="[
            'inline-flex items-center justify-center whitespace-nowrap rounded-md text-sm font-medium ring-offset-background transition-colors focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:pointer-events-none disabled:opacity-50',
            selectedOptions.includes(option.id) 
              ? 'bg-blue-600 text-white' 
              : 'border border-input bg-background hover:bg-blue-50 hover:text-blue-600'
          ]"
          @click="handleSelectOption(option.id)"
        >
          {{ option.label }}
        </button>
      </div>
      <button 
        class="w-full inline-flex items-center justify-center whitespace-nowrap rounded-md text-sm font-medium ring-offset-background transition-colors focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:pointer-events-none disabled:opacity-50 bg-primary text-primary-foreground hover:bg-primary/90 h-10 px-4 py-2"
        :disabled="selectedOptions.length === 0"
        @click="handleSubmit"
      >
        다음
      </button>
    </div>
  </div>
</template>

<script setup>
// 여기에 script 로직이 추가될 예정
// import { ref } from 'vue';
// const props = defineProps({
//   question: String,
//   options: Array,
//   onSelect: Function,
//   allowMultiple: Boolean
// });
// const selectedOptions = ref([]);
// 
// function handleSelectOption(optionId) {...}
// function handleSubmit() {...}
</script>

<style scoped>
/* 필요한 스타일 추가 */
</style>