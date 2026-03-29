# File Handling

This page covers file upload and download using the `InputFile` sealed type.

## InputFile Types

`InputFile` is a sealed interface with two implementations, created via companion object factory methods.

### Reference (file_id or URL)

```kotlin
// Reference by file_id string
InputFile.reference("AgACAgIAAxkBAAIc...")

// Reference by URL (use Ktor's Url type)
InputFile.reference(Url("https://example.com/image.png"))
```

### Binary Upload

```kotlin
// Upload from ByteReadChannel producer
InputFile.binary(fileName = "document.pdf") {
    ByteReadChannel(bytes)
}

// With explicit content type
InputFile.binary(
    fileName = "image.png",
    contentType = ContentType.Image.PNG
) { ByteReadChannel(imageBytes) }

// From ChannelProvider directly
InputFile.binary(
    content = ChannelProvider { ByteReadChannel(bytes) },
    fileName = "report.pdf",
    contentType = ContentType.Application.Pdf
)
```

## File Upload

### Reference by File ID

When a user sends a file, you can reference it by `file_id`.

```kotlin
onMessageEvent {
    whenMessageEventPhoto {
        val photo = event.message.photo?.maxByOrNull { it.fileSize ?: 0 }
        if (photo != null) {
            replyPhoto(
                photo = InputFile.reference(photo.fileId),
                caption = "Echoed!"
            )
        }
    }
}
```

### Upload Binary Content

```kotlin
import com.hiczp.telegram.bot.protocol.type.InputFile
import io.ktor.utils.io.ByteReadChannel

// Upload from byte array
val bytes = readFileBytes("document.pdf")
client.sendDocument(
    chatId = chatId.toString(),
    document = InputFile.binary(fileName = "document.pdf") {
        ByteReadChannel(bytes)
    }
)
```

### Upload from File System

```kotlin
import okio.Path
import okio.SystemFileSystem
import okio.buffered

commandEndpoint("photo") {
    val imagePath = Path("../resources/photo.jpg")
    val inputFile = InputFile.binary(
        content = ChannelProvider {
            ByteReadChannel(SystemFileSystem.source(imagePath).buffered())
        },
        fileName = imagePath.name,
        contentType = ContentType.Image.JPEG,
    )
    replyPhoto(photo = inputFile, caption = "Here's the file!")
}
```

## File Download

Use `client.getFile()` to get file metadata, then `client.downloadFile()` to download.

`downloadFile()` returns an `HttpStatement`. Use `.execute { }` to process the response.

```kotlin
val fileInfo = client.getFile(fileId).getOrThrow()
val filePath = fileInfo.filePath ?: error("No file path")

client.downloadFile(filePath).execute { response ->
    val channel: ByteReadChannel = response.bodyAsChannel()
    // Read bytes from channel
    val bytes = channel.readRemaining().readByteArray()
}
```

## Supported Methods

Methods that support file upload via `InputFile`:

| Method          | File Parameter |
|-----------------|----------------|
| `sendPhoto`     | `photo`        |
| `sendDocument`  | `document`     |
| `sendVideo`     | `video`        |
| `sendAnimation` | `animation`    |
| `sendVoice`     | `voice`        |
| `sendVideoNote` | `videoNote`    |
| `sendAudio`     | `audio`        |
| `sendSticker`   | `sticker`      |

All of these have both a base API method on `TelegramBotApi` and convenience `send*`/`reply*` extensions in
`TelegramBotEventContext<MessageEvent>`.

## Example: Echo Document

```kotlin
onMessageEvent {
    whenMessageEventDocument {
        val document = event.message.document ?: return@whenMessageEventDocument
        replyDocument(
            document = InputFile.reference(document.fileId)
        )
    }
}
```

## Example: Download and Re-upload Sticker

```kotlin
onMessageEvent {
    whenMessageEventSticker {
        val sticker = event.message.sticker ?: return@whenMessageEventSticker
        sendChatAction(ChatAction.TYPING)

        val filePath = client.getFile(sticker.fileId).getOrThrow().filePath
            ?: return@whenMessageEventSticker

        client.downloadFile(filePath).execute { response ->
            val byteReadChannel = response.bodyAsChannel()
            replyPhoto(
                photo = InputFile.binary { byteReadChannel },
                caption = "Echoed sticker!"
            )
        }
    }
}
```

## Next Steps

- [Handler DSL](Handler-DSL) - Event routing
- [Advanced Topics](Advanced-Topics) - Error handling, union types
