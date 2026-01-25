// Auto-generated from Swagger specificationDo not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlin.Long
import kotlinx.serialization.Serializable

/**
 * Describes the birthdate of a user.
 */
@Serializable
public data class Birthdate(
    /**
     * Day of the user's birth; 1-31
     */
    public val day: Long,
    /**
     * Month of the user's birth; 1-12
     */
    public val month: Long,
    /**
     * *Optional*. Year of the user's birth
     */
    public val year: Long? = null,
)
