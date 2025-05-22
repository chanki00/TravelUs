import {ref, computed} from "vue"
import {defineStore} from "pinia"
import { userAi } from "@/axios";
import { jwtDecode } from 'jwt-decode'

export const useUserStore = defineStore('user', () => {
    const _isLoggedIn = ref(false);
    const _loginUser = ref({})

    const isLoggedIn = computed(() => _isLoggedIn.value)
    const loginUser = computed(() => _loginUser.value)

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


    return {isLoggedIn, loginUser, _isLoggedIn, _loginUser, login, logout, tokens}
},
{persist : {storage: sessionStorage}}
)