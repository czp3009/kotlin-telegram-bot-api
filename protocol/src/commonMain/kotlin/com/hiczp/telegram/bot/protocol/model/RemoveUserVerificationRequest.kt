// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class RemoveUserVerificationRequest(
    /**
     * Unique identifier of the target user
     */
    @SerialName("user_id")
    public val userId: Long,
)
