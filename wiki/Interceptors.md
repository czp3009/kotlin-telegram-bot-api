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

## Basic Interceptor

```kotlin
val loggingInterceptor: TelegramEventInterceptor = { context ->
    println("Before: ${context.event.updateId}")
    process(context)  // Pass to next layer
    println("After: ${context.event.updateId}")
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

### LoggingInterceptor

```kotlin
import com.hiczp.telegram.bot.application.interceptor.builtin.logging.loggingInterceptor

loggingInterceptor()
```

### ConversationInterceptor

```kotlin
import com.hiczp.telegram.bot.application.interceptor.builtin.conversation.conversationInterceptor

interceptors = listOf(conversationInterceptor())
```

## Custom Interceptors

### Exception Handling

```kotlin
val exceptionInterceptor: TelegramEventInterceptor = {
    try {
        process(context)
    } catch (e: TelegramErrorResponseException) {
        context.event.extractChatId()?.let { chatId ->
            context.client.sendMessage(chatId.toString(), "Error: ${e.description}")
        }
    }
}
```

### Authentication

```kotlin
val authInterceptor: TelegramEventInterceptor = {
    val userId = extractUserId(context.event)
    context.attributes[IsAuthenticatedKey] = checkAuth(userId)
    process(context)
}
```

## Sharing State

Use attributes to share data between interceptors and handlers

```kotlin
val UserIdKey = AttributeKey<Long?>("userId")

// Set in interceptor
val myInterceptor: TelegramEventInterceptor = {
    context.attributes[UserIdKey] = extractUserId(context.event)
    process(context)
}

// Access in handler
handle {
    val userId = attributes[UserIdKey]
}
```

## Short-circuiting

Don't call `process()` to prevent further execution

```kotlin
val maintenanceInterceptor: TelegramEventInterceptor = {
    if (isMaintenanceMode) {
        context.event.extractChatId()?.let { chatId ->
            context.client.sendMessage(chatId.toString(), "Bot is under maintenance.")
        }
        return  // Don't process further
    }
    process(context)
}
```

## Order Matters

Interceptors execute in order

```kotlin
interceptors = listOf(
    loggingInterceptor(),          // 1. Logs entry
    authInterceptor,               // 2. Sets user info
    exceptionInterceptor           // 3. Catches exceptions
)
```
