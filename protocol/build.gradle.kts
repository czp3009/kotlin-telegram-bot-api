import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinSerialization)
}

@Suppress("OPT_IN_USAGE")
kotlin {
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

    // JS
    js {
        browser()
        nodejs()
    }

    // WASM
    wasmJs {
        browser()
        nodejs()
        d8()
    }

    sourceSets {
        commonMain.dependencies {
            // Only depend on ktorfit-lib-light, no KSP code generation needed
            implementation(libs.ktorfit.lib.light)
            implementation(libs.kotlinx.serialization.json)
        }
    }
}

val downloadSwagger by tasks.registering(DownloadTelegramBotApiSwaggerTask::class)

val generateKtorfitInterfaces by tasks.registering(GenerateKtorfitInterfacesTask::class) {
    swaggerFile.set(downloadSwagger.flatMap { it.outputFile })
    dependsOn(downloadSwagger)
}

tasks.withType<KotlinCompilationTask<*>>().configureEach {
    dependsOn(generateKtorfitInterfaces)
}
