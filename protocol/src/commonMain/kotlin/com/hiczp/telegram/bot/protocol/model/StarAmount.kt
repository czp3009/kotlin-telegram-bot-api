// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.model

import kotlin.Long
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Describes an amount of Telegram Stars.
 */
@Serializable
public data class StarAmount(
    /**
     * Integer amount of Telegram Stars, rounded to 0; can be negative
     */
    public val amount: Long,
    /**
     * *Optional*. The number of 1/1000000000 shares of Telegram Stars; from -999999999 to 999999999; can be negative if and only if *amount* is non-positive
     */
    @SerialName("nanostar_amount")
    public val nanostarAmount: Long? = null,
)
