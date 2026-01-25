// Auto-generated from Swagger specificationDo not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

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
    public val location: JsonElement? = null,
)
