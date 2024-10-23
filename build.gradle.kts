plugins {
    id("java")
    id("com.gradleup.shadow") version "8.3.0"
    id("xyz.jpenilla.run-paper") version "2.2.3"
}

group = "net.hectus.neobb"
version = "0.0.9"
description = "A better version of Block Battles, where multiple players compete in a turn-based game, similar to chess or card games."

java.sourceCompatibility = JavaVersion.VERSION_21
java.targetCompatibility = JavaVersion.VERSION_21

repositories {
    mavenLocal()
    mavenCentral()

    maven("https://marcpg.com/repo/")
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.xenondevs.xyz/releases")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.1-R0.1-SNAPSHOT")
    implementation("org.postgresql:postgresql:42.7.4")
    implementation("xyz.xenondevs.invui:invui:1.38")
    implementation("com.marcpg:libpg:0.1.2")
}

tasks {
    build {
        dependsOn(shadowJar)
    }
    runServer {
        dependsOn(shadowJar)
        minecraftVersion("1.21.1")
        downloadPlugins {
            url("https://github.com/playit-cloud/playit-minecraft-plugin/releases/latest/download/playit-minecraft-plugin.jar")
        }
    }
    shadowJar {
        archiveClassifier.set("")
        manifest {
            // Required for InvUI to properly work. Sadly, it will make paper remap the plugin, which takes a while.
            attributes["paperweight-mappings-namespace"] = "spigot"
        }
    }
    processResources {
        exclude("en_US.properties")
        filter {
            it.replace("\${version}", version.toString())
        }
    }
}
