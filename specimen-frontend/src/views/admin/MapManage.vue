<template>
  <div class="map-manage-container">
    <el-container>
      <el-aside width="200px">
        <div class="logo">管理后台</div>
        <el-menu 
          :default-active="$route.path" 
          router
          background-color="#304156"
          text-color="#fff"
          active-text-color="#409EFF"
        >
          <el-menu-item index="/admin/dashboard">
            <el-icon><DataAnalysis /></el-icon>
            <span>数据仪表盘</span>
          </el-menu-item>
          <el-menu-item index="/admin/specimens">
            <el-icon><Tickets /></el-icon>
            <span>标本管理</span>
          </el-menu-item>
          <el-menu-item index="/admin/map">
            <el-icon><Location /></el-icon>
            <span>地图标注</span>
          </el-menu-item>
          <el-menu-item index="/admin/users">
            <el-icon><User /></el-icon>
            <span>用户管理</span>
          </el-menu-item>
          <el-menu-item index="/admin/announcements">
            <el-icon><Bell /></el-icon>
            <span>公告管理</span>
          </el-menu-item>
          <el-menu-item index="/admin/collections">
            <el-icon><Document /></el-icon>
            <span>采集记录</span>
          </el-menu-item>
          <el-menu-item index="/admin/export">
            <el-icon><Download /></el-icon>
            <span>数据导出</span>
          </el-menu-item>
        </el-menu>
      </el-aside>

      <el-container>
        <el-header class="header">
          <span class="title">地图标注管理</span>
          <el-dropdown>
            <span class="user-info">
              <el-icon><User /></el-icon>
              {{ userStore.username }}
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="userStore.logout()">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </el-header>

        <el-main>
          <el-card style="height: calc(100vh - 140px);">
            <div class="map-controls">
              <el-button type="primary" @click="fetchMarkers">
                <el-icon><Refresh /></el-icon>
                刷新地图
              </el-button>
              <el-text type="info">提示：拖动标注点可调整位置</el-text>
            </div>
            <div class="map-wrapper">
              <map-viewer 
                :markers="markers" 
                :center="mapCenter" 
                :zoom="mapZoom"
                :editable="true"
                @marker-drag="handleMarkerDrag"
              />
            </div>
          </el-card>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useUserStore } from '@/store/user'
import { useMapStore } from '@/store/map'
import { updateMapMarker } from '@/api/map'
import { ElMessage } from 'element-plus'
import MapViewer from '@/components/MapViewer.vue'
import { DataAnalysis, Tickets, Location, User, Bell, Document, Download, Refresh } from '@element-plus/icons-vue'

const userStore = useUserStore()
const mapStore = useMapStore()

const markers = ref([])
const mapCenter = ref([116.397428, 39.90923])
const mapZoom = ref(13)

onMounted(() => {
  fetchMarkers()
})

const fetchMarkers = async () => {
  await mapStore.fetchMarkers()
  markers.value = mapStore.markers
}

const handleMarkerDrag = async ({ id, longitude, latitude }) => {
  try {
    const res = await updateMapMarker(id, { longitude, latitude })
    if (res.code === 200) {
      ElMessage.success('标注位置已更新')
      fetchMarkers()
    }
  } catch (error) {
    ElMessage.error('更新失败')
  }
}
</script>

<style scoped lang="scss">
.map-manage-container {
  height: 100vh;
}

.el-aside {
  background-color: #304156;
  color: #fff;
  
  .logo {
    height: 60px;
    line-height: 60px;
    text-align: center;
    font-size: 20px;
    font-weight: bold;
    color: #fff;
    background-color: #263445;
  }
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: #fff;
  box-shadow: 0 1px 4px rgba(0,21,41,.08);
  
  .title {
    font-size: 18px;
    font-weight: bold;
    color: #303133;
  }
  
  .user-info {
    display: flex;
    align-items: center;
    cursor: pointer;
    color: #303133;
  }
}

.el-main {
  background: #f0f2f5;
  padding: 20px;
}

.map-controls {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.map-wrapper {
  height: calc(100% - 60px);
}
</style>

