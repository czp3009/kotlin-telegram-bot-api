// Auto-generated from Swagger specificationDo not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Contains the list of gifts received and owned by a user or a chat.
 */
@Serializable
public data class OwnedGifts(
    /**
     * The total number of gifts owned by the user or the chat
     */
    @SerialName("total_count")
    public val totalCount: Long,
    /**
     * The list of gifts
     */
    public val gifts: List<OwnedGift>,
    /**
     * *Optional*. Offset for the next request. If empty, then there are no more results
     */
    @SerialName("next_offset")
    public val nextOffset: String? = null,
)
