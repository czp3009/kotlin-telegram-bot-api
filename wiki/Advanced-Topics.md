# Advanced Topics

This page covers advanced usage patterns.

## Error Handling

When `TelegramBotClient` is configured with `throwOnErrorResponse = true` (the default), API errors
throw `TelegramErrorResponseException`.

```kotlin
import com.hiczp.telegram.bot.protocol.exception.TelegramErrorResponseException

try {
    client.sendMessage(chatId = "123", text = "Hello")
} catch (e: TelegramErrorResponseException) {
    when (e.errorCode) {
        403 -> println("Bot blocked by user")
        429 -> println("Rate limited: retry after ${e.parameters?.retryAfter} seconds")
        400 -> println("Bad request: ${e.description}")
    }
    if (e.isRetryable) { /* 429 or 5xx - safe to retry */
    }
}
```

### TelegramErrorResponseException Properties

| Property      | Type                  | Description                     |
|---------------|-----------------------|---------------------------------|
| `description` | `String`              | Error description from Telegram |
| `errorCode`   | `Int`                 | HTTP error code                 |
| `parameters`  | `ResponseParameters?` | Optional (e.g., `retryAfter`)   |
| `isRetryable` | `Boolean`             | `true` for 429 or 5xx errors    |

### TelegramResponse

All API calls return `TelegramResponse<T>`, which provides multiple handling patterns.

```kotlin
val response = client.getMe()

// Throw on error
val user = response.getOrThrow()

// Callback pattern (suspend)
response.onSuccess { user -> println(user) }
response.onError { exception -> println(exception) }

// Functional fold (suspend)
val result = response.fold(
    onSuccess = { "OK: $it" },
    onError = { "Error: $it" }
)

// Convert to Kotlin Result
val kotlinResult: Result<User> = response.toResult()
```

## Union Types

Some API methods return different types depending on context.

| Method                    | Union Type                |
|---------------------------|---------------------------|
| `editMessageText`         | `Union<Message, Boolean>` |
| `editMessageCaption`      | `Union<Message, Boolean>` |
| `editMessageMedia`        | `Union<Message, Boolean>` |
| `editMessageLiveLocation` | `Union<Message, Boolean>` |
| `stopMessageLiveLocation` | `Union<Message, Boolean>` |
| `editMessageReplyMarkup`  | `Union<Message, Boolean>` |
| `setGameScore`            | `Union<Message, Boolean>` |

```kotlin
val response = client.editMessageText(
    chatId = "123456789",
    messageId = 42,
    text = "Updated text"
)

response.onSuccess { union ->
    val message = union.firstOrNull()  // Message if regular message was edited
    val boolean = union.secondOrNull() // Boolean if inline message was edited

    when {
        message != null -> println("Edited message: ${message.messageId}")
        boolean != null -> println("Edit result: $boolean")
    }
}
```

The `Union` sealed class uses `@Serializable(with = UnionSerializer::class)`. In most cases, you do not need to
configure `unionSerializersModule` in your `Json` instance.

## Thread Safety

Use thread-safe collections in concurrent mode.

```kotlin
// DANGEROUS in CONCURRENT mode
val processedIds = mutableListOf<Long>()

// SAFE
val processedIds = ConcurrentLinkedQueue<Long>()

// Or use SEQUENTIAL mode for strict ordering
```

## Testing

### Mock Update Source

`MockTelegramUpdateSource` consumes updates from a `Channel<Update>`. It supports infinite start/stop cycles and does
not close the underlying channel when stopped.

```kotlin
import kotlinx.coroutines.channels.Channel
import com.hiczp.telegram.bot.application.updatesource.MockTelegramUpdateSource

val channel = Channel<Update>(Channel.UNLIMITED)
val updateSource = MockTelegramUpdateSource(channel)

val app = TelegramBotApplication(
    client = client,
    updateSource = updateSource,
    interceptors = emptyList(),
    eventDispatcher = dispatcher
)

app.start()

// Push test updates via the channel
channel.send(testUpdate)

// Stop and restart
app.stop()
// channel remains open for next start cycle
```

Exception handling in `MockTelegramUpdateSource`:

| Exception                            | Behavior                            |
|--------------------------------------|-------------------------------------|
| `TelegramBotShuttingDownException`   | Breaks loop, does not close channel |
| `CancellationException` (`isActive`) | Logs warning, continues             |
| Other `Throwable`                    | Logs error, continues               |

### Simple Update Source

`SimpleTelegramUpdateSource` allows external injection via `push()`. Designed for distributed systems where updates are
received by one component and processed by another.

