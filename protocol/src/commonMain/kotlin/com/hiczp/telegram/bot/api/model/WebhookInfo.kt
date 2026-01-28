// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Describes the current status of a webhook.
 */
@Serializable
public data class WebhookInfo(
    /**
     * Webhook URL, may be empty if webhook is not set up
     */
    public val url: String,
    /**
     * *True*, if a custom certificate was provided for webhook certificate checks
     */
    @SerialName("has_custom_certificate")
    public val hasCustomCertificate: Boolean,
    /**
     * Number of updates awaiting delivery
     */
    @SerialName("pending_update_count")
    public val pendingUpdateCount: Long,
    /**
     * *Optional*. Currently used webhook IP address
     */
    @SerialName("ip_address")
    public val ipAddress: String? = null,
    /**
     * *Optional*. Unix time for the most recent error that happened when trying to deliver an update via webhook
     */
    @SerialName("last_error_date")
    public val lastErrorDate: Long? = null,
    /**
     * *Optional*. Error message in human-readable format for the most recent error that happened when trying to deliver an update via webhook
     */
    @SerialName("last_error_message")
    public val lastErrorMessage: String? = null,
    /**
     * *Optional*. Unix time of the most recent error that happened when trying to synchronize available updates with Telegram datacenters
     */
    @SerialName("last_synchronization_error_date")
    public val lastSynchronizationErrorDate: Long? = null,
    /**
     * *Optional*. The maximum allowed number of simultaneous HTTPS connections to the webhook for update delivery
     */
    @SerialName("max_connections")
    public val maxConnections: Long? = null,
    /**
     * *Optional*. A list of update types the bot is subscribed to. Defaults to all update types except *chat_member*
     */
    @SerialName("allowed_updates")
    public val allowedUpdates: List<String>? = null,
)
