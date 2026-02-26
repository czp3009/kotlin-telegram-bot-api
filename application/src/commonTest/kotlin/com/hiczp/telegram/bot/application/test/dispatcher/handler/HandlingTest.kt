package com.hiczp.telegram.bot.application.test.dispatcher.handler

import com.hiczp.telegram.bot.application.context.ProvidedUserTelegramBotEventContext
import com.hiczp.telegram.bot.application.context.TelegramBotEventContext
import com.hiczp.telegram.bot.application.dispatcher.handler.*
import com.hiczp.telegram.bot.client.TelegramBotClient
import com.hiczp.telegram.bot.protocol.event.CallbackQueryEvent
import com.hiczp.telegram.bot.protocol.event.InlineQueryEvent
import com.hiczp.telegram.bot.protocol.event.MessageEvent
import com.hiczp.telegram.bot.protocol.event.TelegramBotEvent
import com.hiczp.telegram.bot.protocol.model.*
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
            command("start") {
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
            command("start") {
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
            command("start") {
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
            command("start") {
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
            command("START") {
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
            command("start") {
                invokedHandlers.add("start")
            }
            command("help") {
                invokedHandlers.add("help")
            }
            command("stop") {
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
    fun `regex handler should match pattern`() = runTest {
        val routeNode = handling {
            regex(Regex("(?i)hello.*")) {
                invokedHandlers.add("hello_regex")
            }
        }

        val context = createContext(createMessageEvent(text = "Hello World"))
        val result = routeNode.execute(context)

        assertTrue(result)
        assertEquals(listOf("hello_regex"), invokedHandlers)
    }

    @Test
    fun `regex handler should not match non-matching text`() = runTest {
        val routeNode = handling {
            regex(Regex("^hello")) {
                invokedHandlers.add("hello_regex")
            }
        }

        val context = createContext(createMessageEvent(text = "Say hello"))
        val result = routeNode.execute(context)

        assertFalse(result)
        assertTrue(invokedHandlers.isEmpty())
    }

    @Test
    fun `regex handler should match complex pattern`() = runTest {
        val routeNode = handling {
            regex(Regex(".*\\d{4}-\\d{2}-\\d{2}.*")) {
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

    // ==================== Event Type Filtering Tests ====================

    @Test
    fun `on filter should route by event type`() = runTest {
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
    fun `on with predicate should filter events`() = runTest {
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

    // ==================== Filter Tests ====================

    @Test
    fun `filter should allow custom predicates`() = runTest {
        val routeNode = handling {
            on<MessageEvent> {
                filter({ (it.event.message.text?.length ?: 0) > 5 }) {
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

    // ==================== Include Tests ====================

    @Test
    fun `include should merge route nodes`() = runTest {
        val adminRoutes = handling {
            command("admin") {
                invokedHandlers.add("admin")
            }
        }

        val mainRoutes = handling {
            command("start") {
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
    fun `should short-circuit on first matching handler`() = runTest {
        val routeNode = handling {
            on<MessageEvent> {
                filter({ true }) {
                    handle { invokedHandlers.add("first") }
                }
                filter({ true }) {
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
                filter({ false }) {
                    handle { invokedHandlers.add("filtered_out") }
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
                filter({ it.event.message.chat.id == 100L }) {
                    command("admin") {
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

        // Test command not matching filter
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
            command("start") {
                invokedHandlers.add("start")
            }
        }

        val context = createContext(createMessageEvent(text = "random text"))
        val result = routeNode.execute(context)

        assertFalse(result)
        assertTrue(invokedHandlers.isEmpty())
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
            command("start") {
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
}
