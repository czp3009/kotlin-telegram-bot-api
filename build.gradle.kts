import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.KotlinMultiplatform
import com.vanniktech.maven.publish.MavenPublishBaseExtension
import com.vanniktech.maven.publish.MavenPublishPlugin
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.jetbrains.kotlin.gradle.targets.js.testing.KotlinJsTest

plugins {
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.kotlinJvm) apply false
    alias(libs.plugins.androidKotlinMultiplatformLibrary) apply false
    alias(libs.plugins.mavenPublish) apply false
}

group = "com.hiczp"
version = "0.0.1"

val kotlinMultiplatformPluginId = libs.plugins.kotlinMultiplatform.get().pluginId

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
    }

    tasks.withType<KotlinJsTest>().configureEach {
        if (compilation.wasmTarget == null) {
            useMocha {
                timeout = "20s"
            }
        }
    }
}

subprojects {
    if (name !in listOf("protocol", "client", "application", "application-updatesource-webhook")) return@subprojects

    apply<MavenPublishPlugin>()

    configure<MavenPublishBaseExtension> {
        coordinates(project.group.toString(), "telegram-bot-api-${project.name}", project.version.toString())
        publishToMavenCentral()
        signAllPublications()

        pom {
            name = project.name
            description = "A Kotlin Multiplatform library for the Telegram Bot API"
            inceptionYear = "2026"
            url = "https://github.com/czp3009/kotlin-telegram-bot-api"
            licenses {
                license {
                    name = "MIT"
                    url = "https://opensource.org/licenses/MIT"
                    distribution = "https://opensource.org/licenses/MIT"
                }
            }
            developers {
                developer {
                    id = "czp3009"
                    name = "czp3009"
                    email = "czp3009@gmail.com"
                    url = "https://github.com/czp3009"
                }
            }
            scm {
                url = "https://github.com/czp3009/kotlin-telegram-bot-api"
                connection = "scm:git:git://github.com/czp3009/kotlin-telegram-bot-api.git"
                developerConnection = "scm:git:ssh://git@github.com/czp3009/kotlin-telegram-bot-api.git"
            }
        }
    }

    pluginManager.withPlugin(kotlinMultiplatformPluginId) {
        configure<MavenPublishBaseExtension> {
            //TODO may generate javadoc with Dokka
            configure(KotlinMultiplatform(javadocJar = JavadocJar.Empty()))
        }
    }
}
