// Auto-generated from Swagger specificationDo not modify this file manually
package com.hiczp.telegram.bot.api.form

import io.ktor.client.request.forms.InputProvider
import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName

public data class SetWebhookForm(
    /**
     * HTTPS URL to send updates to. Use an empty string to remove webhook integration
     */
    public val url: String,
    /**
     * Upload your public key certificate so that the root certificate in use can be checked. See our [self-signed guide](https://core.telegram.org/bots/self-signed) for details.
     */
    public val certificate: InputProvider? = null,
    /**
     * The fixed IP address which will be used to send webhook requests instead of the IP address resolved through DNS
     */
    @SerialName("ip_address")
    public val ipAddress: String? = null,
    /**
     * The maximum allowed number of simultaneous HTTPS connections to the webhook for update delivery, 1-100. Defaults to *40*. Use lower values to limit the load on your bot's server, and higher values to increase your bot's throughput.
     */
    @SerialName("max_connections")
    public val maxConnections: Long? = null,
    /**
     * A JSON-serialized list of the update types you want your bot to receive. For example, specify `["message", "edited_channel_post", "callback_query"]` to only receive updates of these types. See [Update](https://core.telegram.org/bots/api#update) for a complete list of available update types. Specify an empty list to receive all update types except *chat_member*, *message_reaction*, and *message_reaction_count* (default). If not specified, the previous setting will be used.Please note that this parameter doesn't affect updates created before the call to the setWebhook, so unwanted updates may be received for a short period of time.
     */
    @SerialName("allowed_updates")
    public val allowedUpdates: List<String>? = null,
    /**
     * Pass *True* to drop all pending updates
     */
    @SerialName("drop_pending_updates")
    public val dropPendingUpdates: Boolean? = null,
    /**
     * A secret token to be sent in a header “X-Telegram-Bot-Api-Secret-Token” in every webhook request, 1-256 characters. Only characters `A-Z`, `a-z`, `0-9`, `_` and `-` are allowed. The header is useful to ensure that the request comes from a webhook set by you.
     */
    @SerialName("secret_token")
    public val secretToken: String? = null,
)
