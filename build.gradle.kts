plugins {
    id("java")
    id("idea")

    kotlin("jvm") version "2.1.10"
    kotlin("plugin.serialization") version "2.1.10"

    id("com.gradleup.shadow") version "8.3.6"
    id("xyz.jpenilla.run-paper") version "2.3.1"
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.16"
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

java.sourceCompatibility = JavaVersion.VERSION_21
java.targetCompatibility = JavaVersion.VERSION_21

idea {
    module {
        isDownloadJavadoc = true
        isDownloadSources = true
    }
}

repositories {
    mavenCentral()

    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.xenondevs.xyz/releases/")
    maven("https://marcpg.com/repo/")
}

dependencies {
    paperweight.paperDevBundle("1.21.5-no-moonrise-SNAPSHOT")

    implementation("com.marcpg:libpg-paper:1.0.0")
    implementation("com.marcpg:libpg-storage-database-sql:1.0.0")
    implementation("com.marcpg:libpg-storage-json:1.0.0")

    compileOnly("xyz.xenondevs.invui:invui:1.44")

    compileOnly(kotlin("stdlib"))
    compileOnly(kotlin("reflect"))
    compileOnly("org.jetbrains.kotlinx:kotlinx-serialization-json-jvm:1.8.1")
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
        minecraftVersion("1.21.4")
//        downloadPlugins {
//            url("https://github.com/playit-cloud/playit-minecraft-plugin/releases/latest/download/playit-minecraft-plugin.jar")
//        }get
    }
    shadowJar {
        archiveClassifier.set("")
    }
    processResources {
        filter {
            it.replace("\${version}", version.toString())
        }
    }
}
