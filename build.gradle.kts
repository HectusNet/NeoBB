plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("xyz.jpenilla.run-paper") version "2.2.3"
}

group = "net.hectus.bb"
version = "0.0.1"
description = "Two people compete in a turn-based match with random blocks which do random things."

java.sourceCompatibility = JavaVersion.VERSION_17
java.targetCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()

    maven("https://marcpg.com/repo/")
    maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    implementation("com.marcpg:libpg:0.1.1")
    implementation("org.postgresql:postgresql:42.7.3")
    implementation("com.github.stefvanschie.inventoryframework:IF:0.10.13")
    compileOnly("io.papermc.paper:paper-api:1.20.4-R0.1-SNAPSHOT")
}

tasks {
    build { dependsOn(shadowJar) }
    processResources {
        filter {
            it.replace("\${version}", version.toString())
        }
    }
    shadowJar {
        relocate("com.github.stefvanschie.inventoryframework", "net.hectus.bb.inventoryframework")
        relocate("com.marcpg.libpg", "net.hectus.bb.libpg")
    }
    runServer {
        minecraftVersion("1.20.4")
    }
}