```kotlin
import com.hiczp.telegram.bot.application.updatesource.SimpleTelegramUpdateSource

val updateSource = SimpleTelegramUpdateSource()

val app = TelegramBotApplication(
    client = client,
    updateSource = updateSource,
    interceptors = emptyList(),
    eventDispatcher = dispatcher
)

app.start()

// Push updates from external source (e.g., message queue consumer)
updateSource.push(update)
```

**Architecture Pattern:**

```
┌─────────────────┐     ┌─────────────────┐     ┌──────────────────────────────┐
│  Webhook Server │────>│  Message Queue  │────>│  Worker Node                 │
│  (Bot Instance) │     │  (Kafka/Rabbit) │     │  SimpleTelegramUpdateSource  │
└─────────────────┘     └─────────────────┘     └──────────────────────────────┘
```

**Key Features:**

- Stateless design - supports multiple start/stop cycles
- `push(update)` processes updates synchronously via the consumer callback
- Thread-safe - safe for concurrent use from multiple threads
- Exceptions from the consumer propagate to the caller

**Exception Semantics:**

| Exception                            | Behavior                                    |
|--------------------------------------|---------------------------------------------|
| `IllegalStateException`              | Source not started - propagate to caller    |
| `TelegramBotShuttingDownException`   | Bot shutting down - propagate (should nack) |
| `CancellationException` (`isActive`) | Logs warning, propagates                    |
| Other `Throwable`                    | Logs error, propagates to caller            |

**Pub/Sub Acknowledgment Strategy:**

When consuming from a message queue, use the exception to decide whether to ack or nack:

- **No exception**: Ack. Message processed successfully.
- `TelegramBotShuttingDownException`: Nack/Retry. Bot is shutting down; redeliver to another worker.
- `CancellationException`: Nack/Retry. Coroutine was cancelled; message may have been partially processed.
- `IllegalStateException`: Nack/Retry. Source not started; retry after ready.
- Other `Throwable`: Nack or Ack based on your business requirements (ack for permanent errors, nack for transient).

## Inline Keyboards DSL

Build inline keyboards using a type-safe DSL.

```kotlin
import com.hiczp.telegram.bot.protocol.extension.inlineKeyboard
import com.hiczp.telegram.bot.protocol.extension.callbackButton
import com.hiczp.telegram.bot.protocol.extension.urlButton

val keyboard = inlineKeyboard {
    row {
        callbackButton("Yes", "vote_yes")
        callbackButton("No", "vote_no")
    }
    row {
        urlButton("Visit", "https://example.com")
    }
}

client.sendMessage(
    chatId = chatId.toString(),
    text = "Vote:",
    replyMarkup = keyboard
)
```

Available button builders within a row:

| Builder                                                  | Description                       |
|----------------------------------------------------------|-----------------------------------|
| `callbackButton(text, callbackData, style?)`             | Callback query button             |
| `urlButton(text, url, style?)`                           | URL button                        |
| `webAppButton(text, url, style?)`                        | Web App button                    |
| `loginButton(text, loginUrl, style?)`                    | Login button                      |
| `switchInlineButton(text, query, style?)`                | Switch to inline query            |
| `switchInlineCurrentChatButton(text, query, style?)`     | Switch inline in current chat     |
| `switchInlineChosenChatButton(text, chosenChat, style?)` | Switch inline in chosen chat type |
| `copyTextButton(text, copyText, style?)`                 | Copy text to clipboard button     |
| `payButton(text)`                                        | Payment button                    |

All button builders accept an optional `style` parameter. Available styles:

| Style                               | Value       | Description            |
|-------------------------------------|-------------|------------------------|
| `InlineKeyboardButtonStyle.PRIMARY` | `"primary"` | Blue/bold button       |
| `InlineKeyboardButtonStyle.DANGER`  | `"danger"`  | Red/destructive button |
| `InlineKeyboardButtonStyle.SUCCESS` | `"success"` | Green/positive button  |

### Convenience Functions

```kotlin
// Quick row of callback buttons
callbackRow("Yes" to "yes", "No" to "no")
callbackRow(InlineKeyboardButtonStyle.DANGER, "Delete" to "delete", "Cancel" to "cancel")

// Quick keyboard from rows
val keyboard = inlineKeyboardOf(
    listOf("Yes" to "yes", "No" to "no"),
    listOf("Help" to "help")
)
```

## Related Links

- [Telegram Bot API Documentation](https://core.telegram.org/bots/api)
- [Ktorfit](https://github.com/Foso/Ktorfit)
