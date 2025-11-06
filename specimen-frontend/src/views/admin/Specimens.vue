<template>
  <div class="specimens-admin-container">
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
          <span class="title">标本管理</span>
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
            <template #header>
              <div class="card-header">
                <el-button type="primary" @click="handleAdd">
                  <el-icon><Plus /></el-icon>
                  新增标本
                </el-button>
                <search-bar v-model="queryParams.keyword" @search="handleSearch" style="width: 300px;" />
              </div>
            </template>

            <el-table :data="specimens" v-loading="loading" stripe>
              <el-table-column prop="specimenCode" label="标本编号" width="150" />
              <el-table-column label="图片" width="100">
                <template #default="{ row }">
                  <el-image :src="row.imageUrl" fit="cover" style="width: 60px; height: 60px;" />
                </template>
              </el-table-column>
              <el-table-column prop="name" label="名称" />
              <el-table-column prop="family" label="科属" width="120" />
              <el-table-column prop="collector" label="采集人" width="100" />
              <el-table-column prop="location" label="采集地点" />
              <el-table-column label="操作" width="200" fixed="right">
                <template #default="{ row }">
                  <el-button type="primary" link @click="handleEdit(row)">编辑</el-button>
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

    <!-- 新增/编辑对话框 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="700px">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="标本名称" prop="name">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="科属" prop="family">
          <el-input v-model="form.family" />
        </el-form-item>
        <el-form-item label="采集人" prop="collector">
          <el-input v-model="form.collector" />
        </el-form-item>
        <el-form-item label="采集地点" prop="location">
          <el-input v-model="form.location" />
        </el-form-item>
        <el-form-item label="经度" prop="longitude">
          <el-input-number v-model="form.longitude" :precision="6" style="width: 100%;" />
        </el-form-item>
        <el-form-item label="纬度" prop="latitude">
          <el-input-number v-model="form.latitude" :precision="6" style="width: 100%;" />
        </el-form-item>
        <el-form-item label="采集时间" prop="collectionTime">
          <el-date-picker v-model="form.collectionTime" type="date" style="width: 100%;" />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input v-model="form.description" type="textarea" :rows="3" />
        </el-form-item>
        <el-form-item label="图片">
          <upload-image v-model="form.images" :limit="5" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useUserStore } from '@/store/user'
import { getSpecimens, addSpecimen, updateSpecimen, deleteSpecimen } from '@/api/specimen'
import { ElMessage, ElMessageBox } from 'element-plus'
import SearchBar from '@/components/SearchBar.vue'
import UploadImage from '@/components/UploadImage.vue'
import { DataAnalysis, Tickets, Location, User, Bell, Document, Download, Plus } from '@element-plus/icons-vue'

const userStore = useUserStore()

const specimens = ref([])
const total = ref(0)
const loading = ref(false)
const dialogVisible = ref(false)
const dialogTitle = ref('新增标本')
const submitLoading = ref(false)
const formRef = ref(null)

const queryParams = reactive({
  page: 1,
  pageSize: 10,
  keyword: ''
})

const form = reactive({
  id: null,
  name: '',
  family: '',
  collector: '',
  location: '',
  longitude: null,
  latitude: null,
  collectionTime: new Date(),
  description: '',
  images: []
})

const rules = {
  name: [{ required: true, message: '请输入标本名称', trigger: 'blur' }],
  family: [{ required: true, message: '请输入科属', trigger: 'blur' }],
  collector: [{ required: true, message: '请输入采集人', trigger: 'blur' }],
  location: [{ required: true, message: '请输入采集地点', trigger: 'blur' }],
  longitude: [{ required: true, message: '请输入经度', trigger: 'blur' }],
  latitude: [{ required: true, message: '请输入纬度', trigger: 'blur' }]
}

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

const handleAdd = () => {
  dialogTitle.value = '新增标本'
  resetForm()
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑标本'
  Object.assign(form, row)
  dialogVisible.value = true
}

const handleSubmit = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (valid) {
      submitLoading.value = true
      try {
        const api = form.id ? updateSpecimen(form.id, form) : addSpecimen(form)
        const res = await api
        if (res.code === 200) {
          ElMessage.success('保存成功')
          dialogVisible.value = false
          fetchData()
        }
      } finally {
        submitLoading.value = false
      }
    }
  })
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除该标本吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    const res = await deleteSpecimen(row.id)
    if (res.code === 200) {
      ElMessage.success('删除成功')
      fetchData()
    }
  } catch (error) {
    // 取消删除
  }
}

const resetForm = () => {
  Object.assign(form, {
    id: null,
    name: '',
    family: '',
    collector: '',
    location: '',
    longitude: null,
    latitude: null,
    collectionTime: new Date(),
    description: '',
    images: []
  })
}
</script>

<style scoped lang="scss">
.specimens-admin-container {
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

