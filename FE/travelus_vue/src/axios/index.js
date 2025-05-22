import axios from "axios"
import { useUserStore } from "@/store/user"

const userAi = axios.create({
    baseURL: 'http://localhost:8080',
    headers: {
        'Content-Type': 'application/json'
    },
    withCredentials: true
})

userAi.interceptors.request.use(
    async (config) => {
        console.log("요청 발신: " , config.method, config.url, config.data)

        const userStore = useUserStore();
        if (userStore.tokens?.accessToken) {
            config.headers['Authorization'] = `Bearer ${userStore.tokens.accessToken}`
        }

        return config;
    }
)

userAi.interceptors.response.use(
  (response) => {
    console.log('[응답 수신 1]: ', response.status, response.data)
    return response
  },
  async (error) => {
    console.log('[오류 수신 1]: ', error)

    return Promise.reject(error)
  },
)

export {userAi}