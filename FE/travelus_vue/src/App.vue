<template>

  <div class="font-bmjua">
    <AppHeader />
    <!-- <UserLogin /> -->
    <!-- <UserRegist /> -->
    <!-- <MainPage /> -->
    <!-- <TripShare /> -->
    <!-- <TripAiPlan/> -->
    <RouterView />
  </div>
  <!-- <Index/> -->
</template>

<script setup>
import {RouterView} from "vue-router"
import AppHeader from './components/fragments/AppHeader.vue'
// import TripAiPlan from './views/trip/TripAiPlan.vue'
// import UserLogin from './components/user/UserLogin.vue'
// import UserRegist from './components/user/UserRegist.vue'
// import MainPage from './components/MainPage.vue'
// import TripShare from './components/trip/TripShare.vue'
</script>

<style scoped>
@import './assets/main.css';
</style>
