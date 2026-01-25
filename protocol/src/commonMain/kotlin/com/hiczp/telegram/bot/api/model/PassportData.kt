// Auto-generated from Swagger specificationDo not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

/**
 * Describes Telegram Passport data shared with the bot by the user.
 */
@Serializable
public data class PassportData(
    /**
     * Array with information about documents and other Telegram Passport elements that was shared with the bot
     */
    public val `data`: List<EncryptedPassportElement>,
    /**
     * Encrypted credentials required to decrypt the data
     */
    public val credentials: JsonElement?,
)
