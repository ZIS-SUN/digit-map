<template>
  <div class="upload-image">
    <el-upload
      :action="uploadUrl"
      :headers="headers"
      list-type="picture-card"
      :on-success="handleSuccess"
      :on-remove="handleRemove"
      :on-preview="handlePreview"
      :file-list="fileList"
      :limit="limit"
    >
      <el-icon><Plus /></el-icon>
    </el-upload>
    
    <el-dialog v-model="previewVisible" title="图片预览">
      <img :src="previewUrl" style="width: 100%;" />
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, defineProps, defineEmits } from 'vue'
import { Plus } from '@element-plus/icons-vue'

const props = defineProps({
  modelValue: {
    type: Array,
    default: () => []
  },
  limit: {
    type: Number,
    default: 3
  }
})

const emit = defineEmits(['update:modelValue'])

const uploadUrl = computed(() => '/api/upload')
const headers = computed(() => ({
  Authorization: `Bearer ${localStorage.getItem('token')}`
}))

const fileList = ref(props.modelValue || [])
const previewVisible = ref(false)
const previewUrl = ref('')

const handleSuccess = (response, file) => {
  if (response.code === 200) {
    fileList.value.push({
      name: file.name,
      url: response.data.url
    })
    emit('update:modelValue', fileList.value)
  }
}

const handleRemove = (file) => {
  const index = fileList.value.findIndex(item => item.url === file.url)
  if (index > -1) {
    fileList.value.splice(index, 1)
    emit('update:modelValue', fileList.value)
  }
}

const handlePreview = (file) => {
  previewUrl.value = file.url
  previewVisible.value = true
}
</script>

<style scoped lang="scss">
.upload-image {
  :deep(.el-upload-list__item) {
    transition: all 0.3s;
  }
}
</style>

