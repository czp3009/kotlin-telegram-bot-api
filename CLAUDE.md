# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

A Kotlin Multiplatform library for the Telegram Bot API. It provides type-safe, auto-generated API bindings from the
Telegram Bot API OpenAPI specification using Ktorfit.

## Build Commands

```bash
# Build the entire project
./gradlew build

# Build a specific module
./gradlew :protocol:build
./gradlew :client:build
./gradlew :application:build

# Run tests (JVM only - tests only run on JVM and desktop native targets)
./gradlew :protocol:jvmTest
./gradlew :client:jvmTest
./gradlew :application:jvmTest

# Run a specific test class
./gradlew :protocol:jvmTest --tests "com.hiczp.telegram.bot.protocol.test.TelegramBotApiTest"
```

## Code Generation

The protocol module's API interfaces and models are auto-generated from the Telegram Bot API OpenAPI specification. Do
not edit generated files manually.

```bash
# Download latest OpenAPI spec and regenerate all protocol code
./gradlew downloadSwagger
./gradlew generateKtorfitInterfaces
```

Generated files are marked with:

```kotlin
// Auto-generated from Swagger specification, do not modify this file manually
```

The `type/` directory contains handwritten code and is preserved during regeneration:

- `TelegramResponse.kt` - Response wrapper with success/error handling
- `InputFile.kt` - File upload sealed type

## Architecture

### Module Structure

```
kotlin-telegram-bot-api/
├── protocol-annotation/   # Annotations for KSP code generation (IncomingUpdate, IncomingUpdateContainer)
├── protocol/              # Core protocol definitions (Kotlin Multiplatform)
│   ├── model/            # Auto-generated data classes (Message, User, Chat, etc.) and JSON body extensions
│   ├── form/             # Auto-generated multipart form wrappers and extension functions
│   ├── query/            # Auto-generated query extension functions for JSON-serialized GET params
│   ├── type/             # Handwritten types (TelegramResponse, InputFile, etc.)
│   ├── plugin/           # Handwritten Ktor client plugins (long polling, error handling, file download)
│   ├── exception/        # Handwritten exception types
│   ├── extension/        # Handwritten extension functions
│   └── TelegramBotApi.kt # Auto-generated Ktorfit interface
├── protocol-update-codegen/ # KSP processor that generates TelegramBotEvent sealed interface from Update model
├── client/                # High-level client wrapper
│   └── TelegramBotClient.kt
└── application/           # Bot application framework with lifecycle management
    ├── TelegramBotApplication.kt      # Main orchestrator
    ├── updatesource/                  # Update sources (long polling, mock)
    ├── interceptor/                   # Interceptor infrastructure and built-in interceptors
    │   └── builtin/conversation/      # Conversation FSM support
    ├── dispatcher/                    # Event dispatching
    │   └── handler/                   # Handler DSL (handling {}, command(), etc.)
    ├── context/                       # Event context and helper extensions
    └── command/                       # Command parsing utilities
```

### Key Dependencies

- **Ktorfit**: HTTP client interface generation (like Retrofit for Ktor)
- **kotlinx.serialization**: JSON serialization
- **Ktor**: HTTP client engine
- **KSP**: Kotlin Symbol Processing for code generation

### Protocol Module (`:protocol`)

The core module containing:

- `TelegramBotApi` interface with Ktorfit annotations (`@GET`, `@POST`, `@Body`, `@Query`)
- Model classes with `@Serializable` and `@SerialName` for snake_case mapping
- Sealed interfaces for union types (`ReplyMarkup`, `BackgroundFill`, etc.)
- Multipart form wrappers for file uploads
- Query extensions for JSON-serialized GET parameters
- JSON body extensions for parameter-scattered API calls (in `model/Queries.kt`)

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

#### JSON Body Extensions (`model/Queries.kt`)

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
- **Interceptor pipeline**: "Onion model" middleware pattern for cross-cutting concerns
- **Event dispatching**: Routes events to business logic handlers

