// Auto-generated from Swagger specification, do not modify this file manually
@file:OptIn(InternalSerializationApi::class)

package com.hiczp.telegram.bot.protocol.union

import kotlin.OptIn
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.SerialKind
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonEncoder

/**
 * Serializer for [Union] that tries to deserialize as each type in order.
 *
 * If none of the types match, throws a [SerializationException].
 */
public class UnionSerializer<A, B>(
    private val serializerA: KSerializer<A>,
    private val serializerB: KSerializer<B>,
) : KSerializer<Union<A, B>> {
    override val descriptor: SerialDescriptor =
            buildSerialDescriptor("com.hiczp.telegram.bot.protocol.union.Union", SerialKind.CONTEXTUAL)

    override fun deserialize(decoder: Decoder): Union<A, B> {
        require(decoder is JsonDecoder) { "Only JSON is supported" }
        val jsonElement = decoder.decodeJsonElement()

        runCatching {
            Union.First(decoder.json.decodeFromJsonElement(serializerA, jsonElement))
        }.getOrNull()?.let { return it }
        runCatching {
            Union.Second(decoder.json.decodeFromJsonElement(serializerB, jsonElement))
        }.getOrNull()?.let { return it }
        val typeNames = listOf(serializerA.descriptor.serialName, serializerB.descriptor.serialName).joinToString(", ")
        throw SerializationException("Could not deserialize Union<$typeNames>")
    }

    override fun serialize(encoder: Encoder, `value`: Union<A, B>) {
        require(encoder is JsonEncoder) { "Only JSON is supported" }
        when (value) {
            is Union.First -> encoder.encodeSerializableValue(serializerA, value.value)
            is Union.Second -> encoder.encodeSerializableValue(serializerB, value.value)
        }
    }
}
