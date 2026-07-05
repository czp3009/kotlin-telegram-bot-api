// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.model

import kotlin.Boolean
import kotlin.Long
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class SetManagedBotAccessSettingsRequest(
    /**
     * User identifier of the managed bot whose access settings will be changed
     */
    @SerialName("user_id")
    public val userId: Long,
    /**
     * Pass *True*, if only selected users can access the bot. The bot's owner can always access it.
     */
    @SerialName("is_access_restricted")
    public val isAccessRestricted: Boolean,
    /**
     * A JSON-serialized list of up to 10 identifiers of users who will have access to the bot in addition to its owner. Ignored if *is_access_restricted* is false.
     */
    @SerialName("added_user_ids")
    public val addedUserIds: List<Long>? = null,
)
