// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class UpgradeGiftRequest(
    /**
     * Unique identifier of the business connection
     */
    @SerialName("business_connection_id")
    public val businessConnectionId: String,
    /**
     * Unique identifier of the regular gift that should be upgraded to a unique one
     */
    @SerialName("owned_gift_id")
    public val ownedGiftId: String,
    /**
     * Pass *True* to keep the original gift text, sender and receiver in the upgraded gift
     */
    @SerialName("keep_original_details")
    public val keepOriginalDetails: Boolean? = null,
    /**
     * The amount of Telegram Stars that will be paid for the upgrade from the business account balance. If `gift.prepaid_upgrade_star_count > 0`, then pass 0, otherwise, the *can_transfer_stars* business bot right is required and `gift.upgrade_star_count` must be passed.
     */
    @SerialName("star_count")
    public val starCount: Long? = null,
)
