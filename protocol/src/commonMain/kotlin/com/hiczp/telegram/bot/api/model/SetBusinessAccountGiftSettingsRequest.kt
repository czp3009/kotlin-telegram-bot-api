// Auto-generated from Swagger specificationDo not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
public data class SetBusinessAccountGiftSettingsRequest(
    /**
     * Unique identifier of the business connection
     */
    @SerialName("business_connection_id")
    public val businessConnectionId: String,
    /**
     * Pass *True*, if a button for sending a gift to the user or by the business account must always be shown in the input field
     */
    @SerialName("show_gift_button")
    public val showGiftButton: Boolean,
    /**
     * Types of gifts accepted by the business account
     */
    @SerialName("accepted_gift_types")
    public val acceptedGiftTypes: JsonElement?,
)
