// Auto-generated from Swagger specificationDo not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlin.Long
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Describes why a request was unsuccessful.
 */
@Serializable
public data class ResponseParameters(
    /**
     * *Optional*. The group has been migrated to a supergroup with the specified identifier. This number may have more than 32 significant bits and some programming languages may have difficulty/silent defects in interpreting it. But it has at most 52 significant bits, so a signed 64-bit integer or double-precision float type are safe for storing this identifier.
     */
    @SerialName("migrate_to_chat_id")
    public val migrateToChatId: Long? = null,
    /**
     * *Optional*. In case of exceeding flood control, the number of seconds left to wait before the request can be repeated
     */
    @SerialName("retry_after")
    public val retryAfter: Long? = null,
)
