// Auto-generated from Swagger specificationDo not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * This object represent a user's profile pictures.
 */
@Serializable
public data class UserProfilePhotos(
    /**
     * Total number of profile pictures the target user has
     */
    @SerialName("total_count")
    public val totalCount: Long,
    /**
     * Requested profile pictures (in up to 4 sizes each)
     */
    public val photos: List<List<PhotoSize>>,
)
