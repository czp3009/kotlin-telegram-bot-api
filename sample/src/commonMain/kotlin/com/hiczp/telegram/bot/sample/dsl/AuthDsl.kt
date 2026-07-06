package com.hiczp.telegram.bot.sample.dsl

import com.hiczp.telegram.bot.application.dispatcher.handler.HandlerRoute
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
 * A user-defined authentication DSL built from [HandlerRoute.filter].
 *
 * This example demonstrates how to create a reusable DSL that performs **async**
 * authorization checks (e.g., querying a database or external service).
 *
 * Example usage:
 * ```kotlin
 * val authService = MockAuthService()
 *
 * handling {
 *     requireAuth(authService) {
 *         command("admin") {
 *             handle { /* Show admin help */ }
 *             subCommand("status") { handle { /* ... */ } }
 *         }
 *     }
 * }
 * ```
 *
 * @param authService The authentication service to use for checking permissions.
 * @param build Lambda for defining protected child routes.
 */
fun HandlerRoute<MessageEvent>.requireAuth(
    authService: MockAuthService,
    build: HandlerRoute<MessageEvent>.() -> Unit
) = filter({ authService.isAdmin(event.message.from?.id) }, build)
