/**
 * Blacklist Interceptor
 *
 * An interceptor that blocks users in a blacklist from using the bot.
 * When a blocked user sends an event, the interceptor sends an optional error message
 * and stops further processing.
 *
 * ## Usage
 *
 * ```kotlin
 * // Create a shared blacklist
 * val blacklist = mutableSetOf<Long>()
 *
 * // Create the interceptor with a filter
 * val interceptors = listOf(
 *     createBlacklistInterceptor(
 *         blacklist = blacklist,
 *         messageFilter = { event -> event is MessageEvent }
 *     )
 * )
 *
 * // Only block for command messages
 * val interceptors = listOf(
 *     createBlacklistInterceptor(
 *         blacklist = blacklist,
 *         messageFilter = { event ->
 *             event is MessageEvent && event.message.isCommand
 *         }
 *     )
 * )
 *
 * // In handlers, use banUser() to add users to blacklist
 * commandEndpoint("ban") {
 *     val targetUserId = event.message.replyToMessage?.from?.id
 *     if (targetUserId != null) {
 *         banUser(targetUserId)
 *         replyMessage("User $targetUserId has been banned.")
 *     }
 * }
 * ```
 *
 * @see banUser
 * @see banCurrentUser
 */
package com.hiczp.telegram.bot.sample.basic.interceptor

import com.hiczp.telegram.bot.application.context.TelegramBotEventContext
import com.hiczp.telegram.bot.application.context.extractUserId
import com.hiczp.telegram.bot.application.interceptor.TelegramEventInterceptor
import com.hiczp.telegram.bot.protocol.event.TelegramBotEvent
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.util.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

private val logger = KotlinLogging.logger("BlacklistInterceptor")

private val BanUserAttributeKey = AttributeKey<MutableSet<Long>>("BlacklistInterceptor.usersToBan")

/**
 * Creates a blacklist interceptor with its own mutable blacklist.
 *
 * @param blacklist A mutable set to user IDs that are blocked.
 * @param messageFilter A filter function that determines which events should be checked against the blacklist.
 *                      Return `true` to check this event, `false` to skip blacklist check for this event.
 * @param onBlocked Optional callback invoked when a blocked user is detected.
 * @return A [TelegramEventInterceptor] that blocks blacklisted users.
 */
fun createBlacklistInterceptor(
    blacklist: MutableSet<Long>,
    messageFilter: (TelegramBotEvent) -> Boolean,
    onBlocked: (suspend (TelegramBotEventContext<TelegramBotEvent>, Long) -> Unit)? = null,
): TelegramEventInterceptor {
    // Mutex for thread-safe access to the blacklist
    val mutex = Mutex()

    return Interceptor@{ context ->
        // Check if this event should be filtered
        if (!messageFilter(context.event)) {
            process(context)
            return@Interceptor
        }

        val userId = context.event.extractUserId()

        // Check if the user is in the blacklist (read is safe without the lock)
        if (userId != null && blacklist.contains(userId)) {
            logger.warn { "Blocked user $userId attempted to use the bot" }
            onBlocked?.invoke(context, userId)
            // Don't call process(context) - stops the interceptor chain
            return@Interceptor
        }

        // Initialize the ban set for this request
        context.attributes.put(BanUserAttributeKey, mutableSetOf())

        // User doesn't in blacklist, continue processing
        process(context)

        // After processing, settle: add any marked users to the blacklist (write requires lock)
        val banSet = context.attributes.getOrNull(BanUserAttributeKey)
        if (!banSet.isNullOrEmpty()) {
            mutex.withLock {
                blacklist.addAll(banSet)
            }
            logger.info { "Added users to blacklist: $banSet" }
        }
    }
}

/**
 * Marks a user to be added to the blacklist after the current event processing completes.
 *
 * This function should be called within a handler to ban a user. The user will be added
 * to the blacklist after the handler finishes executing.
 *
 * Note: This only works when [createBlacklistInterceptor] is installed in the interceptor chain.
 *
 * @param userId The user ID to ban.
 */
fun TelegramBotEventContext<*>.banUser(userId: Long): Boolean {
    val banSet = attributes.getOrNull(BanUserAttributeKey)
    return if (banSet != null) {
        banSet.add(userId)
        logger.debug { "User $userId marked for banning" }
        true
    } else {
        logger.warn { "banUser() called but BlacklistInterceptor is not installed or attribute not available" }
        false
    }
}

/**
 * Marks the current user (from the event) to be added to the blacklist.
 *
 * Convenience function that extracts the user ID from the current event.
 *
 * @return `true` if the user was marked for banning, `false` if no user ID could be extracted.
 */
fun TelegramBotEventContext<*>.banCurrentUser(): Boolean {
    val userId = event.extractUserId()
    return if (userId != null) {
        banUser(userId)
    } else {
        false
    }
}
