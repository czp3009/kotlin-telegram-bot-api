// Auto-generated from Swagger specificationDo not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * This object contains information about a poll.
 */
@Serializable
public data class Poll(
    /**
     * Unique poll identifier
     */
    public val id: String,
    /**
     * Poll question, 1-300 characters
     */
    public val question: String,
    /**
     * *Optional*. Special entities that appear in the *question*. Currently, only custom emoji entities are allowed in poll questions
     */
    @SerialName("question_entities")
    public val questionEntities: List<MessageEntity>? = null,
    /**
     * List of poll options
     */
    public val options: List<PollOption>,
    /**
     * Total number of users that voted in the poll
     */
    @SerialName("total_voter_count")
    public val totalVoterCount: Long,
    /**
     * *True*, if the poll is closed
     */
    @SerialName("is_closed")
    public val isClosed: Boolean,
    /**
     * *True*, if the poll is anonymous
     */
    @SerialName("is_anonymous")
    public val isAnonymous: Boolean,
    /**
     * Poll type, currently can be “regular” or “quiz”
     */
    public val type: String,
    /**
     * *True*, if the poll allows multiple answers
     */
    @SerialName("allows_multiple_answers")
    public val allowsMultipleAnswers: Boolean,
    /**
     * *Optional*. 0-based identifier of the correct answer option. Available only for polls in the quiz mode, which are closed, or was sent (not forwarded) by the bot or to the private chat with the bot.
     */
    @SerialName("correct_option_id")
    public val correctOptionId: Long? = null,
    /**
     * *Optional*. Text that is shown when a user chooses an incorrect answer or taps on the lamp icon in a quiz-style poll, 0-200 characters
     */
    public val explanation: String? = null,
    /**
     * *Optional*. Special entities like usernames, URLs, bot commands, etc. that appear in the *explanation*
     */
    @SerialName("explanation_entities")
    public val explanationEntities: List<MessageEntity>? = null,
    /**
     * *Optional*. Amount of time in seconds the poll will be active after creation
     */
    @SerialName("open_period")
    public val openPeriod: Long? = null,
    /**
     * *Optional*. Point in time (Unix timestamp) when the poll will be automatically closed
     */
    @SerialName("close_date")
    public val closeDate: Long? = null,
)
