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

# Run a single test (JVM)
./gradlew jvmTest --tests "com.hiczp.telegram.bot.api.TelegramBotApiTest.getUpdates"

# Clean build
./gradlew clean

# Regenerate API code from latest Telegram spec
./gradlew downloadSwagger
./gradlew generateKtorfitInterfaces
```

## Running Integration Tests

Integration tests require environment variables to be set:

- `BOT_TOKEN`: Telegram bot token
- `TEST_CHAT_ID`: Chat ID to use for sending test messages

These tests interact with the live Telegram API and are useful for validating API changes but require
a real bot token and test chat.

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
├── TelegramBotApi.kt               # Auto-generated API interface
├── TelegramBotApiExtensions.kt     # Extension functions
├── model/                          # Telegram API models (auto-generated)
├── form/                           # Multipart form classes (auto-generated)
├── type/                           # Core types (handwritten)
│   ├── TelegramResponse.kt         # Response wrapper with error handling
│   ├── TelegramErrorResponse.kt    # Internal error response deserialization
│   ├── InputFile.kt                # File upload handling
│   └── IncomingUpdate.kt           # Interface for update types used in polymorphic handling
├── extension/                      # Additional extensions (handwritten)
│   └── Messages.kt                 # Message deletion helpers
├── plugin/                         # Ktor client plugins
│   ├── TelegramFileDownloadPlugin.kt    # File download URL transformation
│   ├── TelegramLongPollingPlugin.kt     # Long polling timeout configuration
│   └── TelegramServerErrorPlugin.kt     # Server error to exception conversion
└── exception/                      # Error handling
    └── TelegramErrorResponseException.kt
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
- **KSP** (Kotlin Symbol Processing): Used for KtorFit compiler plugin (only for test source sets)
- **Dependencies**: Defined in `gradle/libs.versions.toml` using version catalogs
- **Multiplatform Targets**: Supports JVM, JS, WASM, and native targets across Apple, Linux, Windows, and Android
  platforms
- **BuildSrc**: Contains custom Gradle tasks for code generation (DownloadTelegramBotApiSwaggerTask,
  GenerateKtorfitInterfacesTask)

### Testing

- Tests use kotlinx-coroutines-test
- Platform-specific HTTP clients: CIO/Curl for JVM/desktop native, JS for web targets
- Common tests shared across all platforms in `commonTest` source set
- `desktopNativeTest` and `otherNativeTest` test source sets provide platform-specific configurations

### Ktor Client Setup

The library's plugins are designed to work together. A typical production client setup:

```kotlin
val httpClient = HttpClient(CIO) {
    install(TelegramLongPollingPlugin)      // For getUpdates long polling
    install(TelegramServerErrorPlugin)      // Convert API errors to exceptions
    install(HttpRequestRetry) {
        retryOnExceptionIf { request, response, cause ->
            cause is TelegramErrorResponseException && cause.isRetryable
        }
        delayMillis {
            (cause as? TelegramErrorResponseException)?.parameters?.retryAfter?.times(1000) ?: 1_000
        }
    }
}
```

**Important**: Do not use Ktor's `expectSuccess` option with `TelegramServerErrorPlugin`, as Telegram returns errors
with HTTP 200 OK status codes.

### File Upload Handling

The library provides a flexible `InputFile` type that supports multiple upload scenarios:

- **File reference**: Use existing `file_id` or URL (sent as string in multipart form)
- **File content**: Upload new file content via `ChannelProvider` with optional filename and content type
- **Extension**: `InputFile.toFormPart(multipartName)` converts to Ktor's `FormPart<ChannelProvider>`

For multi-part uploads with dynamic attachments (e.g., `sendMediaGroup`), form classes include an `attachments`
parameter that accepts a list of `FormPart<ChannelProvider>` for files referenced via `attach://<name>`.

### Long Polling

The `TelegramLongPollingPlugin` configures appropriate timeouts for long polling operations:

- Automatically increases timeout to 60 seconds for `getUpdates` method
- Sets both `requestTimeoutMillis` and `socketTimeoutMillis` to prevent premature connection closure
- Only affects `getUpdates`; other API requests use default timeout settings

### File Download

The `TelegramFileDownloadPlugin` automatically transforms file download URLs:

- Methods annotated with `@TelegramFileDownload` (like `downloadFile`) automatically modify the request path
- The plugin inserts `/file` between the bot token and file path: `https://api.telegram.org/bot<token>/file/<file_path>`
- Returns `HttpStatement` for streaming or processing downloaded content

### Server Error Handling

The `TelegramServerErrorPlugin` enables try-catch error handling by converting API errors to exceptions:

- Automatically parses error responses and throws `TelegramErrorResponseException`
- **Do not enable Ktor's `expectSuccess`** when using this plugin (Telegram returns errors with HTTP 200)
- When combined with Ktor's `HttpRequestRetry` plugin, check `cause.isRetryable` for retry logic
- Use `retryAfter` from error parameters for rate limiting (HTTP 429) delays

### Error Handling

`TelegramResponse<T>` wraps all API responses with convenient methods:

- `getOrThrow()`: Returns result or throws `TelegramErrorResponseException`
- `exceptionOrNull()`: Returns exception if response is an error, null otherwise
- `onSuccess/onError`: Callback-based error handling
- `fold()`: Functional pattern for success/error branches
- `toResult()`: Converts to Kotlin `Result<T>`
- `isRetryable` extension property: True for rate limiting (429) and server errors (5xx)

### Important Notes

- **Do not modify** files marked with `// Auto-generated from Swagger specification, do not modify this file manually`
- Changes to the API should be made by regenerating from the OpenAPI spec, not by editing generated files
- The `type/` directory contains handwritten code (e.g., `TelegramResponse.kt`, `IncomingUpdate.kt`,
  `TelegramErrorResponse.kt`) that is preserved during regeneration
- Rate limit handling: Use `isRetryable` extension property to identify retryable errors (HTTP 429 and 5xx)
