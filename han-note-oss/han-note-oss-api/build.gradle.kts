plugins {
    id("java-library")
}

group = property("group") as String
version = property("version") as String

tasks.jar {
    archiveBaseName.set("han-note-oss-api")
}

dependencies {
    implementation(platform(project(":platform")))
    implementation(project(":framework:common"))
    implementation("org.springframework.boot:spring-boot-starter-restclient")
    implementation("org.springframework.cloud:spring-cloud-starter-loadbalancer")


}
