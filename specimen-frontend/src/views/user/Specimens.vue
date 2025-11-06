<template>
  <div class="specimens-container">
    <el-header class="header">
      <div class="logo" @click="$router.push('/home')">🌿 植物标本库</div>
      <el-menu mode="horizontal" default-active="/specimens" router>
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
      <!-- 搜索和筛选 -->
      <el-card class="filter-card">
        <el-row :gutter="20">
          <el-col :span="8">
            <search-bar 
              v-model="queryParams.keyword" 
              placeholder="搜索标本名称"
              @search="handleSearch"
            />
          </el-col>
          <el-col :span="6">
            <el-select v-model="queryParams.category" placeholder="选择科属" clearable @change="handleSearch">
              <el-option label="全部" value="" />
              <el-option label="蔷薇科" value="蔷薇科" />
              <el-option label="菊科" value="菊科" />
              <el-option label="禾本科" value="禾本科" />
              <el-option label="豆科" value="豆科" />
            </el-select>
          </el-col>
        </el-row>
      </el-card>

      <!-- 标本列表 -->
      <el-card class="list-card" v-loading="loading">
        <el-row :gutter="20">
          <el-col :span="6" v-for="specimen in specimens" :key="specimen.id">
            <specimen-card :specimen="specimen" @click="toDetail(specimen.id)" />
          </el-col>
          <el-empty v-if="!loading && specimens.length === 0" description="暂无数据" />
        </el-row>

        <!-- 分页 -->
        <el-pagination
          v-if="total > 0"
          v-model:current-page="queryParams.page"
          v-model:page-size="queryParams.pageSize"
          :total="total"
          :page-sizes="[12, 24, 48]"
          layout="total, sizes, prev, pager, next, jumper"
          @current-change="fetchData"
          @size-change="fetchData"
          class="pagination"
        />
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/store/user'
import { getSpecimens } from '@/api/specimen'
import SpecimenCard from '@/components/SpecimenCard.vue'
import SearchBar from '@/components/SearchBar.vue'
import { User } from '@element-plus/icons-vue'

const router = useRouter()
const userStore = useUserStore()

const specimens = ref([])
const total = ref(0)
const loading = ref(false)

const queryParams = reactive({
  page: 1,
  pageSize: 12,
  keyword: '',
  category: ''
})

onMounted(() => {
  fetchData()
})

const fetchData = async () => {
  loading.value = true
  try {
    const res = await getSpecimens(queryParams)
    if (res.code === 200) {
      specimens.value = res.data.list
      total.value = res.data.total
    }
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  queryParams.page = 1
  fetchData()
}

const toDetail = (id) => {
  router.push(`/specimen/${id}`)
}
</script>

<style scoped lang="scss">
.specimens-container {
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

.filter-card {
  margin-bottom: 20px;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}
</style>

