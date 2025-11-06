<template>
  <div class="login-container">
    <div class="login-box">
      <h2 class="title">植物标本库管理系统</h2>
      <el-form :model="loginForm" :rules="rules" ref="formRef" class="login-form">
        <el-form-item prop="username">
          <el-input 
            v-model="loginForm.username" 
            placeholder="请输入用户名"
            prefix-icon="User"
          />
        </el-form-item>
        <el-form-item prop="password">
          <el-input 
            v-model="loginForm.password" 
            type="password"
            placeholder="请输入密码"
            prefix-icon="Lock"
            @keyup.enter="handleLogin"
          />
        </el-form-item>
        <el-form-item>
          <el-button 
            type="primary" 
            class="login-btn" 
            @click="handleLogin"
            :loading="loading"
          >
            登录
          </el-button>
        </el-form-item>
        <div class="links">
          <router-link to="/register">没有账号？立即注册</router-link>
          <router-link to="/admin/login">管理员登录</router-link>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useUserStore } from '@/store/user'

const userStore = useUserStore()
const formRef = ref(null)
const loading = ref(false)

const loginForm = reactive({
  username: '',
  password: ''
})

const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度至少6位', trigger: 'blur' }
  ]
}

const handleLogin = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        await userStore.userLogin(loginForm)
      } finally {
        loading.value = false
      }
    }
  })
}
</script>

<style scoped lang="scss">
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background: linear-gradient(135deg, #4CAF50 0%, #2E7D32 100%);
}

.login-box {
  width: 400px;
  padding: 40px;
  background: white;
  border-radius: 10px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
}

.title {
  text-align: center;
  color: #303133;
  margin-bottom: 30px;
  font-size: 24px;
}

.login-form {
  .login-btn {
    width: 100%;
  }
}

.links {
  display: flex;
  justify-content: space-between;
  margin-top: 10px;
  
  a {
    color: #4CAF50;
    text-decoration: none;
    font-size: 14px;
    
    &:hover {
      color: #2E7D32;
    }
  }
}
</style>

