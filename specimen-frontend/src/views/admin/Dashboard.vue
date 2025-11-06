<template>
  <div class="dashboard-container">
    <el-container>
      <!-- 侧边栏 -->
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

      <!-- 主内容区 -->
      <el-container>
        <el-header class="header">
          <span class="title">数据仪表盘</span>
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
          <div v-loading="loading">
            <!-- 统计卡片 -->
            <el-row :gutter="20" class="stats-cards">
              <el-col :span="6">
                <el-card>
                  <el-statistic title="标本总量" :value="stats.specimenCount || 0">
                    <template #prefix>
                      <el-icon color="#4CAF50"><Tickets /></el-icon>
                    </template>
                  </el-statistic>
                </el-card>
              </el-col>
              <el-col :span="6">
                <el-card>
                  <el-statistic title="采集次数" :value="stats.collectionCount || 0">
                    <template #prefix>
                      <el-icon color="#FF9800"><Document /></el-icon>
                    </template>
                  </el-statistic>
                </el-card>
              </el-col>
              <el-col :span="6">
                <el-card>
                  <el-statistic title="用户数量" :value="stats.userCount || 0">
                    <template #prefix>
                      <el-icon color="#2196F3"><User /></el-icon>
                    </template>
                  </el-statistic>
                </el-card>
              </el-col>
              <el-col :span="6">
                <el-card>
                  <el-statistic title="地图标注" :value="stats.markerCount || 0">
                    <template #prefix>
                      <el-icon color="#9C27B0"><Location /></el-icon>
                    </template>
                  </el-statistic>
                </el-card>
              </el-col>
            </el-row>

            <!-- 图表 -->
            <el-row :gutter="20" style="margin-top: 20px;">
              <el-col :span="12">
                <el-card>
                  <template #header>
                    <span>科属分布</span>
                  </template>
                  <chart-panel :option="pieChartOption" height="350px" />
                </el-card>
              </el-col>
              <el-col :span="12">
                <el-card>
                  <template #header>
                    <span>采集趋势</span>
                  </template>
                  <chart-panel :option="lineChartOption" height="350px" />
                </el-card>
              </el-col>
            </el-row>
          </div>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useUserStore } from '@/store/user'
import { getDashboardStats } from '@/api/dashboard'
import ChartPanel from '@/components/ChartPanel.vue'
import { DataAnalysis, Tickets, Location, User, Bell, Document, Download } from '@element-plus/icons-vue'

const userStore = useUserStore()

const loading = ref(false)
const stats = reactive({
  specimenCount: 0,
  collectionCount: 0,
  userCount: 0,
  markerCount: 0
})

const pieChartOption = ref({
  tooltip: {
    trigger: 'item'
  },
  legend: {
    orient: 'vertical',
    left: 'left'
  },
  series: [{
    type: 'pie',
    radius: '50%',
    data: [],
    emphasis: {
      itemStyle: {
        shadowBlur: 10,
        shadowOffsetX: 0,
        shadowColor: 'rgba(0, 0, 0, 0.5)'
      }
    }
  }]
})

const lineChartOption = ref({
  tooltip: {
    trigger: 'axis'
  },
  xAxis: {
    type: 'category',
    data: []
  },
  yAxis: {
    type: 'value'
  },
  series: [{
    data: [],
    type: 'line',
    smooth: true,
    itemStyle: {
      color: '#4CAF50'
    },
    areaStyle: {
      color: 'rgba(76, 175, 80, 0.2)'
    }
  }]
})

onMounted(() => {
  fetchStats()
})

const fetchStats = async () => {
  loading.value = true
  try {
    const res = await getDashboardStats()
    if (res.code === 200) {
      const data = res.data
      
      stats.specimenCount = data.specimenCount
      stats.collectionCount = data.collectionCount
      stats.userCount = data.userCount
      stats.markerCount = data.markerCount
      
      // 更新饼图数据
      pieChartOption.value.series[0].data = data.familyData?.map(item => ({
        name: item.family,
        value: item.count
      })) || []
      
      // 更新折线图数据
      lineChartOption.value.xAxis.data = data.trendData?.map(item => item.date) || []
      lineChartOption.value.series[0].data = data.trendData?.map(item => item.count) || []
    }
  } finally {
    loading.value = false
  }
}
</script>

<style scoped lang="scss">
.dashboard-container {
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
    
    .el-icon {
      margin-right: 5px;
    }
  }
}

.el-main {
  background: #f0f2f5;
}

.stats-cards {
  .el-card {
    :deep(.el-statistic__head) {
      font-size: 14px;
      color: #909399;
    }
    
    :deep(.el-statistic__content) {
      font-size: 28px;
      font-weight: bold;
    }
  }
}
</style>

