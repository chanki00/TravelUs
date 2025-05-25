<template>
  <div class="min-h-screen bg-gradient-to-b from-blue-50 to-white">
    <!-- <Navbar /> -->
    <Hero />
    <PopularTrips />

    <!-- ✅ 채팅창 삽입 (테스트용) -->
    <div class="fixed bottom-4 right-4 w-[300px] bg-white border rounded shadow-lg z-50">
      <TripChat :chatroom-id="1" />
    </div>

    <!-- <Footer /> -->
    <footer class="py-12 px-6 border-t">
      <div class="max-w-7xl mx-auto text-center">
        <p class="text-gray-500 text-sm">
          &copy; {{ new Date().getFullYear() }} Travelus. All rights reserved.
        </p>
      </div>
    </footer>
  </div>
</template>

<script setup>
import Hero from '@/components/common/Hero.vue'
import PopularTrips from '@/components/common/PopularTrips.vue'
// import Footer from '@/components/common/Footers.vue';
// import TripChat from '@/components/trip/TripChat.vue' // ✅ 여기에 import
</script>

<style scoped>
/* 필요한 스타일 추가 */
</style>
