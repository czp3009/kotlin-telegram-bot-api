// Auto-generated from Swagger specificationDo not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlin.Long
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Describes a topic of a direct messages chat.
 */
@Serializable
public data class DirectMessagesTopic(
    /**
     * Unique identifier of the topic. This number may have more than 32 significant bits and some programming languages may have difficulty/silent defects in interpreting it. But it has at most 52 significant bits, so a 64-bit integer or double-precision float type are safe for storing this identifier.
     */
    @SerialName("topic_id")
    public val topicId: Long,
    /**
     * *Optional*. Information about the user that created the topic. Currently, it is always present
     */
    public val user: User? = null,
)
