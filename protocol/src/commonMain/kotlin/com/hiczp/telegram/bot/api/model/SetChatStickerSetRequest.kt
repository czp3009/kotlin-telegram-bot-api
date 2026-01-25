// Auto-generated from Swagger specificationDo not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class SetChatStickerSetRequest(
    /**
     * Unique identifier for the target chat or username of the target supergroup (in the format `@supergroupusername`)
     */
    @SerialName("chat_id")
    public val chatId: String,
    /**
     * Name of the sticker set to be set as the group sticker set
     */
    @SerialName("sticker_set_name")
    public val stickerSetName: String,
)
