// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.model

import kotlin.collections.List
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
