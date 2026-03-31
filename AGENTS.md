# AGENTS.md - Han-Note Project Guide

Guidelines for agentic coding agents working on the han-note project.

## Project Overview

Han-Note is a microservices application built with:
- **Java 25** with Gradle 8.x (Kotlin DSL)
- **Spring Boot 4.0.5** / **Spring Cloud 2025.1.1** / **Spring Cloud Alibaba 2025.1.0.0**
- **PostgreSQL** with MyBatis / **Redis** for caching
- **Sa-Token** for authentication / **Nacos** for service discovery and configuration

## Build Commands

```bash
./gradlew build                  # Build all modules
./gradlew build -x test          # Build without tests
./gradlew clean build            # Clean build
./gradlew compileJava            # Compile only
./gradlew :han-note-auth:build   # Build specific module
```

## Test Commands

```bash
./gradlew test                                    # Run all tests
./gradlew :han-note-auth:test                     # Run tests for specific module
./gradlew :han-note-auth:test --tests "com.hanserwei.hannote.auth.UserServiceTest"          # Single test class
./gradlew :han-note-auth:test --tests "com.hanserwei.hannote.auth.UserServiceTest.testLogin" # Single test method
```

## Run Commands

```bash
./gradlew :han-note-auth:bootRun                    # Auth service
./gradlew :han-note-gateway:bootRun                 # Gateway service
./gradlew :han-note-oss:han-note-oss-biz:bootRun    # OSS service
./gradlew :han-note-user:han-note-user-biz:bootRun  # User service
```

## Module Structure

```
han-note/
├── platform/                    # Dependency BOM
├── framework/
│   ├── common/                  # Response, exceptions, utilities
│   ├── han-note-spring-boot-starter-biz-context/
│   ├── han-note-spring-boot-starter-jackson/
│   └── han-note-spring-boot-starter-biz-operationlog/
├── han-note-auth/               # Authentication service
├── han-note-gateway/            # API Gateway (WebFlux)
├── han-note-oss/                # Object storage service
│   ├── han-note-oss-api/
│   └── han-note-oss-biz/
└── han-note-user/               # User service
    ├── han-note-user-api/
    └── han-note-user-biz/
```

## Code Style Guidelines

### Formatting

- **Indentation**: 4 spaces (no tabs)
- **Line length**: 120 characters max
- **Braces**: K&R style (opening brace on same line)
- **Blank lines**: One between methods, two between major sections

### Imports

- Use fully qualified imports (no wildcards)
- Order: Java standard → Third-party → Internal project (com.hanserwei.*) → Jakarta

### Naming Conventions

| Type | Convention | Example |
|------|------------|---------|
| Classes | PascalCase | `UserService`, `UserDO` |
| Methods | camelCase | `getUserById()`, `loginAndRegister()` |
| Constants | UPPER_SNAKE_CASE | `HEADER_USER_ID`, `STRATEGY_TYPE_RUSTFS` |
| Variables | camelCase | `userId`, `userDO` |

### Suffixes

- **DO**: Data Objects (database entities) - `UserDO`, `RoleDO`
- **VO**: Value Objects (API request/response) - `UserLoginReqVO`, `UpdatePasswordReqVO`
- **Mapper**: MyBatis mappers - `UserDOMapper`
- **Service/ServiceImpl**: Service interfaces and implementations
- **Controller**: REST controllers - `UserController`
- **Properties**: Configuration properties - `AliyunOssProperties`

### Types and Data Structures

- **Java records** for VO/DTO classes:
```java
public record UserLoginReqVO(
    @NotBlank(message = "手机号不能为空") @PhoneNumber String phone,
    String password,
    String code,
    @NotNull(message = "登录类型不能为空") Integer type
) {}
```

- **Lombok @Data + @Builder** for DO classes:
```java
@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class UserDO {
    private Long id;
    private String phone;
}
```

- **Java 17+ switch expressions** for pattern matching:
```java
Response<?> result = switch (ex) {
    case NotLoginException e -> {
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        yield Response.fail(ResponseCodeEnum.UNAUTHORIZED.getErrorCode(), ex.getMessage());
    }
    default -> Response.fail(ResponseCodeEnum.SYSTEM_ERROR);
};
```

### Dependency Injection

Use `@Resource` (not `@Autowired`):
```java
@Resource
private UserService userService;
```

### Exception Handling

```java
// Business errors - use BizException with enum
if (Objects.isNull(userDO)) {
    throw new BizException(ResponseCodeEnum.USER_NOT_FOUND);
}

// Parameter validation - use Preconditions
Preconditions.checkArgument(StringUtils.isNotBlank(code), "验证码不能为空");

// Response code enum
@Getter @AllArgsConstructor
public enum ResponseCodeEnum implements BaseExceptionInterface {
    SYSTEM_ERROR("AUTH-10000", "系统错误"),
    USER_NOT_FOUND("AUTH-20003", "该用户不存在");
    private final String errorCode;
    private final String errorMessage;
}
```

### Response Pattern

Always return `Response<T>` from controller methods:
```java
@PostMapping("/login")
public Response<String> login(@Validated @RequestBody UserLoginReqVO vo) {
    return userService.loginAndRegister(vo);
}

@GetMapping("/detail")
public Response<UserDetailVO> getDetail() {
    return Response.success(userService.getDetail());
}
```

### Controller/Service Pattern

```java
@RestController @RequestMapping("/user") @Slf4j
public class UserController {
    @Resource
    private UserService userService;

    @PostMapping("/login")
    @ApiOperationLog(description = "用户登录/注册")
    public Response<String> login(@Validated @RequestBody UserLoginReqVO vo) {
        return userService.loginAndRegister(vo);
    }
}

@Slf4j @Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserDOMapper userDOMapper;

    @Override
    public Response<String> loginAndRegister(UserLoginReqVO vo) {
        return Response.success(token);
    }
}
```

## Important Notes

- **Author tag**: Include `@author hanserwei` in class Javadocs
- **Logging**: Use `@Slf4j` with `log.info()`, `log.warn()`, `log.error()`
- **Validation**: Use Jakarta annotations (`@NotBlank`, `@NotNull`, `@Validated`)
- **Database**: PostgreSQL with MyBatis; use `BIGSERIAL` for auto-increment IDs
- **Gateway**: WebFlux-based, uses reactive patterns with `Mono` and `Flux`
