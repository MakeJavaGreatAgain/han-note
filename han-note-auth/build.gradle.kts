plugins {
    id("org.springframework.boot")
    `java-library`
}

group = property("group") as String
version = property("version") as String

tasks.jar {
    archiveBaseName.set("han-note-auth")
}

dependencies {
    implementation(platform(project(":platform")))
    implementation(project(":framework:common"))
    implementation("org.springframework.boot:spring-boot-starter-webmvc")
    // 业务日志切面
    implementation(project(":framework:han-note-spring-boot-starter-biz-operationlog"))
    implementation("org.mybatis.spring.boot:mybatis-spring-boot-starter")
    runtimeOnly("com.mysql:mysql-connector-j")
}