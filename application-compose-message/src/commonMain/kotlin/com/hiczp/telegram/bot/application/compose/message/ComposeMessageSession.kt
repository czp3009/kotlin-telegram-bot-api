package com.hiczp.telegram.bot.application.compose.message

import androidx.compose.runtime.*
import com.hiczp.telegram.bot.application.compose.message.applier.MessageApplier
import com.hiczp.telegram.bot.application.compose.message.node.RootMessageNode
import com.hiczp.telegram.bot.protocol.TelegramBotApi
import com.hiczp.telegram.bot.protocol.model.InlineKeyboardMarkup
import com.hiczp.telegram.bot.protocol.model.editMessageText
import com.hiczp.telegram.bot.protocol.model.sendMessage
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.atomicfu.atomic
import kotlinx.atomicfu.locks.SynchronizedObject
import kotlinx.atomicfu.locks.synchronized
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield
import kotlin.time.Clock

private val logger = KotlinLogging.logger("ComposeMessageSession")

/**
 * Unique identifier for a compose message session.
 *
 * Used to route callback query events to the correct session.
 *
 * @property chatId The chat ID where the message is sent.
 * @property threadId The thread/topic ID within the chat, or null for main chat.
 */
data class ComposeMessageId(
    val chatId: Long,
    val threadId: Long? = null,
)

/**
 * Manages an active Compose message session.
 *
 * This class handles:
 * - Running the Compose composition with an event-driven clock
 * - Binding the messageId after the initial message is sent
 * - Updating the message when composition changes
 * - Routing callback query events to button onClick handlers
 *
 * @property id The identifier for this session.
 * @property client The Telegram Bot API client.
 * @property scope The coroutine scope for launching async operations.
 */
class ComposeMessageSession(
    val id: ComposeMessageId,
    private val client: TelegramBotApi,
    private val scope: CoroutineScope,
) : SynchronizedObject() {
    /**
     * The message ID bound after the initial message is sent.
     * Null until the first composition commits to Telegram.
     */
    var boundMessageId: Long? = null
        private set

    private val rootNode = RootMessageNode()

    /**
     * Map of button internal IDs to their onClick callbacks.
     */
    private val actionMap = mutableMapOf<String, () -> Unit>()

    /**
     * Counter for generating unique button IDs.
     */
    private val buttonIdCounter = atomic(0)

    /**
     * Flag indicating if the session has been disposed.
     */
    private val disposed = atomic(false)

    /**
     * The current composition, or null if not started.
     */
    private var composition: Composition? = null

    /**
     * The recomposer driving composition updates.
     */
    private var recomposer: Recomposer? = null

    /**
     * Job tracking the recomposer's runRecomposeAndApplyChanges loop.
     */
    private var recomposerJob: Job? = null

    /**
     * Allocates a unique button ID and registers its onClick callback.
     *
     * This combines ID allocation with callback registration in a single call,
     * eliminating the need for separate SideEffect calls.
     *
     * @param onClick The callback to invoke when the button is clicked.
     * @return A unique ID string to use as callback_data.
     */
    fun registerButtonCallback(onClick: () -> Unit): String {
        val id = buttonIdCounter.getAndIncrement().toString()
        synchronized(this) {
            actionMap[id] = onClick
        }
        return id
    }

    /**
     * Updates an existing button's onClick callback.
     *
     * @param id The button's internal ID (previously allocated).
     * @param onClick The new callback.
     */
    fun updateButtonCallback(id: String, onClick: () -> Unit) {
        synchronized(this) {
            actionMap[id] = onClick
        }
    }

    /**
     * Event-driven monotonic frame clock for Compose.
     *
     * Uses [yield] to surrender control, enabling batched state updates
     * and event-driven recomposition instead of continuous 60fps rendering.
     */
    private val frameClock = object : MonotonicFrameClock {
        override suspend fun <R> withFrameNanos(onFrame: (frameTimeNanos: Long) -> R): R {
            yield()
            // Get nanosecond precision by combining epochSeconds and nanosecondsOfSecond
            val now = Clock.System.now()
            return onFrame(now.epochSeconds * 1_000_000_000L + now.nanosecondsOfSecond)
        }
    }

    /**
     * Starts the compose session with the given content.
     *
     * @param content The composable content defining the message UI.
     */
    fun start(content: @Composable ComposeMessageScope.() -> Unit) {
        if (composition != null) {
            error("Session already started")
        }

        val recomposer = Recomposer(scope.coroutineContext + frameClock)
        this.recomposer = recomposer

        recomposerJob = scope.launch {
            recomposer.runRecomposeAndApplyChanges()
        }

        val applier = MessageApplier(rootNode) { steadyRoot ->
            commitToTelegram(steadyRoot)
        }

        composition = Composition(applier, recomposer).apply {
            setContent {
                CompositionLocalProvider(LocalComposeMessageSession provides this@ComposeMessageSession) {
                    ComposeMessageScope.content()
                }
            }
        }
    }

    /**
     * Commits the current node tree state to Telegram.
     *
     * - If [boundMessageId] is null, sends a new message and binds the ID.
     * - Otherwise, edits the existing message.
     */
    private fun commitToTelegram(node: RootMessageNode) {
        scope.launch {
            try {
                val text = node.text
                val markup = node.toReplyMarkup()

                if (boundMessageId == null) {
                    // First send - create new message
                    val response = client.sendMessage(
                        chatId = id.chatId.toString(),
                        messageThreadId = id.threadId,
                        text = text,
                        parseMode = node.parseMode,
                        replyMarkup = markup,
                    ).getOrThrow()

                    boundMessageId = response.messageId
                    // Register to global routing table
                    ComposeMessageRegistry.register(id, boundMessageId!!, this@ComposeMessageSession)
                    logger.debug { "ComposeMessage session started: ${id.chatId}/${boundMessageId}" }
                } else {
                    // Subsequent updates - edit existing message
                    // Note: editMessageText only supports InlineKeyboardMarkup
                    val inlineMarkup = markup as? InlineKeyboardMarkup
                    client.editMessageText(
                        chatId = id.chatId.toString(),
                        messageId = boundMessageId!!,
                        text = text,
                        parseMode = node.parseMode,
                        replyMarkup = inlineMarkup,
                    ).getOrThrow()
                }
            } catch (e: Exception) {
                logger.error(e) { "Failed to commit ComposeMessage to Telegram" }
            }
        }
    }

    /**
     * Handles a callback query event routed from [ComposeMessageRegistry].
     *
     * @param callbackData The callback_data from the clicked button.
     */
    fun handleCallback(callbackData: String) {
        val onClick: (() -> Unit)? = synchronized(this) {
            actionMap[callbackData]
        }
        onClick?.invoke()
    }

    /**
     * Disposes this session, cleaning up resources.
     *
     * Also removes the session from the global registry.
     */
    fun dispose() {
        if (disposed.getAndSet(true)) {
            // Already disposed
            return
        }
        boundMessageId?.let { msgId ->
            ComposeMessageRegistry.unregister(id, msgId)
        }
        composition?.dispose()
        recomposerJob?.cancel()
        recomposer?.cancel()
        synchronized(this) {
            actionMap.clear()
        }
        logger.debug { "ComposeMessage session disposed: ${id.chatId}/${boundMessageId}" }
    }

    /**
     * Checks if this session has been disposed.
     */
    fun isDisposed(): Boolean = disposed.value
}

