// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.model

import kotlin.Boolean
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Rich formatted message.
 */
@Serializable
public data class RichMessage(
    /**
     * Content of the message
     */
    public val blocks: List<RichBlock>,
    /**
     * *Optional*. *True*, if the rich message must be shown right-to-left
     */
    @SerialName("is_rtl")
    public val isRtl: Boolean? = null,
)
