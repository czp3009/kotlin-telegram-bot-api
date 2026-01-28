// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * This object represents an inline keyboard that appears right next to the message it belongs to.
 */
@Serializable
public data class InlineKeyboardMarkup(
    /**
     * Array of button rows, each represented by an Array of [InlineKeyboardButton](https://core.telegram.org/bots/api#inlinekeyboardbutton) objects
     */
    @SerialName("inline_keyboard")
    public val inlineKeyboard: List<List<InlineKeyboardButton>>,
) : ReplyMarkup
