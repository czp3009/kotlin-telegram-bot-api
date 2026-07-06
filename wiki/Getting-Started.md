# Getting Started

This guide creates a long-polling bot and shows the two dispatcher styles currently available in the application
module.

## Prerequisites

- Kotlin 2.4.0 or compatible project setup
- A Telegram bot token from [@BotFather](https://t.me/BotFather)

## Installation

```kotlin
repositories {
    mavenCentral()
}

dependencies {
    implementation("com.hiczp:telegram-bot-api-application:$version")
}
```

## Echo Bot

`SimpleTelegramEventDispatcher` is useful when one lambda is enough.

```kotlin
import com.hiczp.telegram.bot.application.TelegramBotApplication
import com.hiczp.telegram.bot.application.dispatcher.SimpleTelegramEventDispatcher
import com.hiczp.telegram.bot.protocol.event.MessageEvent

suspend fun main(args: Array<String>) {
    val botToken = args.firstOrNull() ?: error("Bot token is required")

    val dispatcher = SimpleTelegramEventDispatcher { context ->
        val event = context.event
        if (event is MessageEvent) {
            val text = event.message.text ?: return@SimpleTelegramEventDispatcher
            context.client.sendMessage(
                chatId = event.message.chat.id.toString(),
                text = text
            )
        }
    }

    val app = TelegramBotApplication.longPolling(
        botToken = botToken,
        eventDispatcher = dispatcher
    )

    app.start()
    app.join()
}
```

## Command Bot

`HandlerTelegramEventDispatcher` uses a route tree. Routes are filters; `handle` consumes the event.

```kotlin
import com.hiczp.telegram.bot.application.TelegramBotApplication
import com.hiczp.telegram.bot.application.context.action.replyMessage
import com.hiczp.telegram.bot.application.dispatcher.handler.HandlerTelegramEventDispatcher
import com.hiczp.telegram.bot.application.dispatcher.handler.command.command
import com.hiczp.telegram.bot.application.dispatcher.handler.handling

suspend fun main(args: Array<String>) {
    val botToken = args.firstOrNull() ?: error("Bot token is required")

    val routes = handling {
        command("start") {
            handle {
                replyMessage("Welcome!")
            }
        }

        command("ping") {
            handle {
                replyMessage("Pong!")
            }
        }
    }

    val app = TelegramBotApplication.longPolling(
        botToken = botToken,
        eventDispatcher = HandlerTelegramEventDispatcher(routes)
    )

    app.start()
    app.join()
}
```

## Running Samples

```bash
./gradlew :sample:jvmRun --args="YOUR_BOT_TOKEN" -DmainClass="com.hiczp.telegram.bot.sample.basic.echo.EchoBotKt" --quiet
./gradlew :sample:jvmRun --args="YOUR_BOT_TOKEN" -DmainClass="com.hiczp.telegram.bot.sample.basic.command.CommandBotKt" --quiet
```

Running a sample performs real Telegram API calls. Use `./gradlew :sample:assemble` for compile-only verification.

## Basic Configuration

The long-polling factory creates a `TelegramBotClient` and a `LongPollingTelegramUpdateSource` with default settings.

```kotlin
val app = TelegramBotApplication.longPolling(
    botToken = "YOUR_BOT_TOKEN",
    eventDispatcher = dispatcher
)
```

Install interceptors around any dispatcher:

```kotlin
val app = TelegramBotApplication.longPolling(
    botToken = "YOUR_BOT_TOKEN",
    interceptors = listOf(loggingInterceptor(), conversationInterceptor()),
    eventDispatcher = dispatcher
)
```

Use the constructor when you need to configure the client or update source directly:

```kotlin
import com.hiczp.telegram.bot.application.TelegramBotApplication
import com.hiczp.telegram.bot.application.updatesource.LongPollingTelegramUpdateSource
import com.hiczp.telegram.bot.application.updatesource.LongPollingTelegramUpdateSource.ProcessingMode
import com.hiczp.telegram.bot.client.TelegramBotClient

val client = TelegramBotClient(botToken = "YOUR_BOT_TOKEN")
val updateSource = LongPollingTelegramUpdateSource(
    client = client,
    allowedUpdates = listOf("message", "callback_query"),
    processingMode = ProcessingMode.CONCURRENT_BATCH,
    maxPendingUpdates = 50,
    getUpdatesTimeout = 30,
    fastFail = false,
)

val app = TelegramBotApplication(
    client = client,
    updateSource = updateSource,
    interceptors = emptyList(),
    eventDispatcher = dispatcher
)
```

## Processing Modes

| Mode               | Concurrency             | Backpressure                           | Delivery guarantee |
|--------------------|-------------------------|----------------------------------------|--------------------|
| `SEQUENTIAL`       | One update at a time    | Natural flow control                   | At-least-once      |
| `CONCURRENT_BATCH` | Concurrent within batch | Batch-level                            | At-least-once      |
| `CONCURRENT`       | Fully concurrent        | None unless `maxPendingUpdates` is set | At-most-once       |

`CONCURRENT` is the default. Shared mutable state must be synchronized or replaced with thread-safe structures.

## Graceful Shutdown

```kotlin
app.start()

Runtime.getRuntime().addShutdownHook(Thread {
    runBlocking {
        app.stopSuspend(5.seconds)
    }
})

app.join()
```

- `stop(gracePeriod)` starts shutdown and returns a `Job`.
- `stopSuspend(gracePeriod)` waits for the shutdown job and the application scope to finish.
- A `TelegramBotApplication` instance can only be started once. Create a new instance after it has stopped.

## Next Steps

- [Handler DSL](Handler-DSL)
- [Command DSL](Command-DSL)
- [Update Sources](Update-Sources)
- [Interceptors](Interceptors)
- [Conversations](Conversations)
