<template>
  <el-card class="specimen-card" shadow="hover" @click="handleClick">
    <div class="image-wrapper">
      <el-image 
        :src="specimen.imageUrl || '/default-specimen.jpg'" 
        fit="cover"
        class="specimen-image"
      >
        <template #error>
          <div class="image-slot">
            <el-icon><Picture /></el-icon>
          </div>
        </template>
      </el-image>
    </div>
    <div class="specimen-info">
      <h3 class="specimen-name">{{ specimen.name }}</h3>
      <div class="specimen-detail">
        <p><el-tag size="small">{{ specimen.family }}</el-tag></p>
        <p class="collector">采集人：{{ specimen.collector }}</p>
        <p class="time">{{ formatDate(specimen.collectionTime) }}</p>
      </div>
    </div>
  </el-card>
</template>

<script setup>
import { defineProps, defineEmits } from 'vue'
import { Picture } from '@element-plus/icons-vue'

const props = defineProps({
  specimen: {
    type: Object,
    required: true
  }
})

const emit = defineEmits(['click'])

const handleClick = () => {
  emit('click', props.specimen)
}

const formatDate = (dateStr) => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  return date.toLocaleDateString('zh-CN')
}
</script>

<style scoped lang="scss">
.specimen-card {
  cursor: pointer;
  transition: transform 0.3s;
  
  &:hover {
    transform: translateY(-5px);
  }
}

.image-wrapper {
  width: 100%;
  height: 200px;
  overflow: hidden;
  border-radius: 4px;
}

.specimen-image {
  width: 100%;
  height: 100%;
}

.image-slot {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
  background-color: #f5f7fa;
  color: #909399;
  font-size: 30px;
}

.specimen-info {
  margin-top: 15px;
}

.specimen-name {
  font-size: 18px;
  font-weight: bold;
  color: #303133;
  margin-bottom: 10px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.specimen-detail {
  font-size: 14px;
  color: #606266;
  
  p {
    margin: 5px 0;
  }
  
  .collector {
    color: #909399;
  }
  
  .time {
    color: #C0C4CC;
    font-size: 12px;
  }
}
</style>

