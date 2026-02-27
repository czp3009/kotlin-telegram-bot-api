package com.hiczp.telegram.bot.application.test.dispatcher.handler

import com.hiczp.telegram.bot.application.context.ProvidedUserTelegramBotEventContext
import com.hiczp.telegram.bot.application.context.TelegramBotEventContext
import com.hiczp.telegram.bot.application.dispatcher.handler.handling
import com.hiczp.telegram.bot.application.dispatcher.handler.include
import com.hiczp.telegram.bot.application.dispatcher.handler.match
import com.hiczp.telegram.bot.application.dispatcher.handler.matcher.*
import com.hiczp.telegram.bot.application.dispatcher.handler.whenMatch
import com.hiczp.telegram.bot.client.TelegramBotClient
import com.hiczp.telegram.bot.protocol.event.CallbackQueryEvent
import com.hiczp.telegram.bot.protocol.event.InlineQueryEvent
import com.hiczp.telegram.bot.protocol.event.MessageEvent
import com.hiczp.telegram.bot.protocol.event.TelegramBotEvent
import com.hiczp.telegram.bot.protocol.model.*
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import kotlin.test.*
import kotlin.time.Clock

/**
 * Unit tests for the [handling] DSL routing functionality.
 *
 * These tests focus on the routing tree structure and matching logic.
 */
class HandlingTest {
    private val testBotUser = User(
        id = 123456789L,
        isBot = true,
        firstName = "TestBot",
        username = "test_bot",
    )
    private val testChat = Chat(id = 100L, type = "private")
    private val testUser = User(id = 1L, isBot = false, firstName = "TestUser")

    // Fake client
    private val mockClient = TelegramBotClient(botToken = "xxx")

    private val testScope = TestScope()

