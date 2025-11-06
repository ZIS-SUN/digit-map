import { defineStore } from 'pinia'
import { login, adminLogin, getUserInfo } from '@/api/auth'
import { ElMessage } from 'element-plus'
import router from '@/router'

export const useUserStore = defineStore('user', {
  state: () => ({
    token: localStorage.getItem('token') || '',
    userInfo: JSON.parse(localStorage.getItem('userInfo') || 'null'),
    isAdmin: localStorage.getItem('isAdmin') === 'true'
  }),

  getters: {
    isLoggedIn: (state) => !!state.token,
    username: (state) => state.userInfo?.username || ''
  },

  actions: {
    // 用户登录
    async userLogin(loginForm) {
      try {
        const res = await login(loginForm)
        if (res.code === 200) {
          this.token = res.data.token
          this.userInfo = res.data.userInfo
          this.isAdmin = false
          
          localStorage.setItem('token', this.token)
          localStorage.setItem('userInfo', JSON.stringify(this.userInfo))
          localStorage.setItem('isAdmin', 'false')
          
          ElMessage.success('登录成功')
          router.push('/home')
          return true
        }
      } catch (error) {
        ElMessage.error('登录失败')
        return false
      }
    },

    // 管理员登录
    async adminUserLogin(loginForm) {
      try {
        const res = await adminLogin(loginForm)
        if (res.code === 200) {
          this.token = res.data.token
          this.userInfo = res.data.userInfo
          this.isAdmin = true
          
          localStorage.setItem('token', this.token)
          localStorage.setItem('userInfo', JSON.stringify(this.userInfo))
          localStorage.setItem('isAdmin', 'true')
          
          ElMessage.success('登录成功')
          router.push('/admin/dashboard')
          return true
        }
      } catch (error) {
        ElMessage.error('登录失败')
        return false
      }
    },

    // 获取用户信息
    async fetchUserInfo() {
      try {
        const res = await getUserInfo()
        if (res.code === 200) {
          this.userInfo = res.data
          localStorage.setItem('userInfo', JSON.stringify(this.userInfo))
          return true
        }
      } catch (error) {
        return false
      }
    },

    // 登出
    logout() {
      this.token = ''
      this.userInfo = null
      this.isAdmin = false
      
      localStorage.removeItem('token')
      localStorage.removeItem('userInfo')
      localStorage.removeItem('isAdmin')
      
      ElMessage.success('已退出登录')
      router.push('/login')
    }
  }
})

