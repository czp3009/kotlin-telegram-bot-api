// Auto-generated from Swagger specification, do not modify this file manually
//
// A SerializersModule that provides serializers for all Union types used in the Telegram Bot API.
//
// Usage:
// ```kotlin
// val json = Json {
//     serializersModule += unionSerializersModule
// }
// ```
package com.hiczp.telegram.bot.protocol.union

import kotlinx.serialization.modules.SerializersModule

public val unionSerializersModule: SerializersModule
    get() = SerializersModule {
        contextual(Union::class) { args -> UnionSerializer(args[0], args[1]) }
    }
