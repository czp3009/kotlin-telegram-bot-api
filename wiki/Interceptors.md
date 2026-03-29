# Interceptors

Interceptors provide a middleware pipeline using the "onion model" pattern.

## Onion Model

```
┌────────────────────────────────────────────┐
│ Interceptor 1 (before)                     │
│   ┌──────────────────────────────────────┐ │
│   │ Interceptor 2 (before)               │ │
│   │   ┌────────────────────────────────┐ │ │
│   │   │   Event Dispatcher             │ │ │
│   │   │   (Handler Execution)          │ │ │
│   │   └────────────────────────────────┘ │ │
│   │ Interceptor 2 (after)                │ │
│   └──────────────────────────────────────┘ │
│ Interceptor 1 (after)                      │
└────────────────────────────────────────────┘
```

## Type Definition

```kotlin
typealias TelegramEventInterceptor =
    suspend TelegramEventProcessor.(TelegramBotEventContext<TelegramBotEvent>) -> Unit
```

Where `TelegramEventProcessor` has a single method:

```kotlin
interface TelegramEventProcessor {
    suspend fun process(context: TelegramBotEventContext<TelegramBotEvent>)
}
```

## Basic Interceptor

```kotlin
val loggingInterceptor: TelegramEventInterceptor = Interceptor@{ context ->
    println("Before: ${context.event.updateId}")
    process(context)  // Pass to next layer
    println("After: ${context.event.updateId}")
}
```

Note: The return label `Interceptor@` comes from the `TelegramEventInterceptor` typealias's underlying type
`Interceptor`.

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

### LoggingInterceptor

Logs all received events with configurable log level and formatter.

```kotlin
import com.hiczp.telegram.bot.application.interceptor.builtin.logging.loggingInterceptor

loggingInterceptor()  // Default: INFO level, toString() formatter
loggingInterceptor(level = Level.DEBUG)  // Custom level
loggingInterceptor(formatter = { event -> "Received: ${event.updateId}" })  // Custom formatter
```

Signature:

```kotlin
fun loggingInterceptor(
    level: Level = Level.INFO,
    formatter: (TelegramBotEvent) -> String = { event -> "$event" },
): TelegramEventInterceptor
```

### ConversationInterceptor

Enables multi-turn conversation support. Must be installed for `startConversation` to work.

```kotlin
import com.hiczp.telegram.bot.application.interceptor.builtin.conversation.conversationInterceptor

interceptors = listOf(conversationInterceptor())
```

Signature:

```kotlin
fun conversationInterceptor(
    manager: ConversationManager = ConversationManager()
): TelegramEventInterceptor
```

See [Conversations](Conversations) for detailed usage.

## Custom Interceptors

### Exception Handling

```kotlin
val exceptionInterceptor: TelegramEventInterceptor = Interceptor@{ context ->
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

### Blacklist Filter

```kotlin
val blacklistInterceptor: TelegramEventInterceptor = Interceptor@{ context ->
    val userId = context.event.extractUserId()
    if (userId != null && userId in blacklist) {
        // Don't call process() — this stops the pipeline
        return@Interceptor
    }
    process(context)
}
```

### Timeout Guard

```kotlin
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.TimeoutCancellationException

fun timeoutInterceptor(
    timeout: Duration,
    messageFilter: (TelegramBotEvent) -> Boolean,
    onTimeout: (suspend (TelegramBotEventContext<TelegramBotEvent>, TimeoutCancellationException) -> Unit)? = null,
): TelegramEventInterceptor = Interceptor@{ context ->
    if (!messageFilter(context.event)) {
        process(context)
        return@Interceptor
    }

    try {
        withTimeout(timeout) {
            process(context)
        }
    } catch (e: TimeoutCancellationException) {
        onTimeout?.invoke(context, e)
        throw e
    }
}
```

### Localization

```kotlin
import io.ktor.util.AttributeKey

val LanguageCodeAttributeKey = AttributeKey<String>("LanguageCode")

val localizationInterceptor: TelegramEventInterceptor = Interceptor@{ context ->
    val languageCode = context.event.extractLanguageCode()
    if (languageCode != null) {
        context.attributes.put(LanguageCodeAttributeKey, languageCode)
    }
    process(context)
}

// Access in handler
val TelegramBotEventContext<*>.languageCode: String?
    get() = attributes.getOrNull(LanguageCodeAttributeKey)
```

## Sharing State

Use attributes to share data between interceptors and handlers.

```kotlin
import io.ktor.util.AttributeKey

val UserIdKey = AttributeKey<Long?>("userId")

// Set in interceptor
val myInterceptor: TelegramEventInterceptor = Interceptor@{ context ->
    context.attributes.put(UserIdKey, context.event.extractUserId())
    process(context)
}

// Access in handler
handle {
    val userId = attributes.getOrNull(UserIdKey)
}
```

## Short-circuiting

Don't call `process()` to prevent further execution.

```kotlin
val maintenanceInterceptor: TelegramEventInterceptor = Interceptor@{ context ->
    if (isMaintenanceMode) {
        val chatId = context.event.extractChatId()
        if (chatId != null) {
            context.client.sendMessage(
                chatId = chatId.toString(),
                text = "Bot is under maintenance."
            )
        }
        // Don't call process() — this stops the pipeline
        return@Interceptor
    }
    process(context)
}
```

## Order Matters

Interceptors execute in the order provided. The first interceptor is the outermost layer.

```kotlin
interceptors = listOf(
    loggingInterceptor(),          // 1. Logs entry (outermost)
    authInterceptor,               // 2. Sets user info
    exceptionInterceptor           // 3. Catches exceptions (innermost)
)
```

## TelegramBotEventContext

The context object available in interceptors and handlers:

```kotlin
interface TelegramBotEventContext<out T : TelegramBotEvent> {
    val client: TelegramBotApi       // API client for sending responses
    val event: T                     // The event being processed
    val applicationScope: CoroutineScope  // Scope for fire-and-forget tasks
    val attributes: Attributes       // Type-safe key-value storage

    suspend fun me(): User           // Bot's own user info (cached)
}
```
