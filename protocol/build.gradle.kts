import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.ktorfit)
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
            implementation(libs.ktorfit.lib.light)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.ktor.http)
        }
        
        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.coroutines.test)
            implementation(libs.ktor.client.logging)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.ktorfit.lib)
            implementation(libs.kotlinLogging)
        }
        jvmTest.dependencies { 
            implementation(libs.logback.classic)
            implementation(libs.ktor.client.cio)
        }
        nativeTest.dependencies {
            implementation(libs.ktor.client.curl)
        }
    }
}

ktorfit {
    //https://github.com/Foso/Ktorfit/issues/1015
    compilerPluginVersion.set("2.3.3")
}

dependencies {
    //only run ksp for test
    kotlin.targets.forEach { target ->
        val configName = "ksp${target.name.replaceFirstChar { it.uppercase() }}Test"
        if (configurations.findByName(configName) != null) {
            add(configName, libs.ktorfit.ksp)
        }
    }
}

val downloadSwagger by tasks.registering(DownloadTelegramBotApiSwaggerTask::class)

val generateKtorfitInterfaces by tasks.registering(GenerateKtorfitInterfacesTask::class) {
    swaggerFile.set(downloadSwagger.flatMap { it.outputFile })
    dependsOn(downloadSwagger)
}

tasks.configureEach {
    if (name.startsWith("ksp") && name.contains("KotlinMetadata")) {
        dependsOn(generateKtorfitInterfaces)
    }
}

tasks.withType<KotlinCompilationTask<*>>().configureEach {
    dependsOn(generateKtorfitInterfaces)
}
