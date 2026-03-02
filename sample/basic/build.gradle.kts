import com.hiczp.telegram.bot.buildScript.configureAllTargets

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKotlinMultiplatformLibrary)
}

kotlin {
    configureAllTargets("com.hiczp.telegram.bot.sample.basic")

    sourceSets {
        commonMain.dependencies {
            implementation(project(":application"))
            implementation(libs.kotlin.logging)
        }

        jvmMain.dependencies {
            implementation(libs.logback.classic)
            implementation(libs.ktor.client.cio)
        }

        mingwMain.dependencies {
            implementation(libs.ktor.client.winhttp)
        }

        linuxMain.dependencies {
            implementation(libs.ktor.client.curl)
        }

        appleMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }

        androidNativeMain.dependencies {
            implementation(libs.ktor.client.cio)
        }

        webMain.dependencies {
            implementation(libs.ktor.client.js)
        }
    }
}
