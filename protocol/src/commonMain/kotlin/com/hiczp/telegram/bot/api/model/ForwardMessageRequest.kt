// Auto-generated from Swagger specificationDo not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class ForwardMessageRequest(
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
     * Identifier of the direct messages topic to which the message will be forwarded; required if the message is forwarded to a direct messages chat
     */
    @SerialName("direct_messages_topic_id")
    public val directMessagesTopicId: Long? = null,
    /**
     * Unique identifier for the chat where the original message was sent (or channel username in the format `@channelusername`)
     */
    @SerialName("from_chat_id")
    public val fromChatId: String,
    /**
     * New start timestamp for the forwarded video in the message
     */
    @SerialName("video_start_timestamp")
    public val videoStartTimestamp: Long? = null,
    /**
     * Sends the message [silently](https://telegram.org/blog/channels-2-0#silent-messages). Users will receive a notification with no sound.
     */
    @SerialName("disable_notification")
    public val disableNotification: Boolean? = null,
    /**
     * Protects the contents of the forwarded message from forwarding and saving
     */
    @SerialName("protect_content")
    public val protectContent: Boolean? = null,
    /**
     * Unique identifier of the message effect to be added to the message; only available when forwarding to private chats
     */
    @SerialName("message_effect_id")
    public val messageEffectId: String? = null,
    /**
     * A JSON-serialized object containing the parameters of the suggested post to send; for direct messages chats only
     */
    @SerialName("suggested_post_parameters")
    public val suggestedPostParameters: SuggestedPostParameters? = null,
    /**
     * Message identifier in the chat specified in *from_chat_id*
     */
    @SerialName("message_id")
    public val messageId: Long,
)
