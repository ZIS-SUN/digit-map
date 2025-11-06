<template>
  <div class="export-container">
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
          <span class="title">数据导出</span>
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
          <el-row :gutter="20">
            <el-col :span="8">
              <el-card class="export-card" @click="handleExport('specimens')">
                <div class="card-content">
                  <el-icon :size="60" color="#4CAF50"><Tickets /></el-icon>
                  <h3>标本数据</h3>
                  <p>导出所有标本信息到Excel</p>
                  <el-button type="primary" :loading="loading.specimens">
                    <el-icon><Download /></el-icon>
                    导出Excel
                  </el-button>
                </div>
              </el-card>
            </el-col>

            <el-col :span="8">
              <el-card class="export-card" @click="handleExport('collections')">
                <div class="card-content">
                  <el-icon :size="60" color="#FF9800"><Document /></el-icon>
                  <h3>采集记录</h3>
                  <p>导出所有采集记录到Excel</p>
                  <el-button type="primary" :loading="loading.collections">
                    <el-icon><Download /></el-icon>
                    导出Excel
                  </el-button>
                </div>
              </el-card>
            </el-col>

            <el-col :span="8">
              <el-card class="export-card" @click="handleExport('users')">
                <div class="card-content">
                  <el-icon :size="60" color="#2196F3"><User /></el-icon>
                  <h3>用户数据</h3>
                  <p>导出所有用户信息到Excel</p>
                  <el-button type="primary" :loading="loading.users">
                    <el-icon><Download /></el-icon>
                    导出Excel
                  </el-button>
                </div>
              </el-card>
            </el-col>
          </el-row>

          <el-card style="margin-top: 20px;">
            <template #header>
              <span>导出说明</span>
            </template>
            <el-descriptions :column="1" border>
              <el-descriptions-item label="导出格式">Excel (.xlsx)</el-descriptions-item>
              <el-descriptions-item label="文件编码">UTF-8</el-descriptions-item>
              <el-descriptions-item label="数据范围">全部数据</el-descriptions-item>
              <el-descriptions-item label="注意事项">
                导出的数据包含所有字段信息，请妥善保管，避免泄露
              </el-descriptions-item>
            </el-descriptions>
          </el-card>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup>
import { reactive } from 'vue'
import { useUserStore } from '@/store/user'
import { exportSpecimens } from '@/api/specimen'
import { ElMessage } from 'element-plus'
import { DataAnalysis, Tickets, Location, User, Bell, Document, Download } from '@element-plus/icons-vue'

const userStore = useUserStore()

const loading = reactive({
  specimens: false,
  collections: false,
  users: false
})

const handleExport = async (type) => {
  loading[type] = true
  
  try {
    let filename = ''
    let blob = null
    
    switch(type) {
      case 'specimens':
        filename = '标本数据.xlsx'
        const res = await exportSpecimens()
        blob = new Blob([res], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
        break
      case 'collections':
        filename = '采集记录.xlsx'
        // 这里应该调用导出采集记录的API
        ElMessage.info('采集记录导出功能开发中')
        return
      case 'users':
        filename = '用户数据.xlsx'
        // 这里应该调用导出用户数据的API
        ElMessage.info('用户数据导出功能开发中')
        return
    }
    
    if (blob) {
      // 创建下载链接
      const url = window.URL.createObjectURL(blob)
      const link = document.createElement('a')
      link.href = url
      link.download = filename
      link.click()
      window.URL.revokeObjectURL(url)
      
      ElMessage.success('导出成功')
    }
  } catch (error) {
    ElMessage.error('导出失败')
  } finally {
    loading[type] = false
  }
}
</script>

<style scoped lang="scss">
.export-container {
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
}

.export-card {
  cursor: pointer;
  transition: all 0.3s;
  
  &:hover {
    transform: translateY(-5px);
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  }
  
  .card-content {
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 30px 0;
    
    h3 {
      margin: 15px 0 10px;
      color: #303133;
    }
    
    p {
      color: #909399;
      margin-bottom: 20px;
    }
  }
}
</style>

