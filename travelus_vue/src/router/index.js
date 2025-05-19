import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    name: 'Index',
    component: () => import('@/views/index.vue')
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue')
  },
  {
    path: '/regist',
    name: 'Regist',
    component: () => import('@/views/Regist.vue')
  },
  {
    path: '/profile',
    name: 'Profile',
    component: () => import('@/views/Profile.vue')
  },
//   {
//     path: '/planner',
//     name: 'PlannerSelection',
//     component: () => import('@/views/PlannerSelection.vue')
//   },
//   {
//     path: '/travel-planner',
//     name: 'TravelPlanner',
//     component: () => import('@/views/TravelPlanner.vue')
//   },
  {
    path: '/ai-planner',
    name: 'AiPlanner',
    component: () => import('@/views/AiPlanner.vue')
  },
//   {
//     path: '/travel-planner/result',
//     name: 'TravelPlanResult',
//     component: () => import('@/views/TravelPlanResult.vue')
//   },
//   {
//     path: '/ai-planner/result',
//     name: 'AiPlanResult',
//     component: () => import('@/views/TravelPlanResult.vue')
//   },
  {
    path: '/companion',
    name: 'Companion',
    component: () => import('@/views/CompanionList.vue')
  },
//   {
//     path: '/trip-recruitment',
//     name: 'TripRecruitment',
//     component: () => import('@/views/TripRecruitment.vue')
//   },
//   {
//     path: '/trip-board',
//     name: 'TripBoard',
//     component: () => import('@/views/TripBoard.vue')
//   },
//   {
//     path: '/trip/:id',
//     name: 'TripDetail',
//     component: () => import('@/views/TripDetail.vue')
//   },
//   {
//     path: '/profile',
//     name: 'Profile',
//     component: () => import('@/views/Profile.vue')
//   },
//   {
//     path: '/admin',
//     name: 'AdminDashboard',
//     component: () => import('@/views/AdminDashboard.vue')
//   },
//   {
//     path: '/:pathMatch(.*)*',
//     name: 'NotFound',
//     component: () => import('@/views/NotFound.vue')
//   }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router