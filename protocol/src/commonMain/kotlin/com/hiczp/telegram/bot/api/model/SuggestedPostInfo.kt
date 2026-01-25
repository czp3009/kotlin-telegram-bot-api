// Auto-generated from Swagger specificationDo not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

/**
 * Contains information about a suggested post.
 */
@Serializable
public data class SuggestedPostInfo(
    /**
     * State of the suggested post. Currently, it can be one of “pending”, “approved”, “declined”.
     */
    public val state: String,
    /**
     * *Optional*. Proposed price of the post. If the field is omitted, then the post is unpaid.
     */
    public val price: JsonElement? = null,
    /**
     * *Optional*. Proposed send date of the post. If the field is omitted, then the post can be published at any time within 30 days at the sole discretion of the user or administrator who approves it.
     */
    @SerialName("send_date")
    public val sendDate: Long? = null,
)
