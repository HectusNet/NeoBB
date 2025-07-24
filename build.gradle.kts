plugins {
    id("java")

    kotlin("jvm") version "2.2.0"
    kotlin("plugin.serialization") version "2.2.0"

    id("com.gradleup.shadow") version "8.3.8"
    id("xyz.jpenilla.run-paper") version "2.3.1"
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.18"
}

group = "net.hectus.neobb"
version = "0.1.0"
description = "A better version of Block Battles, where multiple players compete in a turn-based game, similar to chess or card games."

kotlin {
    jvmToolchain(21)
    compilerOptions {
        freeCompilerArgs.add("-opt-in=kotlin.time.ExperimentalTime")
    }
}

repositories {
    mavenLocal()
    mavenCentral()

    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.xenondevs.xyz/releases/")
    maven("https://marcpg.com/repo/")
}

dependencies {
    paperweight.paperDevBundle("1.21.8-R0.1-SNAPSHOT")

    implementation("com.marcpg:ktlibpg-platform-adventure:2.0.0")
    implementation("com.marcpg:ktlibpg-platform-brigadier:2.0.0")
    implementation("com.marcpg:ktlibpg-platform-paper:2.0.0")
    implementation("com.marcpg:ktlibpg-storage-database-sql:2.0.0")
    implementation("com.marcpg:ktlibpg-storage-json:2.0.0")

    compileOnly("xyz.xenondevs.invui:invui:1.46")

    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))
    compileOnly("org.jetbrains.kotlinx:kotlinx-serialization-json-jvm:1.9.0")
}

tasks {
    reobfJar {
        dependsOn(jar)
    }
    build {
        dependsOn(shadowJar, reobfJar)
    }
    runServer {
        dependsOn(shadowJar)
        minecraftVersion("1.21.8")
    }
    shadowJar {
        archiveClassifier.set("")
    }
}
