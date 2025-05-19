import { createRouter, createWebHistory } from 'vue-router'
import MainPage from '@/components/MainPage.vue'
import TripPlan from '@/components/trip/TripPlan.vue'
import TripRecruit from '@/components/trip/TripRecruit.vue'
import TripMatch from '@/components/trip/TripMatch.vue'
import TripShare from '@/components/trip/TripShare.vue'
import UserLogin from '@/components/user/UserLogin.vue'
import UserRegist from '@/components/user/UserRegist.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'main',
      component: MainPage, // eager-loading
    },
    {
        path: "/plan",
        name: "plan",
        component: TripPlan
    },
    {
        path: "/recruit",
        name: "recruit",
        component: TripRecruit
    },
    {
        path: "/match",
        name: "match",
        component: TripMatch
    },
    {
        path: "/share",
        name: "share",
        component: TripShare
    },
    {
        path: "/login",
        name: "login",
        component: UserLogin
    },
    {
        path: "/regist",
        name: "regist",
        component: UserRegist
    },
  ],
})

export default router