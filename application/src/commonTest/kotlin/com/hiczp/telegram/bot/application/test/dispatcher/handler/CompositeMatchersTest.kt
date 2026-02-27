package com.hiczp.telegram.bot.application.test.dispatcher.handler

import com.hiczp.telegram.bot.application.context.ProvidedUserTelegramBotEventContext
import com.hiczp.telegram.bot.application.context.TelegramBotEventContext
import com.hiczp.telegram.bot.application.dispatcher.handler.handling
import com.hiczp.telegram.bot.application.dispatcher.handler.matcher.*
import com.hiczp.telegram.bot.client.TelegramBotClient
import com.hiczp.telegram.bot.protocol.event.MessageEvent
import com.hiczp.telegram.bot.protocol.event.TelegramBotEvent
import com.hiczp.telegram.bot.protocol.model.Chat
import com.hiczp.telegram.bot.protocol.model.Message
import com.hiczp.telegram.bot.protocol.model.User
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import kotlin.test.*
import kotlin.time.Clock

class CompositeMatchersTest {
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
        chatId: Long = 100L,
        userId: Long = 1L,
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

    @Test
    fun `allOf should match when all predicates are true`() = runTest {
        val routeNode = handling {
            on<MessageEvent> {
                allOf(
                    { it.event.message.text != null },
                    { it.event.message.chat.id == 100L },
                    { it.event.message.from?.id == 1L }
                ) {
                    handle { invokedHandlers.add("all_matched") }
                }
            }
        }

        val context = createContext(createMessageEvent(text = "hello"))
        val result = routeNode.execute(context)

        assertTrue(result)
        assertEquals(listOf("all_matched"), invokedHandlers)
    }

    @Test
    fun `allOf should not match when any predicate is false`() = runTest {
        val routeNode = handling {
            on<MessageEvent> {
                allOf(
                    { it.event.message.text != null },
                    { it.event.message.chat.id == 999L },
                    { it.event.message.from?.id == 1L }
                ) {
                    handle { invokedHandlers.add("all_matched") }
                }
            }
        }

        val context = createContext(createMessageEvent(text = "hello"))
        val result = routeNode.execute(context)

        assertFalse(result)
        assertTrue(invokedHandlers.isEmpty())
    }

    @Test
    fun `allOf with empty predicates should match`() = runTest {
        val routeNode = handling {
            on<MessageEvent> {
                allOf {
                    handle { invokedHandlers.add("empty_allOf") }
                }
            }
        }

        val context = createContext(createMessageEvent(text = "hello"))
        val result = routeNode.execute(context)

        assertTrue(result)
        assertEquals(listOf("empty_allOf"), invokedHandlers)
    }

    @Test
    fun `anyOf should match when any predicate is true`() = runTest {
        val routeNode = handling {
            on<MessageEvent> {
                anyOf(
                    { it.event.message.chat.id == 999L },
                    { it.event.message.text?.contains("hello") == true },
                    { it.event.message.from?.id == 999L }
                ) {
                    handle { invokedHandlers.add("any_matched") }
                }
            }
        }

        val context = createContext(createMessageEvent(text = "hello world"))
        val result = routeNode.execute(context)

        assertTrue(result)
        assertEquals(listOf("any_matched"), invokedHandlers)
    }

    @Test
    fun `anyOf should not match when all predicates are false`() = runTest {
        val routeNode = handling {
            on<MessageEvent> {
                anyOf(
                    { it.event.message.chat.id == 999L },
                    { it.event.message.text?.contains("xyz") == true },
                    { it.event.message.from?.id == 999L }
                ) {
                    handle { invokedHandlers.add("any_matched") }
                }
            }
        }

        val context = createContext(createMessageEvent(text = "hello world"))
        val result = routeNode.execute(context)

        assertFalse(result)
        assertTrue(invokedHandlers.isEmpty())
    }

    @Test
    fun `anyOf with empty predicates should not match`() = runTest {
        val routeNode = handling {
            on<MessageEvent> {
                anyOf {
                    handle { invokedHandlers.add("empty_anyOf") }
                }
            }
        }

        val context = createContext(createMessageEvent(text = "hello"))
        val result = routeNode.execute(context)

        assertFalse(result)
        assertTrue(invokedHandlers.isEmpty())
    }

    @Test
    fun `not should match when predicate is false`() = runTest {
        val routeNode = handling {
            on<MessageEvent> {
                not({ it.event.message.text?.startsWith("/") == true }) {
                    handle { invokedHandlers.add("not_command") }
                }
            }
        }

        val context = createContext(createMessageEvent(text = "hello"))
        val result = routeNode.execute(context)

        assertTrue(result)
        assertEquals(listOf("not_command"), invokedHandlers)
    }

    @Test
    fun `not should not match when predicate is true`() = runTest {
        val routeNode = handling {
            on<MessageEvent> {
                not({ it.event.message.text?.startsWith("/") == true }) {
                    handle { invokedHandlers.add("not_command") }
                }
            }
        }

        val context = createContext(createMessageEvent(text = "/start"))
        val result = routeNode.execute(context)

        assertFalse(result)
        assertTrue(invokedHandlers.isEmpty())
    }

    @Test
    fun `whenAllOf should combine allOf with handle`() = runTest {
        val routeNode = handling {
            on<MessageEvent> {
                whenAllOf(
                    { it.event.message.text != null },
                    { it.event.message.chat.id == 100L }
                ) { ctx ->
                    invokedHandlers.add("whenAllOf:${ctx.event.message.text}")
                }
            }
        }

        val context = createContext(createMessageEvent(text = "hello"))
        val result = routeNode.execute(context)

        assertTrue(result)
        assertEquals(listOf("whenAllOf:hello"), invokedHandlers)
    }

