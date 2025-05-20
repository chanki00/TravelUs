<template>
  <div class="fixed inset-0 bg-black bg-opacity-50 z-50 flex items-center justify-center p-4">
    <div class="bg-white rounded-lg max-w-2xl w-full max-h-[90vh] overflow-hidden flex flex-col">
      <!-- Header -->
      <div class="p-4 border-b flex items-center justify-between">
        <h3 class="text-xl font-bold">{{ place.title }}</h3>
        <button @click="$emit('close')" class="p-2 hover:bg-gray-100 rounded-full">
          <x-icon class="h-5 w-5" />
        </button>
      </div>

      <!-- Content -->
      <div class="flex-1 overflow-y-auto p-4">
        <div class="flex flex-col md:flex-row gap-4">
          <img
            :src="place.image"
            :alt="place.name"
            class="w-full md:w-1/3 h-48 md:h-auto object-cover rounded-lg"
          />

          <div class="flex-1">
            <div class="flex items-center gap-2 mb-3">
              <span class="px-2 py-1 bg-blue-100 text-blue-800 text-xs rounded-full">
                {{ place.category }}
              </span>
              <div class="flex items-center text-yellow-500">
                <star-icon class="h-4 w-4 fill-current" />
                <span class="ml-1 text-sm">{{ place.rating }}</span>
              </div>
            </div>

            <p class="text-gray-700 mb-4">{{ place.overview }}</p>

            <div class="space-y-2">
              <div class="flex items-start gap-2">
                <map-pin-icon class="h-5 w-5 text-gray-500 mt-0.5" />
                <span>{{ place.addr }}</span>
              </div>

              <div v-if="place.hours" class="flex items-start gap-2">
                <clock-icon class="h-5 w-5 text-gray-500 mt-0.5" />
                <span>{{ place.hours }}</span>
              </div>

              <div v-if="place.tel" class="flex items-start gap-2">
                <phone-icon class="h-5 w-5 text-gray-500 mt-0.5" />
                <span>{{ place.tel }}</span>
              </div>

              <div v-if="place.homepage" class="flex items-start gap-2">
                <globe-icon class="h-5 w-5 text-gray-500 mt-0.5" />
                <a :href="place.homepage" target="_blank" class="text-blue-600 hover:underline">
                  웹사이트 방문
                </a>
              </div>
            </div>

            <!-- Restaurant menu -->
            <div v-if="place.menu" class="mt-4">
              <h4 class="font-medium mb-2">메뉴</h4>
              <ul class="space-y-1">
                <li v-for="(item, index) in place.menu" :key="index" class="text-sm">
                  {{ item }}
                </li>
              </ul>
            </div>

            <!-- Accommodation details -->
            <div v-if="place.checkIn" class="mt-4">
              <h4 class="font-medium mb-2">숙소 정보</h4>
              <div class="space-y-1 text-sm">
                <p>체크인: {{ place.checkIn }}</p>
                <p>체크아웃: {{ place.checkOut }}</p>
              </div>
            </div>

            <!-- Amenities -->
            <div v-if="place.amenities" class="mt-4">
              <h4 class="font-medium mb-2">편의 시설</h4>
              <div class="flex flex-wrap gap-2">
                <span
                  v-for="(amenity, index) in place.amenities"
                  :key="index"
                  class="px-2 py-1 bg-gray-100 text-gray-800 text-xs rounded-full"
                >
                  {{ amenity }}
                </span>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- Footer -->
      <div class="p-4 border-t flex justify-end">
        <button @click="$emit('close')" class="px-4 py-2 border rounded-md hover:bg-gray-50 mr-2">
          닫기
        </button>
        <button
          @click="$emit('add-to-itinerary')"
          class="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 flex items-center gap-1"
        >
          <plus-icon class="h-4 w-4" />
          일정에 추가
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import {
  X as XIcon,
  Plus as PlusIcon,
  MapPin as MapPinIcon,
  Clock as ClockIcon,
  Phone as PhoneIcon,
  Globe as GlobeIcon,
  Star as StarIcon,
} from 'lucide-vue-next'

defineProps({
  place: {
    type: Object,
    required: true,
  },
})

defineEmits(['close', 'add-to-itinerary'])
</script>
