package com.hiczp.telegram.bot.application.test.dispatcher.handler

import com.hiczp.telegram.bot.application.context.ProvidedUserTelegramBotEventContext
import com.hiczp.telegram.bot.application.context.TelegramBotEventContext
import com.hiczp.telegram.bot.application.dispatcher.handler.handling
import com.hiczp.telegram.bot.application.dispatcher.handler.include
import com.hiczp.telegram.bot.application.dispatcher.handler.matcher.message
import com.hiczp.telegram.bot.application.dispatcher.handler.matcher.text
import com.hiczp.telegram.bot.client.TelegramBotClient
import com.hiczp.telegram.bot.protocol.event.CallbackQueryEvent
import com.hiczp.telegram.bot.protocol.event.MessageEvent
import com.hiczp.telegram.bot.protocol.event.TelegramBotEvent
import com.hiczp.telegram.bot.protocol.model.CallbackQuery
import com.hiczp.telegram.bot.protocol.model.Chat
import com.hiczp.telegram.bot.protocol.model.Message
import com.hiczp.telegram.bot.protocol.model.User
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.time.Clock

class HandlerDslTest {
    private val botUser = User(id = 1L, isBot = true, firstName = "Bot", username = "test_bot")
    private val user = User(id = 2L, isBot = false, firstName = "User")
    private val client = TelegramBotClient(botToken = "xxx")
    private val applicationScope = TestScope()

    private fun context(event: TelegramBotEvent): TelegramBotEventContext<TelegramBotEvent> =
        ProvidedUserTelegramBotEventContext(
            client = client,
            event = event,
            applicationScope = applicationScope,
            botUser = botUser
        )

    private fun message(text: String): MessageEvent =
        MessageEvent(
            updateId = 1L,
            message = Message(
                messageId = 1L,
                date = Clock.System.now().epochSeconds,
                chat = Chat(id = 100L, type = "private"),
                from = user,
                text = text
            )
        )

    private fun callback(data: String): CallbackQueryEvent =
        CallbackQueryEvent(
            updateId = 1L,
            callbackQuery = CallbackQuery(
                id = "callback",
                from = user,
                chatInstance = "chat-instance",
                data = data
            )
        )

    @Test
    fun `filter should backtrack through all ancestors`() = runTest {
        val trace = mutableListOf<String>()
        val route = handling {
            filter({
                trace.add("A")
                true
            }) {
                filter({
                    trace.add("B")
                    true
                }) {
                    filter({
                        trace.add("C")
                        false
                    }) {
                        handle { trace.add("unreachable") }
                    }
                }
            }

            filter({
                trace.add("D")
                true
            }) {
                filter({
                    trace.add("E")
                    true
                }) {
                    filter({
                        trace.add("F")
                        true
                    }) {
                        handle { trace.add("handled") }
                    }
                }
            }
        }

        assertTrue(route.execute(context(message("hello"))))
        assertEquals(listOf("A", "B", "C", "D", "E", "F", "handled"), trace)
    }

    @Test
    fun `type filter should narrow event type`() = runTest {
        val invoked = mutableListOf<String>()
        val route = handling {
            message {
                text("hello") {
                    handle {
                        invoked.add(event.message.text!!)
                    }
                }
            }
        }

        assertTrue(route.execute(context(message("hello"))))
        assertEquals(listOf("hello"), invoked)

        invoked.clear()
        assertFalse(route.execute(context(callback("hello"))))
        assertTrue(invoked.isEmpty())
    }

    @Test
    fun `parent handle should act as branch fallback`() = runTest {
        val invoked = mutableListOf<String>()
        val route = handling {
            message {
                text("known") {
                    handle { invoked.add("known") }
                }
                handle { invoked.add("message-fallback") }
            }
        }

        assertTrue(route.execute(context(message("other"))))
        assertEquals(listOf("message-fallback"), invoked)
    }

    @Test
    fun `route block should run at build time and handle should run at dispatch time`() = runTest {
        var buildCount = 0
        var handleCount = 0

        val route = handling {
            buildCount++
            message {
                buildCount++
                handle { handleCount++ }
            }
        }

        assertEquals(2, buildCount)
        assertEquals(0, handleCount)

        route.execute(context(message("first")))
        route.execute(context(message("second")))

        assertEquals(2, buildCount)
        assertEquals(2, handleCount)
    }

    @Test
    fun `filter predicate should be suspend`() = runTest {
        val allowed = CompletableDeferred<Boolean>()
        val invoked = mutableListOf<String>()
        val route = handling {
            filter({ allowed.await() }) {
                handle { invoked.add("handled") }
            }
        }

        val job = launch {
            assertTrue(route.execute(context(message("hello"))))
        }
        allowed.complete(true)
        job.join()

        assertEquals(listOf("handled"), invoked)
    }

    @Test
    fun `include should mount another route tree`() = runTest {
        val invoked = mutableListOf<String>()
        val child = handling {
            message {
                text("child") {
                    handle { invoked.add("child") }
                }
            }
        }
        val route = handling {
            include(child)
        }

        assertTrue(route.execute(context(message("child"))))
        assertEquals(listOf("child"), invoked)
    }
}
