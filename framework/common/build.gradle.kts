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
    implementation(platform(project(":platform")))
    api("org.springframework.boot:spring-boot-starter-validation")
    compileOnly(platform(project(":platform")))
    annotationProcessor(platform(project(":platform")))
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    implementation("tools.jackson.core:jackson-core")
    implementation("tools.jackson.core:jackson-databind")
    api("com.google.guava:guava")
    api("org.apache.commons:commons-lang3")
    api("cn.hutool:hutool-all")
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifactId = "han-note-common"
            from(components["java"])
        }
    }
}