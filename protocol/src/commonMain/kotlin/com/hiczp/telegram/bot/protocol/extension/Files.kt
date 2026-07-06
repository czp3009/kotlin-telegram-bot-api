package com.hiczp.telegram.bot.protocol.extension

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
import com.hiczp.telegram.bot.protocol.model.File as TelegramFile

/**
 * Reuses this Telegram-hosted file by its file_id.
 */
fun Animation.asInputFile(): InputFile = InputFile.reference(fileId)

/**
 * Reuses this Telegram-hosted file by its file_id.
 */
fun Audio.asInputFile(): InputFile = InputFile.reference(fileId)

/**
 * Reuses this Telegram-hosted file by its file_id.
 */
fun Document.asInputFile(): InputFile = InputFile.reference(fileId)

/**
 * Reuses this Telegram-hosted file by its file_id.
 */
fun TelegramFile.asInputFile(): InputFile = InputFile.reference(fileId)

/**
 * Reuses this Telegram-hosted file by its file_id.
 */
fun LivePhoto.asInputFile(): InputFile = InputFile.reference(fileId)

/**
 * Reuses this Telegram-hosted file by its file_id.
 */
fun PassportFile.asInputFile(): InputFile = InputFile.reference(fileId)

/**
 * Reuses this Telegram-hosted file by its file_id.
 */
fun PhotoSize.asInputFile(): InputFile = InputFile.reference(fileId)

/**
 * Reuses this Telegram-hosted file by its file_id.
 */
fun Sticker.asInputFile(): InputFile = InputFile.reference(fileId)

/**
 * Reuses this Telegram-hosted file by its file_id.
 */
fun Video.asInputFile(): InputFile = InputFile.reference(fileId)

/**
 * Reuses this Telegram-hosted file by its file_id.
 */
fun VideoNote.asInputFile(): InputFile = InputFile.reference(fileId)

/**
 * Reuses this Telegram-hosted file by its file_id.
 */
fun VideoQuality.asInputFile(): InputFile = InputFile.reference(fileId)

/**
 * Reuses this Telegram-hosted file by its file_id.
 */
fun Voice.asInputFile(): InputFile = InputFile.reference(fileId)
