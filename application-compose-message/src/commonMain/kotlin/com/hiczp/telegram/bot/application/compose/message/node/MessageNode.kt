package com.hiczp.telegram.bot.application.compose.message.node

import com.hiczp.telegram.bot.protocol.model.*

/**
 * Base class for all compose message nodes.
 *
 * Each node type manages its own children list structure and can be rendered
 * to Telegram API structures.
 */
sealed class MessageNode {
    var parent: MessageNode? = null
}

/**
 * Root node representing a complete Telegram message with text and optional keyboard.
 */
class RootMessageNode : MessageNode() {
    var text: String = ""
    var parseMode: String? = null
    var keyboard: KeyboardNode? = null

    /**
     * Generates the ReplyMarkup for this message.
     */
    fun toReplyMarkup(): ReplyMarkup? = keyboard?.toReplyMarkup()
}

/**
 * Base class for keyboard nodes.
 */
sealed class KeyboardNode : MessageNode() {
    abstract fun toReplyMarkup(): ReplyMarkup
}

/**
 * Node representing an inline keyboard with rows of buttons.
 * Each button can have an onClick callback that gets triggered when the user clicks it.
 */
class InlineKeyboardNode : KeyboardNode() {
    val rows: MutableList<InlineKeyboardRowNode> = mutableListOf()

    override fun toReplyMarkup(): ReplyMarkup = InlineKeyboardMarkup(
        inlineKeyboard = rows.map { row -> row.buttons.map { it.toInlineKeyboardButton() } }
    )
}

/**
 * Node representing a single row of inline keyboard buttons.
 */
class InlineKeyboardRowNode : MessageNode() {
    val buttons: MutableList<ButtonNode> = mutableListOf()
}

/**
 * Node representing an inline keyboard button with optional onClick callback.
 *
 * @property internalId A unique identifier used as callback_data for routing click events.
 * @property text The button label text.
 */
class ButtonNode(
    var internalId: String,
    var text: String,
) : MessageNode() {
    /**
     * Additional button properties for non-callback button types.
     */
    var url: String? = null
    var webAppUrl: String? = null
    var callbackData: String? = null // Custom callback data (overrides internalId if set)
    var copyText: String? = null // Text to copy to clipboard
    var style: String? = null
    var iconCustomEmojiId: String? = null

    /**
     * Flag to indicate if this button has an onClick callback.
     * Used to determine if callback_data should be included in the button.
     */
    var hasOnClick: Boolean = false

    /**
     * Converts this node to a Telegram InlineKeyboardButton.
     */
    fun toInlineKeyboardButton(): InlineKeyboardButton = InlineKeyboardButton(
        text = text,
        callbackData = callbackData ?: internalId.takeIf { hasOnClick },
        url = url,
        webApp = webAppUrl?.let { com.hiczp.telegram.bot.protocol.model.WebAppInfo(it) },
        copyText = copyText?.let { CopyTextButton(it) },
        style = style,
        iconCustomEmojiId = iconCustomEmojiId,
    )
}

/**
 * Node representing a reply keyboard with rows of buttons.
 */
class ReplyKeyboardNode : KeyboardNode() {
    var isPersistent: Boolean? = null
    var resizeKeyboard: Boolean? = null
    var oneTimeKeyboard: Boolean? = null
    var inputFieldPlaceholder: String? = null
    var selective: Boolean? = null

    val rows: MutableList<ReplyKeyboardRowNode> = mutableListOf()

    override fun toReplyMarkup(): ReplyMarkup = ReplyKeyboardMarkup(
        keyboard = rows.map { row -> row.buttons.toList() },
        isPersistent = isPersistent,
        resizeKeyboard = resizeKeyboard,
        oneTimeKeyboard = oneTimeKeyboard,
        inputFieldPlaceholder = inputFieldPlaceholder,
        selective = selective,
    )
}

/**
 * Node representing a single row of reply keyboard buttons.
 */
class ReplyKeyboardRowNode : MessageNode() {
    val buttons: MutableList<KeyboardButton> = mutableListOf()
}

/**
 * Node representing a force reply markup.
 */
class ForceReplyNode : KeyboardNode() {
    var inputFieldPlaceholder: String? = null
    var selective: Boolean? = null

    override fun toReplyMarkup(): ReplyMarkup = ForceReply(
        forceReply = true,
        inputFieldPlaceholder = inputFieldPlaceholder,
        selective = selective,
    )
}

/**
 * Node representing a reply keyboard remove markup.
 */
class ReplyKeyboardRemoveNode : KeyboardNode() {
    var selective: Boolean? = null

    override fun toReplyMarkup(): ReplyMarkup = ReplyKeyboardRemove(
        removeKeyboard = true,
        selective = selective,
    )
}
