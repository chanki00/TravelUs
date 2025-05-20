<template>
  <section class="py-16 px-6 md:px-12">
    <div class="max-w-7xl mx-auto">
      <h2 class="text-2xl md:text-3xl font-bold mb-2">인기 여행 일정</h2>
      <p class="text-gray-500 mb-8">다른 여행자들의 인기 일정을 둘러보세요</p>

      <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
        <div
          v-for="trip in popularTrips"
          :key="trip.id"
          class="animate-fade-in"
          :style="{ animationDelay: `${parseInt(trip.id) * 150}ms` }"
        >
          <trip-card v-bind="trip" />
        </div>
      </div>
    </div>
  </section>
</template>

<script setup>
// 여기에 script 로직이 추가될 예정
// import TripCard from '@/components/trip/TripCard.vue';
</script>

<style scoped>
/* 필요한 스타일 추가 */
</style>