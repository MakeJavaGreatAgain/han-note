plugins {
    id("java-library")
    id("maven-publish")
}

group = property("group") as String
version = property("version") as String

tasks.jar {
    archiveBaseName.set("han-note-common")
}

dependencies {
    // 需要传递的依赖
    api("org.springframework.boot:spring-boot-starter-validation")
    api("com.google.guava:guava")
    api("org.apache.commons:commons-lang3")
    api("cn.hutool:hutool-all")
    // 平台依赖
    implementation(platform(project(":platform")))
    compileOnly(platform(project(":platform")))
    annotationProcessor(platform(project(":platform")))
    // lombok依赖
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)
    // jackson依赖
    implementation("tools.jackson.core:jackson-core")
    implementation("tools.jackson.core:jackson-databind")
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifactId = "han-note-common"
            from(components["java"])
        }
    }
}