<template>
  <div class="announcement-container">
    <el-header class="header">
      <div class="logo" @click="$router.push('/home')">🌿 植物标本库</div>
      <el-menu mode="horizontal" default-active="/announcement" router>
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
      <el-card v-loading="loading">
        <template #header>
          <span>📢 系统公告</span>
        </template>

        <el-timeline>
          <el-timeline-item 
            v-for="item in announcements" 
            :key="item.id"
            :timestamp="formatDate(item.publishTime)"
            placement="top"
          >
            <el-card @click="viewDetail(item)">
              <h4>{{ item.title }}</h4>
              <p class="content">{{ item.content }}</p>
            </el-card>
          </el-timeline-item>
        </el-timeline>

        <el-empty v-if="!loading && announcements.length === 0" description="暂无公告" />

        <!-- 分页 -->
        <el-pagination
          v-if="total > 0"
          v-model:current-page="queryParams.page"
          v-model:page-size="queryParams.pageSize"
          :total="total"
          layout="total, prev, pager, next"
          @current-change="fetchData"
          class="pagination"
        />
      </el-card>
    </div>

    <!-- 公告详情对话框 -->
    <el-dialog v-model="dialogVisible" title="公告详情" width="600px">
      <div v-if="currentAnnouncement">
        <h3>{{ currentAnnouncement.title }}</h3>
        <el-divider />
        <div class="announcement-meta">
          <span>发布时间：{{ formatDate(currentAnnouncement.publishTime) }}</span>
        </div>
        <div class="announcement-content" v-html="currentAnnouncement.content"></div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useUserStore } from '@/store/user'
import { getAnnouncements } from '@/api/announcement'
import { User } from '@element-plus/icons-vue'

const userStore = useUserStore()

const announcements = ref([])
const total = ref(0)
const loading = ref(false)
const dialogVisible = ref(false)
const currentAnnouncement = ref(null)

const queryParams = reactive({
  page: 1,
  pageSize: 10
})

onMounted(() => {
  fetchData()
})

const fetchData = async () => {
  loading.value = true
  try {
    const res = await getAnnouncements(queryParams)
    if (res.code === 200) {
      announcements.value = res.data.list
      total.value = res.data.total
    }
  } finally {
    loading.value = false
  }
}

const formatDate = (dateStr) => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  return date.toLocaleString('zh-CN')
}

const viewDetail = (item) => {
  currentAnnouncement.value = item
  dialogVisible.value = true
}
</script>

<style scoped lang="scss">
.announcement-container {
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

.el-timeline-item {
  .el-card {
    cursor: pointer;
    transition: all 0.3s;
    
    &:hover {
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
    }
    
    h4 {
      margin-bottom: 10px;
      color: #303133;
    }
    
    .content {
      color: #606266;
      line-height: 1.6;
      overflow: hidden;
      text-overflow: ellipsis;
      display: -webkit-box;
      -webkit-line-clamp: 2;
      -webkit-box-orient: vertical;
    }
  }
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}

.announcement-meta {
  color: #909399;
  font-size: 14px;
  margin-bottom: 15px;
}

.announcement-content {
  line-height: 1.8;
  color: #606266;
}
</style>

