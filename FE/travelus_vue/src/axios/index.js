import axios from 'axios'
import { useUserStore } from '@/store/user'

const userAi = axios.create({
  baseURL: 'http://localhost:8080',
  headers: {
    'Content-Type': 'application/json',
  },
  withCredentials: true,
})

// 요청 인터셉터
userAi.interceptors.request.use(async (config) => {
  const userStore = useUserStore()
  console.log('요청 발신: ', config.method, config.url, config.data)
  console.log('토큰있니', userStore.tokens.accessToken)

  if (userStore.tokens?.accessToken) {
    config.headers['accessToken'] = `Bearer ${userStore.tokens.accessToken}`
  }

  return config
})

// 응답 인터셉터
userAi.interceptors.response.use(
  (response) => {
    console.log('[응답 수신 1]: ', response.status, response.data)
    return response
  },
  async (error) => {
    const originalRequest = error.config
    const userStore = useUserStore()

    // accessToken 만료 시 재발급 시도
    if (error.response?.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true

      try {
        const refreshRes = await axios.post('http://localhost:8080/api/auth/refresh', null, {
          headers: {
            refreshToken: userStore.tokens.refreshToken,
          },
          withCredentials: true,
        })

        const newAccessToken = refreshRes.data.accessToken
        console.log('재발급된 accessToken:', newAccessToken)

        // Pinia 저장소에 새 토큰 저장
        userStore.setAccessToken(newAccessToken)

        // 헤더에 새 accessToken 설정 후 재요청
        originalRequest.headers['accessToken'] = `Bearer ${newAccessToken}`
        return userAi(originalRequest)
      } catch (refreshError) {
        console.error('토큰 재발급 실패:', refreshError)
        userStore.logout()
        return Promise.reject(refreshError)
      }
    }

    return Promise.reject(error)
  },
)

export { userAi }
