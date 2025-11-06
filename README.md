# 植物标本库管理系统 (Digit Map)

基于Spring Boot + Vue3的高校植物标本数字化管理系统，支持标本信息管理、数字地图标注、采集记录管理等功能。

## 项目简介

本系统是一个完整的前后端分离项目，旨在为高校植物标本库提供数字化管理解决方案。系统包含用户端和管理端两个部分，实现了标本信息录入、查询、统计分析以及地图可视化等功能。

## 技术栈

### 后端技术
- Spring Boot 3.2.0
- MyBatis Plus 3.5.5
- MySQL 8.x
- JWT + BCrypt
- Knife4j (API文档)

### 前端技术
- Vue 3
- Element Plus
- Pinia
- ECharts
- 高德地图API
- Axios

## 项目结构

```
digit-map/
├── specimen-backend/          # 后端项目
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/         # Java源码
│   │   │   └── resources/    # 配置文件和SQL脚本
│   │   │       ├── application.yml
│   │   │       ├── schema.sql        # 数据库结构
│   │   │       └── test-data.sql     # 测试数据
│   └── pom.xml               # Maven配置
│
├── specimen-frontend/         # 前端项目
│   ├── src/
│   │   ├── api/              # API接口
│   │   ├── components/       # 组件
│   │   ├── views/            # 页面
│   │   ├── router/           # 路由
│   │   └── store/            # 状态管理
│   └── package.json          # NPM配置
│
├── 测试账号.md                # 测试账号密码
└── README.md                 # 项目说明
```

## 快速开始

### 环境要求

- JDK 17+
- Node.js 16+
- MySQL 8.0+
- Maven 3.6+

### 后端部署

1. **创建数据库**

```bash
# 登录MySQL
mysql -u root -p

# 创建数据库
source specimen-backend/src/main/resources/schema.sql

# 导入测试数据（可选）
source specimen-backend/src/main/resources/test-data.sql
```

2. **配置数据库连接**

修改 `specimen-backend/src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/specimen_db
    username: your_username
    password: your_password
```

3. **启动后端服务**

```bash
cd specimen-backend
mvn clean install
mvn spring-boot:run
```

后端服务默认运行在 `http://localhost:8080`

API文档访问: `http://localhost:8080/doc.html`

### 前端部署

1. **安装依赖**

```bash
cd specimen-frontend
npm install
```

2. **启动开发服务器**

```bash
npm run dev
```

前端应用默认运行在 `http://localhost:3000`

3. **构建生产版本**

```bash
npm run build
```

## 功能模块

### 用户端
- ✅ 用户注册/登录
- ✅ 标本信息浏览与搜索
- ✅ 标本详情查看
- ✅ 数字地图标注展示
- ✅ 采集记录提交
- ✅ 个人采集记录管理
- ✅ 公告信息查看
- ✅ 数据统计图表

### 管理端
- ✅ 管理员登录
- ✅ 数据仪表盘
- ✅ 标本信息管理（CRUD）
- ✅ 地图标注管理
- ✅ 用户管理
- ✅ 公告管理
- ✅ 采集记录审核
- ✅ 数据导出（Excel）

## 测试账号

详见 [测试账号.md](./测试账号.md)

**管理员**: admin / admin123  
**用户**: testuser / user123

## API接口

### 用户端主要接口

- `POST /api/auth/login` - 用户登录
- `POST /api/auth/register` - 用户注册
- `GET /api/specimens` - 查询标本列表
- `GET /api/specimens/{id}` - 查看标本详情
- `GET /api/map/markers` - 获取地图标注
- `POST /api/collections` - 提交采集记录
- `GET /api/announcements` - 查看公告

### 管理端主要接口

- `POST /admin/auth/login` - 管理员登录
- `GET /admin/dashboard/stats` - 统计数据
- `GET/POST/PUT/DELETE /admin/specimens` - 标本管理
- `GET /admin/specimens/export` - 导出标本数据
- `GET /admin/users` - 用户管理
- `GET/POST/PUT/DELETE /admin/announcements` - 公告管理

详细API文档请访问: `http://localhost:8080/doc.html`

## 开发说明

### 统一返回格式

```json
{
  "code": 0,
  "msg": "ok",
  "data": {}
}
```

### JWT认证

需要认证的接口请在请求头中携带Token：

```
Authorization: Bearer {token}
```

## 部署说明

### 后端打包

```bash
cd specimen-backend
mvn clean package -DskipTests
java -jar target/specimen-backend-1.0.0.jar
```

### 前端打包

```bash
cd specimen-frontend
npm run build
# 将 dist 目录部署到 Web 服务器
```

## 许可证

MIT License

## 贡献

欢迎提交Issue和Pull Request！

## 联系方式

- GitHub: https://github.com/ZIS-SUN/digit-map
- Issues: https://github.com/ZIS-SUN/digit-map/issues

