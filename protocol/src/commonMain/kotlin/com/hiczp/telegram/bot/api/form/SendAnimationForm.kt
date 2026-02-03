// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.api.form

import com.hiczp.telegram.bot.api.model.MessageEntity
import com.hiczp.telegram.bot.api.model.ReplyMarkup
import com.hiczp.telegram.bot.api.model.ReplyParameters
import com.hiczp.telegram.bot.api.model.SuggestedPostParameters
import io.ktor.client.request.forms.ChannelProvider
import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName

public data class SendAnimationForm(
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
     * Animation to send. Pass a file_id as String to send an animation that exists on the Telegram servers (recommended), pass an HTTP URL as a String for Telegram to get an animation from the Internet, or upload a new animation using multipart/form-data. [More information on Sending Files ](https://core.telegram.org/bots/api#sending-files)
     */
    public val animation: ChannelProvider,
    /**
     * Duration of sent animation in seconds
     */
    public val duration: Long? = null,
    /**
     * Animation width
     */
    public val width: Long? = null,
    /**
     * Animation height
     */
    public val height: Long? = null,
    /**
     * Thumbnail of the file sent; can be ignored if thumbnail generation for the file is supported server-side. The thumbnail should be in JPEG format and less than 200 kB in size. A thumbnail's width and height should not exceed 320. Ignored if the file is not uploaded using multipart/form-data. Thumbnails can't be reused and can be only uploaded as a new file, so you can pass “attach://<file_attach_name>” if the thumbnail was uploaded using multipart/form-data under <file_attach_name>. [More information on Sending Files ](https://core.telegram.org/bots/api#sending-files)
     */
    public val thumbnail: ChannelProvider? = null,
    /**
     * Animation caption (may also be used when resending animation by *file_id*), 0-1024 characters after entities parsing
     */
    public val caption: String? = null,
    /**
     * Mode for parsing entities in the animation caption. See [formatting options](https://core.telegram.org/bots/api#formatting-options) for more details.
     */
    @SerialName("parse_mode")
    public val parseMode: String? = null,
    /**
     * A JSON-serialized list of special entities that appear in the caption, which can be specified instead of *parse_mode*
     */
    @SerialName("caption_entities")
    public val captionEntities: List<MessageEntity>? = null,
    /**
     * Pass *True*, if the caption must be shown above the message media
     */
    @SerialName("show_caption_above_media")
    public val showCaptionAboveMedia: Boolean? = null,
    /**
     * Pass *True* if the animation needs to be covered with a spoiler animation
     */
    @SerialName("has_spoiler")
    public val hasSpoiler: Boolean? = null,
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
    public val suggestedPostParameters: SuggestedPostParameters? = null,
    /**
     * Description of the message to reply to
     */
    @SerialName("reply_parameters")
    public val replyParameters: ReplyParameters? = null,
    /**
     * Additional interface options. A JSON-serialized object for an [inline keyboard](https://core.telegram.org/bots/features#inline-keyboards), [custom reply keyboard](https://core.telegram.org/bots/features#keyboards), instructions to remove a reply keyboard or to force a reply from the user
     */
    @SerialName("reply_markup")
    public val replyMarkup: ReplyMarkup? = null,
)
