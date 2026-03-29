# Architecture

This page explains the architecture and design of the library.

## Module Overview

```
┌──────────────────────────────────────────────────────────────────┐
│                        User Application                          │
│  ┌─────────────┐  ┌────────────────┐  ┌──────────────────────┐  │
│  │   Routes     │  │  Interceptors  │  │  Business Logic      │  │
│  └─────────────┘  └────────────────┘  └──────────────────────┘  │
└──────────────────────────────────────────────────────────────────┘
                                 │
                                 ▼
┌──────────────────────────────────────────────────────────────────┐
│                      Application Module                          │
│  ┌───────────────────────┐  ┌─────────────────────────────────┐ │
│  │ TelegramBotApplication │  │      Update Sources             │ │
│  │  - Lifecycle          │  │  - LongPolling                  │ │
│  │  - Event Processing   │  │  - Webhook (separate module)    │ │
│  │  - startTime          │  │  - Mock (testing)               │ │
│  └───────────────────────┘  │  - Simple (external injection)   │ │
│                             └─────────────────────────────────┘ │
│  ┌───────────────────────┐  ┌─────────────────────────────────┐ │
│  │    Event Dispatcher   │  │      Interceptors               │ │
│  │  - Handler DSL        │  │  - Conversation                 │ │
│  │  - Route Matching     │  │  - Logging                      │ │
│  └───────────────────────┘  │  - Custom                        │ │
│                             └─────────────────────────────────┘ │
└──────────────────────────────────────────────────────────────────┘
                                 │
                                 ▼
┌──────────────────────────────────────────────────────────────────┐
│                         Client Module                            │
│  ┌──────────────────────────────────────────────────────────────┐│
│  │                    TelegramBotClient                          ││
│  │  - HTTP Client Wrapper (delegates to TelegramBotApi)         ││
│  │  - Retry Logic (3 retries, respects retry_after)             ││
│  │  - Rate Limiting (429 backoff with 1-2s jitter)               ││
│  │  - File Download (TelegramFileDownloadPlugin)                 ││
│  │  - Error Response Handling (throwOnErrorResponse)             ││
│  └──────────────────────────────────────────────────────────────┘│
└──────────────────────────────────────────────────────────────────┘
                                 │
                                 ▼
┌──────────────────────────────────────────────────────────────────┐
│                        Protocol Module                           │
│  ┌────────────────┐  ┌────────────────┐  ┌────────────────────┐ │
│  │ TelegramBotApi │  │    Models      │  │      Events        │ │
│  │ (Ktorfit)      │  │  - Message     │  │  - MessageEvent    │ │
│  │                │  │  - User        │  │  - CallbackQuery   │ │
│  │                │  │  - Chat        │  │  - InlineQuery     │ │
│  └────────────────┘  └────────────────┘  └────────────────────┘ │
│  ┌────────────────┐  ┌────────────────┐  ┌────────────────────┐ │
│  │  Extensions    │  │    Unions      │  │     Plugins        │ │
│  │  - Keyboards   │  │  - Union<A, B> │  │  - FileDownload    │ │
│  │  - Messages    │  │  - Serializer  │  │  - LongPolling     │ │
│  └────────────────┘  └────────────────┘  └────────────────────┘ │
└──────────────────────────────────────────────────────────────────┘
                                 │
                                 ▼
                        Telegram Bot API
```

## Request Flow

```
1. Update Fetching
   UpdateSource (LongPoll/Webhook) ──> TelegramBotClient ──> Telegram API
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
   │   │   │     Event Dispatcher               │ │ │
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

| Directory           | Description                                                                               |
|---------------------|-------------------------------------------------------------------------------------------|
| `TelegramBotApi.kt` | Ktorfit interface with HTTP annotations                                                   |
| `model/`            | Data classes for Telegram entities                                                        |
| `form/`             | Multipart form wrappers for file uploads                                                  |
| `query/`            | Extension functions for GET parameters                                                    |
| `type/`             | Handwritten types (TelegramResponse, InputFile)                                           |
| `constant/`         | Handwritten constants (ChatAction, ParseMode, ChatType, DiceEmoji, etc.)                  |
| `union/`            | Union type handling for multi-return methods                                              |
| `event/`            | Generated event types from Update model                                                   |
| `extension/`        | Handwritten extension functions (keyboards, messages)                                     |
| `plugin/`           | Handwritten Ktor client plugins (FileDownload, LongPolling, ServerError)                  |
| `exception/`        | Handwritten exception types (TelegramErrorResponseException, UnrecognizedUpdateException) |

### Client Module (`:client`)

High-level HTTP client wrapper:

- Pre-configured `TelegramBotClient` with sensible defaults
- Automatic retry for transient failures (3 retries, respects `retry_after`)
- Rate limiting compliance (429 backoff with jitter between 1-2 seconds)
- Long polling optimization (35-second timeout for getUpdates via `TelegramLongPollingPlugin`)
- File download support via `TelegramFileDownloadPlugin`
- Test environment support (`useTestEnvironment`)
- Configurable error response handling (`throwOnErrorResponse`, defaults to `true`)

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

Handlers receive a `CoroutineScope`. Any coroutines launched inside a handler will be awaited before the dispatch
completes.

```kotlin
commandEndpoint("process") {
    launch { task1() }
    launch { task2() }
    // Both complete before dispatch finishes
}
```

For fire-and-forget tasks that outlive the current handler, use `applicationScope`:

```kotlin
commandEndpoint("background") {
    applicationScope.launch {
        delay(10.seconds)
        client.sendMessage(chatId.toString(), "Delayed message")
    }
}
```

## Lifecycle

```
  NEW ──────> RUNNING ──────> STOPPING ──────> STOPPED
   │                               ▲
   └─────────── stop() ────────────┘
         (before start)
