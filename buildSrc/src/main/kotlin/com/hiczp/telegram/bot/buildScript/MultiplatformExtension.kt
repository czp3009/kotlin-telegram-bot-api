@file:Suppress("UnusedReceiverParameter")

package com.hiczp.telegram.bot.buildScript

import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget
import org.gradle.api.plugins.ExtensionAware
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

/**
 * Configure all supported Kotlin multiplatform targets for the Telegram Bot API project.
 *
 * This includes:
 * - JVM (toolchain 21)
 * - Android Library (minSdk 34, compileSdk 36, JVM 17)
 * - JS (browser, nodejs)
 * - WASM (browser, nodejs, d8)
 * - Native targets: Windows, Linux, macOS, iOS, watchOS, tvOS, Android Native
 *
 * @param namespace The namespace for the Android library configuration.
 *
 * Note: Uses inline function to leverage caller's classpath for compileOnly dependencies.
 */
@Suppress("OPT_IN_USAGE", "NOTHING_TO_INLINE")
inline fun KotlinMultiplatformExtension.configureAllTargets(namespace: String) {
    jvmToolchain(21)

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

    // Android - use ExtensionAware.configure to access the androidLibrary extension
    (this as ExtensionAware).configure<KotlinMultiplatformAndroidLibraryTarget> {
        this.namespace = namespace
        this.compileSdk = 36
        this.minSdk = 34
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
}
