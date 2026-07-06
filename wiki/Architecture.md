# Architecture

The project is split into generated protocol bindings, a configured client, and an application framework.

## Modules

```text
User code
  |
  v
:application
  - TelegramBotApplication
  - update sources
  - interceptors
  - dispatchers
  |
  v
:client
  - TelegramBotClient
  |
  v
:protocol
  - TelegramBotApi
  - generated models
  - generated events
  - forms, queries, body helpers
  - handwritten InputFile, TelegramResponse, plugins
  |
  v
Telegram Bot API
```

## Request Flow

1. `TelegramUpdateSource` receives an `Update`.
2. `Update.toTelegramBotEvent()` converts it to a generated `TelegramBotEvent`.
3. `TelegramEventPipeline` creates a `TelegramBotEventContext`.
4. Interceptors run as an onion pipeline.
5. The dispatcher handles the event.
6. Handler code calls `TelegramBotApi` through the context `client`.

```text
UpdateSource -> Update -> TelegramBotEvent -> Interceptors -> Dispatcher -> Handler -> Telegram API
```

## Protocol Module

`protocol` contains generated Telegram Bot API definitions and a small set of handwritten support types.

| Area                       | Description                                      |
|----------------------------|--------------------------------------------------|
| `TelegramBotApi.kt`        | Ktorfit interface                                |
| `model/`                   | Generated serializable Telegram models           |
| `model/Bodies.kt`          | Generated JSON body scatter extensions           |
| `form/`                    | Generated multipart form wrappers and extensions |
| `query/`                   | Generated query serialization extensions         |
| `event/`                   | KSP-generated event classes from `Update`        |
| `union/`                   | Generated response union type support            |
| `type/TelegramResponse.kt` | Handwritten response wrapper                     |
| `type/InputFile.kt`        | Handwritten upload type                          |
| `plugin/`                  | Handwritten Ktor client plugins                  |

Generated protocol sources are marked and should not be edited manually.

## Client Module

`TelegramBotClient` wraps `TelegramBotApi` with defaults:

- JSON content negotiation
- Telegram server error handling
- retry support for transient failures and rate limits
- long-polling-friendly timeout plugin
- file download URL rewriting
- optional Telegram test environment support

The application does not close `TelegramBotClient.httpClient` when stopped. Close shared clients yourself when the
process is done with them.

## Application Module

`TelegramBotApplication` owns lifecycle and update processing.

```kotlin
val app = TelegramBotApplication(
    client = client,
    updateSource = updateSource,
    interceptors = listOf(loggingInterceptor()),
    eventDispatcher = dispatcher
)
```

The convenience long-polling factory creates the client and update source:

```kotlin
val app = TelegramBotApplication.longPolling(
    botToken = "YOUR_TOKEN",
    eventDispatcher = dispatcher
)
```

An application instance can only be started once.

## Update Sources

| Source                            | Purpose                                            |
|-----------------------------------|----------------------------------------------------|
| `LongPollingTelegramUpdateSource` | Fetches updates with `getUpdates`                  |
| `MockTelegramUpdateSource`        | Reads updates from a `Channel<Update>` for tests   |
| `SimpleTelegramUpdateSource`      | Lets external code push updates into the framework |
| `WebhookTelegramUpdateSource`     | Receives updates through an embedded Ktor server   |

`WebhookTelegramUpdateSource` lives in `:application-updatesource-webhook`.

See [Update Sources](Update-Sources) for processing modes, queue integration, and testing sources.

## Interceptor Pipeline

Interceptors wrap all dispatchers.

```kotlin
val interceptor: TelegramEventInterceptor = { context ->
    println("before ${context.event.updateId}")
    process(context)
    println("after ${context.event.updateId}")
}
```

The first interceptor in the list is the outermost layer. Not calling `process(context)` short-circuits the event.

## Dispatchers

`TelegramEventDispatcher` is the final routing abstraction:

```kotlin
interface TelegramEventDispatcher {
    suspend fun dispatch(context: TelegramBotEventContext<TelegramBotEvent>)
}
```

Built-in implementations:

- `SimpleTelegramEventDispatcher` delegates every event to one lambda.
- `HandlerTelegramEventDispatcher` executes a filter/handler route tree.

## Handler DSL

The handler DSL is based on composable filters and explicit handlers.

```kotlin
val routes = handling {
    message {
        privateChat {
            text("hello") {
                handle {
                    replyMessage("Hi!")
                }
            }
        }
    }

    command("start") {
        handle {
            replyMessage("Welcome!")
        }
    }

    handle {
        println("Unhandled event: $event")
    }
}
```

Route matching is depth-first with full backtracking. A branch that matches but does not consume the event allows the
dispatcher to try later sibling branches. A `handle` consumes the event.

Route blocks run at dispatcher construction time. Runtime side effects belong in filter predicates or `handle` blocks.

## Structured Concurrency

`handle` receives a `CoroutineScope`. Child coroutines launched inside it complete before dispatch returns.

```kotlin
command("process") {
    handle {
        launch { processPartA() }
        launch { processPartB() }
    }
}
```

Use `applicationScope` for work that should continue after the current handler returns:

```kotlin
command("background") {
    handle {
        applicationScope.launch {
            delay(10.seconds)
            client.sendMessage(event.message.chat.id.toString(), "Delayed message")
        }
    }
}
```

## Lifecycle

```text
NEW -> RUNNING -> STOPPING -> STOPPED
```

- `start()` transitions from `NEW` to `RUNNING`.
- `stop(gracePeriod)` starts shutdown and returns a `Job`.
- `stopSuspend(gracePeriod)` waits for shutdown and the application scope.
- `join()` waits for the main update-source job.

Graceful shutdown:

1. State becomes `STOPPING`.
2. New updates are rejected with `TelegramBotShuttingDownException`.
3. `updateSource.stop(gracePeriod)` cuts off the source.
4. Existing handlers are allowed to finish within the grace period.
5. `updateSource.onFinalize()` runs cleanup.
6. The application scope is cancelled.

## Long Polling Semantics

| Mode               | Offset behavior                                                                        |
|--------------------|----------------------------------------------------------------------------------------|
| `SEQUENTIAL`       | Advances after each successful or failed business update; preserves offset on shutdown |
| `CONCURRENT_BATCH` | Advances after the batch unless shutdown aborts the batch                              |
| `CONCURRENT`       | Advances after launching the batch                                                     |

Business exceptions are logged so one bad update does not permanently block polling. Shutdown exceptions preserve data
where the processing mode can still avoid acknowledging unprocessed updates.

## Unrecognized Updates

When Telegram adds a new update type, `Update.toTelegramBotEvent()` can throw `UnrecognizedUpdateException`. The
application logs and skips it by default. Override `onUnrecognizedUpdate` to store or report it.

```kotlin
val app = TelegramBotApplication(
    client = client,
    updateSource = updateSource,
    interceptors = emptyList(),
    eventDispatcher = dispatcher,
    onUnrecognizedUpdate = { _, update ->
        logger.warn { "Unknown update type: $update" }
    }
)
```

## Dependency Graph

```text
application -> client -> protocol
application-updatesource-webhook -> application
protocol-update-codegen -> protocol-annotation
protocol -> protocol-annotation
```
