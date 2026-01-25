// Auto-generated from Swagger specificationDo not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

/**
 * This object describes the type of a reaction. Currently, it can be one of
 * ReactionTypeEmoji ReactionTypeCustomEmoji ReactionTypePaid
 */
@Serializable
@JsonClassDiscriminator("type")
@OptIn(ExperimentalSerializationApi::class)
public sealed interface ReactionType

/**
 * The reaction is based on an emoji.
 */
@Serializable
@SerialName("emoji")
public data class ReactionTypeEmoji(
    /**
     * Reaction emoji. Currently, it can be one of "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""
     */
    public val emoji: String,
) : ReactionType

/**
 * The reaction is based on a custom emoji.
 */
@Serializable
@SerialName("custom_emoji")
public data class ReactionTypeCustomEmoji(
    /**
     * Custom emoji identifier
     */
    @SerialName("custom_emoji_id")
    public val customEmojiId: String,
) : ReactionType

/**
 * The reaction is paid.
 */
@Serializable
@SerialName("paid")
public class ReactionTypePaid : ReactionType
