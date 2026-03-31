# Han-Note

基于 Spring Boot 4 与 Spring Cloud 的笔记应用后端微服务项目。

## 项目概览

Han-Note 当前仓库包含网关、认证、用户、对象存储以及若干公共框架模块，采用 Gradle 多模块组织，依赖 Nacos 做配置中心和服务发现，数据库使用 PostgreSQL，缓存使用 Redis。

## 技术栈

| 技术 | 版本 |
|------|------|
| Java | 25 |
| Gradle Wrapper | 9.4.0 |
| Spring Boot | 4.0.5 |
| Spring Cloud | 2025.1.1 |
| Spring Cloud Alibaba | 2025.1.0.0 |
| MyBatis Spring Boot Starter | 4.0.1 |
| Sa-Token | 1.45.0 |
| PostgreSQL | 项目依赖 |
| Redis | 项目依赖 |
| Nacos | 项目依赖 |

## 仓库结构

```text
han-note/
├── platform/                                            # BOM 依赖管理
├── framework/
│   ├── common/                                          # 通用响应、异常、工具类
│   ├── han-note-spring-boot-starter-biz-context/        # 用户上下文 Starter
│   ├── han-note-spring-boot-starter-biz-operationlog/   # 操作日志 Starter
│   └── han-note-spring-boot-starter-jackson/            # Jackson Starter
├── han-note-auth/                                       # 认证服务
├── han-note-gateway/                                    # Gateway 网关
├── han-note-oss/
│   ├── han-note-oss-api/                                # OSS 接口定义
│   └── han-note-oss-biz/                                # OSS 业务实现
├── han-note-user/
│   ├── han-note-user-api/                               # 用户服务接口模块
│   └── han-note-user-biz/                               # 用户服务业务实现
└── sql/                                                 # 初始化脚本
```

## 当前模块

### `han-note-gateway`

- Spring Cloud Gateway + WebFlux
- 接入 Nacos 配置与服务发现
- 使用 Sa-Token Reactor 方案做鉴权
- 网关具体路由规则不在仓库内，按当前实现由 Nacos 配置下发

### `han-note-auth`

- 认证与账号相关能力
- 使用 PostgreSQL、Redis、MyBatis
- 当前可见接口：
  - `POST /login`
  - `POST /logout`
  - `POST /password/update`
  - `POST /verification/code/login/send`
  - `POST /verification/code/updatepassword/send`

### `han-note-user`

- 用户资料维护服务
- 使用 PostgreSQL、MyBatis
- 当前可见接口：
  - `POST /user/update`，`multipart/form-data`

### `han-note-oss`

- 文件上传服务
- 通过策略模式支持多种存储实现
- 当前依赖包含：
  - RustFS / S3 SDK
  - 阿里云 OSS SDK
  - 腾讯云 COS SDK
- 当前可见接口：
  - `POST /file/upload`

### `framework/*`

- 抽取公共响应体、异常、上下文透传、操作日志与 Jackson 配置
- 为业务模块提供统一基础设施

## 本地开发前置条件

- JDK 25
- PostgreSQL
- Redis
- Nacos

建议直接使用仓库自带 Wrapper：

```bash
./gradlew -v
```

## 配置说明

各微服务默认激活 `dev` 环境，并通过 Nacos 加载外部配置。

当前仓库中的服务名如下：

| 服务 | `spring.application.name` |
|------|---------------------------|
| 网关 | `han-note-gateway` |
| 认证服务 | `han-note-auth` |
| 用户服务 | `han-note-user` |
| OSS 服务 | `han-note-oss` |

### Nacos Data ID

按当前 `application.yml` 配置，至少需要准备以下 Data ID：

- `han-note-common-dev.yaml`
- `han-note-db-dev.yaml`
- `han-note-sa-token-dev.yaml`
- `han-note-auth-dev.yaml`
- `han-note-gateway-dev.yaml`
- `han-note-user-dev.yaml`
- `han-note-oss-dev.yaml`

说明：

- `han-note-auth` 依赖 `common`、`db`、`sa-token` 和服务自身配置
- `han-note-gateway` 依赖 `common`、`db`、`sa-token` 和服务自身配置
- `han-note-user` 依赖 `common`、`db` 和服务自身配置
- `han-note-oss` 依赖 `common` 和服务自身配置
- 端口、数据库连接、Redis、Gateway 路由等运行期配置以 Nacos 中的内容为准

## 数据库初始化

仓库已提供初始化脚本：

- `sql/table.sql`
- `sql/data.sql`

执行顺序：

```bash
psql -U <username> -d <database> -f sql/table.sql
psql -U <username> -d <database> -f sql/data.sql
```

当前脚本主要包含：

- 用户表 `t_user`
- 角色表 `t_role`
- 权限表 `t_permission`
- 用户角色关联表 `t_user_role_rel`
- 角色权限关联表 `t_role_permission_rel`

## 构建与测试

```bash
# 构建全部模块
./gradlew build

# 跳过测试构建
./gradlew build -x test

# 清理并构建
./gradlew clean build

# 编译全部模块
./gradlew compileJava

# 运行全部测试
./gradlew test
```

按模块执行：

```bash
./gradlew :han-note-auth:build
./gradlew :han-note-gateway:build
./gradlew :han-note-oss:han-note-oss-biz:build
./gradlew :han-note-user:han-note-user-biz:build

./gradlew :han-note-auth:test
./gradlew :han-note-oss:han-note-oss-biz:test
./gradlew :han-note-user:han-note-user-biz:test
```

## 启动方式

```bash
./gradlew :han-note-auth:bootRun
./gradlew :han-note-gateway:bootRun
./gradlew :han-note-oss:han-note-oss-biz:bootRun
./gradlew :han-note-user:han-note-user-biz:bootRun
```

建议启动顺序：

1. PostgreSQL / Redis / Nacos
2. `han-note-auth`
3. `han-note-user`
4. `han-note-oss`
5. `han-note-gateway`

## 开发约定

- 统一使用 `@Resource` 注入依赖
- Controller 统一返回 `Response<T>`
- VO/DTO 优先使用 `record`
- DO 使用 Lombok 建模
- 使用 MyBatis 进行数据库访问
- 类注释保留 `@author hanserwei`

更详细的开发规范见 [AGENTS.md](./AGENTS.md)。
