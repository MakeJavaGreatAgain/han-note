plugins {
    id("java-library")
    id("maven-publish")
}

group = property("group") as String
version = property("version") as String

tasks.jar {
    archiveBaseName.set("han-note-spring-boot-starter-jackson")
}

dependencies {
    // 平台依赖
    implementation(platform(project(":platform")))
    compileOnly(platform(project(":platform")))
    annotationProcessor(platform(project(":platform")))
    // lombok依赖
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)
    // 通用工具依赖
    implementation(project(":framework:common"))
    // jackson依赖
    implementation("tools.jackson.core:jackson-core")
    implementation("tools.jackson.core:jackson-databind")
    // spring boot 自动装配依赖
    implementation("org.springframework.boot:spring-boot-autoconfigure")
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifactId = "han-note-spring-boot-starter-jackson"
            from(components["java"])
        }
    }
}