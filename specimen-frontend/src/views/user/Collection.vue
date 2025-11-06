<template>
  <div class="collection-container">
    <el-header class="header">
      <div class="logo" @click="$router.push('/home')">🌿 植物标本库</div>
      <el-menu mode="horizontal" default-active="/collection" router>
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
      <el-card>
        <template #header>
          <div class="card-header">
            <span>我的采集记录</span>
            <el-button type="primary" @click="dialogVisible = true">
              <el-icon><Plus /></el-icon>
              新增采集记录
            </el-button>
          </div>
        </template>

        <!-- 采集记录列表 -->
        <el-table :data="collections" v-loading="loading" stripe>
          <el-table-column prop="specimenName" label="标本名称" />
          <el-table-column prop="family" label="科属" width="120" />
          <el-table-column prop="location" label="采集地点" />
          <el-table-column label="经纬度" width="180">
            <template #default="{ row }">
              {{ row.longitude }}, {{ row.latitude }}
            </template>
          </el-table-column>
          <el-table-column prop="collectionTime" label="采集时间" width="120">
            <template #default="{ row }">
              {{ formatDate(row.collectionTime) }}
            </template>
          </el-table-column>
          <el-table-column label="操作" width="100">
            <template #default="{ row }">
              <el-button type="primary" link @click="viewDetail(row)">查看</el-button>
            </template>
          </el-table-column>
        </el-table>

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

    <!-- 新增采集记录对话框 -->
    <el-dialog v-model="dialogVisible" title="新增采集记录" width="600px">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="标本名称" prop="specimenName">
          <el-input v-model="form.specimenName" placeholder="请输入标本名称" />
        </el-form-item>
        <el-form-item label="科属" prop="family">
          <el-input v-model="form.family" placeholder="请输入科属" />
        </el-form-item>
        <el-form-item label="采集地点" prop="location">
          <el-input v-model="form.location" placeholder="请输入采集地点" />
        </el-form-item>
        <el-form-item label="经度" prop="longitude">
          <el-input-number v-model="form.longitude" :precision="6" :step="0.000001" style="width: 100%;" />
          <el-button @click="getCurrentLocation" size="small" style="margin-top: 5px;">
            <el-icon><Location /></el-icon>
            自动定位
          </el-button>
        </el-form-item>
        <el-form-item label="纬度" prop="latitude">
          <el-input-number v-model="form.latitude" :precision="6" :step="0.000001" style="width: 100%;" />
        </el-form-item>
        <el-form-item label="采集时间" prop="collectionTime">
          <el-date-picker 
            v-model="form.collectionTime" 
            type="date" 
            placeholder="选择日期"
            style="width: 100%;"
          />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input 
            v-model="form.description" 
            type="textarea" 
            :rows="3"
            placeholder="请输入描述信息"
          />
        </el-form-item>
        <el-form-item label="图片上传">
          <upload-image v-model="form.images" :limit="3" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading">提交</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useUserStore } from '@/store/user'
import { getCollections, addCollection } from '@/api/collection'
import { ElMessage } from 'element-plus'
import { User, Plus, Location } from '@element-plus/icons-vue'
import UploadImage from '@/components/UploadImage.vue'

const userStore = useUserStore()

const collections = ref([])
const total = ref(0)
const loading = ref(false)
const dialogVisible = ref(false)
const submitLoading = ref(false)
const formRef = ref(null)

const queryParams = reactive({
  page: 1,
  pageSize: 10
})

const form = reactive({
  specimenName: '',
  family: '',
  location: '',
  longitude: null,
  latitude: null,
  collectionTime: new Date(),
  description: '',
  images: []
})

const rules = {
  specimenName: [{ required: true, message: '请输入标本名称', trigger: 'blur' }],
  family: [{ required: true, message: '请输入科属', trigger: 'blur' }],
  location: [{ required: true, message: '请输入采集地点', trigger: 'blur' }],
  longitude: [{ required: true, message: '请输入经度', trigger: 'blur' }],
  latitude: [{ required: true, message: '请输入纬度', trigger: 'blur' }],
  collectionTime: [{ required: true, message: '请选择采集时间', trigger: 'change' }]
}

onMounted(() => {
  fetchData()
})

const fetchData = async () => {
  loading.value = true
  try {
    const res = await getCollections(queryParams)
    if (res.code === 200) {
      collections.value = res.data.list
      total.value = res.data.total
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

const getCurrentLocation = () => {
  if (navigator.geolocation) {
    navigator.geolocation.getCurrentPosition(
      (position) => {
        form.longitude = position.coords.longitude
        form.latitude = position.coords.latitude
        ElMessage.success('定位成功')
      },
      () => {
        ElMessage.error('定位失败，请手动输入')
      }
    )
  } else {
    ElMessage.error('浏览器不支持定位功能')
  }
}

const handleSubmit = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (valid) {
      submitLoading.value = true
      try {
        const res = await addCollection(form)
        if (res.code === 200) {
          ElMessage.success('提交成功')
          dialogVisible.value = false
          resetForm()
          fetchData()
        }
      } finally {
        submitLoading.value = false
      }
    }
  })
}

const resetForm = () => {
  Object.assign(form, {
    specimenName: '',
    family: '',
    location: '',
    longitude: null,
    latitude: null,
    collectionTime: new Date(),
    description: '',
    images: []
  })
}

const viewDetail = (row) => {
  ElMessage.info('查看详情功能')
}
</script>

<style scoped lang="scss">
.collection-container {
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

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}
</style>

