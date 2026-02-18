// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class SetPassportDataErrorsRequest(
    /**
     * User identifier
     */
    @SerialName("user_id")
    public val userId: Long,
    /**
     * A JSON-serialized array describing the errors
     */
    public val errors: List<PassportElementError>,
)
