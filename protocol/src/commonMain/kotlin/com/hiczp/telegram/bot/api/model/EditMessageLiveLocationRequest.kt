// Auto-generated from Swagger specificationDo not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlin.Double
import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
public data class EditMessageLiveLocationRequest(
    /**
     * Unique identifier of the business connection on behalf of which the message to be edited was sent
     */
    @SerialName("business_connection_id")
    public val businessConnectionId: String? = null,
    /**
     * Required if *inline_message_id* is not specified. Unique identifier for the target chat or username of the target channel (in the format `@channelusername`)
     */
    @SerialName("chat_id")
    public val chatId: String? = null,
    /**
     * Required if *inline_message_id* is not specified. Identifier of the message to edit
     */
    @SerialName("message_id")
    public val messageId: Long? = null,
    /**
     * Required if *chat_id* and *message_id* are not specified. Identifier of the inline message
     */
    @SerialName("inline_message_id")
    public val inlineMessageId: String? = null,
    /**
     * Latitude of new location
     */
    public val latitude: Double,
    /**
     * Longitude of new location
     */
    public val longitude: Double,
    /**
     * New period in seconds during which the location can be updated, starting from the message send date. If 0x7FFFFFFF is specified, then the location can be updated forever. Otherwise, the new value must not exceed the current *live_period* by more than a day, and the live location expiration date must remain within the next 90 days. If not specified, then *live_period* remains unchanged
     */
    @SerialName("live_period")
    public val livePeriod: Long? = null,
    /**
     * The radius of uncertainty for the location, measured in meters; 0-1500
     */
    @SerialName("horizontal_accuracy")
    public val horizontalAccuracy: Double? = null,
    /**
     * Direction in which the user is moving, in degrees. Must be between 1 and 360 if specified.
     */
    public val heading: Long? = null,
    /**
     * The maximum distance for proximity alerts about approaching another chat member, in meters. Must be between 1 and 100000 if specified.
     */
    @SerialName("proximity_alert_radius")
    public val proximityAlertRadius: Long? = null,
    /**
     * A JSON-serialized object for a new [inline keyboard](https://core.telegram.org/bots/features#inline-keyboards).
     */
    @SerialName("reply_markup")
    public val replyMarkup: JsonElement? = null,
)
