# AGENTS.md - Han-Note Project Guide

This document provides guidelines for agentic coding agents working on the han-note project.

## Project Overview

Han-Note is a microservices application built with:
- **Java 25** with Gradle 8.x (Kotlin DSL)
- **Spring Boot 4.0.5** / **Spring Cloud 2025.1.1** / **Spring Cloud Alibaba 2025.1.0.0**
- **PostgreSQL** database with MyBatis
- **Sa-Token** for authentication/authorization
- **Nacos** for service discovery and configuration
- **Redis** for caching

## Build Commands

```bash
# Build all modules
./gradlew build

# Build without tests
./gradlew build -x test

# Clean build
./gradlew clean build

# Compile only
./gradlew compileJava

# Build specific module
./gradlew :han-note-auth:build
./gradlew :han-note-gateway:build
./gradlew :han-note-oss:han-note-oss-biz:build
```

## Test Commands

```bash
# Run all tests
./gradlew test

# Run tests for a specific module
./gradlew :han-note-auth:test
./gradlew :han-note-oss:han-note-oss-biz:test

# Run a single test class
./gradlew :han-note-auth:test --tests "com.hanserwei.hannote.auth.UserServiceTest"

# Run a single test method
./gradlew :han-note-auth:test --tests "com.hanserwei.hannote.auth.UserServiceTest.testLogin"
```

## Run Commands

```bash
# Run auth service
./gradlew :han-note-auth:bootRun

# Run gateway service
./gradlew :han-note-gateway:bootRun

# Run OSS service
./gradlew :han-note-oss:han-note-oss-biz:bootRun
```

## Code Style Guidelines

### Imports

```java
// Import order (separated by blank lines):
// 1. Java standard library
// 2. Third-party libraries (Spring, Guava, etc.)
// 3. Internal project imports (com.hanserwei.*)
// 4. Jakarta/Javax imports
```

- Use fully qualified imports (no wildcards)
- Group imports logically with blank lines between groups

### Formatting

- **Indentation**: 4 spaces (no tabs)
- **Line length**: 120 characters max
- **Braces**: K&R style (opening brace on same line)
- **Blank lines**: One blank line between methods, two between major sections

### Naming Conventions

| Type | Convention | Example |
|------|------------|---------|
| Classes | PascalCase | `UserService`, `UserDO` |
| Methods | camelCase | `getUserById()`, `loginAndRegister()` |
| Constants | UPPER_SNAKE_CASE | `HEADER_USER_ID`, `STRATEGY_TYPE_RUSTFS` |
| Variables | camelCase | `userId`, `userDO` |
| Packages | lowercase | `com.hanserwei.hannote.auth` |

### Suffixes

- **DO**: Data Objects (database entities) - `UserDO`, `RoleDO`
- **VO**: Value Objects (API request/response) - `UserLoginReqVO`, `UpdatePasswordReqVO`
- **Mapper**: MyBatis mappers - `UserDOMapper`, `RoleDOMapper`
- **Service**: Service interfaces - `UserService`
- **ServiceImpl**: Service implementations - `UserServiceImpl`
- **Controller**: REST controllers - `UserController`
- **Properties**: Configuration properties - `AliyunOssProperties`

### Types and Data Structures

- Use **Java records** for VO/DTO classes (immutable data carriers):
```java
public record UserLoginReqVO(
    @NotBlank(message = "手机号不能为空")
    @PhoneNumber
    String phone,
    String password,
    String code,
    @NotNull(message = "登录类型不能为空")
    Integer type
) {}
```

- Use **Lombok @Data + @Builder** for DO classes (database entities):
```java
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDO {
    private Long id;
    private String phone;
    // ...
}
```

- Use **Java 17+ switch expressions** for pattern matching:
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

- Use `@Resource` (not `@Autowired`):
```java
@Resource
private UserService userService;
```

### Exception Handling

- Use `BizException` for business errors:
```java
if (Objects.isNull(userDO)) {
    throw new BizException(ResponseCodeEnum.USER_NOT_FOUND);
}
```

- Use `Preconditions.checkArgument()` for parameter validation:
```java
Preconditions.checkArgument(StringUtils.isNotBlank(code), "验证码不能为空");
```

- Create response code enums implementing `BaseExceptionInterface`:
```java
@Getter
@AllArgsConstructor
public enum ResponseCodeEnum implements BaseExceptionInterface {
    SYSTEM_ERROR("AUTH-10000", "系统错误"),
    USER_NOT_FOUND("AUTH-20003", "该用户不存在");

    private final String errorCode;
    private final String errorMessage;
}
```

### Response Pattern

- Always return `Response<T>` from controller methods:
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

### Controller Pattern

```java
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Resource
    private UserService userService;

    @PostMapping("/login")
    @ApiOperationLog(description = "用户登录/注册")
    public Response<String> login(@Validated @RequestBody UserLoginReqVO vo) {
        return userService.loginAndRegister(vo);
    }
}
```

### Service Pattern

```java
@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserDOMapper userDOMapper;

    @Override
    public Response<String> loginAndRegister(UserLoginReqVO vo) {
        // Business logic
        return Response.success(token);
    }
}
```

### Global Exception Handler

```java
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler({BizException.class})
    @ResponseBody
    public Response<Object> handleBizException(HttpServletRequest request, BizException e) {
        log.warn("{} request fail, errorCode: {}, errorMessage: {}", 
            request.getRequestURI(), e.getErrorCode(), e.getErrorMessage());
        return Response.fail(e);
    }
}
```

## Module Structure

```
han-note/
├── platform/                    # Dependency BOM
├── framework/
│   ├── common/                  # Common utilities, Response, exceptions
│   ├── han-note-spring-boot-starter-biz-context/
│   ├── han-note-spring-boot-starter-jackson/
│   └── han-note-spring-boot-starter-biz-operationlog/
├── han-note-auth/               # Authentication service
├── han-note-gateway/            # API Gateway (WebFlux)
└── han-note-oss/                # Object storage service
    ├── han-note-oss-api/        # API definitions
    └── han-note-oss-biz/        # Business implementation
```

## Key Patterns

### Strategy Pattern (File Storage)

```java
public interface FileStrategy {
    String uploadFile(MultipartFile file, String bucketName);
}

@Configuration
public class FileStrategyFactory {
    @Bean
    @RefreshScope
    public FileStrategy getFileStrategy(@Value("${storage.type}") String strategyType) {
        return switch (strategyType) {
            case STRATEGY_TYPE_RUSTFS -> new RustfsFileStrategy();
            case STRATEGY_TYPE_ALIYUN -> new AliyunOSSFileStrategy();
            default -> throw new IllegalArgumentException("不可用的存储类型");
        };
    }
}
```

### Custom Spring Boot Starter

```java
@AutoConfiguration
public class ContextAutoConfiguration {
    @Bean
    public HeaderUserId2ContextFilter headerUserId2ContextFilter() {
        return new HeaderUserId2ContextFilter();
    }
}
```

Register in `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`.

## Important Notes

- **Author tag**: Include `@author hanserwei` in class Javadocs
- **Logging**: Use `@Slf4j` annotation with `log.info()`, `log.warn()`, `log.error()`
- **Validation**: Use Jakarta validation annotations (`@NotBlank`, `@NotNull`, `@Validated`)
- **Database**: PostgreSQL with MyBatis; use `BIGSERIAL` for auto-increment IDs
- **Gateway**: WebFlux-based, uses reactive patterns with `Mono` and `Flux`
