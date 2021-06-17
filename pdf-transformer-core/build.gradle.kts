plugins {
    kotlin("jvm")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.apache.pdfbox:pdfbox:${Versions.pdfbox}")
    implementation("org.apache.pdfbox:pdfbox-tools:${Versions.pdfbox}")
}
