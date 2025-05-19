<template>
  <router-link :to="`/trip/${id}`">
    <div class="card-travel group h-full">
      <div class="w-full h-48 overflow-hidden">
        <img
          :src="image"
          :alt="title"
          class="w-full h-full object-cover group-hover:scale-105 transition-transform duration-300"
        />
      </div>
      <div class="p-4">
        <h3 class="font-medium text-lg mb-1">{{ title }}</h3>
        <p class="text-gray-500 text-sm mb-2">{{ location }}</p>
        <div class="flex items-center gap-2">
          <div class="w-6 h-6 rounded-full bg-gray-200 flex items-center justify-center">
            <span class="text-xs">{{ author.charAt(0) }}</span>
          </div>
          <span class="text-xs text-gray-600">{{ author }}</span>
        </div>
        <div v-if="tags && tags.length > 0" class="flex flex-wrap gap-1 mt-3">
          <span 
            v-for="(tag, index) in tags.slice(0, 2)" 
            :key="index" 
            class="px-2 py-1 bg-blue-50 text-blue-600 text-xs rounded-full"
          >
            {{ tag }}
          </span>
          <span 
            v-if="tags.length > 2" 
            class="px-2 py-1 bg-gray-50 text-gray-600 text-xs rounded-full"
          >
            +{{ tags.length - 2 }}
          </span>
        </div>
      </div>
    </div>
  </router-link>
</template>

<script setup>
// 여기에 script 로직이 추가될 예정
// defineProps(['id', 'title', 'location', 'author', 'image', 'tags']);
</script>

<style scoped>
/* 필요한 스타일 추가 */
</style>