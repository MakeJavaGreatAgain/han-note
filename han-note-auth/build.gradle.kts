plugins {
    id("org.springframework.boot")
    id("java-library")
}

group = property("group") as String
version = property("version") as String

tasks.jar {
    archiveBaseName.set("han-note-auth")
}

dependencies {
    implementation(platform(project(":platform")))
    annotationProcessor(platform(project(":platform")))
    implementation(project(":framework:common"))
    implementation("org.springframework.boot:spring-boot-starter-webmvc")
    // 业务日志切面
    implementation(project(":framework:han-note-spring-boot-starter-biz-operationlog"))
    implementation(project(":framework:han-note-spring-boot-starter-jackson"))
    implementation("org.projectlombok:lombok")
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    // Mybatis
    implementation("org.mybatis.spring.boot:mybatis-spring-boot-starter")
    // MySQL驱动
    runtimeOnly("com.mysql:mysql-connector-j")
}