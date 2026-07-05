// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.model

import kotlin.Long
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class ReplaceManagedBotTokenRequest(
    /**
     * User identifier of the managed bot whose token will be replaced
     */
    @SerialName("user_id")
    public val userId: Long,
)
