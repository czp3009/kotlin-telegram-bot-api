# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

A Kotlin Multiplatform library for the Telegram Bot API. It provides type-safe, auto-generated API bindings from the
Telegram Bot API OpenAPI specification using Ktorfit.

## Build Commands

```bash
# Build the entire project without executing tests
./gradlew assemble

# Full verification, including test tasks. Some tests may call real Telegram APIs.
./gradlew build

# Build a specific module without executing tests
./gradlew :protocol:assemble
./gradlew :client:assemble
./gradlew :application:assemble

# Run tests only when explicitly intended
./gradlew :protocol:jvmTest
./gradlew :client:jvmTest
./gradlew :application:jvmTest

# Run a specific test class
./gradlew :protocol:jvmTest --tests "com.hiczp.telegram.bot.protocol.test.TelegramBotApiTest"

# Run a sample bot (JVM)
./gradlew :sample:jvmRun --args="YOUR_BOT_TOKEN" -DmainClass="com.hiczp.telegram.bot.sample.basic.echo.EchoBotKt" --quiet
```

## Code Generation

The protocol module's API interfaces and models are auto-generated from the Telegram Bot API OpenAPI specification. Do
not edit generated files manually.

```bash
# Download latest OpenAPI spec and regenerate all protocol code
./gradlew downloadSwagger
./gradlew generateKtorfitInterfaces
```

The currently generated protocol sources target Telegram Bot API 10.1.

Generated files are marked with:

```kotlin
// Auto-generated from Swagger specification, do not modify this file manually
```

The `type/` directory contains handwritten code and is preserved during regeneration:

- `TelegramResponse.kt` - Response wrapper with success/error handling
- `InputFile.kt` - File upload sealed type

## Architecture

### Request Flow

1. **Update Fetching**: `UpdateSource` (e.g., long polling) fetches updates from Telegram via `TelegramBotClient`
2. **Event Conversion**: Raw `Update` objects are converted to typed `TelegramBotEvent` subclasses
3. **Interceptor Pipeline**: Events pass through the interceptor chain (onion model)
4. **Event Dispatching**: `EventDispatcher` routes events to matching handlers
5. **Response**: Handlers use `TelegramBotClient` to send responses back to Telegram

### Module Structure

```
kotlin-telegram-bot-api/
+-- protocol-annotation/   # Annotations for KSP code generation (IncomingUpdate, IncomingUpdateContainer)
+-- protocol/              # Core protocol definitions (Kotlin Multiplatform)
|   +-- model/             # Auto-generated data classes (Message, User, Chat, etc.) and JSON body extensions
|   +-- form/              # Auto-generated multipart form wrappers and extension functions
|   +-- query/             # Auto-generated query extension functions for JSON-serialized GET params
|   +-- type/              # Handwritten types (TelegramResponse, InputFile, etc.)
|   +-- union/             # Union type handling (Union sealed class, UnionSerializer)
|   +-- plugin/            # Handwritten Ktor client plugins (long polling, error handling, file download)
|   +-- exception/         # Handwritten exception types
|   +-- extension/         # Handwritten extension functions
|   +-- TelegramBotApi.kt  # Auto-generated Ktorfit interface
+-- protocol-update-codegen/ # KSP processor that generates TelegramBotEvent sealed interface from Update model
+-- client/                # High-level client wrapper
|   +-- TelegramBotClient.kt
+-- application/           # Bot application framework with lifecycle management
|   +-- TelegramBotApplication.kt      # Main orchestrator
|   +-- updatesource/                  # Update sources (long polling, mock, simple)
|   +-- interceptor/                   # Interceptor infrastructure and built-in interceptors
|   |   +-- builtin/conversation/      # Conversation FSM support
|   +-- dispatcher/                    # Event dispatching
|   |   +-- handler/                   # Handler DSL (handling {}, command(), etc.)
|   +-- context/                       # Event context and helper extensions
|   +-- command/                       # Command parsing utilities
+-- application-updatesource-webhook/  # Webhook update source module
|   +-- WebhookTelegramUpdateSource.kt # Webhook-based update source using embedded Ktor server
+-- buildSrc/              # Custom Gradle build scripts (configureAllTargets, etc.)
+-- sample/                # Example bot implementations
    +-- basic/             # EchoBot and CommandBot samples
    +-- advanced/          # FileBot and ConversationBot samples
```

