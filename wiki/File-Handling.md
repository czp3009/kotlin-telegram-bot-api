# File Handling

This page covers file upload and download using the `InputFile` sealed type.

## InputFile Types

```kotlin
sealed class InputFile {
    // Reference existing file by file_id or URL
    data class Reference(val id: String) : InputFile()

    // Upload binary content
    data class Binary(
        val fileName: String,
        val contentProvider: () -> ByteReadChannel
    ) : InputFile()
}
```

## File Upload

### Reference by File ID

When a user sends a file, you can reference it by `file_id`

```kotlin
onMessageEvent {
    whenMessageEventPhoto {
        // Forward photo using file_id
        val fileId = event.message.photo?.lastOrNull()?.fileId
        if (fileId != null) {
            client.sendPhoto(
                chatId = event.message.chat.id.toString(),
                photo = InputFile.reference(fileId)
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
    chatId = chatId,
    document = InputFile.binary(fileName = "document.pdf") {
        ByteReadChannel(bytes)
    }
)
```

## File Download

Use `TelegramBotClient.downloadFile()`

```kotlin
val file = client.getFile(fileId)
val bytes = client.downloadFile(file)

// Save to disk
File("downloaded_file.pdf").writeBytes(bytes)
```

## Supported Methods

Methods that support file upload

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

## Example: Echo Document

```kotlin
onMessageEvent {
    whenMessageEventDocument {
        val document = event.message.document ?: return@whenMessageEventDocument
        val fileId = document.fileId

        client.sendDocument(
            chatId = event.message.chat.id.toString(),
            document = InputFile.reference(fileId)
        )
    }
}
```

## Example: Generate and Send

```kotlin
commandEndpoint("report") {
    // Generate PDF report
    val pdfBytes = generateReport()

    client.sendDocument(
        chatId = event.message.chat.id.toString(),
        document = InputFile.binary(fileName = "report.pdf") {
            ByteReadChannel(pdfBytes)
        },
        caption = "Here is your report"
    )
}
```

## Next Steps

- [Handler DSL](Handler-DSL) - Event routing
- [Advanced Topics](Advanced-Topics) - Error handling
