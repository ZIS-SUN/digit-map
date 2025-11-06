# 植物标本库管理系统 - 后端

基于Spring Boot 3.x的高校植物标本库管理系统后端服务。

## 技术栈

- **框架**: Spring Boot 3.2.0
- **ORM**: MyBatis Plus 3.5.5
- **数据库**: MySQL 8.x
- **安全**: JWT + BCrypt
- **文档**: Knife4j
- **构建工具**: Maven

## 功能特性

### 用户端功能
- 用户注册/登录
- 标本信息浏览与查询
- 数字地图标注展示
- 采集记录提交
- 公告信息查看
- 个人信息管理

### 管理端功能
- 管理员登录
- 标本信息管理（增删改查）
- 地图标注管理
- 用户管理
- 公告管理
- 数据统计分析
- 标本数据导出

## 快速开始

### 1. 环境要求

- JDK 17+
- Maven 3.6+
- MySQL 8.0+

### 2. 数据库配置

```bash
# 创建数据库
mysql -u root -p < src/main/resources/schema.sql
```

修改 `application.yml` 中的数据库配置：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/specimen_db
    username: your_username
    password: your_password
```

### 3. 构建运行

```bash
# 安装依赖
mvn clean install

# 运行项目
mvn spring-boot:run
```

服务启动后访问：
- 接口文档: http://localhost:8080/doc.html
- 后端API: http://localhost:8080

### 4. 默认账号

**管理员账号**
- 用户名: admin
- 密码: admin123

**测试用户**
- 用户名: testuser
- 密码: user123

## 项目结构

```
specimen-backend/
├── src/main/java/com/specimen/
│   ├── controller/           # 控制器层
│   │   ├── user/            # 用户端接口
│   │   └── admin/           # 管理端接口
│   ├── service/             # 服务层
│   │   └── impl/            # 服务实现
│   ├── mapper/              # 数据访问层
│   ├── entity/              # 实体类
│   ├── dto/                 # 数据传输对象
│   ├── config/              # 配置类
│   ├── util/                # 工具类
│   └── common/              # 通用类
├── src/main/resources/
│   ├── application.yml      # 配置文件
│   └── schema.sql           # 数据库脚本
└── pom.xml                  # Maven配置
```

## API接口

### 用户端接口

| 接口 | 方法 | 说明 | 认证 |
|------|------|------|------|
| `/api/auth/login` | POST | 用户登录 | 否 |
| `/api/auth/register` | POST | 用户注册 | 否 |
| `/api/specimens` | GET | 查询标本列表 | 否 |
| `/api/specimens/{id}` | GET | 查看标本详情 | 否 |
| `/api/map/markers` | GET | 获取地图标注 | 否 |
| `/api/collections` | POST | 提交采集记录 | 是 |
| `/api/collections/my` | GET | 我的采集记录 | 是 |
| `/api/announcements` | GET | 查看公告 | 否 |
| `/api/user/profile` | GET | 个人信息 | 是 |

### 管理端接口

| 接口 | 方法 | 说明 | 权限 |
|------|------|------|------|
| `/admin/auth/login` | POST | 管理员登录 | 否 |
| `/admin/dashboard/stats` | GET | 统计数据 | admin |
| `/admin/specimens` | GET/POST/PUT/DELETE | 标本管理 | admin |
| `/admin/specimens/export` | GET | 导出标本 | admin |
| `/admin/users` | GET | 用户管理 | admin |
| `/admin/announcements` | GET/POST/PUT/DELETE | 公告管理 | admin |
| `/admin/map/markers` | GET | 地图管理 | admin |

## 配置说明

### JWT配置
```yaml
jwt:
  secret: your-secret-key  # 请修改为随机字符串
  expiration: 86400000     # token过期时间（毫秒）
```

### 文件上传配置
```yaml
file:
  upload-path: ./uploads/specimens  # 文件上传路径
spring:
  servlet:
    multipart:
      max-file-size: 10MB
      max-request-size: 10MB
```

## 开发说明

### 统一返回格式

```json
{
  "code": 0,
  "msg": "ok",
  "data": {}
}
```

- code: 0表示成功，其他值表示错误
- msg: 返回消息
- data: 返回数据

### JWT认证

请求头中需要携带Token：
```
Authorization: Bearer {token}
```

### 异常处理

系统已配置全局异常处理器，自动捕获并返回统一格式的错误信息。

## 部署说明

### 1. 打包

```bash
mvn clean package -DskipTests
```

### 2. 运行

```bash
java -jar target/specimen-backend-1.0.0.jar
```

### 3. 生产环境配置

创建 `application-prod.yml` 配置生产环境参数：

```yaml
spring:
  datasource:
    url: jdbc:mysql://your-db-host:3306/specimen_db
    username: prod_user
    password: prod_password

jwt:
  secret: your-production-secret-key
```

运行时指定profile：
```bash
java -jar specimen-backend-1.0.0.jar --spring.profiles.active=prod
```

## 许可证

MIT License

## 联系方式

- 项目地址: [GitHub仓库地址]
- 问题反馈: [Issues页面]
- 技术支持: dev@specimen.com
