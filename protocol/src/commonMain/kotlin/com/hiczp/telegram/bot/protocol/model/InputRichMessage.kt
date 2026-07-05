// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.model

import kotlin.Boolean
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Describes a rich message to be sent. Exactly one of the fields html or markdown must be used.
 */
@Serializable
public data class InputRichMessage(
    /**
     * *Optional*. Content of the rich message to send described using HTML formatting. See [rich message formatting options](https://core.telegram.org/bots/api#rich-message-formatting-options) for more details.
     */
    public val html: String? = null,
    /**
     * *Optional*. Content of the rich message to send described using Markdown formatting. See [rich message formatting options](https://core.telegram.org/bots/api#rich-message-formatting-options) for more details.
     */
    public val markdown: String? = null,
    /**
     * *Optional*. Pass *True* if the rich message must be shown right-to-left
     */
    @SerialName("is_rtl")
    public val isRtl: Boolean? = null,
    /**
     * *Optional*. Pass *True* to skip automatic detection of entities (e.g., URLs, email addresses, username mentions, hashtags, cashtags, bot commands, or phone numbers) in the text
     */
    @SerialName("skip_entity_detection")
    public val skipEntityDetection: Boolean? = null,
)
