import './assets/main.css'

import { createApp } from 'vue'
import App from './App.vue'
import router from "./router"
import { useKakao } from 'vue3-kakao-maps/@utils';

useKakao(import.meta.env.VITE_KAKAO_MAP_KEY);

const app = createApp(App)
app.use(router)

app.mount('#app')
