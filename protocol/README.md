# Protocol

Kotlin Multiplatform protocol module for the Telegram Bot API.

It provides type-safe API definitions generated from the Telegram Bot API OpenAPI specification, including models,
API interfaces, multipart form wrappers, and query/body helper extensions.

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
    - Auto-generated typed extensions for query-heavy GET methods
    - Serialize non-primitive query values (for example `List<String>`, `BotCommandScope`) via `Json.encodeToString`
    - Keep call sites strongly typed while delegating to generated `TelegramBotApi` string-based query methods

- **JSON body extensions** (`model/Bodies.kt`)
    - Auto-generated scatter extensions for POST methods with JSON request bodies
    - Accept individual parameters matching request class fields instead of requiring pre-constructed request objects
    - Provide an ergonomic API with named parameters and default values

- **Union type handling** (`union/`)
  - `Union<A, B>` sealed class for API responses that can return different types
  - `UnionSerializer` tries deserialization for each type in order (A first, then B)
  - `unionSerializersModule` provides contextual serializers for all Union types
  - Used when Telegram API returns either a `Message` object or a `Boolean` (e.g., `sendMessage` with
    `business_connection_id`)

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

- **Message extensions** (`extension/Messages.kt`) - Utilities for working with Message objects:
  ```kotlin
  // Check if a message is inaccessible (e.g., auto-deleted or from a deleted channel)
  if (message.isInaccessible) {
      handleInaccessibleMessage(message)
  }

  // Delete single or multiple messages
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

#### File Upload

##### 1) Reference an existing file (`file_id`) with a scatter function

```kotlin
api.sendPhoto(
  chatId = "123456789",
    photo = InputFile.reference("AgACAgQAAxkBA...")
)
```

##### 2) Upload binary content with a scatter function

```kotlin
api.sendDocument(
  chatId = "123456789",
    document = InputFile.binary(
        fileName = "report.pdf",
        contentType = ContentType.Application.Pdf
    ) {
        ByteReadChannel(pdfBytes)
    }
)
```

##### 3) Use form wrapper overload

```kotlin
val form = SendVideoForm(
  chatId = "123456789",
    video = InputFile.reference("https://example.com/video.mp4"),
    caption = "demo"
)

api.sendVideo(form)
```

##### 4) Dynamic multipart attachments (`attach://...`)

Some multipart form wrappers include an `attachments: List<FormPart<ChannelProvider>>?` property.
Use this when request payload fields reference dynamically attached files through `attach://<file_attach_name>`.

Example: Sending multiple photos in a media group:

```kotlin
api.sendMediaGroup(
  chatId = "123456789",
  media = listOf(
    InputMediaPhoto(media = "attach://photo1"),
    InputMediaPhoto(media = "attach://photo2"),
  ),
  attachments = listOf(
    InputFile.binary { ByteReadChannel(photo1Bytes) }.toFormPart("photo1"),
    InputFile.binary { ByteReadChannel(photo2Bytes) }.toFormPart("photo2"),
  )
)
```

Example: Setting bot profile photo with dynamic attachment:

```kotlin
api.setMyProfilePhoto(
  photo = InputProfilePhotoStatic(photo = "attach://photo1"),
  attachments = listOf(
    InputFile.binary(
      fileName = "profile.jpg",
      contentType = ContentType.Image.Jpeg
    ) { ByteReadChannel(imageBytes) }.toFormPart("photo1")
  )
)
```

#### File Download

Use `downloadFile` method with `HttpStatement` to download files from Telegram servers.
The `TelegramFileDownloadPlugin` must be installed for correct URL transformation.

```kotlin
// First get file info from Telegram
val fileInfo = api.getFile(fileId).getOrThrow()
val filePath = fileInfo.filePath ?: error("No file path available")

// Download the file
api.downloadFile(filePath).execute { response ->
  val byteReadChannel = response.bodyAsChannel()
  val buffer = ByteArray(4096)
  var totalBytes = 0L

  while (!byteReadChannel.exhausted()) {
    val bytesRead = byteReadChannel.readAvailable(buffer)
    totalBytes += bytesRead
    // Process bytes...
  }

  println("Downloaded $totalBytes bytes")
}
```

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

### JSON Body Extension Usage

For POST methods with JSON request bodies, `model/Bodies.kt` extensions allow calling with scattered parameters:

```kotlin
// Instead of constructing Request objects:
api.sendMessage(SendMessageRequest(chatId = "123456789", text = "Hello"))

// Use scattered parameters directly:
api.sendMessage(
  chatId = "123456789",
  text = "Hello",
  parseMode = ParseMode.Markdown,
  disableNotification = true
)
```

These extensions construct the Request object internally and call the underlying `TelegramBotApi` method.

### Union Type Usage

Some Telegram API methods can return different response types. The `Union<A, B>` sealed class handles these cases:

```kotlin
// sendMessage can return either Message or Boolean (when using business_connection_id)
val response = api.sendMessage(chatId = "123456789", text = "Hello")

response.onSuccess { union ->
  // Check which type was returned
  val message = union.firstOrNull()  // Returns Message if present
  val boolean = union.secondOrNull() // Returns Boolean if present

  when {
    message != null -> println("Sent message: ${message.messageId}")
    boolean != null -> println("Send result: $boolean")
  }
}
```

**Important:** The `unionSerializersModule` must be included in your Json configuration:

```kotlin
val json = Json(DefaultJson) {
  ignoreUnknownKeys = true
  encodeDefaults = true
  explicitNulls = false
  serializersModule += unionSerializersModule  // Required for Union types
}
```

## Ktor Client Setup

A typical production client setup using the `HttpClient` directly:

```kotlin
val httpClient = HttpClient(createKtorEngine()) {
  install(ContentNegotiation) {
    json(Json(DefaultJson) {
      ignoreUnknownKeys = true
      encodeDefaults = true
      explicitNulls = false
    })
  }
  install(DefaultRequest) {
    headers.appendIfNameAbsent(HttpHeaders.ContentType, ContentType.Application.Json.toString())
  }
    install(TelegramLongPollingPlugin)
  install(TelegramFileDownloadPlugin)
    install(TelegramServerErrorPlugin)
}

val api = Ktorfit.Builder()
  .httpClient(httpClient)
  .baseUrl("https://api.telegram.org/bot${botToken}/")
  .build()
  .createTelegramBotApi()
```

Important: Telegram can return error payloads with HTTP 200 status. Do not enable Ktor `expectSuccess` when using
`TelegramServerErrorPlugin`.

For a simpler setup with sensible defaults, consider using the [Client Module](../client) which provides
`TelegramBotClient` with pre-configured HTTP client settings.

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

## Supported Platforms

JVM, Android, JS, WASM, Linux, macOS, Windows, iOS, watchOS, tvOS, Android Native.

## Related Links

- [Telegram Bot API Official Documentation](https://core.telegram.org/bots/api)
- [OpenAPI Specification Source](https://github.com/czp3009/telegram-bot-api-swagger)
- [Ktorfit](https://github.com/Foso/Ktorfit)
- [Client Module](../client) - High-level client wrapper
- [Application Module](../application) - Bot application framework
