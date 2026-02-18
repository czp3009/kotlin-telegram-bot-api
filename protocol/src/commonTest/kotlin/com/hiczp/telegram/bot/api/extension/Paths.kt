package com.hiczp.telegram.bot.api.extension

import com.hiczp.telegram.bot.api.type.BinaryInputFile
import com.hiczp.telegram.bot.api.type.toFormPart
import io.ktor.client.request.forms.*
import io.ktor.http.*
import io.ktor.utils.io.*
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem

internal fun Path.toInputFile(contentType: ContentType? = null): BinaryInputFile {
    val fileMeta = SystemFileSystem.metadataOrNull(this) ?: error("File $this not found")
    val fileSize = if (fileMeta.size != -1L) {
        fileMeta.size
    } else {
        null
    }
    return BinaryInputFile(
        content = ChannelProvider(fileSize) { ByteReadChannel(SystemFileSystem.source(this).buffered()) },
        contentType = contentType,
        fileName = name,
    )
}

internal fun Path.toFormPart(multipartName: String, contentType: ContentType? = null) =
    toInputFile(contentType).toFormPart(multipartName)
