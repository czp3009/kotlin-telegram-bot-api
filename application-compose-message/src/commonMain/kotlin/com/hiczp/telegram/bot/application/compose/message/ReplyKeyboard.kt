package com.hiczp.telegram.bot.application.compose.message

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import androidx.compose.runtime.currentComposer
import com.hiczp.telegram.bot.application.compose.message.applier.MessageApplier
import com.hiczp.telegram.bot.application.compose.message.node.*
import com.hiczp.telegram.bot.protocol.model.*

/**
 * Emits a reply keyboard into the composition.
 *
 * Reply keyboards replace the user's keyboard and are typically used for
 * simple option selection. Unlike inline keyboards, reply keyboards are
 * not attached to specific messages.
 *
 * Example:
 * ```kotlin
 * composeMessage {
 *     Text("Choose an option:")
 *     ReplyKeyboard(resizeKeyboard = true) {
 *         Row {
 *             Button("Option 1")
 *             Button("Option 2")
 *         }
 *     }
 * }
 * ```
 *
 * @param resizeKeyboard Whether to resize the keyboard.
 * @param oneTimeKeyboard Whether the keyboard should hide after use.
 * @param isPersistent Whether the keyboard should persist.
 * @param inputFieldPlaceholder Placeholder text for the input field.
 * @param selective Whether to show for specific users only.
 * @param content The row definitions for this keyboard.
 */
@Composable
fun ReplyKeyboard(
    resizeKeyboard: Boolean? = null,
    oneTimeKeyboard: Boolean? = null,
    isPersistent: Boolean? = null,
    inputFieldPlaceholder: String? = null,
    selective: Boolean? = null,
    content: @Composable ReplyKeyboardScope.() -> Unit,
) {
    ComposeNode<ReplyKeyboardNode, MessageApplier>(
        factory = ::ReplyKeyboardNode,
        update = {
            set(isPersistent) { this.isPersistent = it }
            set(resizeKeyboard) { this.resizeKeyboard = it }
            set(oneTimeKeyboard) { this.oneTimeKeyboard = it }
            set(inputFieldPlaceholder) { this.inputFieldPlaceholder = it }
            set(selective) { this.selective = it }
        },
        content = { ReplyKeyboardScope.content() }
    )
}

/**
 * Emits a ForceReply markup into the composition.
 *
 * This forces the user to reply to the message.
 *
 * @param inputFieldPlaceholder Optional placeholder text for the input field.
 * @param selective Whether to force reply for specific users only.
 */
@Composable
fun ForceReply(
    inputFieldPlaceholder: String? = null,
    selective: Boolean? = null,
) {
    val applier = currentComposer.applier as? MessageApplier ?: return
    val root = applier.root as? RootMessageNode ?: return
    root.keyboard = ForceReplyNode().apply {
        this.inputFieldPlaceholder = inputFieldPlaceholder
        this.selective = selective
    }
}

/**
 * Emits a ReplyKeyboardRemove markup into the composition.
 *
 * This removes the current reply keyboard.
 *
 * @param selective Whether to remove for specific users only.
 */
@Composable
fun RemoveKeyboard(selective: Boolean? = null) {
    val applier = currentComposer.applier as? MessageApplier ?: return
    val root = applier.root as? RootMessageNode ?: return
    root.keyboard = ReplyKeyboardRemoveNode().apply { this.selective = selective }
}

/**
 * Scope for defining reply keyboard rows.
 */
@ReplyKeyboardDsl
object ReplyKeyboardScope {
    /**
     * Adds a row of buttons to the keyboard.
     *
     * @param content The button definitions for this row.
     */
    @Composable
    fun Row(content: @Composable ReplyKeyboardRowScope.() -> Unit) {
        ComposeNode<ReplyKeyboardRowNode, MessageApplier>(
            factory = ::ReplyKeyboardRowNode,
            update = {},
            content = { ReplyKeyboardRowScope.content() }
        )
    }
}

/**
 * Scope for a single row of reply keyboard buttons.
 */
@ReplyKeyboardDsl
object ReplyKeyboardRowScope {
    /**
     * Accesses the current row node being composed.
     */
    internal val currentRow: ReplyKeyboardRowNode
        @Composable
        get() = currentComposer.applier.current as ReplyKeyboardRowNode
}

/**
 * Internal helper to add a KeyboardButton to the current row.
 */
@Composable
private fun ReplyKeyboardRowScope.AddButton(button: KeyboardButton) {
    currentRow.buttons.add(button)
}

/**
 * Adds a simple text button.
 *
 * When clicked, the button sends the text as a message.
 *
 * @param text The button label (also sent as the message text).
 * @param style Optional button style.
 * @param iconCustomEmojiId Optional custom emoji ID for the button icon.
 */
