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
}
