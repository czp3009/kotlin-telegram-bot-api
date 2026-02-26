# Client

A high-level Kotlin Multiplatform client wrapper for the Telegram Bot API.

It provides `TelegramBotClient` — a pre-configured client with sensible defaults, built on top of
the [protocol](../protocol) module.

## Key Features

- **Sensible Defaults**: Pre-configured JSON serialization, timeouts, and retry logic
- **Automatic Retry**: Built-in retry for transient failures and rate limiting
- **Test Environment Support**: Easy switch to Telegram's test environment
- **Flexible Error Handling**: Configurable error response handling modes
- **Long Polling Optimization**: Ships with the long polling plugin pre-installed
- **File Download Support**: Built-in file download URL rewriting
- **Type-Safe Events**: Uses the auto-generated `TelegramBotEvent` sealed interface from the [protocol](../protocol)
  module

## Quick Start

```kotlin
import com.hiczp.telegram.bot.client.TelegramBotClient
import io.ktor.client.engine.cio.*

val client = TelegramBotClient(
    botToken = "123456:ABC-DEF1234ghIkl-zyx57W2v1u123ew11"
)

// Get bot information
val me = client.getMe().getOrThrow()
println("Bot username: @${me.username}")

// Send a message
client.sendMessage(
    chatId = "123456789",
    text = "Hello from Kotlin!"
)
```

## Configuration

### Constructor Parameters

| Parameter                 | Type                             | Default                      | Description                          |
|---------------------------|----------------------------------|------------------------------|--------------------------------------|
| `botToken`                | `String`                         | Required                     | Bot token from BotFather             |
| `httpClientEngine`        | `HttpClientEngine?`              | `null`                       | The Ktor engine for network requests |
| `baseUrl`                 | `String`                         | `"https://api.telegram.org"` | API base URL (for local bot servers) |
| `useTestEnvironment`      | `Boolean`                        | `false`                      | Use Telegram's test environment      |
| `throwOnErrorResponse`    | `Boolean`                        | `true`                       | Throw exception on error responses   |
| `additionalConfiguration` | `HttpClientConfig<*>.() -> Unit` | `{}`                         | Custom HttpClient configuration      |

### Custom Configuration

```kotlin
val client = TelegramBotClient(
    botToken = System.getenv("BOT_TOKEN"),
    httpClientEngine = CIO.create(),
    baseUrl = "https://my-local-bot-server.example.com",
    useTestEnvironment = true,
    throwOnErrorResponse = false,
    additionalConfiguration = {
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.INFO
        }
    }
)
```

## Error Handling

### With Exceptions (default)

When `throwOnErrorResponse = true` (default), API errors throw `TelegramErrorResponseException`:

```kotlin
try {
    client.sendMessage(chatId, text = "Hello")
} catch (e: TelegramErrorResponseException) {
    println("Error ${e.errorCode}: ${e.description}")
}
```

### Without Exceptions

When `throwOnErrorResponse = false`, handle errors via `TelegramResponse`:

```kotlin
val response = client.sendMessage(chatId, text = "Hello")

response.onSuccess { message ->
    println("Sent message: ${message.messageId}")
}

response.onError { error ->
    println("Error: $error")
}

// Or use fold
response.fold(
    onSuccess = { message -> handleSuccess(message) },
    onError = { error -> handleError(error) }
)
```

## File Uploads

The client uses `InputFile` from the protocol module for file operations:

```kotlin
import com.hiczp.telegram.bot.protocol.type.InputFile

// Reference existing file by file_id
client.sendPhoto(
    chatId = "123456789",
    photo = InputFile.reference("AgACAgQAAxkBA...")
)

// Upload binary content
client.sendDocument(
    chatId = "123456789",
    document = InputFile.binary(fileName = "report.pdf") {
        ByteReadChannel(pdfBytes)
    }
)

// Upload from URL
client.sendPhoto(
    chatId = "123456789",
    photo = InputFile.reference("https://example.com/photo.jpg")
)
```

## Event Handling

The client module uses the type-safe event model from the protocol module. Event classes are
auto-generated from the `Update` model.

### TelegramBotEvent Sealed Interface

All events implement the `TelegramBotEvent` sealed interface, which provides a common `updateId` property:

```kotlin
sealed interface TelegramBotEvent {
    val updateId: Long
}
```

### Converting Updates to Events

Use the `toTelegramBotEvent()` extension function to convert an `Update` to the appropriate event type:

```kotlin
import com.hiczp.telegram.bot.protocol.event.*

val updates = client.getUpdates().getOrThrow()
for (update in updates) {
    val event = update.toTelegramBotEvent()
    when (event) {
        is MessageEvent -> handleMessage(event.message)
        is EditedMessageEvent -> handleEditedMessage(event.editedMessage)
        is CallbackQueryEvent -> handleCallbackQuery(event.callbackQuery)
        is InlineQueryEvent -> handleInlineQuery(event.inlineQuery)
        // ... handle other event types
    }
}
```

## Long Polling

The client comes with `TelegramLongPollingPlugin` pre-installed, which applies appropriate timeouts for `getUpdates`:

```kotlin
client.getUpdates(
    offset = lastUpdateId + 1,
    timeout = 30,
    allowedUpdates = listOf("message", "callback_query")
)
```

## Platform-Specific Engines

Choose an engine appropriate for your target platform. The examples below are common choices, not strict requirements:

### JVM

```kotlin
import io.ktor.client.engine.cio.*

TelegramBotClient(
    botToken = "YOUR_TOKEN",
    httpClientEngine = CIO.create()
)
```

### Android

```kotlin
import io.ktor.client.engine.android.*

TelegramBotClient(
    botToken = "YOUR_TOKEN",
    httpClientEngine = Android.create()
)
```

### iOS/macOS

```kotlin
import io.ktor.client.engine.darwin.*

TelegramBotClient(
    botToken = "YOUR_TOKEN",
    httpClientEngine = Darwin.create()
)
```

### Linux/Windows

```kotlin
import io.ktor.client.engine.curl.*

TelegramBotClient(
    botToken = "YOUR_TOKEN",
    httpClientEngine = Curl.create()
)
```

### JavaScript

```kotlin
import io.ktor.client.engine.js.*

TelegramBotClient(
    botToken = "YOUR_TOKEN",
    httpClientEngine = Js.create()
)
```

## Supported Platforms

JVM, Android, JS, WASM, Linux, macOS, Windows, iOS, watchOS, tvOS, Android Native.

Tests only run on JVM and desktop native targets (Linux, macOS, Windows).

## Related Links

- [Protocol Module](../protocol) - Core API definitions
- [Application Module](../application) - Bot application framework with lifecycle management
- [Telegram Bot API Documentation](https://core.telegram.org/bots/api)
- [Ktor Documentation](https://ktor.io/docs/client.html)
- [Ktorfit](https://github.com/Foso/Ktorfit)
