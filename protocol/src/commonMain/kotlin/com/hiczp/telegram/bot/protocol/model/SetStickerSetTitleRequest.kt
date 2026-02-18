// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.model

import kotlinx.serialization.Serializable

@Serializable
public data class SetStickerSetTitleRequest(
    /**
     * Sticker set name
     */
    public val name: String,
    /**
     * Sticker set title, 1-64 characters
     */
    public val title: String,
)
