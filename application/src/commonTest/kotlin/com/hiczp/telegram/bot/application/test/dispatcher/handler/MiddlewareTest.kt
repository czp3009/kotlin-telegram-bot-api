package com.hiczp.telegram.bot.application.test.dispatcher.handler

import com.hiczp.telegram.bot.application.context.ProvidedUserTelegramBotEventContext
import com.hiczp.telegram.bot.application.context.TelegramBotEventContext
import com.hiczp.telegram.bot.application.dispatcher.handler.*
import com.hiczp.telegram.bot.application.dispatcher.handler.matcher.onMessageEvent
import com.hiczp.telegram.bot.client.TelegramBotClient
import com.hiczp.telegram.bot.protocol.event.MessageEvent
import com.hiczp.telegram.bot.protocol.event.TelegramBotEvent
import com.hiczp.telegram.bot.protocol.model.Chat
import com.hiczp.telegram.bot.protocol.model.Message
import com.hiczp.telegram.bot.protocol.model.User
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import kotlin.test.*
import kotlin.time.Clock

/**
 * Unit tests for the [middleware] and [whenMiddleware] DSL functions.
 */
class MiddlewareTest {
    private val testBotUser = User(
        id = 123456789L,
        isBot = true,
        firstName = "TestBot",
        username = "test_bot",
    )

    private val mockClient = TelegramBotClient(botToken = "xxx")
    private val testScope = TestScope()

    private val invokedHandlers = mutableListOf<String>()

    @BeforeTest
    fun resetTracking() {
        invokedHandlers.clear()
    }

    private fun createContext(
        event: TelegramBotEvent,
    ): TelegramBotEventContext<TelegramBotEvent> {
        return ProvidedUserTelegramBotEventContext(
            client = mockClient,
            event = event,
            applicationScope = testScope,
            botUser = testBotUser,
        )
    }

    private fun createMessageEvent(
        text: String?,
        userId: Long = 1L,
        chatId: Long = 100L,
    ): MessageEvent {
        return MessageEvent(
            updateId = 1L,
            message = Message(
                messageId = 1L,
                date = Clock.System.now().epochSeconds,
                chat = Chat(id = chatId, type = "private"),
                from = User(id = userId, isBot = false, firstName = "User"),
                text = text
            )
        )
    }

    // ==================== Basic Middleware Tests ====================

    @Test
    fun `middleware should allow event when predicate returns true`() = runTest {
        val routeNode = handling {
            onMessageEvent {
                middleware(
                    predicate = { true },
                    build = {
                        handle { invokedHandlers.add("inside_middleware") }
                    }
                )
            }
        }

        val context = createContext(createMessageEvent(text = "hello"))
        val result = routeNode.execute(context)

        assertTrue(result)
        assertEquals(listOf("inside_middleware"), invokedHandlers)
    }

    @Test
    fun `middleware should reject event when predicate returns false without onRejected`() = runTest {
        val routeNode = handling {
            onMessageEvent {
                middleware(
                    predicate = { false },
                    build = {
                        handle { invokedHandlers.add("inside_middleware") }
                    }
                )
            }
        }

        val context = createContext(createMessageEvent(text = "hello"))
        val result = routeNode.execute(context)

        // Without onRejected, event is not consumed
        assertFalse(result)
        assertTrue(invokedHandlers.isEmpty())
    }

    @Test
    fun `middleware should invoke onRejected when predicate returns false`() = runTest {
        val routeNode = handling {
            onMessageEvent {
                middleware(
                    predicate = { false },
                    onRejected = { invokedHandlers.add("rejected") },
                    build = {
                        handle { invokedHandlers.add("inside_middleware") }
                    }
                )
            }
        }

        val context = createContext(createMessageEvent(text = "hello"))
        val result = routeNode.execute(context)

        // With onRejected, event is consumed
        assertTrue(result)
        assertEquals(listOf("rejected"), invokedHandlers)
    }

    @Test
    fun `middleware should pass event through when predicate returns true and onRejected is set`() = runTest {
        val routeNode = handling {
            onMessageEvent {
                middleware(
                    predicate = { true },
                    onRejected = { invokedHandlers.add("rejected") },
                    build = {
                        handle { invokedHandlers.add("inside_middleware") }
                    }
                )
            }
        }

        val context = createContext(createMessageEvent(text = "hello"))
        val result = routeNode.execute(context)

        assertTrue(result)
        assertEquals(listOf("inside_middleware"), invokedHandlers)
    }

    // ==================== Suspend Predicate Tests ====================

