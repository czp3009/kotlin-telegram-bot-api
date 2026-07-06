# Interceptors

Interceptors are the application-level onion pipeline. They wrap every dispatcher, including the handler dispatcher,
simple dispatcher, and any future dispatcher implementations.

Handler filters decide whether a route branch should be entered. Interceptors are a higher layer and are used for
cross-cutting concerns such as logging, authentication, metrics, timeout guards, and conversations.

## Onion Model

```
Interceptor 1 before
  Interceptor 2 before
    Event Dispatcher
  Interceptor 2 after
Interceptor 1 after
```

The first interceptor in the list is the outermost layer.

## Type Definition

```kotlin
interface TelegramEventProcessor {
    suspend fun process(context: TelegramBotEventContext<TelegramBotEvent>)
}

typealias TelegramEventInterceptor =
    suspend TelegramEventProcessor.(TelegramBotEventContext<TelegramBotEvent>) -> Unit
```

Call `process(context)` to continue to the next interceptor or dispatcher. Skip it to short-circuit the pipeline.

## Basic Interceptor

```kotlin
val loggingInterceptor: TelegramEventInterceptor = { context ->
    println("Before: ${context.event.updateId}")
    process(context)
    println("After: ${context.event.updateId}")
}
```

Use an explicit lambda label when you need to return early:

```kotlin
val blacklistInterceptor: TelegramEventInterceptor = blacklist@{ context ->
    val userId = context.event.extractUserId()
    if (userId != null && userId in blacklist) {
        return@blacklist
    }
    process(context)
}
```

## Installing

```kotlin
val app = TelegramBotApplication.longPolling(
    botToken = "YOUR_TOKEN",
    interceptors = listOf(
        loggingInterceptor(),
        conversationInterceptor()
    ),
    eventDispatcher = dispatcher
)
```

## Built-in Interceptors

### Logging

```kotlin
import com.hiczp.telegram.bot.application.interceptor.builtin.logging.loggingInterceptor

loggingInterceptor()
loggingInterceptor(level = Level.DEBUG)
loggingInterceptor(formatter = { event -> "Received: ${event.updateId}" })
```

Signature:

```kotlin
fun loggingInterceptor(
    level: Level = Level.INFO,
    formatter: (TelegramBotEvent) -> String = { event -> "$event" },
): TelegramEventInterceptor
```

### Conversations

`conversationInterceptor()` enables `conversation` and `conversationCommand`. Install it once.

```kotlin
import com.hiczp.telegram.bot.application.interceptor.builtin.conversation.conversationInterceptor

val app = TelegramBotApplication.longPolling(
    botToken = "YOUR_TOKEN",
    interceptors = listOf(conversationInterceptor()),
    eventDispatcher = dispatcher
)
```

See [Conversations](Conversations) for usage.

## Custom Interceptors

### Exception Handling

```kotlin
val exceptionInterceptor: TelegramEventInterceptor = { context ->
    try {
        process(context)
    } catch (e: TelegramErrorResponseException) {
        val chatId = context.event.extractChatId()
        if (chatId != null) {
            context.client.sendMessage(
                chatId = chatId.toString(),
                text = "Error: ${e.description}"
            )
        }
    }
}
```

### Timeout Guard

```kotlin
fun timeoutInterceptor(
    timeout: Duration,
    shouldApply: (TelegramBotEvent) -> Boolean,
    onTimeout: suspend (TelegramBotEventContext<TelegramBotEvent>, TimeoutCancellationException) -> Unit = { _, _ -> },
): TelegramEventInterceptor = interceptor@{ context ->
    if (!shouldApply(context.event)) {
        process(context)
        return@interceptor
    }

    try {
        withTimeout(timeout) {
            process(context)
        }
    } catch (e: TimeoutCancellationException) {
        onTimeout(context, e)
        throw e
    }
}
```

### Localization

```kotlin
val languageCodeKey = AttributeKey<String>("languageCode")

val localizationInterceptor: TelegramEventInterceptor = { context ->
    val languageCode = context.event.extractLanguageCode()
    if (languageCode != null) {
        context.attributes.put(languageCodeKey, languageCode)
    }
    process(context)
}

val TelegramBotEventContext<*>.languageCode: String?
    get() = attributes.getOrNull(languageCodeKey)
```

## Sharing State

`TelegramBotEventContext.attributes` is request-scoped storage shared by interceptors and handlers.

```kotlin
val userIdKey = AttributeKey<Long?>("userId")

val userInterceptor: TelegramEventInterceptor = { context ->
    context.attributes.put(userIdKey, context.event.extractUserId())
    process(context)
}

handling {
    handle {
        val userId = attributes.getOrNull(userIdKey)
    }
}
```

## Short-Circuiting

Do not call `process(context)` when the event should not reach inner layers.

```kotlin
val maintenanceInterceptor: TelegramEventInterceptor = maintenance@{ context ->
    if (isMaintenanceMode) {
        val chatId = context.event.extractChatId()
        if (chatId != null) {
            context.client.sendMessage(
                chatId = chatId.toString(),
                text = "Bot is under maintenance."
            )
        }
        return@maintenance
    }

    process(context)
}
```

## Handler Filters Vs Interceptors

Use a handler filter when the condition is part of route selection:

```kotlin
message {
    filter({ event.message.chat.id in allowedChats }) {
        command("admin") {
            handle {
                replyMessage("admin")
            }
        }
    }
}
```

Use an interceptor when the logic should wrap all dispatching or should be shared across dispatcher implementations.

## TelegramBotEventContext

```kotlin
interface TelegramBotEventContext<out T : TelegramBotEvent> {
    val client: TelegramBotApi
    val event: T
    val applicationScope: CoroutineScope
    val attributes: Attributes

    suspend fun me(): User
}
```
