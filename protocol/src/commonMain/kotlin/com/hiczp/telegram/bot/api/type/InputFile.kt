package com.hiczp.telegram.bot.api.type

import io.ktor.client.request.forms.*
import io.ktor.http.*
import io.ktor.utils.io.*

/**
 * Represents a file input for Telegram Bot API multipart operations.
 *
 * This sealed type supports two upload modes:
 * - [ReferenceInputFile]: reference an existing Telegram file (`file_id`) or a public URL
 * - [BinaryInputFile]: upload new binary content using [ChannelProvider]
 *
 * Use [toFormPart] to convert an [InputFile] to Ktor multipart form data.
 */
sealed interface InputFile {
    companion object {
        /**
         * Creates an [InputFile] that references an existing Telegram file by `file_id`
         * or a publicly accessible URL.
         */
        fun reference(reference: String) = ReferenceInputFile(reference)

        /**
         * Creates an [InputFile] that references a publicly accessible URL.
         */
        fun reference(url: Url) = ReferenceInputFile(url)

        /**
         * Creates an [InputFile] that uploads binary content.
         *
         * @param content Binary content provider for multipart upload
         * @param fileName Optional filename exposed via multipart `Content-Disposition`
         * @param contentType Optional MIME type sent as multipart `Content-Type`
         */
        fun binary(content: ChannelProvider, fileName: String? = null, contentType: ContentType? = null) =
            BinaryInputFile(content, fileName, contentType)

        /**
         * Creates an [InputFile] that uploads binary content from a lazy [ByteReadChannel] producer.
         *
         * @param fileName Optional filename exposed via multipart `Content-Disposition`
         * @param contentType Optional MIME type sent as multipart `Content-Type`
         * @param block Producer that returns a [ByteReadChannel] with file bytes
         */
        fun binary(fileName: String? = null, contentType: ContentType? = null, block: () -> ByteReadChannel) =
            BinaryInputFile(fileName, contentType, block)
    }
}

/**
 * Represents a file reference (file_id or URL) to be used in Telegram Bot API.
 */
data class ReferenceInputFile(
    /**
     * file_id or URL that presents the file
     */
    val reference: String,
) : InputFile {
    constructor(url: Url) : this(reference = url.toString())

    init {
        require(reference.isNotBlank()) { "reference must not be blank" }
    }
}

/**
 * Represents binary file content to be uploaded to Telegram Bot API.
 */
data class BinaryInputFile(
    /**
     * Post a new file
     */
    val content: ChannelProvider,
    /**
     * The filename displayed to the user. If this value is not set, the multipart name will be used.
     */
    val fileName: String? = null,
    /**
     * When uploading new files, it is best to provide the MIME type
     */
    val contentType: ContentType? = null
) : InputFile {
    /**
     * Secondary constructor that creates a BinaryInputFile from a ByteReadChannel block.
     * Parameters are ordered as fileName, contentType, block for convenience.
     */
    constructor(
        fileName: String? = null,
        contentType: ContentType? = null,
        block: () -> ByteReadChannel,
    ) : this(content = ChannelProvider(block = block), fileName = fileName, contentType = contentType)

    init {
        if (fileName != null) {
            require(fileName.isNotBlank()) { "fileName must not be blank" }
        }
    }
}

/**
 * Converts this [InputFile] to a Ktor [FormPart] for multipart/form-data upload.
 *
 * For [ReferenceInputFile], the reference string is written as the multipart field content.
 * For [BinaryInputFile], the provided binary channel is used and multipart headers are populated.
 *
 * @param multipartName The multipart field name (for example: `photo`, `video`, `audio`)
 * @return Multipart form part represented as [FormPart]<[ChannelProvider]>
 */
fun InputFile.toFormPart(multipartName: String): FormPart<ChannelProvider> {
    require(multipartName.isNotBlank()) { "multipartName must not be blank" }
    return when (this) {
        is ReferenceInputFile -> FormPart(
            key = multipartName,
            value = ChannelProvider { ByteReadChannel(reference) },
        )

        is BinaryInputFile -> FormPart(
            key = multipartName,
            value = content,
            headers = headers {
                append(HttpHeaders.ContentDisposition, "filename='${fileName ?: multipartName}'")
                contentType?.let { append(HttpHeaders.ContentType, it.toString()) }
            }
        )
    }
}
