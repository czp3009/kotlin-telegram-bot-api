// Auto-generated from Swagger specificationDo not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlin.Boolean
import kotlin.Long
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * This object defines the criteria used to request a suitable chat. Information about the selected chat will be shared with the bot when the corresponding button is pressed. The bot will be granted requested rights in the chat if appropriate. More about requesting chats .
 */
@Serializable
public data class KeyboardButtonRequestChat(
    /**
     * Signed 32-bit identifier of the request, which will be received back in the [ChatShared](https://core.telegram.org/bots/api#chatshared) object. Must be unique within the message
     */
    @SerialName("request_id")
    public val requestId: Long,
    /**
     * Pass *True* to request a channel chat, pass *False* to request a group or a supergroup chat.
     */
    @SerialName("chat_is_channel")
    public val chatIsChannel: Boolean,
    /**
     * *Optional*. Pass *True* to request a forum supergroup, pass *False* to request a non-forum chat. If not specified, no additional restrictions are applied.
     */
    @SerialName("chat_is_forum")
    public val chatIsForum: Boolean? = null,
    /**
     * *Optional*. Pass *True* to request a supergroup or a channel with a username, pass *False* to request a chat without a username. If not specified, no additional restrictions are applied.
     */
    @SerialName("chat_has_username")
    public val chatHasUsername: Boolean? = null,
    /**
     * *Optional*. Pass *True* to request a chat owned by the user. Otherwise, no additional restrictions are applied.
     */
    @SerialName("chat_is_created")
    public val chatIsCreated: Boolean? = null,
    /**
     * *Optional*. A JSON-serialized object listing the required administrator rights of the user in the chat. The rights must be a superset of *bot_administrator_rights*. If not specified, no additional restrictions are applied.
     */
    @SerialName("user_administrator_rights")
    public val userAdministratorRights: ChatAdministratorRights? = null,
    /**
     * *Optional*. A JSON-serialized object listing the required administrator rights of the bot in the chat. The rights must be a subset of *user_administrator_rights*. If not specified, no additional restrictions are applied.
     */
    @SerialName("bot_administrator_rights")
    public val botAdministratorRights: ChatAdministratorRights? = null,
    /**
     * *Optional*. Pass *True* to request a chat with the bot as a member. Otherwise, no additional restrictions are applied.
     */
    @SerialName("bot_is_member")
    public val botIsMember: Boolean? = null,
    /**
     * *Optional*. Pass *True* to request the chat's title
     */
    @SerialName("request_title")
    public val requestTitle: Boolean? = null,
    /**
     * *Optional*. Pass *True* to request the chat's username
     */
    @SerialName("request_username")
    public val requestUsername: Boolean? = null,
    /**
     * *Optional*. Pass *True* to request the chat's photo
     */
    @SerialName("request_photo")
    public val requestPhoto: Boolean? = null,
)
