<template>
  <div class="profile-container">
    <el-header class="header">
      <div class="logo" @click="$router.push('/home')">🌿 植物标本库</div>
      <el-button @click="$router.back()" type="primary" plain>返回</el-button>
    </el-header>

    <div class="main-content">
      <el-row :gutter="20">
        <!-- 左侧个人信息 -->
        <el-col :span="8">
          <el-card class="user-card">
            <div class="user-avatar">
              <el-avatar :size="100" :icon="UserFilled" />
            </div>
            <div class="user-name">{{ userInfo.username }}</div>
            <div class="user-email">{{ userInfo.email }}</div>
            <el-divider />
            <div class="user-stats">
              <div class="stat-item">
                <div class="stat-value">{{ userInfo.collectionCount || 0 }}</div>
                <div class="stat-label">采集记录</div>
              </div>
            </div>
          </el-card>
        </el-col>

        <!-- 右侧信息编辑 -->
        <el-col :span="16">
          <!-- 基本信息 -->
          <el-card class="info-card">
            <template #header>
              <span>基本信息</span>
            </template>
            <el-form :model="form" label-width="100px">
              <el-form-item label="用户名">
                <el-input v-model="form.username" disabled />
              </el-form-item>
              <el-form-item label="邮箱">
                <el-input v-model="form.email" />
              </el-form-item>
              <el-form-item label="手机号">
                <el-input v-model="form.phone" />
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="updateInfo" :loading="loading">
                  保存修改
                </el-button>
              </el-form-item>
            </el-form>
          </el-card>

          <!-- 修改密码 -->
          <el-card class="password-card" style="margin-top: 20px;">
            <template #header>
              <span>修改密码</span>
            </template>
            <el-form :model="passwordForm" :rules="passwordRules" ref="passwordFormRef" label-width="100px">
              <el-form-item label="原密码" prop="oldPassword">
                <el-input v-model="passwordForm.oldPassword" type="password" />
              </el-form-item>
              <el-form-item label="新密码" prop="newPassword">
                <el-input v-model="passwordForm.newPassword" type="password" />
              </el-form-item>
              <el-form-item label="确认密码" prop="confirmPassword">
                <el-input v-model="passwordForm.confirmPassword" type="password" />
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="changePassword" :loading="passwordLoading">
                  修改密码
                </el-button>
              </el-form-item>
            </el-form>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useUserStore } from '@/store/user'
import { getUserInfo, updateUserInfo, changePassword as changePasswordApi } from '@/api/auth'
import { ElMessage } from 'element-plus'
import { UserFilled } from '@element-plus/icons-vue'

const userStore = useUserStore()

const userInfo = ref({})
const loading = ref(false)
const passwordLoading = ref(false)
const passwordFormRef = ref(null)

const form = reactive({
  username: '',
  email: '',
  phone: ''
})

const passwordForm = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

const validateConfirmPassword = (rule, value, callback) => {
  if (value !== passwordForm.newPassword) {
    callback(new Error('两次输入密码不一致'))
  } else {
    callback()
  }
}

const passwordRules = {
  oldPassword: [{ required: true, message: '请输入原密码', trigger: 'blur' }],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, message: '密码长度至少6位', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    { validator: validateConfirmPassword, trigger: 'blur' }
  ]
}

onMounted(() => {
  fetchUserInfo()
})

const fetchUserInfo = async () => {
  try {
    const res = await getUserInfo()
    if (res.code === 200) {
      userInfo.value = res.data
      form.username = res.data.username
      form.email = res.data.email
      form.phone = res.data.phone
    }
  } catch (error) {
    // 错误已在拦截器中处理
  }
}

const updateInfo = async () => {
  loading.value = true
  try {
    const res = await updateUserInfo({
      email: form.email,
      phone: form.phone
    })
    if (res.code === 200) {
      ElMessage.success('保存成功')
      await userStore.fetchUserInfo()
    }
  } finally {
    loading.value = false
  }
}

const changePassword = async () => {
  if (!passwordFormRef.value) return
  
  await passwordFormRef.value.validate(async (valid) => {
    if (valid) {
      passwordLoading.value = true
      try {
        const res = await changePasswordApi({
          oldPassword: passwordForm.oldPassword,
          newPassword: passwordForm.newPassword
        })
        if (res.code === 200) {
          ElMessage.success('密码修改成功，请重新登录')
          setTimeout(() => {
            userStore.logout()
          }, 1500)
        }
      } finally {
        passwordLoading.value = false
      }
    }
  })
}
</script>

<style scoped lang="scss">
.profile-container {
  min-height: 100vh;
  background: #f5f5f5;
}

.header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: white;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  padding: 0 20px;
  height: 60px;
}

.logo {
  font-size: 20px;
  font-weight: bold;
  color: #4CAF50;
  cursor: pointer;
}

.main-content {
  max-width: 1200px;
  margin: 20px auto;
  padding: 0 20px;
}

.user-card {
  text-align: center;
  
  .user-avatar {
    margin: 20px 0;
  }
  
  .user-name {
    font-size: 20px;
    font-weight: bold;
    color: #303133;
    margin-bottom: 10px;
  }
  
  .user-email {
    color: #909399;
    margin-bottom: 20px;
  }
  
  .user-stats {
    display: flex;
    justify-content: center;
    
    .stat-item {
      padding: 0 30px;
      
      .stat-value {
        font-size: 24px;
        font-weight: bold;
        color: #4CAF50;
      }
      
      .stat-label {
        font-size: 14px;
        color: #909399;
        margin-top: 5px;
      }
    }
  }
}
</style>

