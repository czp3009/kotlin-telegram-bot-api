# Getting Started

This guide will help you set up and create your first Telegram bot.

## Prerequisites

- Kotlin 1.9+ or Java 17+
- A Telegram bot token (obtain from [@BotFather](https://t.me/BotFather))

## Installation

### Gradle (Kotlin DSL)

```kotlin
repositories {
    mavenCentral()
}

dependencies {
    implementation("com.hiczp.telegram.bot:application:$version")
}
```

### Maven

```xml
<dependency>
    <groupId>com.hiczp.telegram.bot</groupId>
    <artifactId>application</artifactId>
    <version>${version}</version>
</dependency>
```

## Your First Bot

### Echo Bot

```kotlin
import com.hiczp.telegram.bot.application.TelegramBotApplication
import com.hiczp.telegram.bot.application.dispatcher.SimpleTelegramEventDispatcher
import com.hiczp.telegram.bot.protocol.event.MessageEvent

suspend fun main(args: Array<String>) {
    val botToken = args.firstOrNull() ?: error("Bot token is required")
    
    val app = TelegramBotApplication.longPolling(
        botToken = botToken,
        eventDispatcher = SimpleTelegramEventDispatcher { context ->
            if (context.event is MessageEvent) {
                val text = context.event.message.text ?: return@SimpleTelegramEventDispatcher
                client.sendMessage(
                    chatId = context.event.message.chat.id.toString(),
                    text = text
                )
            }
        }
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
val app = TelegramBotApplication.longPolling(
    botToken = "YOUR_BOT_TOKEN",
    interceptors = listOf(loggingInterceptor(), conversationInterceptor()),
    eventDispatcher = HandlerTelegramEventDispatcher(routes)
)
```

### Advanced Configuration

```kotlin
val client = TelegramBotClient(botToken = "YOUR_BOT_TOKEN")

val updateSource = LongPollingTelegramUpdateSource(
    client = client,
    allowedUpdates = listOf("message", "callback_query"),
    processingMode = ProcessingMode.CONCURRENT_BATCH
)

val app = TelegramBotApplication(
    client = client,
    updateSource = updateSource,
    eventDispatcher = HandlerTelegramEventDispatcher(routes)
)
```

## Processing Modes

| Mode               | Concurrency             | Backpressure                   | Use Case                      |
|--------------------|-------------------------|--------------------------------|-------------------------------|
| `SEQUENTIAL`       | One at a time           | Natural flow control           | Strict ordering required      |
| `CONCURRENT_BATCH` | Concurrent within batch | Batch-level                    | General purpose (recommended) |
| `CONCURRENT`       | Fully concurrent        | None (use `maxPendingUpdates`) | High throughput               |

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

## Next Steps

- [Handler DSL](Handler-DSL) - Advanced event handling
- [Conversations](Conversations) - Multi-turn interactions
