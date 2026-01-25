// Auto-generated from Swagger specificationDo not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlin.Boolean
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class RemoveBusinessAccountProfilePhotoRequest(
    /**
     * Unique identifier of the business connection
     */
    @SerialName("business_connection_id")
    public val businessConnectionId: String,
    /**
     * Pass *True* to remove the public photo, which is visible even if the main photo is hidden by the business account's privacy settings. After the main photo is removed, the previous profile photo (if present) becomes the main photo.
     */
    @SerialName("is_public")
    public val isPublic: Boolean? = null,
)
