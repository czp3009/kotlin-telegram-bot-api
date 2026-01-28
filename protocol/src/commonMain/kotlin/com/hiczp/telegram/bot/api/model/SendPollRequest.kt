// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class SendPollRequest(
    /**
     * Unique identifier of the business connection on behalf of which the message will be sent
     */
    @SerialName("business_connection_id")
    public val businessConnectionId: String? = null,
    /**
     * Unique identifier for the target chat or username of the target channel (in the format `@channelusername`). Polls can't be sent to channel direct messages chats.
     */
    @SerialName("chat_id")
    public val chatId: String,
    /**
     * Unique identifier for the target message thread (topic) of a forum; for forum supergroups and private chats of bots with forum topic mode enabled only
     */
    @SerialName("message_thread_id")
    public val messageThreadId: Long? = null,
    /**
     * Poll question, 1-300 characters
     */
    public val question: String,
    /**
     * Mode for parsing entities in the question. See [formatting options](https://core.telegram.org/bots/api#formatting-options) for more details. Currently, only custom emoji entities are allowed
     */
    @SerialName("question_parse_mode")
    public val questionParseMode: String? = null,
    /**
     * A JSON-serialized list of special entities that appear in the poll question. It can be specified instead of *question_parse_mode*
     */
    @SerialName("question_entities")
    public val questionEntities: List<MessageEntity>? = null,
    /**
     * A JSON-serialized list of 2-12 answer options
     */
    public val options: List<InputPollOption>,
    /**
     * *True*, if the poll needs to be anonymous, defaults to *True*
     */
    @SerialName("is_anonymous")
    public val isAnonymous: Boolean? = null,
    /**
     * Poll type, “quiz” or “regular”, defaults to “regular”
     */
    public val type: String? = null,
    /**
     * *True*, if the poll allows multiple answers, ignored for polls in quiz mode, defaults to *False*
     */
    @SerialName("allows_multiple_answers")
    public val allowsMultipleAnswers: Boolean? = null,
    /**
     * 0-based identifier of the correct answer option, required for polls in quiz mode
     */
    @SerialName("correct_option_id")
    public val correctOptionId: Long? = null,
    /**
     * Text that is shown when a user chooses an incorrect answer or taps on the lamp icon in a quiz-style poll, 0-200 characters with at most 2 line feeds after entities parsing
     */
    public val explanation: String? = null,
    /**
     * Mode for parsing entities in the explanation. See [formatting options](https://core.telegram.org/bots/api#formatting-options) for more details.
     */
    @SerialName("explanation_parse_mode")
    public val explanationParseMode: String? = null,
    /**
     * A JSON-serialized list of special entities that appear in the poll explanation. It can be specified instead of *explanation_parse_mode*
     */
    @SerialName("explanation_entities")
    public val explanationEntities: List<MessageEntity>? = null,
    /**
     * Amount of time in seconds the poll will be active after creation, 5-600. Can't be used together with *close_date*.
     */
    @SerialName("open_period")
    public val openPeriod: Long? = null,
    /**
     * Point in time (Unix timestamp) when the poll will be automatically closed. Must be at least 5 and no more than 600 seconds in the future. Can't be used together with *open_period*.
     */
    @SerialName("close_date")
    public val closeDate: Long? = null,
    /**
     * Pass *True* if the poll needs to be immediately closed. This can be useful for poll preview.
     */
    @SerialName("is_closed")
    public val isClosed: Boolean? = null,
    /**
     * Sends the message [silently](https://telegram.org/blog/channels-2-0#silent-messages). Users will receive a notification with no sound.
     */
    @SerialName("disable_notification")
    public val disableNotification: Boolean? = null,
    /**
     * Protects the contents of the sent message from forwarding and saving
     */
    @SerialName("protect_content")
    public val protectContent: Boolean? = null,
    /**
     * Pass *True* to allow up to 1000 messages per second, ignoring [broadcasting limits](https://core.telegram.org/bots/faq#how-can-i-message-all-of-my-bot-39s-subscribers-at-once) for a fee of 0.1 Telegram Stars per message. The relevant Stars will be withdrawn from the bot's balance
     */
    @SerialName("allow_paid_broadcast")
    public val allowPaidBroadcast: Boolean? = null,
    /**
     * Unique identifier of the message effect to be added to the message; for private chats only
     */
    @SerialName("message_effect_id")
    public val messageEffectId: String? = null,
    /**
     * Description of the message to reply to
     */
    @SerialName("reply_parameters")
    public val replyParameters: ReplyParameters? = null,
    /**
     * Additional interface options. A JSON-serialized object for an [inline keyboard](https://core.telegram.org/bots/features#inline-keyboards), [custom reply keyboard](https://core.telegram.org/bots/features#keyboards), instructions to remove a reply keyboard or to force a reply from the user
     */
    @SerialName("reply_markup")
    public val replyMarkup: ReplyMarkup? = null,
)
