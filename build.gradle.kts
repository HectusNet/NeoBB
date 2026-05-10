plugins {
    java

    alias(libs.plugins.kotlin)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.shadow)

    alias(libs.plugins.jpnilla.runPaper)
    alias(libs.plugins.paperweight.userdev)

    alias(libs.plugins.versionCatalogueUpdate)
}

group = "net.hectus.neobb"
version = "0.2.2"
description = "A better version of Block Battles, where multiple players compete in a turn-based game, similar to chess or card games."

versionCatalogUpdate {
    pin { // Don't auto-update these.
        versions.add("invui")
        versions.add("kotlin")
        versions.add("paper")
    }
    keep { // Don't auto-remove these.
        versions.add("paper")
    }
}

kotlin {
    jvmToolchain(21)
//    compilerOptions {
//        freeCompilerArgs.add("-opt-in=kotlin.time.ExperimentalTime")
//    }
}

repositories {
    mavenLocal()
    mavenCentral()

//    maven("https://marcpg.com/repo/")
    maven("https://repo.faststats.dev/releases/")
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.xenondevs.xyz/releases/")
}

dependencies {
    paperweight.paperDevBundle(libs.versions.paper.get())

    implementation(libs.ktlibpg.paper)
    implementation(libs.ktlibpg.storage.sql)

    compileOnly(libs.invui)
    compileOnly(libs.faststats)

    compileOnly(kotlin("stdlib"))
    implementation(kotlin("reflect"))
    implementation(libs.kotlin.serialization.json)
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
        relocate("dev.faststats", "$group.libs.faststats")
    }
}
