plugins {
    alias(libs.plugins.spring.boot) apply false
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