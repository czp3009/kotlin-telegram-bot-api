plugins {
    alias(libs.plugins.kotlinJvm)
}

repositories {
    mavenCentral()
    google()
}

dependencies {
    implementation(project(":protocol-annotation"))
    implementation(libs.ksp.symbol.processing.api)
    implementation(libs.kotlinpoet)
    implementation(libs.kotlinpoet.ksp)
}
