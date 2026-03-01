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
specialized matcher files in
`application/src/commonMain/kotlin/com/hiczp/telegram/bot/application/dispatcher/handler/matcher/`:

- **Handling.kt** - Core DSL (`handling`, `match`, `whenMatch`, `include`, `select`)
- **Composite.kt** - Logic combinators (`allOf`, `anyOf`, `not`, `whenAllOf`, `whenAnyOf`, `whenNot`)
- **EventType.kt** - Event type matchers (`onMessageEvent`, `whenMessageEvent`, `onChannelPostEvent`,
  `whenChannelPostEvent`, `onEditedMessageEvent`, `whenEditedMessageEvent`, `onEditedChannelPostEvent`,
  `whenEditedChannelPostEvent`, etc.)
- **MessageEvent.kt** - Message handlers (`onText`, `whenText`, `onTextRegex`, `whenTextRegex`, `onTextContains`,
  `whenTextContains`, `onTextStartsWith`, `whenTextStartsWith`, `onTextEndsWith`, `whenTextEndsWith`, `onPhoto`,
  `whenPhoto`, `onVideo`, `whenVideo`, `onAudio`, `whenAudio`, `onDocument`, `whenDocument`, `onSticker`, `whenSticker`,
  `onVoice`, `whenVoice`, `onVideoNote`, `whenVideoNote`, `onAnimation`, `whenAnimation`, `onContact`, `whenContact`,
  `onLocation`, `whenLocation`, `onVenue`, `whenVenue`, `onPoll`, `whenPoll`, `onDice`, `whenDice`, `onReply`,
  `whenReply`,
  `onReplyTo`, `whenReplyTo`, `onFromUser`, `whenFromUser`, `onFromUsers`, `whenFromUsers`, `onInChat`, `whenInChat`,
  `onInChats`, `whenInChats`, `onForwarded`, `whenForwarded`, `onForwardedFromUser`, `whenForwardedFromUser`,
  `onForwardedFromChat`, `whenForwardedFromChat`, `onForwardedHiddenUser`, `whenForwardedHiddenUser`)
- **CallbackQuery.kt** - Callback query handlers (`onCallbackData`, `whenCallbackData`, `onCallbackDataRegex`,
  `whenCallbackDataRegex`, `onCallbackDataContains`, `whenCallbackDataContains`, `onCallbackDataStartsWith`,
  `whenCallbackDataStartsWith`, `onCallbackQueryFromUser`, `whenCallbackQueryFromUser`, `onCallbackQueryInChat`,
  `whenCallbackQueryInChat`)
- **InlineQuery.kt** - Inline query handlers (`onInlineQuery`, `whenInlineQuery`, `onInlineQueryRegex`,
  `whenInlineQueryRegex`, `onInlineQueryContains`, `whenInlineQueryContains`, `onInlineQueryStartsWith`,
  `whenInlineQueryStartsWith`, `onInlineQueryFromUser`, `whenInlineQueryFromUser`, `onChosenInlineResult`,
  `whenChosenInlineResult`, `onChosenInlineResultFromUser`, `whenChosenInlineResultFromUser`)
- **ChatType.kt** - Chat type filters (`onPrivateChat`, `whenPrivateChat`, `onGroupChat`, `whenGroupChat`,
  `onSupergroupChat`, `whenSupergroupChat`, `onChannel`, `whenChannel`)
- **EditedMessage.kt** - Edited message handlers (`onEditedText`, `whenEditedText`, `onEditedTextRegex`,
  `whenEditedText`, `onEditedTextRegex`, `whenEditedTextRegex`, `onEditedTextContains`, `whenEditedTextContains`,
  `onEditedTextStartsWith`, `whenEditedTextStartsWith`, `onEditedTextEndsWith`, `whenEditedTextEndsWith`,
  `onEditedPhoto`, `whenEditedPhoto`, `onEditedVideo`, `whenEditedVideo`, `onEditedDocument`, `whenEditedDocument`,
  `onEditedAudio`, `whenEditedAudio`, `onEditedFromUser`, `whenEditedFromUser`, `onEditedInChat`, `whenEditedInChat`,
  `onEditedChannelPost`, `whenEditedChannelPost`, `onEditedChannelPostText`, `whenEditedChannelPostText`,
  `onEditedChannelPostTextRegex`, `whenEditedChannelPostTextRegex`, `onEditedChannelPostFromChat`,
  `whenEditedChannelPostFromChat`)
