# File Handling

File uploads use the handwritten `InputFile` type. File downloads use `getFile()` plus `downloadFile()`.

## InputFile

`InputFile` has two modes.

### Reference

Use an existing Telegram `file_id` or a public URL.

```kotlin
InputFile.reference("AgACAgIAAxkBAAIc...")
InputFile.reference(Url("https://example.com/image.png"))
```

### Binary Upload

Upload new bytes through a `ByteReadChannel` producer.

```kotlin
InputFile.binary(fileName = "document.pdf") {
    ByteReadChannel(bytes)
}

InputFile.binary(
    fileName = "image.jpg",
    contentType = ContentType.Image.JPEG
) {
    ByteReadChannel(imageBytes)
}
```

You can also pass a `ChannelProvider` explicitly:

```kotlin
InputFile.binary(
    content = ChannelProvider { ByteReadChannel(bytes) },
    fileName = "report.pdf",
    contentType = ContentType.Application.Pdf
)
```

## Upload A Local File

```kotlin
command("photo") {
    handle {
        val imagePath = Path("../resources/telegram.jpg")
        val inputFile = InputFile.binary(
            content = ChannelProvider {
                ByteReadChannel(SystemFileSystem.source(imagePath).buffered())
            },
            fileName = imagePath.name,
            contentType = ContentType.Image.JPEG,
        )

        replyPhoto(
            photo = inputFile,
            caption = "Here's telegram.jpg!"
        )
    }
}
```

## Echo A Photo By File ID

```kotlin
message {
    photo {
        handle {
            val photo = event.message.photo?.maxByOrNull { it.fileSize ?: 0 }
                ?: return@handle

            replyPhoto(
                photo = InputFile.reference(photo.fileId),
                caption = "Echoed photo"
            )
        }
    }
}
```

When Telegram already has the file, referencing `file_id` is cheaper than downloading and re-uploading it.

## Download Files

Use `client.getFile()` to resolve a file path, then `client.downloadFile()` to download it. `downloadFile()` returns an
`HttpStatement`, so process the response inside `execute`.

```kotlin
val fileInfo = client.getFile(fileId).getOrThrow()
val filePath = fileInfo.filePath ?: error("No file path")

client.downloadFile(filePath).execute { response ->
    val channel = response.bodyAsChannel()
    val bytes = channel.readRemaining().readByteArray()
}
```

## Download And Re-upload

```kotlin
message {
    sticker {
        handle {
            val sticker = event.message.sticker ?: return@handle

            launch {
                sendChatAction(ChatAction.TYPING)
            }

            val filePath = client.getFile(sticker.fileId).getOrThrow().filePath
                ?: return@handle

            client.downloadFile(filePath).execute { response ->
                val channel = response.bodyAsChannel()
                replyPhoto(
                    photo = InputFile.binary { channel },
                    caption = "Sticker echoed"
                )
            }
        }
    }
}
```

## Multipart Methods

Common upload methods include:

| Method          | File parameter |
|-----------------|----------------|
| `sendPhoto`     | `photo`        |
| `sendDocument`  | `document`     |
| `sendVideo`     | `video`        |
| `sendAnimation` | `animation`    |
| `sendVoice`     | `voice`        |
| `sendVideoNote` | `videoNote`    |
| `sendAudio`     | `audio`        |
| `sendSticker`   | `sticker`      |

The protocol module generates multipart form wrappers and scatter functions for these calls. The application module
also provides context actions such as `replyPhoto`, `replyDocument`, and `sendChatAction` for handler code.

## Dynamic Attachments

Some Telegram methods use `attach://name` references inside JSON-like multipart fields. Pass matching attachment parts
with `InputFile.toFormPart(name)`.

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

## Next Steps

- [Handler DSL](Handler-DSL)
- [Advanced Topics](Advanced-Topics)
