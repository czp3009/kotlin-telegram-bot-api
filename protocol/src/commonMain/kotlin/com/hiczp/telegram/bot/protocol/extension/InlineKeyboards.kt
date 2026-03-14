package com.hiczp.telegram.bot.protocol.extension

import com.hiczp.telegram.bot.protocol.constant.InlineKeyboardButtonStyle
import com.hiczp.telegram.bot.protocol.model.*

/**
 * DslMarker for inline keyboard builders.
 */
@DslMarker
annotation class InlineKeyboardDsl

/**
 * Builder for a single row of inline keyboard buttons.
 *
 * This class is NOT thread-safe. Do not share instances between threads or coroutines.
 * Create a new builder for each use.
 */
@InlineKeyboardDsl
class InlineKeyboardRowBuilder {
    private val buttons = mutableListOf<InlineKeyboardButton>()

    /**
     * Adds a callback button.
     * @param text Label text on the button
     * @param callbackData Data to be sent in a callback query to the bot when the button is pressed, 1-64 bytes
     * @param style Optional style of the button. Use [InlineKeyboardButtonStyle] constants.
     */
    fun callbackButton(text: String, callbackData: String, style: String? = null) {
        buttons += InlineKeyboardButton(text, style = style, callbackData = callbackData)
    }

    /**
     * Adds a URL button.
     * @param text Label text on the button
     * @param url HTTP or tg:// URL to be opened when the button is pressed
     * @param style Optional style of the button. Use [InlineKeyboardButtonStyle] constants.
     */
    fun urlButton(text: String, url: String, style: String? = null) {
        buttons += InlineKeyboardButton(text, style = style, url = url)
    }

    /**
     * Adds a Web App button.
     * @param text Label text on the button
     * @param url HTTPS URL of the Web App to be opened
     * @param style Optional style of the button. Use [InlineKeyboardButtonStyle] constants.
     */
    fun webAppButton(text: String, url: String, style: String? = null) {
        buttons += InlineKeyboardButton(text, style = style, webApp = WebAppInfo(url))
    }

    /**
     * Adds a login URL button.
     * @param text Label text on the button
     * @param loginUrl Description of the login URL
     * @param style Optional style of the button. Use [InlineKeyboardButtonStyle] constants.
     */
    fun loginButton(text: String, loginUrl: LoginUrl, style: String? = null) {
        buttons += InlineKeyboardButton(text, style = style, loginUrl = loginUrl)
    }

    /**
     * Adds a switch inline query button that prompts the user to select a chat.
     * @param text Label text on the button
     * @param query Inline query to be inserted, may be empty
     * @param style Optional style of the button. Use [InlineKeyboardButtonStyle] constants.
     */
    fun switchInlineButton(text: String, query: String = "", style: String? = null) {
        buttons += InlineKeyboardButton(text, style = style, switchInlineQuery = query)
    }

    /**
     * Adds a switch inline query button that inserts the query in the current chat.
     * @param text Label text on the button
     * @param query Inline query to be inserted, may be empty
     * @param style Optional style of the button. Use [InlineKeyboardButtonStyle] constants.
     */
    fun switchInlineCurrentChatButton(
        text: String,
        query: String = "",
        style: String? = null
    ) {
        buttons += InlineKeyboardButton(
            text,
            style = style,
            switchInlineQueryCurrentChat = query
        )
    }

    /**
     * Adds a switch inline query button that prompts the user to select a chat of the specified type.
     * @param text Label text on the button
     * @param chosenChat Description of the chat types to choose from
     * @param style Optional style of the button. Use [InlineKeyboardButtonStyle] constants.
     */
    fun switchInlineChosenChatButton(
        text: String,
        chosenChat: SwitchInlineQueryChosenChat,
        style: String? = null
    ) {
        buttons += InlineKeyboardButton(
            text,
            style = style,
            switchInlineQueryChosenChat = chosenChat
        )
    }

    /**
     * Adds a copy text button.
     * @param text Label text on the button
     * @param copyText Text to be copied to the clipboard when the button is pressed
     * @param style Optional style of the button. Use [InlineKeyboardButtonStyle] constants.
     */
    fun copyTextButton(text: String, copyText: String, style: String? = null) {
        buttons += InlineKeyboardButton(text, style = style, copyText = CopyTextButton(copyText))
    }

