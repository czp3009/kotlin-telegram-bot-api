# Client

A high-level Kotlin Multiplatform client wrapper for the Telegram Bot API.

It provides `TelegramBotClient` - a pre-configured client with sensible defaults, built on top of
the [protocol](../protocol) module.

## Key Features

- **Sensible Defaults**: Pre-configured JSON serialization, timeouts, and retry logic
- **Automatic Retry**: Built-in retry for transient failures and rate limiting
- **Test Environment Support**: Easy switch to Telegram's test environment
- **Flexible Error Handling**: Configurable error response handling modes
- **Long Polling Optimization**: Pre-installed long polling plugin
- **File Download Support**: Built-in file download URL rewriting

## Quick Start

```kotlin
import com.hiczp.telegram.bot.client.TelegramBotClient
import io.ktor.client.engine.cio.*

val client = TelegramBotClient(
    ktorEngine = CIO.create(),
    botToken = "123456:ABC-DEF1234ghIkl-zyx57W2v1u123ew11"
)

// Get bot information
val me = client.getMe().getOrThrow()
println("Bot username: @${me.username}")

// Send a message
client.sendMessage(
    chatId = ChatId.from(123456789L),
    text = "Hello from Kotlin!"
)
```

## Configuration

### Constructor Parameters

| Parameter                 | Type                             | Default                      | Description                          |
|---------------------------|----------------------------------|------------------------------|--------------------------------------|
| `ktorEngine`              | `HttpClientEngine`               | Required                     | The Ktor engine for network requests |
| `botToken`                | `String`                         | Required                     | Bot token from BotFather             |
| `baseUrl`                 | `String`                         | `"https://api.telegram.org"` | API base URL (for local bot servers) |
| `useTestEnvironment`      | `Boolean`                        | `false`                      | Use Telegram's test environment      |
| `throwOnErrorResponse`    | `Boolean`                        | `true`                       | Throw exception on error responses   |
| `additionalConfiguration` | `HttpClientConfig<*>.() -> Unit` | `{}`                         | Custom HttpClient configuration      |

### Custom Configuration

```kotlin
val client = TelegramBotClient(
    ktorEngine = CIO.create(),
    botToken = System.getenv("BOT_TOKEN"),
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
    chatId = ChatId.from(123456789L),
    photo = InputFile.reference("AgACAgQAAxkBA...")
)

// Upload binary content
client.sendDocument(
    chatId = ChatId.from(123456789L),
    document = InputFile.binary(fileName = "report.pdf") {
        ByteReadChannel(pdfBytes)
    }
)

// Upload from URL
client.sendPhoto(
    chatId = ChatId.from(123456789L),
    photo = InputFile.reference("https://example.com/photo.jpg")
)
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

Choose the appropriate engine for your platform:

### JVM

```kotlin
import io.ktor.client.engine.cio.*

TelegramBotClient(
    ktorEngine = CIO.create(),
    botToken = "YOUR_TOKEN"
)
```

### Android

```kotlin
import io.ktor.client.engine.android.*

TelegramBotClient(
    ktorEngine = Android.create(),
    botToken = "YOUR_TOKEN"
)
```

### iOS/macOS

```kotlin
import io.ktor.client.engine.darwin.*

TelegramBotClient(
    ktorEngine = Darwin.create(),
    botToken = "YOUR_TOKEN"
)
```

### Linux/Windows

```kotlin
import io.ktor.client.engine.curl.*

TelegramBotClient(
    ktorEngine = Curl.create(),
    botToken = "YOUR_TOKEN"
)
```

### JavaScript

```kotlin
import io.ktor.client.engine.js.*

TelegramBotClient(
    ktorEngine = Js.create(),
    botToken = "YOUR_TOKEN"
)
```

## Related Links

- [Protocol Module](../protocol) - Core API definitions
- [Telegram Bot API Documentation](https://core.telegram.org/bots/api)
- [Ktor Documentation](https://ktor.io/docs/client.html)
- [Ktorfit](https://github.com/Foso/Ktorfit)
