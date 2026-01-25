// Auto-generated from Swagger specificationDo not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

/**
 * This object represents the content of a service message, sent whenever a user in the chat triggers a proximity alert set by another user.
 */
@Serializable
public data class ProximityAlertTriggered(
    /**
     * User that triggered the alert
     */
    public val traveler: JsonElement?,
    /**
     * User that set the alert
     */
    public val watcher: JsonElement?,
    /**
     * The distance between the users
     */
    public val distance: Long,
)
