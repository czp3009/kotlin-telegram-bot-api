// Auto-generated from Swagger specificationDo not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class TransferBusinessAccountStarsRequest(
    /**
     * Unique identifier of the business connection
     */
    @SerialName("business_connection_id")
    public val businessConnectionId: String,
    /**
     * Number of Telegram Stars to transfer; 1-10000
     */
    @SerialName("star_count")
    public val starCount: Long,
)
