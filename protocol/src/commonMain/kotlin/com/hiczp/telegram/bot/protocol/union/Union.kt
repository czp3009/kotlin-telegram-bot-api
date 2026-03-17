// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.union

import kotlin.Nothing
import kotlinx.serialization.Serializable

/**
 * Sealed class representing a union of A, B possible types.
 *
 * Used for API responses that can return different types (e.g., a Message object or a Boolean).
 */
@Serializable(with = UnionSerializer::class)
public sealed class Union<out A, out B> {
    /**
     * Returns the value if this is [First], null otherwise.
     */
    public fun firstOrNull(): A? = (this as? First)?.value

    /**
     * Returns the value if this is [Second], null otherwise.
     */
    public fun secondOrNull(): B? = (this as? Second)?.value

    /**
     * Holds a value of type A.
     */
    public data class First<out A>(
        public val `value`: A,
    ) : Union<A, Nothing>()

    /**
     * Holds a value of type B.
     */
    public data class Second<out B>(
        public val `value`: B,
    ) : Union<Nothing, B>()
}
