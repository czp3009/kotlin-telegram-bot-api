// Auto-generated from Swagger specificationDo not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class SetMyCommandsRequest(
    /**
     * A JSON-serialized list of bot commands to be set as the list of the bot's commands. At most 100 commands can be specified.
     */
    public val commands: List<BotCommand>,
    /**
     * A JSON-serialized object, describing scope of users for which the commands are relevant. Defaults to [BotCommandScopeDefault](https://core.telegram.org/bots/api#botcommandscopedefault).
     */
    public val scope: BotCommandScope? = null,
    /**
     * A two-letter ISO 639-1 language code. If empty, commands will be applied to all users from the given scope, for whose language there are no dedicated commands
     */
    @SerialName("language_code")
    public val languageCode: String? = null,
)