Key components:

- `TelegramBotApplication` - Main orchestrator handling lifecycle and update processing
- `TelegramUpdateSource` - Interface for update sources (long polling, webhook, mock)
- `TelegramEventInterceptor` - Middleware function type for request interception
- `TelegramEventDispatcher` - Routes events to handlers
- `TelegramBotContext` - Request-scoped context with client and attributes

### Response Handling

All API calls return `TelegramResponse<T>`:

```kotlin
val response = api.getMe()
response.onSuccess { user -> println(user) }
response.onError { error -> println(error) }

// Or throw on error
val user = api.getMe().getOrThrow()
```

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

- `client` - TelegramBotClient for API calls
- `event` - The TelegramBotEvent being processed
- `applicationScope` - Coroutine scope for launching concurrent tasks
- `attributes` - Type-safe storage for sharing data between interceptors

### Handler DSL (Application Module)

The `HandlerTelegramEventDispatcher` provides a type-safe routing DSL for event handling. The DSL is organized into
specialized matcher files in `application/src/commonMain/kotlin/com/hiczp/telegram/bot/application/dispatcher/handler/`:

- **Handling.kt** - Core DSL (`handling`, `match`, `whenMatch`, `include`, `select`)
- **CompositeMatchers.kt** - Logic combinators (`allOf`, `anyOf`, `not`)
- **MessageEventMatchers.kt** - Message handlers (`command`, `text`, `textRegex`, media types, etc.)
- **CallbackQueryMatchers.kt** - Callback query handlers (`callbackData`, `callbackDataRegex`)
- **InlineQueryMatchers.kt** - Inline query handlers (`inlineQuery`, `inlineQueryRegex`)
- **ChatTypeMatchers.kt** - Chat type filters (`privateChat`, `groupChat`, `supergroupChat`, `channel`)
- **EditedMessageMatchers.kt** - Edited message handlers
- **ChatEventMatchers.kt** - Chat event handlers (joins, member updates)
- **ServiceMessageMatchers.kt** - Service message handlers
- **PollMatchers.kt** - Poll and reaction handlers
- **PaymentMatchers.kt** - Payment event handlers

Each matcher provides two variants: one for the specific event type scope (e.g., inside `on<MessageEvent>`) and one
for the root level that auto-wraps with `on<EventType>`.

