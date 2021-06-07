plugins {
    kotlin("jvm") version "1.5.10"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.apache.pdfbox:pdfbox:3.0.0-RC1")
    implementation("org.apache.pdfbox:pdfbox-tools:3.0.0-RC1")
}
