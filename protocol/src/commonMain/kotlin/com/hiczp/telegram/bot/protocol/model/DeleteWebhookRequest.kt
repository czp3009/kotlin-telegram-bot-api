// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.model

import kotlin.Boolean
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class DeleteWebhookRequest(
    /**
     * Pass *True* to drop all pending updates
     */
    @SerialName("drop_pending_updates")
    public val dropPendingUpdates: Boolean? = null,
)
