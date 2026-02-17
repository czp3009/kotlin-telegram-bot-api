package com.hiczp.telegram.bot.api.extension

import com.hiczp.telegram.bot.api.type.BinaryInputFile
import com.hiczp.telegram.bot.api.type.toFormPart
import io.ktor.http.*
import io.ktor.utils.io.*
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem

internal fun Path.toInputFile(contentType: ContentType? = null) =
    BinaryInputFile(
        contentType = contentType,
        fileName = name,
    ) { ByteReadChannel(SystemFileSystem.source(this).buffered()) }

internal fun Path.toFormPart(multipartName: String, contentType: ContentType? = null) =
    toInputFile(contentType).toFormPart(multipartName)