- **ChatEvent.kt** - Chat event handlers (`onChatJoinRequest`, `whenChatJoinRequest`, `onChatJoinRequestFromChat`,
  `whenChatJoinRequestFromChat`, `onChatJoinRequestFromUser`, `whenChatJoinRequestFromUser`, `onChatMemberUpdated`,
  `whenChatMemberUpdated`, `onChatMemberUpdatedInChat`, `whenChatMemberUpdatedInChat`, `onMyChatMemberUpdated`,
  `whenMyChatMemberUpdated`, `onMyChatMemberUpdatedInChat`, `whenMyChatMemberUpdatedInChat`, `onChatBoost`,
  `whenChatBoost`, `onChatBoostInChat`, `whenChatBoostInChat`, `onRemovedChatBoost`, `whenRemovedChatBoost`,
  `onRemovedChatBoostInChat`, `whenRemovedChatBoostInChat`)
- **ServiceMessage.kt** - Service message handlers (`onNewChatMembers`, `whenNewChatMembers`, `onLeftChatMember`,
  `whenLeftChatMember`, `onNewChatTitle`, `whenNewChatTitle`, `onNewChatPhoto`, `whenNewChatPhoto`,
  `onDeleteChatPhoto`, `whenDeleteChatPhoto`, `onPinnedMessage`, `whenPinnedMessage`, `onGroupCreated`,
  `whenGroupCreated`, `onSupergroupCreated`, `whenSupergroupCreated`, `onChannelCreated`, `whenChannelCreated`,
  `onVideoChatStarted`, `whenVideoChatStarted`, `onVideoChatEnded`, `whenVideoChatEnded`, `onVideoChatScheduled`,
  `whenVideoChatScheduled`, `onVideoChatParticipantsInvited`, `whenVideoChatParticipantsInvited`, `onForumTopicCreated`,
  `whenForumTopicCreated`, `onForumTopicEdited`, `whenForumTopicEdited`, `onForumTopicClosed`, `whenForumTopicClosed`,
  `onForumTopicReopened`, `whenForumTopicReopened`, `onGeneralForumTopicHidden`, `whenGeneralForumTopicHidden`,
  `onGeneralForumTopicUnhidden`, `whenGeneralForumTopicUnhidden`, `onGiveawayCreated`, `whenGiveawayCreated`,
  `onGiveawayCompleted`, `whenGiveawayCompleted`, `onBoostAdded`, `whenBoostAdded`, `onMigrateToSupergroup`,
  `whenMigrateToSupergroup`, `onMigrateFromGroup`, `whenMigrateFromGroup`, `onMessageAutoDeleteTimerChanged`,
  `whenMessageAutoDeleteTimerChanged`, `onWebAppData`, `whenWebAppData`)
- **Poll.kt** - Poll and reaction handlers (`onPollUpdate`, `whenPollUpdate`, `onPollAnswer`, `whenPollAnswer`,
  `onPollAnswerFromUser`, `whenPollAnswerFromUser`, `onMessageReaction`, `whenMessageReaction`,
  `onMessageReactionInChat`,
  `whenMessageReactionInChat`, `onMessageReactionToMessage`, `whenMessageReactionToMessage`, `onMessageReactionCount`,
  `whenMessageReactionCount`, `onMessageReactionCountInChat`, `whenMessageReactionCountInChat`)
- **Payment.kt** - Payment event handlers (`onShippingQuery`, `whenShippingQuery`, `onShippingQueryFromUser`,
  `whenShippingQueryFromUser`, `onShippingQueryWithPayload`, `whenShippingQueryWithPayload`, `onPreCheckoutQuery`,
  `whenPreCheckoutQuery`, `onPreCheckoutQueryFromUser`, `whenPreCheckoutQueryFromUser`, `onPreCheckoutQueryWithPayload`,
  `whenPreCheckoutQueryWithPayload`, `onPreCheckoutQueryWithCurrency`, `whenPreCheckoutQueryWithCurrency`,
  `onPurchasedPaidMedia`, `whenPurchasedPaidMedia`, `onPurchasedPaidMediaFromUser`, `whenPurchasedPaidMediaFromUser`)
- **Business.kt** - Business event handlers (`onBusinessConnection`, `whenBusinessConnection`,
  `onBusinessConnectionFromUser`, `whenBusinessConnectionFromUser`, `onBusinessMessage`, `whenBusinessMessage`,
  `onBusinessMessageFromUser`, `whenBusinessMessageFromUser`, `onBusinessMessageInChat`, `whenBusinessMessageInChat`,
  `onEditedBusinessMessage`, `whenEditedBusinessMessage`, `onEditedBusinessMessageFromUser`,
  `whenEditedBusinessMessageFromUser`, `onEditedBusinessMessageInChat`, `whenEditedBusinessMessageInChat`,
  `onDeletedBusinessMessages`, `whenDeletedBusinessMessages`, `onDeletedBusinessMessagesInChat`,
  `whenDeletedBusinessMessagesInChat`)

**Naming Convention:**

- Functions starting with `when` (e.g., `whenText`, `whenPhoto`, `whenMatch`) are **terminal operations** that take a
  handler directly and cannot be chained further.
