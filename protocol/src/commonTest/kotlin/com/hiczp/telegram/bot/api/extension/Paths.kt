package com.hiczp.telegram.bot.api.extension

import com.hiczp.telegram.bot.api.type.InputFile
import com.hiczp.telegram.bot.api.type.toFormPart
import io.ktor.http.*
import io.ktor.utils.io.*
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.readByteArray

internal fun Path.byteReadChannel(): ByteReadChannel =
    SystemFileSystem.source(this).buffered().use {
        it.readByteArray()
    }.let {
        ByteReadChannel(it)
    }

internal fun Path.toFormPart(multipartName: String, contentType: ContentType? = null) =
    InputFile(
        contentType = contentType,
        fileName = this.name,
    ) { this.byteReadChannel() }.toFormPart(multipartName)
