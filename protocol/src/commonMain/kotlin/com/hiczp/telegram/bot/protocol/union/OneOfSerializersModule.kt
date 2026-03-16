// Auto-generated from Swagger specification, do not modify this file manually
//
// A SerializersModule that provides serializers for all OneOf types used in the Telegram Bot API.
//
// Usage:
// ```kotlin
// val json = Json {
//     serializersModule += oneOfSerializersModule
// }
// ```
package com.hiczp.telegram.bot.protocol.union

import com.hiczp.telegram.bot.protocol.model.Message
import com.hiczp.telegram.bot.protocol.union.OneOf
import com.hiczp.telegram.bot.protocol.union.OneOfSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.modules.SerializersModule

public val oneOfSerializersModule: SerializersModule
  get() = SerializersModule {
      contextual(OneOf::class) { args -> OneOfSerializer<Message, Boolean>(args[0] as KSerializer<Message>, args[1] as KSerializer<Boolean>) }
  }
