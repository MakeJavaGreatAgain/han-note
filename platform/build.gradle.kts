plugins {
    id("java-platform")
}

javaPlatform{
    allowDependencies()
}

val springBoot: String by project
val springCloud: String by project
val springCloudAlibaba: String by project
val hutool: String by project
val saToken: String by project

dependencies {
    api(platform("org.springframework.boot:spring-boot-dependencies:$springBoot"))
    api(platform("org.springframework.cloud:spring-cloud-dependencies:$springCloud"))
    api(platform("com.alibaba.cloud:spring-cloud-alibaba-dependencies:$springCloudAlibaba"))
    api(platform("cn.hutool:hutool-all:$hutool"))
    api(platform("cn.dev33:sa-token-dependencies:$saToken"))
    
    constraints {
        api(libs.lombok)
        api(libs.mybatis.spring.boot.starter)
        api(libs.ttl)
        api(libs.aliyun.dypnsapi)
    }
}
