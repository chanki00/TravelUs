<template>
  <div>
    <button 
      @click="isOpen = true"
      class="inline-flex items-center justify-center gap-2 px-4 py-2 rounded-md border bg-white hover:bg-gray-50"
    >
      <users-icon class="h-4 w-4" />
      <span>참여자 {{ members.length }}명</span>
    </button>

    <div v-if="isOpen" class="fixed inset-0 z-50 flex items-center justify-center">
      <!-- Backdrop -->
      <div class="absolute inset-0 bg-black/50" @click="isOpen = false"></div>
      
      <!-- Content -->
      <div class="relative bg-white rounded-lg shadow-lg w-full max-w-md overflow-hidden">
        <div class="p-4 border-b">
          <h2 class="text-lg font-semibold">여행 참여자 목록</h2>
        </div>
        
        <div class="py-4 px-4">
          <div class="space-y-4">
            <div 
              v-for="member in members" 
              :key="member.id"
              class="flex items-center justify-between"
            >
              <div class="flex items-center gap-3">
                <div class="w-10 h-10 rounded-full bg-gray-200 overflow-hidden">
                  <img v-if="member.image" :src="member.image" :alt="member.name" class="w-full h-full object-cover" />
                  <div v-else class="w-full h-full flex items-center justify-center font-medium">
                    {{ member.name.slice(0, 2) }}
                  </div>
                </div>
                <div>
                  <div class="font-medium flex items-center gap-2">
                    {{ member.name }}
                    <span 
                      v-if="member.role === '호스트'"
                      class="px-2 py-0.5 text-xs bg-gray-100 rounded-full"
                    >
                      호스트
                    </span>
                  </div>
                  <div class="flex items-center gap-1.5 text-sm">
                    <div
                      :class="[
                        'w-2 h-2 rounded-full',
                        member.status === 'online' ? 'bg-green-500' : 'bg-gray-300'
                      ]"
                    ></div>
                    <span class="text-gray-500">{{ member.status === 'online' ? '온라인' : '오프라인' }}</span>
                  </div>
                </div>
              </div>
              <button class="text-sm text-gray-600 hover:text-gray-900">
                메시지
              </button>
            </div>
          </div>
        </div>
        
        <div class="p-4 border-t flex justify-end">
          <button 
            class="px-4 py-2 bg-gray-100 rounded-md hover:bg-gray-200"
            @click="isOpen = false"
          >
            닫기
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { Users as UsersIcon } from 'lucide-vue-next'

const props = defineProps({
  members: {
    type: Array,
    default: () => []
  }
})

const isOpen = ref(false)
</script>

<style scoped>
/* 추가 스타일이 필요한 경우 여기에 작성 */
</style>