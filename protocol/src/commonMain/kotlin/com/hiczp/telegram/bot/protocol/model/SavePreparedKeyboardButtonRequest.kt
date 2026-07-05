// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.model

import kotlin.Long
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class SavePreparedKeyboardButtonRequest(
    /**
     * Unique identifier of the target user that can use the button
     */
    @SerialName("user_id")
    public val userId: Long,
    /**
     * A JSON-serialized object describing the button to be saved. The button must be of the type *request_users*, *request_chat*, or *request_managed_bot*.
     */
    public val button: KeyboardButton,
)
