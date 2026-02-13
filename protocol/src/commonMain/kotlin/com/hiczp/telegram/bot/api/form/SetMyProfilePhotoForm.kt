// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.api.form

import com.hiczp.telegram.bot.api.model.InputProfilePhoto
import io.ktor.client.request.forms.ChannelProvider
import io.ktor.client.request.forms.FormPart
import kotlin.collections.List

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
