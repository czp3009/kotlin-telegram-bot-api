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

# Run tests (JVM only - tests only run on JVM and desktop native targets)
./gradlew :protocol:jvmTest
./gradlew :client:jvmTest

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
- `IncomingUpdate.kt` - Marker interface for Update field types
- `IncomingUpdateContainer.kt` - Annotation for Update container

## Architecture

### Module Structure

```
kotlin-telegram-bot-api/
├── protocol/           # Core protocol definitions (Kotlin Multiplatform)
│   ├── model/         # Auto-generated data classes (Message, User, Chat, etc.)
│   ├── form/          # Auto-generated multipart form wrappers and extension functions
│   ├── query/         # Auto-generated query extension functions for JSON-serialized GET params
│   ├── type/          # Handwritten types (TelegramResponse, InputFile, etc.)
│   ├── plugin/        # Handwritten Ktor client plugins
│   ├── exception/     # Handwritten exception types
│   ├── extension/     # Handwritten extension functions
│   └── TelegramBotApi.kt  # Auto-generated Ktorfit interface
├── client/            # High-level client wrapper
│   └── TelegramBotClient.kt
└── buildSrc/          # Gradle tasks for code generation
    └── GenerateKtorfitInterfacesTask.kt
```

### Key Dependencies

- **Ktorfit**: HTTP client interface generation (like Retrofit for Ktor)
- **kotlinx.serialization**: JSON serialization
- **Ktor**: HTTP client engine

### Protocol Module (`:protocol`)

The core module containing:

- `TelegramBotApi` interface with Ktorfit annotations (`@GET`, `@POST`, `@Body`, `@Query`)
- Model classes with `@Serializable` and `@SerialName` for snake_case mapping
- Sealed interfaces for union types (`ReplyMarkup`, `BackgroundFill`, etc.)
- Multipart form wrappers for file uploads
- Query extensions for JSON-serialized GET parameters

### Client Module (`:client`)

High-level wrapper that:

- Provides `TelegramBotClient` with sensible defaults
- Configures JSON content negotiation
- Adds retry logic for transient failures and rate limiting
- Supports Telegram test environment
- Installs custom plugins (long polling, error handling, file download)

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

### Supported Platforms

Both modules target: JVM, Android, JS, WASM, Linux, macOS, Windows, iOS, watchOS, tvOS.

Tests only run on JVM and desktop native targets (Linux, macOS, Windows).
