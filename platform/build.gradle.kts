plugins {
    id("java-platform")
}

javaPlatform{
    allowDependencies()
}

dependencies {
    api(platform("org.springframework.boot:spring-boot-dependencies:${springBootVersion}"))
    api(platform("org.springframework.cloud:spring-cloud-dependencies:${springCloudVersion}"))
    api(platform("com.alibaba.cloud:spring-cloud-alibaba-dependencies:${springCloudAlibabaVersion}"))
    api(platform("cn.hutool:hutool-all:${hutoolVersion}"))
    api(platform("cn.dev33:sa-token-dependencies:${saTokenVersion}"))
    
    // 常用工具库版本约束
    constraints {
        api("org.projectlombok:lombok:${lombokVersion}")
        api("org.mybatis.spring.boot:mybatis-spring-boot-starter:${mybatisVersion}")
        api("com.aliyun:alibabacloud-dypnsapi20170525:${aliyunDypnsapiVersion}")
    }
}