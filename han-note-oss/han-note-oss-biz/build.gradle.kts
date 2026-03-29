plugins {
    alias(libs.plugins.spring.boot)
    id("java-library")
}

group = property("group") as String
version = property("version") as String

tasks.jar {
    archiveBaseName.set("han-note-oss-biz")
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
    // Internal Projects
    implementation(project(":framework:common"))

    // AWS SDK (用于 Rustfs)
    implementation(libs.awssdk)

    // Aliyun OSS SDK
    implementation(libs.aliyun.oss)

    // Lombok
    implementation(libs.lombok)
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)

    // Test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}