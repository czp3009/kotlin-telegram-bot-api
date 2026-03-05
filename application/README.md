# Telegram Bot Application Framework

A Kotlin coroutines-based Telegram bot application framework providing lifecycle management, event dispatching, Handler
DSL, Command DSL, and interceptor pipeline.

## Table of Contents

- [Quick Start](#quick-start)
- [Lifecycle](#lifecycle)
- [Update Sources](#update-sources)
    - [LongPollingTelegramUpdateSource](#longpollingtelegramupdatesource)
    - [MockTelegramUpdateSource](#mocktelegramupdatesource)
- [Event Dispatchers](#event-dispatchers)
    - [SimpleTelegramEventDispatcher](#simpletelegrameventdispatcher)
    - [HandlerTelegramEventDispatcher](#handlertelegrameventdispatcher)
- [Concurrency Model](#concurrency-model)
    - [Update Processing Modes](#update-processing-modes)
    - [Handler Scope vs applicationScope](#handler-scope-vs-applicationscope)
    - [Limiting Concurrency](#limiting-concurrency)
- [Handler DSL](#handler-dsl)
    - [Event Type Routing](#event-type-routing)
    - [Text Matching](#text-matching)
    - [Composite Matchers](#composite-matchers)
    - [Fallback Handler](#fallback-handler)
    - [Route Modularization](#route-modularization)
- [Command DSL](#command-dsl)
    - [Simple Commands](#simple-commands)
    - [Commands with Arguments](#commands-with-arguments)
    - [Subcommands](#subcommands)
    - [Argument Validation](#argument-validation)
    - [Error Handling](#error-handling)
- [Interceptors](#interceptors)
    - [Onion Model](#onion-model)
    - [Short-Circuit](#short-circuit)
    - [Attributes](#attributes)
    - [Built-in Interceptors](#built-in-interceptors)
    - [Middleware](#middleware)

## Quick Start

Create a simple long-polling bot using the factory method:

```kotlin
val routes = handling {
    commandEndpoint("start") {
        sendMessage("Welcome!")
    }
    commandEndpoint("ping") {
        replyMessage("pong")
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
NEW → RUNNING → STOPPING → STOPPED
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
    suspend fun stop()

    // Perform final cleanup (e.g., sending Final ACK)
    suspend fun onFinalize()
}
```

### LongPollingTelegramUpdateSource

The primary update source for production use, implementing long polling with configurable processing modes:

```kotlin
val updateSource = LongPollingTelegramUpdateSource(
    client = telegramBotApi,
    allowedUpdates = listOf("message", "callback_query"),  // Filter update types
    processingMode = ProcessingMode.CONCURRENT_BATCH,      // Processing strategy
    maxPendingUpdates = 100,                               // Limit concurrent processing
    fastFail = false,                                      // Retry on polling errors
    coroutineDispatcher = Dispatchers.IO                   // Custom dispatcher
)

val app = TelegramBotApplication(
    botToken = "YOUR_TOKEN",
    updateSource = updateSource,
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

1. `stop()` cancels the fetch scope immediately
2. `onFinalize()` sends a final `getUpdates` with the last offset as ACK

### MockTelegramUpdateSource

A test-oriented update source that reads from a `Channel<Update>`:

```kotlin
// Create a channel for test updates
val testChannel = Channel<Update>(Channel.UNLIMITED)

// Create mock source
val mockSource = MockTelegramUpdateSource(testChannel)

// Use in tests
val app = TelegramBotApplication(
    botToken = "TEST_TOKEN",
    updateSource = mockSource,
    eventDispatcher = dispatcher
)

app.start()

// Feed test updates
testChannel.trySend(testUpdate1)
testChannel.trySend(testUpdate2)

// Stop and restart (mock source supports infinite cycles)
app.stop()
app.start()

// Resume from where it left off
testChannel.trySend(testUpdate3)
```

**Key Features:**

- Supports infinite start/stop cycles (unlike network-based sources)
- `stop()` suspends consumption but keeps the channel open
- Resumes from where it left off on restart
- Naturally completes when the source channel is closed

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
    val event = context.event
    when (event) {
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
    commandEndpoint("start") {
        sendMessage("Welcome!")
    }

    // Event type matching
    onMessageEvent {
        whenMessageEventText("hello") { /* exact match */ }
        whenMessageEventTextRegex(Regex("(?i)^hello")) { /* regex */ }
        whenMessageEventPhoto { /* photo message */ }
    }

    onCallbackQueryEvent {
        whenCallbackQueryEventData("confirm") { /* callback data */ }
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
val app = TelegramBotApplication.longPolling(
    botToken = "YOUR_TOKEN",
    processingMode = ProcessingMode.CONCURRENT_BATCH,  // Default is CONCURRENT
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
commandEndpoint("process") {
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
```

**Key Points**:

- Child coroutines are cancelled when handler is cancelled
- Exceptions propagate to parent (unless using `SupervisorJob`)
- Guarantees completion before next stage

#### applicationScope (Fire-and-Forget)

Use `applicationScope` for tasks that should **outlive the current handler**:

```kotlin
commandEndpoint("background") {
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
val app = TelegramBotApplication.longPolling(
    botToken = "YOUR_TOKEN",
    processingMode = ProcessingMode.CONCURRENT,
    maxPendingUpdates = 10,  // Max 10 concurrent handlers
    eventDispatcher = dispatcher
)
```

## Handler DSL

The `handling` DSL provides type-safe routing for Telegram events.

### Event Type Routing

```kotlin
handling {
    // Message events
    onMessageEvent {
        handle { println("Message received") }
    }

    // Callback query events
    onCallbackQueryEvent {
        handle { println("Callback received") }
    }

    // Inline query events
    onInlineQueryEvent {
        handle { println("Inline query received") }
    }

    // Poll events
    onPollEvent {
        handle { println("Poll update") }
    }
}
```

### Text Matching

```kotlin
onMessageEvent {
    // Exact text match
    whenMessageEventText("hello") { /* ... */ }

    // Regex match
    whenMessageEventTextRegex(Regex("(?i)^hello")) { /* ... */ }

    // Contains substring
    whenMessageEventTextContains("help", ignoreCase = true) { /* ... */ }

    // Starts with prefix
    whenMessageEventTextStartsWith("/admin") { /* ... */ }

    // Ends with suffix
    whenMessageEventTextEndsWith("please") { /* ... */ }
}
```

### Media Type Matching

```kotlin
onMessageEvent {
    whenMessageEventPhoto { /* photo message */ }
    whenMessageEventVideo { /* video message */ }
    whenMessageEventDocument { /* document message */ }
    whenMessageEventAudio { /* audio message */ }
    whenMessageEventSticker { /* sticker message */ }
    whenMessageEventVoice { /* voice message */ }
    whenMessageEventAnimation { /* animation/GIF */ }
}
```

### Chat Type Filters

```kotlin
onMessageEvent {
    whenMessageEventPrivateChat { /* private chat only */ }
    whenMessageEventGroupChat { /* group chat only */ }
    whenMessageEventSupergroupChat { /* supergroup only */ }
    whenMessageEventChannel { /* channel only */ }
}
```

### User/Chat Filters

```kotlin
onMessageEvent {
    // From specific user
    whenMessageEventFromUser(123456L) { /* ... */ }

    // From multiple users
    whenMessageEventFromUsers(setOf(1L, 2L, 3L)) { /* ... */ }

    // In specific chat
    whenMessageEventInChat(-100123456L) { /* ... */ }

    // In multiple chats
    whenMessageEventInChats(setOf(100L, 200L)) { /* ... */ }
}
```

### Reply Message Detection

```kotlin
onMessageEvent {
    // Any reply
    whenMessageEventReply { /* ... */ }

    // Reply to specific message
    whenMessageEventReplyTo(messageId = 42L) { /* ... */ }
}
```

### Forwarded Message Detection

```kotlin
onMessageEvent {
    // Any forwarded message
    whenMessageEventForwarded { /* ... */ }

    // Forwarded from specific user
    whenMessageEventForwardedFromUser(123L) { /* ... */ }

    // Forwarded from specific chat
    whenMessageEventForwardedFromChat(-100L) { /* ... */ }
}
```

### Composite Matchers

```kotlin
onMessageEvent {
    // All conditions must be true
    whenAllOf(
        { it.event.message.text != null },
        { it.event.message.chat.id == 100L }
    ) {
        println("Text message in chat 100")
    }

    // Any condition must be true
    whenAnyOf(
        { it.event.message.photo != null },
        { it.event.message.video != null }
    ) {
        println("Photo or video")
    }

    // Condition must be false
    whenNot({ it.event.message.text?.startsWith("/") == true }) {
        println("Not a command")
    }

    // Nested composition
    allOf({ it.event.message.text != null }) {
        anyOf(
            { it.event.message.chat.id == 100L },
            { it.event.message.from?.id == 1L }
        ) {
            whenNot({ it.event.message.text?.startsWith("/") == true }) {
                println("Complex condition matched")
            }
        }
    }
}
```

### Custom Predicates

```kotlin
onMessageEvent {
    whenMatch({ (it.event.message.text?.length ?: 0) > 10 }) {
        sendMessage("Long message!")
    }
}
```

### Fallback Handler

`handle` is invoked when no child route consumes the event, acting as a "dead letter" handler:

```kotlin
handling {
    commandEndpoint("start") { /* ... */ }
    commandEndpoint("help") { /* ... */ }

    // Catch all unhandled events
    handle {
        sendMessage("Unknown command. Type /help for available commands.")
    }
}
```

**Note**: Child routes always take precedence over `handle`, regardless of declaration order.

### Route Modularization

Use `include` to merge multiple route modules:

```kotlin
val adminRoutes = handling {
    commandEndpoint("admin") { /* ... */ }
    commandEndpoint("admin status") { /* ... */ }
}

val userRoutes = handling {
    commandEndpoint("profile") { /* ... */ }
}

val mainRoutes = handling {
    commandEndpoint("start") { /* ... */ }
    include(adminRoutes)
    include(userRoutes)
}
```

## Command DSL

The Command DSL provides structured command parsing and handling.

### Simple Commands

```kotlin
handling {
    // Command without arguments
    commandEndpoint("ping") {
        replyMessage("pong")
    }

    // Supports /command@bot_username format
    commandEndpoint("start") {
        sendMessage("Welcome!")
    }
}
```

### Commands with Arguments

Define argument classes extending `BotArguments`:

```kotlin
class BanArgs : BotArguments("Ban a user") {
    val username: String by requireArgument("Username to ban")
    val duration: String? by optionalArgument("Ban duration")
    val reason: String by optionalArgument("Ban reason", default = "Violation")
}

handling {
    command("ban", ::BanArgs) {
        val username = arguments.username
        val duration = arguments.duration
        val reason = arguments.reason
        sendMessage("Banned $username")
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

        subCommandEndpoint("status") {
            showStatus()
        }

        subCommand("user") {
            subCommandEndpoint("list") { listUsers() }
            subCommandEndpoint("add") { addUser() }
            subCommand("ban", ::BanArgs) {
                banUser(arguments.username)
            }
        }
    }
}

// /admin           → showAdminHelp()
// /admin status    → showStatus()
// /admin user list → listUsers()
// /admin user ban john → banUser("john")
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
  // If arguments fail to parse, help message is sent automatically
  val username = arguments.username
  val duration = arguments.duration
  sendMessage("Banned $username")
}
```

To customize error handling, provide an `onError` callback:

```kotlin
command("ban", ::BanArgs, onError = { e ->
  sendMessage("Error: ${e.message}")
}) {
  // Custom error handling
}
```

To catch exceptions at the interceptor level, rethrow from `onError`:

```kotlin
command("ban", ::BanArgs, onError = { e -> throw e }) {
  // ...
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
    interceptors = listOf(loggingInterceptor),
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
val UserAuthenticatedKey = AttributeKey<Boolean>("user_authenticated")
val UserDataKey = AttributeKey<UserData>("user_data")

// Set attributes in interceptor
val authInterceptor: TelegramEventInterceptor = { context ->
    val userId = when (val event = context.event) {
        is MessageEvent -> event.message.from?.id
        is CallbackQueryEvent -> event.callbackQuery.from.id
        else -> null
    }

    if (userId != null) {
        val userData = fetchUserData(userId)
        context.attributes.put(UserDataKey, userData)
        context.attributes.put(UserAuthenticatedKey, true)
    }

    this.process(context)
}

// Read attributes in handler
handling {
    onMessageEvent {
        handle {
            val isAuthenticated = attributes.getOrNull(UserAuthenticatedKey) ?: false
            val userData = attributes.getOrNull(UserDataKey)

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
    commandEndpoint("survey") {
        startConversation(
            timeout = 5.minutes,
            onTimeout = {
                sendMessage("Survey timed out")
            }
        ) {
            sendMessage("What is your name?")
            val name = awaitText()

            sendMessage("How old are you?")
            val age = awaitText()

            sendMessage("Thanks $name, you are $age years old")
        }
    }
}
```

### Middleware

Use `middleware` in Handler DSL for conditional control:

```kotlin
val adminUserIds = setOf(1L, 2L, 3L)

handling {
    onMessageEvent {
        middleware(
            predicate = { ctx -> ctx.event.message.from?.id in adminUserIds },
            onRejected = {
                sendMessage("Admin only")
            }
        ) {
            commandEndpoint("admin") { /* admin commands */ }
            commandEndpoint("config") { /* config commands */ }
        }
    }
}
```

#### Custom Extensions

```kotlin
// Define extension function
fun <T : TelegramBotEvent> HandlerRoute<T>.requireAuth(
    allowedUsers: Set<Long>,
    build: HandlerRoute<T>.() -> Unit
) = middleware(
    predicate = { ctx ->
        when (val event = ctx.event) {
            is MessageEvent -> event.message.from?.id in allowedUsers
            else -> false
        }
    },
    onRejected = {
        if (event is MessageEvent) {
            sendMessage("Unauthorized")
        }
    }
) {
    build()
}

// Usage
handling {
    onMessageEvent {
        requireAuth(adminUserIds) {
            commandEndpoint("admin") { /* ... */ }
        }
    }
}
```

#### whenMiddleware

Simplified middleware combining predicate and handler:

```kotlin
onMessageEvent {
    whenMiddleware(
        predicate = { it.event.message.from?.id == 123L },
        onRejected = { sendMessage("Rejected") }
    ) {
        sendMessage("Allowed")
    }
}
```
