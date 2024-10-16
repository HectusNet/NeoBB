plugins {
    id("java")
    id("com.gradleup.shadow") version "8.3.0"
    id("xyz.jpenilla.run-paper") version "2.2.3"
}

group = "net.hectus.neobb"
version = "0.0.8"
description = "A reworked version of NeoBB with some interesting spins."

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
    compileOnly("io.papermc.paper:paper-api:1.21-R0.1-SNAPSHOT")
    implementation("org.postgresql:postgresql:42.7.4")
    implementation("xyz.xenondevs.invui:invui:1.37")
    implementation("com.marcpg:libpg:0.1.1")
}

tasks {
    build {
        dependsOn(shadowJar)
    }
    runServer {
        dependsOn(shadowJar)
        minecraftVersion("1.21")
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
