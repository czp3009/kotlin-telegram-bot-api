// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.model

import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Cell in a table.
 */
@Serializable
public data class RichBlockTableCell(
    /**
     * *Optional*. Text in the cell. If omitted, then the cell is invisible.
     */
    public val text: RichText? = null,
    /**
     * *Optional*. *True*, if the cell is a header cell
     */
    @SerialName("is_header")
    public val isHeader: Boolean? = null,
    /**
     * *Optional*. The number of columns the cell spans if it is bigger than 1
     */
    public val colspan: Long? = null,
    /**
     * *Optional*. The number of rows the cell spans if it is bigger than 1
     */
    public val rowspan: Long? = null,
    /**
     * Horizontal cell content alignment. Currently, must be one of “left”, “center”, or “right”.
     */
    public val align: String,
    /**
     * Vertical cell content alignment. Currently, must be one of “top”, “middle”, or “bottom”.
     */
    public val valign: String,
)
