// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class EditStoryRequest(
    /**
     * Unique identifier of the business connection
     */
    @SerialName("business_connection_id")
    public val businessConnectionId: String,
    /**
     * Unique identifier of the story to edit
     */
    @SerialName("story_id")
    public val storyId: Long,
    /**
     * Content of the story
     */
    public val content: InputStoryContent,
    /**
     * Caption of the story, 0-2048 characters after entities parsing
     */
    public val caption: String? = null,
    /**
     * Mode for parsing entities in the story caption. See [formatting options](https://core.telegram.org/bots/api#formatting-options) for more details.
     */
    @SerialName("parse_mode")
    public val parseMode: String? = null,
    /**
     * A JSON-serialized list of special entities that appear in the caption, which can be specified instead of *parse_mode*
     */
    @SerialName("caption_entities")
    public val captionEntities: List<MessageEntity>? = null,
    /**
     * A JSON-serialized list of clickable areas to be shown on the story
     */
    public val areas: List<StoryArea>? = null,
)
