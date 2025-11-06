<template>
  <div class="announcements-admin-container">
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
          <span class="title">公告管理</span>
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
              <el-button type="primary" @click="handleAdd">
                <el-icon><Plus /></el-icon>
                发布公告
              </el-button>
            </template>

            <el-table :data="announcements" v-loading="loading" stripe>
              <el-table-column prop="id" label="ID" width="80" />
              <el-table-column prop="title" label="标题" />
              <el-table-column prop="content" label="内容" show-overflow-tooltip />
              <el-table-column label="置顶" width="100">
                <template #default="{ row }">
                  <el-tag :type="row.isTop ? 'warning' : 'info'">
                    {{ row.isTop ? '是' : '否' }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="publishTime" label="发布时间" width="180">
                <template #default="{ row }">
                  {{ formatDate(row.publishTime) }}
                </template>
              </el-table-column>
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
        <el-form-item label="公告标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入公告标题" />
        </el-form-item>
        <el-form-item label="公告内容" prop="content">
          <el-input 
            v-model="form.content" 
            type="textarea" 
            :rows="6"
            placeholder="请输入公告内容"
          />
        </el-form-item>
        <el-form-item label="是否置顶">
          <el-switch v-model="form.isTop" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading">发布</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useUserStore } from '@/store/user'
import { getAnnouncements, addAnnouncement, updateAnnouncement, deleteAnnouncement } from '@/api/announcement'
import { ElMessage, ElMessageBox } from 'element-plus'
import { DataAnalysis, Tickets, Location, User, Bell, Document, Download, Plus } from '@element-plus/icons-vue'

const userStore = useUserStore()

const announcements = ref([])
const total = ref(0)
const loading = ref(false)
const dialogVisible = ref(false)
const dialogTitle = ref('发布公告')
const submitLoading = ref(false)
const formRef = ref(null)

const queryParams = reactive({
  page: 1,
  pageSize: 10
})

const form = reactive({
  id: null,
  title: '',
  content: '',
  isTop: false
})

const rules = {
  title: [{ required: true, message: '请输入公告标题', trigger: 'blur' }],
  content: [{ required: true, message: '请输入公告内容', trigger: 'blur' }]
}

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

const handleAdd = () => {
  dialogTitle.value = '发布公告'
  resetForm()
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑公告'
  Object.assign(form, row)
  dialogVisible.value = true
}

const handleSubmit = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (valid) {
      submitLoading.value = true
      try {
        const api = form.id ? updateAnnouncement(form.id, form) : addAnnouncement(form)
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
    await ElMessageBox.confirm('确定要删除该公告吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    const res = await deleteAnnouncement(row.id)
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
    title: '',
    content: '',
    isTop: false
  })
}

const formatDate = (dateStr) => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  return date.toLocaleString('zh-CN')
}
</script>

<style scoped lang="scss">
.announcements-admin-container {
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

