# Kotlin Telegram Bot API

> **⚠️ Early Development Warning**
> This project is still in its early stages of development and may contain critical bugs. Use at your own risk.

[![Kotlin](https://img.shields.io/badge/Kotlin-Multiplatform-7F52FF)](https://kotlinlang.org/)

A Kotlin Multiplatform library for the [Telegram Bot API](https://core.telegram.org/bots/api). It provides type-safe,
auto-generated API bindings from the Telegram Bot API OpenAPI specification
using [Ktorfit](https://github.com/Foso/Ktorfit).

## Features

- **Multiplatform**: JVM, Android, JS, WASM, Linux, macOS, Windows, iOS, watchOS, tvOS, Android Native
- **Type-safe**: Auto-generated models and API interfaces from OpenAPI specification
- **Coroutine-based**: Built with Kotlin coroutines for asynchronous operations
- **Modular architecture**: Use only what you need

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

| Platform       | Support |
|----------------|---------|
| JVM            | ✅       |
| Android        | ✅       |
| JS             | ✅       |
| WASM           | ✅       |
| Linux          | ✅       |
| macOS          | ✅       |
| Windows        | ✅       |
| iOS            | ✅       |
| watchOS        | ✅       |
| tvOS           | ✅       |
| Android Native | ✅       |

**Note:** Tests only run on JVM and desktop native targets (Linux, macOS, Windows).

## Documentation

📚 **Full documentation is available in the [Wiki](wiki/).**

## Related Links

- [Telegram Bot API Documentation](https://core.telegram.org/bots/api)
- [OpenAPI Specification Source](https://github.com/czp3009/telegram-bot-api-swagger)
- [Ktorfit](https://github.com/Foso/Ktorfit)
- [Ktor](https://ktor.io/)

## License

MIT License
