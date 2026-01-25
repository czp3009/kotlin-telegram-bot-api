// Auto-generated from Swagger specificationDo not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlinx.serialization.Serializable

/**
 * Describes data required for decrypting and authenticating EncryptedPassportElement. See the Telegram Passport Documentation for a complete description of the data decryption and authentication processes.
 */
@Serializable
public data class EncryptedCredentials(
    /**
     * Base64-encoded encrypted JSON-serialized data with unique user's payload, data hashes and secrets required for [EncryptedPassportElement](https://core.telegram.org/bots/api#encryptedpassportelement) decryption and authentication
     */
    public val `data`: String,
    /**
     * Base64-encoded data hash for data authentication
     */
    public val hash: String,
    /**
     * Base64-encoded secret, encrypted with the bot's public RSA key, required for data decryption
     */
    public val secret: String,
)
