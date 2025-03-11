plugins {
    id("java")
    id("com.gradleup.shadow") version "8.3.6"
    id("xyz.jpenilla.run-paper") version "2.3.1"
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.14"
}

group = "net.hectus.neobb"
version = "0.0.9"
description = "A better version of Block Battles, where multiple players compete in a turn-based game, similar to chess or card games."

java.sourceCompatibility = JavaVersion.VERSION_21
java.targetCompatibility = JavaVersion.VERSION_21

repositories {
    mavenCentral()

    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.xenondevs.xyz/releases")
    maven("https://marcpg.com/repo/")
}

dependencies {
    paperweight.paperDevBundle("1.21.4-R0.1-SNAPSHOT")
    implementation("com.marcpg:libpg-paper:1.0.0")

    implementation("org.postgresql:postgresql:42.7.5")
    implementation("com.marcpg:libpg-storage-database-sql:1.0.0")

    compileOnly("xyz.xenondevs.invui:invui:1.44")
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
        downloadPlugins {
            url("https://github.com/playit-cloud/playit-minecraft-plugin/releases/latest/download/playit-minecraft-plugin.jar")
        }
    }
    shadowJar {
        archiveClassifier.set("")
    }
    processResources {
        exclude("en_US.properties")
        filter {
            it.replace("\${version}", version.toString())
        }
    }
}
