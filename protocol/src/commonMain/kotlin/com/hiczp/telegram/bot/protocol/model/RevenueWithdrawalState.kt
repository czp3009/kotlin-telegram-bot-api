// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.model

import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

/**
 * This object describes the state of a revenue withdrawal operation. Currently, it can be one of
 * RevenueWithdrawalStatePending RevenueWithdrawalStateSucceeded RevenueWithdrawalStateFailed
 */
@Serializable
@JsonClassDiscriminator("type")
@OptIn(ExperimentalSerializationApi::class)
public sealed interface RevenueWithdrawalState

/**
 * The withdrawal is in progress.
 */
@Serializable
@SerialName("pending")
public class RevenueWithdrawalStatePending : RevenueWithdrawalState

/**
 * The withdrawal succeeded.
 */
@Serializable
@SerialName("succeeded")
public data class RevenueWithdrawalStateSucceeded(
    /**
     * Date the withdrawal was completed in Unix time
     */
    public val date: Long,
    /**
     * An HTTPS URL that can be used to see transaction details
     */
    public val url: String,
) : RevenueWithdrawalState

/**
 * The withdrawal failed and the transaction was refunded.
 */
@Serializable
@SerialName("failed")
public class RevenueWithdrawalStateFailed : RevenueWithdrawalState
