# Kotlin Telegram Bot API

> **Early Development Warning**
> This project is still in its early stages of development and may contain critical bugs. Use at your own risk.

[![Kotlin](https://img.shields.io/badge/Kotlin-Multiplatform-7F52FF)](https://kotlinlang.org/)

A Kotlin Multiplatform library for the [Telegram Bot API](https://core.telegram.org/bots/api). It provides type-safe,
auto-generated API bindings from the Telegram Bot API OpenAPI specification
using [Ktorfit](https://github.com/Foso/Ktorfit).

## Features

- **Multiplatform**: JVM, Android, JS, WASM, Linux, macOS ARM64, Windows, iOS, watchOS ARM/simulator ARM64, tvOS
  ARM/simulator ARM64, Android Native
- **Type-safe**: Auto-generated models and API interfaces from OpenAPI specification
- **Coroutine-based**: Built with Kotlin coroutines for asynchronous operations
- **Modular architecture**: Use only what you need

## Current Toolchain

| Component        | Version |
|------------------|---------|
| Kotlin           | 2.4.0   |
| Gradle           | 9.6.1   |
| Ktor             | 3.5.1   |
| Ktorfit          | 2.7.5   |
| Telegram Bot API | 10.1    |

## Installation

```kotlin
// build.gradle.kts
implementation("com.hiczp:telegram-bot-api-application:$version")
```

## Quick Example

```kotlin
import com.hiczp.telegram.bot.application.TelegramBotApplication
import com.hiczp.telegram.bot.application.dispatcher.handler.HandlerTelegramEventDispatcher
import com.hiczp.telegram.bot.application.dispatcher.handler.handling
import com.hiczp.telegram.bot.application.dispatcher.handler.command.commandEndpoint

suspend fun main() {
    val routes = handling {
        commandEndpoint("start") { replyMessage("Welcome!") }
        commandEndpoint("ping") { replyMessage("pong") }
    }

    val app = TelegramBotApplication.longPolling(
        botToken = "YOUR_BOT_TOKEN",
        eventDispatcher = HandlerTelegramEventDispatcher(routes)
    )

    app.start()
    app.join()
}
```

## Modules

| Module                              | Description                                 |
|-------------------------------------|---------------------------------------------|
| `:protocol`                         | Auto-generated Telegram Bot API definitions |
| `:client`                           | High-level HTTP client wrapper              |
| `:application`                      | Bot framework with lifecycle management     |
| `:application-updatesource-webhook` | Webhook update source                       |

## Supported Platforms

| Platform       | Targets                                                                            |
|----------------|------------------------------------------------------------------------------------|
| JVM            | JVM 17 toolchain                                                                   |
| Android        | Android Kotlin Multiplatform library                                               |
| JS             | Browser and Node.js                                                                |
| WASM           | Browser, Node.js, and d8                                                           |
| Linux          | `linuxX64`, `linuxArm64`                                                           |
| macOS          | `macosArm64`                                                                       |
| Windows        | `mingwX64`                                                                         |
| iOS            | `iosArm64`, `iosSimulatorArm64`                                                    |
| watchOS        | `watchosArm32`, `watchosArm64`, `watchosSimulatorArm64`                            |
| tvOS           | `tvosArm64`, `tvosSimulatorArm64`                                                  |
| Android Native | `androidNativeArm32`, `androidNativeArm64`, `androidNativeX64`, `androidNativeX86` |

**Note:** Some test tasks can make real Telegram API calls. Use `./gradlew assemble` for build-only verification.

## Documentation

Start with the module READMEs:

- [Protocol](protocol/README.md)
- [Client](client/README.md)
- [Application](application/README.md)
- [Webhook update source](application-updatesource-webhook/README.md)
- [Samples](sample/README.md)

## Related Links

- [Telegram Bot API Documentation](https://core.telegram.org/bots/api)
- [OpenAPI Specification Source](https://github.com/czp3009/telegram-bot-api-swagger)
- [Ktorfit](https://github.com/Foso/Ktorfit)
- [Ktor](https://ktor.io/)

## License

MIT License
