// Auto-generated from Swagger specificationDo not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Describes the paid media added to a message.
 */
@Serializable
public data class PaidMediaInfo(
    /**
     * The number of Telegram Stars that must be paid to buy access to the media
     */
    @SerialName("star_count")
    public val starCount: Long,
    /**
     * Information about the paid media
     */
    @SerialName("paid_media")
    public val paidMedia: List<PaidMedia>,
)
