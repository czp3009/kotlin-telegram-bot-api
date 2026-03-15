import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.androidKotlinMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
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

    // JVM
    jvm()

    // Android
    androidLibrary {
        namespace = "com.hiczp.telegram.bot.application.compose.message"
        compileSdk = 36
        minSdk = 34
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    // JS
    js {
        browser()
        nodejs()
    }

    // WASM
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
        nodejs()
        d8()
    }

    compilerOptions {
        freeCompilerArgs.add("-Xcontext-parameters")
    }

    sourceSets {
        commonMain.dependencies {
            api(project(":application"))
            implementation(libs.compose.runtime)
            implementation(libs.kotlin.logging)
            implementation(libs.kotlinx.atomicfu)
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(libs.kotlinx.coroutines.test)
        }

        jvmTest.dependencies {
            implementation(libs.logback.classic)
        }
    }
}
