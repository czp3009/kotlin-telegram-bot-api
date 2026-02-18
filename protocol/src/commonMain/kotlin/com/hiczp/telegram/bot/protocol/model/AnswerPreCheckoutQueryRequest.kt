// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class AnswerPreCheckoutQueryRequest(
    /**
     * Unique identifier for the query to be answered
     */
    @SerialName("pre_checkout_query_id")
    public val preCheckoutQueryId: String,
    /**
     * Specify *True* if everything is alright (goods are available, etc.) and the bot is ready to proceed with the order. Use *False* if there are any problems.
     */
    public val ok: Boolean,
    /**
     * Required if *ok* is *False*. Error message in human readable form that explains the reason for failure to proceed with the checkout (e.g. "Sorry, somebody just bought the last of our amazing black T-shirts while you were busy filling out your payment details. Please choose a different color or garment!"). Telegram will display this message to the user.
     */
    @SerialName("error_message")
    public val errorMessage: String? = null,
)
