package com.hiczp.telegram.bot.protocol.test.extension

import com.hiczp.telegram.bot.protocol.extension.asInputFile
import com.hiczp.telegram.bot.protocol.model.Animation
import com.hiczp.telegram.bot.protocol.model.Audio
import com.hiczp.telegram.bot.protocol.model.Document
import com.hiczp.telegram.bot.protocol.model.LivePhoto
import com.hiczp.telegram.bot.protocol.model.PassportFile
import com.hiczp.telegram.bot.protocol.model.PhotoSize
import com.hiczp.telegram.bot.protocol.model.Sticker
import com.hiczp.telegram.bot.protocol.model.Video
import com.hiczp.telegram.bot.protocol.model.VideoNote
import com.hiczp.telegram.bot.protocol.model.VideoQuality
import com.hiczp.telegram.bot.protocol.model.Voice
import com.hiczp.telegram.bot.protocol.type.InputFile
import com.hiczp.telegram.bot.protocol.type.ReferenceInputFile
import com.hiczp.telegram.bot.protocol.model.File as TelegramFile
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class FilesTest {
    @Test
    fun asInputFileReusesFileId() {
        val inputFiles = listOf(
            Animation(fileId = "animation", fileUniqueId = "u", width = 1L, height = 1L, duration = 1L).asInputFile(),
            Audio(fileId = "audio", fileUniqueId = "u", duration = 1L).asInputFile(),
            Document(fileId = "document", fileUniqueId = "u").asInputFile(),
            TelegramFile(fileId = "file", fileUniqueId = "u").asInputFile(),
            LivePhoto(fileId = "live_photo", fileUniqueId = "u", width = 1L, height = 1L, duration = 1L).asInputFile(),
            PassportFile(fileId = "passport", fileUniqueId = "u", fileSize = 1L, fileDate = 1L).asInputFile(),
            PhotoSize(fileId = "photo", fileUniqueId = "u", width = 1L, height = 1L).asInputFile(),
            Sticker(
                fileId = "sticker",
                fileUniqueId = "u",
                type = "regular",
                width = 1L,
                height = 1L,
                isAnimated = false,
                isVideo = false,
            ).asInputFile(),
            Video(fileId = "video", fileUniqueId = "u", width = 1L, height = 1L, duration = 1L).asInputFile(),
            VideoNote(fileId = "video_note", fileUniqueId = "u", length = 1L, duration = 1L).asInputFile(),
            VideoQuality(
                fileId = "video_quality",
                fileUniqueId = "u",
                width = 1L,
                height = 1L,
                codec = "h264",
            ).asInputFile(),
            Voice(fileId = "voice", fileUniqueId = "u", duration = 1L).asInputFile(),
        )

        val references = inputFiles.map { it.reference }
        assertEquals(
            listOf(
                "animation",
                "audio",
                "document",
                "file",
                "live_photo",
                "passport",
                "photo",
                "sticker",
                "video",
                "video_note",
                "video_quality",
                "voice",
            ),
            references,
        )
    }

    private val InputFile.reference: String
        get() = assertIs<ReferenceInputFile>(this).reference
}
