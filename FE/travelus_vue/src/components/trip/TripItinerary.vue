<template>
  <div class="h-full flex flex-col">
    <h2 class="text-xl font-medium mb-4">일정표</h2>

    <div class="flex border-b mb-4">
      <button 
        v-for="(day, index) in itinerary" 
        :key="index"
        :class="[
          'flex-1 py-2 px-4 text-center',
          activeDay === index ? 'border-b-2 border-blue-600 text-blue-600' : 'text-gray-500'
        ]"
        @click="activeDay = index"
      >
        Day {{ index + 1 }}
      </button>
    </div>

    <div class="flex-1 overflow-auto">
      <div v-for="(day, dayIndex) in itinerary" :key="dayIndex" v-show="activeDay === dayIndex">
        <div class="border rounded-md overflow-hidden">
          <div class="p-4 border-b bg-gray-50">
            <h3 class="text-lg font-medium">{{ dayIndex + 1 }}일차</h3>
          </div>
          <div class="p-4">
            <div v-if="day.items.length === 0" class="text-center py-8 text-gray-500">
              <calendar-icon class="h-12 w-12 mx-auto mb-2 text-gray-300" />
              <p>아직 일정이 없습니다</p>
              <p class="text-sm mt-1">검색 결과에서 장소를 추가해보세요</p>
            </div>
            <div v-else class="space-y-4">
              <div 
                v-for="(item, itemIndex) in day.items" 
                :key="itemIndex"
                class="flex items-start gap-3 pb-3 border-b last:border-b-0 last:pb-0 group"
              >
                <div class="text-sm font-medium text-gray-500 w-12">{{ item.time }}</div>
                <div class="flex-1">
                  <div class="font-medium">{{ item.title }}</div>
                  <div class="text-xs text-blue-600 bg-blue-50 px-2 py-0.5 rounded-full inline-block mt-1">
                    {{ item.type }}
                  </div>
                </div>
                <button 
                  @click="removeItem(dayIndex, itemIndex)" 
                  class="p-1.5 text-gray-400 group-hover:text-red-500 transition-colors rounded-full hover:bg-gray-100"
                  title="일정에서 제거"
                >
                  <trash-icon class="h-4 w-4" />
                </button>
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
import { 
  Trash as TrashIcon,
  Calendar as CalendarIcon
} from 'lucide-vue-next'

const props = defineProps({
  itinerary: {
    type: Array,
    required: true
  }
})

const emit = defineEmits(['remove-item'])

const activeDay = ref(0)

// 일정 항목 제거
const removeItem = (dayIndex, itemIndex) => {
  emit('remove-item', { dayIndex, itemIndex })
}
</script>
