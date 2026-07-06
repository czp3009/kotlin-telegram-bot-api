# Update Sources

`TelegramUpdateSource` controls how raw Telegram `Update` objects enter the application.

```kotlin
interface TelegramUpdateSource {
    suspend fun start(consume: suspend (Update) -> Unit)
    suspend fun stop(gracePeriod: Duration)
    suspend fun onFinalize()
}
```

`TelegramBotApplication` converts each `Update` to `TelegramBotEvent`, runs interceptors, and then calls the dispatcher.

## Long Polling

The simple factory uses `LongPollingTelegramUpdateSource` with default settings:

```kotlin
val app = TelegramBotApplication.longPolling(
    botToken = "YOUR_TOKEN",
    eventDispatcher = dispatcher
)
```

Use explicit construction for `allowedUpdates`, processing mode, concurrency limits, timeout, or fast-fail behavior.

```kotlin
val client = TelegramBotClient(botToken = "YOUR_TOKEN")
val updateSource = LongPollingTelegramUpdateSource(
    client = client,
    allowedUpdates = listOf("message", "callback_query"),
    processingMode = ProcessingMode.CONCURRENT_BATCH,
    maxPendingUpdates = 50,
    getUpdatesTimeout = 30,
    fastFail = false,
)

val app = TelegramBotApplication(
    client = client,
    updateSource = updateSource,
    interceptors = emptyList(),
    eventDispatcher = dispatcher
)
```

### Processing Modes

| Mode               | Behavior                                                       | Offset semantics                                            |
|--------------------|----------------------------------------------------------------|-------------------------------------------------------------|
| `SEQUENTIAL`       | Processes one update at a time                                 | Advances per processed update; preserves offset on shutdown |
| `CONCURRENT_BATCH` | Processes a fetched batch concurrently and waits for the batch | Advances after the batch unless shutdown aborts it          |
| `CONCURRENT`       | Launches updates concurrently without waiting for a batch      | Advances after launching the batch                          |

`CONCURRENT` is the default. Use `maxPendingUpdates` to cap concurrently running update handlers in concurrent modes.

```kotlin
val updateSource = LongPollingTelegramUpdateSource(
    client = client,
    processingMode = ProcessingMode.CONCURRENT,
    maxPendingUpdates = 10,
)
```

### Polling Failures

When `fastFail = false`, polling errors are logged and retried. If Telegram returns `retry_after`, the source delays for
that duration before polling again. When `fastFail = true`, polling exceptions propagate out of the update source.

### Shutdown

`stop(gracePeriod)` cancels the internal fetch scope immediately. `onFinalize()` sends a final `getUpdates` call with
the last known offset when there is an offset to acknowledge.

## Mock Update Source

`MockTelegramUpdateSource` reads from a `Channel<Update>` and is meant for tests.

```kotlin
val channel = Channel<Update>(Channel.UNLIMITED)
val updateSource = MockTelegramUpdateSource(channel)

val app = TelegramBotApplication(
    client = client,
    updateSource = updateSource,
    interceptors = emptyList(),
    eventDispatcher = dispatcher
)

app.start()
channel.send(update)
app.stopSuspend()
```

The source keeps the channel open when stopped. The source itself supports repeated start/stop cycles, but a
`TelegramBotApplication` instance is single-use; create a new application instance for another application lifecycle.

## Simple Update Source

`SimpleTelegramUpdateSource` lets external code push updates synchronously.

```kotlin
val updateSource = SimpleTelegramUpdateSource()

val app = TelegramBotApplication(
    client = client,
    updateSource = updateSource,
    interceptors = emptyList(),
    eventDispatcher = dispatcher
)

app.start()

messageQueue.consume { message ->
    val update = decodeUpdate(message)
    updateSource.push(update)
}
```

`push(update)` calls the current consumer and returns only after processing finishes. Exceptions propagate to the
caller,
which makes it suitable for message queue ack/nack decisions.

| Result                             | Suggested queue action                                 |
|------------------------------------|--------------------------------------------------------|
| No exception                       | Ack                                                    |
| `TelegramBotShuttingDownException` | Nack/retry                                             |
| `CancellationException`            | Nack/retry                                             |
| `IllegalStateException`            | Nack/retry after the worker is ready                   |
| Other `Throwable`                  | Depends on whether the error is permanent or transient |

Calling `stop()` clears the consumer callback and lets `start()` return. It does not cancel external coroutines already
inside `push()`.

## Webhook Update Source

`WebhookTelegramUpdateSource` is provided by `:application-updatesource-webhook` and receives updates through an
embedded Ktor server.

```kotlin
val updateSource = WebhookTelegramUpdateSource(
    applicationEngineFactory = Netty,
    path = "/webhook",
    configureEngine = {
        connector {
            host = "0.0.0.0"
            port = 8443
        }
    }
)
```

See [Webhook](Webhook) for HTTPS setup and webhook registration.

## Application Lifecycle

`TelegramBotApplication` is single-use:

```text
NEW -> RUNNING -> STOPPING -> STOPPED
```

After an application stops, create a new application instance. Some update source implementations can be reused by a
new owner, but the application object cannot be started again.
