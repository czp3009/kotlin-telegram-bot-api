package com.hiczp.telegram.bot.protocol.test.model

import com.hiczp.telegram.bot.protocol.constant.ChatType
import com.hiczp.telegram.bot.protocol.model.Chat
import com.hiczp.telegram.bot.protocol.model.ChatMember
import com.hiczp.telegram.bot.protocol.model.ChatMemberMember
import com.hiczp.telegram.bot.protocol.model.InaccessibleMessage
import com.hiczp.telegram.bot.protocol.model.InlineQueryResult
import com.hiczp.telegram.bot.protocol.model.InlineQueryResultPhoto
import com.hiczp.telegram.bot.protocol.model.MaybeInaccessibleMessage
import com.hiczp.telegram.bot.protocol.model.User
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

class UnionParentPropertiesTest {
    private val json = Json
    private val user = User(id = 1L, isBot = false, firstName = "User")
    private val chat = Chat(id = 100L, type = ChatType.GROUP)

    @Test
    fun defaultPolymorphicParentPropertiesDoNotAddFields() {
        val member: ChatMember = ChatMemberMember(user = user)
        val encoded = json.encodeToJsonElement<ChatMember>(member).jsonObject

        assertEquals(setOf("status", "user"), encoded.keys)
        assertEquals("member", encoded["status"]?.jsonPrimitive?.content)

        val decoded = json.decodeFromJsonElement<ChatMember>(encoded)
        assertIs<ChatMemberMember>(decoded)
        assertEquals(user, decoded.user)
    }

    @Test
    fun customParentPropertiesDoNotAddFields() {
        val message: MaybeInaccessibleMessage = InaccessibleMessage(
            chat = chat,
            messageId = 42L,
            date = 0L,
        )

        val encodedAsParent = json.encodeToJsonElement<MaybeInaccessibleMessage>(message).jsonObject
        val encodedAsConcrete = json.encodeToJsonElement(message as InaccessibleMessage).jsonObject

        assertEquals(encodedAsConcrete, encodedAsParent)
        assertEquals(setOf("chat", "message_id", "date"), encodedAsParent.keys)
    }

    @Test
    fun duplicatedDiscriminatorCustomSerializerKeepsConcreteShape() {
        val result: InlineQueryResult = InlineQueryResultPhoto(
            id = "photo",
            photoUrl = "https://example.com/photo.jpg",
            thumbnailUrl = "https://example.com/thumb.jpg",
        )

        val encodedAsParent = json.encodeToJsonElement<InlineQueryResult>(result).jsonObject
        val encodedAsConcrete = json.encodeToJsonElement(result as InlineQueryResultPhoto).jsonObject

        assertEquals(encodedAsConcrete, encodedAsParent)
        assertEquals(setOf("type", "id", "photo_url", "thumbnail_url"), encodedAsParent.keys)
        assertEquals("photo", encodedAsParent["type"]?.jsonPrimitive?.content)
    }
}
