plugins {
    alias(libs.plugins.kotlinJvm)
}

repositories {
    mavenCentral()
    google()
}

dependencies {
    implementation(libs.ksp.symbol.processing.api)
    implementation(libs.kotlinpoet)
    implementation(libs.kotlinpoet.ksp)
}
