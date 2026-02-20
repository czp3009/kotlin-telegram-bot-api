import com.hiczp.telegram.bot.buildScript.configureAllTargets
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.androidKotlinMultiplatformLibrary)
}

kotlin {
    configureAllTargets("com.hiczp.telegram.bot.client")

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
