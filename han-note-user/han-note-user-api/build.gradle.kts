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
}
