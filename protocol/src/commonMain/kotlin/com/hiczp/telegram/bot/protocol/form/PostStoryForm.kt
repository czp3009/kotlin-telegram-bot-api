// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.form

import com.hiczp.telegram.bot.protocol.model.InputStoryContent
import com.hiczp.telegram.bot.protocol.model.MessageEntity
import com.hiczp.telegram.bot.protocol.model.StoryArea
import io.ktor.client.request.forms.ChannelProvider
import io.ktor.client.request.forms.FormPart
import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName

public data class PostStoryForm(
    /**
     * Unique identifier of the business connection
     */
    @SerialName("business_connection_id")
    public val businessConnectionId: String,
    /**
     * Content of the story
     */
    public val content: InputStoryContent,
    /**
     * Period after which the story is moved to the archive, in seconds; must be one of `6 * 3600`, `12 * 3600`, `86400`, or `2 * 86400`
     */
    @SerialName("active_period")
    public val activePeriod: Long,
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
    /**
     * Pass *True* to keep the story accessible after it expires
     */
    @SerialName("post_to_chat_page")
    public val postToChatPage: Boolean? = null,
    /**
     * Pass *True* if the content of the story must be protected from forwarding and screenshotting
     */
    @SerialName("protect_content")
    public val protectContent: Boolean? = null,
    /**
     * Additional file attachments referenced via attach://<file_attach_name> in media fields
     */
    public val attachments: List<FormPart<ChannelProvider>>? = null,
)
