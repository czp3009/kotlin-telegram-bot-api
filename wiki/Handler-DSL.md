# Handler DSL

The Handler DSL provides a type-safe, declarative way to route Telegram events to handlers.

## Overview

```kotlin
val routes = handling {
    onMessageEvent {
        whenMessageEventText("hello") { sendMessage("Hi!") }
    }
}

val dispatcher = HandlerTelegramEventDispatcher(routes)
```

## Core Functions

### handling {}

Creates the root route

```kotlin
val routes = handling {
    // Define routes here
}
```

### handle {}

Catch-all handler

```kotlin
handling {
    commandEndpoint("start") { /* ... */ }
    
    // Fallback for unhandled events
    handle { println("Unhandled: $event") }
}
```

## Event Type Scoping

```kotlin
handling {
    // Generic type scoping
    on<MessageEvent> { /* ... */ }
    on<CallbackQueryEvent> { /* ... */ }
    
    // Convenience shorthands (root level only)
    onMessageEvent { /* ... */ }
}
```

## Command Handling

### Simple Commands

```kotlin
handling {
    commandEndpoint("start") { replyMessage("Welcome!") }
    commandEndpoint("ping") { replyMessage("Pong!") }
}
```

### Commands with Arguments

```kotlin
class EchoArgs : BotArguments("Echo a message") {
    val message: String by requireArgument("The message to echo")
}

handling {
    command("echo", ::EchoArgs) {
        repeat(arguments.count) { replyMessage(arguments.message) }
    }
}
```

### Subcommands

```kotlin
handling {
    command("admin") {
        handle { showAdminHelp() }
        subCommandEndpoint("status") { showStatus() }
    }
}
```

## Message Matchers

### Text Matchers

```kotlin
onMessageEvent {
    whenMessageEventText("hello") { /* exact */ }
    whenMessageEventTextRegex(Regex("(?i)^hello")) { /* regex */ }
    whenMessageEventTextContains("help") { /* contains */ }
}
```

### Media Matchers

```kotlin
onMessageEvent {
    whenMessageEventPhoto { /* photo */ }
    whenMessageEventVideo { /* video */ }
    whenMessageEventDocument { /* document */ }
}
```

### User/Chat Filters

```kotlin
onMessageEvent {
    whenMessageEventFromUser(123456789L) { /* from specific user */ }
    whenMessageEventInChat(-1001234567890L) { /* in specific chat */ }
}
```

### Chat Type Filters

```kotlin
onMessageEvent {
    whenMessageEventPrivateChat { /* private chat */ }
    whenMessageEventGroupChat { /* group chat */ }
}
```

## Callback Query Matchers

```kotlin
onCallbackQueryEvent {
    whenCallbackQueryEventData("confirm") { /* exact */ }
    whenCallbackQueryEventDataRegex(Regex("action_\\d+")) { /* regex */ }
}
```

## Middleware

Conditional execution

```kotlin
onMessageEvent {
    middleware(
        predicate = { it.event.message.text?.length ?: 0 > 10 },
        onRejected = { sendMessage("Message too short") }
    ) {
        handle { /* protected handler */ }
    }
}
```

## Naming Convention

- Functions starting with `when` are **terminal** - they take a handler directly
- Functions without `when` prefix are **composable** - they take a build lambda

## Context Extensions

```kotlin
handling {
    commandEndpoint("demo") {
        sendMessage("Hello!")              // Send message to current chat
        replyMessage("This is a reply")     // Reply to current message
        val chatId = event.extractChatId() // Extract chat ID from event
    }
}
```

## Next Steps

- [Conversations](Conversations) - Multi-turn interactions
- [Interceptors](Interceptors) - Middleware pipeline
