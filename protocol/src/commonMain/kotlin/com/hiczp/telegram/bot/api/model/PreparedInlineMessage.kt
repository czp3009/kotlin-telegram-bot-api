// Auto-generated from Swagger specificationDo not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Describes an inline message to be sent by a user of a Mini App.
 */
@Serializable
public data class PreparedInlineMessage(
    /**
     * Unique identifier of the prepared message
     */
    public val id: String,
    /**
     * Expiration date of the prepared message, in Unix time. Expired prepared messages can no longer be used
     */
    @SerialName("expiration_date")
    public val expirationDate: Long,
)
