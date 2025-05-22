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
                  
                  <!-- 메모 영역 - 수정 시 레이아웃이 깨지지 않도록 별도 영역으로 분리 -->
                  <div class="mt-2">
                    <div v-if="editingItemId === `${dayIndex}-${itemIndex}`" class="relative">
                      <textarea
                        v-model="editedDescription"
                        @blur="saveDescription(dayIndex, itemIndex)"
                        @keydown.enter="saveDescription(dayIndex, itemIndex)"
                        class="w-full border border-blue-200 focus:border-blue-500 focus:outline-none bg-blue-50 px-2 py-1 rounded text-sm resize-none"
                        rows="2"
                        ref="descriptionTextarea"
                        placeholder="여행 계획에 대한 설명을 입력하세요"
                      ></textarea>
                      <div class="absolute right-2 bottom-2 flex gap-1">
                        <button 
                          @click="saveDescription(dayIndex, itemIndex)" 
                          class="text-xs bg-blue-500 text-white px-2 py-0.5 rounded hover:bg-blue-600"
                        >
                          저장
                        </button>
                        <button 
                          @click="cancelEditing()" 
                          class="text-xs bg-gray-200 text-gray-700 px-2 py-0.5 rounded hover:bg-gray-300"
                        >
                          취소
                        </button>
                      </div>
                    </div>
                    <div v-else class="flex items-center">
                      <p class="text-sm text-gray-600" v-if="item.memo">
                        {{ item.memo }}
                      </p>
                      <p class="text-sm text-gray-400 italic" v-else>
                        메모 추가하기
                      </p>
                      <button 
                        @click="startEditingDescription(dayIndex, itemIndex, item.memo || '')" 
                        class="ml-1 p-1 text-gray-400 hover:text-blue-500 transition-colors rounded-full hover:bg-gray-100"
                      >
                        <pencil-icon class="h-3.5 w-3.5" />
                      </button>
                    </div>
                  </div>
                </div>
                <img :src="item.image" :alt="item.title" class="w-16 h-16 object-cover rounded-md" />
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
import { ref, nextTick, defineEmits, computed } from 'vue'
import { 
  Trash as TrashIcon,
  Calendar as CalendarIcon,
  Pencil as PencilIcon
} from 'lucide-vue-next'

const props = defineProps({
  itinerary: {
    type: Array,
    required: true
  },
  modelValue: Number
})

const emit = defineEmits(['update:modelValue', 'remove-item', 'update-memo'])

const activeDay = computed({
  get: () => props.modelValue,
  set: (val) => emit('update:modelValue', val),
})

// 메모 편집 관련 상태
const editingItemId = ref(null)
const editedDescription = ref('')
const descriptionTextarea = ref(null)

// 메모 편집 시작
const startEditingDescription = (dayIndex, itemIndex, currentDescription) => {
  editingItemId.value = `${dayIndex}-${itemIndex}`
  editedDescription.value = currentDescription
  
}

// 메모 저장
const saveDescription = (dayIndex, itemIndex) => {
  if (editedDescription.value.trim() !== '') {
    // 부모 컴포넌트에 업데이트 이벤트 발생 - memo 필드로 변경
    emit('update-memo', { 
      dayIndex, 
      itemIndex, 
      memo: editedDescription.value.trim() 
    })
    
    // 로컬 데이터 업데이트 (props는 직접 수정하지 않음)
    if (props.itinerary[dayIndex] && props.itinerary[dayIndex].items[itemIndex]) {
      // 이 부분은 실제로 props를 직접 수정하지 않고, 부모 컴포넌트에서 처리하도록 함
    }
  }
  editingItemId.value = null
}

// 메모 편집 취소
const cancelEditing = () => {
  editingItemId.value = null
}

// 일정 항목 제거
const removeItem = (dayIndex, itemIndex) => {
  emit('remove-item', { dayIndex, itemIndex })
}

// 아이템의 메모 가져오기 - memo 필드로 변경
const getItemDescription = (dayIndex, itemIndex) => {
  if (props.itinerary && 
      props.itinerary[dayIndex] && 
      props.itinerary[dayIndex].items && 
      props.itinerary[dayIndex].items[itemIndex]) {
    return props.itinerary[dayIndex].items[itemIndex].memo || ''
  }
  return ''
}
</script>
