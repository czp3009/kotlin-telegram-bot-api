// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Describes data sent from a Web App to the bot.
 */
@Serializable
public data class WebAppData(
    /**
     * The data. Be aware that a bad client can send arbitrary data in this field.
     */
    public val `data`: String,
    /**
     * Text of the *web_app* keyboard button from which the Web App was opened. Be aware that a bad client can send arbitrary data in this field.
     */
    @SerialName("button_text")
    public val buttonText: String,
)
