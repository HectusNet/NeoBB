plugins {
    id("java")
    id("io.github.goooler.shadow") version "8.1.8"
    id("xyz.jpenilla.run-paper") version "2.2.3"
}

group = "net.hectus.neobb"
version = "0.0.4"
description = "A reworked version of NeoBB with some interesting spins."

java.sourceCompatibility = JavaVersion.VERSION_21
java.targetCompatibility = JavaVersion.VERSION_21

repositories {
    mavenLocal()
    mavenCentral()

    maven("https://marcpg.com/repo/")
    maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://maven.enginehub.org/repo")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21-R0.1-SNAPSHOT")
    implementation("org.postgresql:postgresql:42.7.3")
    implementation("com.github.stefvanschie.inventoryframework:IF:0.10.17")
    implementation("com.marcpg:libpg:0.1.1")
}

tasks {
    build {
        dependsOn(shadowJar)
    }
    runServer {
        dependsOn(shadowJar)
        minecraftVersion("1.21")
        downloadPlugins {
            url("https://github.com/playit-cloud/playit-minecraft-plugin/releases/latest/download/playit-minecraft-plugin.jar")
            modrinth("1u6JkXh5", "Jo76t1oi")
        }
    }
    shadowJar {
        archiveClassifier.set("")
        manifest {
            // This is just so paper won't remap the plugin.
            attributes["paperweight-mappings-namespace"] = "mojang"
        }
    }
    processResources {
        exclude("en_US.properties")
        filter {
            it.replace("\${version}", version.toString())
        }
    }
}
