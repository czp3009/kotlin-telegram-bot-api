// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.form

import com.hiczp.telegram.bot.protocol.model.InputProfilePhoto
import io.ktor.client.request.forms.*

public data class SetMyProfilePhotoForm(
    /**
     * The new profile photo to set
     */
    public val photo: InputProfilePhoto,
    /**
     * Additional file attachments referenced via attach://<file_attach_name> in media fields
     */
    public val attachments: List<FormPart<ChannelProvider>>? = null,
)
