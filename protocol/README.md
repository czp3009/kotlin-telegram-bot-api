# Protocol

Kotlin Multiplatform protocol module for the Telegram Bot API.

It provides type-safe API definitions generated from the Telegram Bot API OpenAPI specification, including models,
API interfaces, multipart form wrappers, and multipart helper extensions.

## Key Features

### Auto-Generated API Definitions

Code is generated from the
[Telegram Bot API OpenAPI specification](https://github.com/czp3009/telegram-bot-api-swagger).

- **Model classes** (`model/`)
    - Kotlin data/sealed types for Telegram entities (`Message`, `User`, `Chat`, etc.)
    - `@Serializable` support via kotlinx.serialization
    - `@SerialName` mapping for snake_case API fields
    - Union handling with sealed interfaces where applicable
  - Generated `Update` is annotated with `@IncomingUpdateContainer` (from `:protocol-annotation` module)
  - Optional non-primitive fields inside `Update` implement `IncomingUpdate` (from `:protocol-annotation` module)

- **Multipart form wrappers and extensions** (`form/`)
    - `*Form.kt`: wrapper classes for multipart operations
    - `Forms.kt`: extension functions for multipart requests
    - Two extension styles are generated for multipart operations:
        - **Scatter functions**: accept individual arguments (including `InputFile` upload fields)
        - **Form functions**: accept a pre-built `*Form` wrapper object
    - Multipart helpers convert `InputFile` values to `FormPart<ChannelProvider>` internally via `toFormPart`

- **API interface** (`TelegramBotApi.kt`)
    - Ktorfit-based interface (`@GET`, `@POST`, `@Body`, `@Query`)
    - All API calls return `TelegramResponse<T>` for unified success/error handling
  - GET methods with non-primitive query parameters expose raw serialized `String`/`String?` signatures

- **Query extensions** (`query/Queries.kt`)
    - Auto-generated typed extension functions for query-heavy GET methods
    - Serialize non-primitive query values (for example `List<String>`, `BotCommandScope`) via `Json.encodeToString`
    - Keep call sites strongly typed while delegating to generated `TelegramBotApi` string-based query methods

### Handwritten Types and Utilities

- **`TelegramResponse<T>`** (`type/TelegramResponse.kt`)
  ```kotlin
  val me = api.getMe().getOrThrow()

  api.getMe().onSuccess { user ->
      println(user)
  }

  api.getMe().fold(
      onSuccess = { user -> println(user) },
      onError = { error -> println(error) }
  )
  ```

- **Message extensions** (`extension/Messages.kt`)
  ```kotlin
  if (message.isInaccessible) {
      handleInaccessibleMessage(message)
  }

  api.deleteMessage(message)
  api.deleteMessages(message1, message2, message3)
  ```

- **Long polling plugin** (`plugin/TelegramLongPollingPlugin.kt`)
    - Applies long-polling-friendly timeouts for `getUpdates`

- **Server error plugin** (`plugin/TelegramServerErrorPlugin.kt`)
    - Converts Telegram API errors to `TelegramErrorResponseException`
    - Enables standard try/catch handling for API errors

- **File download plugin** (`plugin/TelegramFileDownloadPlugin.kt`)
    - Rewrites Telegram file download requests for methods marked with `@TelegramFileDownload`

## InputFile Multipart API

`InputFile` (`type/InputFile.kt`) is the central upload type used by multipart form wrappers and scatter functions.

It is a sealed type with two modes:

- `ReferenceInputFile`: references existing Telegram `file_id` or public URL
- `BinaryInputFile`: uploads new binary content via `ChannelProvider`

Factory methods:

- `InputFile.reference(reference: String)`
- `InputFile.reference(url: Url)`
- `InputFile.binary(content: ChannelProvider, fileName: String? = null, contentType: ContentType? = null)`
- `InputFile.binary(fileName: String? = null, contentType: ContentType? = null) { ... }`

`InputFile.toFormPart(multipartName)` converts the value to `FormPart<ChannelProvider>` for multipart requests.

### Multipart Usage Examples

#### 1) Reference an existing file (`file_id`) using scatter function

```kotlin
api.sendPhoto(
    chatId = ChatId.from(123456789L),
    photo = InputFile.reference("AgACAgQAAxkBA...")
)
```

#### 2) Upload binary content using scatter function

```kotlin
api.sendDocument(
    chatId = ChatId.from(123456789L),
    document = InputFile.binary(
        fileName = "report.pdf",
        contentType = ContentType.Application.Pdf
    ) {
        ByteReadChannel(pdfBytes)
    }
)
```

#### 3) Use form wrapper overload

```kotlin
val form = SendVideoForm(
    chatId = ChatId.from(123456789L),
    video = InputFile.reference("https://example.com/video.mp4"),
    caption = "demo"
)

api.sendVideo(form)
```

### Dynamic multipart attachments (`attach://...`)

Some multipart form wrappers include an `attachments: List<FormPart<ChannelProvider>>?` property.

Use this when request payload fields reference dynamically attached files through `attach://<file_attach_name>`.

### Query Extension Usage

For GET methods that require JSON-serialized query parameters, prefer `query/Queries.kt` extensions:

```kotlin
api.getUpdates(
    allowedUpdates = listOf("message", "callback_query")
)

api.getMyCommands(
    scope = BotCommandScope.Default,
    languageCode = "en"
)
```

These extensions perform serialization and call the underlying generated `TelegramBotApi` methods.

## Ktor Client Setup

A typical production client setup:

```kotlin
val httpClient = HttpClient(CIO) {
    install(TelegramLongPollingPlugin)
    install(TelegramServerErrorPlugin)
    install(HttpRequestRetry) {
        retryOnExceptionIf { _, _, cause ->
            cause is TelegramErrorResponseException && cause.isRetryable
        }
        delayMillis {
            (cause as? TelegramErrorResponseException)?.parameters?.retryAfter?.times(1000) ?: 1_000
        }
    }
}
```

Important: Telegram can return error payloads with HTTP 200 status. Do not enable Ktor `expectSuccess` when using
`TelegramServerErrorPlugin`.

## Exception Handling

`TelegramErrorResponseException` (`exception/TelegramErrorResponseException.kt`):

- Contains Telegram error payload details (`description`, `errorCode`, optional `parameters`)
- Supports `isRetryable` checks for rate-limit and server-side retry scenarios

## Code Generation

Regenerate protocol code from the latest spec:

```bash
./gradlew downloadSwagger
./gradlew generateKtorfitInterfaces
```

Generated files contain:

```kotlin
// Auto-generated from Swagger specification, do not modify this file manually
```

Do not edit generated files manually; update generation inputs and regenerate instead.

## Related Links

- [Telegram Bot API Official Documentation](https://core.telegram.org/bots/api)
- [OpenAPI Specification Source](https://github.com/czp3009/telegram-bot-api-swagger)
- [Ktorfit](https://github.com/Foso/Ktorfit)
