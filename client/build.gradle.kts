import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKotlinMultiplatformLibrary)
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

    // Android
    androidLibrary {
        namespace = "com.hiczp.telegram.bot.client"
        compileSdk = 36
        minSdk = 34
        compilerOptions {
            jvmTarget = JvmTarget.JVM_17
        }
    }

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
            api(project(":protocol"))
            api(libs.kotlinx.coroutines.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(libs.kotlinx.coroutines.test)
            implementation(libs.ktor.client.logging)
        }

        jvmTest.dependencies {
            implementation(libs.logback.classic)
            implementation(libs.ktor.client.cio)
        }

        val commonTest by getting
        val otherTest by creating {
            dependsOn(commonTest)
        }

        //only support running unit test on jvm
        targets.configureEach {
            val testCompilation = compilations.findByName("test") ?: return@configureEach
            val testSourceSet = testCompilation.defaultSourceSet
            if (platformType != KotlinPlatformType.jvm) {
                testSourceSet.dependsOn(otherTest)
            }
        }
    }
}

val generateTelegramBotEvent by tasks.registering(GenerateTelegramBotEventTask::class) {
    protocolSourceDir.set(project(":protocol").layout.projectDirectory.dir("src/commonMain/kotlin"))
    outputDir.set(layout.projectDirectory.dir("src/commonMain/kotlin"))
    dependsOn(project(":protocol").tasks.named("generateKtorfitInterfaces"))
}

tasks.withType<KotlinCompilationTask<*>>().configureEach {
    dependsOn(generateTelegramBotEvent)
}
