rootProject.name = "kotlin-telegram-bot-api"

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
    }
}

dependencyResolutionManagement {
    @Suppress("UnstableApiUsage")
    repositories {
        mavenCentral()
        google()
    }
}

include(":protocol-annotation")
include(":protocol")
include(":protocol-update-codegen")
include(":client")
include(":application")
