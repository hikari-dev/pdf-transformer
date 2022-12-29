plugins {
    kotlin("jvm") version 1.8.0 apply false
}

allprojects {
    group = "dev.hikari"
    version = "1.0.0"
    description = "A simple tool to transform pdf file to png file."

    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}