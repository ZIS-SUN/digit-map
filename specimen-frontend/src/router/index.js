import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/store/user'

const routes = [
  {
    path: '/',
    redirect: '/home'
  },
  // 用户端路由
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/user/Login.vue'),
    meta: { requiresAuth: false }
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/views/user/Register.vue'),
    meta: { requiresAuth: false }
  },
  {
    path: '/home',
    name: 'Home',
    component: () => import('@/views/user/Home.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/specimens',
    name: 'Specimens',
    component: () => import('@/views/user/Specimens.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/specimen/:id',
    name: 'SpecimenDetail',
    component: () => import('@/views/user/SpecimenDetail.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/map',
    name: 'Map',
    component: () => import('@/views/user/Map.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/collection',
    name: 'Collection',
    component: () => import('@/views/user/Collection.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/profile',
    name: 'Profile',
    component: () => import('@/views/user/Profile.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/announcement',
    name: 'Announcement',
    component: () => import('@/views/user/Announcement.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/stats',
    name: 'Stats',
    component: () => import('@/views/user/Stats.vue'),
    meta: { requiresAuth: true }
  },
  // 管理端路由
  {
    path: '/admin/login',
    name: 'AdminLogin',
    component: () => import('@/views/admin/Login.vue'),
    meta: { requiresAuth: false }
  },
  {
    path: '/admin',
    redirect: '/admin/dashboard'
  },
  {
    path: '/admin/dashboard',
    name: 'AdminDashboard',
    component: () => import('@/views/admin/Dashboard.vue'),
    meta: { requiresAuth: true, requiresAdmin: true }
  },
  {
    path: '/admin/specimens',
    name: 'AdminSpecimens',
    component: () => import('@/views/admin/Specimens.vue'),
    meta: { requiresAuth: true, requiresAdmin: true }
  },
  {
    path: '/admin/map',
    name: 'AdminMap',
    component: () => import('@/views/admin/MapManage.vue'),
    meta: { requiresAuth: true, requiresAdmin: true }
  },
  {
    path: '/admin/users',
    name: 'AdminUsers',
    component: () => import('@/views/admin/Users.vue'),
    meta: { requiresAuth: true, requiresAdmin: true }
  },
  {
    path: '/admin/announcements',
    name: 'AdminAnnouncements',
    component: () => import('@/views/admin/Announcements.vue'),
    meta: { requiresAuth: true, requiresAdmin: true }
  },
  {
    path: '/admin/collections',
    name: 'AdminCollections',
    component: () => import('@/views/admin/Collections.vue'),
    meta: { requiresAuth: true, requiresAdmin: true }
  },
  {
    path: '/admin/export',
    name: 'AdminExport',
    component: () => import('@/views/admin/Export.vue'),
    meta: { requiresAuth: true, requiresAdmin: true }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const userStore = useUserStore()
  const isLoggedIn = userStore.isLoggedIn
  const isAdmin = userStore.isAdmin

  // 需要登录的页面
  if (to.meta.requiresAuth && !isLoggedIn) {
    next('/login')
    return
  }

  // 需要管理员权限的页面
  if (to.meta.requiresAdmin && !isAdmin) {
    next('/login')
    return
  }

  // 已登录用户访问登录页，跳转到首页
  if (isLoggedIn && (to.path === '/login' || to.path === '/register')) {
    if (isAdmin) {
      next('/admin/dashboard')
    } else {
      next('/home')
    }
    return
  }

  next()
})

export default router

