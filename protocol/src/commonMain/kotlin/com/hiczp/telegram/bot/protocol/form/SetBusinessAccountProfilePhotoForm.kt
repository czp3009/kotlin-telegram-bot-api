// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.form

import com.hiczp.telegram.bot.protocol.model.InputProfilePhoto
import io.ktor.client.request.forms.*
import kotlinx.serialization.SerialName

public data class SetBusinessAccountProfilePhotoForm(
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
    /**
     * Additional file attachments referenced via attach://<file_attach_name> in media fields
     */
    public val attachments: List<FormPart<ChannelProvider>>? = null,
)
