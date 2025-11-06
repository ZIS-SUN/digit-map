<template>
  <div class="search-bar">
    <el-input
      v-model="searchText"
      :placeholder="placeholder"
      clearable
      @clear="handleSearch"
      @keyup.enter="handleSearch"
    >
      <template #append>
        <el-button :icon="Search" @click="handleSearch" />
      </template>
    </el-input>
  </div>
</template>

<script setup>
import { ref, defineProps, defineEmits } from 'vue'
import { Search } from '@element-plus/icons-vue'

const props = defineProps({
  placeholder: {
    type: String,
    default: '请输入搜索关键词'
  },
  modelValue: {
    type: String,
    default: ''
  }
})

const emit = defineEmits(['update:modelValue', 'search'])

const searchText = ref(props.modelValue)

const handleSearch = () => {
  emit('update:modelValue', searchText.value)
  emit('search', searchText.value)
}
</script>

<style scoped lang="scss">
.search-bar {
  width: 100%;
  
  .el-input {
    width: 100%;
  }
}
</style>

