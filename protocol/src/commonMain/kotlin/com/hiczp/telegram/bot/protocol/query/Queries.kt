// Auto-generated from Swagger specification, do not modify this file manuallyExtension functions for Telegram Bot API query operations.
//
// This file provides typed query extension functions for methods whose query parameters
// require JSON serialization before sending to Telegram API.
@file:Suppress(
    "RedundantVisibilityModifier",
    "unused",
    "DuplicatedCode",
)

package com.hiczp.telegram.bot.protocol.query

import com.hiczp.telegram.bot.protocol.TelegramBotApi
import com.hiczp.telegram.bot.protocol.model.BotCommand
import com.hiczp.telegram.bot.protocol.model.BotCommandScope
import com.hiczp.telegram.bot.protocol.model.Sticker
import com.hiczp.telegram.bot.protocol.model.Update
import com.hiczp.telegram.bot.protocol.type.TelegramResponse
import kotlin.Long
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List
import kotlinx.serialization.json.Json

/**
 * Use this method to receive incoming updates using long polling (wiki). Returns an Array of Update objects.
 * Notes 1. This method will not work if an outgoing webhook is set up. 2. In order to avoid getting duplicate updates, recalculate offset after each server response.
 *
 * @param offset Identifier of the first update to be returned. Must be greater by one than the highest among the identifiers of previously received updates. By default, updates starting with the earliest unconfirmed update are returned. An update is considered confirmed as soon as [getUpdates](https://core.telegram.org/bots/api#getupdates) is called with an *offset* higher than its *update_id*. The negative offset can be specified to retrieve updates starting from *-offset* update from the end of the updates queue. All previous updates will be forgotten.
 * @param limit Limits the number of updates to be retrieved. Values between 1-100 are accepted. Defaults to 100.
 * @param timeout Timeout in seconds for long polling. Defaults to 0, i.e. usual short polling. Should be positive, short polling should be used for testing purposes only.
 * @param allowedUpdates A JSON-serialized list of the update types you want your bot to receive. For example, specify `["message", "edited_channel_post", "callback_query"]` to only receive updates of these types. See [Update](https://core.telegram.org/bots/api#update) for a complete list of available update types. Specify an empty list to receive all update types except *chat_member*, *message_reaction*, and *message_reaction_count* (default). If not specified, the previous setting will be used.Please note that this parameter doesn't affect updates created before the call to getUpdates, so unwanted updates may be received for a short period of time.
 */
public suspend fun TelegramBotApi.getUpdates(
    offset: Long? = null,
    limit: Long? = null,
    timeout: Long? = null,
    allowedUpdates: List<String>? = null,
): TelegramResponse<List<Update>> = getUpdates(
    offset = offset,
    limit = limit,
    timeout = timeout,
    allowedUpdates = allowedUpdates?.let { Json.encodeToString(it) }
)

/**
 * Use this method to get the current list of the bot's commands for the given scope and user language. Returns an Array of BotCommand objects. If commands aren't set, an empty list is returned.
 *
 * @param scope A JSON-serialized object, describing scope of users. Defaults to [BotCommandScopeDefault](https://core.telegram.org/bots/api#botcommandscopedefault).
 * @param languageCode A two-letter ISO 639-1 language code or an empty string
 */
public suspend fun TelegramBotApi.getMyCommands(scope: BotCommandScope? = null, languageCode: String? = null): TelegramResponse<List<BotCommand>> = getMyCommands(
    scope = scope?.let { Json.encodeToString(it) },
    languageCode = languageCode
)

/**
 * Use this method to get information about custom emoji stickers by their identifiers. Returns an Array of Sticker objects.
 *
 * @param customEmojiIds A JSON-serialized list of custom emoji identifiers. At most 200 custom emoji identifiers can be specified.
 */
public suspend fun TelegramBotApi.getCustomEmojiStickers(customEmojiIds: List<String>): TelegramResponse<List<Sticker>> = getCustomEmojiStickers(
    customEmojiIds = Json.encodeToString(customEmojiIds)
)