    // Track which handlers were invoked
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
        chat: Chat = testChat,
        text: String,
    ): MessageEvent {
        return MessageEvent(
            updateId = 1L,
            message = Message(
                messageId = 1L,
                date = Clock.System.now().epochSeconds,
                chat = chat,
                from = testUser,
                text = text
            )
        )
    }

    private fun createCallbackQueryEvent(
        data: String,
    ): CallbackQueryEvent {
        return CallbackQueryEvent(
            updateId = 1L,
            callbackQuery = CallbackQuery(
                id = "1",
                from = testUser,
                chatInstance = "test_instance",
                data = data
            )
        )
    }

    private fun createInlineQueryEvent(
        query: String,
    ): InlineQueryEvent {
        return InlineQueryEvent(
            updateId = 1L,
            inlineQuery = InlineQuery(
                id = "1",
                from = testUser,
                query = query,
                offset = "0"
            )
        )
    }

    // ==================== Command Routing Tests ====================

    @Test
    fun `command handler should match exact command`() = runTest {
        val routeNode = handling {
            command("start") { _, _ ->
                invokedHandlers.add("start")
            }
        }

        val context = createContext(createMessageEvent(text = "/start"))
        val result = routeNode.execute(context)

        assertTrue(result)
        assertEquals(listOf("start"), invokedHandlers)
    }

    @Test
    fun `command handler should match command with bot username`() = runTest {
        val routeNode = handling {
            command("start") { _, _ ->
                invokedHandlers.add("start")
            }
        }

        val context = createContext(createMessageEvent(text = "/start@test_bot"))
        val result = routeNode.execute(context)

        assertTrue(result)
        assertEquals(listOf("start"), invokedHandlers)
    }

    @Test
    fun `command handler should not match command with different bot username`() = runTest {
        val routeNode = handling {
            command("start") { _, _ ->
                invokedHandlers.add("start")
            }
        }

        val context = createContext(createMessageEvent(text = "/start@other_bot"))
        val result = routeNode.execute(context)

        assertFalse(result)
        assertTrue(invokedHandlers.isEmpty())
    }

    @Test
    fun `command handler should not match different command`() = runTest {
        val routeNode = handling {
            command("start") { _, _ ->
                invokedHandlers.add("start")
            }
        }

        val context = createContext(createMessageEvent(text = "/help"))
        val result = routeNode.execute(context)

        assertFalse(result)
        assertTrue(invokedHandlers.isEmpty())
    }

    @Test
    fun `command handler should be case insensitive`() = runTest {
        val routeNode = handling {
            command("START") { _, _ ->
                invokedHandlers.add("start")
            }
        }

        val context = createContext(createMessageEvent(text = "/start"))
        val result = routeNode.execute(context)

        assertTrue(result)
        assertEquals(listOf("start"), invokedHandlers)
    }

    @Test
    fun `multiple command handlers should route correctly`() = runTest {
        val routeNode = handling {
            command("start") { _, _ ->
                invokedHandlers.add("start")
            }
            command("help") { _, _ ->
                invokedHandlers.add("help")
            }
            command("stop") { _, _ ->
                invokedHandlers.add("stop")
            }
        }

        // Test /start
        var context = createContext(createMessageEvent(text = "/start"))
        assertTrue(routeNode.execute(context))
        assertEquals(listOf("start"), invokedHandlers)

        // Test /help
        resetTracking()
        context = createContext(createMessageEvent(text = "/help"))
        assertTrue(routeNode.execute(context))
        assertEquals(listOf("help"), invokedHandlers)

        // Test /stop
        resetTracking()
        context = createContext(createMessageEvent(text = "/stop"))
        assertTrue(routeNode.execute(context))
        assertEquals(listOf("stop"), invokedHandlers)
    }

    // ==================== Text Routing Tests ====================

    @Test
    fun `text handler should match exact text`() = runTest {
        val routeNode = handling {
            text("hello") {
                invokedHandlers.add("hello")
            }
        }

        val context = createContext(createMessageEvent(text = "hello"))
        val result = routeNode.execute(context)

        assertTrue(result)
        assertEquals(listOf("hello"), invokedHandlers)
    }

    @Test
    fun `text handler should not match different text`() = runTest {
        val routeNode = handling {
            text("hello") {
                invokedHandlers.add("hello")
            }
        }

        val context = createContext(createMessageEvent(text = "Hello")) // Case-sensitive
        val result = routeNode.execute(context)

        assertFalse(result)
        assertTrue(invokedHandlers.isEmpty())
    }

    @Test
    fun `text handler should not match partial text`() = runTest {
        val routeNode = handling {
            text("hello") {
                invokedHandlers.add("hello")
            }
        }

        val context = createContext(createMessageEvent(text = "hello world"))
        val result = routeNode.execute(context)

        assertFalse(result)
        assertTrue(invokedHandlers.isEmpty())
    }

    // ==================== Regex Routing Tests ====================

    @Test
    fun `textRegex handler should match pattern`() = runTest {
        val routeNode = handling {
            textRegex(Regex("(?i)hello.*")) {
                invokedHandlers.add("hello_regex")
            }
        }

        val context = createContext(createMessageEvent(text = "Hello World"))
        val result = routeNode.execute(context)

        assertTrue(result)
        assertEquals(listOf("hello_regex"), invokedHandlers)
    }

    @Test
    fun `textRegex handler should not match non-matching text`() = runTest {
        val routeNode = handling {
            textRegex(Regex("^hello")) {
                invokedHandlers.add("hello_regex")
            }
        }

        val context = createContext(createMessageEvent(text = "Say hello"))
        val result = routeNode.execute(context)

        assertFalse(result)
        assertTrue(invokedHandlers.isEmpty())
    }

    @Test
    fun `textRegex handler should match complex pattern`() = runTest {
        val routeNode = handling {
            textRegex(Regex(".*\\d{4}-\\d{2}-\\d{2}.*")) {
                invokedHandlers.add("date")
            }
        }

        val context = createContext(createMessageEvent(text = "Date: 2024-01-15"))
        val result = routeNode.execute(context)

        assertTrue(result)
        assertEquals(listOf("date"), invokedHandlers)
    }

    // ==================== Callback Data Routing Tests ====================

    @Test
    fun `callbackData handler should match exact data`() = runTest {
        val routeNode = handling {
            callbackData("confirm") {
                invokedHandlers.add("confirm")
            }
        }

        val context = createContext(createCallbackQueryEvent(data = "confirm"))
        val result = routeNode.execute(context)

        assertTrue(result)
        assertEquals(listOf("confirm"), invokedHandlers)
    }

    @Test
    fun `callbackData handler should not match different data`() = runTest {
        val routeNode = handling {
            callbackData("confirm") {
                invokedHandlers.add("confirm")
            }
        }

        val context = createContext(createCallbackQueryEvent(data = "cancel"))
        val result = routeNode.execute(context)

        assertFalse(result)
        assertTrue(invokedHandlers.isEmpty())
    }

    // ==================== Inline Query Routing Tests ====================

    @Test
    fun `inlineQuery handler should match exact query`() = runTest {
        val routeNode = handling {
            inlineQuery("search") {
                invokedHandlers.add("search")
            }
        }

        val context = createContext(createInlineQueryEvent(query = "search"))
        val result = routeNode.execute(context)

        assertTrue(result)
        assertEquals(listOf("search"), invokedHandlers)
    }

    @Test
    fun `inlineQuery handler should not match different query`() = runTest {
        val routeNode = handling {
            inlineQuery("search") {
                invokedHandlers.add("search")
            }
        }

        val context = createContext(createInlineQueryEvent(query = "find"))
        val result = routeNode.execute(context)

        assertFalse(result)
        assertTrue(invokedHandlers.isEmpty())
    }

    // ==================== Event Type Matching Tests ====================

    @Test
    fun `on match should route by event type`() = runTest {
        val routeNode = handling {
            on<MessageEvent> {
                handle { invokedHandlers.add("message") }
            }
            on<CallbackQueryEvent> {
                handle { invokedHandlers.add("callback") }
            }
        }

        // Test MessageEvent
        var context = createContext(createMessageEvent(text = "test"))
        assertTrue(routeNode.execute(context))
        assertEquals(listOf("message"), invokedHandlers)

        // Test CallbackQueryEvent
        resetTracking()
        context = createContext(createCallbackQueryEvent(data = "data"))
        assertTrue(routeNode.execute(context))
        assertEquals(listOf("callback"), invokedHandlers)
    }

    @Test
    fun `on with predicate should match events`() = runTest {
        val routeNode = handling {
            on<MessageEvent>({ it.event.message.chat.id == 100L }) {
                handle { invokedHandlers.add("chat_100") }
            }
        }

        // Should match - chat id be 100?
        var context = createContext(createMessageEvent(text = "test"))
        assertTrue(routeNode.execute(context))
        assertEquals(listOf("chat_100"), invokedHandlers)

        // Should not match - different chat
        resetTracking()
        val otherChat = Chat(id = 999L, type = "private")
        context = createContext(createMessageEvent(text = "test", chat = otherChat))
        assertFalse(routeNode.execute(context))
        assertTrue(invokedHandlers.isEmpty())
    }

    // ==================== Match Tests ====================

    @Test
    fun `match should allow custom predicates`() = runTest {
        val routeNode = handling {
            on<MessageEvent> {
                match({ (it.event.message.text?.length ?: 0) > 5 }) {
                    handle { invokedHandlers.add("long_message") }
                }
            }
        }

        // Should match - text length > 5
        var context = createContext(createMessageEvent(text = "hello world"))
        assertTrue(routeNode.execute(context))
        assertEquals(listOf("long_message"), invokedHandlers)

        // Should not match - text length <= 5
        resetTracking()
        context = createContext(createMessageEvent(text = "hi"))
        assertFalse(routeNode.execute(context))
        assertTrue(invokedHandlers.isEmpty())
    }

    @Test
    fun `whenMatch should combine matching and handling`() = runTest {
        val routeNode = handling {
            on<MessageEvent> {
                whenMatch({ (it.event.message.text?.length ?: 0) > 5 }) { ctx ->
                    invokedHandlers.add("long_message:${ctx.event.message.text}")
                }
            }
        }

        // Should match - text length > 5
        var context = createContext(createMessageEvent(text = "hello world"))
        assertTrue(routeNode.execute(context))
        assertEquals(listOf("long_message:hello world"), invokedHandlers)

        // Should not match - text length <= 5
        resetTracking()
        context = createContext(createMessageEvent(text = "hi"))
        assertFalse(routeNode.execute(context))
        assertTrue(invokedHandlers.isEmpty())
    }

    @Test
    fun `whenMatch should support CoroutineScope receiver`() = runTest {
        var launchExecuted = false

        val routeNode = handling {
            on<MessageEvent> {
                whenMatch({ it.event.message.text?.contains("test") == true }) {
                    launch {
                        launchExecuted = true
                    }
                }
            }
        }

        val context = createContext(createMessageEvent(text = "test message"))
        val result = routeNode.execute(context)

        assertTrue(result)
        assertTrue(launchExecuted, "launch inside whenMatch handler should be executed")
    }

    // ==================== Include Tests ====================

    @Test
    fun `include should merge route nodes`() = runTest {
        val adminRoutes = handling {
            command("admin") { _, _ ->
                invokedHandlers.add("admin")
            }
        }

        val mainRoutes = handling {
            command("start") { _, _ ->
                invokedHandlers.add("start")
            }
            include(adminRoutes)
        }

        // Test the main route
        var context = createContext(createMessageEvent(text = "/start"))
        assertTrue(mainRoutes.execute(context))
        assertEquals(listOf("start"), invokedHandlers)

        // Test included route
        resetTracking()
        context = createContext(createMessageEvent(text = "/admin"))
        assertTrue(mainRoutes.execute(context))
        assertEquals(listOf("admin"), invokedHandlers)
    }

    // ==================== Depth-First Short-Circuit Tests ====================

    @Test
    fun `handle acts as fallback when children do not consume`() = runTest {
        // handle is invoked only when no child consumes the event
        val routeNode = handling {
            on<MessageEvent> {
                command("start") { _, _ ->
                    invokedHandlers.add("start")
                }
                text("hello") {
                    invokedHandlers.add("hello")
                }
                handle { invokedHandlers.add("handle_fallback") }
            }
        }

        // Test command match - child consumes, handle not invoked
        var context = createContext(createMessageEvent(text = "/start"))
        assertTrue(routeNode.execute(context))
        assertEquals(listOf("start"), invokedHandlers)

        // Test text match - child consumes, handle not invoked
        resetTracking()
        context = createContext(createMessageEvent(text = "hello"))
        assertTrue(routeNode.execute(context))
        assertEquals(listOf("hello"), invokedHandlers)

        // Test no child match - handle acts as fallback
        resetTracking()
        context = createContext(createMessageEvent(text = "random text"))
        assertTrue(routeNode.execute(context))
        assertEquals(listOf("handle_fallback"), invokedHandlers, "handle should act as fallback")
    }

    @Test
    fun `handle position does not affect priority - children always tried first`() = runTest {
        // handle position in DSL doesn't matter - children are always tried first
        val routeNode = handling {
            on<MessageEvent> {
                handle { invokedHandlers.add("handle") }
                command("start") { _, _ ->
                    invokedHandlers.add("start")
                }
            }
        }

        // Even though handle is declared first, command (child) is tried first
        val context = createContext(createMessageEvent(text = "/start"))
        val result = routeNode.execute(context)

        assertTrue(result)
        assertEquals(
            listOf("start"),
            invokedHandlers,
            "children are tried before handle regardless of declaration order"
        )
    }

    @Test
    fun `match with handle inside should not short-circuit siblings when match fails`() = runTest {
        val routeNode = handling {
            on<MessageEvent> {
                match({ (it.event.message.text?.length ?: 0) > 100 }) {
                    handle { invokedHandlers.add("long_text") }
                }
                handle { invokedHandlers.add("fallback") }
            }
        }

        // Match fails, should fall through to handle
        val context = createContext(createMessageEvent(text = "short"))
        val result = routeNode.execute(context)

        assertTrue(result)
        assertEquals(listOf("fallback"), invokedHandlers, "should fall through to handle when match fails")
    }

    @Test
    fun `should short-circuit on first matching handler`() = runTest {
        val routeNode = handling {
            on<MessageEvent> {
                match({ true }) {
                    handle { invokedHandlers.add("first") }
                }
                match({ true }) {
                    handle { invokedHandlers.add("second") }
                }
            }
        }

        val context = createContext(createMessageEvent(text = "test"))
        val result = routeNode.execute(context)

        assertTrue(result)
        // Only the first handler should be invoked due to short-circuit
        assertEquals(listOf("first"), invokedHandlers)
    }

    @Test
    fun `should continue to sibling if child does not consume`() = runTest {
        val routeNode = handling {
            on<MessageEvent> {
                match({ false }) {
                    handle { invokedHandlers.add("matched_out") }
                }
                text("hello") {
                    invokedHandlers.add("hello")
                }
            }
        }

        val context = createContext(createMessageEvent(text = "hello"))
        val result = routeNode.execute(context)

        assertTrue(result)
        assertEquals(listOf("hello"), invokedHandlers)
    }

    @Test
    fun `nested routes should work correctly`() = runTest {
        val routeNode = handling {
            on<MessageEvent> {
                match({ it.event.message.chat.id == 100L }) {
                    command("admin") { _, _ ->
                        invokedHandlers.add("admin_chat_100")
                    }
                    text("ping") {
                        invokedHandlers.add("ping_chat_100")
                    }
                }
            }
        }

        // Test nested command
        var context = createContext(createMessageEvent(text = "/admin"))
        assertTrue(routeNode.execute(context))
        assertEquals(listOf("admin_chat_100"), invokedHandlers)

        // Test nested text
        resetTracking()
        context = createContext(createMessageEvent(text = "ping"))
        assertTrue(routeNode.execute(context))
        assertEquals(listOf("ping_chat_100"), invokedHandlers)

        // Test command not matching match
        resetTracking()
        val otherChat = Chat(id = 999L, type = "private")
        context = createContext(createMessageEvent(text = "/admin", chat = otherChat))
        assertFalse(routeNode.execute(context))
        assertTrue(invokedHandlers.isEmpty())
    }

    // ==================== No Match Tests ====================

    @Test
    fun `should return false when no handler matches`() = runTest {
        val routeNode = handling {
            command("start") { _, _ ->
                invokedHandlers.add("start")
            }
        }

        val context = createContext(createMessageEvent(text = "random text"))
        val result = routeNode.execute(context)

        assertFalse(result)
        assertTrue(invokedHandlers.isEmpty())
    }

    @Test
    fun `root level handle should act as dead letter handler`() = runTest {
        // handle at root level catches all unhandled events (dead letter mechanism)
        val routeNode = handling {
            command("start") { _, _ ->
                invokedHandlers.add("start")
            }
            text("hello") {
                invokedHandlers.add("hello")
            }
            // Dead letter handler - catches everything else
            handle { ctx ->
                invokedHandlers.add("dead_letter:${ctx.event.updateId}")
            }
        }

        // Test command match - dead letter not invoked
        var context = createContext(createMessageEvent(text = "/start"))
        assertTrue(routeNode.execute(context))
        assertEquals(listOf("start"), invokedHandlers)

        // Test text match - dead letter not invoked
        resetTracking()
        context = createContext(createMessageEvent(text = "hello"))
        assertTrue(routeNode.execute(context))
        assertEquals(listOf("hello"), invokedHandlers)

        // Test unmatched event - dead letter catches it
        resetTracking()
        context = createContext(createMessageEvent(text = "unhandled message"))
        assertTrue(routeNode.execute(context))
        assertEquals(listOf("dead_letter:1"), invokedHandlers, "dead letter handler should catch unmatched events")

        // Test callback query - dead letter catches it (different event type)
        resetTracking()
        context = createContext(createCallbackQueryEvent(data = "unknown"))
        assertTrue(routeNode.execute(context))
        assertEquals(
            listOf("dead_letter:1"),
            invokedHandlers,
            "dead letter handler should catch unmatched callback queries"
        )
    }

    @Test
    fun `root level handle with on blocks should act as dead letter handler`() = runTest {
        val routeNode = handling {
            on<MessageEvent> {
                command("start") { _, _ ->
                    invokedHandlers.add("start")
                }
            }
            on<CallbackQueryEvent> {
                callbackData("confirm") {
                    invokedHandlers.add("confirm")
                }
            }
            // Dead letter handler for all other events
            handle {
                invokedHandlers.add("dead_letter")
            }
        }

        // Test matched command
        var context = createContext(createMessageEvent(text = "/start"))
        assertTrue(routeNode.execute(context))
        assertEquals(listOf("start"), invokedHandlers)

        // Test matched callback
        resetTracking()
        context = createContext(createCallbackQueryEvent(data = "confirm"))
        assertTrue(routeNode.execute(context))
        assertEquals(listOf("confirm"), invokedHandlers)

        // Test unmatched message - dead letter
        resetTracking()
        context = createContext(createMessageEvent(text = "unknown"))
        assertTrue(routeNode.execute(context))
        assertEquals(listOf("dead_letter"), invokedHandlers)

        // Test unmatched callback - dead letter
        resetTracking()
        context = createContext(createCallbackQueryEvent(data = "unknown"))
        assertTrue(routeNode.execute(context))
        assertEquals(listOf("dead_letter"), invokedHandlers)

        // Test inline query (not handled by any on block) - dead letter
        resetTracking()
        context = createContext(createInlineQueryEvent(query = "search"))
        assertTrue(routeNode.execute(context))
        assertEquals(listOf("dead_letter"), invokedHandlers)
    }

    @Test
    fun `empty handling should not match any event`() = runTest {
        val routeNode = handling {}

        val context = createContext(createMessageEvent(text = "test"))
        val result = routeNode.execute(context)

        assertFalse(result)
    }

    // ==================== Select (Low-Level) Tests ====================

    @Test
    fun `select should allow custom selectors`() = runTest {
        val routeNode = handling {
            on<MessageEvent> {
                select(
                    selector = { ctx ->
                        // Custom selector that transforms the context
                        if (ctx.event.message.text?.startsWith("special:") == true) {
                            ctx
                        } else null
                    },
                    build = {
                        handle { invokedHandlers.add("special") }
                    }
                )
            }
        }

        // Should match - start with "special:"
        var context = createContext(createMessageEvent(text = "special: value"))
        assertTrue(routeNode.execute(context))
        assertEquals(listOf("special"), invokedHandlers)

        // Should not match
        resetTracking()
        context = createContext(createMessageEvent(text = "normal value"))
        assertFalse(routeNode.execute(context))
        assertTrue(invokedHandlers.isEmpty())
    }

    // ==================== Mixed Event Types Tests ====================

    @Test
    fun `mixed event type handlers should route correctly`() = runTest {
        val routeNode = handling {
            command("start") { _, _ ->
                invokedHandlers.add("cmd_start")
            }
            text("hello") {
                invokedHandlers.add("txt_hello")
            }
            callbackData("confirm") {
                invokedHandlers.add("cb_confirm")
            }
            inlineQuery("search") {
                invokedHandlers.add("inline_search")
            }
        }

        // Test command
        var context = createContext(createMessageEvent(text = "/start"))
        assertTrue(routeNode.execute(context))
        assertEquals(listOf("cmd_start"), invokedHandlers)

        // Test text
        resetTracking()
        context = createContext(createMessageEvent(text = "hello"))
        assertTrue(routeNode.execute(context))
        assertEquals(listOf("txt_hello"), invokedHandlers)

        // Test callback
        resetTracking()
        context = createContext(createCallbackQueryEvent(data = "confirm"))
        assertTrue(routeNode.execute(context))
        assertEquals(listOf("cb_confirm"), invokedHandlers)

        // Test inline query
        resetTracking()
        context = createContext(createInlineQueryEvent(query = "search"))
        assertTrue(routeNode.execute(context))
        assertEquals(listOf("inline_search"), invokedHandlers)
    }

    // ==================== Coroutine Scope Tests ====================

    @Test
    fun `handler should have CoroutineScope receiver for launch`() = runTest {
        var launchExecuted = false

        val routeNode = handling {
            command("start") { _, _ ->
                launch {
                    launchExecuted = true
                }
            }
        }

        val context = createContext(createMessageEvent(text = "/start"))
        val result = routeNode.execute(context)

        assertTrue(result)
        assertTrue(launchExecuted, "launch inside handler should be executed")
    }

    @Test
    fun `dispatch should wait for handler launch to complete`() = runTest {
        val handlerMainCompleted = CompletableDeferred<Unit>()
        val launchCompleted = CompletableDeferred<Unit>()

        val routeNode = handling {
            command("start") { _, _ ->
                launch {
                    // Wait for main handler to complete first (controlled order)
                    handlerMainCompleted.await()
                    launchCompleted.complete(Unit)
                }
                handlerMainCompleted.complete(Unit)
            }
        }

        val context = createContext(createMessageEvent(text = "/start"))
        val result = routeNode.execute(context)

        assertTrue(result)
        // If dispatch waits for launch, this should be completed
        assertTrue(launchCompleted.isCompleted, "dispatch should wait for all launched coroutines to complete")
    }

    @Test
    fun `dispatch should wait for multiple launches to complete`() = runTest {
        val results = mutableSetOf<String>()

        val routeNode = handling {
            command("start") { _, _ ->
                launch {
                    results.add("first")
                }
                launch {
                    results.add("second")
                }
                launch {
                    results.add("third")
                }
            }
        }

        val context = createContext(createMessageEvent(text = "/start"))
        routeNode.execute(context)

        // All launches should complete - use Set to avoid ordering issues
        assertEquals(3, results.size, "all launches should complete")
        assertTrue("first" in results, "first should complete")
        assertTrue("second" in results, "second should complete")
        assertTrue("third" in results, "third should complete")
    }

    @Test
    fun `nested coroutineScope should work in handler`() = runTest {
        var nestedScopeExecuted = false

        val routeNode = handling {
            command("start") { _, _ ->
                coroutineScope {
                    launch {
                        nestedScopeExecuted = true
                    }
                }
            }
        }

        val context = createContext(createMessageEvent(text = "/start"))
        val result = routeNode.execute(context)

        assertTrue(result)
        assertTrue(nestedScopeExecuted, "nested coroutineScope launch should be executed")
    }

    // ==================== Callback Data Regex Tests ====================

    @Test
    fun `callbackDataRegex should match pattern`() = runTest {
        val routeNode = handling {
            callbackDataRegex(Regex("action_\\d+")) {
                invokedHandlers.add("action")
            }
        }

        val context = createContext(createCallbackQueryEvent(data = "action_123"))
        val result = routeNode.execute(context)

        assertTrue(result)
        assertEquals(listOf("action"), invokedHandlers)
    }

    @Test
    fun `callbackDataRegex should not match non-matching data`() = runTest {
        val routeNode = handling {
            callbackDataRegex(Regex("action_\\d+")) {
                invokedHandlers.add("action")
            }
        }

        val context = createContext(createCallbackQueryEvent(data = "other_data"))
        val result = routeNode.execute(context)

        assertFalse(result)
        assertTrue(invokedHandlers.isEmpty())
    }

    // ==================== Inline Query Regex Tests ====================

    @Test
    fun `inlineQueryRegex should match pattern`() = runTest {
        val routeNode = handling {
            inlineQueryRegex(Regex("search:.*")) {
                invokedHandlers.add("search")
            }
        }

        val context = createContext(createInlineQueryEvent(query = "search: hello"))
        val result = routeNode.execute(context)

        assertTrue(result)
        assertEquals(listOf("search"), invokedHandlers)
    }

    @Test
    fun `inlineQueryRegex should not match non-matching query`() = runTest {
        val routeNode = handling {
            inlineQueryRegex(Regex("search:.*")) {
                invokedHandlers.add("search")
            }
        }

        val context = createContext(createInlineQueryEvent(query = "find something"))
        val result = routeNode.execute(context)

        assertFalse(result)
        assertTrue(invokedHandlers.isEmpty())
    }

    // ==================== Message Type Filter Tests ====================

    private fun createPhotoMessageEvent(): MessageEvent {
        return MessageEvent(
            updateId = 1L,
            message = Message(
                messageId = 1L,
                date = Clock.System.now().epochSeconds,
                chat = testChat,
                from = testUser,
                text = "Photo message",
                photo = listOf(
                    PhotoSize(fileId = "file1", fileUniqueId = "unique1", width = 100, height = 100)
                )
            )
        )
    }

    private fun createVideoMessageEvent(): MessageEvent {
        return MessageEvent(
            updateId = 1L,
            message = Message(
                messageId = 1L,
                date = Clock.System.now().epochSeconds,
                chat = testChat,
                from = testUser,
                text = "Video message",
                video = Video(
                    fileId = "video1",
                    fileUniqueId = "vunique1",
                    width = 640,
                    height = 480,
                    duration = 60L
                )
            )
        )
    }

    private fun createDocumentMessageEvent(): MessageEvent {
        return MessageEvent(
            updateId = 1L,
            message = Message(
                messageId = 1L,
                date = Clock.System.now().epochSeconds,
                chat = testChat,
                from = testUser,
                text = "Document message",
                document = Document(
                    fileId = "doc1",
                    fileUniqueId = "dunique1"
                )
            )
        )
    }

    private fun createStickerMessageEvent(): MessageEvent {
        return MessageEvent(
            updateId = 1L,
            message = Message(
                messageId = 1L,
                date = Clock.System.now().epochSeconds,
                chat = testChat,
                from = testUser,
                sticker = Sticker(
                    fileId = "sticker1",
                    fileUniqueId = "sunique1",
                    type = "regular",
                    width = 512,
                    height = 512,
                    isAnimated = false,
                    isVideo = false
                )
            )
        )
    }

    @Test
    fun `photo handler should match photo messages`() = runTest {
        val routeNode = handling {
            photo {
                invokedHandlers.add("photo")
            }
        }

        val context = createContext(createPhotoMessageEvent())
        val result = routeNode.execute(context)

        assertTrue(result)
        assertEquals(listOf("photo"), invokedHandlers)
    }

    @Test
    fun `photo handler should not match text messages`() = runTest {
        val routeNode = handling {
            photo {
                invokedHandlers.add("photo")
            }
        }

        val context = createContext(createMessageEvent(text = "hello"))
        val result = routeNode.execute(context)

        assertFalse(result)
        assertTrue(invokedHandlers.isEmpty())
    }

    @Test
    fun `video handler should match video messages`() = runTest {
        val routeNode = handling {
            video {
                invokedHandlers.add("video")
            }
        }

        val context = createContext(createVideoMessageEvent())
        val result = routeNode.execute(context)

        assertTrue(result)
        assertEquals(listOf("video"), invokedHandlers)
    }

    @Test
    fun `document handler should match document messages`() = runTest {
        val routeNode = handling {
            document {
                invokedHandlers.add("document")
            }
        }

        val context = createContext(createDocumentMessageEvent())
        val result = routeNode.execute(context)

        assertTrue(result)
        assertEquals(listOf("document"), invokedHandlers)
    }

    @Test
    fun `sticker handler should match sticker messages`() = runTest {
        val routeNode = handling {
            sticker {
                invokedHandlers.add("sticker")
            }
        }

        val context = createContext(createStickerMessageEvent())
        val result = routeNode.execute(context)

        assertTrue(result)
        assertEquals(listOf("sticker"), invokedHandlers)
    }

    // ==================== Chat Type Filter Tests ====================

    @Test
    fun `privateChat handler should match private chat messages`() = runTest {
        val routeNode = handling {
            privateChat {
                invokedHandlers.add("private")
            }
        }

        val context = createContext(createMessageEvent(text = "hello"))
        val result = routeNode.execute(context)

        assertTrue(result)
        assertEquals(listOf("private"), invokedHandlers)
    }

    @Test
    fun `privateChat handler should not match group chat messages`() = runTest {
        val routeNode = handling {
            privateChat {
                invokedHandlers.add("private")
            }
        }

        val groupChat = Chat(id = -100L, type = "group")
        val context = createContext(createMessageEvent(text = "hello", chat = groupChat))
        val result = routeNode.execute(context)

        assertFalse(result)
        assertTrue(invokedHandlers.isEmpty())
    }

    @Test
    fun `groupChat handler should match group chat messages`() = runTest {
        val routeNode = handling {
            groupChat {
                invokedHandlers.add("group")
            }
        }

        val groupChat = Chat(id = -100L, type = "group")
        val context = createContext(createMessageEvent(text = "hello", chat = groupChat))
        val result = routeNode.execute(context)

        assertTrue(result)
        assertEquals(listOf("group"), invokedHandlers)
    }

    @Test
    fun `supergroupChat handler should match supergroup chat messages`() = runTest {
        val routeNode = handling {
            supergroupChat {
                invokedHandlers.add("supergroup")
            }
        }

        val supergroupChat = Chat(id = -100123456L, type = "supergroup")
        val context = createContext(createMessageEvent(text = "hello", chat = supergroupChat))
        val result = routeNode.execute(context)

        assertTrue(result)
        assertEquals(listOf("supergroup"), invokedHandlers)
    }

    @Test
    fun `channel handler should match channel messages`() = runTest {
        val routeNode = handling {
            channel {
                invokedHandlers.add("channel")
            }
        }

        val channelChat = Chat(id = -100123456789L, type = "channel")
        val context = createContext(createMessageEvent(text = "hello", chat = channelChat))
        val result = routeNode.execute(context)

        assertTrue(result)
        assertEquals(listOf("channel"), invokedHandlers)
    }

    // ==================== Reply Message Tests ====================

    private fun createReplyMessageEvent(replyToMessageId: Long = 1L): MessageEvent {
        val originalMessage = Message(
            messageId = replyToMessageId,
            date = Clock.System.now().epochSeconds,
            chat = testChat,
            from = testUser,
            text = "Original message"
        )
        return MessageEvent(
            updateId = 1L,
            message = Message(
                messageId = 2L,
                date = Clock.System.now().epochSeconds,
                chat = testChat,
                from = testUser,
                text = "Reply message",
                replyToMessage = originalMessage
            )
        )
    }

    @Test
    fun `reply handler should match reply messages`() = runTest {
        val routeNode = handling {
            reply {
                invokedHandlers.add("reply")
            }
        }

        val context = createContext(createReplyMessageEvent())
        val result = routeNode.execute(context)

        assertTrue(result)
        assertEquals(listOf("reply"), invokedHandlers)
    }

    @Test
    fun `reply handler should not match non-reply messages`() = runTest {
        val routeNode = handling {
            reply {
                invokedHandlers.add("reply")
            }
        }

        val context = createContext(createMessageEvent(text = "hello"))
        val result = routeNode.execute(context)

        assertFalse(result)
        assertTrue(invokedHandlers.isEmpty())
    }

    @Test
    fun `replyTo handler should match replies to specific message`() = runTest {
        val routeNode = handling {
            replyTo(messageId = 42L) {
                invokedHandlers.add("reply_to_42")
            }
        }

        val originalMessage = Message(
            messageId = 42L,
            date = Clock.System.now().epochSeconds,
            chat = testChat,
            from = testUser,
            text = "Original message"
        )
        val replyEvent = MessageEvent(
            updateId = 1L,
            message = Message(
                messageId = 2L,
                date = Clock.System.now().epochSeconds,
                chat = testChat,
                from = testUser,
                text = "Reply message",
                replyToMessage = originalMessage
            )
        )

        val context = createContext(replyEvent)
        val result = routeNode.execute(context)

        assertTrue(result)
        assertEquals(listOf("reply_to_42"), invokedHandlers)
    }

    @Test
    fun `replyTo handler should not match replies to different message`() = runTest {
        val routeNode = handling {
            replyTo(messageId = 42L) {
                invokedHandlers.add("reply_to_42")
            }
        }

        // Reply to message 99, not 42
        val context = createContext(createReplyMessageEvent(replyToMessageId = 99L))
        val result = routeNode.execute(context)

        assertFalse(result)
        assertTrue(invokedHandlers.isEmpty())
    }
}
