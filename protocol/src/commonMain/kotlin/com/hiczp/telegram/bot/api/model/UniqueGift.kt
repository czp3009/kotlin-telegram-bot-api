// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * This object describes a unique gift that was upgraded from a regular gift.
 */
@Serializable
public data class UniqueGift(
    /**
     * Identifier of the regular gift from which the gift was upgraded
     */
    @SerialName("gift_id")
    public val giftId: String,
    /**
     * Human-readable name of the regular gift from which this unique gift was upgraded
     */
    @SerialName("base_name")
    public val baseName: String,
    /**
     * Unique name of the gift. This name can be used in `https://t.me/nft/...` links and story areas
     */
    public val name: String,
    /**
     * Unique number of the upgraded gift among gifts upgraded from the same regular gift
     */
    public val number: Long,
    /**
     * Model of the gift
     */
    public val model: UniqueGiftModel,
    /**
     * Symbol of the gift
     */
    public val symbol: UniqueGiftSymbol,
    /**
     * Backdrop of the gift
     */
    public val backdrop: UniqueGiftBackdrop,
    /**
     * *Optional*. *True*, if the original regular gift was exclusively purchaseable by Telegram Premium subscribers
     */
    @SerialName("is_premium")
    public val isPremium: Boolean? = null,
    /**
     * *Optional*. *True*, if the gift was used to craft another gift and isn't available anymore
     */
    @SerialName("is_burned")
    public val isBurned: Boolean? = null,
    /**
     * *Optional*. *True*, if the gift is assigned from the TON blockchain and can't be resold or transferred in Telegram
     */
    @SerialName("is_from_blockchain")
    public val isFromBlockchain: Boolean? = null,
    /**
     * *Optional*. The color scheme that can be used by the gift's owner for the chat's name, replies to messages and link previews; for business account gifts and gifts that are currently on sale only
     */
    public val colors: UniqueGiftColors? = null,
    /**
     * *Optional*. Information about the chat that published the gift
     */
    @SerialName("publisher_chat")
    public val publisherChat: Chat? = null,
)
