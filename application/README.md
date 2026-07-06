# Telegram Bot Application Framework

A Kotlin coroutines-based Telegram bot application framework providing lifecycle management, event dispatching, Handler
DSL, Command DSL, and interceptor pipeline.

## Project Compatibility

- Kotlin 2.4.0, with stable context parameters. No `-Xcontext-parameters` compiler flag is required.
- The repository is built with Gradle 9.6.1 and Ktor 3.5.1.
- For build-only verification, use `./gradlew assemble`. Some test tasks can make real Telegram API calls.
- Supported targets follow the shared multiplatform configuration: JVM, Android, JS, WASM, Linux, Windows, Android
  Native, macOS ARM64, iOS, watchOS ARM/simulator ARM64, and tvOS ARM/simulator ARM64.

## Table of Contents

- [Quick Start](#quick-start)
- [Lifecycle](#lifecycle)
- [Update Sources](#update-sources)
    - [LongPollingTelegramUpdateSource](#longpollingtelegramupdatesource)
    - [MockTelegramUpdateSource](#mocktelegramupdatesource)
  - [SimpleTelegramUpdateSource](#simpletelegramupdatesource)
  - [WebhookTelegramUpdateSource](#webhooktelegramupdatesource)
- [Event Dispatchers](#event-dispatchers)
    - [SimpleTelegramEventDispatcher](#simpletelegrameventdispatcher)
    - [HandlerTelegramEventDispatcher](#handlertelegrameventdispatcher)
- [Concurrency Model](#concurrency-model)
    - [Update Processing Modes](#update-processing-modes)
    - [Handler Scope vs applicationScope](#handler-scope-vs-applicationscope)
    - [Limiting Concurrency](#limiting-concurrency)
- [Handler DSL](#handler-dsl)
- [Command DSL](#command-dsl)
    - [Subcommands](#subcommands)
    - [Argument Validation](#argument-validation)
    - [Error Handling](#error-handling)
- [Interceptors](#interceptors)
    - [Onion Model](#onion-model)
    - [Short-Circuit](#short-circuit)
    - [Attributes](#attributes)
    - [Built-in Interceptors](#built-in-interceptors)
  - [Custom Handler Filters](#custom-handler-filters)

## Quick Start

Create a simple long-polling bot using the factory method:

```kotlin
val routes = handling {
  command("start") {
    handle {
      replyMessage("Welcome!")
    }
  }
  command("ping") {
    handle {
      replyMessage("pong")
    }
    }
}

val app = TelegramBotApplication.longPolling(
    botToken = "YOUR_BOT_TOKEN",
    eventDispatcher = HandlerTelegramEventDispatcher(routes)
)

app.start()
app.join() // Suspend until stopped
```

## Lifecycle

`TelegramBotApplication` manages the complete lifecycle of the bot, including startup, running, and graceful shutdown.

### State Transitions

```
NEW -> RUNNING -> STOPPING -> STOPPED
```

- **NEW**: Initial state, not yet started
- **RUNNING**: Running and processing updates
- **STOPPING**: Shutting down, waiting for in-flight tasks
- **STOPPED**: Completely stopped

### Start and Stop

```kotlin
val app = TelegramBotApplication.longPolling(
    botToken = "YOUR_TOKEN",
    eventDispatcher = dispatcher
)

// Start the bot (can only be started once)
app.start()

// Wait for the bot to stop (only waits for main job, not applicationScope coroutines)
app.join()

// Graceful shutdown with max 5 seconds wait
app.stop(5.seconds)

// Or suspending version that waits for all coroutines including applicationScope
app.stopSuspend(5.seconds)
```

**Automatic Shutdown:**

When the `updateSource` exits (normally, with exception, or cancelled), graceful shutdown is triggered automatically.
This ensures the bot stops cleanly even if the update source fails due to network errors or authentication issues.

**`join()` vs `stopSuspend()`:**

- `join()` - Only waits for the main update processing job to complete. Coroutines launched in `applicationScope` may
  still be running when this returns.
- `stopSuspend()` - Waits for both the main job AND all `applicationScope` coroutines to complete. Use this when you
  need to ensure all background work is finished.

### Graceful Shutdown Flow

1. State transitions to `STOPPING`, new updates are rejected
2. Update source is cut off immediately
3. Wait within grace period for in-flight handlers to complete
4. Force cancel if timeout
5. Call `onFinalize` for final cleanup

```kotlin
// Test: Graceful shutdown cancels long-running handlers
val dispatcher = SimpleTelegramEventDispatcher {
    // Simulate long-running work
    CompletableDeferred<Unit>().await() // Never completes
}

// ... setup and start application

// Stop with short timeout
app.stop(100.milliseconds).join()

// Handler is cancelled with CancellationException
```

## Update Sources

The `TelegramUpdateSource` interface defines how the application receives updates from Telegram. Implementations control
the update fetching strategy and processing semantics.

```kotlin
interface TelegramUpdateSource {
    // Start fetching updates and pass them to the consumer callback
    suspend fun start(consume: suspend (Update) -> Unit)

    // Trigger source shutdown (cuts off network polling)
    // gracePeriod: The grace period for graceful shutdown. Implementation may ignore this parameter.
    suspend fun stop(gracePeriod: Duration)

    // Perform final cleanup (e.g., sending Final ACK)
    suspend fun onFinalize()
}
```

### LongPollingTelegramUpdateSource

The primary update source for production use, implementing long polling with configurable processing modes:

```kotlin
val client = TelegramBotClient(botToken = "YOUR_TOKEN")

val updateSource = LongPollingTelegramUpdateSource(
  client = client,
    allowedUpdates = listOf("message", "callback_query"),  // Filter update types
    processingMode = ProcessingMode.CONCURRENT_BATCH,      // Processing strategy
    maxPendingUpdates = 100,                               // Limit concurrent processing
    fastFail = false,                                      // Retry on polling errors
    coroutineDispatcher = Dispatchers.IO                   // Custom dispatcher
)

val app = TelegramBotApplication(
  client = client,
    updateSource = updateSource,
  interceptors = emptyList(),
    eventDispatcher = dispatcher
)
```

**Parameters:**

| Parameter             | Description                                                     |
|-----------------------|-----------------------------------------------------------------|
| `client`              | TelegramBotApi instance for fetching updates                    |
| `allowedUpdates`      | List of update types to receive (null = all types)              |
| `processingMode`      | How updates are processed (see Concurrency Model)               |
| `maxPendingUpdates`   | Maximum concurrent handlers in CONCURRENT mode                  |
| `fastFail`            | If true, polling errors propagate; if false, retry with backoff |
| `coroutineDispatcher` | Dispatcher for internal fetch scope                             |

**Exception Handling:**

- `TelegramBotShuttingDownException` - Signals graceful shutdown. Batch aborted, offset NOT advanced (zero data loss).
- `CancellationException` / `Exception` - Business logic failure. Update skipped, offset advanced (prevents infinite
  loops).

**Graceful Shutdown:**

1. `stop(gracePeriod)` cancels the fetch scope immediately (gracePeriod is ignored for long polling)
2. `onFinalize()` sends a final `getUpdates` with the last offset as ACK

### MockTelegramUpdateSource

A test-oriented update source that reads from a `Channel<Update>`:

```kotlin
val client = TelegramBotClient(botToken = "TEST_TOKEN")

// Create a channel for test updates
val testChannel = Channel<Update>(Channel.UNLIMITED)

// Create mock source
val mockSource = MockTelegramUpdateSource(testChannel)

// Use in tests
val app = TelegramBotApplication(
    client = client,
    updateSource = mockSource,
    interceptors = emptyList(),
    eventDispatcher = dispatcher
)

app.start()

// Feed test updates
testChannel.trySend(testUpdate1)
testChannel.trySend(testUpdate2)

app.stopSuspend()
```

**Key Features:**

- Supports infinite start/stop cycles (unlike network-based sources)
- `stop(gracePeriod)` suspends consumption but keeps the channel open (gracePeriod is ignored)
- Keeps the channel open for a later source cycle
- Naturally completes when the source channel is closed

`TelegramBotApplication` instances cannot be restarted. Create a new application instance for another cycle.

### SimpleTelegramUpdateSource

A stateless update source that allows external injection of updates via `push()`. Designed for distributed systems
where updates are received by one component and processed by another:

```kotlin
// Architecture pattern:
// Webhook Server -> Message Queue (Kafka/RabbitMQ) -> Worker Node (SimpleTelegramUpdateSource)

val client = TelegramBotClient(botToken = "YOUR_TOKEN")
val source = SimpleTelegramUpdateSource()

val app = TelegramBotApplication(
    client = client,
    updateSource = source,
    interceptors = emptyList(),
    eventDispatcher = dispatcher
)
app.start()

// Push updates from external source (e.g., pubsub subscriber)
pubSubSubscriber.subscribe { message ->
  val update = parseUpdate(message)
  source.push(update)
}

// Graceful shutdown
app.stop()
```

**Key Features:**

- Stateless design with support for multiple start/stop cycles
- `push(update)` processes updates synchronously via the consumer callback
- Thread-safe for concurrent use from multiple threads
- `start()` suspends until `stop()` is called

**Pub/Sub Acknowledgment Strategy:**

When consuming from a message queue, use the exception type to decide whether to ack or nack:

| Exception                          | Action     | Reason                                                         |
|------------------------------------|------------|----------------------------------------------------------------|
| Success (no exception)             | Ack        | Message processed successfully                                 |
| `TelegramBotShuttingDownException` | Nack/Retry | Bot is shutting down; redeliver to another worker              |
| `CancellationException`            | Nack/Retry | Coroutine cancelled; message may be partially processed        |
| `IllegalStateException`            | Nack/Retry | Source not started; retry after source is ready                |
| Other `Throwable`                  | Depends    | Business error; ack to avoid retry loop, or nack for transient |

**Example with Google PubSub:**

```kotlin
val subscriber = pubSubSubscriber.subscribe { message ->
  val update = parseUpdate(message)
  try {
    source.push(update)
    message.ack()  // Success
  } catch (e: TelegramBotShuttingDownException) {
    message.nack()  // Redeliver to another worker
  } catch (e: CancellationException) {
    message.nack()  // Redeliver later
  } catch (e: IllegalStateException) {
    message.nack()  // Source not ready, retry
  } catch (e: Throwable) {
    logger.error(e) { "Business error processing update" }
    message.ack()  // Ack to avoid retry loop for permanent errors
  }
}
```

**Concurrent Push with Backpressure:**

When receiving updates concurrently, use a `Semaphore` to limit concurrency. Manage the external scope
independently to allow graceful shutdown:

```kotlin
val source = SimpleTelegramUpdateSource()
val client = TelegramBotClient(botToken = "YOUR_TOKEN")
val app = TelegramBotApplication(
    client = client,
    updateSource = source,
    interceptors = emptyList(),
    eventDispatcher = dispatcher
)
app.start()

// External scope for consuming from pubsub
val consumerScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
val semaphore = Semaphore(permits = 10)  // Max 10 concurrent updates

val consumerJob = consumerScope.launch {
  pubSubSubscriber.subscribe { message ->
    semaphore.withPermit {
      val update = parseUpdate(message)
      source.push(update)
    }
  }
}

// Graceful shutdown: stop subscriber first, then cancel scope, then stop app
pubSubSubscriber.stop()  // Stop receiving new messages
consumerJob.join()       // Wait for in-flight messages to complete processing
app.stop()               // Then stop the application
```

**Important:** Calling `stop()` does NOT cancel coroutines that are currently executing `push()`.
If external code calls `push()` from its own coroutines (not managed by `TelegramBotApplication`),
those coroutines will continue running until they complete naturally. Callers are responsible for
managing their own coroutine lifecycle and cancellation.

**Graceful Shutdown:**

1. `stop(gracePeriod)` clears the consumer callback and signals `start()` to return
2. The source can be started again by another owner after shutdown
3. `onFinalize()` is a no-op since all state is in-memory

`TelegramBotApplication` instances are single-use; create a new application instance after stopping one.

### WebhookTelegramUpdateSource

A webhook-based update source available in the `application-updatesource-webhook` module. It receives updates via an
embedded Ktor server.

```kotlin
// Add dependency: com.hiczp:telegram-bot-api-application-updatesource-webhook

val client = TelegramBotClient(botToken = "YOUR_TOKEN")
val webhookSource = WebhookTelegramUpdateSource(
  applicationEngineFactory = CIO,  // or Netty, Jetty, etc.
  path = "/webhook",
  configureEngine = {
    connector {
      port = 8443
      host = "0.0.0.0"
    }
    // SSL configuration for production
  }
)

val app = TelegramBotApplication(
    client = client,
    updateSource = webhookSource,
    interceptors = emptyList(),
    eventDispatcher = dispatcher
)
```

**Parameters:**

| Parameter                  | Description                                         |
|----------------------------|-----------------------------------------------------|
| `applicationEngineFactory` | Ktor engine factory (CIO, Netty, Jetty, etc.)       |
| `path`                     | Webhook endpoint path (default: "/")                |
| `configureEngine`          | Engine configuration lambda (port, host, SSL, etc.) |
| `configureApplication`     | Additional Ktor application configuration           |

**Exception Handling:**

- `CancellationException` - Re-thrown to propagate coroutine cancellation
- `TelegramBotShuttingDownException` - Re-thrown during framework shutdown
- Other exceptions - Logged and re-thrown; Telegram may retry the webhook request

**Graceful Shutdown:**

1. `stop(gracePeriod)` stops the embedded server with the specified grace period
2. Pending HTTP handlers are cancelled via Ktor's lifecycle
3. `onFinalize()` is called after the server has stopped

**Key Differences from LongPolling:**

- Updates are processed synchronously within the HTTP request handler
- Handler failures can fail the HTTP request, so Telegram may retry
- Requires public HTTPS endpoint with valid certificate (or local tunnel for development)

## Event Dispatchers

The `TelegramEventDispatcher` interface routes events to business logic handlers. Two implementations are available for
different complexity levels:

```kotlin
interface TelegramEventDispatcher {
    suspend fun dispatch(context: TelegramBotEventContext<TelegramBotEvent>)
}
```

### SimpleTelegramEventDispatcher

A minimal dispatcher that delegates all events to a single lambda. Best for simple bots or testing:

```kotlin
val dispatcher = SimpleTelegramEventDispatcher { context ->
    when (val event = context.event) {
        is MessageEvent -> {
            context.client.sendMessage(
                chatId = event.message.chat.id.toString(),
                text = event.message.text ?: "No text"
            )
        }
        is CallbackQueryEvent -> {
            context.client.answerCallbackQuery(event.callbackQuery.id)
        }
        else -> println("Unhandled event: ${event.updateId}")
    }
}
```

### HandlerTelegramEventDispatcher

A full-featured dispatcher with type-safe routing DSL. Best for production bots:

```kotlin
val routes = handling {
    // Command handlers
  command("start") {
    handle {
      replyMessage("Welcome!")
    }
    }

    // Event type matching
  message {
    text("hello") {
      handle { /* exact match */ }
    }
    text(Regex("(?i)^hello")) {
      handle { /* regex */ }
    }
    photo {
      handle { /* photo message */ }
    }
    }

  callbackQuery {
    data("confirm") {
      handle { /* callback data */ }
    }
    }

    // Dead letter handler (catches unhandled events)
    handle {
        println("Unhandled event: ${event.updateId}")
    }
}

val dispatcher = HandlerTelegramEventDispatcher(routes)
```

**Dead Letter Handling:**

Two options for handling unmatched events:

1. **Root-level `handle`** (recommended): Add at the end of routing DSL
   ```kotlin
   handling {
       // ... routes ...
       handle { /* catches all unhandled events */ }
   }
   ```

2. **Override `deadLetter` method**: Subclass for custom handling
   ```kotlin
   class MyDispatcher(rootNode: RouteNode) : HandlerTelegramEventDispatcher(rootNode) {
       override suspend fun deadLetter(context: TelegramBotEventContext<TelegramBotEvent>) {
           myDeadLetterQueue.send(context.event)
       }
   }
   ```

## Concurrency Model

### Update Processing Modes

The `LongPollingTelegramUpdateSource` supports three processing modes controlled by `processingMode` parameter:

```kotlin
val client = TelegramBotClient(botToken = "YOUR_TOKEN")
val updateSource = LongPollingTelegramUpdateSource(
  client = client,
    processingMode = ProcessingMode.CONCURRENT_BATCH,  // Default is CONCURRENT
)

val app = TelegramBotApplication(
  client = client,
  updateSource = updateSource,
  interceptors = emptyList(),
    eventDispatcher = dispatcher
)
```

| Mode               | Concurrency             | Backpressure                   | Delivery Guarantee          |
|--------------------|-------------------------|--------------------------------|-----------------------------|
| `SEQUENTIAL`       | One at a time           | Natural flow control           | At-Least-Once               |
| `CONCURRENT_BATCH` | Concurrent within batch | Batch-level                    | At-Least-Once (Recommended) |
| `CONCURRENT`       | Fully concurrent        | None (use `maxPendingUpdates`) | At-Most-Once                |

**Default mode is `CONCURRENT`**, meaning handlers are invoked concurrently. **Global mutable state is NOT safe**
without
synchronization:

```kotlin
// DANGEROUS: Race condition in concurrent mode
val processedTexts = mutableListOf<String>()

val dispatcher = SimpleTelegramEventDispatcher { context ->
    val text = (context.event as MessageEvent).message.text ?: ""
    processedTexts.add(text)  // NOT thread-safe!
}

// SAFE: Use thread-safe collections or synchronization
val processedTexts = ConcurrentLinkedQueue<String>()
// Or use SEQUENTIAL mode for strict ordering
```

### Handler Scope vs applicationScope

Understanding coroutine scopes is critical for correct concurrency behavior:

#### Handler Scope (Structured Concurrency)

`launch` inside a handler creates child coroutines that **must complete before dispatch returns**:

```kotlin
command("process") {
    handle {
        // These complete before the handler returns
        launch {
            val result1 = slowOperation1()
            sendMessage("Result 1: $result1")
        }
        launch {
            val result2 = slowOperation2()
            sendMessage("Result 2: $result2")
        }
        // Handler waits for all launches
    }
}
```

**Key Points**:

- Child coroutines are cancelled when handler is cancelled
- Exceptions propagate to parent (unless using `SupervisorJob`)
- Guarantees completion before next stage

#### applicationScope (Fire-and-Forget)

Use `applicationScope` for tasks that should **outlive the current handler**:

```kotlin
command("background") {
    handle {
        // In handler scope - waits for completion
        launch {
            println("Completes before handler returns")
        }

        // In application scope - independent lifecycle
        applicationScope.launch {
            delay(10.seconds)
            println("Continues after handler returned")
            // Cancelled only when application stops
        }

        // Handler returns immediately, applicationScope continues
    }
}
```

**Use Cases for `applicationScope`**:

- Background tasks that shouldn't block the response
- Periodic jobs initiated by a command
- Deferred notifications

**Warning**: `applicationScope` coroutines must check `isActive` for proper cancellation:

```kotlin
applicationScope.launch {
    while (isActive) {  // Check cancellation
        delay(1000)
        doWork()
    }
}
```

### Limiting Concurrency

Use `maxPendingUpdates` to limit concurrent handlers in `CONCURRENT` mode:

```kotlin
val client = TelegramBotClient(botToken = "YOUR_TOKEN")
val updateSource = LongPollingTelegramUpdateSource(
  client = client,
    processingMode = ProcessingMode.CONCURRENT,
    maxPendingUpdates = 10,  // Max 10 concurrent handlers
)

val app = TelegramBotApplication(
  client = client,
  updateSource = updateSource,
  interceptors = emptyList(),
    eventDispatcher = dispatcher
)
```

## Handler DSL

The `handling` DSL builds a route tree from composable filters and explicit handlers.

```kotlin
val routes = handling {
  message {
    privateChat {
      command("start") {
        handle {
          replyMessage("Welcome!")
        }
      }

      text("ping") {
        handle {
          replyMessage("pong")
        }
      }
    }
    }

  callbackQuery {
    data("confirm") {
      handle {
        client.answerCallbackQuery(event.callbackQuery.id)
            }
        }
    }

  handle {
    println("Unhandled event: $event")
    }
}
```

Core rules:

- `filter({ ... }) { ... }` enters a child branch when the suspending predicate returns `true`.
- `filter<T> { ... }` narrows the event type for the child branch.
- Built-in matchers such as `message`, `privateChat`, `command`, `text`, and `photo` are filter wrappers.
- `handle { ... }` runs business logic and consumes the event.
- If a deep branch misses, matching backtracks through ancestors until another sibling branch can be tested.
- A parent `handle` acts as fallback for its branch after child routes miss.

Route blocks run once when the dispatcher is built. Do not put per-update side effects directly in route blocks:

```kotlin
handling {
  command("start") {
    println("runs once while routes are built")

    handle {
      println("runs for every matched update")
    }
    }
}
```

Per-update checks belong in `filter` predicates:

```kotlin
message {
  filter({ authService.isAllowed(event.message.from?.id) }) {
    command("admin") {
      handle {
        replyMessage("admin ok")
      }
    }
  }
}
```

Framework-provided filters are side-effect free. User-defined filters may perform side effects, but filter side effects
are not rolled back if a deeper branch later misses.

Use `include` to compose route modules:

```kotlin
val adminRoutes = handling {
  command("admin") {
    handle { /* ... */ }
  }
}

val mainRoutes = handling {
  command("start") {
    handle { /* ... */ }
  }
    include(adminRoutes)
}
```

## Command DSL

Commands are filters and consume events only when a `handle` runs.

```kotlin
handling {
  command("ping") {
    handle {
      replyMessage("pong")
    }
    }
}
```

Typed arguments use explicit `handle` too:

```kotlin
class BanArgs : BotArguments("Ban a user") {
    val username: String by requireArgument("Username to ban")
    val reason: String by optionalArgument("Ban reason", default = "Violation")
}

handling {
    command("ban", ::BanArgs) {
      handle {
        replyMessage("Banned ${arguments.username}: ${arguments.reason}")
      }
    }
}
```

**Supported Argument Types**:

- `String`
- `Int`, `Long`
- `Double`
- `Boolean` (supports "true"/"false"/"1"/"0"/"yes"/"no"/"on"/"off")
- Enum types

### Subcommands

```kotlin
handling {
    command("admin") {
        // Parent command handler (called when no subcommand matches)
        handle { showAdminHelp() }

      subCommand("status") {
        handle { showStatus() }
        }

        subCommand("user") {
          subCommand("list") { handle { listUsers() } }
          subCommand("add") { handle { addUser() } }
            subCommand("ban", ::BanArgs) {
              handle { banUser(arguments.username) }
            }
        }
    }
}

// /admin           -> showAdminHelp()
// /admin status    -> showStatus()
// /admin user list -> listUsers()
// /admin user ban john -> banUser("john")
```

### Argument Validation

```kotlin
class ValidatedArgs : BotArguments("Validated arguments") {
    val age: Int by requireArgument<Int>("Age")
        .validate { require(it >= 0) { "Age must be non-negative" } }

    val email: String by requireArgument<String>("Email")
        .validate {
            require(it.contains("@")) { "Invalid email format" }
        }
}
```

### Error Handling

By default, when argument parsing fails, a formatted help message is automatically sent to the user:

```kotlin
command("ban", ::BanArgs) {
  handle {
    // If arguments fail to parse, help message is sent automatically
    val username = arguments.username
    sendMessage("Banned $username")
  }
}
```

To customize error handling, provide an `onError` callback:

```kotlin
command("ban", ::BanArgs, onError = { e ->
  sendMessage("Error: ${e.message}")
}) {
  handle {
    // Custom error handling
  }
}
```

To catch exceptions at the interceptor level, rethrow from `onError`:

```kotlin
command("ban", ::BanArgs, onError = { e -> throw e }) {
  handle {
    // ...
  }
}

val interceptor: TelegramEventInterceptor = { context ->
    try {
        this.process(context)
    } catch (e: CommandParseException) {
        val chatId = e.context?.event?.message?.chat?.id
        if (chatId != null) {
            client.sendMessage(chatId, e.generateHelpText())
        }
    }
}
```

## Interceptors

### Onion Model

Interceptors form an "onion model" pipeline where each layer can process before and after inner layers:

```kotlin
val loggingInterceptor: TelegramEventInterceptor = { context ->
    println("Before: ${context.event.updateId}")
    this.process(context) // Pass to next layer
    println("After: ${context.event.updateId}")
}

val app = TelegramBotApplication.longPolling(
    botToken = "TOKEN",
  interceptors = listOf(loggingInterceptor()),
    eventDispatcher = dispatcher
)
```

**Execution order with multiple interceptors**:

```kotlin
val interceptor1: TelegramEventInterceptor = { context ->
    interceptorLog.add("before1")
    try {
        this.process(context)
    } finally {
        interceptorLog.add("after1")
    }
}

val interceptor2: TelegramEventInterceptor = { context ->
    interceptorLog.add("before2")
    try {
        this.process(context)
    } finally {
        interceptorLog.add("after2")
    }
}

// Result: ["before1", "before2", "handler", "after2", "after1"]
```

### Short-Circuit

Interceptors can short-circuit by not calling `process()`:

```kotlin
val authInterceptor: TelegramEventInterceptor = { context ->
    if (context.event is MessageEvent) {
        val userId = context.event.message.from?.id
        if (userId in allowedUserIds) {
            this.process(context) // Continue processing
        } else {
            // Short-circuit: don't call process()
            context.client.sendMessage(
                context.event.message.chat.id.toString(),
                "Unauthorized"
            )
        }
    }
}
```

### Attributes

The `TelegramBotEventContext` provides type-safe attribute storage for sharing data between interceptors and handlers.
This is useful for passing information through the pipeline without modifying the event itself.

```kotlin
import io.ktor.util.AttributeKey

// Define attribute keys
val userAuthenticatedKey = AttributeKey<Boolean>("user_authenticated")
val userDataKey = AttributeKey<UserData>("user_data")

// Set attributes in interceptor
val authInterceptor: TelegramEventInterceptor = { context ->
    val userId = when (val event = context.event) {
        is MessageEvent -> event.message.from?.id
        is CallbackQueryEvent -> event.callbackQuery.from.id
        else -> null
    }

    if (userId != null) {
        val userData = fetchUserData(userId)
        context.attributes.put(userDataKey, userData)
        context.attributes.put(userAuthenticatedKey, true)
    }

    this.process(context)
}

// Read attributes in handler
handling {
  message {
        handle {
            val isAuthenticated = attributes.getOrNull(userAuthenticatedKey) ?: false
            val userData = attributes.getOrNull(userDataKey)

            if (isAuthenticated && userData != null) {
                sendMessage("Welcome back, ${userData.name}!")
            } else {
                sendMessage("Please authenticate first.")
            }
        }
    }
}
```

**Common Use Cases:**

- Authentication/authorization data
- Rate limiting counters
- Request-scoped caching
- User preferences fetched once per request
- Tracing/metrics data

**Key Methods:**

| Method            | Description                  |
|-------------------|------------------------------|
| `put(key, value)` | Store a value                |
| `get(key)`        | Get value (throws if absent) |
| `getOrNull(key)`  | Get value or null            |
| `contains(key)`   | Check if key exists          |
| `remove(key)`     | Remove a value               |

### Built-in Interceptors

#### Logging Interceptor

```kotlin
val app = TelegramBotApplication.longPolling(
    botToken = "TOKEN",
    interceptors = listOf(loggingInterceptor()),
    eventDispatcher = dispatcher
)
```

#### Conversation Interceptor

Supports multi-turn conversations:

```kotlin
val interceptors = listOf(conversationInterceptor())

handling {
  command("survey") {
    handle {
      startConversation(
        timeout = 5.minutes,
        onTimeout = {
          sendMessage("Survey timed out")
        },
        onCancel = {
          sendMessage("Survey cancelled")
        }
      ) {
        send("What is your name?")
        val name = awaitText()

        reply("How old are you, $name?")
        val age = awaitText()

        reply("Thanks $name, you are $age years old")
      }
        }
    }
}
```

**Messaging Methods:**

- `send(text, replyMarkup?)` - Sends a message to the conversation's chat without replying to any specific message.
  Returns `TelegramResponse<Message>` and updates `lastSentMessageId` on success.
- `reply(text, replyToMessageId?)` - Sends a message as a reply. If `replyToMessageId` is null, uses
  `lastAwaitedMessageId`
  to automatically reply to the most recent user input. If no message ID is available, behaves like `send()`.
  Returns `TelegramResponse<Message>` and updates `lastSentMessageId` on success.

**Await Methods:**

- `awaitEvent<T>()` - Awaits any event of type T
- `awaitMessage()` - Awaits the next message event and updates `lastAwaitedMessageId`
- `awaitText()` - Awaits the next text message and updates `lastAwaitedMessageId`
- `awaitCommand()` - Awaits the next command and updates `lastAwaitedMessageId`
- `awaitCallbackQuery()` - Awaits the next callback query event and updates `lastAwaitedMessageId` (if the callback
  has an associated message)
- `awaitReply(messageId?)` - Awaits a reply to a specific message. If `messageId` is null, uses `lastSentMessageId`.
  This is useful for waiting for replies to bot messages sent via `send()` or `reply()`.

**ConversationScope Properties:**

- `lastAwaitedMessageId` - Automatically updated after `awaitMessage()`, `awaitText()`, `awaitCommand()`, and
  `awaitCallbackQuery()` to track the most recent user input. Used by `reply()` for automatic reply targeting.
  **Warning:** This property is mutable, but modifying it directly is generally not recommended. Only modify it if
  you have a specific use case that requires overriding the default tracking behavior.
- `lastSentMessageId` - Automatically updated after `send()` and `reply()` to track the most recent bot message.
  Used by `awaitReply()` for automatic reply waiting. **Warning:** This property is mutable, but modifying it
  directly is generally not recommended. Only modify it if you have a specific use case that requires overriding
  the default tracking behavior.
- `id` - The `ConversationId` identifying this conversation
- `channel` - The channel receiving events for this conversation

**Coroutine Support:**

The `ConversationScope` implements `CoroutineScope`, allowing you to launch child coroutines that are tied to the
conversation's lifecycle. When the conversation ends (completes, times out, or is cancelled), all child coroutines
are automatically cancelled.

**startConversation Parameters:**

| Parameter            | Description                                                                                                               |
|----------------------|---------------------------------------------------------------------------------------------------------------------------|
| `id`                 | The `ConversationId` for this conversation. Defaults to per-user-in-thread-chat conversation.                             |
| `timeout`            | Optional duration after which the conversation times out. Defaults to no timeout.                                         |
| `capacity`           | Channel capacity for buffering events. Defaults to `Channel.UNLIMITED`. See below for options.                            |
| `interceptPredicate` | Function determining which events to intercept. Defaults to `MessageEvent`, `BusinessMessageEvent`, `CallbackQueryEvent`. |
| `cancelPredicate`    | Function determining if an event should cancel the conversation. Defaults to `/cancel` command.                           |
| `onTimeout`          | Callback invoked when the conversation times out.                                                                         |
| `onCancel`           | Callback invoked when the conversation is canceled.                                                                       |
| `block`              | The conversation logic to execute within a `ConversationScope`.                                                           |

**Capacity Options:**

- **`Channel.UNLIMITED`** (default): All matching events are buffered. Use with caution in high-traffic scenarios.
- **Bounded (e.g., `Channel.BUFFERED` or a specific number)**: When the buffer is full, new events are dropped and
  a warning is logged. This protects against memory issues but may lose events.
- **`Channel.RENDEZVOUS`** (capacity = 0): Events are only received when actively awaiting. Events sent while not
  waiting are dropped.

**Intercepted Event Types:**

By default, conversations intercept `MessageEvent`, `BusinessMessageEvent`, and `CallbackQueryEvent`. Customize with
the `interceptPredicate` parameter:

```kotlin
startConversation(
  interceptPredicate = { context ->
    context.event is MessageEvent || context.event is CallbackQueryEvent
  }
) {
  // ...
}
```

The `ConversationId` is a data class with `chatId`, `userId` (optional), and `threadId` (optional):

- `ConversationId(chatId)` - whole chat shares one conversation
- `ConversationId(chatId, userId = userId)` - per-user conversation in a group
- `ConversationId(chatId, threadId = threadId)` - per-thread conversation

### Custom Handler Filters

Reusable handler filters are ordinary extension functions over `HandlerRoute<T>`:

```kotlin
val adminUserIds = setOf(1L, 2L, 3L)

fun HandlerRoute<MessageEvent>.fromAllowedUser(
    allowedUsers: Set<Long>,
    build: HandlerRoute<MessageEvent>.() -> Unit
) = filter({ event.message.from?.id in allowedUsers }, build)

handling {
  message {
    fromAllowedUser(adminUserIds) {
      command("admin") {
        handle {
          replyMessage("Admin command")
        }
      }
    }
    }
}
```