/**
 * CompositionLocal providing access to the current [ComposeMessageSession].
 */
val LocalComposeMessageSession = staticCompositionLocalOf<ComposeMessageSession> {
    error("No ComposeMessageSession provided")
}

/**
 * Scope for composing a Telegram message with text and keyboard.
 */
@ComposeMessageDsl
object ComposeMessageScope {
    /**
     * Sets the text content of the message.
     *
     * @param text The message text.
     * @param parseMode Optional parse mode (e.g., "MarkdownV2", "HTML").
     */
    @Composable
    fun Text(text: String, parseMode: String? = null) {
        val root = (currentComposer.applier as? MessageApplier)?.root as? RootMessageNode ?: return
        root.text = text
        root.parseMode = parseMode
    }

    /**
     * Defines a message with text and optional keyboard content.
     *
     * This is the primary entry point for defining a compose message,
     * following the original design pattern. The [content] lambda can
     * include keyboard definitions.
     *
     * Example:
     * ```kotlin
     * composeMessage {
     *     var count by remember { mutableStateOf(0) }
     *     Message("Count: $count") {
     *         InlineKeyboard {
     *             Row {
     *                 Button("-") { count-- }
     *                 Button("+") { count++ }
     *             }
     *         }
     *     }
     * }
     * ```
     *
     * @param text The message text.
     * @param parseMode Optional parse mode (e.g., "MarkdownV2", "HTML").
     * @param content Additional content such as keyboards.
     */
    @Composable
    fun Message(text: String, parseMode: String? = null, content: @Composable () -> Unit = {}) {
        val root = (currentComposer.applier as? MessageApplier)?.root as? RootMessageNode ?: return
        root.text = text
        root.parseMode = parseMode
        content()
    }
}

/**
 * DSL marker for compose message scopes.
 */
@DslMarker
annotation class ComposeMessageDsl
