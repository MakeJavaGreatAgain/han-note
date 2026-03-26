plugins{
    id("org.springframework.boot") version "4.0.4" apply false
}

allprojects {
    group = property("group") as String
    version = property("version") as String
    
    repositories {
        mavenCentral()
    }
}

configure(subprojects.filter { it.name != "platform" }) {
    apply(plugin = "java")
    
    configure<JavaPluginExtension> {
        toolchain {
            languageVersion = JavaLanguageVersion.of(25)
        }
    }
}