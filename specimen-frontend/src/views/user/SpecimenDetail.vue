<template>
  <div class="detail-container">
    <el-header class="header">
      <div class="logo" @click="$router.push('/home')">🌿 植物标本库</div>
      <el-button @click="$router.back()" type="primary" plain>返回</el-button>
    </el-header>

    <div class="main-content" v-loading="loading">
      <el-card v-if="specimen">
        <el-row :gutter="30">
          <!-- 左侧图片 -->
          <el-col :span="10">
            <el-image 
              :src="specimen.imageUrl" 
              fit="cover"
              class="specimen-image"
            >
              <template #error>
                <div class="image-slot">
                  <el-icon><Picture /></el-icon>
                </div>
              </template>
            </el-image>
          </el-col>

          <!-- 右侧信息 -->
          <el-col :span="14">
            <div class="specimen-info">
              <h1 class="specimen-name">{{ specimen.name }}</h1>
              
              <el-divider />

              <el-descriptions :column="1" border>
                <el-descriptions-item label="标本编号">
                  {{ specimen.specimenCode }}
                </el-descriptions-item>
                <el-descriptions-item label="科属">
                  <el-tag>{{ specimen.family }}</el-tag>
                </el-descriptions-item>
                <el-descriptions-item label="采集人">
                  {{ specimen.collector }}
                </el-descriptions-item>
                <el-descriptions-item label="采集时间">
                  {{ formatDate(specimen.collectionTime) }}
                </el-descriptions-item>
                <el-descriptions-item label="采集地点">
                  {{ specimen.location }}
                </el-descriptions-item>
                <el-descriptions-item label="经纬度">
                  {{ specimen.longitude }}, {{ specimen.latitude }}
                </el-descriptions-item>
                <el-descriptions-item label="描述">
                  {{ specimen.description || '暂无描述' }}
                </el-descriptions-item>
              </el-descriptions>

              <div class="actions" style="margin-top: 30px;">
                <el-button type="primary" @click="viewOnMap">
                  <el-icon><Location /></el-icon>
                  在地图上查看
                </el-button>
              </div>
            </div>
          </el-col>
        </el-row>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getSpecimenDetail } from '@/api/specimen'
import { Picture, Location } from '@element-plus/icons-vue'

const route = useRoute()
const router = useRouter()

const specimen = ref(null)
const loading = ref(false)

onMounted(() => {
  fetchDetail()
})

const fetchDetail = async () => {
  loading.value = true
  try {
    const res = await getSpecimenDetail(route.params.id)
    if (res.code === 200) {
      specimen.value = res.data
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

const viewOnMap = () => {
  router.push(`/map?specimen=${specimen.value.id}`)
}
</script>

<style scoped lang="scss">
.detail-container {
  min-height: 100vh;
  background: #f5f5f5;
}

.header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: white;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  padding: 0 20px;
  height: 60px;
}

.logo {
  font-size: 20px;
  font-weight: bold;
  color: #4CAF50;
  cursor: pointer;
}

.main-content {
  max-width: 1200px;
  margin: 20px auto;
  padding: 0 20px;
}

.specimen-image {
  width: 100%;
  height: 500px;
  border-radius: 8px;
}

.image-slot {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
  background-color: #f5f7fa;
  color: #909399;
  font-size: 60px;
}

.specimen-name {
  font-size: 32px;
  color: #303133;
  margin-bottom: 20px;
}
</style>

