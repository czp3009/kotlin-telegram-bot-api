// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class VerifyUserRequest(
    /**
     * Unique identifier of the target user
     */
    @SerialName("user_id")
    public val userId: Long,
    /**
     * Custom description for the verification; 0-70 characters. Must be empty if the organization isn't allowed to provide a custom verification description.
     */
    @SerialName("custom_description")
    public val customDescription: String? = null,
)