    /**
     * Adds a pay button. Must always be the first button in the first row.
     * @param text Label text on the button
     */
    fun payButton(text: String = "Pay") {
        buttons += InlineKeyboardButton(text, pay = true)
    }

    /**
     * Adds a generic button with full control over all parameters.
     */
    fun button(button: InlineKeyboardButton) {
        buttons += button
    }

    fun build(): List<InlineKeyboardButton> = buttons.toList()
}

/**
 * Builder for inline keyboard markup.
 */
@InlineKeyboardDsl
class InlineKeyboardBuilder {
    private val rows = mutableListOf<List<InlineKeyboardButton>>()

    /**
     * Adds a row of buttons.
     */
    fun row(block: InlineKeyboardRowBuilder.() -> Unit) {
        rows += InlineKeyboardRowBuilder().apply(block).build()
    }

    /**
     * Adds a single callback button in its own row.
     */
    fun callbackButton(text: String, callbackData: String, style: String? = null) {
        row { callbackButton(text, callbackData, style) }
    }

    /**
     * Adds a single URL button in its own row.
     */
    fun urlButton(text: String, url: String, style: String? = null) {
        row { urlButton(text, url, style) }
    }

    /**
     * Adds a single Web App button in its own row.
     */
    fun webAppButton(text: String, url: String, style: String? = null) {
        row { webAppButton(text, url, style) }
    }

    /**
     * Adds a single login button in its own row.
     */
    fun loginButton(text: String, loginUrl: LoginUrl, style: String? = null) {
        row { loginButton(text, loginUrl, style) }
    }

    /**
     * Adds a single switch inline query button in its own row.
     */
    fun switchInlineButton(text: String, query: String = "", style: String? = null) {
        row { switchInlineButton(text, query, style) }
    }

    /**
     * Adds a single switch inline query current chat button in its own row.
     */
    fun switchInlineCurrentChatButton(text: String, query: String = "", style: String? = null) {
        row { switchInlineCurrentChatButton(text, query, style) }
    }

    /**
     * Adds a single switch inline query chosen chat button in its own row.
     */
    fun switchInlineChosenChatButton(
        text: String,
        chosenChat: SwitchInlineQueryChosenChat,
        style: String? = null
    ) {
        row { switchInlineChosenChatButton(text, chosenChat, style) }
    }

    /**
     * Adds a single copy text button in its own row.
     */
    fun copyTextButton(text: String, copyText: String, style: String? = null) {
        row { copyTextButton(text, copyText, style) }
    }

    /**
     * Adds a single pay button in its own row.
     */
    fun payButton(text: String = "Pay") {
        row { payButton(text) }
    }

    /**
     * Adds a row with multiple callback buttons from pairs of (text, callbackData).
     */
    fun callbackRow(vararg buttons: Pair<String, String>) {
        row {
            buttons.forEach { (text, data) -> callbackButton(text, data) }
        }
    }

    /**
     * Adds a row with multiple callback buttons with the same style.
     */
    fun callbackRow(style: String?, vararg buttons: Pair<String, String>) {
        row {
            buttons.forEach { (text, data) -> callbackButton(text, data, style) }
        }
    }

    fun build(): InlineKeyboardMarkup = InlineKeyboardMarkup(rows.toList())
}

/**
 * Builds an [InlineKeyboardMarkup] using a DSL.
 *
 * Example:
 * ```kotlin
 * val keyboard = inlineKeyboard {
 *     row {
 *         callbackButton("Confirm", "confirm")
 *         callbackButton("Cancel", "cancel", InlineKeyboardButtonStyle.DANGER)
 *     }
 *     callbackRow("Yes" to "yes", "No" to "no")
 *     urlButton("Help", "https://example.com/help")
 * }
 * ```
 */
fun inlineKeyboard(block: InlineKeyboardBuilder.() -> Unit): InlineKeyboardMarkup =
    InlineKeyboardBuilder().apply(block).build()

/**
 * Builds an [InlineKeyboardMarkup] from a list of button rows.
 * Each row is a list of button pairs (text to callbackData).
 */
fun inlineKeyboardOf(vararg rows: List<Pair<String, String>>): InlineKeyboardMarkup =
    inlineKeyboard {
        rows.forEach { row -> callbackRow(*row.toTypedArray()) }
    }
