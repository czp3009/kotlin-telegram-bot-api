# Advanced Topics

This page covers advanced usage patterns.

## Error Handling

```kotlin
try {
    client.sendMessage(chatId, text = "Hello")
} catch (e: TelegramErrorResponseException) {
    when (e.errorCode) {
        403 -> println("Bot blocked")
        429 -> println("Rate limited")
    }
}
```

## Union Types

Some methods return different types

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
val response = client.editMessageText(chatId, messageId, text = "Updated")

response.onSuccess { union ->
    val message = union.firstOrNull()  // Message if regular
    val boolean = union.secondOrNull() // Boolean if inline
}
```

## Thread Safety

Use thread-safe collections in concurrent mode

```kotlin
// DANGEROUS
val processedIds = mutableListOf<Long>()

// SAFE
val processedIds = ConcurrentLinkedQueue<Long>()
```

## Testing

### Mock Update Source

```kotlin
val updateSource = MockTelegramUpdateSource(Channel(Channel.UNLIMITED))

val app = TelegramBotApplication(
    client = client,
    updateSource = updateSource,
    eventDispatcher = dispatcher
)

app.start()
updateSource.push(testUpdate)
```

## Related Links

- [Telegram Bot API Documentation](https://core.telegram.org/bots/api)
- [Ktorfit](https://github.com/Foso/Ktorfit)
