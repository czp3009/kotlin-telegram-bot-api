// Auto-generated from Swagger specificationDo not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

/**
 * This object represents one row of the high scores table for a game.
 *
 * And that's about all we've got for now. If you've got any questions, please check out our Bot FAQ
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
    public val user: JsonElement?,
    /**
     * Score
     */
    public val score: Long,
)
