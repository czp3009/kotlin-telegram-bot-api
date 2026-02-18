// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * This object represents the audios displayed on a user's profile.
 */
@Serializable
public data class UserProfileAudios(
    /**
     * Total number of profile audios for the target user
     */
    @SerialName("total_count")
    public val totalCount: Long,
    /**
     * Requested profile audios
     */
    public val audios: List<Audio>,
)
