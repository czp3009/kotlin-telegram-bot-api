// Auto-generated from Swagger specificationDo not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Error response returned when a request fails
 */
@Serializable
public data class Error(
    /**
     * Always false for error responses
     */
    public val ok: Boolean,
    /**
     * Error code
     */
    @SerialName("error_code")
    public val errorCode: Long,
    /**
     * Human-readable description of the error
     */
    public val description: String,
    public val parameters: ResponseParameters? = null,
)
