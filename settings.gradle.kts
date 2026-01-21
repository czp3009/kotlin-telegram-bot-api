rootProject.name = "kotlin-telegram-bot-protocol"

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
