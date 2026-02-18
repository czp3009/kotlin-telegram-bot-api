// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class ConvertGiftToStarsRequest(
    /**
     * Unique identifier of the business connection
     */
    @SerialName("business_connection_id")
    public val businessConnectionId: String,
    /**
     * Unique identifier of the regular gift that should be converted to Telegram Stars
     */
    @SerialName("owned_gift_id")
    public val ownedGiftId: String,
)
