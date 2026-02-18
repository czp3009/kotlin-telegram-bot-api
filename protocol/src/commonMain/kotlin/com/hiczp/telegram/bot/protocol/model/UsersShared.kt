// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * This object contains information about the users whose identifiers were shared with the bot using a KeyboardButtonRequestUsers button.
 */
@Serializable
public data class UsersShared(
    /**
     * Identifier of the request
     */
    @SerialName("request_id")
    public val requestId: Long,
    /**
     * Information about users shared with the bot.
     */
    public val users: List<SharedUser>,
)
