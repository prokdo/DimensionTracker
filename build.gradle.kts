plugins {
    java
}

group = "ru.prokdo"
version = "1.3.0"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:[26.1.2.build,)")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(25))
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "ru.prokdo.DimensionTracker"
    }
}

tasks.processResources {
    filteringCharset = "UTF-8"
    expand(project.properties)
}