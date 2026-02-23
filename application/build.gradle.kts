import com.hiczp.telegram.bot.buildScript.configureAllTargets

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKotlinMultiplatformLibrary)
}

kotlin {
    configureAllTargets("com.hiczp.telegram.bot.application")

    sourceSets {
        commonMain.dependencies {
            api(project(":client"))
            implementation(libs.kotlin.logging)
            implementation(libs.kotlinx.atomicfu)
            implementation(libs.kotlinx.datetime)
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

        mingwTest.dependencies {
            implementation(libs.ktor.client.curl)
        }

        linuxTest.dependencies {
            implementation(libs.ktor.client.curl)
        }

        appleTest.dependencies {
            implementation(libs.ktor.client.darwin)
        }

        androidNativeTest.dependencies {
            implementation(libs.ktor.client.cio)
        }

        webTest.dependencies {
            implementation(libs.ktor.client.js)
        }
    }
}
