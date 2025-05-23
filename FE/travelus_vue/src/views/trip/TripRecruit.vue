<template>
  <div class="min-h-screen bg-gradient-to-b from-blue-50 to-white">
    <div class="py-8 px-6 max-w-7xl mx-auto">
      <h1 class="text-3xl font-bold mb-2">여행 인원 모집</h1>
      <p class="text-gray-500 mb-8">함께 여행할 동행자를 모집하거나 참여해보세요</p>

      <div class="mb-6">
        <div class="border-b mb-6">
          <div class="flex">
            <button
              v-for="tab in tabs"
              :key="tab.value"
              :class="[
                'px-4 py-2',
                activeTab === tab.value
                  ? 'border-b-2 border-blue-600 text-blue-600'
                  : 'text-gray-500',
              ]"
              @click="activeTab = tab.value"
            >
              {{ tab.label }}
            </button>
          </div>
        </div>

        <!-- 모집글 보기 탭 -->
        <div v-if="activeTab === 'browse'">
          <trip-recruit-list @open-trip-details="openTripDetails" />
        </div>

        <!-- 모집글 작성 탭 -->
        <div v-if="activeTab === 'create'">
          <trip-recruit-form />
        </div>
      </div>
    </div>

    <!-- 여행 모집 상세 모달 -->
    <trip-detail-modal
      :show="showTripDetails"
      :trip="selectedTrip"
      @close="showTripDetails = false"
    />
  </div>
</template>

<script setup>
import { ref } from 'vue'
import TripRecruitList from '@/components/trip/TripRecruitList.vue'
import TripRecruitForm from '@/components/trip/TripRecruitForm.vue'
import TripDetailModal from '@/components/trip/TripDetailModal.vue'

// 상태 관리
const activeTab = ref('browse')
const showTripDetails = ref(false)
const selectedTrip = ref({
  id: 0,
  title: '',
  host: '',
  dates: '',
  location: '',
  tags: [],
  memberCount: 0,
  maxMembers: 0,
  description: '',
  imageUrl: '',
  schedule: [],
})

// 탭 정의
const tabs = [
  { value: 'browse', label: '모집글 보기' },
  { value: 'create', label: '모집글 작성' },
]

// 여행 상세 정보 열기
const openTripDetails = (trip) => {
  selectedTrip.value = trip
  showTripDetails.value = true
}
</script>
