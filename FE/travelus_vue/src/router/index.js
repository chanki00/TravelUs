import { createRouter, createWebHistory } from 'vue-router'
import UserLogin from '@/views/user/UserLogin.vue'
import UserRegist from '@/views/user/UserRegist.vue'
import UserProfile from '@/views/user/UserProfile.vue'
import MainPage from '@/views/MainPage.vue'
import TripAiPlan from '@/views/trip/TripAiPlan.vue'
import TripHumanPlan from '@/views/trip/TripHumanPlan.vue'
import TripRecruit from '@/views/trip/TripRecruit.vue'
import TripCompanion from '@/views/trip/TripCompanion.vue'
import TripShare from '@/views/trip/TripShare.vue'
import TripPlanSelect from '@/views/trip/TripPlanSelect.vue'
import AdminDashboard from '@/views/admin/AdminDashboard.vue'
import TripPlan from '@/views/trip/TripPlan.vue'
import TripBoard from '@/views/trip/TripBoard.vue'
import TripDetail from '@/views/trip/TripDetail.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/login',
      name: 'login',
      component: UserLogin,
    },
    {
      path: '/regist',
      name: 'regist',
      component: UserRegist,
    },
    {
      path: '/profile',
      name: 'profile',
      component: UserProfile,
    },
    {
      path: '/',
      name: 'main',
      component: MainPage,
    },
    {
      path: '/plan',
      name: 'planSelect',
      component: TripPlanSelect,
    },
    {
      path: '/aiplan',
      name: 'aiplan',
      component: TripAiPlan,
    },
    {
      path: '/humanplan',
      name: 'humanplan',
      component: TripHumanPlan,
    },
    {
      path: '/recruit',
      name: 'recruit',
      component: TripRecruit,
    },
    {
      path: '/companion',
      name: 'companion',
      component: TripCompanion,
    },
    {
      path: '/share',
      name: 'share',
      // component: TripShare
      component: TripBoard,
    },
    {
      path: '/admin',
      name: 'admin',
      component: AdminDashboard,
    },
      {
    path: "/tripplan",
    name: "TripPlan",
    component: TripPlan,
  },
    {
      path: '/planresult/:planId',
      name: 'PlanResult',
      component: TripPlan,
    },
    {
      path: '/tripdetail/:id',
      name: 'TripDetail',
      component: TripDetail,
    },
  ],
})

export default router
