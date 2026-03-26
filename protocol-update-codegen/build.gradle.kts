plugins {
    alias(libs.plugins.kotlinJvm)
}

dependencies {
    implementation(project(":protocol-annotation"))
    implementation(libs.ksp.symbol.processing.api)
    implementation(libs.kotlinpoet)
    implementation(libs.kotlinpoet.ksp)
    implementation(libs.kotlinx.serialization.json)
}