```

The `TelegramBotApplication` tracks its state:

- `startTime: Instant?` - Set when `start()` is called, `null` before that.
- `start()` - Can only be called once. Transitions `NEW` -> `RUNNING`.
- `stop(gracePeriod)` - Idempotent. Returns a `Job`. Transitions to `STOPPING` -> `STOPPED`.
- `stopSuspend(gracePeriod)` - Suspends until bot and all `applicationScope` coroutines have stopped.
- `join()` - Suspends until the main job completes.

### Graceful Shutdown

1. Transition to `STOPPING` -> new updates rejected with `TelegramBotShuttingDownException`
2. Call `TelegramUpdateSource.stop()` -> cuts off update source immediately
3. Wait within grace period (default: 10 seconds) for main job to complete
4. Force cancel if timeout
5. Call `TelegramUpdateSource.onFinalize()` for final cleanup
6. Cancel `applicationScope`

### Unrecognized Updates

When Telegram introduces new update types not yet supported, `Update.toTelegramBotEvent()` throws
`UnrecognizedUpdateException`. By default, these are logged as warnings and skipped. Customize with
the `onUnrecognizedUpdate` parameter:

```kotlin
val app = TelegramBotApplication(
    client = client,
    updateSource = updateSource,
    interceptors = interceptors,
    eventDispatcher = dispatcher,
    onUnrecognizedUpdate = { client, update ->
        logger.warn { "Unknown update type: $update" }
    }
)
```

## Exception Handling

Exception handling occurs in two places: the `LongPollingTelegramUpdateSource` (per-update) and the
`TelegramBotApplication` (lifecycle).

### In Update Processing (LongPollingTelegramUpdateSource)

Behavior differs by processing mode:

#### SEQUENTIAL Mode

| Exception                            | Behavior                | Offset       |
|--------------------------------------|-------------------------|--------------|
| `TelegramBotShuttingDownException`   | Breaks processing loop  | Not advanced |
| `CancellationException` (`isActive`) | Logs warning, continues | Advanced     |
| Other `Throwable`                    | Logs error, continues   | Advanced     |

#### CONCURRENT_BATCH Mode

| Exception                            | Behavior                            | Offset                    |
|--------------------------------------|-------------------------------------|---------------------------|
| `TelegramBotShuttingDownException`   | Sets abort flag, cancels child task | Not advanced (if aborted) |
| `CancellationException` (`isActive`) | Logs warning, cancels child task    | Advanced (batch)          |
| Other `Throwable`                    | Logs error, swallows                | Advanced (batch)          |

#### CONCURRENT Mode

| Exception                            | Behavior                         | Offset           |
|--------------------------------------|----------------------------------|------------------|
| `TelegramBotShuttingDownException`   | Cancels child task               | Advanced (batch) |
| `CancellationException` (`isActive`) | Logs warning, cancels child task | Advanced (batch) |
| Other `Throwable`                    | Logs error, swallows             | Advanced (batch) |

**Note:** In CONCURRENT and CONCURRENT_BATCH modes, the offset is always advanced to the last update in the batch
(unless shutdown aborts the batch in CONCURRENT_BATCH). Individual update failures do not prevent offset advancement.

### In Application Lifecycle

| Exception                          | Behavior                                      |
|------------------------------------|-----------------------------------------------|
| `TelegramBotShuttingDownException` | Signals framework shutdown, aborts processing |
| `CancellationException`            | Bot lifecycle cancellation                    |
| Other `Throwable`                  | Logged as error, triggers graceful shutdown   |

### Resource Management

- You can create multiple `TelegramBotApplication` instances sharing the same `TelegramBotClient`.
- `TelegramBotApplication` does NOT close `TelegramBotClient.httpClient` when stopped.
- For some `HttpClientEngine` implementations, you must explicitly close `httpClient` to release resources.

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
