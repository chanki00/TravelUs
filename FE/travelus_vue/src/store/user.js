import {ref, computed} from "vue"
import {defineStore} from "pinia"
import { userAi } from "@/axios";
import { jwtDecode } from 'jwt-decode'

export const useUserStore = defineStore('user', () => {
    const _isLoggedIn = ref(false);
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
            userPw: password
        })
        // console.log(response)
        // console.log(response.data.data)
        
        const {accessToken, refreshToken, user} = response.data;
        _tokens.value = {accessToken, refreshToken}

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
        _loginUser.value = {...editUser.value}
    }

    const deleteUser = async () => {
        try {
            await userAi.delete(`/api/v1/user/${loginUser.value.id}`)
            logout()
        } catch (e) {
            console.error("회원 탈퇴 실패:", e)
            alert("회원 탈퇴에 실패했습니다.")
        }
    }

    const getUserList = async () => {
        try {
            const list = await userAi.get("/api/v1/user")

            const users = list.data

            const updatedUsers = await Promise.all(
                users.map(async (user) => {
                    const personality = await getUserTags(user.id, "personal")
                    const preference = await getUserTags(user.id, "trip")

                    return {
                    ...user,
                    personality,
                    preference,
                    }
                })
            )

            _userList.value = updatedUsers
        }
        catch (e) {
            console.log("사용자 조회 실패")
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


    return {isLoggedIn, loginUser, _isLoggedIn, _loginUser, login, logout, tokens, update, deleteUser, getUserList, userList}
},
{persist : {storage: sessionStorage}}
)