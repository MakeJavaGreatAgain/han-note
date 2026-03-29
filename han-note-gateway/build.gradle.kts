plugins {
    alias(libs.plugins.spring.boot)
    id("java-library")
}

group = property("group") as String
version = property("version") as String

tasks.jar {
    archiveBaseName.set("han-note-gateway")
}

dependencies {
    // Platform
    implementation(platform(project(":platform")))
    annotationProcessor(platform(project(":platform")))

    // Spring Boot Starters
    implementation("com.alibaba.cloud:spring-cloud-starter-alibaba-nacos-config")
    implementation("com.alibaba.cloud:spring-cloud-starter-alibaba-nacos-discovery")
    implementation("org.springframework.cloud:spring-cloud-starter-gateway-server-webflux")
    implementation("org.springframework.cloud:spring-cloud-starter-loadbalancer")

    // Sa-Token
    implementation("cn.dev33:sa-token-reactor-spring-boot4-starter")
    implementation("cn.dev33:sa-token-redis-template")

    // Inner
    implementation(project(":framework:common"))
    implementation(project(":framework:han-note-spring-boot-starter-jackson"))

    // Lombok
    implementation(libs.lombok)
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)

}