# Han-Note (小憨书)

一款基于 Spring Boot 4.0 + Spring Cloud 微服务架构的笔记应用后端服务。

## 项目简介

Han-Note 是一个微服务架构的笔记应用后端系统，提供用户认证、文件存储、API网关等核心功能。采用最新技术栈构建，具有高可用、易扩展的特点。

## 技术栈

| 技术 | 版本         | 说明 |
|------|------------|------|
| Java | 25         | 开发语言 |
| Spring Boot | 4.0.5      | 基础框架 |
| Spring Cloud | 2025.1.1   | 微服务框架 |
| Spring Cloud Alibaba | 2025.1.0.0 | 阿里巴巴微服务套件 |
| PostgreSQL | 18+        | 关系型数据库 |
| MyBatis | 4.0.1      | ORM框架 |
| Sa-Token | 1.45.0     | 认证授权框架 |
| Nacos | 3.1.1      | 服务注册与配置中心 |
| Redis | 8.x        | 缓存中间件 |
| Gradle | 9.x        | 构建工具 |

## 项目结构

```
han-note/
├── platform/                                 # 依赖管理 BOM
├── framework/                                # 公共框架模块
│   ├── common/                              # 通用工具类、响应封装、异常处理
│   ├── han-note-spring-boot-starter-biz-context/     # 用户上下文 Starter
│   ├── han-note-spring-boot-starter-jackson/        # Jackson 配置 Starter
│   └── han-note-spring-boot-starter-biz-operationlog/ # 操作日志 Starter
├── han-note-auth/                            # 认证服务 (端口: 8081)
├── han-note-gateway/                         # API网关服务 (端口: 8080)
└── han-note-oss/                             # 对象存储服务
    ├── han-note-oss-api/                    # API定义
    └── han-note-oss-biz/                    # 业务实现
```

## 环境要求

- JDK 25+
- Gradle 9.x
- PostgreSQL 18+
- Redis 8.0+
- Nacos 3.x

## 快速开始

### 1. 克隆项目

```bash
git clone https://github.com/your-repo/han-note.git
cd han-note
```

### 2. 初始化数据库

执行 `sql/table.sql` 和 `sql/data.sql` 初始化数据库表结构和基础数据。

### 3. 配置 Nacos

确保 Nacos 服务已启动，并在 Nacos 控制台创建以下配置：
- `han-note-auth-dev.yaml`
- `han-note-gateway-dev.yaml`
- `han-note-oss-dev.yaml`

### 4. 构建项目

```bash
./gradlew build -x test
```

### 5. 启动服务

按以下顺序启动服务：

```bash
# 1. 启动认证服务
./gradlew :han-note-auth:bootRun

# 2. 启动网关服务
./gradlew :han-note-gateway:bootRun

# 3. 启动OSS服务
./gradlew :han-note-oss:han-note-oss-biz:bootRun
```

## 模块说明

### han-note-auth (认证服务)

用户认证授权服务，提供以下功能：
- 用户登录/注册（验证码登录、密码登录）
- 账号登出
- 密码修改
- 短信验证码发送

**主要接口：**

| 接口 | 方法 | 说明 |
|------|------|------|
| `/user/login` | POST | 用户登录/注册 |
| `/user/logout` | POST | 用户登出 |
| `/user/password/update` | POST | 修改密码 |
| `/verification-code/send` | POST | 发送验证码 |

### han-note-gateway (API网关)

基于 Spring Cloud Gateway 的 API 网关，提供：
- 路由转发
- 统一鉴权
- 请求日志
- 用户ID注入

**路由配置：**

| 路由前缀 | 目标服务 | 说明 |
|----------|----------|------|
| `/auth/**` | han-note-auth | 认证服务 |
| `/oss/**` | han-note-oss | 存储服务 |

### han-note-oss (对象存储服务)

文件存储服务，支持多种存储策略：
- RustFS（自建存储）
- 阿里云 OSS
- 腾讯云 COS

**主要接口：**

| 接口 | 方法 | 说明 |
|------|------|------|
| `/file/upload` | POST | 文件上传 |

## 开发指南

### 构建命令

```bash
# 构建所有模块
./gradlew build

# 构建指定模块
./gradlew :han-note-auth:build

# 编译（不运行测试）
./gradlew build -x test
```

### 测试命令

```bash
# 运行所有测试
./gradlew test

# 运行指定模块测试
./gradlew :han-note-auth:test

# 运行单个测试类
./gradlew :han-note-auth:test --tests "com.hanserwei.hannote.auth.UserServiceTest"

# 运行单个测试方法
./gradlew :han-note-auth:test --tests "com.hanserwei.hannote.auth.UserServiceTest.testLogin"
```

### 代码规范

- 使用 `@Resource` 进行依赖注入
- Controller 返回 `Response<T>` 统一响应
- VO 类使用 Java record
- DO 类使用 Lombok `@Data + @Builder`
- 使用 `@Slf4j` 记录日志
- 类注释包含 `@author hanserwei`

详细规范请参考 [AGENTS.md](./AGENTS.md)

## 架构设计

### 认证流程

```
用户请求 -> Gateway -> Token校验 -> 注入UserId到Header -> 业务服务
```

### 文件存储策略模式

```java
// 通过配置动态切换存储实现
storage:
  type: rustfs  # 可选: rustfs, aliyun, cos
```

## 配置说明

### Sa-Token 配置

```yaml
sa-token:
  token-name: Authorization
  token-prefix: Bearer
  timeout: 2592000  # 30天
  is-concurrent: true
  is-share: true
```

### Nacos 配置

各服务通过 Nacos 进行配置管理，支持动态刷新。

## 许可证

MIT License

## 作者

- hanserwei
- 耄耋爱哈气
- ...
