plugins {
    alias(libs.plugins.spring.boot)
    id("java-library")
}

group = property("group") as String
version = property("version") as String

tasks.jar {
    archiveBaseName.set("han-note-auth")
}

dependencies {
    // Platform
    implementation(platform(project(":platform")))
    annotationProcessor(platform(project(":platform")))

    // Spring Boot Starters
    implementation("org.springframework.boot:spring-boot-starter-webmvc")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")

    // MyBatis
    implementation(libs.mybatis.spring.boot.starter)
    testImplementation(libs.mybatis.spring.boot.starter.test)

    // Internal Projects
    implementation(project(":framework:common"))
    implementation(project(":framework:han-note-spring-boot-starter-biz-operationlog"))
    implementation(project(":framework:han-note-spring-boot-starter-jackson"))

    // Sa-Token
    implementation("cn.dev33:sa-token-spring-boot4-starter")
    implementation("cn.dev33:sa-token-redis-template")

    // Third-party Libraries
    implementation(libs.aliyun.dypnsapi)

    // Lombok
    implementation(libs.lombok)
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)

    // Database
    runtimeOnly("org.postgresql:postgresql")
}