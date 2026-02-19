package com.hiczp.telegram.bot.client.test.event

import com.hiczp.telegram.bot.client.event.MessageEvent
import com.hiczp.telegram.bot.client.event.toTelegramBotEvent
import com.hiczp.telegram.bot.protocol.constant.ChatType
import com.hiczp.telegram.bot.protocol.model.Chat
import com.hiczp.telegram.bot.protocol.model.Message
import com.hiczp.telegram.bot.protocol.model.Update
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class TelegramBotEventTest {
    @Test
    fun constructSubClass() {
        MessageEvent(1, Message(messageId = 1, date = 0L, chat = Chat(id = 1, type = ChatType.PRIVATE)))
    }

    @Test
    fun dispatch() {
        val message = Message(messageId = 1, date = 0L, chat = Chat(id = 1, type = ChatType.PRIVATE))
        val update = Update(updateId = 1, message = message)
        val event = update.toTelegramBotEvent()
        assertIs<MessageEvent>(event)
        assertEquals(message, event.message)
    }
}