### Key Dependencies

- **Ktorfit**: HTTP client interface generation (like Retrofit for Ktor)
- **kotlinx.serialization**: JSON serialization
- **Ktor**: HTTP client engine
- **KSP**: Kotlin Symbol Processing for code generation
- **KotlinPoet**: Code generation in KSP processors

### Compiler Options

The project uses Kotlin 2.4.0. Context parameters are stable, so `-Xcontext-parameters` is no longer needed. Do not add
deprecated or experimental compiler flags unless the code requires a specific language feature.

### Protocol Module (`:protocol`)

The core module containing:

- `TelegramBotApi` interface with Ktorfit annotations (`@GET`, `@POST`, `@Body`, `@Query`)
- Model classes with `@Serializable` and `@SerialName` for snake_case mapping
- Sealed interfaces for union types (`ReplyMarkup`, `BackgroundFill`, etc.)
- Multipart form wrappers for file uploads
- Query extensions for JSON-serialized GET parameters
- JSON body extensions for parameter-scattered API calls (in `model/Bodies.kt`)

#### Generated Model Rules

- Generate all Telegram API schemas as models whenever possible, including virtual or field-overlapping union parents
  such as `MaybeInaccessibleMessage`.
- Reference types are a separate decision from model generation. A field may use a richer representative type when the
  schema shows that type can represent every union branch without data loss.
- Keep code generation logic generic by inspecting schema structure, discriminators, required fields, constant fields,
  and JSON shape. Avoid matching specific Telegram type names unless there is no defensible generic rule.
- Generated `protocol` sources must remain Kotlin Multiplatform-compatible. `buildSrc` is a JVM-only development-time
  code generator and may use JVM APIs, but generated common code must not.

#### Generated Events (`protocol-update-codegen`)

The KSP processor generates `TelegramBotEvent` sealed interface from the `Update` model. Each optional non-primitive
field in `Update` becomes an event subclass (e.g., `MessageEvent`, `CallbackQueryEvent`, `InlineQueryEvent`).

Generated location: `protocol/build/generated/ksp/metadata/commonMain/kotlin/com/hiczp/telegram/bot/protocol/event/`

Events use `@JsonClassDiscriminator("type")` for polymorphic deserialization via `Update.toTelegramBotEvent()`.

### Extension Functions

The protocol module generates several types of extension functions for convenient API usage:

#### Forms Extensions (`form/Forms.kt`)

For multipart form data requests (file uploads), extension functions accept scattered parameters
including `InputFile` types, convert them to multipart `FormPart`, and call the API methods.

#### Query Extensions (`query/Queries.kt`)

For GET operations whose query parameters require JSON serialization, extension functions
keep call sites strongly typed while delegating to generated methods.

#### JSON Body Extensions (`model/Bodies.kt`)

For POST operations with JSON request bodies, extension functions accept individual parameters
matching the Request class fields instead of requiring a pre-constructed Request object:

```kotlin
// Instead of constructing Request objects:
api.sendMessage(SendMessageRequest(chatId = "123", text = "Hello"))

// Use scattered parameters directly:
api.sendMessage(chatId = "123", text = "Hello")
```

This provides a more ergonomic API with named parameters and default values.

### Client Module (`:client`)

High-level wrapper that:

- Provides `TelegramBotClient` with sensible defaults
- Configures JSON content negotiation
- Adds retry logic for transient failures and rate limiting
- Supports Telegram test environment
- Installs custom plugins (long polling, error handling, file download)

### Application Module (`:application`)

Bot application framework providing:

- **Lifecycle management**: Start/stop with graceful shutdown
- **Update processing**: Consumes updates from `TelegramUpdateSource` (e.g., long polling)
- **Interceptor pipeline**: "Onion model" interceptor pattern for cross-cutting concerns
- **Event dispatching**: Routes events to business logic handlers

Key components:

- `TelegramBotApplication` - Main orchestrator handling lifecycle and update processing
- `TelegramUpdateSource` - Interface for update sources (long polling, webhook, mock)
- `TelegramEventInterceptor` - Middleware function type for request interception
- `TelegramEventDispatcher` - Routes events to handlers
- `TelegramBotEventContext` - Request-scoped context with client and attributes

### Response Handling

