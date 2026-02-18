// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.model

import kotlin.Long
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Contains parameters of a post that is being suggested by the bot.
 */
@Serializable
public data class SuggestedPostParameters(
    /**
     * *Optional*. Proposed price for the post. If the field is omitted, then the post is unpaid.
     */
    public val price: SuggestedPostPrice? = null,
    /**
     * *Optional*. Proposed send date of the post. If specified, then the date must be between 300 second and 2678400 seconds (30 days) in the future. If the field is omitted, then the post can be published at any time within 30 days at the sole discretion of the user who approves it.
     */
    @SerialName("send_date")
    public val sendDate: Long? = null,
)
