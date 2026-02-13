// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.api.model

import com.hiczp.telegram.bot.api.type.IncomingUpdate
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * This object contains information about a paid media purchase.
 */
@Serializable
public data class PaidMediaPurchased(
    /**
     * User who purchased the media
     */
    public val from: User,
    /**
     * Bot-specified paid media payload
     */
    @SerialName("paid_media_payload")
    public val paidMediaPayload: String,
) : IncomingUpdate
