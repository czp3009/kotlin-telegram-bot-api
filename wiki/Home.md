# Kotlin Telegram Bot API

A Kotlin Multiplatform library for the [Telegram Bot API](https://core.telegram.org/bots/api). It provides generated
protocol bindings, a configured client, and an application framework for update processing.

## Features

- Type-safe Telegram Bot API models and Ktorfit interfaces generated from OpenAPI
- `TelegramBotClient` with JSON, retry, error handling, long polling, and file download support
- Application lifecycle management with update sources, interceptors, and dispatchers
- Handler DSL built from composable async filters and explicit `handle` blocks
- JVM, Android, JS, WASM, Linux, Windows, Android Native, macOS ARM64, iOS, watchOS, and tvOS targets

## Installation

```kotlin
implementation("com.hiczp:telegram-bot-api-application:$version")
```

Use the webhook update source only when needed:

```kotlin
implementation("com.hiczp:telegram-bot-api-application-updatesource-webhook:$version")
```

## Quick Example

```kotlin
import com.hiczp.telegram.bot.application.TelegramBotApplication
import com.hiczp.telegram.bot.application.context.action.replyMessage
import com.hiczp.telegram.bot.application.dispatcher.handler.HandlerTelegramEventDispatcher
import com.hiczp.telegram.bot.application.dispatcher.handler.command.command
import com.hiczp.telegram.bot.application.dispatcher.handler.handling

suspend fun main() {
    val routes = handling {
        command("start") {
            handle {
                replyMessage("Welcome!")
            }
        }

        command("ping") {
            handle {
                replyMessage("pong")
            }
        }
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
| `:protocol-annotation`              | Annotations used by protocol event codegen  |
| `:protocol-update-codegen`          | KSP processor that generates event classes  |
| `:protocol`                         | Generated Telegram Bot API models and calls |
| `:client`                           | High-level HTTP client wrapper              |
| `:application`                      | Bot application framework                   |
| `:application-updatesource-webhook` | Webhook update source                       |
| `:sample`                           | Runnable sample bots                        |

## Documentation

- [Getting Started](Getting-Started)
- [Architecture](Architecture)
- [Handler DSL](Handler-DSL)
- [Command DSL](Command-DSL)
- [Update Sources](Update-Sources)
- [Interceptors](Interceptors)
- [Conversations](Conversations)
- [File Handling](File-Handling)
- [Webhook](Webhook)
- [Advanced Topics](Advanced-Topics)

## Related Links

- [Telegram Bot API Documentation](https://core.telegram.org/bots/api)
- [OpenAPI Specification Source](https://github.com/czp3009/telegram-bot-api-swagger)
- [Ktorfit](https://github.com/Foso/Ktorfit)
- [Ktor](https://ktor.io/)
