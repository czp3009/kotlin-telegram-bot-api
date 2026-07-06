# Advanced Topics

This page collects protocol, client, concurrency, and testing details that do not fit into the first bot guide.

## Error Handling

`TelegramBotClient` defaults to `throwOnErrorResponse = true`. Telegram error payloads are converted to
`TelegramErrorResponseException`.

```kotlin
try {
    client.sendMessage(chatId = "123", text = "Hello")
} catch (e: TelegramErrorResponseException) {
    when (e.errorCode) {
        403 -> println("Bot blocked by user")
        429 -> println("Rate limited: retry after ${e.parameters?.retryAfter} seconds")
        400 -> println("Bad request: ${e.description}")
    }

    if (e.isRetryable) {
        // 429 or 5xx
    }
}
```

## TelegramResponse

All protocol calls return `TelegramResponse<T>`.

```kotlin
val response = client.getMe()

val user = response.getOrThrow()

response.onSuccess { user ->
    println(user)
}

response.onError { exception ->
    println(exception)
}

val result = response.fold(
    onSuccess = { "OK: $it" },
    onError = { "Error: $it" }
)

val kotlinResult: Result<User> = response.toResult()
```

If you create `TelegramBotClient(throwOnErrorResponse = false)`, inspect `TelegramResponse` instead of catching
exceptions.

## Union Types

Some edit methods return `Message` when editing a normal message and `Boolean` when editing an inline message.

| Method                    | Return type                                 |
|---------------------------|---------------------------------------------|
| `editMessageText`         | `TelegramResponse<Union<Message, Boolean>>` |
| `editMessageCaption`      | `TelegramResponse<Union<Message, Boolean>>` |
| `editMessageMedia`        | `TelegramResponse<Union<Message, Boolean>>` |
| `editMessageLiveLocation` | `TelegramResponse<Union<Message, Boolean>>` |
| `stopMessageLiveLocation` | `TelegramResponse<Union<Message, Boolean>>` |
| `editMessageReplyMarkup`  | `TelegramResponse<Union<Message, Boolean>>` |
| `setGameScore`            | `TelegramResponse<Union<Message, Boolean>>` |

```kotlin
val response = client.editMessageText(
    chatId = "123456789",
    messageId = 42,
    text = "Updated text"
)

response.onSuccess { union ->
    when {
        union.firstOrNull() != null -> println("Edited message: ${union.firstOrNull()?.messageId}")
        union.secondOrNull() != null -> println("Edit result: ${union.secondOrNull()}")
    }
}
```

`Union` is annotated with `@Serializable(with = UnionSerializer::class)`, so normal statically typed usage does not
require registering `unionSerializersModule`.

## Thread Safety

`LongPollingTelegramUpdateSource` defaults to `CONCURRENT`. Handlers can run at the same time.

```kotlin
// Unsafe with concurrent handlers
val processedIds = mutableListOf<Long>()

// Use synchronization, a thread-safe collection, or SEQUENTIAL mode
```

Use `ProcessingMode.SEQUENTIAL` when strict in-process ordering is more important than throughput.

## Inline Keyboard DSL

The protocol module provides helpers for inline keyboards.

```kotlin
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

Button builders include `callbackButton`, `urlButton`, `webAppButton`, `loginButton`, `switchInlineButton`,
`switchInlineCurrentChatButton`, `switchInlineChosenChatButton`, `copyTextButton`, and `payButton`.

Convenience helpers:

```kotlin
callbackRow("Yes" to "yes", "No" to "no")
callbackRow(InlineKeyboardButtonStyle.DANGER, "Delete" to "delete", "Cancel" to "cancel")

val keyboard = inlineKeyboardOf(
    listOf("Yes" to "yes", "No" to "no"),
    listOf("Help" to "help")
)
```

## Related Links

- [Telegram Bot API Documentation](https://core.telegram.org/bots/api)
- [Protocol README](../protocol/README.md)
- [Application README](../application/README.md)
