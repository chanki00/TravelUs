import './assets/main.css'

import { createApp } from 'vue'
import {createPinia} from 'pinia'
import App from './App.vue'
import router from "./router"
import { useKakao } from 'vue3-kakao-maps/@utils';

useKakao(import.meta.env.VITE_KAKAO_MAP_KEY);
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate'

const app = createApp(App)
const pinia = createPinia()

pinia.use(piniaPluginPersistedstate)

app.use(router)
app.use(pinia)

app.mount('#app')
