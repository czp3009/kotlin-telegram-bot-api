// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlin.Boolean
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Describes the options used for link preview generation.
 */
@Serializable
public data class LinkPreviewOptions(
    /**
     * *Optional*. *True*, if the link preview is disabled
     */
    @SerialName("is_disabled")
    public val isDisabled: Boolean? = null,
    /**
     * *Optional*. URL to use for the link preview. If empty, then the first URL found in the message text will be used
     */
    public val url: String? = null,
    /**
     * *Optional*. *True*, if the media in the link preview is supposed to be shrunk; ignored if the URL isn't explicitly specified or media size change isn't supported for the preview
     */
    @SerialName("prefer_small_media")
    public val preferSmallMedia: Boolean? = null,
    /**
     * *Optional*. *True*, if the media in the link preview is supposed to be enlarged; ignored if the URL isn't explicitly specified or media size change isn't supported for the preview
     */
    @SerialName("prefer_large_media")
    public val preferLargeMedia: Boolean? = null,
    /**
     * *Optional*. *True*, if the link preview must be shown above the message text; otherwise, the link preview will be shown below the message text
     */
    @SerialName("show_above_text")
    public val showAboveText: Boolean? = null,
)
