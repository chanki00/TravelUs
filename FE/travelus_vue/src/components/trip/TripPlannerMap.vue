<template>
  <div 
    ref="mapContainer" 
    class="bg-gray-100 rounded-lg overflow-hidden relative h-full" 
  >
    <div class="absolute inset-0 flex items-center justify-center">
      <div class="text-center">
        <p class="text-gray-500 mb-2">지도가 여기에 표시됩니다</p>
        <p class="text-sm text-gray-400">실제 구현 시에는 지도 API를 연동하세요</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'

const props = defineProps({
  locations: {
    type: Array,
    default: () => []
  }
})

const mapContainer = ref(null)

onMounted(() => {
  // 실제 구현 시 지도 초기화 로직 추가
  console.log("Map would be initialized with locations:", props.locations)
})

watch(() => props.locations, (newLocations) => {
  // 위치 변경 시 지도 업데이트 로직 추가
  console.log("Map would be updated with locations:", newLocations)
}, { deep: true })
</script>