@Composable
fun ReplyKeyboardRowScope.Button(
    text: String,
    style: String? = null,
    iconCustomEmojiId: String? = null,
) {
    AddButton(
        KeyboardButton(
            text = text,
            style = style,
            iconCustomEmojiId = iconCustomEmojiId,
        )
    )
}

/**
 * Adds a button that requests the user's contact.
 *
 * @param text The button label.
 * @param style Optional button style.
 * @param iconCustomEmojiId Optional custom emoji ID for the button icon.
 */
@Composable
fun ReplyKeyboardRowScope.ContactButton(
    text: String,
    style: String? = null,
    iconCustomEmojiId: String? = null,
) {
    AddButton(
        KeyboardButton(
            text = text,
            requestContact = true,
            style = style,
            iconCustomEmojiId = iconCustomEmojiId,
        )
    )
}

/**
 * Adds a button that requests the user's location.
 *
 * @param text The button label.
 * @param style Optional button style.
 * @param iconCustomEmojiId Optional custom emoji ID for the button icon.
 */
@Composable
fun ReplyKeyboardRowScope.LocationButton(
    text: String,
    style: String? = null,
    iconCustomEmojiId: String? = null,
) {
    AddButton(
        KeyboardButton(
            text = text,
            requestLocation = true,
            style = style,
            iconCustomEmojiId = iconCustomEmojiId,
        )
    )
}

/**
 * Adds a button that requests a poll.
 *
 * @param text The button label.
 * @param pollType Optional poll type ("quiz" or "regular").
 * @param style Optional button style.
 * @param iconCustomEmojiId Optional custom emoji ID for the button icon.
 */
@Composable
fun ReplyKeyboardRowScope.PollButton(
    text: String,
    pollType: String? = null,
    style: String? = null,
    iconCustomEmojiId: String? = null,
) {
    AddButton(
        KeyboardButton(
            text = text,
            requestPoll = KeyboardButtonPollType(pollType),
            style = style,
            iconCustomEmojiId = iconCustomEmojiId,
        )
    )
}

/**
 * Adds a button that requests users.
 *
 * @param text The button label.
 * @param requestUsers The request users configuration.
 * @param style Optional button style.
 * @param iconCustomEmojiId Optional custom emoji ID for the button icon.
 */
@Composable
fun ReplyKeyboardRowScope.RequestUsersButton(
    text: String,
    requestUsers: KeyboardButtonRequestUsers,
    style: String? = null,
    iconCustomEmojiId: String? = null,
) {
    AddButton(
        KeyboardButton(
            text = text,
            requestUsers = requestUsers,
            style = style,
            iconCustomEmojiId = iconCustomEmojiId,
        )
    )
}

/**
 * Adds a button that requests a chat.
 *
 * @param text The button label.
 * @param requestChat The request chat configuration.
 * @param style Optional button style.
 * @param iconCustomEmojiId Optional custom emoji ID for the button icon.
 */
@Composable
fun ReplyKeyboardRowScope.RequestChatButton(
    text: String,
    requestChat: KeyboardButtonRequestChat,
    style: String? = null,
    iconCustomEmojiId: String? = null,
) {
    AddButton(
        KeyboardButton(
            text = text,
            requestChat = requestChat,
            style = style,
            iconCustomEmojiId = iconCustomEmojiId,
        )
    )
}

/**
 * Adds a Web App button.
 *
 * @param text The button label.
 * @param webApp The Web App info.
 * @param style Optional button style.
 * @param iconCustomEmojiId Optional custom emoji ID for the button icon.
 */
@Composable
fun ReplyKeyboardRowScope.WebAppButton(
    text: String,
    webApp: WebAppInfo,
    style: String? = null,
    iconCustomEmojiId: String? = null,
) {
    AddButton(
        KeyboardButton(
            text = text,
            webApp = webApp,
            style = style,
            iconCustomEmojiId = iconCustomEmojiId,
        )
    )
}

/**
 * Adds a Web App button with URL.
 *
 * @param text The button label.
 * @param url The Web App URL.
 * @param style Optional button style.
 * @param iconCustomEmojiId Optional custom emoji ID for the button icon.
 */
@Composable
fun ReplyKeyboardRowScope.WebAppButton(
    text: String,
    url: String,
    style: String? = null,
    iconCustomEmojiId: String? = null,
) {
    WebAppButton(text, WebAppInfo(url), style, iconCustomEmojiId)
}

/**
 * DSL marker for reply keyboard scopes.
 */
@DslMarker
annotation class ReplyKeyboardDsl
