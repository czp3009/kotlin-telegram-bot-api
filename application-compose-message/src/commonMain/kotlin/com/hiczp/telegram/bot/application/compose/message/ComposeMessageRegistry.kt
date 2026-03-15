package com.hiczp.telegram.bot.application.compose.message

import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.util.collections.*

private val logger = KotlinLogging.logger("ComposeMessageRegistry")

/**
 * Global registry for active [ComposeMessageSession] instances.
 *
 * This object provides routing from Telegram callback query events to the
 * appropriate session's onClick handlers.
 *
 * Sessions are keyed by a composite key of `chatId_messageId` to ensure
 * unique routing even when multiple sessions exist in the same chat.
 */
object ComposeMessageRegistry {
    private val sessions: ConcurrentMap<String, ComposeMessageSession> = ConcurrentMap()

    /**
     * Generates the registry key for a session.
     */
    private fun key(chatId: Long, messageId: Long): String = "${chatId}_$messageId"

    /**
     * Registers a session in the registry.
     *
     * @param id The compose message ID containing chatId and threadId.
     * @param messageId The Telegram message ID.
     * @param session The session to register.
     */
    fun register(id: ComposeMessageId, messageId: Long, session: ComposeMessageSession) {
        val sessionKey = key(id.chatId, messageId)
        sessions[sessionKey] = session
        logger.trace { "Registered session: $sessionKey" }
    }

    /**
     * Unregisters a session from the registry.
     *
     * @param id The compose message ID.
     * @param messageId The Telegram message ID.
     */
    fun unregister(id: ComposeMessageId, messageId: Long) {
        val sessionKey = key(id.chatId, messageId)
        sessions.remove(sessionKey)
        logger.trace { "Unregistered session: $sessionKey" }
    }

    /**
     * Retrieves a session by chat ID and message ID.
     *
     * @param chatId The chat ID.
     * @param messageId The Telegram message ID.
     * @return The session, or null if not found or disposed.
     */
    fun get(chatId: Long, messageId: Long): ComposeMessageSession? {
        val session = sessions[key(chatId, messageId)]
        // Return null if session has been disposed
        return if (session != null && !session.isDisposed()) session else null
    }

    /**
     * Checks if a session exists for the given chat and message.
     *
     * @param chatId The chat ID.
     * @param messageId The Telegram message ID.
     * @return True if a session exists and is not disposed, false otherwise.
     */
    fun contains(chatId: Long, messageId: Long): Boolean {
        val session = sessions[key(chatId, messageId)]
        return session != null && !session.isDisposed()
    }

    /**
     * Returns the number of active sessions.
     */
    val size: Int
        get() = sessions.size

    /**
     * Removes all disposed sessions from the registry.
     *
     * This can be called periodically to clean up resources.
     */
    fun cleanupDisposed() {
        val disposedKeys = sessions.entries
            .filter { it.value.isDisposed() }
            .map { it.key }
        disposedKeys.forEach { sessions.remove(it) }
        if (disposedKeys.isNotEmpty()) {
            logger.debug { "Cleaned up ${disposedKeys.size} disposed sessions" }
        }
    }

    /**
     * Clears all sessions from the registry.
     *
     * Warning: This should only be used during shutdown or testing.
     */
    fun clear() {
        sessions.clear()
        logger.debug { "Cleared all sessions from registry" }
    }
}
