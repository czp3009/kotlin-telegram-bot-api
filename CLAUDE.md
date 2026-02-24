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
    └── TelegramBotApplication.kt
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
pipeline,
allowing pre/post processing:

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

### Supported Platforms

All modules target: JVM, Android, JS, WASM, Linux, macOS, Windows, iOS, watchOS, tvOS, Android Native.

Tests only run on JVM and desktop native targets (Linux, macOS, Windows).
