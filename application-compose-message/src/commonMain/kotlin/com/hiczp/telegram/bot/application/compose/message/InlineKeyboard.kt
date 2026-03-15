package com.hiczp.telegram.bot.application.compose.message

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import androidx.compose.runtime.remember
import com.hiczp.telegram.bot.application.compose.message.applier.MessageApplier
import com.hiczp.telegram.bot.application.compose.message.node.ButtonNode
import com.hiczp.telegram.bot.application.compose.message.node.InlineKeyboardNode
import com.hiczp.telegram.bot.application.compose.message.node.InlineKeyboardRowNode

/**
 * Emits an inline keyboard into the composition.
 *
 * Inline keyboards appear directly below the message and support clickable buttons
 * with callback data for interactive workflows.
 *
 * Example:
 * ```kotlin
 * composeMessage {
 *     Text("Choose an option:")
 *     InlineKeyboard {
 *         Row {
 *             Button("Yes") { handleYes() }
 *             Button("No") { handleNo() }
 *         }
 *     }
 * }
 * ```
 *
 * @param content The row definitions for this keyboard.
 */
@Composable
fun InlineKeyboard(content: @Composable InlineKeyboardScope.() -> Unit) {
    ComposeNode<InlineKeyboardNode, MessageApplier>(
        factory = ::InlineKeyboardNode,
        update = {},
        content = { InlineKeyboardScope.content() }
    )
}

/**
 * Scope for defining inline keyboard rows.
 */
@InlineKeyboardDsl
object InlineKeyboardScope {
    /**
     * Adds a row of buttons to the keyboard.
     *
     * @param content The button definitions for this row.
     */
    @Composable
    fun Row(content: @Composable InlineKeyboardRowScope.() -> Unit) {
        ComposeNode<InlineKeyboardRowNode, MessageApplier>(
            factory = ::InlineKeyboardRowNode,
            update = {},
            content = { InlineKeyboardRowScope.content() }
        )
    }
}

/**
 * Scope for a single row of inline keyboard buttons.
 */
@InlineKeyboardDsl
object InlineKeyboardRowScope

/**
 * Adds a callback button with an onClick handler.
 *
 * This is the primary button type for interactive inline keyboards.
 * When clicked, the [onClick] callback is invoked.
 *
 * @param text The button label.
 * @param style Optional button style.
 * @param iconCustomEmojiId Optional custom emoji ID for the button icon.
 * @param onClick Callback invoked when the button is clicked.
 */
@Composable
fun InlineKeyboardRowScope.Button(
    text: String,
    style: String? = null,
    iconCustomEmojiId: String? = null,
    onClick: () -> Unit,
) {
    val session = LocalComposeMessageSession.current
    // Use remember to keep the same ID across recompositions
    val buttonId = remember { session.registerButtonCallback(onClick) }

    ComposeNode<ButtonNode, MessageApplier>(
        factory = { ButtonNode(buttonId, text).apply { hasOnClick = true } },
        update = {
            set(text) { this.text = it }
            set(style) { this.style = it }
            set(iconCustomEmojiId) { this.iconCustomEmojiId = it }
            // Update callback to capture the latest onClick closure
            set(onClick) { session.updateButtonCallback(buttonId, it) }
        }
    )
}

/**
 * Adds a URL button that opens a web page when clicked.
 *
 * URL buttons do not trigger callbacks; they simply open the URL in the user's browser.
 *
 * @param text The button label.
 * @param url The URL to open.
 * @param style Optional button style.
 * @param iconCustomEmojiId Optional custom emoji ID for the button icon.
 */
@Composable
fun InlineKeyboardRowScope.UrlButton(
    text: String,
    url: String,
    style: String? = null,
    iconCustomEmojiId: String? = null,
) {
    ComposeNode<ButtonNode, MessageApplier>(
        factory = { ButtonNode("", text) },
        update = {
            set(text) { this.text = it }
            set(url) { this.url = it }
            set(style) { this.style = it }
            set(iconCustomEmojiId) { this.iconCustomEmojiId = it }
        }
    )
}

/**
 * Adds a Web App button that opens a Telegram Web App.
 *
 * @param text The button label.
 * @param url The Web App URL.
 * @param style Optional button style.
 * @param iconCustomEmojiId Optional custom emoji ID for the button icon.
 */
@Composable
fun InlineKeyboardRowScope.WebAppButton(
    text: String,
    url: String,
    style: String? = null,
    iconCustomEmojiId: String? = null,
) {
    ComposeNode<ButtonNode, MessageApplier>(
        factory = { ButtonNode("", text) },
        update = {
            set(text) { this.text = it }
            set(url) { this.webAppUrl = it }
            set(style) { this.style = it }
            set(iconCustomEmojiId) { this.iconCustomEmojiId = it }
        }
    )
}

/**
 * Adds a button with custom callback data (without onClick handler).
 *
 * Use this when you want to handle callbacks manually via the event dispatcher
 * rather than through the Compose session's onClick system.
 *
 * @param text The button label.
 * @param callbackData Custom callback data string.
 * @param style Optional button style.
 * @param iconCustomEmojiId Optional custom emoji ID for the button icon.
 */
@Composable
fun InlineKeyboardRowScope.CallbackButton(
    text: String,
    callbackData: String,
    style: String? = null,
    iconCustomEmojiId: String? = null,
) {
    ComposeNode<ButtonNode, MessageApplier>(
        factory = { ButtonNode("", text) },
        update = {
            set(text) { this.text = it }
            set(callbackData) { this.callbackData = it }
            set(style) { this.style = it }
            set(iconCustomEmojiId) { this.iconCustomEmojiId = it }
        }
    )
}

/**
 * Adds a copy text button that copies text to the user's clipboard.
 *
 * When pressed, the button copies the specified text to the user's clipboard.
 *
 * @param text The button label.
 * @param copyText The text to copy when clicked.
 * @param style Optional button style.
 * @param iconCustomEmojiId Optional custom emoji ID for the button icon.
 */
@Composable
fun InlineKeyboardRowScope.CopyTextButton(
    text: String,
    copyText: String,
    style: String? = null,
    iconCustomEmojiId: String? = null,
) {
    ComposeNode<ButtonNode, MessageApplier>(
        factory = { ButtonNode("", text) },
        update = {
            set(text) { this.text = it }
            set(copyText) { this.copyText = it }
            set(style) { this.style = it }
            set(iconCustomEmojiId) { this.iconCustomEmojiId = it }
        }
    )
}

/**
 * DSL marker for inline keyboard scopes.
 */
@DslMarker
annotation class InlineKeyboardDsl
