// Auto-generated from Swagger specificationDo not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlin.Long
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

/**
 * Describes a service message about the approval of a suggested post.
 */
@Serializable
public data class SuggestedPostApproved(
    /**
     * *Optional*. Message containing the suggested post. Note that the [Message](https://core.telegram.org/bots/api#message) object in this field will not contain the *reply_to_message* field even if it itself is a reply.
     */
    @SerialName("suggested_post_message")
    public val suggestedPostMessage: JsonElement? = null,
    /**
     * *Optional*. Amount paid for the post
     */
    public val price: JsonElement? = null,
    /**
     * Date when the post will be published
     */
    @SerialName("send_date")
    public val sendDate: Long,
)
