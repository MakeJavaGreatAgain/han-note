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
    implementation(platform(project(":platform")))
    compileOnly(platform(project(":platform")))
    annotationProcessor(platform(project(":platform")))
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    implementation(project(":framework:common"))
    implementation("tools.jackson.core:jackson-core")
    implementation("tools.jackson.core:jackson-databind")
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