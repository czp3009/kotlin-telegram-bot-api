package com.hiczp.telegram.bot.sample.dsl

import com.hiczp.telegram.bot.application.dispatcher.handler.HandlerBotCall
import com.hiczp.telegram.bot.application.dispatcher.handler.HandlerRoute
import com.hiczp.telegram.bot.application.dispatcher.handler.middleware
import com.hiczp.telegram.bot.protocol.event.MessageEvent
import kotlinx.coroutines.delay

/**
 * A mock authentication service for demonstrating async predicate in [requireAuth].
 *
 * In a real application, this could be a service that queries a database or external API
 * to check user permissions.
 */
class MockAuthService {
    /**
     * Simulates an async database/API call to check if a user is an admin.
     */
    suspend fun isAdmin(userId: Long?): Boolean {
        if (userId == null) return false
        // Simulate network/database delay
        delay(100)
        return userId in adminUserIds
    }

    companion object {
        private val adminUserIds = setOf(196664407L)
    }
}

/**
 * A user-defined authentication DSL that wraps [middleware] for authorization.
 *
 * This example demonstrates how to create a reusable DSL that performs **async**
 * authorization checks (e.g., querying a database or external service).
 *
 * Example usage:
 * ```kotlin
 * val authService = MockAuthService()
 *
 * handling {
 *     requireAuth(
 *         authService = authService,
 *         onRejected = { replyMessage("Unauthorized") }
 *     ) {
 *         command("admin") {
 *             handle { /* Show admin help */ }
 *             subCommandEndpoint("status") { /* ... */ }
 *         }
 *     }
 * }
 * ```
 *
 * @param authService The authentication service to use for checking permissions.
 * @param onRejected Handler invoked when authorization fails.
 * @param build Lambda for defining protected child routes.
 */
fun HandlerRoute<MessageEvent>.requireAuth(
    authService: MockAuthService,
    onRejected: suspend HandlerBotCall<MessageEvent>.() -> Unit,
    build: HandlerRoute<MessageEvent>.() -> Unit
) = middleware(
    predicate = { context ->
        authService.isAdmin(context.event.message.from?.id)
    },
    onRejected = onRejected,
    build = build
)
