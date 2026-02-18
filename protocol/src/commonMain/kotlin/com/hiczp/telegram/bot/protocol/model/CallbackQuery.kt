// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.model

import com.hiczp.telegram.bot.protocol.type.IncomingUpdate
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * This object represents an incoming callback query from a callback button in an inline keyboard. If the button that originated the query was attached to a message sent by the bot, the field message will be present. If the button was attached to a message sent via the bot (in inline mode), the field inline_message_id will be present. Exactly one of the fields data or game_short_name will be present.
 * NOTE: After the user presses a callback button, Telegram clients will display a progress bar until you call answerCallbackQuery. It is, therefore, necessary to react by calling answerCallbackQuery even if no notification to the user is needed (e.g., without specifying any of the optional parameters).
 */
@Serializable
public data class CallbackQuery(
    /**
     * Unique identifier for this query
     */
    public val id: String,
    /**
     * Sender
     */
    public val from: User,
    /**
     * *Optional*. Message sent by the bot with the callback button that originated the query
     */
    public val message: Message? = null,
    /**
     * *Optional*. Identifier of the message sent via the bot in inline mode, that originated the query.
     */
    @SerialName("inline_message_id")
    public val inlineMessageId: String? = null,
    /**
     * Global identifier, uniquely corresponding to the chat to which the message with the callback button was sent. Useful for high scores in [games](https://core.telegram.org/bots/api#games).
     */
    @SerialName("chat_instance")
    public val chatInstance: String,
    /**
     * *Optional*. Data associated with the callback button. Be aware that the message originated the query can contain no callback buttons with this data.
     */
    public val `data`: String? = null,
    /**
     * *Optional*. Short name of a [Game](https://core.telegram.org/bots/api#games) to be returned, serves as the unique identifier for the game
     */
    @SerialName("game_short_name")
    public val gameShortName: String? = null,
) : IncomingUpdate
