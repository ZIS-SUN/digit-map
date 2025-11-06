<template>
  <div class="map-container">
    <el-header class="header">
      <div class="logo" @click="$router.push('/home')">🌿 植物标本库</div>
      <el-menu mode="horizontal" default-active="/map" router>
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
              <el-dropdown-item @click="$router.push('/profile')">个人中心</el-dropdown-item>
              <el-dropdown-item @click="userStore.logout()" divided>退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </el-header>

    <div class="map-wrapper" v-loading="loading">
      <map-viewer :markers="markers" :center="mapCenter" :zoom="mapZoom" />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useUserStore } from '@/store/user'
import { useMapStore } from '@/store/map'
import MapViewer from '@/components/MapViewer.vue'
import { User } from '@element-plus/icons-vue'

const userStore = useUserStore()
const mapStore = useMapStore()

const markers = ref([])
const mapCenter = ref([116.397428, 39.90923])
const mapZoom = ref(13)
const loading = ref(false)

onMounted(() => {
  fetchMarkers()
})

const fetchMarkers = async () => {
  loading.value = true
  try {
    await mapStore.fetchMarkers()
    markers.value = mapStore.markers
  } finally {
    loading.value = false
  }
}
</script>

<style scoped lang="scss">
.map-container {
  display: flex;
  flex-direction: column;
  height: 100vh;
}

.header {
  display: flex;
  align-items: center;
  background: white;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  padding: 0 20px;
  height: 60px;
  flex-shrink: 0;
}

.logo {
  font-size: 20px;
  font-weight: bold;
  color: #4CAF50;
  margin-right: 30px;
  cursor: pointer;
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
  }
}

.map-wrapper {
  flex: 1;
  position: relative;
}
</style>

