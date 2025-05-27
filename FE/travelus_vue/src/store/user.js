import { ref, computed } from 'vue'
import { defineStore } from 'pinia'
import { userAi } from '@/axios'
import { jwtDecode } from 'jwt-decode'
import Cookies from 'js-cookie'

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

      const { accessToken, refreshToken, user } = response.data
      _tokens.value = { accessToken, refreshToken }

      _loginUser.value = user
      _isLoggedIn.value = true

      console.log(isLoggedIn.value)
      console.log(loginUser.value.name)
    }

    const fetchUser = async () => {
      try {
        const res = await userAi.get('/api/v1/user/me', {
          withCredentials: true, // ← 쿠키 전송 허용
        });

        const user = res.data;

        _loginUser.value = user;
        _isLoggedIn.value = true;

        console.log('유저 정보:', user);
        console.log('로그인 여부:', _isLoggedIn.value);
        console.log('유저 이름:', _loginUser.value.name);
      } catch (e) {
        console.warn('유저 정보를 가져오지 못했습니다:', e);
        _loginUser.value = {};
        _isLoggedIn.value = false;
      }
    };


    const logout = async () => {
      try {
        const refreshToken = _tokens.value.refreshToken;
        await userAi.post('/api/auth/logout', {}, {
          headers: {
            'refreshtoken': refreshToken
          },
          withCredentials: true
        });

        // 프론트 상태 정리
        _isLoggedIn.value = false;
        _loginUser.value = {};
        _tokens.value = {};

        // (HttpOnly가 아니라면) accessToken 수동 삭제
        Cookies.remove('Authorization', { path: '/' });

        console.log('로그아웃 완료');
      } catch (e) {
        console.error('로그아웃 실패:', e);
      }
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
      fetchUser,
    }
  },
  { persist: { storage: sessionStorage } },
)
