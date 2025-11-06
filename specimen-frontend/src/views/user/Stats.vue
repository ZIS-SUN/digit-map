<template>
  <div class="stats-container">
    <el-header class="header">
      <div class="logo" @click="$router.push('/home')">🌿 植物标本库</div>
      <el-menu mode="horizontal" default-active="/stats" router>
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

    <div class="main-content">
      <!-- 统计卡片 -->
      <el-row :gutter="20" class="stats-cards">
        <el-col :span="8">
          <el-card>
            <el-statistic title="我的采集记录" :value="stats.totalCollections || 0">
              <template #prefix>
                <el-icon color="#4CAF50"><Document /></el-icon>
              </template>
            </el-statistic>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card>
            <el-statistic title="采集科属数" :value="stats.familyCount || 0">
              <template #prefix>
                <el-icon color="#FF9800"><Collection /></el-icon>
              </template>
            </el-statistic>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card>
            <el-statistic title="本月采集" :value="stats.monthCollections || 0">
              <template #prefix>
                <el-icon color="#2196F3"><Calendar /></el-icon>
              </template>
            </el-statistic>
          </el-card>
        </el-col>
      </el-row>

      <!-- 图表 -->
      <el-row :gutter="20" style="margin-top: 20px;">
        <el-col :span="12">
          <el-card v-loading="loading">
            <template #header>
              <span>月度采集趋势</span>
            </template>
            <chart-panel :option="lineChartOption" height="350px" />
          </el-card>
        </el-col>
        <el-col :span="12">
          <el-card v-loading="loading">
            <template #header>
              <span>科属分布</span>
            </template>
            <chart-panel :option="pieChartOption" height="350px" />
          </el-card>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useUserStore } from '@/store/user'
import { getCollectionStats } from '@/api/collection'
import ChartPanel from '@/components/ChartPanel.vue'
import { User, Document, Collection, Calendar } from '@element-plus/icons-vue'

const userStore = useUserStore()

const loading = ref(false)
const stats = reactive({
  totalCollections: 0,
  familyCount: 0,
  monthCollections: 0
})

const lineChartOption = ref({
  title: {
    text: '采集趋势'
  },
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

const pieChartOption = ref({
  title: {
    text: '科属占比'
  },
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

onMounted(() => {
  fetchStats()
})

const fetchStats = async () => {
  loading.value = true
  try {
    const res = await getCollectionStats()
    if (res.code === 200) {
      const data = res.data
      
      // 更新统计数据
      stats.totalCollections = data.totalCollections
      stats.familyCount = data.familyCount
      stats.monthCollections = data.monthCollections
      
      // 更新折线图数据
      lineChartOption.value.xAxis.data = data.monthlyData?.map(item => item.month) || []
      lineChartOption.value.series[0].data = data.monthlyData?.map(item => item.count) || []
      
      // 更新饼图数据
      pieChartOption.value.series[0].data = data.familyData?.map(item => ({
        name: item.family,
        value: item.count
      })) || []
    }
  } finally {
    loading.value = false
  }
}
</script>

<style scoped lang="scss">
.stats-container {
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

.main-content {
  max-width: 1200px;
  margin: 20px auto;
  padding: 0 20px;
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

