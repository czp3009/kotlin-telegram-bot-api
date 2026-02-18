// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

/**
 * This object describes the source of a transaction, or its recipient for outgoing transactions. Currently, it can be one of
 * TransactionPartnerUser TransactionPartnerChat TransactionPartnerAffiliateProgram TransactionPartnerFragment TransactionPartnerTelegramAds TransactionPartnerTelegramApi TransactionPartnerOther
 */
@Serializable
@JsonClassDiscriminator("type")
@OptIn(ExperimentalSerializationApi::class)
public sealed interface TransactionPartner

/**
 * Describes a transaction with a user.
 */
@Serializable
@SerialName("user")
public data class TransactionPartnerUser(
    /**
     * Type of the transaction, currently one of “invoice_payment” for payments via invoices, “paid_media_payment” for payments for paid media, “gift_purchase” for gifts sent by the bot, “premium_purchase” for Telegram Premium subscriptions gifted by the bot, “business_account_transfer” for direct transfers from managed business accounts
     */
    @SerialName("transaction_type")
    public val transactionType: String,
    /**
     * Information about the user
     */
    public val user: User,
    /**
     * *Optional*. Information about the affiliate that received a commission via this transaction. Can be available only for “invoice_payment” and “paid_media_payment” transactions.
     */
    public val affiliate: AffiliateInfo? = null,
    /**
     * *Optional*. Bot-specified invoice payload. Can be available only for “invoice_payment” transactions.
     */
    @SerialName("invoice_payload")
    public val invoicePayload: String? = null,
    /**
     * *Optional*. The duration of the paid subscription. Can be available only for “invoice_payment” transactions.
     */
    @SerialName("subscription_period")
    public val subscriptionPeriod: Long? = null,
    /**
     * *Optional*. Information about the paid media bought by the user; for “paid_media_payment” transactions only
     */
    @SerialName("paid_media")
    public val paidMedia: List<PaidMedia>? = null,
    /**
     * *Optional*. Bot-specified paid media payload. Can be available only for “paid_media_payment” transactions.
     */
    @SerialName("paid_media_payload")
    public val paidMediaPayload: String? = null,
    /**
     * *Optional*. The gift sent to the user by the bot; for “gift_purchase” transactions only
     */
    public val gift: Gift? = null,
    /**
     * *Optional*. Number of months the gifted Telegram Premium subscription will be active for; for “premium_purchase” transactions only
     */
    @SerialName("premium_subscription_duration")
    public val premiumSubscriptionDuration: Long? = null,
) : TransactionPartner

/**
 * Describes a transaction with a chat.
 */
@Serializable
@SerialName("chat")
public data class TransactionPartnerChat(
    /**
     * Information about the chat
     */
    public val chat: Chat,
    /**
     * *Optional*. The gift sent to the chat by the bot
     */
    public val gift: Gift? = null,
) : TransactionPartner

/**
 * Describes the affiliate program that issued the affiliate commission received via this transaction.
 */
@Serializable
@SerialName("affiliate_program")
public data class TransactionPartnerAffiliateProgram(
    /**
     * *Optional*. Information about the bot that sponsored the affiliate program
     */
    @SerialName("sponsor_user")
    public val sponsorUser: User? = null,
    /**
     * The number of Telegram Stars received by the bot for each 1000 Telegram Stars received by the affiliate program sponsor from referred users
     */
    @SerialName("commission_per_mille")
    public val commissionPerMille: Long,
) : TransactionPartner

/**
 * Describes a withdrawal transaction with Fragment.
 */
@Serializable
@SerialName("fragment")
public data class TransactionPartnerFragment(
    /**
     * *Optional*. State of the transaction if the transaction is outgoing
     */
    @SerialName("withdrawal_state")
    public val withdrawalState: RevenueWithdrawalState? = null,
) : TransactionPartner

/**
 * Describes a withdrawal transaction to the Telegram Ads platform.
 */
@Serializable
@SerialName("telegram_ads")
public class TransactionPartnerTelegramAds : TransactionPartner

/**
 * Describes a transaction with payment for paid broadcasting.
 */
@Serializable
@SerialName("telegram_api")
public data class TransactionPartnerTelegramApi(
    /**
     * The number of successful requests that exceeded regular limits and were therefore billed
     */
    @SerialName("request_count")
    public val requestCount: Long,
) : TransactionPartner

/**
 * Describes a transaction with an unknown source or recipient.
 */
@Serializable
@SerialName("other")
public class TransactionPartnerOther : TransactionPartner
