pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven("https://maven.neoforged.net/releases")
        maven("https://maven.fabricmc.net/")
        maven("https://maven.kikugie.dev/releases")
        maven("https://repo.spongepowered.org/repository/maven-public/")
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
    id("dev.kikugie.stonecutter") version "0.9.8"
}

stonecutter {
    create(rootProject) {
        version("1.21.1-neoforge", "1.21.1").buildscript("build.neoforge.gradle.kts")
        version("1.20.1-forge", "1.20.1").buildscript("build.forge.gradle.kts")
        version("1.20.1-fabric", "1.20.1").buildscript("build.fabric.gradle.kts")

        vcsVersion = "1.21.1-neoforge"
    }
}

rootProject.name = "nowheel"
