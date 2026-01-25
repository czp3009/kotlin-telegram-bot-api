// Auto-generated from Swagger specificationDo not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
public data class SendStickerRequest(
    /**
     * Unique identifier of the business connection on behalf of which the message will be sent
     */
    @SerialName("business_connection_id")
    public val businessConnectionId: String? = null,
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
     * Sticker to send. Pass a file_id as String to send a file that exists on the Telegram servers (recommended), pass an HTTP URL as a String for Telegram to get a .WEBP sticker from the Internet, or upload a new .WEBP, .TGS, or .WEBM sticker using multipart/form-data. [More information on Sending Files ](https://core.telegram.org/bots/api#sending-files). Video and animated stickers can't be sent via an HTTP URL.
     */
    public val sticker: String,
    /**
     * Emoji associated with the sticker; only for just uploaded stickers
     */
    public val emoji: String? = null,
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
     * Unique identifier of the message effect to be added to the message; for private chats only
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
