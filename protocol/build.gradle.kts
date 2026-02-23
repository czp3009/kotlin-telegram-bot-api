import com.hiczp.telegram.bot.buildScript.configureAllTargets
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.androidKotlinMultiplatformLibrary)
    alias(libs.plugins.ksp)
    alias(libs.plugins.ktorfit)
}

kotlin {
    configureAllTargets("com.hiczp.telegram.bot.protocol")

    sourceSets {
        commonMain.dependencies {
            api(project(":protocol-annotation"))
            api(libs.ktorfit.lib.light)
            api(libs.kotlinx.serialization.json)
            api(libs.ktor.client.core)
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.coroutines.test)
            implementation(libs.ktor.client.logging)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.kotlin.logging)
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
        val desktopNativeTargets = setOf("mingwX64", "linuxArm64", "linuxX64", "macosArm64", "macosX64")
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

val downloadSwagger by tasks.registering(DownloadTelegramBotApiSwaggerTask::class)

val generateKtorfitInterfaces by tasks.registering(GenerateKtorfitInterfacesTask::class) {
    swaggerFile.set(downloadSwagger.flatMap { it.outputFile })
    dependsOn(downloadSwagger)
}

// KSP processor for generating TelegramBotEvent classes
dependencies {
    add("kspCommonMainMetadata", project(":protocol-update-codegen"))
}

tasks.configureEach {
    if (name == "kspCommonMainKotlinMetadata") {
        dependsOn(generateKtorfitInterfaces)
    }
}
