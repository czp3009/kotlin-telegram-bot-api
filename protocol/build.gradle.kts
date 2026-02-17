import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType
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

    val desktopNativeTargets = setOf("mingwX64", "linuxArm64", "linuxX64", "macosArm64", "macosX64")

    sourceSets {
        commonMain.dependencies {
            api(libs.ktorfit.lib.light)
            api(libs.kotlinx.serialization.json)
            api(libs.ktor.http)
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

        val commonTest by getting
        val desktopNativeTest by creating {
            dependsOn(commonTest)
            dependencies {
                implementation(libs.ktor.client.curl)
            }
        }
        val otherTest by creating {
            dependsOn(commonTest)
        }

        //only support running unit test on jvm and desktopNative
        targets.configureEach {
            val testCompilation = compilations.findByName("test") ?: return@configureEach
            val testSourceSet = testCompilation.defaultSourceSet
            if (platformType == KotlinPlatformType.native && name in desktopNativeTargets) {
                testSourceSet.dependsOn(desktopNativeTest)
            } else if (platformType != KotlinPlatformType.jvm) {
                testSourceSet.dependsOn(otherTest)
            }
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
