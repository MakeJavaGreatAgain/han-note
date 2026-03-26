plugins {
    `java-library`
    `maven-publish`
}

group = property("group") as String
version = property("version") as String

tasks.jar {
    archiveBaseName.set("han-note-spring-boot-starter-biz-operationlog")
}

dependencies {
    implementation(platform(project(":platform")))
    compileOnly(platform(project(":platform")))
    annotationProcessor(platform(project(":platform")))
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    implementation("org.springframework.boot:spring-boot-starter-aspectj")
    implementation(project(":framework:common"))
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifactId = "han-note-spring-boot-starter-biz-operationlog"
            from(components["java"])
        }
    }
}