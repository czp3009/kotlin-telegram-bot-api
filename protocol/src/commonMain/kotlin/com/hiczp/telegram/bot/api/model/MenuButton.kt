// Auto-generated from Swagger specificationDo not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import kotlinx.serialization.json.JsonElement

/**
 * This object describes the bot's menu button in a private chat. It should be one of
 * MenuButtonCommands MenuButtonWebApp MenuButtonDefault
 * If a menu button other than MenuButtonDefault is set for a private chat, then it is applied in the chat. Otherwise the default menu button is applied. By default, the menu button opens the list of bot commands.
 */
@Serializable
@JsonClassDiscriminator("type")
@OptIn(ExperimentalSerializationApi::class)
public sealed interface MenuButton

/**
 * Represents a menu button, which opens the bot's list of commands.
 */
@Serializable
@SerialName("commands")
public class MenuButtonCommands : MenuButton

/**
 * Represents a menu button, which launches a Web App.
 */
@Serializable
@SerialName("web_app")
public data class MenuButtonWebApp(
    /**
     * Text on the button
     */
    public val text: String,
    /**
     * Description of the Web App that will be launched when the user presses the button. The Web App will be able to send an arbitrary message on behalf of the user using the method [answerWebAppQuery](https://core.telegram.org/bots/api#answerwebappquery). Alternatively, a `t.me` link to a Web App of the bot can be specified in the object instead of the Web App's URL, in which case the Web App will be opened as if the user pressed the link.
     */
    @SerialName("web_app")
    public val webApp: JsonElement?,
) : MenuButton

/**
 * Describes that no specific value for the menu button was set.
 */
@Serializable
@SerialName("default")
public class MenuButtonDefault : MenuButton
