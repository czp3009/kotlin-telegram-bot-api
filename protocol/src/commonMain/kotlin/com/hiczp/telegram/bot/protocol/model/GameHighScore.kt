// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.model

import kotlin.Long
import kotlinx.serialization.Serializable

/**
 * This object represents one row of the high scores table for a game.
 *
 * And that's about all we've got for now. If you've got any questions, please check out our Bot FAQ  -
 */
@Serializable
public data class GameHighScore(
    /**
     * Position in high score table for the game
     */
    public val position: Long,
    /**
     * User
     */
    public val user: User,
    /**
     * Score
     */
    public val score: Long,
)
