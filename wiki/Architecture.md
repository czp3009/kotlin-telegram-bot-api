# Architecture

This page explains the architecture and design of the library.

## Module Overview

```
┌─────────────────────────────────────────────────────────────────┐
│                         User Application                             │
│  ┌─────────────┐  ┌────────────────┐  ┌─────────────────────────┐   │
│  │   Routes    │  │  Interceptors  │  │  Business Logic         │   │
│  └─────────────┘  └────────────────┘  └─────────────────────────┘   │
└─────────────────────────────────────────────────────────────────┘
                                  │
                                  ▼
┌─────────────────────────────────────────────────────────────────┐
│                       Application Module                             │
│  ┌──────────────────────┐  ┌────────────────────────────────────┐   │
│  │ TelegramBotApplication│  │      Update Sources                │   │
│  │  - Lifecycle         │  │  - LongPolling                     │   │
│  │  - Event Processing  │  │  - Webhook (separate module)       │   │
│  └──────────────────────┘  │  - Mock (testing)                  │   │
│                            │  │  - Simple (external injection)      │   │
│  ┌──────────────────────┐  └────────────────────────────────────┘   │
│                            │  ┌────────────────────────────────────┐   │
│  ┌──────────────────────┐  ┌────────────────────────────────────┐   │
│  │    Event Dispatcher  │                                          │
│  │  - Handler DSL      │  ┌────────────────────────────────────┐   │
│  │  - Route Matching   │  │      Interceptors                   │   │
│  └──────────────────────┘  │  - Conversation                    │   │
│                            │  │  - Logging                          │   │
│                            │  │  - Custom                           │   │
│                            └────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────┘
                                  │
                                  ▼
┌─────────────────────────────────────────────────────────────────┐
│                          Client Module                               │
│  ┌──────────────────────────────────────────────────────────────┐   │
│  │                    TelegramBotClient                          │   │
│  │  - HTTP Client Wrapper                                        │   │
│  │  - Retry Logic                                                │   │
│  │  - Rate Limiting                                                │   │
│  │  - File Download                                                │   │
│  └──────────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────┘
                                  │
                                  ▼
┌─────────────────────────────────────────────────────────────────┐
│                         Protocol Module                              │
│  ┌────────────────┐  ┌────────────────┐  ┌────────────────────┐     │
│  │ TelegramBotApi │  │    Models      │  │      Events        │     │
│  │ (Ktorfit)      │  │  - Message     │  │  - MessageEvent    │     │
│  │                │  │  - User        │  │  - CallbackQueryEvent │     │
│  │                │  │  - Chat        │  │  - InlineQueryEvent   │     │
│  └────────────────┘  └────────────────┘  └────────────────────┘     │
└─────────────────────────────────────────────────────────────────┘
                                  │
                                  ▼
                         Telegram Bot API
```

## Request Flow

```
1. Update Fetching
   UpdateSource (LongPoll) ──> TelegramBotClient ──> Telegram API
          │
          ▼ Raw Update objects
          
2. Event Conversion
   Update.toTelegramBotEvent() ──> TelegramBotEvent (MessageEvent, CallbackQueryEvent, etc.)
                               │
                               ▼
                               
3. Interceptor Pipeline (Onion Model)
   ┌────────────────────────────────────────────────┐
   │ Interceptor 1 (before)                         │
   │   ┌──────────────────────────────────────────┐ │
   │   │ Interceptor 2 (before)                   │ │
   │   │   ┌────────────────────────────────────┐ │ │
   │   │   │     Event Dispatcher             │ │ │
   │   │   │     (Route Matching)               │ │ │
   │   │   └────────────────────────────────────┘ │ │
   │   │ Interceptor 2 (after)                    │ │
   │   └──────────────────────────────────────────┘ │
   │ Interceptor 1 (after)                          │
   └────────────────────────────────────────────────┘
                               │
                               ▼
                               
4. Handler Execution
   Handler ──> TelegramBotClient ──> Telegram API
```