    @Test
    fun `middleware predicate should support suspend functions with state tracking`() = runTest {
        var predicateCallCount = 0

        val routeNode = handling {
            onMessageEvent {
                middleware(
                    predicate = {
                        predicateCallCount++
                        true
                    },
                    build = {
                        handle { invokedHandlers.add("inside_middleware") }
                    }
                )
            }
        }

        val context = createContext(createMessageEvent(text = "hello"))
        val result = routeNode.execute(context)

        assertTrue(result)
        assertEquals(1, predicateCallCount, "predicate should be called exactly once")
        assertEquals(listOf("inside_middleware"), invokedHandlers)
    }

    @Test
    fun `middleware should simulate auth check with allowed user IDs`() = runTest {
        // Simulate an auth service with allowed user IDs
        val allowedUserIds = setOf(1L, 2L, 3L)

        suspend fun checkAuth(userId: Long?): Boolean {
            // Pure function check, no delay
            return userId != null && userId in allowedUserIds
        }

        val routeNode = handling {
            onMessageEvent {
                middleware(
                    predicate = { ctx -> checkAuth(ctx.event.message.from?.id) },
                    onRejected = { invokedHandlers.add("unauthorized") },
                    build = {
                        handle { invokedHandlers.add("authorized:${event.message.from?.id}") }
                    }
                )
            }
        }

        // Test authorized user
        var context = createContext(createMessageEvent(text = "admin", userId = 2L))
        assertTrue(routeNode.execute(context))
        assertEquals(listOf("authorized:2"), invokedHandlers)

        // Test unauthorized user
        resetTracking()
        context = createContext(createMessageEvent(text = "admin", userId = 999L))
        assertTrue(routeNode.execute(context))
        assertEquals(listOf("unauthorized"), invokedHandlers)
    }

    // ==================== whenMiddleware Tests ====================

    @Test
    fun `whenMiddleware should combine middleware with handler`() = runTest {
        val routeNode = handling {
            onMessageEvent {
                whenMiddleware(
                    predicate = { it.event.message.from?.id == 1L },
                    onRejected = { invokedHandlers.add("rejected") }
                ) {
                    invokedHandlers.add("handler")
                }
            }
        }

        // Test matching predicate
        var context = createContext(createMessageEvent(text = "test", userId = 1L))
        assertTrue(routeNode.execute(context))
        assertEquals(listOf("handler"), invokedHandlers)

        // Test non-matching predicate
        resetTracking()
        context = createContext(createMessageEvent(text = "test", userId = 2L))
        assertTrue(routeNode.execute(context))
        assertEquals(listOf("rejected"), invokedHandlers)
    }

    @Test
    fun `whenMiddleware without onRejected should not consume event on rejection`() = runTest {
        val routeNode = handling {
            onMessageEvent {
                whenMiddleware(
                    predicate = { it.event.message.from?.id == 1L }
                ) {
                    invokedHandlers.add("handler")
                }
            }
        }

        // Test non-matching predicate - event should not be consumed
        val context = createContext(createMessageEvent(text = "test", userId = 2L))
        val result = routeNode.execute(context)

        assertFalse(result)
        assertTrue(invokedHandlers.isEmpty())
    }

    // ==================== Nested Middleware Tests ====================

    @Test
    fun `nested middleware should work correctly`() = runTest {
        val routeNode = handling {
            onMessageEvent {
                middleware(
                    predicate = { it.event.message.from?.id != null },
                    onRejected = { invokedHandlers.add("no_user") }
                ) {
                    middleware(
                        predicate = { ctx -> ctx.event.message.from?.id == 1L },
                        onRejected = { invokedHandlers.add("not_admin") }
                    ) {
                        handle { invokedHandlers.add("admin_action") }
                    }
                }
            }
        }

        // Test admin user
        var context = createContext(createMessageEvent(text = "test", userId = 1L))
        assertTrue(routeNode.execute(context))
        assertEquals(listOf("admin_action"), invokedHandlers)

        // Test non-admin user
        resetTracking()
        context = createContext(createMessageEvent(text = "test", userId = 2L))
        assertTrue(routeNode.execute(context))
        assertEquals(listOf("not_admin"), invokedHandlers)
    }