```kotlin
val dispatcher = HandlerTelegramEventDispatcher(handling {
    // Command handlers (supports /command and /command@bot_username formats)
    // The parsed parameter provides access to command arguments
    command("start") { ctx, parsed ->
        ctx.client.sendMessage(ctx.event.message.chat.id, "Welcome!")
    }
    command("ban") { ctx, parsed ->
        // For "/ban @user 7d", parsed.args = ["@user", "7d"]
        val username = parsed.args.getOrNull(0)
        val duration = parsed.args.getOrNull(1)
    }

    // Event type matching
    on<MessageEvent> {
        text("hello") { ctx -> /* exact text match */ }
        textRegex(Regex("(?i)^hello")) { ctx -> /* regex match */ }
        textContains("help", ignoreCase = true) { ctx -> /* substring match */ }
        textStartsWith("/admin") { ctx -> /* prefix match */ }

        // Media type handlers
        photo { ctx -> /* photo message */ }
        video { ctx -> /* video message */ }
        document { ctx -> /* document message */ }
        sticker { ctx -> /* sticker message */ }

        // User/chat filters
        fromUser(123456L) { ctx -> /* from specific user */ }
        inChat(-100123456L) { ctx -> /* in specific chat */ }

        // Reply detection
        reply { ctx -> /* is a reply */ }
        replyTo(messageId = 42L) { ctx -> /* reply to specific message */ }

        // Forwarded messages
        forwarded { ctx -> /* forwarded message */ }
        forwardedFromChat(-100123L) { ctx -> /* forwarded from specific chat */ }

        // Conditional handler with whenMatch
        whenMatch({ (it.event.message.text?.length ?: 0) > 10 }) { ctx ->
            ctx.client.sendMessage(ctx.event.message.chat.id, "Long message!")
        }

        // Composite matchers - combine predicates
        allOf(
            { it.event.message.text != null },
            { it.event.message.chat.id == 100L }
        ) {
            handle { ctx -> println("Private text message in chat 100") }
        }

        anyOf(
            { it.event.message.photo != null },
            { it.event.message.video != null }
        ) {
            handle { ctx -> println("Photo or video") }
        }

        not({ it.event.message.text?.startsWith("/") == true }) {
            handle { ctx -> println("Not a command") }
        }
    }

    // Chat type filters
    privateChat { ctx -> /* private chat only */ }
    groupChat { ctx -> /* group chat only */ }
    supergroupChat { ctx -> /* supergroup only */ }
    channel { ctx -> /* channel only */ }

    on<CallbackQueryEvent> {
        callbackData("confirm") { ctx -> /* callback handling */ }
        callbackDataRegex(Regex("action_\\d+")) { ctx -> /* pattern match */ }
    }

    on<InlineQueryEvent> {
        inlineQuery("search") { ctx -> /* exact query match */ }
        inlineQueryStartsWith("find:") { ctx -> /* prefix match */ }
    }

    // Include routes from other modules
    include(adminRoutes)

    // Dead letter handler - catches all unhandled events
    handle { ctx ->
        println("Unhandled event: ${ctx.event.updateId}")
    }
})
```

#### Structured Concurrency in Handlers

Handlers receive a `CoroutineScope` receiver, enabling structured concurrency. Any coroutines launched
inside a handler will be awaited before the dispatch completes:

```kotlin
command("process") { ctx, _ ->
    // Launch concurrent operations
    launch {
        val result = slowOperation()
        ctx.client.sendMessage(ctx.event.message.chat.id, "Result: $result")
    }
    launch {
        anotherAsyncTask()
    }
    // dispatch waits for all launches to complete
}
```

#### Dead Letter Handler

A root-level `handle` acts as a fallback for all unhandled events, replacing the need for a separate
dead letter mechanism:

```kotlin
handling {
    command("start") { _, _ -> /* ... */ }
    command("help") { _, _ -> /* ... */ }

    // Catches all unhandled events (unknown commands, other event types, etc.)
    handle { ctx ->
        ctx.client.sendMessage(
            ctx.event.extractChatId()!!,
            "Unknown command. Type /help for available commands."
        )
    }
}
```

### Conversations (Application Module)

Multi-turn conversations are supported via the `conversationInterceptor` and `startConversation` DSL:

```kotlin
// Install the conversation interceptor
val interceptors = listOf(conversationInterceptor)

// In a handler, start a conversation
command("survey") { ctx, _ ->
    startConversation(
        timeout = 5.minutes,
        onTimeout = { reply("Survey timed out.") },
        onCancel = { reply("Survey cancelled.") }
    ) {
        val name = awaitTextMessage().event.message.text
        reply("Hello, $name!")
        val age = awaitTextMessage().event.message.text
        reply("Thanks! You are $age years old.")
    }
}
```

### Quick Start (Application Module)

Use the factory method for simple long-polling bots:

```kotlin
val app = TelegramBotApplication.longPolling(
    botToken = "YOUR_TOKEN",
    eventDispatcher = dispatcher,
    interceptors = listOf(loggingInterceptor)
)
app.start()
app.join()  // Suspend until stopped
// app.stop(5.seconds) for graceful shutdown
```

### Supported Platforms

All modules target: JVM, Android, JS, WASM, Linux, macOS, Windows, iOS, watchOS, tvOS, Android Native.

Tests only run on JVM and desktop native targets (Linux, macOS, Windows).
