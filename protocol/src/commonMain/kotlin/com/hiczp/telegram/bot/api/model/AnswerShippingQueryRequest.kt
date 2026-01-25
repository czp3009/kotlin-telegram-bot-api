// Auto-generated from Swagger specificationDo not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlin.Boolean
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class AnswerShippingQueryRequest(
    /**
     * Unique identifier for the query to be answered
     */
    @SerialName("shipping_query_id")
    public val shippingQueryId: String,
    /**
     * Pass *True* if delivery to the specified address is possible and *False* if there are any problems (for example, if delivery to the specified address is not possible)
     */
    public val ok: Boolean,
    /**
     * Required if *ok* is *True*. A JSON-serialized array of available shipping options.
     */
    @SerialName("shipping_options")
    public val shippingOptions: List<ShippingOption>? = null,
    /**
     * Required if *ok* is *False*. Error message in human readable form that explains why it is impossible to complete the order (e.g. “Sorry, delivery to your desired address is unavailable”). Telegram will display this message to the user.
     */
    @SerialName("error_message")
    public val errorMessage: String? = null,
)
