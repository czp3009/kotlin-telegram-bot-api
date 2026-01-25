// Auto-generated from Swagger specificationDo not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class SetBusinessAccountUsernameRequest(
    /**
     * Unique identifier of the business connection
     */
    @SerialName("business_connection_id")
    public val businessConnectionId: String,
    /**
     * The new value of the username for the business account; 0-32 characters
     */
    public val username: String? = null,
)
