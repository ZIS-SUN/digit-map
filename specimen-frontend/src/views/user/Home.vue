<template>
  <div class="home-container">
    <!-- 顶部导航栏 -->
    <el-header class="header">
      <div class="logo">🌿 植物标本库</div>
      <el-menu mode="horizontal" :default-active="activeMenu" router>
        <el-menu-item index="/home">首页</el-menu-item>
        <el-menu-item index="/specimens">标本列表</el-menu-item>
        <el-menu-item index="/map">地图分布</el-menu-item>
        <el-menu-item index="/collection">采集记录</el-menu-item>
        <el-menu-item index="/announcement">公告中心</el-menu-item>
        <el-menu-item index="/stats">数据统计</el-menu-item>
      </el-menu>
      <div class="user-info">
        <el-dropdown>
          <span class="user-name">
            <el-icon><User /></el-icon>
            {{ userStore.username }}
          </span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item @click="toProfile">个人中心</el-dropdown-item>
              <el-dropdown-item @click="handleLogout" divided>退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </el-header>

    <!-- 主要内容区 -->
    <div class="main-content">
      <!-- 公告区 -->
      <el-card class="announcement-card" v-if="announcements.length > 0">
        <template #header>
          <div class="card-header">
            <span>📢 系统公告</span>
            <el-link type="primary" @click="toAnnouncement">查看更多</el-link>
          </div>
        </template>
        <el-carousel height="80px" direction="vertical" :autoplay="true">
          <el-carousel-item v-for="item in announcements" :key="item.id">
            <div class="announcement-item" @click="toAnnouncementDetail(item.id)">
              <span>{{ item.title }}</span>
              <span class="time">{{ formatDate(item.publishTime) }}</span>
            </div>
          </el-carousel-item>
        </el-carousel>
      </el-card>

      <!-- 热门标本展示 -->
      <el-card class="specimens-card">
        <template #header>
          <div class="card-header">
            <span>🔥 热门标本</span>
            <el-link type="primary" @click="toSpecimens">查看全部</el-link>
          </div>
        </template>
        <el-row :gutter="20" v-loading="loading">
          <el-col :span="6" v-for="specimen in hotSpecimens" :key="specimen.id">
            <specimen-card :specimen="specimen" @click="toSpecimenDetail(specimen.id)" />
          </el-col>
          <el-empty v-if="!loading && hotSpecimens.length === 0" description="暂无数据" />
        </el-row>
      </el-card>

      <!-- 快捷入口 -->
      <el-card class="quick-entry-card">
        <template #header>
          <span>⚡ 快捷入口</span>
        </template>
        <div class="quick-entries">
          <div class="entry-item" @click="$router.push('/map')">
            <el-icon :size="40" color="#4CAF50"><Location /></el-icon>
            <span>地图分布</span>
          </div>
          <div class="entry-item" @click="$router.push('/collection')">
            <el-icon :size="40" color="#4CAF50"><Plus /></el-icon>
            <span>采集记录</span>
          </div>
          <div class="entry-item" @click="$router.push('/stats')">
            <el-icon :size="40" color="#4CAF50"><DataAnalysis /></el-icon>
            <span>数据统计</span>
          </div>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/store/user'
import { getAnnouncements } from '@/api/announcement'
import { getSpecimens } from '@/api/specimen'
import SpecimenCard from '@/components/SpecimenCard.vue'
import { User, Location, Plus, DataAnalysis } from '@element-plus/icons-vue'

const router = useRouter()
const userStore = useUserStore()

const activeMenu = ref('/home')
const announcements = ref([])
const hotSpecimens = ref([])
const loading = ref(false)

onMounted(() => {
  fetchData()
})

const fetchData = async () => {
  loading.value = true
  try {
    // 获取公告
    const announcementRes = await getAnnouncements({ page: 1, pageSize: 5 })
    if (announcementRes.code === 200) {
      announcements.value = announcementRes.data.list
    }

    // 获取热门标本
    const specimenRes = await getSpecimens({ hot: true, page: 1, pageSize: 8 })
    if (specimenRes.code === 200) {
      hotSpecimens.value = specimenRes.data.list
    }
  } finally {
    loading.value = false
  }
}

const formatDate = (dateStr) => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  return date.toLocaleDateString('zh-CN')
}

const toProfile = () => {
  router.push('/profile')
}

const toAnnouncement = () => {
  router.push('/announcement')
}

const toAnnouncementDetail = (id) => {
  router.push(`/announcement?id=${id}`)
}

const toSpecimens = () => {
  router.push('/specimens')
}

const toSpecimenDetail = (id) => {
  router.push(`/specimen/${id}`)
}

const handleLogout = () => {
  userStore.logout()
}
</script>

<style scoped lang="scss">
.home-container {
  min-height: 100vh;
  background: #f5f5f5;
}

.header {
  display: flex;
  align-items: center;
  background: white;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  padding: 0 20px;
  height: 60px;
}

.logo {
  font-size: 20px;
  font-weight: bold;
  color: #4CAF50;
  margin-right: 30px;
}

.el-menu {
  flex: 1;
  border: none;
}

.user-info {
  .user-name {
    display: flex;
    align-items: center;
    cursor: pointer;
    color: #303133;
    
    .el-icon {
      margin-right: 5px;
    }
  }
}

.main-content {
  max-width: 1200px;
  margin: 20px auto;
  padding: 0 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.announcement-card {
  margin-bottom: 20px;
  
  .announcement-item {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 10px;
    cursor: pointer;
    
    &:hover {
      background: #f5f5f5;
    }
    
    .time {
      color: #909399;
      font-size: 12px;
    }
  }
}

.specimens-card {
  margin-bottom: 20px;
}

.quick-entry-card {
  .quick-entries {
    display: flex;
    justify-content: space-around;
    
    .entry-item {
      display: flex;
      flex-direction: column;
      align-items: center;
      padding: 20px;
      cursor: pointer;
      transition: all 0.3s;
      
      &:hover {
        transform: translateY(-5px);
      }
      
      span {
        margin-top: 10px;
        color: #606266;
      }
    }
  }
}
</style>

