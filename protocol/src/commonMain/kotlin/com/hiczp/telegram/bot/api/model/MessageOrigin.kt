// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

/**
 * This object describes the origin of a message. It can be one of
 * MessageOriginUser MessageOriginHiddenUser MessageOriginChat MessageOriginChannel
 */
@Serializable
@JsonClassDiscriminator("type")
@OptIn(ExperimentalSerializationApi::class)
public sealed interface MessageOrigin

/**
 * The message was originally sent by a known user.
 */
@Serializable
@SerialName("user")
public data class MessageOriginUser(
    /**
     * Date the message was sent originally in Unix time
     */
    public val date: Long,
    /**
     * User that sent the message originally
     */
    @SerialName("sender_user")
    public val senderUser: User,
) : MessageOrigin

/**
 * The message was originally sent by an unknown user.
 */
@Serializable
@SerialName("hidden_user")
public data class MessageOriginHiddenUser(
    /**
     * Date the message was sent originally in Unix time
     */
    public val date: Long,
    /**
     * Name of the user that sent the message originally
     */
    @SerialName("sender_user_name")
    public val senderUserName: String,
) : MessageOrigin

/**
 * The message was originally sent on behalf of a chat to a group chat.
 */
@Serializable
@SerialName("chat")
public data class MessageOriginChat(
    /**
     * Date the message was sent originally in Unix time
     */
    public val date: Long,
    /**
     * Chat that sent the message originally
     */
    @SerialName("sender_chat")
    public val senderChat: Chat,
    /**
     * *Optional*. For messages originally sent by an anonymous chat administrator, original message author signature
     */
    @SerialName("author_signature")
    public val authorSignature: String? = null,
) : MessageOrigin

/**
 * The message was originally sent to a channel chat.
 */
@Serializable
@SerialName("channel")
public data class MessageOriginChannel(
    /**
     * Date the message was sent originally in Unix time
     */
    public val date: Long,
    /**
     * Channel chat to which the message was originally sent
     */
    public val chat: Chat,
    /**
     * Unique message identifier inside the chat
     */
    @SerialName("message_id")
    public val messageId: Long,
    /**
     * *Optional*. Signature of the original post author
     */
    @SerialName("author_signature")
    public val authorSignature: String? = null,
) : MessageOrigin
