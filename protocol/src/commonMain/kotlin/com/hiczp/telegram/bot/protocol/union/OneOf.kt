// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.union

import kotlin.Nothing
import kotlinx.serialization.Serializable

/**
 * Sealed class representing a union of A, B possible types.
 *
 * Used for API responses that can return different types (e.g., a Message object or a Boolean).
 */
@Serializable(with = OneOfSerializer::class)
public sealed class OneOf<out A, out B> {
  /**
   * Holds a value of type A.
   */
  public data class First<out A>(
    public val `value`: A,
  ) : OneOf<A, Nothing>()

  /**
   * Holds a value of type B.
   */
  public data class Second<out B>(
    public val `value`: B,
  ) : OneOf<Nothing, B>()
}
