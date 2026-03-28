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
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.mybatis.spring.boot:mybatis-spring-boot-starter:4.0.1")
    testImplementation("org.mybatis.spring.boot:mybatis-spring-boot-starter-test:4.0.1")
    annotationProcessor(platform(project(":platform")))
    implementation(project(":framework:common"))
    implementation("org.springframework.boot:spring-boot-starter-webmvc")
    // 业务日志切面
    implementation(project(":framework:han-note-spring-boot-starter-biz-operationlog"))
    implementation(project(":framework:han-note-spring-boot-starter-jackson"))
    implementation("org.projectlombok:lombok")
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    // PostgreSQL
    runtimeOnly("org.postgresql:postgresql")
    // sa-token
    implementation("cn.dev33:sa-token-spring-boot4-starter")
    implementation("cn.dev33:sa-token-redis-template")
    // 云通短信
    implementation("com.aliyun:alibabacloud-dypnsapi20170525:2.0.0")
}