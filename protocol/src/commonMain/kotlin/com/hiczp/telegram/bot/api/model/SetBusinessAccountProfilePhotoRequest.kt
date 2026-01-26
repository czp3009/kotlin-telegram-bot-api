// Auto-generated from Swagger specificationDo not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlin.Boolean
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class SetBusinessAccountProfilePhotoRequest(
    /**
     * Unique identifier of the business connection
     */
    @SerialName("business_connection_id")
    public val businessConnectionId: String,
    /**
     * The new profile photo to set
     */
    public val photo: InputProfilePhoto,
    /**
     * Pass *True* to set the public photo, which will be visible even if the main photo is hidden by the business account's privacy settings. An account can have only one public photo.
     */
    @SerialName("is_public")
    public val isPublic: Boolean? = null,
)
