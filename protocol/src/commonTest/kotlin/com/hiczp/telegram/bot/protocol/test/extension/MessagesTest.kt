package com.hiczp.telegram.bot.protocol.test.extension

import com.hiczp.telegram.bot.protocol.constant.ChatType
import com.hiczp.telegram.bot.protocol.extension.isInaccessible
import com.hiczp.telegram.bot.protocol.extension.largestPhoto
import com.hiczp.telegram.bot.protocol.extension.largestPhotoSize
import com.hiczp.telegram.bot.protocol.extension.toReplyParameters
import com.hiczp.telegram.bot.protocol.model.Chat
import com.hiczp.telegram.bot.protocol.model.InaccessibleMessage
import com.hiczp.telegram.bot.protocol.model.Message
import com.hiczp.telegram.bot.protocol.model.MessageEntity
import com.hiczp.telegram.bot.protocol.model.PhotoSize
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class MessagesTest {
    private val chat = Chat(id = 100L, type = ChatType.PRIVATE)

    @Test
    fun largestPhotoSizePrefersPixelAreaAndThenFileSize() {
        val small = PhotoSize(fileId = "small", fileUniqueId = "u1", width = 100L, height = 100L, fileSize = 10L)
        val sameAreaSmallerFile = PhotoSize(fileId = "same1", fileUniqueId = "u2", width = 200L, height = 100L, fileSize = 20L)
        val sameAreaLargerFile = PhotoSize(fileId = "same2", fileUniqueId = "u3", width = 100L, height = 200L, fileSize = 30L)
        val large = PhotoSize(fileId = "large", fileUniqueId = "u4", width = 300L, height = 200L, fileSize = null)

        assertEquals(large, listOf(small, sameAreaSmallerFile, sameAreaLargerFile, large).largestPhotoSize)
        assertEquals(sameAreaLargerFile, listOf(sameAreaSmallerFile, sameAreaLargerFile).largestPhotoSize)
        assertNull(emptyList<PhotoSize>().largestPhotoSize)
    }

    @Test
    fun messageLargestPhotoUsesPhotoSizes() {
        val small = PhotoSize(fileId = "small", fileUniqueId = "u1", width = 100L, height = 100L)
        val large = PhotoSize(fileId = "large", fileUniqueId = "u2", width = 200L, height = 200L)

        val message = Message(messageId = 1L, date = 1L, chat = chat, photo = listOf(small, large))

        assertEquals(large, message.largestPhoto)
        assertNull(Message(messageId = 2L, date = 1L, chat = chat).largestPhoto)
    }

    @Test
    fun isInaccessibleUsesParentDate() {
        assertFalse(Message(messageId = 1L, date = 1L, chat = chat).isInaccessible)
        assertTrue(InaccessibleMessage(chat = chat, messageId = 2L, date = 0L).isInaccessible)
    }

    @Test
    fun toReplyParametersTargetsMessage() {
        val message = Message(messageId = 42L, date = 1L, chat = chat)
        val quoteEntity = MessageEntity(type = "bold", offset = 0L, length = 2L)

        val replyParameters = message.toReplyParameters(
            includeChatId = true,
            allowSendingWithoutReply = true,
            quote = "hi",
            quoteParseMode = "HTML",
            quoteEntities = listOf(quoteEntity),
            quotePosition = 3L,
            checklistTaskId = 4L,
            pollOptionId = "option",
        )

        assertEquals(42L, replyParameters.messageId)
        assertEquals("100", replyParameters.chatId)
        assertEquals(true, replyParameters.allowSendingWithoutReply)
        assertEquals("hi", replyParameters.quote)
        assertEquals("HTML", replyParameters.quoteParseMode)
        assertEquals(listOf(quoteEntity), replyParameters.quoteEntities)
        assertEquals(3L, replyParameters.quotePosition)
        assertEquals(4L, replyParameters.checklistTaskId)
        assertEquals("option", replyParameters.pollOptionId)
    }

    @Test
    fun toReplyParametersOmitsChatIdByDefault() {
        val replyParameters = Message(messageId = 42L, date = 1L, chat = chat).toReplyParameters()

        assertEquals(42L, replyParameters.messageId)
        assertNull(replyParameters.chatId)
    }
}
