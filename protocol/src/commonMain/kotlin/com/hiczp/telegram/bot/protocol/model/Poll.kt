// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.model

import com.hiczp.telegram.bot.protocol.`annotation`.IncomingUpdate
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
     * *True*, if the poll allows to change the chosen answer options
     */
    @SerialName("allows_revoting")
    public val allowsRevoting: Boolean,
    /**
     * *True* if voting is limited to users who have been members of the chat where the poll was originally sent for more than 24 hours
     */
    @SerialName("members_only")
    public val membersOnly: Boolean,
    /**
     * *Optional*. A list of two-letter [ISO 3166-1 alpha-2](https://en.wikipedia.org/wiki/ISO_3166-1_alpha-2) country codes indicating the countries from which users can vote in the poll. The country code “FT” is used for users with anonymous numbers. If omitted, then users from any country can participate in the poll.
     */
    @SerialName("country_codes")
    public val countryCodes: List<String>? = null,
    /**
     * *Optional*. Array of 0-based identifiers of the correct answer options. Available only for polls in quiz mode which are closed or were sent (not forwarded) by the bot or to the private chat with the bot.
     */
    @SerialName("correct_option_ids")
    public val correctOptionIds: List<Long>? = null,
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
     * *Optional*. Media added to the quiz explanation
     */
    @SerialName("explanation_media")
    public val explanationMedia: PollMedia? = null,
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
    /**
     * *Optional*. Description of the poll; for polls inside the [Message](https://core.telegram.org/bots/api#message) object only
     */
    public val description: String? = null,
    /**
     * *Optional*. Special entities like usernames, URLs, bot commands, etc. that appear in the description
     */
    @SerialName("description_entities")
    public val descriptionEntities: List<MessageEntity>? = null,
    /**
     * *Optional*. Media added to the poll description; for polls inside the [Message](https://core.telegram.org/bots/api#message) object only
     */
    public val media: PollMedia? = null,
) : IncomingUpdate
