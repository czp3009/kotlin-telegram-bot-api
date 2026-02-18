// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.model

import kotlinx.serialization.Serializable

/**
 * This object represents the content of a service message, sent whenever a user in the chat triggers a proximity alert set by another user.
 */
@Serializable
public data class ProximityAlertTriggered(
    /**
     * User that triggered the alert
     */
    public val traveler: User,
    /**
     * User that set the alert
     */
    public val watcher: User,
    /**
     * The distance between the users
     */
    public val distance: Long,
)
