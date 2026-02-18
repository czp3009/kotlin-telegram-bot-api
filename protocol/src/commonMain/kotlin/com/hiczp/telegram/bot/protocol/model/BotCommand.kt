// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.model

import kotlinx.serialization.Serializable

/**
 * This object represents a bot command.
 */
@Serializable
public data class BotCommand(
    /**
     * Text of the command; 1-32 characters. Can contain only lowercase English letters, digits and underscores.
     */
    public val command: String,
    /**
     * Description of the command; 1-256 characters.
     */
    public val description: String,
)