- Functions without `when` prefix (e.g., `match`, `allOf`, `anyOf`, `not`) are **composable** and take a build lambda
  for further nesting.

Each matcher provides two variants: one for the specific event type scope (e.g., inside `onMessageEvent`) and one
for the root level that auto-wraps with event type.

```kotlin
val dispatcher = HandlerTelegramEventDispatcher(handling {
    // Simple command handlers (supports /command and /command@bot_username formats)
    commandEndpoint("start") { ctx ->
        ctx.client.sendMessage(ctx.event.message.chat.id, "Welcome!")
    }

    // Command with typed arguments using BotArguments
    class BanArgs : BotArguments("Ban a user") {
        val username: String by requireArgument("User to ban")
        val duration: String? by optionalArgument("Ban duration")
    }
    command("ban", ::BanArgs) { ctx ->
        // ctx.arguments.username and ctx.arguments.duration are typed
        val username = ctx.arguments.username
        val duration = ctx.arguments.duration
    }

    // Command with subcommands
    command("admin") {
        handle { ctx -> /* Show admin help */ }
        subCommandEndpoint("status") { ctx -> /* Show status */ }
        subCommand("user") {
            subCommandEndpoint("list") { ctx -> /* List users */ }
            subCommandEndpoint("add") { ctx -> /* Add user */ }
        }
    }

    // Event type matching
    onMessageEvent {
        whenText("hello") { ctx -> /* exact text match */ }
        whenTextRegex(Regex("(?i)^hello")) { ctx -> /* regex match */ }
        whenTextContains("help", ignoreCase = true) { ctx -> /* substring match */ }
        whenTextStartsWith("/admin") { ctx -> /* prefix match */ }

        // Media type handlers
        whenPhoto { ctx -> /* photo message */ }
        whenVideo { ctx -> /* video message */ }
        whenDocument { ctx -> /* document message */ }
        whenSticker { ctx -> /* sticker message */ }

        // User/chat filters
        whenFromUser(123456L) { ctx -> /* from specific user */ }
        whenInChat(-100123456L) { ctx -> /* in specific chat */ }

        // Reply detection
        whenReply { ctx -> /* is a reply */ }
        whenReplyTo(messageId = 42L) { ctx -> /* reply to specific message */ }

        // Forwarded messages
        whenForwarded { ctx -> /* forwarded message */ }
        whenForwardedFromChat(-100123L) { ctx -> /* forwarded from specific chat */ }

        // Conditional handler with whenMatch
        whenMatch({ (it.event.message.text?.length ?: 0) > 10 }) { ctx ->
            ctx.client.sendMessage(ctx.event.message.chat.id, "Long message!")
        }

        // Composite matchers - terminal versions that take handler directly
        whenAllOf(
            { it.event.message.text != null },
            { it.event.message.chat.id == 100L }
        ) { ctx -> println("Private text message in chat 100") }

        whenAnyOf(
            { it.event.message.photo != null },
            { it.event.message.video != null }
        ) { ctx -> println("Photo or video") }

        whenNot({ it.event.message.text?.startsWith("/") == true }) { ctx ->
            println("Not a command")
        }
    }

    // Chat type filters
    whenPrivateChat { ctx -> /* private chat only */ }
    whenGroupChat { ctx -> /* group chat only */ }
    whenSupergroupChat { ctx -> /* supergroup only */ }
    whenChannel { ctx -> /* channel only */ }

    onCallbackQueryEvent {
        whenCallbackData("confirm") { ctx -> /* callback handling */ }
        whenCallbackDataRegex(Regex("action_\\d+")) { ctx -> /* pattern match */ }
    }

    onInlineQueryEvent {
        whenInlineQuery("search") { ctx -> /* exact query match */ }
        whenInlineQueryStartsWith("find:") { ctx -> /* prefix match */ }
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
command("process") {
    handle { ctx ->
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
}
```

#### Dead Letter Handler

A root-level `handle` acts as a fallback for all unhandled events, replacing the need for a separate
dead letter mechanism:

```kotlin
handling {
    command("start") { handle { /* ... */ } }
    command("help") { handle { /* ... */ } }

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
command("survey") {
  handle { ctx ->
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
          // Use id.chatId to send messages in the conversation
            val name = awaitTextMessage().event.message.text
          client.sendMessage(id.chatId.toString(), "Hello, $name!")
            val age = awaitTextMessage().event.message.text
          client.sendMessage(id.chatId.toString(), "Thanks! You are $age years old.")
        }
    }
}
```

The `ConversationId` is a data class with `chatId`, `userId` (optional), and `threadId` (optional):

- `ConversationId(chatId)` - whole chat shares one conversation
- `ConversationId(chatId, userId = userId)` - per-user conversation in a group
- `ConversationId(chatId, threadId = threadId)` - per-thread conversation

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
