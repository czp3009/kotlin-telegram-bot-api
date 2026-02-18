// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * This object represents an inline keyboard button that copies specified text to the clipboard.
 */
@Serializable
public data class CopyTextButton(
    /**
     * The text to be copied to the clipboard; 1-256 characters
     */
    public val text: String,
)
