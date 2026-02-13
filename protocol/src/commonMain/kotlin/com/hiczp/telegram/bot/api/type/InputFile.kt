package com.hiczp.telegram.bot.api.type

import io.ktor.client.request.forms.*
import io.ktor.http.*
import io.ktor.utils.io.*

/**
 * This object represents the contents of a file to be uploaded. Must be posted using multipart/form-data in the usual way that files are uploaded via the browser.
 */
data class InputFile(
    /**
     * file_id or URL that presents the file
     */
    val reference: String? = null,
    /**
     * Post a new file
     */
    val content: ChannelProvider? = null,
    /**
     * The filename displayed to the user. If this value is not set, the multipart name will be used
     */
    val fileName: String? = null,
    /**
     * When uploading new files, it is best to provide the MIME type
     */
    val contentType: ContentType? = null,
) {
    constructor(url: Url) : this(reference = url.toString())
    constructor(
        contentType: ContentType? = null,
        fileName: String? = null,
        block: () -> ByteReadChannel,
    ) : this(content = ChannelProvider(block = block), fileName = fileName, contentType = contentType)

    init {
        if (reference == null && content == null) error("Either reference or content must be specified")
        if (fileName != null) {
            require(fileName.isNotBlank()) { "fileName must not be blank" }
        }
    }
}

/**
 * Converts this InputFile to a FormPart for multipart/form-data upload.
 *
 * @param multipartName The field name to use in the multipart form (e.g., "photo", "video", "audio")
 * @return A FormPart instance containing either the file reference or content
 */
fun InputFile.toFormPart(multipartName: String): FormPart<ChannelProvider> {
    require(multipartName.isNotBlank()) { "multipartName must not be blank" }
    return if (reference != null) {
        FormPart(key = multipartName, value = ChannelProvider { ByteReadChannel(reference) })
    } else if (content != null) {
        FormPart(
            key = multipartName,
            value = content,
            headers = headers {
                append(HttpHeaders.ContentDisposition, "filename='${fileName ?: multipartName}'")
                if (contentType != null) {
                    append(HttpHeaders.ContentType, contentType.toString())
                }
            }
        )
    } else {
        error("reference and content cannot both be null")
    }
}
