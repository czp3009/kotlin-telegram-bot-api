rootProject.name = "kotlin-telegram-bot-api"

pluginManagement {
    repositories {
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

include(":protocol")
