plugins {
    id("java-platform")
}

javaPlatform{
    allowDependencies()
}

dependencies {
    api(platform("org.springframework.boot:spring-boot-dependencies:4.0.4"))
    api(platform("org.springframework.cloud:spring-cloud-dependencies:2025.1.1"))
    api(platform("com.alibaba.cloud:spring-cloud-alibaba-dependencies:2025.1.0.0"))
}