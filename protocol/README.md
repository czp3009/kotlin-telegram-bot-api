# Protocol

This is a Kotlin Multiplatform project that provides type-safe definitions for the Telegram Bot API, including
auto-generated model classes, form classes, and API interfaces from the OpenAPI specification.

## Key Features

### Auto-Generated API Definitions

This project contains code automatically generated from
the [Telegram Bot API OpenAPI specification](https://github.com/czp3009/telegram-bot-api-swagger):

- **Model Classes**: Located in the `model/` directory, containing data classes for all Telegram API entities such as
  `Message`, `User`, `Chat`, etc.
    - Annotated with `@Serializable` for kotlinx.serialization support
    - Field names mapped using `@SerialName` (e.g., `firstName` maps to `first_name`)
    - Supports sealed interfaces for union types (e.g., `ReplyMarkup`)

- **Form Classes**: Located in the `form/` directory, providing convenient functions for multipart file upload
  operations
    - **Scatter functions**: Accept individual parameters as named arguments
    - **Bridge functions**: Accept string file references and convert to `FormPart`
    - **Form functions**: Accept pre-constructed form wrapper classes

- **API Interface**: `TelegramBotApi.kt` defines all API methods
    - Uses Ktorfit annotations (`@GET`, `@POST`, `@Body`, `@Query`)
    - All responses are wrapped in `TelegramResponse<T>` for unified error handling

### Utility Tools and Extensions

In addition to auto-generated code, the project also provides handwritten utility classes and extension functions:

- **`TelegramResponse<T>`** (`type/TelegramResponse.kt`): Unified response wrapper
  ```kotlin
  // Multiple error handling approaches
  val result = api.getMe().getOrThrow()
  api.getMe().onSuccess { user -> println(user) }
  api.getMe().fold(
      onSuccess = { user -> println(user) },
      onError = { error -> println(error) }
  )
  
  // Check if response is retryable (429 or 5xx errors)
  if (response.isRetryable) { /* retry logic */ }
  ```

- **Message Operation Extensions** (`extension/Messages.kt`):
  ```kotlin
  // Check if message is inaccessible
  if (message.isInaccessible) { /* handle inaccessible message */ }
  
  // Convenient deletion methods
  api.deleteMessage(message)
  api.deleteMessages(message1, message2, message3)
  ```

- **File Download Plugin** (`plugin/TelegramFileDownloadPlugin.kt`): Ktor client plugin that automatically transforms
  file download URLs
    - Converts `https://api.telegram.org/bot<token>/<file_path>` to
    - `https://api.telegram.org/file/bot<token>/<file_path>`

- **`InputFile`** (`type/InputFile.kt`): Flexible file upload type
    - Supports referencing existing files via `file_id` or URL
    - Supports uploading new file content via `ChannelProvider`
    - Provides `toFormPart()` extension function to convert to Ktor's `FormPart`

### Framework-Agnostic Design

While this project is designed for the [Ktorfit](https://github.com/Foso/Ktorfit) framework and uses its annotations, it
can theoretically be adapted to other HTTP client frameworks:

- Clear API interface definitions using standard REST annotations
- Model classes use kotlinx.serialization, framework-agnostic
- Can be used with any framework that supports similar annotations or can adapt to these definitions

**Important**: This project only provides API definitions and does not include a complete HTTP client implementation. It
must be used in conjunction with Ktorfit or other HTTP frameworks.

## Usage

For complete usage examples, please refer to the [`protocol-sample`](../protocol-sample) subproject:

## Code Generation

To regenerate API code from the latest specification:

```bash
# Download the latest OpenAPI specification
./gradlew downloadSwagger

# Generate Kotlin code
./gradlew generateKtorfitInterfaces
```

Generated files contain the comment `// Auto-generated from Swagger specification, do not modify this file manually`.
Please do not modify them manually.

## Related Links

- [Telegram Bot API Official Documentation](https://core.telegram.org/bots/api)
- [OpenAPI Specification Source](https://github.com/czp3009/telegram-bot-api-swagger)
- [Ktorfit Framework](https://github.com/Foso/Ktorfit)
