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
 * This object represents the scope to which bot commands are applied. Currently, the following 7 scopes are supported:
 * BotCommandScopeDefault BotCommandScopeAllPrivateChats BotCommandScopeAllGroupChats BotCommandScopeAllChatAdministrators BotCommandScopeChat BotCommandScopeChatAdministrators BotCommandScopeChatMember
 */
@Serializable
@JsonClassDiscriminator("type")
@OptIn(ExperimentalSerializationApi::class)
public sealed interface BotCommandScope

/**
 * Represents the default scope of bot commands. Default commands are used if no commands with a narrower scope are specified for the user.
 */
@Serializable
@SerialName("default")
public class BotCommandScopeDefault : BotCommandScope

/**
 * Represents the scope of bot commands, covering all private chats.
 */
@Serializable
@SerialName("all_private_chats")
public class BotCommandScopeAllPrivateChats : BotCommandScope

/**
 * Represents the scope of bot commands, covering all group and supergroup chats.
 */
@Serializable
@SerialName("all_group_chats")
public class BotCommandScopeAllGroupChats : BotCommandScope

/**
 * Represents the scope of bot commands, covering all group and supergroup chat administrators.
 */
@Serializable
@SerialName("all_chat_administrators")
public class BotCommandScopeAllChatAdministrators : BotCommandScope

/**
 * Represents the scope of bot commands, covering a specific chat.
 */
@Serializable
@SerialName("chat")
public data class BotCommandScopeChat(
    /**
     * Unique identifier for the target chat or username of the target supergroup (in the format `@supergroupusername`). Channel direct messages chats and channel chats aren't supported.
     */
    @SerialName("chat_id")
    public val chatId: String,
) : BotCommandScope

/**
 * Represents the scope of bot commands, covering all administrators of a specific group or supergroup chat.
 */
@Serializable
@SerialName("chat_administrators")
public data class BotCommandScopeChatAdministrators(
    /**
     * Unique identifier for the target chat or username of the target supergroup (in the format `@supergroupusername`). Channel direct messages chats and channel chats aren't supported.
     */
    @SerialName("chat_id")
    public val chatId: String,
) : BotCommandScope

/**
 * Represents the scope of bot commands, covering a specific member of a group or supergroup chat.
 */
@Serializable
@SerialName("chat_member")
public data class BotCommandScopeChatMember(
    /**
     * Unique identifier for the target chat or username of the target supergroup (in the format `@supergroupusername`). Channel direct messages chats and channel chats aren't supported.
     */
    @SerialName("chat_id")
    public val chatId: String,
    /**
     * Unique identifier of the target user
     */
    @SerialName("user_id")
    public val userId: Long,
) : BotCommandScope
