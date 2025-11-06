# 植物标本库管理系统 - 前端

基于Vue3 + Element Plus + Pinia的植物标本管理系统前端项目

## 技术栈

- Vue 3
- Vue Router
- Pinia
- Element Plus
- ECharts
- Axios
- 高德地图API

## 安装依赖

```bash
npm install
```

## 开发运行

```bash
npm run dev
```

访问 http://localhost:3000

## 构建部署

```bash
npm run build
```

## 项目结构

```
specimen-frontend/
├── src/
│   ├── api/              # 接口封装
│   ├── assets/           # 静态资源
│   ├── components/       # 通用组件
│   ├── router/           # 路由配置
│   ├── store/            # Pinia状态管理
│   ├── utils/            # 工具函数
│   ├── views/
│   │   ├── user/         # 用户端页面
│   │   └── admin/        # 管理端页面
│   ├── App.vue
│   └── main.js
```

## 功能模块

### 用户端
- 登录注册
- 首页展示
- 标本列表与详情
- 地图分布
- 采集记录
- 个人中心
- 数据统计

### 管理端
- 管理员登录
- 数据仪表盘
- 标本管理
- 地图标注管理
- 用户管理
- 公告管理
- 采集记录管理
- 数据导出

