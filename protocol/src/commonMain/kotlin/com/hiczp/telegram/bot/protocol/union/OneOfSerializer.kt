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
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonEncoder

/**
 * Serializer for [OneOf] that tries to deserialize as each type in order.
 *
 * If none of the types match, throws a [SerializationException].
 */
public class OneOfSerializer<A, B>(
  private val serializerA: KSerializer<A>,
  private val serializerB: KSerializer<B>,
) : KSerializer<OneOf<A, B>> {
  override val descriptor: SerialDescriptor = buildSerialDescriptor("OneOf", SerialKind.CONTEXTUAL)

  override fun deserialize(decoder: Decoder): OneOf<A, B> {
    require(decoder is JsonDecoder) { "Only JSON is supported" }
    val jsonElement = decoder.decodeJsonElement()

    return try {
        OneOf.First(decoder.json.decodeFromJsonElement(serializerA, jsonElement))
    } catch (e: Exception) {
        try {
            OneOf.Second(decoder.json.decodeFromJsonElement(serializerB, jsonElement))
        } catch (e: Exception) {
            throw SerializationException(
                "Could not deserialize OneOf: none of the types matched",
                e
            )
        }
    }
  }

  override fun serialize(encoder: Encoder, `value`: OneOf<A, B>) {
    require(encoder is JsonEncoder) { "Only JSON is supported" }
    when (value) {
        is OneOf.First -> encoder.encodeSerializableValue(serializerA, value.value)
        is OneOf.Second -> encoder.encodeSerializableValue(serializerB, value.value)
    }
  }
}