All API calls return `TelegramResponse<T>`:

```kotlin
val response = api.getMe()
response.onSuccess { user -> println(user) }
response.onError { error -> println(error) }

// Or throw on error
val user = api.getMe().getOrThrow()
```

### Union Types

Some Telegram API methods can return different response types depending on the context. The `Union<A, B>` sealed class
handles these cases:

```kotlin
// editMessageText returns Message when editing a regular message, or Boolean when editing an inline message
val response: TelegramResponse<Union<Message, Boolean>> = api.editMessageText(
  chatId = "123456789",
  messageId = 42,
  text = "Updated text"
)

response.onSuccess { union ->
  // Check which type was returned
  val message = union.firstOrNull()  // Returns Message if present (regular message was edited)
  val boolean = union.secondOrNull() // Returns Boolean if present (inline message was edited)

  when {
    message != null -> println("Edited message: ${message.messageId}")
    boolean != null -> println("Edit result: $boolean")
  }
}
```

**API methods that return Union types:**

- `editMessageText` - Returns `Union<Message, Boolean>`
- `editMessageCaption` - Returns `Union<Message, Boolean>`
- `editMessageMedia` - Returns `Union<Message, Boolean>`
- `editMessageLiveLocation` - Returns `Union<Message, Boolean>`
- `stopMessageLiveLocation` - Returns `Union<Message, Boolean>`
- `editMessageReplyMarkup` - Returns `Union<Message, Boolean>`
- `setGameScore` - Returns `Union<Message, Boolean>`

**Note:** The `Union` class uses `@Serializable(with = UnionSerializer::class)`, which means the serializer is
automatically resolved at compile time when type parameters are known. In most cases, you do **not** need to configure
`unionSerializersModule` in your `Json` instance. The `unionSerializersModule` is only needed in rare cases where
contextual serialization is required (e.g., when using `@Contextual` annotations or runtime reflection-based
serialization lookup).

### File Uploads

Use `InputFile` sealed type for multipart uploads:

```kotlin
// Reference existing file by file_id or URL
api.sendPhoto(chatId, photo = InputFile.reference("file_id"))

// Upload binary content
api.sendDocument(
    chatId = chatId,
    document = InputFile.binary(fileName = "file.pdf") { ByteReadChannel(bytes) }
)
```

### Interceptor Pattern (Application Module)

The application module uses an "onion model" interceptor pattern. Interceptors wrap around the event processing
pipeline, allowing pre/post processing:

```kotlin
val loggingInterceptor: TelegramEventInterceptor = { context ->
    println("Before: ${context.event.updateId}")
    this.process(context)  // Pass to next layer
    println("After: ${context.event.updateId}")
}
```

The `TelegramBotEventContext` provides access to:

- `client` - TelegramBotApi for API calls
- `event` - The TelegramBotEvent being processed
- `applicationScope` - Coroutine scope for launching concurrent tasks
- `attributes` - Type-safe storage for sharing data between interceptors

#### Built-in Interceptors

The application module provides built-in interceptors in `interceptor/builtin/`:

- **LoggingInterceptor** (`logging/LoggingInterceptor.kt`) - Logs all received events with configurable log level and
  formatter. Useful for debugging and monitoring.

```kotlin
import com.hiczp.telegram.bot.application.interceptor.builtin.logging.loggingInterceptor

val app = TelegramBotApplication.longPolling(
    botToken = "YOUR_TOKEN",
    interceptors = listOf(loggingInterceptor()),
    eventDispatcher = eventDispatcher
)
```

- **ConversationInterceptor** (`conversation/`) - Enables multi-turn conversation support. See Conversations section.

### Handler DSL (Application Module)

The `HandlerTelegramEventDispatcher` routes events through a tree of async filters and explicit handlers. Core routing
functions are in `application/src/commonMain/kotlin/com/hiczp/telegram/bot/application/dispatcher/handler/`, and
specialized filter wrappers are in `handler/matcher/`:

- **Handling.kt** - `handling` and `include`
- **HandlerRoute.kt** - `filter { ... }`, `filter<T> { ... }`, and `handle { ... }`
- **CommandDsl.kt** - `command`, typed `command`, and `subCommand`
- **EventType.kt** - root event filters such as `message`, `callbackQuery`, `inlineQuery`
- **Message.kt** - message filters such as `text`, `photo`, `privateChat`, `fromUser`, `inChat`
- **Callback.kt** - callback query filters such as `data`, `dataStartsWith`, `fromUser`
- **Inline.kt** - inline query filters such as `query`, `queryStartsWith`, `fromUser`
- **OtherEvents.kt** - poll, payment, chat membership, and boost filters

