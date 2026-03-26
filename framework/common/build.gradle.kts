plugins {
    `java-library`
    `maven-publish`
}

group = property("group") as String
version = property("version") as String

tasks.jar {
    archiveBaseName.set("han-note-common")
}

dependencies {
    implementation(platform(project(":platform")))
    compileOnly(platform(project(":platform")))
    annotationProcessor(platform(project(":platform")))
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
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