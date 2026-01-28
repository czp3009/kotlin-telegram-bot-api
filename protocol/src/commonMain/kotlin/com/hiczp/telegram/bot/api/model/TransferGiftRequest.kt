// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class TransferGiftRequest(
    /**
     * Unique identifier of the business connection
     */
    @SerialName("business_connection_id")
    public val businessConnectionId: String,
    /**
     * Unique identifier of the regular gift that should be transferred
     */
    @SerialName("owned_gift_id")
    public val ownedGiftId: String,
    /**
     * Unique identifier of the chat which will own the gift. The chat must be active in the last 24 hours.
     */
    @SerialName("new_owner_chat_id")
    public val newOwnerChatId: Long,
    /**
     * The amount of Telegram Stars that will be paid for the transfer from the business account balance. If positive, then the *can_transfer_stars* business bot right is required.
     */
    @SerialName("star_count")
    public val starCount: Long? = null,
)
