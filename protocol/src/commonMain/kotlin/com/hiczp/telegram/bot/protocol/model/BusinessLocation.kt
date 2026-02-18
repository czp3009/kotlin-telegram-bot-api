// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.model

import kotlinx.serialization.Serializable

/**
 * Contains information about the location of a Telegram Business account.
 */
@Serializable
public data class BusinessLocation(
    /**
     * Address of the business
     */
    public val address: String,
    /**
     * *Optional*. Location of the business
     */
    public val location: Location? = null,
)
