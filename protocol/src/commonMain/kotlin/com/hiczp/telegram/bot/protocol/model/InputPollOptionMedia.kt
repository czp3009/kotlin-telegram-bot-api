// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.model

import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

/**
 * This object represents the content of a poll option to be sent. It should be one of
 * InputMediaAnimation InputMediaLink InputMediaLivePhoto InputMediaLocation InputMediaPhoto InputMediaSticker InputMediaVenue InputMediaVideo
 */
@Serializable
@JsonClassDiscriminator("type")
@OptIn(ExperimentalSerializationApi::class)
public sealed interface InputPollOptionMedia

/**
 * Represents an HTTP link to be sent.
 */
@Serializable
@SerialName("link")
public data class InputMediaLink(
    /**
     * HTTP URL of the link
     */
    public val url: String,
) : InputPollOptionMedia

/**
 * Represents a sticker file to be sent.
 */
@Serializable
@SerialName("sticker")
public data class InputMediaSticker(
    /**
     * File to send. Pass a file_id to send a file that exists on the Telegram servers (recommended), pass an HTTP URL for Telegram to get a .WEBP sticker from the Internet, or pass "attach://<file_attach_name>" to upload a new .WEBP, .TGS, or .WEBM sticker using multipart/form-data under <file_attach_name> name. [More information on Sending Files ](https://core.telegram.org/bots/api#sending-files)
     */
    public val media: String,
    /**
     * *Optional*. Emoji associated with the sticker; only for just uploaded stickers
     */
    public val emoji: String? = null,
) : InputPollOptionMedia
