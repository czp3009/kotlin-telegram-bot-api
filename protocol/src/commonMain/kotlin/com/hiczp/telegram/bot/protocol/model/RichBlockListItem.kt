// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.model

import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * An item of a list.
 */
@Serializable
public data class RichBlockListItem(
    /**
     * Label of the item
     */
    public val label: String,
    /**
     * The content of the item
     */
    public val blocks: List<RichBlock>,
    /**
     * *Optional*. *True*, if the item has a checkbox
     */
    @SerialName("has_checkbox")
    public val hasCheckbox: Boolean? = null,
    /**
     * *Optional*. *True*, if the item has a checked checkbox
     */
    @SerialName("is_checked")
    public val isChecked: Boolean? = null,
    /**
     * *Optional*. For ordered lists, the numeric value of the item label
     */
    public val `value`: Long? = null,
    /**
     * *Optional*. For ordered lists, the type of the item label; must be one of “a” for lowercase letters, “A” for uppercase letters, “i” for lowercase Roman numerals, “I” for uppercase Roman numerals, or “1” for decimal numbers
     */
    public val type: String? = null,
)
