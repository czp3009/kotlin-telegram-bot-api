// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.model

import kotlin.Long
import kotlin.OptIn
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.SerialKind
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.decodeFromJsonElement

/**
 * This object describes a message that can be inaccessible to the bot. It can be one of
 * Message InaccessibleMessage
 */
@Serializable(with = MaybeInaccessibleMessageSerializer::class)
public sealed interface MaybeInaccessibleMessage {
    public val chat: Chat

    public val date: Long

    public val messageId: Long
}

@OptIn(InternalSerializationApi::class)
public object MaybeInaccessibleMessageSerializer : KSerializer<MaybeInaccessibleMessage> {
    override val descriptor: SerialDescriptor =
            buildSerialDescriptor("com.hiczp.telegram.bot.protocol.model.MaybeInaccessibleMessage", SerialKind.CONTEXTUAL)

    override fun deserialize(decoder: Decoder): MaybeInaccessibleMessage {
        require(decoder is JsonDecoder) { "Only JSON is supported" }
        val jsonElement = decoder.decodeJsonElement()
        if (jsonElement is JsonObject) {
            if ((jsonElement["date"] as? JsonPrimitive)?.content == "0") {
                runCatching { decoder.json.decodeFromJsonElement(InaccessibleMessage.serializer(), jsonElement) }.getOrNull()?.let { return it }
            }
            runCatching { decoder.json.decodeFromJsonElement(Message.serializer(), jsonElement) }.getOrNull()?.let { return it }
        }
        throw SerializationException("Could not deserialize MaybeInaccessibleMessage")
    }

    override fun serialize(encoder: Encoder, `value`: MaybeInaccessibleMessage) {
        require(encoder is JsonEncoder) { "Only JSON is supported" }
        when (value) {
            is Message -> encoder.encodeSerializableValue(Message.serializer(), value)
            is InaccessibleMessage -> encoder.encodeSerializableValue(InaccessibleMessage.serializer(), value)
            else -> throw SerializationException("Unsupported MaybeInaccessibleMessage implementation")
        }
    }
}
