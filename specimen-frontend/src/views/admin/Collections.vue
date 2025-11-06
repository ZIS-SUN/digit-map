<template>
  <div class="collections-admin-container">
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
          <span class="title">采集记录管理</span>
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
          <el-card>
            <el-table :data="collections" v-loading="loading" stripe>
              <el-table-column prop="id" label="ID" width="80" />
              <el-table-column prop="specimenName" label="标本名称" />
              <el-table-column prop="family" label="科属" width="120" />
              <el-table-column prop="collector" label="采集人" width="100" />
              <el-table-column prop="location" label="采集地点" />
              <el-table-column label="经纬度" width="180">
                <template #default="{ row }">
                  {{ row.longitude }}, {{ row.latitude }}
                </template>
              </el-table-column>
              <el-table-column prop="collectionTime" label="采集时间" width="180">
                <template #default="{ row }">
                  {{ formatDate(row.collectionTime) }}
                </template>
              </el-table-column>
              <el-table-column label="操作" width="100" fixed="right">
                <template #default="{ row }">
                  <el-button type="danger" link @click="handleDelete(row)">删除</el-button>
                </template>
              </el-table-column>
            </el-table>

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
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useUserStore } from '@/store/user'
import { getAllCollections, deleteCollection } from '@/api/collection'
import { ElMessage, ElMessageBox } from 'element-plus'
import { DataAnalysis, Tickets, Location, User, Bell, Document, Download } from '@element-plus/icons-vue'

const userStore = useUserStore()

const collections = ref([])
const total = ref(0)
const loading = ref(false)

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
    const res = await getAllCollections(queryParams)
    if (res.code === 200) {
      collections.value = res.data.list
      total.value = res.data.total
    }
  } finally {
    loading.value = false
  }
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除该采集记录吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    const res = await deleteCollection(row.id)
    if (res.code === 200) {
      ElMessage.success('删除成功')
      fetchData()
    }
  } catch (error) {
    // 取消删除
  }
}

const formatDate = (dateStr) => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  return date.toLocaleString('zh-CN')
}
</script>

<style scoped lang="scss">
.collections-admin-container {
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

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}
</style>

