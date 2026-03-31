plugins {
    alias(libs.plugins.spring.boot)
    id("java-library")
}

group = property("group") as String
version = property("version") as String

tasks.jar {
    archiveBaseName.set("han-note-user-biz")
}

tasks.test {
    useJUnitPlatform()
}

dependencies {
    // Platform
    implementation(platform(project(":platform")))
    annotationProcessor(platform(project(":platform")))

    // Spring Boot Starters
    implementation("org.springframework.boot:spring-boot-starter-webmvc")
    implementation("com.alibaba.cloud:spring-cloud-starter-alibaba-nacos-config")
    implementation("com.alibaba.cloud:spring-cloud-starter-alibaba-nacos-discovery")
    implementation("org.mybatis.spring.boot:mybatis-spring-boot-starter:4.0.1")
    runtimeOnly("org.postgresql:postgresql")

    // Internal Projects
    implementation(project(":framework:common"))
    implementation(project(":framework:han-note-spring-boot-starter-biz-operationlog"))
    implementation(project(":framework:han-note-spring-boot-starter-biz-context"))
    implementation(project(":framework:han-note-spring-boot-starter-jackson"))

    // Lombok
    implementation(libs.lombok)
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)

    // Test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.mybatis.spring.boot:mybatis-spring-boot-starter-test:4.0.1")
}