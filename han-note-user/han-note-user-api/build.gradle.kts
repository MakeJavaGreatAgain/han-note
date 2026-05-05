plugins {
    id("java-library")
}

group = property("group") as String
version = property("version") as String

tasks.jar {
    archiveBaseName.set("han-note-user-api")
}

dependencies {
    implementation(platform(project(":platform")))
    implementation(project(":framework:common"))
    implementation("org.springframework.boot:spring-boot-starter-restclient")
    implementation("org.springframework.cloud:spring-cloud-starter-loadbalancer")

    //user-biz
    implementation(project(":han-note-user:han-note-user-biz"))
}
