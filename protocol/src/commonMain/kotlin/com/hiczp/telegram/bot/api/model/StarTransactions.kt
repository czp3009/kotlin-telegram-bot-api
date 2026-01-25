// Auto-generated from Swagger specificationDo not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlinx.serialization.Serializable

/**
 * Contains a list of Telegram Star transactions.
 */
@Serializable
public data class StarTransactions(
    /**
     * The list of transactions
     */
    public val transactions: List<StarTransaction>,
)
