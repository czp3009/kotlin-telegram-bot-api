plugins {
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.androidKotlinMultiplatformLibrary) apply false
}

group = "com.hiczp"
version = "0.0.1"

subprojects {
    group = rootProject.group
    version = rootProject.version
}