Core rules:

- `filter({ ... }) { ... }` enters a child branch when the suspending predicate returns `true`.
- `filter<T> { ... }` narrows the event type for the child branch.
- Built-in matchers are filter wrappers and do not consume events by themselves.
- `handle { ... }` runs runtime business logic and consumes the event.
- Matching is depth-first with full backtracking. If a deep branch does not consume the event, matching returns through
  ancestors and tries later siblings.
- A parent `handle` acts as fallback for its branch after all child routes miss.
- Route blocks run once while building the dispatcher; per-update side effects belong in filter predicates or
  `handle` blocks.

```kotlin
val dispatcher = HandlerTelegramEventDispatcher(handling {
    command("start") {
        handle {
            replyMessage("Welcome!")
        }
    }

    command("admin") {
        subCommand("status") {
            handle {
                replyMessage("System status: OK")
            }
        }

        handle {
            replyMessage("Admin help")
        }
    }

    message {
        privateChat {
            text("hello") {
                handle {
                    replyMessage("Hi!")
                }
            }

            photo {
                handle {
                    replyMessage("Photo received")
                }
            }

            filter({ event.message.from?.id in adminUserIds }) {
                command("reload") {
                    handle {
                        replyMessage("Reloading")
                    }
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

    include(adminRoutes)

    handle {
        println("Unhandled event: ${event.updateId}")
    }
})
```

Typed command arguments use explicit `handle`:

```kotlin
class BanArgs : BotArguments("Ban a user") {
    val username: String by requireArgument("User to ban")
    val duration: String? by optionalArgument("Ban duration")
}

command("ban", ::BanArgs) {
    handle {
        val username = arguments.username
        val duration = arguments.duration
        replyMessage("Banned $username for ${duration ?: "unspecified duration"}")
    }
}
```

#### Structured Concurrency in Handlers

Handlers receive a `CoroutineScope` receiver, enabling structured concurrency. Any coroutines launched
inside a handler will be awaited before the dispatch completes:

```kotlin
command("process") {
  handle {
        // Launch concurrent operations
        launch {
            val result = slowOperation()
          sendMessage("Result: $result")
        }
        launch {
            anotherAsyncTask()
        }
        // dispatch waits for all launches to complete
    }
}
```

For fire-and-forget tasks that should outlive the current handler, use `applicationScope`:

```kotlin
command("background") {
  handle {
    // In application scope - continues after handler returns
    applicationScope.launch {
      delay(10.seconds)
      client.sendMessage(event.message.chat.id.toString(), "Delayed message")
    }
    // Handler returns immediately
  }
}
```

#### Dead Letter Handler

A root-level `handle` acts as a fallback for all unhandled events, replacing the need for a separate
dead letter mechanism:

```kotlin
handling {
  command("start") {
    handle { /* ... */ }
  }
  command("help") {
    handle { /* ... */ }
  }

    // Catches all unhandled events (unknown commands, other event types, etc.)
  handle {
    client.sendMessage(
      event.extractChatId()!!,
            "Unknown command. Type /help for available commands."
        )
    }
}
```

### Conversations (Application Module)

Multi-turn conversations are supported via the `conversationInterceptor` and `startConversation` DSL:

```kotlin
// Install the conversation interceptor
val interceptors = listOf(conversationInterceptor())

// In a handler, start a conversation
command("survey") {
  handle {
    startConversation(
      timeout = 5.minutes,
      onTimeout = {
        val chatId = event.extractChatId()
        if (chatId != null) {
          client.sendMessage(chatId.toString(), "Survey timed out.")
        }
      },
      onCancel = {
        val chatId = event.extractChatId()
        if (chatId != null) {
          client.sendMessage(chatId.toString(), "Survey cancelled.")
        }
      }
    ) {
      // Use send() for simple messages
      send("What is your name?")
      val name = awaitText()

      // Use reply() to respond to the most recent user message
      reply("Hello, $name! How old are you?")
      val age = awaitText()

      // reply() automatically responds to the last awaited message
      reply("Thanks! You are $age years old.")
    }
  }
}
```

