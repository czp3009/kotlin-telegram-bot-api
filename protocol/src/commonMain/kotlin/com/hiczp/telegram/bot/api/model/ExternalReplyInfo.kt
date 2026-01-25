// Auto-generated from Swagger specificationDo not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

/**
 * This object contains information about a message that is being replied to, which may come from another chat or forum topic.
 */
@Serializable
public data class ExternalReplyInfo(
    /**
     * Origin of the message replied to by the given message
     */
    public val origin: JsonElement?,
    /**
     * *Optional*. Chat the original message belongs to. Available only if the chat is a supergroup or a channel.
     */
    public val chat: JsonElement? = null,
    /**
     * *Optional*. Unique message identifier inside the original chat. Available only if the original chat is a supergroup or a channel.
     */
    @SerialName("message_id")
    public val messageId: Long? = null,
    /**
     * *Optional*. Options used for link preview generation for the original message, if it is a text message
     */
    @SerialName("link_preview_options")
    public val linkPreviewOptions: JsonElement? = null,
    /**
     * *Optional*. Message is an animation, information about the animation
     */
    public val animation: JsonElement? = null,
    /**
     * *Optional*. Message is an audio file, information about the file
     */
    public val audio: JsonElement? = null,
    /**
     * *Optional*. Message is a general file, information about the file
     */
    public val document: JsonElement? = null,
    /**
     * *Optional*. Message contains paid media; information about the paid media
     */
    @SerialName("paid_media")
    public val paidMedia: JsonElement? = null,
    /**
     * *Optional*. Message is a photo, available sizes of the photo
     */
    public val photo: List<PhotoSize>? = null,
    /**
     * *Optional*. Message is a sticker, information about the sticker
     */
    public val sticker: JsonElement? = null,
    /**
     * *Optional*. Message is a forwarded story
     */
    public val story: JsonElement? = null,
    /**
     * *Optional*. Message is a video, information about the video
     */
    public val video: JsonElement? = null,
    /**
     * *Optional*. Message is a [video note](https://telegram.org/blog/video-messages-and-telescope), information about the video message
     */
    @SerialName("video_note")
    public val videoNote: JsonElement? = null,
    /**
     * *Optional*. Message is a voice message, information about the file
     */
    public val voice: JsonElement? = null,
    /**
     * *Optional*. *True*, if the message media is covered by a spoiler animation
     */
    @SerialName("has_media_spoiler")
    public val hasMediaSpoiler: Boolean? = null,
    /**
     * *Optional*. Message is a checklist
     */
    public val checklist: JsonElement? = null,
    /**
     * *Optional*. Message is a shared contact, information about the contact
     */
    public val contact: JsonElement? = null,
    /**
     * *Optional*. Message is a dice with random value
     */
    public val dice: JsonElement? = null,
    /**
     * *Optional*. Message is a game, information about the game. [More about games ](https://core.telegram.org/bots/api#games)
     */
    public val game: JsonElement? = null,
    /**
     * *Optional*. Message is a scheduled giveaway, information about the giveaway
     */
    public val giveaway: JsonElement? = null,
    /**
     * *Optional*. A giveaway with public winners was completed
     */
    @SerialName("giveaway_winners")
    public val giveawayWinners: JsonElement? = null,
    /**
     * *Optional*. Message is an invoice for a [payment](https://core.telegram.org/bots/api#payments), information about the invoice. [More about payments ](https://core.telegram.org/bots/api#payments)
     */
    public val invoice: JsonElement? = null,
    /**
     * *Optional*. Message is a shared location, information about the location
     */
    public val location: JsonElement? = null,
    /**
     * *Optional*. Message is a native poll, information about the poll
     */
    public val poll: JsonElement? = null,
    /**
     * *Optional*. Message is a venue, information about the venue
     */
    public val venue: JsonElement? = null,
)
