package com.hiczp.telegram.bot.protocol.test.event

import com.hiczp.telegram.bot.protocol.constant.ChatType
import com.hiczp.telegram.bot.protocol.event.MessageEvent
import com.hiczp.telegram.bot.protocol.event.TelegramBotEvent
import com.hiczp.telegram.bot.protocol.event.toTelegramBotEvent
import com.hiczp.telegram.bot.protocol.event.toUpdate
import com.hiczp.telegram.bot.protocol.model.Chat
import com.hiczp.telegram.bot.protocol.model.Message
import com.hiczp.telegram.bot.protocol.model.Update
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

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

    @Test
    fun polymorphicSerialization() {
        val message = Message(messageId = 1, date = 0L, chat = Chat(id = 1, type = ChatType.PRIVATE))
        val event = MessageEvent(updateId = 1, message = message)

        val json = Json.encodeToString<TelegramBotEvent>(event)
        println("Serialized event: $json")

        // Verify the JSON contains the type discriminator
        assertTrue(json.contains("\"type\":\"message\""), "JSON should contain type discriminator")

        // Deserialize back
        val deserialized = Json.decodeFromString<TelegramBotEvent>(json)
        assertIs<MessageEvent>(deserialized)
        assertEquals(1L, deserialized.updateId)
        assertEquals(message, deserialized.message)
    }

    @Test
    fun toUpdate() {
        val message = Message(messageId = 1, date = 0L, chat = Chat(id = 1, type = ChatType.PRIVATE))
        val event = MessageEvent(updateId = 1, message = message)

        val update = event.toUpdate()

        assertEquals(1L, update.updateId)
        assertEquals(message, update.message)
        // All other fields should be null
        assertEquals(null, update.editedMessage)
        assertEquals(null, update.channelPost)
        assertEquals(null, update.callbackQuery)
    }

    @Test
    fun roundTrip() {
        val originalMessage = Message(messageId = 1, date = 0L, chat = Chat(id = 1, type = ChatType.PRIVATE))
        val originalUpdate = Update(updateId = 1, message = originalMessage)

        // Update -> Event -> Update
        val event = originalUpdate.toTelegramBotEvent()
        assertIs<MessageEvent>(event)
        val roundTripUpdate = event.toUpdate()

        assertEquals(originalUpdate.updateId, roundTripUpdate.updateId)
        assertEquals(originalUpdate.message, roundTripUpdate.message)
    }
}
