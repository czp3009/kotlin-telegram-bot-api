# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a Kotlin Multiplatform library for the Telegram Bot API. It provides type-safe, auto-generated Kotlin bindings
for Telegram's REST API, supporting JVM, JavaScript, WebAssembly, and native platforms (iOS, macOS, Android, Linux,
Windows).

## Build Commands

```bash
# Build all targets
./gradlew build

# Build specific platform
./gradlew jvmJar          # JVM version
./gradlew jsJar           # JavaScript version
./gradlew wasmJsJar       # WebAssembly version

# Run tests
./gradlew test            # Common tests
./gradlew jvmTest         # JVM tests
./gradlew allTests        # All platform tests

# Clean build
./gradlew clean

# Regenerate API code from latest Telegram spec
./gradlew downloadSwagger
./gradlew generateKtorfitInterfaces
```

## Architecture

### Code Generation Pipeline

The project uses a sophisticated code generation workflow to stay in sync with Telegram's API:

1. **Download Phase** (`DownloadTelegramBotApiSwaggerTask`):
    - Fetches the latest OpenAPI specification from GitHub releases (czp3009/telegram-bot-api-swagger)
    - Stored in `build/generated/swagger/telegram-bot-api.json`

2. **Generation Phase** (`GenerateKtorfitInterfacesTask`):
    - Parses the OpenAPI spec using Jackson
    - Generates Kotlin code using KotlinPoet
    - Outputs to `protocol/src/commonMain/kotlin/com/hiczp/telegram/bot/api/`

### Key Generated Components

- **`TelegramBotApi.kt`**: Main HTTP interface with KtorFit annotations (`@GET`, `@POST`, `@Body`, `@Query`)
- **`model/`**: Data classes for Telegram entities (Message, User, Chat, etc.) with `@Serializable` annotations
- **`form/`**: Specialized classes for multipart file uploads, converting InputFile to ChannelProvider
- **`TelegramBotApiExtensions.kt`**: Helper extensions for the API

### Source Structure

```
protocol/src/commonMain/kotlin/com/hiczp/telegram/bot/api/
├── TelegramBotApi.kt          # Auto-generated API interface
├── TelegramBotApiExtensions.kt   # Extension functions
├── model/                      # Telegram API models
├── form/                       # Multipart form classes
├── type/                       # Core types
│   ├── TelegramResponse.kt       # Response wrapper with error handling
│   └── IncomingUpdate.kt        # Interface for update types used in polymorphic handling
└── extension/                   # Additional extensions
```

### Special Type Handling

The code generator handles several complex patterns from the OpenAPI spec:

- **Sealed Interfaces**: Union types (oneOf) become sealed interfaces with `@JsonClassDiscriminator`
- **ReplyMarkup**: All reply markup types implement the `ReplyMarkup` sealed interface
- **IncomingUpdate**: Types used in Update's optional non-primitive fields implement `IncomingUpdate` for polymorphic
  handling
- **Field-Overlapping Unions**: Types like `MaybeInaccessibleMessage` use the largest subclass for deserialization

### Build Configuration

- **JVM Toolchain**: Java 21
- **KSP** (Kotlin Symbol Processing): Used for KtorFit compiler plugin
- **Dependencies**: Defined in `gradle/libs.versions.toml` using version catalogs
- **Multiplatform Targets**: Supports JVM, JS, WASM, and native targets across Apple, Linux, Windows, and Android
  platforms

### Testing

- Tests use kotlinx-coroutines-test
- Platform-specific HTTP clients: CIO for JVM/desktop native, JS for web targets
- Common tests shared across all platforms in `commonTest` source set

### Important Notes

- **Do not modify** files marked with `// Auto-generated from Swagger specification, do not modify this file manually`
- Changes to the API should be made by regenerating from the OpenAPI spec, not by editing generated files
- The `type/` directory contains handwritten code (e.g., `TelegramResponse.kt`, `IncomingUpdate.kt`) that is preserved
  during regeneration
