// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Describes a service message about the rejection of a suggested post.
 */
@Serializable
public data class SuggestedPostDeclined(
    /**
     * *Optional*. Message containing the suggested post. Note that the [Message](https://core.telegram.org/bots/api#message) object in this field will not contain the *reply_to_message* field even if it itself is a reply.
     */
    @SerialName("suggested_post_message")
    public val suggestedPostMessage: Message? = null,
    /**
     * *Optional*. Comment with which the post was declined
     */
    public val comment: String? = null,
)
