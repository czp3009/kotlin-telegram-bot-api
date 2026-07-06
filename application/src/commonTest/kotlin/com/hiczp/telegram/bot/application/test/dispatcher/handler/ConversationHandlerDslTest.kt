package com.hiczp.telegram.bot.application.test.dispatcher.handler

import com.hiczp.telegram.bot.application.context.ProvidedUserTelegramBotEventContext
import com.hiczp.telegram.bot.application.context.TelegramBotEventContext
import com.hiczp.telegram.bot.application.context.extractChatId
import com.hiczp.telegram.bot.application.context.extractUserId
import com.hiczp.telegram.bot.application.dispatcher.handler.RouteNode
import com.hiczp.telegram.bot.application.dispatcher.handler.handling
import com.hiczp.telegram.bot.application.dispatcher.handler.matcher.message
import com.hiczp.telegram.bot.application.dispatcher.handler.matcher.text
import com.hiczp.telegram.bot.application.interceptor.TelegramEventProcessor
import com.hiczp.telegram.bot.application.interceptor.builtin.conversation.*
import com.hiczp.telegram.bot.client.TelegramBotClient
import com.hiczp.telegram.bot.protocol.event.MessageEvent
import com.hiczp.telegram.bot.protocol.event.TelegramBotEvent
import com.hiczp.telegram.bot.protocol.model.Chat
import com.hiczp.telegram.bot.protocol.model.Message
import com.hiczp.telegram.bot.protocol.model.User
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.time.Clock

class ConversationHandlerDslTest {
    private val botUser = User(id = 1L, isBot = true, firstName = "Bot", username = "test_bot")
    private val user = User(id = 2L, isBot = false, firstName = "User")
    private val client = TelegramBotClient(botToken = "xxx")

    private fun context(scope: TestScope, text: String, messageId: Long): TelegramBotEventContext<TelegramBotEvent> =
        ProvidedUserTelegramBotEventContext(
            client = client,
            event = MessageEvent(
                updateId = messageId,
                message = Message(
                    messageId = messageId,
                    date = Clock.System.now().epochSeconds,
                    chat = Chat(id = 100L, type = "private"),
                    from = user,
                    text = text
                )
            ),
            applicationScope = scope,
            botUser = botUser
        )

    private suspend fun dispatch(
        route: RouteNode,
        manager: ConversationManager,
        context: TelegramBotEventContext<TelegramBotEvent>,
    ) {
        val processor = object : TelegramEventProcessor {
            override suspend fun process(context: TelegramBotEventContext<TelegramBotEvent>) {
                route.execute(context)
            }
        }
        conversationInterceptor(manager).invoke(processor, context)
    }

    @Test
    fun `conversationCommand should resume active conversation before normal handlers`() = runTest {
        val manager = ConversationManager()
        val conversationMessages = mutableListOf<String>()
        val normalMessages = mutableListOf<String>()
        val route = handling {
            conversationCommand("survey") {
                conversationMessages.add(awaitText())
            }
            message {
                text("hello") {
                    handle { normalMessages.add("hello") }
                }
            }
        }

        dispatch(route, manager, context(this, "/survey@test_bot", messageId = 1L))
        advanceUntilIdle()

        assertTrue(manager.activeConversations.isNotEmpty())

        dispatch(route, manager, context(this, "hello", messageId = 2L))
        advanceUntilIdle()

        assertEquals(listOf("hello"), conversationMessages)
        assertTrue(normalMessages.isEmpty())
        assertTrue(manager.activeConversations.isEmpty())
    }

    @Test
    fun `conversation should backtrack when start predicate does not match`() = runTest {
        val manager = ConversationManager()
        val normalMessages = mutableListOf<String>()
        val route = handling {
            conversation(
                id = {
                    ConversationId(
                        chatId = event.extractChatId()!!,
                        userId = event.extractUserId()
                    )
                },
                start = {
                    val currentEvent = event
                    currentEvent is MessageEvent && currentEvent.message.text == "/survey"
                },
            ) {
                awaitText()
            }
            message {
                text("hello") {
                    handle { normalMessages.add("hello") }
                }
            }
        }

        dispatch(route, manager, context(this, "hello", messageId = 1L))
        advanceUntilIdle()

        assertEquals(listOf("hello"), normalMessages)
        assertTrue(manager.activeConversations.isEmpty())
    }

    @Test
    fun `nested conversation starter should use parent filters only for start event`() = runTest {
        val manager = ConversationManager()
        val conversationMessages = mutableListOf<String>()
        val normalMessages = mutableListOf<String>()
        val route = handling {
            message {
                text("/survey") {
                    conversation {
                        conversationMessages.add(awaitText())
                    }
                }
                text("hello") {
                    handle { normalMessages.add("hello") }
                }
            }
        }

        dispatch(route, manager, context(this, "/survey", messageId = 1L))
        advanceUntilIdle()

        assertTrue(manager.activeConversations.isNotEmpty())

        dispatch(route, manager, context(this, "hello", messageId = 2L))
        advanceUntilIdle()

        assertEquals(listOf("hello"), conversationMessages)
        assertTrue(normalMessages.isEmpty())
        assertTrue(manager.activeConversations.isEmpty())
    }
}
