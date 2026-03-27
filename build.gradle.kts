import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.jetbrains.kotlin.gradle.targets.js.testing.KotlinJsTest

plugins {
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.kotlinJvm) apply false
    alias(libs.plugins.androidKotlinMultiplatformLibrary) apply false
}

group = "com.hiczp"
version = "0.0.1"

subprojects {
    group = rootProject.group
    version = rootProject.version

    tasks.withType<AbstractTestTask>().configureEach {
        testLogging {
            events("passed", "skipped", "failed")
            showStandardStreams = true
            exceptionFormat = TestExceptionFormat.FULL
            showExceptions = true
            showCauses = true
            showStackTraces = true
        }

        afterSuite(KotlinClosure2({ description: TestDescriptor, result: TestResult ->
            if (description.parent == null) {
                println()
                println("Test Summary: ${result.testCount} tests, ${result.successfulTestCount} passed, ${result.failedTestCount} failed, ${result.skippedTestCount} skipped")
            }
        }))
    }

    tasks.withType<KotlinJsTest>().configureEach {
        if (compilation.wasmTarget == null) {
            useMocha {
                timeout = "20s"
            }
        }
    }
}
