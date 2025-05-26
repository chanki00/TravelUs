import { ref, computed } from 'vue'
import { defineStore } from 'pinia'
import { userAi } from '@/axios'
import { jwtDecode } from 'jwt-decode'

export const useUserStore = defineStore(
  'user',
  () => {
    const _isLoggedIn = ref(false)
    const _loginUser = ref({})
    const _userList = ref([])

    const isLoggedIn = computed(() => _isLoggedIn.value)
    const loginUser = computed(() => _loginUser.value)
    const userList = computed(() => _userList.value)

    const _tokens = ref({})
    const tokens = computed(() => _tokens.value)

    const login = async (id, password) => {
      const response = await userAi.post('/api/v1/user/login', {
        userId: id,
        userPw: password,
      })
      // console.log(response)
      // console.log(response.data.data)

      const { accessToken, refreshToken, user } = response.data
      _tokens.value = { accessToken, refreshToken }

      // const decoded = jwtDecode(accessToken)
      _loginUser.value = user
      _isLoggedIn.value = true

      console.log(isLoggedIn.value)
      console.log(loginUser.value.name)
    }

    const logout = () => {
      _isLoggedIn.value = false
      _loginUser.value = {}
      _tokens.value = {}
    }

    const update = (editUser) => {
      _loginUser.value = { ...editUser.value }
    }

    const deleteUser = async () => {
      try {
        await userAi.delete(`/api/v1/user/${loginUser.value.id}`)
        logout()
      } catch (e) {
        console.error('회원 탈퇴 실패:', e)
        alert('회원 탈퇴에 실패했습니다.')
      }
    }

    const getUserList = async () => {
  try {
    const list = await userAi.get('/api/v1/user')
    const users = list.data

    const updatedUsers = await Promise.all(
      users.map(async (user) => {
        const personality = await getUserTags(user.id, 'personal')
        const preference = await getUserTags(user.id, 'trip')

        // ✅ 성별 변환
        const convertGender = (code) => {
          switch (code) {
            case 'M':
              return '남성'
            case 'F':
              return '여성'
            default:
              return '기타'
          }
        }

        // ✅ 나이대 변환
        const convertAgeGroup = (age) => {
          switch (age) {
            case '50대':
              return '50대 이상'
            case '40대':
              return '40대'
            case '30대':
              return '30대'
            case '20대':
              return '20대'
            default:
              return '기타'
          }
        }


        // ✅ 주소 변환
        const convertAddress = (addr) => {
          switch (addr) {
            case '서울':
            case '부산':
            case '인천':
            case '대구':
              return addr
            default:
              return '기타'
          }
        }


        return {
          ...user,
          personality,
          preference,
          gender: convertGender(user.gender),
          age: convertAgeGroup(user.age),
          address: convertAddress(user.address),
        }
      }),
    )

    _userList.value = updatedUsers
  } catch (e) {
    console.log('사용자 조회 실패')
  }
}


    const getUserTags = async (userId, type) => {
      try {
        const res = await userAi.get(`/api/v1/tag/user/${type}/${userId}`)
        return res.data
      } catch (e) {
        console.error(`${type} 태그 가져오기 실패`, e)
        return []
      }
    }

    const setAccessToken = (token) => {
      _tokens.value.accessToken = token
    }

    return {
      isLoggedIn,
      loginUser,
      _isLoggedIn,
      _loginUser,
      login,
      logout,
      tokens,
      update,
      deleteUser,
      getUserList,
      userList,
      setAccessToken,
    }
  },
  { persist: { storage: sessionStorage } },
)
