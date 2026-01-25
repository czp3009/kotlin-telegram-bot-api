// Auto-generated from Swagger specificationDo not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlin.Long
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
public data class SetChatMenuButtonRequest(
    /**
     * Unique identifier for the target private chat. If not specified, default bot's menu button will be changed
     */
    @SerialName("chat_id")
    public val chatId: Long? = null,
    /**
     * A JSON-serialized object for the bot's new menu button. Defaults to [MenuButtonDefault](https://core.telegram.org/bots/api#menubuttondefault)
     */
    @SerialName("menu_button")
    public val menuButton: JsonElement? = null,
)