    @Test
    fun `middleware with multiple child handlers`() = runTest {
        val routeNode = handling {
            onMessageEvent {
                middleware(
                    predicate = { it.event.message.from?.id == 1L },
                    onRejected = { invokedHandlers.add("unauthorized") }
                ) {
                    handle { invokedHandlers.add("handler1") }
                    handle { invokedHandlers.add("handler2") }  // Replaces handler1
                }
            }
        }

        val context = createContext(createMessageEvent(text = "test", userId = 1L))
        routeNode.execute(context)

        // Only last handler should be invoked (handle replaces previous)
        assertEquals(listOf("handler2"), invokedHandlers)
    }

    // ==================== Middleware with Siblings Tests ====================

    @Test
    fun `middleware rejection without onRejected should fall through to siblings`() = runTest {
        val routeNode = handling {
            onMessageEvent {
                middleware(
                    predicate = { it.event.message.from?.id == 1L },
                    build = {
                        handle { invokedHandlers.add("protected") }
                    }
                )
                handle { invokedHandlers.add("fallback") }
            }
        }

        // Test rejected - should fall through to fallback
        val context = createContext(createMessageEvent(text = "test", userId = 2L))
        val result = routeNode.execute(context)

        assertTrue(result)
        assertEquals(listOf("fallback"), invokedHandlers)
    }

    @Test
    fun `middleware rejection with onRejected should NOT fall through to siblings`() = runTest {
        val routeNode = handling {
            onMessageEvent {
                middleware(
                    predicate = { it.event.message.from?.id == 1L },
                    onRejected = { invokedHandlers.add("unauthorized") },
                    build = {
                        handle { invokedHandlers.add("protected") }
                    }
                )
                handle { invokedHandlers.add("fallback") }
            }
        }

        // Test rejected - should NOT fall through to fallback (event consumed by onRejected)
        val context = createContext(createMessageEvent(text = "test", userId = 2L))
        val result = routeNode.execute(context)

        assertTrue(result)
        assertEquals(listOf("unauthorized"), invokedHandlers)
    }

    // ==================== Custom requireAuth Extension Test ====================

    @Test
    fun `custom requireAuth extension using middleware`() = runTest {
        // Simulate how a user would define their own requireAuth extension
        val adminUserIds = setOf(1L, 2L)

        // User-defined extension function using middleware
        fun <T : TelegramBotEvent> HandlerRoute<T>.requireAuth(build: HandlerRoute<T>.() -> Unit) =
            middleware(
                predicate = { ctx ->
                    // Extract user ID from different event types
                    val userId = when (val event = ctx.event) {
                        is MessageEvent -> event.message.from?.id
                        else -> null
                    }
                    userId in adminUserIds
                },
                onRejected = {
                    invokedHandlers.add("unauthorized:${event.updateId}")
                }
            ) {
                build()
            }

        val routeNode = handling {
            onMessageEvent {
                requireAuth {
                    handle { invokedHandlers.add("admin:${event.message.from?.id}") }
                }
            }
        }

        // Test admin user
        var context = createContext(createMessageEvent(text = "test", userId = 1L))
        assertTrue(routeNode.execute(context))
        assertEquals(listOf("admin:1"), invokedHandlers)

        // Test non-admin user
        resetTracking()
        context = createContext(createMessageEvent(text = "test", userId = 999L))
        assertTrue(routeNode.execute(context))
        assertEquals(listOf("unauthorized:1"), invokedHandlers)
    }

    @Test
    fun `custom suspend requireAuth extension with CompletableDeferred`() = runTest {
        // Simulate an async auth service using CompletableDeferred for deterministic control
        val adminUserIds = setOf(1L, 2L)
        val authResult = CompletableDeferred<Boolean>()

        suspend fun checkAuthAsync(userId: Long?): Boolean {
            // Wait for the result to be completed externally (deterministic control)
            return authResult.await() && userId in adminUserIds
        }

        // User-defined extension function with suspend predicate
        fun <T : TelegramBotEvent> HandlerRoute<T>.requireAuthAsync(build: HandlerRoute<T>.() -> Unit) =
            middleware(
                predicate = { ctx ->
                    val userId = when (val event = ctx.event) {
                        is MessageEvent -> event.message.from?.id
                        else -> null
                    }
                    checkAuthAsync(userId)
                },
                onRejected = {
                    invokedHandlers.add("unauthorized:${event.updateId}")
                }
            ) {
                build()
            }

        val routeNode = handling {
            onMessageEvent {
                requireAuthAsync {
                    handle { invokedHandlers.add("admin:${event.message.from?.id}") }
                }
            }
        }

        // Test with admin user - complete auth successfully
        val context1 = createContext(createMessageEvent(text = "test", userId = 1L))
        val job = launch {
            routeNode.execute(context1)
        }
        authResult.complete(true)
        job.join()
        assertEquals(listOf("admin:1"), invokedHandlers)

        // Test with non-admin user (auth passes but not in admin list)
        resetTracking()
        val authResult2 = CompletableDeferred<Boolean>()
        suspend fun checkAuthAsync2(userId: Long?): Boolean {
            return authResult2.await() && userId in adminUserIds
        }

        val routeNode2 = handling {
            onMessageEvent {
                middleware(
                    predicate = { ctx ->
                        val userId = when (val event = ctx.event) {
                            is MessageEvent -> event.message.from?.id
                            else -> null
                        }
                        checkAuthAsync2(userId)
                    },
                    onRejected = {
                        invokedHandlers.add("unauthorized:${event.updateId}")
                    }
                ) {
                    handle { invokedHandlers.add("admin:${event.message.from?.id}") }
                }
            }
        }

        val context2 = createContext(createMessageEvent(text = "test", userId = 999L))
        val job2 = launch {
            routeNode2.execute(context2)
        }
        authResult2.complete(true)
        job2.join()
        assertEquals(listOf("unauthorized:1"), invokedHandlers)
    }

