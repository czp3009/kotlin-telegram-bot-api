plugins {
    alias(libs.plugins.kotlinJvm)
}

repositories {
    mavenCentral()
    google()
    gradlePluginPortal()
}

dependencies {
    implementation(libs.jackson.databind)
    implementation(libs.kotlinpoet)
    // compileOnly for type access without causing version conflicts
    compileOnly(gradleKotlinDsl())
    compileOnly(libs.kotlin.gradle.plugin)
    compileOnly(libs.android.kotlin.multiplatform.library.plugin)
}
