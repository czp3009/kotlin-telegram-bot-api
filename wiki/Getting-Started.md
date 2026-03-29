# Getting Started

This guide will help you set up and create your first Telegram bot.

## Prerequisites

- Kotlin 2.0+ (project uses Kotlin 2.3.10)
- A Telegram bot token (obtain from [@BotFather](https://t.me/BotFather))

## Installation

```kotlin
repositories {
    mavenCentral()
}

dependencies {
    implementation("com.hiczp:telegram-bot-api-application:$version")
}
```

## Your First Bot

### Echo Bot

```kotlin
import com.hiczp.telegram.bot.application.TelegramBotApplication
import com.hiczp.telegram.bot.application.dispatcher.SimpleTelegramEventDispatcher
import com.hiczp.telegram.bot.protocol.event.MessageEvent

suspend fun main(args: Array<String>) {
    val botToken = args.firstOrNull() ?: error("Bot token is required")

  val eventDispatcher = SimpleTelegramEventDispatcher { context ->
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
    eventDispatcher = eventDispatcher
    )

    app.start()
    app.join()
}
```

### Command Bot

```kotlin
import com.hiczp.telegram.bot.application.TelegramBotApplication
import com.hiczp.telegram.bot.application.dispatcher.handler.HandlerTelegramEventDispatcher
import com.hiczp.telegram.bot.application.dispatcher.handler.handling
import com.hiczp.telegram.bot.application.dispatcher.handler.command.commandEndpoint

suspend fun main(args: Array<String>) {
    val botToken = args.firstOrNull() ?: error("Bot token is required")

    val routes = handling {
        commandEndpoint("start") { replyMessage("Welcome!") }
        commandEndpoint("ping") { replyMessage("Pong!") }
    }

    val app = TelegramBotApplication.longPolling(
        botToken = botToken,
        eventDispatcher = HandlerTelegramEventDispatcher(routes)
    )

    app.start()
    app.join()
}
```

## Running Your Bot

```bash
# Build
./gradlew build

# Run with JVM
./gradlew :your-module:jvmRun --args="YOUR_BOT_TOKEN" --quiet
```

### Running Samples

```bash
# EchoBot
./gradlew :sample:jvmRun --args="YOUR_BOT_TOKEN" -DmainClass="com.hiczp.telegram.bot.sample.basic.echo.EchoBotKt" --quiet
```

## Configuration

### Basic Long Polling

```kotlin
val app = TelegramBotApplication.longPolling(
    botToken = "YOUR_BOT_TOKEN",
    eventDispatcher = dispatcher
)
```

### With Interceptors

```kotlin
import com.hiczp.telegram.bot.application.interceptor.builtin.logging.loggingInterceptor
import com.hiczp.telegram.bot.application.interceptor.builtin.conversation.conversationInterceptor

val app = TelegramBotApplication.longPolling(
    botToken = "YOUR_BOT_TOKEN",
    interceptors = listOf(loggingInterceptor(), conversationInterceptor()),
    eventDispatcher = HandlerTelegramEventDispatcher(routes)
)
```

### Advanced Configuration

```kotlin
import com.hiczp.telegram.bot.client.TelegramBotClient
import com.hiczp.telegram.bot.application.updatesource.LongPollingTelegramUpdateSource

val client = TelegramBotClient(botToken = "YOUR_BOT_TOKEN")

val updateSource = LongPollingTelegramUpdateSource(
    client = client,
    allowedUpdates = listOf("message", "callback_query"),
  processingMode = LongPollingTelegramUpdateSource.ProcessingMode.CONCURRENT_BATCH,
  maxPendingUpdates = 50,
  getUpdatesTimeout = 30,
  fastFail = false,
)

val app = TelegramBotApplication(
    client = client,
    updateSource = updateSource,
  interceptors = emptyList(),
    eventDispatcher = HandlerTelegramEventDispatcher(routes)
)
```

## Processing Modes

| Mode               | Concurrency             | Backpressure                   | Delivery Guarantee          |
|--------------------|-------------------------|--------------------------------|-----------------------------|
| `SEQUENTIAL`       | One at a time           | Natural flow control           | At-Least-Once               |
| `CONCURRENT_BATCH` | Concurrent within batch | Batch-level                    | At-Least-Once (recommended) |
| `CONCURRENT`       | Fully concurrent        | None (use `maxPendingUpdates`) | At-Most-Once (**default**)  |

**Note:** Default mode is `CONCURRENT`. Global mutable state is NOT safe without synchronization.

## Graceful Shutdown

```kotlin
app.start()

// Register shutdown hook (JVM)
Runtime.getRuntime().addShutdownHook(Thread {
    runBlocking {
        app.stop(5.seconds)
    }
})

app.join()
```

### stop() vs stopSuspend()

- `stop(gracePeriod)` - Returns a `Job` immediately, does not suspend. Use when you need to trigger shutdown from
  non-suspending code or want to wait separately.
- `stopSuspend(gracePeriod)` - Suspends until the bot AND all coroutines in `applicationScope` have fully stopped. Use
  when you need to ensure complete cleanup.

## Next Steps

- [Handler DSL](Handler-DSL) - Advanced event handling
- [Conversations](Conversations) - Multi-turn interactions
