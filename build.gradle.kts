plugins {
    java
}

group = "ru.prokdo"
version = providers.gradleProperty("releaseTag").map { tag ->
    require(Regex("""v\d+\.\d+\.\d+""").matches(tag)) {
        "Invalid release tag '$tag': expected vMAJOR.MINOR.PATCH"
    }
    tag.removePrefix("v")
}.orElse(providers.gradleProperty("devCommit").map { "$it-dev" }).getOrElse("local-dev")

val apiVersion = "1.21"
val javaVersion = 21

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/") {
        content {
            includeModule("io.papermc.paper", "paper-api")
            includeModule("com.mojang", "brigadier")
            includeModule("net.md-5", "bungeecord-chat")
        }
    }
    maven("https://repo.helpch.at/releases") {
        content {
            includeModule("me.clip", "placeholderapi")
        }
    }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:$apiVersion-R0.1-SNAPSHOT")
    compileOnly("me.clip:placeholderapi:2.12.3")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(javaVersion))
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    options.release.set(javaVersion)
}

tasks.processResources {
    filteringCharset = "UTF-8"
    val pluginMetadata = mapOf("version" to project.version.toString(), "apiVersion" to apiVersion)
    inputs.properties(pluginMetadata)
    filesMatching("plugin.yml") {
        expand(pluginMetadata)
    }
}
