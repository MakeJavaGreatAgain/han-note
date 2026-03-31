plugins {
    id("java-library")
    id("maven-publish")
}

group = property("group") as String
version = property("version") as String

tasks.jar {
    archiveBaseName.set("han-note-spring-boot-starter-biz-context")
}

dependencies {
    // 平台依赖
    implementation(platform(project(":platform")))
    implementation("org.springframework.boot:spring-boot-starter-webmvc")
    compileOnly(platform(project(":platform")))
    annotationProcessor(platform(project(":platform")))
    // lombok依赖
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    // 通用工具依赖
    implementation(project(":framework:common"))
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifactId = "han-note-spring-boot-starter-biz-context"
            from(components["java"])
        }
    }
}
