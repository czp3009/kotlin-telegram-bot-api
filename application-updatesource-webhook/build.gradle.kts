import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKotlinMultiplatformLibrary)
}

kotlin {
    jvmToolchain(17)

    applyDefaultHierarchyTemplate()

    // Windows
    mingwX64()

    // Linux
    linuxArm64()
    linuxX64()

    // macOS
    macosArm64()
    macosX64()

    // iOS
    iosSimulatorArm64()
    iosArm64()
    iosX64()

    // watchOS
    watchosSimulatorArm64()
    watchosArm32()
    watchosArm64()
    watchosX64()

    // tvOS
    tvosSimulatorArm64()
    tvosArm64()
    tvosX64()

    // Android Native
    androidNativeArm32()
    androidNativeArm64()
    androidNativeX64()
    androidNativeX86()

    // JVM
    jvm()

    // Android
    androidLibrary {
        namespace = "com.hiczp.telegram.bot.application.updatesource.webhook"
        compileSdk = 36
        minSdk = 34
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    // JS - HTTP server is not supported in browser
    js {
        nodejs()
    }

    // WASM - HTTP server is not supported in browser and d8
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        nodejs()
    }

    sourceSets {
        commonMain.dependencies {
            api(project(":application"))
            api(libs.ktor.server.core)
            implementation(libs.kotlin.logging)
            implementation(libs.kotlinx.atomicfu)
            implementation(libs.ktor.server.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(libs.kotlinx.coroutines.test)
            implementation(libs.ktor.server.cio)
        }

        jvmTest.dependencies {
            implementation(libs.logback.classic)
        }

        wasmJsTest.dependencies {
            implementation(libs.ktor.server.cio)
        }
    }
}