**Messaging Methods:**

- `send(text)` - Sends a message to the conversation's chat without replying to any specific message. Returns
  `TelegramResponse<Message>` and updates `lastSentMessageId` on success.
- `reply(text, replyToMessageId?)` - Sends a message as a reply. If `replyToMessageId` is null, uses
  `lastAwaitedMessageId` to automatically reply to the most recent user input. If no message ID is available,
  behaves like `send()`. Returns `TelegramResponse<Message>` and updates `lastSentMessageId` on success.

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

**Intercepted Event Types:**

By default, conversations intercept `MessageEvent`, `BusinessMessageEvent`, and `CallbackQueryEvent`. You can customize
this with the `interceptPredicate` parameter:

```kotlin
startConversation(
  interceptPredicate = { context ->
    context.event is MessageEvent || context.event is CallbackQueryEvent
  }
) {
  // ...
}
```

The `ConversationId` is a data class with `chatId`, `threadId` (optional), and `userId` (optional):

- `ConversationId(chatId)` - whole chat shares one conversation
- `ConversationId(chatId, userId = userId)` - per-user conversation in a group
- `ConversationId(chatId, threadId = threadId)` - per-thread conversation

### Simple Update Source

A stateless update source that allows external injection of updates via `push()`. Designed for distributed systems
where updates are received by one component and processed by another:

```kotlin
// Create the source
val source = SimpleTelegramUpdateSource()

// Use with TelegramBotApplication
val app = TelegramBotApplication(
  client = client,
  updateSource = source,
  interceptors = emptyList(),
  eventDispatcher = dispatcher
)
app.start()

// Push updates from external source (e.g., message queue consumer)
messageQueue.consume { update ->
  // This processes the update synchronously
  source.push(update)
}

// Graceful shutdown
app.stop()
```

**Architecture Pattern:**

```
+-----------------+     +-----------------+     +------------------------------+
|  Webhook Server |---->|  Message Queue  |---->|  Worker Node                 |
|  (Bot Instance) |     |  (Kafka/Rabbit) |     |  SimpleTelegramUpdateSource  |
+-----------------+     +-----------------+     +------------------------------+
```

**Key Features:**

- Stateless design - supports multiple start/stop cycles
- `push(update)` processes updates synchronously via the consumer callback
- Thread-safe - safe for concurrent use from multiple threads
- Exceptions from the consumer propagate to the caller
- The source can be started again after stopping, but a `TelegramBotApplication` instance cannot be restarted

**Exception Semantics:**

| Exception                          | Behavior                                         |
|------------------------------------|--------------------------------------------------|
| `IllegalStateException`            | Source is not currently started                  |
| `TelegramBotShuttingDownException` | Propagates to caller, signals framework shutdown |
| `CancellationException`            | Propagates if coroutine is being cancelled       |
| Other `Throwable`                  | Logged and re-thrown to the caller               |

**Graceful Shutdown:**

1. `stop(gracePeriod)` clears the consumer callback (gracePeriod is ignored)
2. The source can be started again by another owner after shutdown
3. `onFinalize()` is a no-op since all state is in-memory

### Webhook Update Source (`:application-updatesource-webhook`)

A separate module providing webhook-based update receiving. Uses an embedded Ktor server:

```kotlin
implementation("com.hiczp:telegram-bot-api-application-updatesource-webhook:$version")
```

#### Quick Start with Webhook

Use the `webhook` factory method for simple webhook-based bots:

```kotlin
val app = TelegramBotApplication.webhook(
  botToken = "YOUR_TOKEN",
  applicationEngineFactory = Netty,
  path = "/webhook",
  configureEngine = {
    connector {
      host = "0.0.0.0"
      port = 8443
    }
  },
  eventDispatcher = dispatcher,
  interceptors = listOf(loggingInterceptor())
)
app.start()
app.join()  // Suspend until stopped
// app.stop(5.seconds) for graceful shutdown
```

#### Manual Webhook Setup

For more control, manually create `WebhookTelegramUpdateSource`:

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