    @Test
    fun `whenAnyOf should combine anyOf with handle`() = runTest {
        val routeNode = handling {
            on<MessageEvent> {
                whenAnyOf(
                    { it.event.message.text?.contains("hello") == true },
                    { it.event.message.text?.contains("world") == true }
                ) { ctx ->
                    invokedHandlers.add("whenAnyOf:${ctx.event.message.text}")
                }
            }
        }

        val context = createContext(createMessageEvent(text = "hello there"))
        val result = routeNode.execute(context)

        assertTrue(result)
        assertEquals(listOf("whenAnyOf:hello there"), invokedHandlers)
    }

    @Test
    fun `whenNot should combine not with handle`() = runTest {
        val routeNode = handling {
            on<MessageEvent> {
                whenNot({ it.event.message.text?.length?.let { l -> l > 10 } == true }) { ctx ->
                    invokedHandlers.add("short_message:${ctx.event.message.text}")
                }
            }
        }

        // Short message should match
        var context = createContext(createMessageEvent(text = "hi"))
        assertTrue(routeNode.execute(context))
        assertEquals(listOf("short_message:hi"), invokedHandlers)

        // Long message should not match
        resetTracking()
        context = createContext(createMessageEvent(text = "this is a long message"))
        assertFalse(routeNode.execute(context))
        assertTrue(invokedHandlers.isEmpty())
    }

    @Test
    fun `allOf combined with inlined conditions`() = runTest {
        // Match: has text AND (contains "hello" OR contains "hi") AND NOT a command
        // We inline the OR and NOT conditions into the allOf predicates
        val routeNode = handling {
            on<MessageEvent> {
                allOf(
                    { it.event.message.text != null },
                    { ctx ->
                        val text = ctx.event.message.text
                        text?.contains("hello", ignoreCase = true) == true ||
                                text?.contains("hi", ignoreCase = true) == true
                    },
                    { it.event.message.text?.startsWith("/") != true }
                ) {
                    handle { ctx ->
                        invokedHandlers.add("greeting:${ctx.event.message.text}")
                    }
                }
            }
        }

        // Should match - contains "hello" and not a command
        var context = createContext(createMessageEvent(text = "Hello there!"))
        assertTrue(routeNode.execute(context))
        assertEquals(listOf("greeting:Hello there!"), invokedHandlers)

        // Should match - contains "hi" and not a command
        resetTracking()
        context = createContext(createMessageEvent(text = "Hi, how are you?"))
        assertTrue(routeNode.execute(context))
        assertEquals(listOf("greeting:Hi, how are you?"), invokedHandlers)

        // Should not match - is a command
        resetTracking()
        context = createContext(createMessageEvent(text = "/hello"))
        assertFalse(routeNode.execute(context))
        assertTrue(invokedHandlers.isEmpty())

        // Should not match - doesn't contain greeting
        resetTracking()
        context = createContext(createMessageEvent(text = "Goodbye!"))
        assertFalse(routeNode.execute(context))
        assertTrue(invokedHandlers.isEmpty())
    }

    @Test
    fun `composite matchers with fallback handle`() = runTest {
        val routeNode = handling {
            on<MessageEvent> {
                allOf(
                    { it.event.message.text != null },
                    { it.event.message.chat.id == 100L }
                ) {
                    handle { invokedHandlers.add("matched") }
                }
                handle { invokedHandlers.add("fallback") }
            }
        }

        // Should match allOf
        var context = createContext(createMessageEvent(text = "hello"))
        assertTrue(routeNode.execute(context))
        assertEquals(listOf("matched"), invokedHandlers)

        // Should fall through to handle (different chat)
        resetTracking()
        context = createContext(createMessageEvent(text = "hello", chatId = 999L))
        assertTrue(routeNode.execute(context))
        assertEquals(listOf("fallback"), invokedHandlers)
    }

    @Test
    fun `composite matchers at root level`() = runTest {
        val routeNode = handling {
            anyOf(
                { ctx -> ctx.event is MessageEvent },
                { ctx -> ctx.event is MessageEvent && (ctx.event as MessageEvent).message.text?.contains("test") == true }
            ) {
                handle { invokedHandlers.add("root_composite") }
            }
        }

        val context = createContext(createMessageEvent(text = "hello"))
        val result = routeNode.execute(context)

        assertTrue(result)
        assertEquals(listOf("root_composite"), invokedHandlers)
    }

    @Test
    fun `chained composite matchers`() = runTest {
        val routeNode = handling {
            on<MessageEvent> {
                // First check: must have text
                allOf({ it.event.message.text != null }) {
                    // Second check: must be private chat or from specific user
                    anyOf(
                        { it.event.message.chat.id == 100L },
                        { it.event.message.from?.id == 1L }
                    ) {
                        // Third check: must not be a command
                        not({ it.event.message.text?.startsWith("/") == true }) {
                            handle { ctx ->
                                invokedHandlers.add("chained:${ctx.event.message.text}")
                            }
                        }
                    }
                }
            }
        }

        // Should match all conditions
        val context = createContext(createMessageEvent(text = "hello"))
        val result = routeNode.execute(context)

        assertTrue(result)
        assertEquals(listOf("chained:hello"), invokedHandlers)
    }
}