## Module Details

### Protocol Module (`:protocol`)

Auto-generated Telegram Bot API definitions.

| Directory           | Description                                     |
|---------------------|-------------------------------------------------|
| `TelegramBotApi.kt` | Ktorfit interface with HTTP annotations         |
| `model/`            | Data classes for Telegram entities              |
| `form/`             | Multipart form wrappers for file uploads        |
| `query/`            | Extension functions for GET parameters          |
| `type/`             | Handwritten types (TelegramResponse, InputFile) |
| `event/`            | Generated event types from Update model         |

### Client Module (`:client`)

High-level HTTP client wrapper:

- Pre-configured `TelegramBotClient` with sensible defaults
- Automatic retry for transient failures
- Rate limiting compliance
- Test environment support
- File download utilities

### Application Module (`:application`)

Bot application framework:

| Component                         | Description                                     |
|-----------------------------------|-------------------------------------------------|
| `TelegramBotApplication`          | Main orchestrator for lifecycle                 |
| `TelegramUpdateSource`            | Interface for fetching updates                  |
| `LongPollingTelegramUpdateSource` | Long polling implementation                     |
| `SimpleTelegramUpdateSource`      | External update injection (distributed systems) |
| `MockTelegramUpdateSource`        | Testing utility (Channel-based)                 |
| `TelegramEventInterceptor`        | Middleware function type                        |
| `HandlerTelegramEventDispatcher`  | Type-safe routing DSL                           |
| `SimpleTelegramEventDispatcher`   | Single lambda handler                           |

### Webhook Module (`:application-updatesource-webhook`)

Webhook-based update receiving using an embedded Ktor server.

## Key Design Patterns

### Onion Model Interceptors

Interceptors wrap around the event processing pipeline:

```kotlin
val interceptor: TelegramEventInterceptor = { context ->
    // Pre-processing
    println("Before: ${context.event.updateId}")
    process(context)  // Call next layer
    // Post-processing
    println("After: ${context.event.updateId}")
}
```

### Handler DSL

Type-safe routing using Kotlin DSL

```kotlin
handling {
    onMessageEvent {
        whenMessageEventText("hello") { replyMessage("Hi!") }
    }
}
```

### Structured Concurrency

Handlers receive a `CoroutineScope`

```kotlin
commandEndpoint("process") {
    launch { task1() }
    launch { task2() }
    // Both complete before dispatch finishes
}
```

## Lifecycle

```
  NEW ──────> RUNNING ──────> STOPPING ──────> STOPPED
                 │                               ▲
                 └───────────────────────────────┘
                          (stop() called)
```

### Graceful Shutdown

1. Transition to `STOPPING` → new updates rejected
2. Call `TelegramUpdateSource.stop()` → cuts off update source
3. Wait within grace period for handlers to complete
4. Force cancel if timeout
5. Call `TelegramUpdateSource.onFinalize()` for cleanup

## Exception Handling

| Exception                          | Behavior                                          |
|------------------------------------|---------------------------------------------------|
| `TelegramBotShuttingDownException` | Propagates, signals framework shutdown            |
| `CancellationException`            | Logs and advances offset (prevents stuck bot)     |
| Other `Exception`                  | Logs error and advances offset (skips bad update) |

## Dependency Graph

```
┌─────────────┐     ┌─────────────┐     ┌─────────────┐
│ application │────>│   client    │────>│  protocol   │
└─────────────┘     └─────────────┘     └─────────────┘
       │
       │ (optional)
       ▼
┌─────────────────────────────┐
│ application-updatesource-   │
│ webhook                     │
└─────────────────────────────┘
```

## Related Links

- [Telegram Bot API Documentation](https://core.telegram.org/bots/api)
- [Ktorfit](https://github.com/Foso/Ktorfit)
- [Ktor](https://ktor.io/)
