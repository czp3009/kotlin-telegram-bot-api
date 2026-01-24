rootProject.name = "kotlin-telegram-bot-api"

pluginManagement {
    repositories {
        mavenCentral()
    }
}

dependencyResolutionManagement {
    @Suppress("UnstableApiUsage")
    repositories {
        mavenCentral()
    }
}

include(":protocol")
