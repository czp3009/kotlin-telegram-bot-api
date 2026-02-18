// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class SetBusinessAccountNameRequest(
    /**
     * Unique identifier of the business connection
     */
    @SerialName("business_connection_id")
    public val businessConnectionId: String,
    /**
     * The new value of the first name for the business account; 1-64 characters
     */
    @SerialName("first_name")
    public val firstName: String,
    /**
     * The new value of the last name for the business account; 0-64 characters
     */
    @SerialName("last_name")
    public val lastName: String? = null,
)
