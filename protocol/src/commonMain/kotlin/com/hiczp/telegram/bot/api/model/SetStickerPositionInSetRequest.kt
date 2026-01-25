// Auto-generated from Swagger specificationDo not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlinx.serialization.Serializable

@Serializable
public data class SetStickerPositionInSetRequest(
    /**
     * File identifier of the sticker
     */
    public val sticker: String,
    /**
     * New sticker position in the set, zero-based
     */
    public val position: Long,
)