    // ==================== Coroutine Scope Tests ====================

    @Test
    fun `middleware handler should have CoroutineScope receiver`() = runTest {
        val launchCompleted = CompletableDeferred<Unit>()

        val routeNode = handling {
            onMessageEvent {
                middleware(
                    predicate = { true },
                    build = {
                        handle {
                            launch {
                                launchCompleted.complete(Unit)
                            }
                        }
                    }
                )
            }
        }

        val context = createContext(createMessageEvent(text = "test"))
        val result = routeNode.execute(context)

        assertTrue(result)
        assertTrue(launchCompleted.isCompleted, "launch inside middleware handler should be executed")
    }

    @Test
    fun `onRejected handler should have CoroutineScope receiver`() = runTest {
        val launchCompleted = CompletableDeferred<Unit>()

        val routeNode = handling {
            onMessageEvent {
                middleware(
                    predicate = { false },
                    onRejected = {
                        launch {
                            launchCompleted.complete(Unit)
                        }
                    },
                    build = {
                        handle { invokedHandlers.add("inside") }
                    }
                )
            }
        }

        val context = createContext(createMessageEvent(text = "test"))
        val result = routeNode.execute(context)

        assertTrue(result)
        assertTrue(launchCompleted.isCompleted, "launch inside onRejected should be executed")
    }

    // ==================== Complex Scenario Tests ====================

    @Test
    fun `middleware with command handlers`() = runTest {
        val routeNode = handling {
            onMessageEvent {
                middleware(
                    predicate = { it.event.message.from?.id == 1L },
                    onRejected = { invokedHandlers.add("unauthorized") }
                ) {
                    handle { invokedHandlers.add("any_message") }
                }
            }
        }

        // Test authorized user
        var context = createContext(createMessageEvent(text = "hello", userId = 1L))
        assertTrue(routeNode.execute(context))
        assertEquals(listOf("any_message"), invokedHandlers)

        // Test unauthorized user
        resetTracking()
        context = createContext(createMessageEvent(text = "hello", userId = 2L))
        assertTrue(routeNode.execute(context))
        assertEquals(listOf("unauthorized"), invokedHandlers)
    }

    @Test
    fun `middleware with match inside`() = runTest {
        val routeNode = handling {
            onMessageEvent {
                middleware(
                    predicate = { it.event.message.from?.id == 1L },
                    onRejected = { invokedHandlers.add("unauthorized") }
                ) {
                    // Use match inside middleware
                    match({ ctx -> ctx.event.message.text?.startsWith("/admin") == true }) {
                        handle { invokedHandlers.add("admin_command") }
                    }
                    handle { invokedHandlers.add("authenticated_fallback") }
                }
            }
        }

        // Test admin command
        var context = createContext(createMessageEvent(text = "/admin stats", userId = 1L))
        assertTrue(routeNode.execute(context))
        assertEquals(listOf("admin_command"), invokedHandlers)

        // Test authenticated but not admin command
        resetTracking()
        context = createContext(createMessageEvent(text = "hello", userId = 1L))
        assertTrue(routeNode.execute(context))
        assertEquals(listOf("authenticated_fallback"), invokedHandlers)

        // Test unauthorized user
        resetTracking()
        context = createContext(createMessageEvent(text = "/admin stats", userId = 2L))
        assertTrue(routeNode.execute(context))
        assertEquals(listOf("unauthorized"), invokedHandlers)
    }
}