val app = TelegramBotApplication(
  client = client,
  updateSource = updateSource,
  interceptors = emptyList(),
  eventDispatcher = dispatcher
)
```

#### SSL Configuration

Telegram requires HTTPS. Use a reverse proxy (nginx) for SSL termination, or configure SSL directly in Ktor.
See `application-updatesource-webhook/README.md` for detailed SSL configuration.

### Quick Start (Application Module)

Use the factory method for simple long-polling bots:

```kotlin
import com.hiczp.telegram.bot.application.interceptor.builtin.logging.loggingInterceptor

val app = TelegramBotApplication.longPolling(
    botToken = "YOUR_TOKEN",
    eventDispatcher = dispatcher,
    interceptors = listOf(loggingInterceptor())
)
app.start()
app.join()  // Suspend until stopped
// app.stop(5.seconds) for graceful shutdown
```

### Event Dispatchers

Two dispatcher implementations are available:

- **`HandlerTelegramEventDispatcher`** - Type-safe routing DSL with matchers (recommended for complex bots)
- **`SimpleTelegramEventDispatcher`** - Single lambda handler for simple use cases:

```kotlin
val eventDispatcher = SimpleTelegramEventDispatcher { context ->
  val event = context.event
  if (event is MessageEvent) {
    context.client.sendMessage(
      chatId = event.message.chat.id.toString(),
      text = event.message.text ?: "No text"
    )
  }
}
```

### Sample Module

Example bot implementations in `:sample`:

```
sample/src/commonMain/kotlin/com/hiczp/telegram/bot/sample/
+-- basic/
|   +-- command/CommandBot.kt      # Typed arguments, subcommands
|   +-- echo/EchoBot.kt            # Minimal long-polling echo bot
|   +-- interceptor/               # Custom interceptors (Blacklist, Timeout, etc.)
+-- advanced/
|   +-- conversation/ConversationBot.kt  # Multi-turn conversations, surveys
|   +-- file/FileBot.kt                  # File upload/download, stickers
+-- dsl/
|   +-- AuthDsl.kt                 # Reusable `requireAuth` handler filter DSL
+-- expert/                        # Placeholder for complex examples
```

**Basic Samples:**

- **EchoBot.kt** - Minimal echo bot with `SimpleTelegramEventDispatcher`
- **CommandBot.kt** - Typed arguments (`BotArguments`), subcommands, `requireAuth` DSL
- **InterceptorBot.kt** - Custom interceptors: localization, timeout, exception catching, blacklist

**Advanced Samples:**

- **FileBot.kt** - File upload/download with progress, sticker echo
- **ConversationBot.kt** - Multi-turn conversations using `startConversation`, `send`, `reply`, `awaitText`,
  `awaitCallbackQuery`

### Supported Platforms

All modules target JVM, Android, JS, WASM, Linux, Windows, Android Native, macOS ARM64, iOS ARM/simulator ARM64,
watchOS ARM/simulator ARM64, and tvOS ARM/simulator ARM64. Apple x64 targets `macosX64`, `iosX64`, `watchosX64`,
and `tvosX64` are not configured.

**Test Source Sets:**

- `jvmTest` - JVM tests
- `desktopNativeTest` - Linux, macOS ARM64, Windows native tests
- `otherTest` / `commonTest` - Other platform tests (not all platforms support running tests)

Some tests can make real Telegram API calls. Use `./gradlew assemble` for build-only verification, and run tests only
when explicitly intended.

## Concurrency Model

### Update Processing Modes

The `LongPollingTelegramUpdateSource` supports three processing modes:

| Mode               | Concurrency             | Backpressure                   | Delivery Guarantee          |
|--------------------|-------------------------|--------------------------------|-----------------------------|
| `SEQUENTIAL`       | One at a time           | Natural flow control           | At-Least-Once               |
| `CONCURRENT_BATCH` | Concurrent within batch | Batch-level                    | At-Least-Once (Recommended) |
| `CONCURRENT`       | Fully concurrent        | None (use `maxPendingUpdates`) | At-Most-Once                |

**Default mode is `CONCURRENT`**. Global mutable state is NOT safe without synchronization.

### Thread Safety

In concurrent mode, use thread-safe collections or synchronization:

```kotlin
// DANGEROUS: Race condition in concurrent mode
val processedTexts = mutableListOf<String>()

// SAFE: Use thread-safe collections
val processedTexts = ConcurrentLinkedQueue<String>()

// Or use SEQUENTIAL mode for strict ordering
```
