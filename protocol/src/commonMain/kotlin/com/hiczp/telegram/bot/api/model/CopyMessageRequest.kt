// Auto-generated from Swagger specificationDo not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
public data class CopyMessageRequest(
    /**
     * Unique identifier for the target chat or username of the target channel (in the format `@channelusername`)
     */
    @SerialName("chat_id")
    public val chatId: String,
    /**
     * Unique identifier for the target message thread (topic) of a forum; for forum supergroups and private chats of bots with forum topic mode enabled only
     */
    @SerialName("message_thread_id")
    public val messageThreadId: Long? = null,
    /**
     * Identifier of the direct messages topic to which the message will be sent; required if the message is sent to a direct messages chat
     */
    @SerialName("direct_messages_topic_id")
    public val directMessagesTopicId: Long? = null,
    /**
     * Unique identifier for the chat where the original message was sent (or channel username in the format `@channelusername`)
     */
    @SerialName("from_chat_id")
    public val fromChatId: String,
    /**
     * Message identifier in the chat specified in *from_chat_id*
     */
    @SerialName("message_id")
    public val messageId: Long,
    /**
     * New start timestamp for the copied video in the message
     */
    @SerialName("video_start_timestamp")
    public val videoStartTimestamp: Long? = null,
    /**
     * New caption for media, 0-1024 characters after entities parsing. If not specified, the original caption is kept
     */
    public val caption: String? = null,
    /**
     * Mode for parsing entities in the new caption. See [formatting options](https://core.telegram.org/bots/api#formatting-options) for more details.
     */
    @SerialName("parse_mode")
    public val parseMode: String? = null,
    /**
     * A JSON-serialized list of special entities that appear in the new caption, which can be specified instead of *parse_mode*
     */
    @SerialName("caption_entities")
    public val captionEntities: List<MessageEntity>? = null,
    /**
     * Pass *True*, if the caption must be shown above the message media. Ignored if a new caption isn't specified.
     */
    @SerialName("show_caption_above_media")
    public val showCaptionAboveMedia: Boolean? = null,
    /**
     * Sends the message [silently](https://telegram.org/blog/channels-2-0#silent-messages). Users will receive a notification with no sound.
     */
    @SerialName("disable_notification")
    public val disableNotification: Boolean? = null,
    /**
     * Protects the contents of the sent message from forwarding and saving
     */
    @SerialName("protect_content")
    public val protectContent: Boolean? = null,
    /**
     * Pass *True* to allow up to 1000 messages per second, ignoring [broadcasting limits](https://core.telegram.org/bots/faq#how-can-i-message-all-of-my-bot-39s-subscribers-at-once) for a fee of 0.1 Telegram Stars per message. The relevant Stars will be withdrawn from the bot's balance
     */
    @SerialName("allow_paid_broadcast")
    public val allowPaidBroadcast: Boolean? = null,
    /**
     * Unique identifier of the message effect to be added to the message; only available when copying to private chats
     */
    @SerialName("message_effect_id")
    public val messageEffectId: String? = null,
    /**
     * A JSON-serialized object containing the parameters of the suggested post to send; for direct messages chats only. If the message is sent as a reply to another suggested post, then that suggested post is automatically declined.
     */
    @SerialName("suggested_post_parameters")
    public val suggestedPostParameters: JsonElement? = null,
    /**
     * Description of the message to reply to
     */
    @SerialName("reply_parameters")
    public val replyParameters: JsonElement? = null,
    /**
     * Additional interface options. A JSON-serialized object for an [inline keyboard](https://core.telegram.org/bots/features#inline-keyboards), [custom reply keyboard](https://core.telegram.org/bots/features#keyboards), instructions to remove a reply keyboard or to force a reply from the user
     */
    @SerialName("reply_markup")
    public val replyMarkup: ReplyMarkup? = null,
)
