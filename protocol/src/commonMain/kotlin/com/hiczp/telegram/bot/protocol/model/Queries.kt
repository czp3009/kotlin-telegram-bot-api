// Auto-generated from Swagger specification, do not modify this file manuallyScatter extension functions for Telegram Bot API JSON body operations.
//
// This file provides typed extension functions for methods that accept JSON request bodies,
// allowing users to pass individual parameters instead of constructing Request objects manually.
//
// Example:
// ```kotlin
// // Instead of:
// api.sendMessage(SendMessageRequest(chatId = "123", text = "Hello"))
//
// // You can use:
// api.sendMessage(chatId = "123", text = "Hello")
// ```
//
// These functions are auto-generated and should not be edited manually.
@file:Suppress(
    "RedundantVisibilityModifier",
    "unused",
    "DuplicatedCode",
)

package com.hiczp.telegram.bot.protocol.model

import com.hiczp.telegram.bot.protocol.TelegramBotApi
import com.hiczp.telegram.bot.protocol.type.TelegramResponse
import kotlin.Boolean
import kotlin.Double
import kotlin.Long
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List

/**
 * Use this method to remove webhook integration if you decide to switch back to getUpdates. Returns True on success.
 *
 * @param dropPendingUpdates Pass *True* to drop all pending updates
 */
public suspend fun TelegramBotApi.deleteWebhook(dropPendingUpdates: Boolean? = null): TelegramResponse<Boolean> {
    val request = DeleteWebhookRequest(
        dropPendingUpdates = dropPendingUpdates
    )
    return deleteWebhook(request)
}

/**
 * Use this method to send text messages. On success, the sent Message is returned.
 *
 * @param businessConnectionId Unique identifier of the business connection on behalf of which the message will be sent
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format `@channelusername`)
 * @param messageThreadId Unique identifier for the target message thread (topic) of a forum; for forum supergroups and private chats of bots with forum topic mode enabled only
 * @param directMessagesTopicId Identifier of the direct messages topic to which the message will be sent; required if the message is sent to a direct messages chat
 * @param text Text of the message to be sent, 1-4096 characters after entities parsing
 * @param parseMode Mode for parsing entities in the message text. See [formatting options](https://core.telegram.org/bots/api#formatting-options) for more details.
 * @param entities A JSON-serialized list of special entities that appear in message text, which can be specified instead of *parse_mode*
 * @param linkPreviewOptions Link preview generation options for the message
 * @param disableNotification Sends the message [silently](https://telegram.org/blog/channels-2-0#silent-messages). Users will receive a notification with no sound.
 * @param protectContent Protects the contents of the sent message from forwarding and saving
 * @param allowPaidBroadcast Pass *True* to allow up to 1000 messages per second, ignoring [broadcasting limits](https://core.telegram.org/bots/faq#how-can-i-message-all-of-my-bot-39s-subscribers-at-once) for a fee of 0.1 Telegram Stars per message. The relevant Stars will be withdrawn from the bot's balance
 * @param messageEffectId Unique identifier of the message effect to be added to the message; for private chats only
 * @param suggestedPostParameters A JSON-serialized object containing the parameters of the suggested post to send; for direct messages chats only. If the message is sent as a reply to another suggested post, then that suggested post is automatically declined.
 * @param replyParameters Description of the message to reply to
 * @param replyMarkup Additional interface options. A JSON-serialized object for an [inline keyboard](https://core.telegram.org/bots/features#inline-keyboards), [custom reply keyboard](https://core.telegram.org/bots/features#keyboards), instructions to remove a reply keyboard or to force a reply from the user
 */
public suspend fun TelegramBotApi.sendMessage(
    businessConnectionId: String? = null,
    chatId: String,
    messageThreadId: Long? = null,
    directMessagesTopicId: Long? = null,
    text: String,
    parseMode: String? = null,
    entities: List<MessageEntity>? = null,
    linkPreviewOptions: LinkPreviewOptions? = null,
    disableNotification: Boolean? = null,
    protectContent: Boolean? = null,
    allowPaidBroadcast: Boolean? = null,
    messageEffectId: String? = null,
    suggestedPostParameters: SuggestedPostParameters? = null,
    replyParameters: ReplyParameters? = null,
    replyMarkup: ReplyMarkup? = null,
): TelegramResponse<Message> {
    val request = SendMessageRequest(
        businessConnectionId = businessConnectionId,
        chatId = chatId,
        messageThreadId = messageThreadId,
        directMessagesTopicId = directMessagesTopicId,
        text = text,
        parseMode = parseMode,
        entities = entities,
        linkPreviewOptions = linkPreviewOptions,
        disableNotification = disableNotification,
        protectContent = protectContent,
        allowPaidBroadcast = allowPaidBroadcast,
        messageEffectId = messageEffectId,
        suggestedPostParameters = suggestedPostParameters,
        replyParameters = replyParameters,
        replyMarkup = replyMarkup
    )
    return sendMessage(request)
}

/**
 * Use this method to forward messages of any kind. Service messages and messages with protected content can't be forwarded. On success, the sent Message is returned.
 *
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format `@channelusername`)
 * @param messageThreadId Unique identifier for the target message thread (topic) of a forum; for forum supergroups and private chats of bots with forum topic mode enabled only
 * @param directMessagesTopicId Identifier of the direct messages topic to which the message will be forwarded; required if the message is forwarded to a direct messages chat
 * @param fromChatId Unique identifier for the chat where the original message was sent (or channel username in the format `@channelusername`)
 * @param videoStartTimestamp New start timestamp for the forwarded video in the message
 * @param disableNotification Sends the message [silently](https://telegram.org/blog/channels-2-0#silent-messages). Users will receive a notification with no sound.
 * @param protectContent Protects the contents of the forwarded message from forwarding and saving
 * @param messageEffectId Unique identifier of the message effect to be added to the message; only available when forwarding to private chats
 * @param suggestedPostParameters A JSON-serialized object containing the parameters of the suggested post to send; for direct messages chats only
 * @param messageId Message identifier in the chat specified in *from_chat_id*
 */
public suspend fun TelegramBotApi.forwardMessage(
    chatId: String,
    messageThreadId: Long? = null,
    directMessagesTopicId: Long? = null,
    fromChatId: String,
    videoStartTimestamp: Long? = null,
    disableNotification: Boolean? = null,
    protectContent: Boolean? = null,
    messageEffectId: String? = null,
    suggestedPostParameters: SuggestedPostParameters? = null,
    messageId: Long,
): TelegramResponse<Message> {
    val request = ForwardMessageRequest(
        chatId = chatId,
        messageThreadId = messageThreadId,
        directMessagesTopicId = directMessagesTopicId,
        fromChatId = fromChatId,
        videoStartTimestamp = videoStartTimestamp,
        disableNotification = disableNotification,
        protectContent = protectContent,
        messageEffectId = messageEffectId,
        suggestedPostParameters = suggestedPostParameters,
        messageId = messageId
    )
    return forwardMessage(request)
}

/**
 * Use this method to forward multiple messages of any kind. If some of the specified messages can't be found or forwarded, they are skipped. Service messages and messages with protected content can't be forwarded. Album grouping is kept for forwarded messages. On success, an array of MessageId of the sent messages is returned.
 *
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format `@channelusername`)
 * @param messageThreadId Unique identifier for the target message thread (topic) of a forum; for forum supergroups and private chats of bots with forum topic mode enabled only
 * @param directMessagesTopicId Identifier of the direct messages topic to which the messages will be forwarded; required if the messages are forwarded to a direct messages chat
 * @param fromChatId Unique identifier for the chat where the original messages were sent (or channel username in the format `@channelusername`)
 * @param messageIds A JSON-serialized list of 1-100 identifiers of messages in the chat *from_chat_id* to forward. The identifiers must be specified in a strictly increasing order.
 * @param disableNotification Sends the messages [silently](https://telegram.org/blog/channels-2-0#silent-messages). Users will receive a notification with no sound.
 * @param protectContent Protects the contents of the forwarded messages from forwarding and saving
 */
public suspend fun TelegramBotApi.forwardMessages(
    chatId: String,
    messageThreadId: Long? = null,
    directMessagesTopicId: Long? = null,
    fromChatId: String,
    messageIds: List<Long>,
    disableNotification: Boolean? = null,
    protectContent: Boolean? = null,
): TelegramResponse<List<MessageId>> {
    val request = ForwardMessagesRequest(
        chatId = chatId,
        messageThreadId = messageThreadId,
        directMessagesTopicId = directMessagesTopicId,
        fromChatId = fromChatId,
        messageIds = messageIds,
        disableNotification = disableNotification,
        protectContent = protectContent
    )
    return forwardMessages(request)
}

/**
 * Use this method to copy messages of any kind. Service messages, paid media messages, giveaway messages, giveaway winners messages, and invoice messages can't be copied. A quiz poll can be copied only if the value of the field correct_option_id is known to the bot. The method is analogous to the method forwardMessage, but the copied message doesn't have a link to the original message. Returns the MessageId of the sent message on success.
 *
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format `@channelusername`)
 * @param messageThreadId Unique identifier for the target message thread (topic) of a forum; for forum supergroups and private chats of bots with forum topic mode enabled only
 * @param directMessagesTopicId Identifier of the direct messages topic to which the message will be sent; required if the message is sent to a direct messages chat
 * @param fromChatId Unique identifier for the chat where the original message was sent (or channel username in the format `@channelusername`)
 * @param messageId Message identifier in the chat specified in *from_chat_id*
 * @param videoStartTimestamp New start timestamp for the copied video in the message
 * @param caption New caption for media, 0-1024 characters after entities parsing. If not specified, the original caption is kept
 * @param parseMode Mode for parsing entities in the new caption. See [formatting options](https://core.telegram.org/bots/api#formatting-options) for more details.
 * @param captionEntities A JSON-serialized list of special entities that appear in the new caption, which can be specified instead of *parse_mode*
 * @param showCaptionAboveMedia Pass *True*, if the caption must be shown above the message media. Ignored if a new caption isn't specified.
 * @param disableNotification Sends the message [silently](https://telegram.org/blog/channels-2-0#silent-messages). Users will receive a notification with no sound.
 * @param protectContent Protects the contents of the sent message from forwarding and saving
 * @param allowPaidBroadcast Pass *True* to allow up to 1000 messages per second, ignoring [broadcasting limits](https://core.telegram.org/bots/faq#how-can-i-message-all-of-my-bot-39s-subscribers-at-once) for a fee of 0.1 Telegram Stars per message. The relevant Stars will be withdrawn from the bot's balance
 * @param messageEffectId Unique identifier of the message effect to be added to the message; only available when copying to private chats
 * @param suggestedPostParameters A JSON-serialized object containing the parameters of the suggested post to send; for direct messages chats only. If the message is sent as a reply to another suggested post, then that suggested post is automatically declined.
 * @param replyParameters Description of the message to reply to
 * @param replyMarkup Additional interface options. A JSON-serialized object for an [inline keyboard](https://core.telegram.org/bots/features#inline-keyboards), [custom reply keyboard](https://core.telegram.org/bots/features#keyboards), instructions to remove a reply keyboard or to force a reply from the user
 */
public suspend fun TelegramBotApi.copyMessage(
    chatId: String,
    messageThreadId: Long? = null,
    directMessagesTopicId: Long? = null,
    fromChatId: String,
    messageId: Long,
    videoStartTimestamp: Long? = null,
    caption: String? = null,
    parseMode: String? = null,
    captionEntities: List<MessageEntity>? = null,
    showCaptionAboveMedia: Boolean? = null,
    disableNotification: Boolean? = null,
    protectContent: Boolean? = null,
    allowPaidBroadcast: Boolean? = null,
    messageEffectId: String? = null,
    suggestedPostParameters: SuggestedPostParameters? = null,
    replyParameters: ReplyParameters? = null,
    replyMarkup: ReplyMarkup? = null,
): TelegramResponse<MessageId> {
    val request = CopyMessageRequest(
        chatId = chatId,
        messageThreadId = messageThreadId,
        directMessagesTopicId = directMessagesTopicId,
        fromChatId = fromChatId,
        messageId = messageId,
        videoStartTimestamp = videoStartTimestamp,
        caption = caption,
        parseMode = parseMode,
        captionEntities = captionEntities,
        showCaptionAboveMedia = showCaptionAboveMedia,
        disableNotification = disableNotification,
        protectContent = protectContent,
        allowPaidBroadcast = allowPaidBroadcast,
        messageEffectId = messageEffectId,
        suggestedPostParameters = suggestedPostParameters,
        replyParameters = replyParameters,
        replyMarkup = replyMarkup
    )
    return copyMessage(request)
}

/**
 * Use this method to copy messages of any kind. If some of the specified messages can't be found or copied, they are skipped. Service messages, paid media messages, giveaway messages, giveaway winners messages, and invoice messages can't be copied. A quiz poll can be copied only if the value of the field correct_option_id is known to the bot. The method is analogous to the method forwardMessages, but the copied messages don't have a link to the original message. Album grouping is kept for copied messages. On success, an array of MessageId of the sent messages is returned.
 *
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format `@channelusername`)
 * @param messageThreadId Unique identifier for the target message thread (topic) of a forum; for forum supergroups and private chats of bots with forum topic mode enabled only
 * @param directMessagesTopicId Identifier of the direct messages topic to which the messages will be sent; required if the messages are sent to a direct messages chat
 * @param fromChatId Unique identifier for the chat where the original messages were sent (or channel username in the format `@channelusername`)
 * @param messageIds A JSON-serialized list of 1-100 identifiers of messages in the chat *from_chat_id* to copy. The identifiers must be specified in a strictly increasing order.
 * @param disableNotification Sends the messages [silently](https://telegram.org/blog/channels-2-0#silent-messages). Users will receive a notification with no sound.
 * @param protectContent Protects the contents of the sent messages from forwarding and saving
 * @param removeCaption Pass *True* to copy the messages without their captions
 */
public suspend fun TelegramBotApi.copyMessages(
    chatId: String,
    messageThreadId: Long? = null,
    directMessagesTopicId: Long? = null,
    fromChatId: String,
    messageIds: List<Long>,
    disableNotification: Boolean? = null,
    protectContent: Boolean? = null,
    removeCaption: Boolean? = null,
): TelegramResponse<List<MessageId>> {
    val request = CopyMessagesRequest(
        chatId = chatId,
        messageThreadId = messageThreadId,
        directMessagesTopicId = directMessagesTopicId,
        fromChatId = fromChatId,
        messageIds = messageIds,
        disableNotification = disableNotification,
        protectContent = protectContent,
        removeCaption = removeCaption
    )
    return copyMessages(request)
}

/**
 * Use this method to send point on the map. On success, the sent Message is returned.
 *
 * @param businessConnectionId Unique identifier of the business connection on behalf of which the message will be sent
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format `@channelusername`)
 * @param messageThreadId Unique identifier for the target message thread (topic) of a forum; for forum supergroups and private chats of bots with forum topic mode enabled only
 * @param directMessagesTopicId Identifier of the direct messages topic to which the message will be sent; required if the message is sent to a direct messages chat
 * @param latitude Latitude of the location
 * @param longitude Longitude of the location
 * @param horizontalAccuracy The radius of uncertainty for the location, measured in meters; 0-1500
 * @param livePeriod Period in seconds during which the location will be updated (see [Live Locations](https://telegram.org/blog/live-locations), should be between 60 and 86400, or 0x7FFFFFFF for live locations that can be edited indefinitely.
 * @param heading For live locations, a direction in which the user is moving, in degrees. Must be between 1 and 360 if specified.
 * @param proximityAlertRadius For live locations, a maximum distance for proximity alerts about approaching another chat member, in meters. Must be between 1 and 100000 if specified.
 * @param disableNotification Sends the message [silently](https://telegram.org/blog/channels-2-0#silent-messages). Users will receive a notification with no sound.
 * @param protectContent Protects the contents of the sent message from forwarding and saving
 * @param allowPaidBroadcast Pass *True* to allow up to 1000 messages per second, ignoring [broadcasting limits](https://core.telegram.org/bots/faq#how-can-i-message-all-of-my-bot-39s-subscribers-at-once) for a fee of 0.1 Telegram Stars per message. The relevant Stars will be withdrawn from the bot's balance
 * @param messageEffectId Unique identifier of the message effect to be added to the message; for private chats only
 * @param suggestedPostParameters A JSON-serialized object containing the parameters of the suggested post to send; for direct messages chats only. If the message is sent as a reply to another suggested post, then that suggested post is automatically declined.
 * @param replyParameters Description of the message to reply to
 * @param replyMarkup Additional interface options. A JSON-serialized object for an [inline keyboard](https://core.telegram.org/bots/features#inline-keyboards), [custom reply keyboard](https://core.telegram.org/bots/features#keyboards), instructions to remove a reply keyboard or to force a reply from the user
 */
public suspend fun TelegramBotApi.sendLocation(
    businessConnectionId: String? = null,
    chatId: String,
    messageThreadId: Long? = null,
    directMessagesTopicId: Long? = null,
    latitude: Double,
    longitude: Double,
    horizontalAccuracy: Double? = null,
    livePeriod: Long? = null,
    heading: Long? = null,
    proximityAlertRadius: Long? = null,
    disableNotification: Boolean? = null,
    protectContent: Boolean? = null,
    allowPaidBroadcast: Boolean? = null,
    messageEffectId: String? = null,
    suggestedPostParameters: SuggestedPostParameters? = null,
    replyParameters: ReplyParameters? = null,
    replyMarkup: ReplyMarkup? = null,
): TelegramResponse<Message> {
    val request = SendLocationRequest(
        businessConnectionId = businessConnectionId,
        chatId = chatId,
        messageThreadId = messageThreadId,
        directMessagesTopicId = directMessagesTopicId,
        latitude = latitude,
        longitude = longitude,
        horizontalAccuracy = horizontalAccuracy,
        livePeriod = livePeriod,
        heading = heading,
        proximityAlertRadius = proximityAlertRadius,
        disableNotification = disableNotification,
        protectContent = protectContent,
        allowPaidBroadcast = allowPaidBroadcast,
        messageEffectId = messageEffectId,
        suggestedPostParameters = suggestedPostParameters,
        replyParameters = replyParameters,
        replyMarkup = replyMarkup
    )
    return sendLocation(request)
}

/**
 * Use this method to send information about a venue. On success, the sent Message is returned.
 *
 * @param businessConnectionId Unique identifier of the business connection on behalf of which the message will be sent
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format `@channelusername`)
 * @param messageThreadId Unique identifier for the target message thread (topic) of a forum; for forum supergroups and private chats of bots with forum topic mode enabled only
 * @param directMessagesTopicId Identifier of the direct messages topic to which the message will be sent; required if the message is sent to a direct messages chat
 * @param latitude Latitude of the venue
 * @param longitude Longitude of the venue
 * @param title Name of the venue
 * @param address Address of the venue
 * @param foursquareId Foursquare identifier of the venue
 * @param foursquareType Foursquare type of the venue, if known. (For example, “arts_entertainment/default”, “arts_entertainment/aquarium” or “food/icecream”.)
 * @param googlePlaceId Google Places identifier of the venue
 * @param googlePlaceType Google Places type of the venue. (See [supported types](https://developers.google.com/places/web-service/supported_types).)
 * @param disableNotification Sends the message [silently](https://telegram.org/blog/channels-2-0#silent-messages). Users will receive a notification with no sound.
 * @param protectContent Protects the contents of the sent message from forwarding and saving
 * @param allowPaidBroadcast Pass *True* to allow up to 1000 messages per second, ignoring [broadcasting limits](https://core.telegram.org/bots/faq#how-can-i-message-all-of-my-bot-39s-subscribers-at-once) for a fee of 0.1 Telegram Stars per message. The relevant Stars will be withdrawn from the bot's balance
 * @param messageEffectId Unique identifier of the message effect to be added to the message; for private chats only
 * @param suggestedPostParameters A JSON-serialized object containing the parameters of the suggested post to send; for direct messages chats only. If the message is sent as a reply to another suggested post, then that suggested post is automatically declined.
 * @param replyParameters Description of the message to reply to
 * @param replyMarkup Additional interface options. A JSON-serialized object for an [inline keyboard](https://core.telegram.org/bots/features#inline-keyboards), [custom reply keyboard](https://core.telegram.org/bots/features#keyboards), instructions to remove a reply keyboard or to force a reply from the user
 */
public suspend fun TelegramBotApi.sendVenue(
    businessConnectionId: String? = null,
    chatId: String,
    messageThreadId: Long? = null,
    directMessagesTopicId: Long? = null,
    latitude: Double,
    longitude: Double,
    title: String,
    address: String,
    foursquareId: String? = null,
    foursquareType: String? = null,
    googlePlaceId: String? = null,
    googlePlaceType: String? = null,
    disableNotification: Boolean? = null,
    protectContent: Boolean? = null,
    allowPaidBroadcast: Boolean? = null,
    messageEffectId: String? = null,
    suggestedPostParameters: SuggestedPostParameters? = null,
    replyParameters: ReplyParameters? = null,
    replyMarkup: ReplyMarkup? = null,
): TelegramResponse<Message> {
    val request = SendVenueRequest(
        businessConnectionId = businessConnectionId,
        chatId = chatId,
        messageThreadId = messageThreadId,
        directMessagesTopicId = directMessagesTopicId,
        latitude = latitude,
        longitude = longitude,
        title = title,
        address = address,
        foursquareId = foursquareId,
        foursquareType = foursquareType,
        googlePlaceId = googlePlaceId,
        googlePlaceType = googlePlaceType,
        disableNotification = disableNotification,
        protectContent = protectContent,
        allowPaidBroadcast = allowPaidBroadcast,
        messageEffectId = messageEffectId,
        suggestedPostParameters = suggestedPostParameters,
        replyParameters = replyParameters,
        replyMarkup = replyMarkup
    )
    return sendVenue(request)
}

/**
 * Use this method to send phone contacts. On success, the sent Message is returned.
 *
 * @param businessConnectionId Unique identifier of the business connection on behalf of which the message will be sent
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format `@channelusername`)
 * @param messageThreadId Unique identifier for the target message thread (topic) of a forum; for forum supergroups and private chats of bots with forum topic mode enabled only
 * @param directMessagesTopicId Identifier of the direct messages topic to which the message will be sent; required if the message is sent to a direct messages chat
 * @param phoneNumber Contact's phone number
 * @param firstName Contact's first name
 * @param lastName Contact's last name
 * @param vcard Additional data about the contact in the form of a [vCard](https://en.wikipedia.org/wiki/VCard), 0-2048 bytes
 * @param disableNotification Sends the message [silently](https://telegram.org/blog/channels-2-0#silent-messages). Users will receive a notification with no sound.
 * @param protectContent Protects the contents of the sent message from forwarding and saving
 * @param allowPaidBroadcast Pass *True* to allow up to 1000 messages per second, ignoring [broadcasting limits](https://core.telegram.org/bots/faq#how-can-i-message-all-of-my-bot-39s-subscribers-at-once) for a fee of 0.1 Telegram Stars per message. The relevant Stars will be withdrawn from the bot's balance
 * @param messageEffectId Unique identifier of the message effect to be added to the message; for private chats only
 * @param suggestedPostParameters A JSON-serialized object containing the parameters of the suggested post to send; for direct messages chats only. If the message is sent as a reply to another suggested post, then that suggested post is automatically declined.
 * @param replyParameters Description of the message to reply to
 * @param replyMarkup Additional interface options. A JSON-serialized object for an [inline keyboard](https://core.telegram.org/bots/features#inline-keyboards), [custom reply keyboard](https://core.telegram.org/bots/features#keyboards), instructions to remove a reply keyboard or to force a reply from the user
 */
public suspend fun TelegramBotApi.sendContact(
    businessConnectionId: String? = null,
    chatId: String,
    messageThreadId: Long? = null,
    directMessagesTopicId: Long? = null,
    phoneNumber: String,
    firstName: String,
    lastName: String? = null,
    vcard: String? = null,
    disableNotification: Boolean? = null,
    protectContent: Boolean? = null,
    allowPaidBroadcast: Boolean? = null,
    messageEffectId: String? = null,
    suggestedPostParameters: SuggestedPostParameters? = null,
    replyParameters: ReplyParameters? = null,
    replyMarkup: ReplyMarkup? = null,
): TelegramResponse<Message> {
    val request = SendContactRequest(
        businessConnectionId = businessConnectionId,
        chatId = chatId,
        messageThreadId = messageThreadId,
        directMessagesTopicId = directMessagesTopicId,
        phoneNumber = phoneNumber,
        firstName = firstName,
        lastName = lastName,
        vcard = vcard,
        disableNotification = disableNotification,
        protectContent = protectContent,
        allowPaidBroadcast = allowPaidBroadcast,
        messageEffectId = messageEffectId,
        suggestedPostParameters = suggestedPostParameters,
        replyParameters = replyParameters,
        replyMarkup = replyMarkup
    )
    return sendContact(request)
}

/**
 * Use this method to send a native poll. On success, the sent Message is returned.
 *
 * @param businessConnectionId Unique identifier of the business connection on behalf of which the message will be sent
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format `@channelusername`). Polls can't be sent to channel direct messages chats.
 * @param messageThreadId Unique identifier for the target message thread (topic) of a forum; for forum supergroups and private chats of bots with forum topic mode enabled only
 * @param question Poll question, 1-300 characters
 * @param questionParseMode Mode for parsing entities in the question. See [formatting options](https://core.telegram.org/bots/api#formatting-options) for more details. Currently, only custom emoji entities are allowed
 * @param questionEntities A JSON-serialized list of special entities that appear in the poll question. It can be specified instead of *question_parse_mode*
 * @param options A JSON-serialized list of 2-12 answer options
 * @param isAnonymous *True*, if the poll needs to be anonymous, defaults to *True*
 * @param type Poll type, “quiz” or “regular”, defaults to “regular”
 * @param allowsMultipleAnswers *True*, if the poll allows multiple answers, ignored for polls in quiz mode, defaults to *False*
 * @param correctOptionId 0-based identifier of the correct answer option, required for polls in quiz mode
 * @param explanation Text that is shown when a user chooses an incorrect answer or taps on the lamp icon in a quiz-style poll, 0-200 characters with at most 2 line feeds after entities parsing
 * @param explanationParseMode Mode for parsing entities in the explanation. See [formatting options](https://core.telegram.org/bots/api#formatting-options) for more details.
 * @param explanationEntities A JSON-serialized list of special entities that appear in the poll explanation. It can be specified instead of *explanation_parse_mode*
 * @param openPeriod Amount of time in seconds the poll will be active after creation, 5-600. Can't be used together with *close_date*.
 * @param closeDate Point in time (Unix timestamp) when the poll will be automatically closed. Must be at least 5 and no more than 600 seconds in the future. Can't be used together with *open_period*.
 * @param isClosed Pass *True* if the poll needs to be immediately closed. This can be useful for poll preview.
 * @param disableNotification Sends the message [silently](https://telegram.org/blog/channels-2-0#silent-messages). Users will receive a notification with no sound.
 * @param protectContent Protects the contents of the sent message from forwarding and saving
 * @param allowPaidBroadcast Pass *True* to allow up to 1000 messages per second, ignoring [broadcasting limits](https://core.telegram.org/bots/faq#how-can-i-message-all-of-my-bot-39s-subscribers-at-once) for a fee of 0.1 Telegram Stars per message. The relevant Stars will be withdrawn from the bot's balance
 * @param messageEffectId Unique identifier of the message effect to be added to the message; for private chats only
 * @param replyParameters Description of the message to reply to
 * @param replyMarkup Additional interface options. A JSON-serialized object for an [inline keyboard](https://core.telegram.org/bots/features#inline-keyboards), [custom reply keyboard](https://core.telegram.org/bots/features#keyboards), instructions to remove a reply keyboard or to force a reply from the user
 */
public suspend fun TelegramBotApi.sendPoll(
    businessConnectionId: String? = null,
    chatId: String,
    messageThreadId: Long? = null,
    question: String,
    questionParseMode: String? = null,
    questionEntities: List<MessageEntity>? = null,
    options: List<InputPollOption>,
    isAnonymous: Boolean? = null,
    type: String? = null,
    allowsMultipleAnswers: Boolean? = null,
    correctOptionId: Long? = null,
    explanation: String? = null,
    explanationParseMode: String? = null,
    explanationEntities: List<MessageEntity>? = null,
    openPeriod: Long? = null,
    closeDate: Long? = null,
    isClosed: Boolean? = null,
    disableNotification: Boolean? = null,
    protectContent: Boolean? = null,
    allowPaidBroadcast: Boolean? = null,
    messageEffectId: String? = null,
    replyParameters: ReplyParameters? = null,
    replyMarkup: ReplyMarkup? = null,
): TelegramResponse<Message> {
    val request = SendPollRequest(
        businessConnectionId = businessConnectionId,
        chatId = chatId,
        messageThreadId = messageThreadId,
        question = question,
        questionParseMode = questionParseMode,
        questionEntities = questionEntities,
        options = options,
        isAnonymous = isAnonymous,
        type = type,
        allowsMultipleAnswers = allowsMultipleAnswers,
        correctOptionId = correctOptionId,
        explanation = explanation,
        explanationParseMode = explanationParseMode,
        explanationEntities = explanationEntities,
        openPeriod = openPeriod,
        closeDate = closeDate,
        isClosed = isClosed,
        disableNotification = disableNotification,
        protectContent = protectContent,
        allowPaidBroadcast = allowPaidBroadcast,
        messageEffectId = messageEffectId,
        replyParameters = replyParameters,
        replyMarkup = replyMarkup
    )
    return sendPoll(request)
}

/**
 * Use this method to send a checklist on behalf of a connected business account. On success, the sent Message is returned.
 *
 * @param businessConnectionId Unique identifier of the business connection on behalf of which the message will be sent
 * @param chatId Unique identifier for the target chat
 * @param checklist A JSON-serialized object for the checklist to send
 * @param disableNotification Sends the message silently. Users will receive a notification with no sound.
 * @param protectContent Protects the contents of the sent message from forwarding and saving
 * @param messageEffectId Unique identifier of the message effect to be added to the message
 * @param replyParameters A JSON-serialized object for description of the message to reply to
 * @param replyMarkup A JSON-serialized object for an inline keyboard
 */
public suspend fun TelegramBotApi.sendChecklist(
    businessConnectionId: String,
    chatId: Long,
    checklist: InputChecklist,
    disableNotification: Boolean? = null,
    protectContent: Boolean? = null,
    messageEffectId: String? = null,
    replyParameters: ReplyParameters? = null,
    replyMarkup: InlineKeyboardMarkup? = null,
): TelegramResponse<Message> {
    val request = SendChecklistRequest(
        businessConnectionId = businessConnectionId,
        chatId = chatId,
        checklist = checklist,
        disableNotification = disableNotification,
        protectContent = protectContent,
        messageEffectId = messageEffectId,
        replyParameters = replyParameters,
        replyMarkup = replyMarkup
    )
    return sendChecklist(request)
}

/**
 * Use this method to send an animated emoji that will display a random value. On success, the sent Message is returned.
 *
 * @param businessConnectionId Unique identifier of the business connection on behalf of which the message will be sent
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format `@channelusername`)
 * @param messageThreadId Unique identifier for the target message thread (topic) of a forum; for forum supergroups and private chats of bots with forum topic mode enabled only
 * @param directMessagesTopicId Identifier of the direct messages topic to which the message will be sent; required if the message is sent to a direct messages chat
 * @param emoji Emoji on which the dice throw animation is based. Currently, must be one of “”, “”, “”, “”, “”, or “”. Dice can have values 1-6 for “”, “” and “”, values 1-5 for “” and “”, and values 1-64 for “”. Defaults to “”
 * @param disableNotification Sends the message [silently](https://telegram.org/blog/channels-2-0#silent-messages). Users will receive a notification with no sound.
 * @param protectContent Protects the contents of the sent message from forwarding
 * @param allowPaidBroadcast Pass *True* to allow up to 1000 messages per second, ignoring [broadcasting limits](https://core.telegram.org/bots/faq#how-can-i-message-all-of-my-bot-39s-subscribers-at-once) for a fee of 0.1 Telegram Stars per message. The relevant Stars will be withdrawn from the bot's balance
 * @param messageEffectId Unique identifier of the message effect to be added to the message; for private chats only
 * @param suggestedPostParameters A JSON-serialized object containing the parameters of the suggested post to send; for direct messages chats only. If the message is sent as a reply to another suggested post, then that suggested post is automatically declined.
 * @param replyParameters Description of the message to reply to
 * @param replyMarkup Additional interface options. A JSON-serialized object for an [inline keyboard](https://core.telegram.org/bots/features#inline-keyboards), [custom reply keyboard](https://core.telegram.org/bots/features#keyboards), instructions to remove a reply keyboard or to force a reply from the user
 */
public suspend fun TelegramBotApi.sendDice(
    businessConnectionId: String? = null,
    chatId: String,
    messageThreadId: Long? = null,
    directMessagesTopicId: Long? = null,
    emoji: String? = null,
    disableNotification: Boolean? = null,
    protectContent: Boolean? = null,
    allowPaidBroadcast: Boolean? = null,
    messageEffectId: String? = null,
    suggestedPostParameters: SuggestedPostParameters? = null,
    replyParameters: ReplyParameters? = null,
    replyMarkup: ReplyMarkup? = null,
): TelegramResponse<Message> {
    val request = SendDiceRequest(
        businessConnectionId = businessConnectionId,
        chatId = chatId,
        messageThreadId = messageThreadId,
        directMessagesTopicId = directMessagesTopicId,
        emoji = emoji,
        disableNotification = disableNotification,
        protectContent = protectContent,
        allowPaidBroadcast = allowPaidBroadcast,
        messageEffectId = messageEffectId,
        suggestedPostParameters = suggestedPostParameters,
        replyParameters = replyParameters,
        replyMarkup = replyMarkup
    )
    return sendDice(request)
}

/**
 * Use this method to stream a partial message to a user while the message is being generated; supported only for bots with forum topic mode enabled. Returns True on success.
 *
 * @param chatId Unique identifier for the target private chat
 * @param messageThreadId Unique identifier for the target message thread
 * @param draftId Unique identifier of the message draft; must be non-zero. Changes of drafts with the same identifier are animated
 * @param text Text of the message to be sent, 1-4096 characters after entities parsing
 * @param parseMode Mode for parsing entities in the message text. See [formatting options](https://core.telegram.org/bots/api#formatting-options) for more details.
 * @param entities A JSON-serialized list of special entities that appear in message text, which can be specified instead of *parse_mode*
 */
public suspend fun TelegramBotApi.sendMessageDraft(
    chatId: Long,
    messageThreadId: Long? = null,
    draftId: Long,
    text: String,
    parseMode: String? = null,
    entities: List<MessageEntity>? = null,
): TelegramResponse<Boolean> {
    val request = SendMessageDraftRequest(
        chatId = chatId,
        messageThreadId = messageThreadId,
        draftId = draftId,
        text = text,
        parseMode = parseMode,
        entities = entities
    )
    return sendMessageDraft(request)
}

/**
 * Use this method when you need to tell the user that something is happening on the bot's side. The status is set for 5 seconds or less (when a message arrives from your bot, Telegram clients clear its typing status). Returns True on success.
 * Example: The ImageBot needs some time to process a request and upload the image. Instead of sending a text message along the lines of “Retrieving image, please wait…”, the bot may use sendChatAction with action = upload_photo. The user will see a “sending photo” status for the bot.
 * We only recommend using this method when a response from the bot will take a noticeable amount of time to arrive.
 *
 * @param businessConnectionId Unique identifier of the business connection on behalf of which the action will be sent
 * @param chatId Unique identifier for the target chat or username of the target supergroup (in the format `@supergroupusername`). Channel chats and channel direct messages chats aren't supported.
 * @param messageThreadId Unique identifier for the target message thread or topic of a forum; for supergroups and private chats of bots with forum topic mode enabled only
 * @param action Type of action to broadcast. Choose one, depending on what the user is about to receive: *typing* for [text messages](https://core.telegram.org/bots/api#sendmessage), *upload_photo* for [photos](https://core.telegram.org/bots/api#sendphoto), *record_video* or *upload_video* for [videos](https://core.telegram.org/bots/api#sendvideo), *record_voice* or *upload_voice* for [voice notes](https://core.telegram.org/bots/api#sendvoice), *upload_document* for [general files](https://core.telegram.org/bots/api#senddocument), *choose_sticker* for [stickers](https://core.telegram.org/bots/api#sendsticker), *find_location* for [location data](https://core.telegram.org/bots/api#sendlocation), *record_video_note* or *upload_video_note* for [video notes](https://core.telegram.org/bots/api#sendvideonote).
 */
public suspend fun TelegramBotApi.sendChatAction(
    businessConnectionId: String? = null,
    chatId: String,
    messageThreadId: Long? = null,
    action: String,
): TelegramResponse<Boolean> {
    val request = SendChatActionRequest(
        businessConnectionId = businessConnectionId,
        chatId = chatId,
        messageThreadId = messageThreadId,
        action = action
    )
    return sendChatAction(request)
}

/**
 * Use this method to change the chosen reactions on a message. Service messages of some types can't be reacted to. Automatically forwarded messages from a channel to its discussion group have the same available reactions as messages in the channel. Bots can't use paid reactions. Returns True on success.
 *
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format `@channelusername`)
 * @param messageId Identifier of the target message. If the message belongs to a media group, the reaction is set to the first non-deleted message in the group instead.
 * @param reaction A JSON-serialized list of reaction types to set on the message. Currently, as non-premium users, bots can set up to one reaction per message. A custom emoji reaction can be used if it is either already present on the message or explicitly allowed by chat administrators. Paid reactions can't be used by bots.
 * @param isBig Pass *True* to set the reaction with a big animation
 */
public suspend fun TelegramBotApi.setMessageReaction(
    chatId: String,
    messageId: Long,
    reaction: List<ReactionType>? = null,
    isBig: Boolean? = null,
): TelegramResponse<Boolean> {
    val request = SetMessageReactionRequest(
        chatId = chatId,
        messageId = messageId,
        reaction = reaction,
        isBig = isBig
    )
    return setMessageReaction(request)
}

/**
 * Changes the emoji status for a given user that previously allowed the bot to manage their emoji status via the Mini App method requestEmojiStatusAccess. Returns True on success.
 *
 * @param userId Unique identifier of the target user
 * @param emojiStatusCustomEmojiId Custom emoji identifier of the emoji status to set. Pass an empty string to remove the status.
 * @param emojiStatusExpirationDate Expiration date of the emoji status, if any
 */
public suspend fun TelegramBotApi.setUserEmojiStatus(
    userId: Long,
    emojiStatusCustomEmojiId: String? = null,
    emojiStatusExpirationDate: Long? = null,
): TelegramResponse<Boolean> {
    val request = SetUserEmojiStatusRequest(
        userId = userId,
        emojiStatusCustomEmojiId = emojiStatusCustomEmojiId,
        emojiStatusExpirationDate = emojiStatusExpirationDate
    )
    return setUserEmojiStatus(request)
}

/**
 * Use this method to ban a user in a group, a supergroup or a channel. In the case of supergroups and channels, the user will not be able to return to the chat on their own using invite links, etc., unless unbanned first. The bot must be an administrator in the chat for this to work and must have the appropriate administrator rights. Returns True on success.
 *
 * @param chatId Unique identifier for the target group or username of the target supergroup or channel (in the format `@channelusername`)
 * @param userId Unique identifier of the target user
 * @param untilDate Date when the user will be unbanned; Unix time. If user is banned for more than 366 days or less than 30 seconds from the current time they are considered to be banned forever. Applied for supergroups and channels only.
 * @param revokeMessages Pass *True* to delete all messages from the chat for the user that is being removed. If *False*, the user will be able to see messages in the group that were sent before the user was removed. Always *True* for supergroups and channels.
 */
public suspend fun TelegramBotApi.banChatMember(
    chatId: String,
    userId: Long,
    untilDate: Long? = null,
    revokeMessages: Boolean? = null,
): TelegramResponse<Boolean> {
    val request = BanChatMemberRequest(
        chatId = chatId,
        userId = userId,
        untilDate = untilDate,
        revokeMessages = revokeMessages
    )
    return banChatMember(request)
}

/**
 * Use this method to unban a previously banned user in a supergroup or channel. The user will not return to the group or channel automatically, but will be able to join via link, etc. The bot must be an administrator for this to work. By default, this method guarantees that after the call the user is not a member of the chat, but will be able to join it. So if the user is a member of the chat they will also be removed from the chat. If you don't want this, use the parameter only_if_banned. Returns True on success.
 *
 * @param chatId Unique identifier for the target group or username of the target supergroup or channel (in the format `@channelusername`)
 * @param userId Unique identifier of the target user
 * @param onlyIfBanned Do nothing if the user is not banned
 */
public suspend fun TelegramBotApi.unbanChatMember(
    chatId: String,
    userId: Long,
    onlyIfBanned: Boolean? = null,
): TelegramResponse<Boolean> {
    val request = UnbanChatMemberRequest(
        chatId = chatId,
        userId = userId,
        onlyIfBanned = onlyIfBanned
    )
    return unbanChatMember(request)
}

/**
 * Use this method to restrict a user in a supergroup. The bot must be an administrator in the supergroup for this to work and must have the appropriate administrator rights. Pass True for all permissions to lift restrictions from a user. Returns True on success.
 *
 * @param chatId Unique identifier for the target chat or username of the target supergroup (in the format `@supergroupusername`)
 * @param userId Unique identifier of the target user
 * @param permissions A JSON-serialized object for new user permissions
 * @param useIndependentChatPermissions Pass *True* if chat permissions are set independently. Otherwise, the *can_send_other_messages* and *can_add_web_page_previews* permissions will imply the *can_send_messages*, *can_send_audios*, *can_send_documents*, *can_send_photos*, *can_send_videos*, *can_send_video_notes*, and *can_send_voice_notes* permissions; the *can_send_polls* permission will imply the *can_send_messages* permission.
 * @param untilDate Date when restrictions will be lifted for the user; Unix time. If user is restricted for more than 366 days or less than 30 seconds from the current time, they are considered to be restricted forever
 */
public suspend fun TelegramBotApi.restrictChatMember(
    chatId: String,
    userId: Long,
    permissions: ChatPermissions,
    useIndependentChatPermissions: Boolean? = null,
    untilDate: Long? = null,
): TelegramResponse<Boolean> {
    val request = RestrictChatMemberRequest(
        chatId = chatId,
        userId = userId,
        permissions = permissions,
        useIndependentChatPermissions = useIndependentChatPermissions,
        untilDate = untilDate
    )
    return restrictChatMember(request)
}

/**
 * Use this method to promote or demote a user in a supergroup or a channel. The bot must be an administrator in the chat for this to work and must have the appropriate administrator rights. Pass False for all boolean parameters to demote a user. Returns True on success.
 *
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format `@channelusername`)
 * @param userId Unique identifier of the target user
 * @param isAnonymous Pass *True* if the administrator's presence in the chat is hidden
 * @param canManageChat Pass *True* if the administrator can access the chat event log, get boost list, see hidden supergroup and channel members, report spam messages, ignore slow mode, and send messages to the chat without paying Telegram Stars. Implied by any other administrator privilege.
 * @param canDeleteMessages Pass *True* if the administrator can delete messages of other users
 * @param canManageVideoChats Pass *True* if the administrator can manage video chats
 * @param canRestrictMembers Pass *True* if the administrator can restrict, ban or unban chat members, or access supergroup statistics. For backward compatibility, defaults to *True* for promotions of channel administrators
 * @param canPromoteMembers Pass *True* if the administrator can add new administrators with a subset of their own privileges or demote administrators that they have promoted, directly or indirectly (promoted by administrators that were appointed by him)
 * @param canChangeInfo Pass *True* if the administrator can change chat title, photo and other settings
 * @param canInviteUsers Pass *True* if the administrator can invite new users to the chat
 * @param canPostStories Pass *True* if the administrator can post stories to the chat
 * @param canEditStories Pass *True* if the administrator can edit stories posted by other users, post stories to the chat page, pin chat stories, and access the chat's story archive
 * @param canDeleteStories Pass *True* if the administrator can delete stories posted by other users
 * @param canPostMessages Pass *True* if the administrator can post messages in the channel, approve suggested posts, or access channel statistics; for channels only
 * @param canEditMessages Pass *True* if the administrator can edit messages of other users and can pin messages; for channels only
 * @param canPinMessages Pass *True* if the administrator can pin messages; for supergroups only
 * @param canManageTopics Pass *True* if the user is allowed to create, rename, close, and reopen forum topics; for supergroups only
 * @param canManageDirectMessages Pass *True* if the administrator can manage direct messages within the channel and decline suggested posts; for channels only
 */
public suspend fun TelegramBotApi.promoteChatMember(
    chatId: String,
    userId: Long,
    isAnonymous: Boolean? = null,
    canManageChat: Boolean? = null,
    canDeleteMessages: Boolean? = null,
    canManageVideoChats: Boolean? = null,
    canRestrictMembers: Boolean? = null,
    canPromoteMembers: Boolean? = null,
    canChangeInfo: Boolean? = null,
    canInviteUsers: Boolean? = null,
    canPostStories: Boolean? = null,
    canEditStories: Boolean? = null,
    canDeleteStories: Boolean? = null,
    canPostMessages: Boolean? = null,
    canEditMessages: Boolean? = null,
    canPinMessages: Boolean? = null,
    canManageTopics: Boolean? = null,
    canManageDirectMessages: Boolean? = null,
): TelegramResponse<Boolean> {
    val request = PromoteChatMemberRequest(
        chatId = chatId,
        userId = userId,
        isAnonymous = isAnonymous,
        canManageChat = canManageChat,
        canDeleteMessages = canDeleteMessages,
        canManageVideoChats = canManageVideoChats,
        canRestrictMembers = canRestrictMembers,
        canPromoteMembers = canPromoteMembers,
        canChangeInfo = canChangeInfo,
        canInviteUsers = canInviteUsers,
        canPostStories = canPostStories,
        canEditStories = canEditStories,
        canDeleteStories = canDeleteStories,
        canPostMessages = canPostMessages,
        canEditMessages = canEditMessages,
        canPinMessages = canPinMessages,
        canManageTopics = canManageTopics,
        canManageDirectMessages = canManageDirectMessages
    )
    return promoteChatMember(request)
}

/**
 * Use this method to set a custom title for an administrator in a supergroup promoted by the bot. Returns True on success.
 *
 * @param chatId Unique identifier for the target chat or username of the target supergroup (in the format `@supergroupusername`)
 * @param userId Unique identifier of the target user
 * @param customTitle New custom title for the administrator; 0-16 characters, emoji are not allowed
 */
public suspend fun TelegramBotApi.setChatAdministratorCustomTitle(
    chatId: String,
    userId: Long,
    customTitle: String,
): TelegramResponse<Boolean> {
    val request = SetChatAdministratorCustomTitleRequest(
        chatId = chatId,
        userId = userId,
        customTitle = customTitle
    )
    return setChatAdministratorCustomTitle(request)
}

/**
 * Use this method to ban a channel chat in a supergroup or a channel. Until the chat is unbanned, the owner of the banned chat won't be able to send messages on behalf of any of their channels. The bot must be an administrator in the supergroup or channel for this to work and must have the appropriate administrator rights. Returns True on success.
 *
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format `@channelusername`)
 * @param senderChatId Unique identifier of the target sender chat
 */
public suspend fun TelegramBotApi.banChatSenderChat(chatId: String, senderChatId: Long): TelegramResponse<Boolean> {
    val request = BanChatSenderChatRequest(
        chatId = chatId,
        senderChatId = senderChatId
    )
    return banChatSenderChat(request)
}

/**
 * Use this method to unban a previously banned channel chat in a supergroup or channel. The bot must be an administrator for this to work and must have the appropriate administrator rights. Returns True on success.
 *
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format `@channelusername`)
 * @param senderChatId Unique identifier of the target sender chat
 */
public suspend fun TelegramBotApi.unbanChatSenderChat(chatId: String, senderChatId: Long): TelegramResponse<Boolean> {
    val request = UnbanChatSenderChatRequest(
        chatId = chatId,
        senderChatId = senderChatId
    )
    return unbanChatSenderChat(request)
}

/**
 * Use this method to set default chat permissions for all members. The bot must be an administrator in the group or a supergroup for this to work and must have the can_restrict_members administrator rights. Returns True on success.
 *
 * @param chatId Unique identifier for the target chat or username of the target supergroup (in the format `@supergroupusername`)
 * @param permissions A JSON-serialized object for new default chat permissions
 * @param useIndependentChatPermissions Pass *True* if chat permissions are set independently. Otherwise, the *can_send_other_messages* and *can_add_web_page_previews* permissions will imply the *can_send_messages*, *can_send_audios*, *can_send_documents*, *can_send_photos*, *can_send_videos*, *can_send_video_notes*, and *can_send_voice_notes* permissions; the *can_send_polls* permission will imply the *can_send_messages* permission.
 */
public suspend fun TelegramBotApi.setChatPermissions(
    chatId: String,
    permissions: ChatPermissions,
    useIndependentChatPermissions: Boolean? = null,
): TelegramResponse<Boolean> {
    val request = SetChatPermissionsRequest(
        chatId = chatId,
        permissions = permissions,
        useIndependentChatPermissions = useIndependentChatPermissions
    )
    return setChatPermissions(request)
}

/**
 * Use this method to generate a new primary invite link for a chat; any previously generated primary link is revoked. The bot must be an administrator in the chat for this to work and must have the appropriate administrator rights. Returns the new invite link as String on success.
 * Note: Each administrator in a chat generates their own invite links. Bots can't use invite links generated by other administrators. If you want your bot to work with invite links, it will need to generate its own link using exportChatInviteLink or by calling the getChat method. If your bot needs to generate a new primary invite link replacing its previous one, use exportChatInviteLink again.
 *
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format `@channelusername`)
 */
public suspend fun TelegramBotApi.exportChatInviteLink(chatId: String): TelegramResponse<String> {
    val request = ExportChatInviteLinkRequest(
        chatId = chatId
    )
    return exportChatInviteLink(request)
}

/**
 * Use this method to create an additional invite link for a chat. The bot must be an administrator in the chat for this to work and must have the appropriate administrator rights. The link can be revoked using the method revokeChatInviteLink. Returns the new invite link as ChatInviteLink object.
 *
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format `@channelusername`)
 * @param name Invite link name; 0-32 characters
 * @param expireDate Point in time (Unix timestamp) when the link will expire
 * @param memberLimit The maximum number of users that can be members of the chat simultaneously after joining the chat via this invite link; 1-99999
 * @param createsJoinRequest *True*, if users joining the chat via the link need to be approved by chat administrators. If *True*, *member_limit* can't be specified
 */
public suspend fun TelegramBotApi.createChatInviteLink(
    chatId: String,
    name: String? = null,
    expireDate: Long? = null,
    memberLimit: Long? = null,
    createsJoinRequest: Boolean? = null,
): TelegramResponse<ChatInviteLink> {
    val request = CreateChatInviteLinkRequest(
        chatId = chatId,
        name = name,
        expireDate = expireDate,
        memberLimit = memberLimit,
        createsJoinRequest = createsJoinRequest
    )
    return createChatInviteLink(request)
}

/**
 * Use this method to edit a non-primary invite link created by the bot. The bot must be an administrator in the chat for this to work and must have the appropriate administrator rights. Returns the edited invite link as a ChatInviteLink object.
 *
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format `@channelusername`)
 * @param inviteLink The invite link to edit
 * @param name Invite link name; 0-32 characters
 * @param expireDate Point in time (Unix timestamp) when the link will expire
 * @param memberLimit The maximum number of users that can be members of the chat simultaneously after joining the chat via this invite link; 1-99999
 * @param createsJoinRequest *True*, if users joining the chat via the link need to be approved by chat administrators. If *True*, *member_limit* can't be specified
 */
public suspend fun TelegramBotApi.editChatInviteLink(
    chatId: String,
    inviteLink: String,
    name: String? = null,
    expireDate: Long? = null,
    memberLimit: Long? = null,
    createsJoinRequest: Boolean? = null,
): TelegramResponse<ChatInviteLink> {
    val request = EditChatInviteLinkRequest(
        chatId = chatId,
        inviteLink = inviteLink,
        name = name,
        expireDate = expireDate,
        memberLimit = memberLimit,
        createsJoinRequest = createsJoinRequest
    )
    return editChatInviteLink(request)
}

/**
 * Use this method to create a subscription invite link for a channel chat. The bot must have the can_invite_users administrator rights. The link can be edited using the method editChatSubscriptionInviteLink or revoked using the method revokeChatInviteLink. Returns the new invite link as a ChatInviteLink object.
 *
 * @param chatId Unique identifier for the target channel chat or username of the target channel (in the format `@channelusername`)
 * @param name Invite link name; 0-32 characters
 * @param subscriptionPeriod The number of seconds the subscription will be active for before the next payment. Currently, it must always be 2592000 (30 days).
 * @param subscriptionPrice The amount of Telegram Stars a user must pay initially and after each subsequent subscription period to be a member of the chat; 1-10000
 */
public suspend fun TelegramBotApi.createChatSubscriptionInviteLink(
    chatId: String,
    name: String? = null,
    subscriptionPeriod: Long,
    subscriptionPrice: Long,
): TelegramResponse<ChatInviteLink> {
    val request = CreateChatSubscriptionInviteLinkRequest(
        chatId = chatId,
        name = name,
        subscriptionPeriod = subscriptionPeriod,
        subscriptionPrice = subscriptionPrice
    )
    return createChatSubscriptionInviteLink(request)
}

/**
 * Use this method to edit a subscription invite link created by the bot. The bot must have the can_invite_users administrator rights. Returns the edited invite link as a ChatInviteLink object.
 *
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format `@channelusername`)
 * @param inviteLink The invite link to edit
 * @param name Invite link name; 0-32 characters
 */
public suspend fun TelegramBotApi.editChatSubscriptionInviteLink(
    chatId: String,
    inviteLink: String,
    name: String? = null,
): TelegramResponse<ChatInviteLink> {
    val request = EditChatSubscriptionInviteLinkRequest(
        chatId = chatId,
        inviteLink = inviteLink,
        name = name
    )
    return editChatSubscriptionInviteLink(request)
}

/**
 * Use this method to revoke an invite link created by the bot. If the primary link is revoked, a new link is automatically generated. The bot must be an administrator in the chat for this to work and must have the appropriate administrator rights. Returns the revoked invite link as ChatInviteLink object.
 *
 * @param chatId Unique identifier of the target chat or username of the target channel (in the format `@channelusername`)
 * @param inviteLink The invite link to revoke
 */
public suspend fun TelegramBotApi.revokeChatInviteLink(chatId: String, inviteLink: String): TelegramResponse<ChatInviteLink> {
    val request = RevokeChatInviteLinkRequest(
        chatId = chatId,
        inviteLink = inviteLink
    )
    return revokeChatInviteLink(request)
}

/**
 * Use this method to approve a chat join request. The bot must be an administrator in the chat for this to work and must have the can_invite_users administrator right. Returns True on success.
 *
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format `@channelusername`)
 * @param userId Unique identifier of the target user
 */
public suspend fun TelegramBotApi.approveChatJoinRequest(chatId: String, userId: Long): TelegramResponse<Boolean> {
    val request = ApproveChatJoinRequestRequest(
        chatId = chatId,
        userId = userId
    )
    return approveChatJoinRequest(request)
}

/**
 * Use this method to decline a chat join request. The bot must be an administrator in the chat for this to work and must have the can_invite_users administrator right. Returns True on success.
 *
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format `@channelusername`)
 * @param userId Unique identifier of the target user
 */
public suspend fun TelegramBotApi.declineChatJoinRequest(chatId: String, userId: Long): TelegramResponse<Boolean> {
    val request = DeclineChatJoinRequestRequest(
        chatId = chatId,
        userId = userId
    )
    return declineChatJoinRequest(request)
}

/**
 * Use this method to delete a chat photo. Photos can't be changed for private chats. The bot must be an administrator in the chat for this to work and must have the appropriate administrator rights. Returns True on success.
 *
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format `@channelusername`)
 */
public suspend fun TelegramBotApi.deleteChatPhoto(chatId: String): TelegramResponse<Boolean> {
    val request = DeleteChatPhotoRequest(
        chatId = chatId
    )
    return deleteChatPhoto(request)
}

/**
 * Use this method to change the title of a chat. Titles can't be changed for private chats. The bot must be an administrator in the chat for this to work and must have the appropriate administrator rights. Returns True on success.
 *
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format `@channelusername`)
 * @param title New chat title, 1-128 characters
 */
public suspend fun TelegramBotApi.setChatTitle(chatId: String, title: String): TelegramResponse<Boolean> {
    val request = SetChatTitleRequest(
        chatId = chatId,
        title = title
    )
    return setChatTitle(request)
}

/**
 * Use this method to change the description of a group, a supergroup or a channel. The bot must be an administrator in the chat for this to work and must have the appropriate administrator rights. Returns True on success.
 *
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format `@channelusername`)
 * @param description New chat description, 0-255 characters
 */
public suspend fun TelegramBotApi.setChatDescription(chatId: String, description: String? = null): TelegramResponse<Boolean> {
    val request = SetChatDescriptionRequest(
        chatId = chatId,
        description = description
    )
    return setChatDescription(request)
}

/**
 * Use this method to add a message to the list of pinned messages in a chat. In private chats and channel direct messages chats, all non-service messages can be pinned. Conversely, the bot must be an administrator with the 'can_pin_messages' right or the 'can_edit_messages' right to pin messages in groups and channels respectively. Returns True on success.
 *
 * @param businessConnectionId Unique identifier of the business connection on behalf of which the message will be pinned
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format `@channelusername`)
 * @param messageId Identifier of a message to pin
 * @param disableNotification Pass *True* if it is not necessary to send a notification to all chat members about the new pinned message. Notifications are always disabled in channels and private chats.
 */
public suspend fun TelegramBotApi.pinChatMessage(
    businessConnectionId: String? = null,
    chatId: String,
    messageId: Long,
    disableNotification: Boolean? = null,
): TelegramResponse<Boolean> {
    val request = PinChatMessageRequest(
        businessConnectionId = businessConnectionId,
        chatId = chatId,
        messageId = messageId,
        disableNotification = disableNotification
    )
    return pinChatMessage(request)
}

/**
 * Use this method to remove a message from the list of pinned messages in a chat. In private chats and channel direct messages chats, all messages can be unpinned. Conversely, the bot must be an administrator with the 'can_pin_messages' right or the 'can_edit_messages' right to unpin messages in groups and channels respectively. Returns True on success.
 *
 * @param businessConnectionId Unique identifier of the business connection on behalf of which the message will be unpinned
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format `@channelusername`)
 * @param messageId Identifier of the message to unpin. Required if *business_connection_id* is specified. If not specified, the most recent pinned message (by sending date) will be unpinned.
 */
public suspend fun TelegramBotApi.unpinChatMessage(
    businessConnectionId: String? = null,
    chatId: String,
    messageId: Long? = null,
): TelegramResponse<Boolean> {
    val request = UnpinChatMessageRequest(
        businessConnectionId = businessConnectionId,
        chatId = chatId,
        messageId = messageId
    )
    return unpinChatMessage(request)
}

/**
 * Use this method to clear the list of pinned messages in a chat. In private chats and channel direct messages chats, no additional rights are required to unpin all pinned messages. Conversely, the bot must be an administrator with the 'can_pin_messages' right or the 'can_edit_messages' right to unpin all pinned messages in groups and channels respectively. Returns True on success.
 *
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format `@channelusername`)
 */
public suspend fun TelegramBotApi.unpinAllChatMessages(chatId: String): TelegramResponse<Boolean> {
    val request = UnpinAllChatMessagesRequest(
        chatId = chatId
    )
    return unpinAllChatMessages(request)
}

/**
 * Use this method for your bot to leave a group, supergroup or channel. Returns True on success.
 *
 * @param chatId Unique identifier for the target chat or username of the target supergroup or channel (in the format `@channelusername`). Channel direct messages chats aren't supported; leave the corresponding channel instead.
 */
public suspend fun TelegramBotApi.leaveChat(chatId: String): TelegramResponse<Boolean> {
    val request = LeaveChatRequest(
        chatId = chatId
    )
    return leaveChat(request)
}

/**
 * Use this method to set a new group sticker set for a supergroup. The bot must be an administrator in the chat for this to work and must have the appropriate administrator rights. Use the field can_set_sticker_set optionally returned in getChat requests to check if the bot can use this method. Returns True on success.
 *
 * @param chatId Unique identifier for the target chat or username of the target supergroup (in the format `@supergroupusername`)
 * @param stickerSetName Name of the sticker set to be set as the group sticker set
 */
public suspend fun TelegramBotApi.setChatStickerSet(chatId: String, stickerSetName: String): TelegramResponse<Boolean> {
    val request = SetChatStickerSetRequest(
        chatId = chatId,
        stickerSetName = stickerSetName
    )
    return setChatStickerSet(request)
}

/**
 * Use this method to delete a group sticker set from a supergroup. The bot must be an administrator in the chat for this to work and must have the appropriate administrator rights. Use the field can_set_sticker_set optionally returned in getChat requests to check if the bot can use this method. Returns True on success.
 *
 * @param chatId Unique identifier for the target chat or username of the target supergroup (in the format `@supergroupusername`)
 */
public suspend fun TelegramBotApi.deleteChatStickerSet(chatId: String): TelegramResponse<Boolean> {
    val request = DeleteChatStickerSetRequest(
        chatId = chatId
    )
    return deleteChatStickerSet(request)
}

/**
 * Use this method to create a topic in a forum supergroup chat or a private chat with a user. In the case of a supergroup chat the bot must be an administrator in the chat for this to work and must have the can_manage_topics administrator right. Returns information about the created topic as a ForumTopic object.
 *
 * @param chatId Unique identifier for the target chat or username of the target supergroup (in the format `@supergroupusername`)
 * @param name Topic name, 1-128 characters
 * @param iconColor Color of the topic icon in RGB format. Currently, must be one of 7322096 (0x6FB9F0), 16766590 (0xFFD67E), 13338331 (0xCB86DB), 9367192 (0x8EEE98), 16749490 (0xFF93B2), or 16478047 (0xFB6F5F)
 * @param iconCustomEmojiId Unique identifier of the custom emoji shown as the topic icon. Use [getForumTopicIconStickers](https://core.telegram.org/bots/api#getforumtopiciconstickers) to get all allowed custom emoji identifiers.
 */
public suspend fun TelegramBotApi.createForumTopic(
    chatId: String,
    name: String,
    iconColor: Long? = null,
    iconCustomEmojiId: String? = null,
): TelegramResponse<ForumTopic> {
    val request = CreateForumTopicRequest(
        chatId = chatId,
        name = name,
        iconColor = iconColor,
        iconCustomEmojiId = iconCustomEmojiId
    )
    return createForumTopic(request)
}

/**
 * Use this method to edit name and icon of a topic in a forum supergroup chat or a private chat with a user. In the case of a supergroup chat the bot must be an administrator in the chat for this to work and must have the can_manage_topics administrator rights, unless it is the creator of the topic. Returns True on success.
 *
 * @param chatId Unique identifier for the target chat or username of the target supergroup (in the format `@supergroupusername`)
 * @param messageThreadId Unique identifier for the target message thread of the forum topic
 * @param name New topic name, 0-128 characters. If not specified or empty, the current name of the topic will be kept
 * @param iconCustomEmojiId New unique identifier of the custom emoji shown as the topic icon. Use [getForumTopicIconStickers](https://core.telegram.org/bots/api#getforumtopiciconstickers) to get all allowed custom emoji identifiers. Pass an empty string to remove the icon. If not specified, the current icon will be kept
 */
public suspend fun TelegramBotApi.editForumTopic(
    chatId: String,
    messageThreadId: Long,
    name: String? = null,
    iconCustomEmojiId: String? = null,
): TelegramResponse<Boolean> {
    val request = EditForumTopicRequest(
        chatId = chatId,
        messageThreadId = messageThreadId,
        name = name,
        iconCustomEmojiId = iconCustomEmojiId
    )
    return editForumTopic(request)
}

/**
 * Use this method to close an open topic in a forum supergroup chat. The bot must be an administrator in the chat for this to work and must have the can_manage_topics administrator rights, unless it is the creator of the topic. Returns True on success.
 *
 * @param chatId Unique identifier for the target chat or username of the target supergroup (in the format `@supergroupusername`)
 * @param messageThreadId Unique identifier for the target message thread of the forum topic
 */
public suspend fun TelegramBotApi.closeForumTopic(chatId: String, messageThreadId: Long): TelegramResponse<Boolean> {
    val request = CloseForumTopicRequest(
        chatId = chatId,
        messageThreadId = messageThreadId
    )
    return closeForumTopic(request)
}

/**
 * Use this method to reopen a closed topic in a forum supergroup chat. The bot must be an administrator in the chat for this to work and must have the can_manage_topics administrator rights, unless it is the creator of the topic. Returns True on success.
 *
 * @param chatId Unique identifier for the target chat or username of the target supergroup (in the format `@supergroupusername`)
 * @param messageThreadId Unique identifier for the target message thread of the forum topic
 */
public suspend fun TelegramBotApi.reopenForumTopic(chatId: String, messageThreadId: Long): TelegramResponse<Boolean> {
    val request = ReopenForumTopicRequest(
        chatId = chatId,
        messageThreadId = messageThreadId
    )
    return reopenForumTopic(request)
}

/**
 * Use this method to delete a forum topic along with all its messages in a forum supergroup chat or a private chat with a user. In the case of a supergroup chat the bot must be an administrator in the chat for this to work and must have the can_delete_messages administrator rights. Returns True on success.
 *
 * @param chatId Unique identifier for the target chat or username of the target supergroup (in the format `@supergroupusername`)
 * @param messageThreadId Unique identifier for the target message thread of the forum topic
 */
public suspend fun TelegramBotApi.deleteForumTopic(chatId: String, messageThreadId: Long): TelegramResponse<Boolean> {
    val request = DeleteForumTopicRequest(
        chatId = chatId,
        messageThreadId = messageThreadId
    )
    return deleteForumTopic(request)
}

/**
 * Use this method to clear the list of pinned messages in a forum topic in a forum supergroup chat or a private chat with a user. In the case of a supergroup chat the bot must be an administrator in the chat for this to work and must have the can_pin_messages administrator right in the supergroup. Returns True on success.
 *
 * @param chatId Unique identifier for the target chat or username of the target supergroup (in the format `@supergroupusername`)
 * @param messageThreadId Unique identifier for the target message thread of the forum topic
 */
public suspend fun TelegramBotApi.unpinAllForumTopicMessages(chatId: String, messageThreadId: Long): TelegramResponse<Boolean> {
    val request = UnpinAllForumTopicMessagesRequest(
        chatId = chatId,
        messageThreadId = messageThreadId
    )
    return unpinAllForumTopicMessages(request)
}

/**
 * Use this method to edit the name of the 'General' topic in a forum supergroup chat. The bot must be an administrator in the chat for this to work and must have the can_manage_topics administrator rights. Returns True on success.
 *
 * @param chatId Unique identifier for the target chat or username of the target supergroup (in the format `@supergroupusername`)
 * @param name New topic name, 1-128 characters
 */
public suspend fun TelegramBotApi.editGeneralForumTopic(chatId: String, name: String): TelegramResponse<Boolean> {
    val request = EditGeneralForumTopicRequest(
        chatId = chatId,
        name = name
    )
    return editGeneralForumTopic(request)
}

/**
 * Use this method to close an open 'General' topic in a forum supergroup chat. The bot must be an administrator in the chat for this to work and must have the can_manage_topics administrator rights. Returns True on success.
 *
 * @param chatId Unique identifier for the target chat or username of the target supergroup (in the format `@supergroupusername`)
 */
public suspend fun TelegramBotApi.closeGeneralForumTopic(chatId: String): TelegramResponse<Boolean> {
    val request = CloseGeneralForumTopicRequest(
        chatId = chatId
    )
    return closeGeneralForumTopic(request)
}

/**
 * Use this method to reopen a closed 'General' topic in a forum supergroup chat. The bot must be an administrator in the chat for this to work and must have the can_manage_topics administrator rights. The topic will be automatically unhidden if it was hidden. Returns True on success.
 *
 * @param chatId Unique identifier for the target chat or username of the target supergroup (in the format `@supergroupusername`)
 */
public suspend fun TelegramBotApi.reopenGeneralForumTopic(chatId: String): TelegramResponse<Boolean> {
    val request = ReopenGeneralForumTopicRequest(
        chatId = chatId
    )
    return reopenGeneralForumTopic(request)
}

/**
 * Use this method to hide the 'General' topic in a forum supergroup chat. The bot must be an administrator in the chat for this to work and must have the can_manage_topics administrator rights. The topic will be automatically closed if it was open. Returns True on success.
 *
 * @param chatId Unique identifier for the target chat or username of the target supergroup (in the format `@supergroupusername`)
 */
public suspend fun TelegramBotApi.hideGeneralForumTopic(chatId: String): TelegramResponse<Boolean> {
    val request = HideGeneralForumTopicRequest(
        chatId = chatId
    )
    return hideGeneralForumTopic(request)
}

/**
 * Use this method to unhide the 'General' topic in a forum supergroup chat. The bot must be an administrator in the chat for this to work and must have the can_manage_topics administrator rights. Returns True on success.
 *
 * @param chatId Unique identifier for the target chat or username of the target supergroup (in the format `@supergroupusername`)
 */
public suspend fun TelegramBotApi.unhideGeneralForumTopic(chatId: String): TelegramResponse<Boolean> {
    val request = UnhideGeneralForumTopicRequest(
        chatId = chatId
    )
    return unhideGeneralForumTopic(request)
}

/**
 * Use this method to clear the list of pinned messages in a General forum topic. The bot must be an administrator in the chat for this to work and must have the can_pin_messages administrator right in the supergroup. Returns True on success.
 *
 * @param chatId Unique identifier for the target chat or username of the target supergroup (in the format `@supergroupusername`)
 */
public suspend fun TelegramBotApi.unpinAllGeneralForumTopicMessages(chatId: String): TelegramResponse<Boolean> {
    val request = UnpinAllGeneralForumTopicMessagesRequest(
        chatId = chatId
    )
    return unpinAllGeneralForumTopicMessages(request)
}

/**
 * Use this method to send answers to callback queries sent from inline keyboards. The answer will be displayed to the user as a notification at the top of the chat screen or as an alert. On success, True is returned.
 * Alternatively, the user can be redirected to the specified Game URL. For this option to work, you must first create a game for your bot via @BotFather and accept the terms. Otherwise, you may use links like t.me/your_bot?start=XXXX that open your bot with a parameter.
 *
 * @param callbackQueryId Unique identifier for the query to be answered
 * @param text Text of the notification. If not specified, nothing will be shown to the user, 0-200 characters
 * @param showAlert If *True*, an alert will be shown by the client instead of a notification at the top of the chat screen. Defaults to *false*.
 * @param url URL that will be opened by the user's client. If you have created a [Game](https://core.telegram.org/bots/api#game) and accepted the conditions via [@BotFather](https://t.me/botfather), specify the URL that opens your game - note that this will only work if the query comes from a [callback_game](https://core.telegram.org/bots/api#inlinekeyboardbutton) button.Otherwise, you may use links like `t.me/your_bot?start=XXXX` that open your bot with a parameter.
 * @param cacheTime The maximum amount of time in seconds that the result of the callback query may be cached client-side. Telegram apps will support caching starting in version 3.14. Defaults to 0.
 */
public suspend fun TelegramBotApi.answerCallbackQuery(
    callbackQueryId: String,
    text: String? = null,
    showAlert: Boolean? = null,
    url: String? = null,
    cacheTime: Long? = null,
): TelegramResponse<Boolean> {
    val request = AnswerCallbackQueryRequest(
        callbackQueryId = callbackQueryId,
        text = text,
        showAlert = showAlert,
        url = url,
        cacheTime = cacheTime
    )
    return answerCallbackQuery(request)
}

/**
 * Use this method to change the list of the bot's commands. See this manual for more details about bot commands. Returns True on success.
 *
 * @param commands A JSON-serialized list of bot commands to be set as the list of the bot's commands. At most 100 commands can be specified.
 * @param scope A JSON-serialized object, describing scope of users for which the commands are relevant. Defaults to [BotCommandScopeDefault](https://core.telegram.org/bots/api#botcommandscopedefault).
 * @param languageCode A two-letter ISO 639-1 language code. If empty, commands will be applied to all users from the given scope, for whose language there are no dedicated commands
 */
public suspend fun TelegramBotApi.setMyCommands(
    commands: List<BotCommand>,
    scope: BotCommandScope? = null,
    languageCode: String? = null,
): TelegramResponse<Boolean> {
    val request = SetMyCommandsRequest(
        commands = commands,
        scope = scope,
        languageCode = languageCode
    )
    return setMyCommands(request)
}

/**
 * Use this method to delete the list of the bot's commands for the given scope and user language. After deletion, higher level commands will be shown to affected users. Returns True on success.
 *
 * @param scope A JSON-serialized object, describing scope of users for which the commands are relevant. Defaults to [BotCommandScopeDefault](https://core.telegram.org/bots/api#botcommandscopedefault).
 * @param languageCode A two-letter ISO 639-1 language code. If empty, commands will be applied to all users from the given scope, for whose language there are no dedicated commands
 */
public suspend fun TelegramBotApi.deleteMyCommands(scope: BotCommandScope? = null, languageCode: String? = null): TelegramResponse<Boolean> {
    val request = DeleteMyCommandsRequest(
        scope = scope,
        languageCode = languageCode
    )
    return deleteMyCommands(request)
}

/**
 * Use this method to change the bot's name. Returns True on success.
 *
 * @param name New bot name; 0-64 characters. Pass an empty string to remove the dedicated name for the given language.
 * @param languageCode A two-letter ISO 639-1 language code. If empty, the name will be shown to all users for whose language there is no dedicated name.
 */
public suspend fun TelegramBotApi.setMyName(name: String? = null, languageCode: String? = null): TelegramResponse<Boolean> {
    val request = SetMyNameRequest(
        name = name,
        languageCode = languageCode
    )
    return setMyName(request)
}

/**
 * Use this method to change the bot's description, which is shown in the chat with the bot if the chat is empty. Returns True on success.
 *
 * @param description New bot description; 0-512 characters. Pass an empty string to remove the dedicated description for the given language.
 * @param languageCode A two-letter ISO 639-1 language code. If empty, the description will be applied to all users for whose language there is no dedicated description.
 */
public suspend fun TelegramBotApi.setMyDescription(description: String? = null, languageCode: String? = null): TelegramResponse<Boolean> {
    val request = SetMyDescriptionRequest(
        description = description,
        languageCode = languageCode
    )
    return setMyDescription(request)
}

/**
 * Use this method to change the bot's short description, which is shown on the bot's profile page and is sent together with the link when users share the bot. Returns True on success.
 *
 * @param shortDescription New short description for the bot; 0-120 characters. Pass an empty string to remove the dedicated short description for the given language.
 * @param languageCode A two-letter ISO 639-1 language code. If empty, the short description will be applied to all users for whose language there is no dedicated short description.
 */
public suspend fun TelegramBotApi.setMyShortDescription(shortDescription: String? = null, languageCode: String? = null): TelegramResponse<Boolean> {
    val request = SetMyShortDescriptionRequest(
        shortDescription = shortDescription,
        languageCode = languageCode
    )
    return setMyShortDescription(request)
}

/**
 * Use this method to change the bot's menu button in a private chat, or the default menu button. Returns True on success.
 *
 * @param chatId Unique identifier for the target private chat. If not specified, default bot's menu button will be changed
 * @param menuButton A JSON-serialized object for the bot's new menu button. Defaults to [MenuButtonDefault](https://core.telegram.org/bots/api#menubuttondefault)
 */
public suspend fun TelegramBotApi.setChatMenuButton(chatId: Long? = null, menuButton: MenuButton? = null): TelegramResponse<Boolean> {
    val request = SetChatMenuButtonRequest(
        chatId = chatId,
        menuButton = menuButton
    )
    return setChatMenuButton(request)
}

/**
 * Use this method to change the default administrator rights requested by the bot when it's added as an administrator to groups or channels. These rights will be suggested to users, but they are free to modify the list before adding the bot. Returns True on success.
 *
 * @param rights A JSON-serialized object describing new default administrator rights. If not specified, the default administrator rights will be cleared.
 * @param forChannels Pass *True* to change the default administrator rights of the bot in channels. Otherwise, the default administrator rights of the bot for groups and supergroups will be changed.
 */
public suspend fun TelegramBotApi.setMyDefaultAdministratorRights(rights: ChatAdministratorRights? = null, forChannels: Boolean? = null): TelegramResponse<Boolean> {
    val request = SetMyDefaultAdministratorRightsRequest(
        rights = rights,
        forChannels = forChannels
    )
    return setMyDefaultAdministratorRights(request)
}

/**
 * Sends a gift to the given user or channel chat. The gift can't be converted to Telegram Stars by the receiver. Returns True on success.
 *
 * @param userId Required if *chat_id* is not specified. Unique identifier of the target user who will receive the gift.
 * @param chatId Required if *user_id* is not specified. Unique identifier for the chat or username of the channel (in the format `@channelusername`) that will receive the gift.
 * @param giftId Identifier of the gift; limited gifts can't be sent to channel chats
 * @param payForUpgrade Pass *True* to pay for the gift upgrade from the bot's balance, thereby making the upgrade free for the receiver
 * @param text Text that will be shown along with the gift; 0-128 characters
 * @param textParseMode Mode for parsing entities in the text. See [formatting options](https://core.telegram.org/bots/api#formatting-options) for more details. Entities other than “bold”, “italic”, “underline”, “strikethrough”, “spoiler”, and “custom_emoji” are ignored.
 * @param textEntities A JSON-serialized list of special entities that appear in the gift text. It can be specified instead of *text_parse_mode*. Entities other than “bold”, “italic”, “underline”, “strikethrough”, “spoiler”, and “custom_emoji” are ignored.
 */
public suspend fun TelegramBotApi.sendGift(
    userId: Long? = null,
    chatId: String? = null,
    giftId: String,
    payForUpgrade: Boolean? = null,
    text: String? = null,
    textParseMode: String? = null,
    textEntities: List<MessageEntity>? = null,
): TelegramResponse<Boolean> {
    val request = SendGiftRequest(
        userId = userId,
        chatId = chatId,
        giftId = giftId,
        payForUpgrade = payForUpgrade,
        text = text,
        textParseMode = textParseMode,
        textEntities = textEntities
    )
    return sendGift(request)
}

/**
 * Gifts a Telegram Premium subscription to the given user. Returns True on success.
 *
 * @param userId Unique identifier of the target user who will receive a Telegram Premium subscription
 * @param monthCount Number of months the Telegram Premium subscription will be active for the user; must be one of 3, 6, or 12
 * @param starCount Number of Telegram Stars to pay for the Telegram Premium subscription; must be 1000 for 3 months, 1500 for 6 months, and 2500 for 12 months
 * @param text Text that will be shown along with the service message about the subscription; 0-128 characters
 * @param textParseMode Mode for parsing entities in the text. See [formatting options](https://core.telegram.org/bots/api#formatting-options) for more details. Entities other than “bold”, “italic”, “underline”, “strikethrough”, “spoiler”, and “custom_emoji” are ignored.
 * @param textEntities A JSON-serialized list of special entities that appear in the gift text. It can be specified instead of *text_parse_mode*. Entities other than “bold”, “italic”, “underline”, “strikethrough”, “spoiler”, and “custom_emoji” are ignored.
 */
public suspend fun TelegramBotApi.giftPremiumSubscription(
    userId: Long,
    monthCount: Long,
    starCount: Long,
    text: String? = null,
    textParseMode: String? = null,
    textEntities: List<MessageEntity>? = null,
): TelegramResponse<Boolean> {
    val request = GiftPremiumSubscriptionRequest(
        userId = userId,
        monthCount = monthCount,
        starCount = starCount,
        text = text,
        textParseMode = textParseMode,
        textEntities = textEntities
    )
    return giftPremiumSubscription(request)
}

/**
 * Verifies a user on behalf of the organization which is represented by the bot. Returns True on success.
 *
 * @param userId Unique identifier of the target user
 * @param customDescription Custom description for the verification; 0-70 characters. Must be empty if the organization isn't allowed to provide a custom verification description.
 */
public suspend fun TelegramBotApi.verifyUser(userId: Long, customDescription: String? = null): TelegramResponse<Boolean> {
    val request = VerifyUserRequest(
        userId = userId,
        customDescription = customDescription
    )
    return verifyUser(request)
}

/**
 * Verifies a chat on behalf of the organization which is represented by the bot. Returns True on success.
 *
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format `@channelusername`). Channel direct messages chats can't be verified.
 * @param customDescription Custom description for the verification; 0-70 characters. Must be empty if the organization isn't allowed to provide a custom verification description.
 */
public suspend fun TelegramBotApi.verifyChat(chatId: String, customDescription: String? = null): TelegramResponse<Boolean> {
    val request = VerifyChatRequest(
        chatId = chatId,
        customDescription = customDescription
    )
    return verifyChat(request)
}

/**
 * Removes verification from a user who is currently verified on behalf of the organization represented by the bot. Returns True on success.
 *
 * @param userId Unique identifier of the target user
 */
public suspend fun TelegramBotApi.removeUserVerification(userId: Long): TelegramResponse<Boolean> {
    val request = RemoveUserVerificationRequest(
        userId = userId
    )
    return removeUserVerification(request)
}

/**
 * Removes verification from a chat that is currently verified on behalf of the organization represented by the bot. Returns True on success.
 *
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format `@channelusername`)
 */
public suspend fun TelegramBotApi.removeChatVerification(chatId: String): TelegramResponse<Boolean> {
    val request = RemoveChatVerificationRequest(
        chatId = chatId
    )
    return removeChatVerification(request)
}

/**
 * Marks incoming message as read on behalf of a business account. Requires the can_read_messages business bot right. Returns True on success.
 *
 * @param businessConnectionId Unique identifier of the business connection on behalf of which to read the message
 * @param chatId Unique identifier of the chat in which the message was received. The chat must have been active in the last 24 hours.
 * @param messageId Unique identifier of the message to mark as read
 */
public suspend fun TelegramBotApi.readBusinessMessage(
    businessConnectionId: String,
    chatId: Long,
    messageId: Long,
): TelegramResponse<Boolean> {
    val request = ReadBusinessMessageRequest(
        businessConnectionId = businessConnectionId,
        chatId = chatId,
        messageId = messageId
    )
    return readBusinessMessage(request)
}

/**
 * Delete messages on behalf of a business account. Requires the can_delete_sent_messages business bot right to delete messages sent by the bot itself, or the can_delete_all_messages business bot right to delete any message. Returns True on success.
 *
 * @param businessConnectionId Unique identifier of the business connection on behalf of which to delete the messages
 * @param messageIds A JSON-serialized list of 1-100 identifiers of messages to delete. All messages must be from the same chat. See [deleteMessage](https://core.telegram.org/bots/api#deletemessage) for limitations on which messages can be deleted
 */
public suspend fun TelegramBotApi.deleteBusinessMessages(businessConnectionId: String, messageIds: List<Long>): TelegramResponse<Boolean> {
    val request = DeleteBusinessMessagesRequest(
        businessConnectionId = businessConnectionId,
        messageIds = messageIds
    )
    return deleteBusinessMessages(request)
}

/**
 * Changes the first and last name of a managed business account. Requires the can_change_name business bot right. Returns True on success.
 *
 * @param businessConnectionId Unique identifier of the business connection
 * @param firstName The new value of the first name for the business account; 1-64 characters
 * @param lastName The new value of the last name for the business account; 0-64 characters
 */
public suspend fun TelegramBotApi.setBusinessAccountName(
    businessConnectionId: String,
    firstName: String,
    lastName: String? = null,
): TelegramResponse<Boolean> {
    val request = SetBusinessAccountNameRequest(
        businessConnectionId = businessConnectionId,
        firstName = firstName,
        lastName = lastName
    )
    return setBusinessAccountName(request)
}

/**
 * Changes the username of a managed business account. Requires the can_change_username business bot right. Returns True on success.
 *
 * @param businessConnectionId Unique identifier of the business connection
 * @param username The new value of the username for the business account; 0-32 characters
 */
public suspend fun TelegramBotApi.setBusinessAccountUsername(businessConnectionId: String, username: String? = null): TelegramResponse<Boolean> {
    val request = SetBusinessAccountUsernameRequest(
        businessConnectionId = businessConnectionId,
        username = username
    )
    return setBusinessAccountUsername(request)
}

/**
 * Changes the bio of a managed business account. Requires the can_change_bio business bot right. Returns True on success.
 *
 * @param businessConnectionId Unique identifier of the business connection
 * @param bio The new value of the bio for the business account; 0-140 characters
 */
public suspend fun TelegramBotApi.setBusinessAccountBio(businessConnectionId: String, bio: String? = null): TelegramResponse<Boolean> {
    val request = SetBusinessAccountBioRequest(
        businessConnectionId = businessConnectionId,
        bio = bio
    )
    return setBusinessAccountBio(request)
}

/**
 * Removes the current profile photo of a managed business account. Requires the can_edit_profile_photo business bot right. Returns True on success.
 *
 * @param businessConnectionId Unique identifier of the business connection
 * @param isPublic Pass *True* to remove the public photo, which is visible even if the main photo is hidden by the business account's privacy settings. After the main photo is removed, the previous profile photo (if present) becomes the main photo.
 */
public suspend fun TelegramBotApi.removeBusinessAccountProfilePhoto(businessConnectionId: String, isPublic: Boolean? = null): TelegramResponse<Boolean> {
    val request = RemoveBusinessAccountProfilePhotoRequest(
        businessConnectionId = businessConnectionId,
        isPublic = isPublic
    )
    return removeBusinessAccountProfilePhoto(request)
}

/**
 * Changes the privacy settings pertaining to incoming gifts in a managed business account. Requires the can_change_gift_settings business bot right. Returns True on success.
 *
 * @param businessConnectionId Unique identifier of the business connection
 * @param showGiftButton Pass *True*, if a button for sending a gift to the user or by the business account must always be shown in the input field
 * @param acceptedGiftTypes Types of gifts accepted by the business account
 */
public suspend fun TelegramBotApi.setBusinessAccountGiftSettings(
    businessConnectionId: String,
    showGiftButton: Boolean,
    acceptedGiftTypes: AcceptedGiftTypes,
): TelegramResponse<Boolean> {
    val request = SetBusinessAccountGiftSettingsRequest(
        businessConnectionId = businessConnectionId,
        showGiftButton = showGiftButton,
        acceptedGiftTypes = acceptedGiftTypes
    )
    return setBusinessAccountGiftSettings(request)
}

/**
 * Transfers Telegram Stars from the business account balance to the bot's balance. Requires the can_transfer_stars business bot right. Returns True on success.
 *
 * @param businessConnectionId Unique identifier of the business connection
 * @param starCount Number of Telegram Stars to transfer; 1-10000
 */
public suspend fun TelegramBotApi.transferBusinessAccountStars(businessConnectionId: String, starCount: Long): TelegramResponse<Boolean> {
    val request = TransferBusinessAccountStarsRequest(
        businessConnectionId = businessConnectionId,
        starCount = starCount
    )
    return transferBusinessAccountStars(request)
}

/**
 * Converts a given regular gift to Telegram Stars. Requires the can_convert_gifts_to_stars business bot right. Returns True on success.
 *
 * @param businessConnectionId Unique identifier of the business connection
 * @param ownedGiftId Unique identifier of the regular gift that should be converted to Telegram Stars
 */
public suspend fun TelegramBotApi.convertGiftToStars(businessConnectionId: String, ownedGiftId: String): TelegramResponse<Boolean> {
    val request = ConvertGiftToStarsRequest(
        businessConnectionId = businessConnectionId,
        ownedGiftId = ownedGiftId
    )
    return convertGiftToStars(request)
}

/**
 * Upgrades a given regular gift to a unique gift. Requires the can_transfer_and_upgrade_gifts business bot right. Additionally requires the can_transfer_stars business bot right if the upgrade is paid. Returns True on success.
 *
 * @param businessConnectionId Unique identifier of the business connection
 * @param ownedGiftId Unique identifier of the regular gift that should be upgraded to a unique one
 * @param keepOriginalDetails Pass *True* to keep the original gift text, sender and receiver in the upgraded gift
 * @param starCount The amount of Telegram Stars that will be paid for the upgrade from the business account balance. If `gift.prepaid_upgrade_star_count > 0`, then pass 0, otherwise, the *can_transfer_stars* business bot right is required and `gift.upgrade_star_count` must be passed.
 */
public suspend fun TelegramBotApi.upgradeGift(
    businessConnectionId: String,
    ownedGiftId: String,
    keepOriginalDetails: Boolean? = null,
    starCount: Long? = null,
): TelegramResponse<Boolean> {
    val request = UpgradeGiftRequest(
        businessConnectionId = businessConnectionId,
        ownedGiftId = ownedGiftId,
        keepOriginalDetails = keepOriginalDetails,
        starCount = starCount
    )
    return upgradeGift(request)
}

/**
 * Transfers an owned unique gift to another user. Requires the can_transfer_and_upgrade_gifts business bot right. Requires can_transfer_stars business bot right if the transfer is paid. Returns True on success.
 *
 * @param businessConnectionId Unique identifier of the business connection
 * @param ownedGiftId Unique identifier of the regular gift that should be transferred
 * @param newOwnerChatId Unique identifier of the chat which will own the gift. The chat must be active in the last 24 hours.
 * @param starCount The amount of Telegram Stars that will be paid for the transfer from the business account balance. If positive, then the *can_transfer_stars* business bot right is required.
 */
public suspend fun TelegramBotApi.transferGift(
    businessConnectionId: String,
    ownedGiftId: String,
    newOwnerChatId: Long,
    starCount: Long? = null,
): TelegramResponse<Boolean> {
    val request = TransferGiftRequest(
        businessConnectionId = businessConnectionId,
        ownedGiftId = ownedGiftId,
        newOwnerChatId = newOwnerChatId,
        starCount = starCount
    )
    return transferGift(request)
}

/**
 * Reposts a story on behalf of a business account from another business account. Both business accounts must be managed by the same bot, and the story on the source account must have been posted (or reposted) by the bot. Requires the can_manage_stories business bot right for both business accounts. Returns Story on success.
 *
 * @param businessConnectionId Unique identifier of the business connection
 * @param fromChatId Unique identifier of the chat which posted the story that should be reposted
 * @param fromStoryId Unique identifier of the story that should be reposted
 * @param activePeriod Period after which the story is moved to the archive, in seconds; must be one of `6 * 3600`, `12 * 3600`, `86400`, or `2 * 86400`
 * @param postToChatPage Pass *True* to keep the story accessible after it expires
 * @param protectContent Pass *True* if the content of the story must be protected from forwarding and screenshotting
 */
public suspend fun TelegramBotApi.repostStory(
    businessConnectionId: String,
    fromChatId: Long,
    fromStoryId: Long,
    activePeriod: Long,
    postToChatPage: Boolean? = null,
    protectContent: Boolean? = null,
): TelegramResponse<Story> {
    val request = RepostStoryRequest(
        businessConnectionId = businessConnectionId,
        fromChatId = fromChatId,
        fromStoryId = fromStoryId,
        activePeriod = activePeriod,
        postToChatPage = postToChatPage,
        protectContent = protectContent
    )
    return repostStory(request)
}

/**
 * Deletes a story previously posted by the bot on behalf of a managed business account. Requires the can_manage_stories business bot right. Returns True on success.
 *
 * @param businessConnectionId Unique identifier of the business connection
 * @param storyId Unique identifier of the story to delete
 */
public suspend fun TelegramBotApi.deleteStory(businessConnectionId: String, storyId: Long): TelegramResponse<Boolean> {
    val request = DeleteStoryRequest(
        businessConnectionId = businessConnectionId,
        storyId = storyId
    )
    return deleteStory(request)
}

/**
 * Use this method to edit text and game messages. On success, if the edited message is not an inline message, the edited Message is returned, otherwise True is returned. Note that business messages that were not sent by the bot and do not contain an inline keyboard can only be edited within 48 hours from the time they were sent.
 *
 * @param businessConnectionId Unique identifier of the business connection on behalf of which the message to be edited was sent
 * @param chatId Required if *inline_message_id* is not specified. Unique identifier for the target chat or username of the target channel (in the format `@channelusername`)
 * @param messageId Required if *inline_message_id* is not specified. Identifier of the message to edit
 * @param inlineMessageId Required if *chat_id* and *message_id* are not specified. Identifier of the inline message
 * @param text New text of the message, 1-4096 characters after entities parsing
 * @param parseMode Mode for parsing entities in the message text. See [formatting options](https://core.telegram.org/bots/api#formatting-options) for more details.
 * @param entities A JSON-serialized list of special entities that appear in message text, which can be specified instead of *parse_mode*
 * @param linkPreviewOptions Link preview generation options for the message
 * @param replyMarkup A JSON-serialized object for an [inline keyboard](https://core.telegram.org/bots/features#inline-keyboards).
 */
public suspend fun TelegramBotApi.editMessageText(
    businessConnectionId: String? = null,
    chatId: String? = null,
    messageId: Long? = null,
    inlineMessageId: String? = null,
    text: String,
    parseMode: String? = null,
    entities: List<MessageEntity>? = null,
    linkPreviewOptions: LinkPreviewOptions? = null,
    replyMarkup: InlineKeyboardMarkup? = null,
): TelegramResponse<Boolean> {
    val request = EditMessageTextRequest(
        businessConnectionId = businessConnectionId,
        chatId = chatId,
        messageId = messageId,
        inlineMessageId = inlineMessageId,
        text = text,
        parseMode = parseMode,
        entities = entities,
        linkPreviewOptions = linkPreviewOptions,
        replyMarkup = replyMarkup
    )
    return editMessageText(request)
}

/**
 * Use this method to edit captions of messages. On success, if the edited message is not an inline message, the edited Message is returned, otherwise True is returned. Note that business messages that were not sent by the bot and do not contain an inline keyboard can only be edited within 48 hours from the time they were sent.
 *
 * @param businessConnectionId Unique identifier of the business connection on behalf of which the message to be edited was sent
 * @param chatId Required if *inline_message_id* is not specified. Unique identifier for the target chat or username of the target channel (in the format `@channelusername`)
 * @param messageId Required if *inline_message_id* is not specified. Identifier of the message to edit
 * @param inlineMessageId Required if *chat_id* and *message_id* are not specified. Identifier of the inline message
 * @param caption New caption of the message, 0-1024 characters after entities parsing
 * @param parseMode Mode for parsing entities in the message caption. See [formatting options](https://core.telegram.org/bots/api#formatting-options) for more details.
 * @param captionEntities A JSON-serialized list of special entities that appear in the caption, which can be specified instead of *parse_mode*
 * @param showCaptionAboveMedia Pass *True*, if the caption must be shown above the message media. Supported only for animation, photo and video messages.
 * @param replyMarkup A JSON-serialized object for an [inline keyboard](https://core.telegram.org/bots/features#inline-keyboards).
 */
public suspend fun TelegramBotApi.editMessageCaption(
    businessConnectionId: String? = null,
    chatId: String? = null,
    messageId: Long? = null,
    inlineMessageId: String? = null,
    caption: String? = null,
    parseMode: String? = null,
    captionEntities: List<MessageEntity>? = null,
    showCaptionAboveMedia: Boolean? = null,
    replyMarkup: InlineKeyboardMarkup? = null,
): TelegramResponse<Boolean> {
    val request = EditMessageCaptionRequest(
        businessConnectionId = businessConnectionId,
        chatId = chatId,
        messageId = messageId,
        inlineMessageId = inlineMessageId,
        caption = caption,
        parseMode = parseMode,
        captionEntities = captionEntities,
        showCaptionAboveMedia = showCaptionAboveMedia,
        replyMarkup = replyMarkup
    )
    return editMessageCaption(request)
}

/**
 * Use this method to edit live location messages. A location can be edited until its live_period expires or editing is explicitly disabled by a call to stopMessageLiveLocation. On success, if the edited message is not an inline message, the edited Message is returned, otherwise True is returned.
 *
 * @param businessConnectionId Unique identifier of the business connection on behalf of which the message to be edited was sent
 * @param chatId Required if *inline_message_id* is not specified. Unique identifier for the target chat or username of the target channel (in the format `@channelusername`)
 * @param messageId Required if *inline_message_id* is not specified. Identifier of the message to edit
 * @param inlineMessageId Required if *chat_id* and *message_id* are not specified. Identifier of the inline message
 * @param latitude Latitude of new location
 * @param longitude Longitude of new location
 * @param livePeriod New period in seconds during which the location can be updated, starting from the message send date. If 0x7FFFFFFF is specified, then the location can be updated forever. Otherwise, the new value must not exceed the current *live_period* by more than a day, and the live location expiration date must remain within the next 90 days. If not specified, then *live_period* remains unchanged
 * @param horizontalAccuracy The radius of uncertainty for the location, measured in meters; 0-1500
 * @param heading Direction in which the user is moving, in degrees. Must be between 1 and 360 if specified.
 * @param proximityAlertRadius The maximum distance for proximity alerts about approaching another chat member, in meters. Must be between 1 and 100000 if specified.
 * @param replyMarkup A JSON-serialized object for a new [inline keyboard](https://core.telegram.org/bots/features#inline-keyboards).
 */
public suspend fun TelegramBotApi.editMessageLiveLocation(
    businessConnectionId: String? = null,
    chatId: String? = null,
    messageId: Long? = null,
    inlineMessageId: String? = null,
    latitude: Double,
    longitude: Double,
    livePeriod: Long? = null,
    horizontalAccuracy: Double? = null,
    heading: Long? = null,
    proximityAlertRadius: Long? = null,
    replyMarkup: InlineKeyboardMarkup? = null,
): TelegramResponse<Boolean> {
    val request = EditMessageLiveLocationRequest(
        businessConnectionId = businessConnectionId,
        chatId = chatId,
        messageId = messageId,
        inlineMessageId = inlineMessageId,
        latitude = latitude,
        longitude = longitude,
        livePeriod = livePeriod,
        horizontalAccuracy = horizontalAccuracy,
        heading = heading,
        proximityAlertRadius = proximityAlertRadius,
        replyMarkup = replyMarkup
    )
    return editMessageLiveLocation(request)
}

/**
 * Use this method to stop updating a live location message before live_period expires. On success, if the message is not an inline message, the edited Message is returned, otherwise True is returned.
 *
 * @param businessConnectionId Unique identifier of the business connection on behalf of which the message to be edited was sent
 * @param chatId Required if *inline_message_id* is not specified. Unique identifier for the target chat or username of the target channel (in the format `@channelusername`)
 * @param messageId Required if *inline_message_id* is not specified. Identifier of the message with live location to stop
 * @param inlineMessageId Required if *chat_id* and *message_id* are not specified. Identifier of the inline message
 * @param replyMarkup A JSON-serialized object for a new [inline keyboard](https://core.telegram.org/bots/features#inline-keyboards).
 */
public suspend fun TelegramBotApi.stopMessageLiveLocation(
    businessConnectionId: String? = null,
    chatId: String? = null,
    messageId: Long? = null,
    inlineMessageId: String? = null,
    replyMarkup: InlineKeyboardMarkup? = null,
): TelegramResponse<Boolean> {
    val request = StopMessageLiveLocationRequest(
        businessConnectionId = businessConnectionId,
        chatId = chatId,
        messageId = messageId,
        inlineMessageId = inlineMessageId,
        replyMarkup = replyMarkup
    )
    return stopMessageLiveLocation(request)
}

/**
 * Use this method to edit a checklist on behalf of a connected business account. On success, the edited Message is returned.
 *
 * @param businessConnectionId Unique identifier of the business connection on behalf of which the message will be sent
 * @param chatId Unique identifier for the target chat
 * @param messageId Unique identifier for the target message
 * @param checklist A JSON-serialized object for the new checklist
 * @param replyMarkup A JSON-serialized object for the new inline keyboard for the message
 */
public suspend fun TelegramBotApi.editMessageChecklist(
    businessConnectionId: String,
    chatId: Long,
    messageId: Long,
    checklist: InputChecklist,
    replyMarkup: InlineKeyboardMarkup? = null,
): TelegramResponse<Message> {
    val request = EditMessageChecklistRequest(
        businessConnectionId = businessConnectionId,
        chatId = chatId,
        messageId = messageId,
        checklist = checklist,
        replyMarkup = replyMarkup
    )
    return editMessageChecklist(request)
}

/**
 * Use this method to edit only the reply markup of messages. On success, if the edited message is not an inline message, the edited Message is returned, otherwise True is returned. Note that business messages that were not sent by the bot and do not contain an inline keyboard can only be edited within 48 hours from the time they were sent.
 *
 * @param businessConnectionId Unique identifier of the business connection on behalf of which the message to be edited was sent
 * @param chatId Required if *inline_message_id* is not specified. Unique identifier for the target chat or username of the target channel (in the format `@channelusername`)
 * @param messageId Required if *inline_message_id* is not specified. Identifier of the message to edit
 * @param inlineMessageId Required if *chat_id* and *message_id* are not specified. Identifier of the inline message
 * @param replyMarkup A JSON-serialized object for an [inline keyboard](https://core.telegram.org/bots/features#inline-keyboards).
 */
public suspend fun TelegramBotApi.editMessageReplyMarkup(
    businessConnectionId: String? = null,
    chatId: String? = null,
    messageId: Long? = null,
    inlineMessageId: String? = null,
    replyMarkup: InlineKeyboardMarkup? = null,
): TelegramResponse<Boolean> {
    val request = EditMessageReplyMarkupRequest(
        businessConnectionId = businessConnectionId,
        chatId = chatId,
        messageId = messageId,
        inlineMessageId = inlineMessageId,
        replyMarkup = replyMarkup
    )
    return editMessageReplyMarkup(request)
}

/**
 * Use this method to stop a poll which was sent by the bot. On success, the stopped Poll is returned.
 *
 * @param businessConnectionId Unique identifier of the business connection on behalf of which the message to be edited was sent
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format `@channelusername`)
 * @param messageId Identifier of the original message with the poll
 * @param replyMarkup A JSON-serialized object for a new message [inline keyboard](https://core.telegram.org/bots/features#inline-keyboards).
 */
public suspend fun TelegramBotApi.stopPoll(
    businessConnectionId: String? = null,
    chatId: String,
    messageId: Long,
    replyMarkup: InlineKeyboardMarkup? = null,
): TelegramResponse<Poll> {
    val request = StopPollRequest(
        businessConnectionId = businessConnectionId,
        chatId = chatId,
        messageId = messageId,
        replyMarkup = replyMarkup
    )
    return stopPoll(request)
}

/**
 * Use this method to approve a suggested post in a direct messages chat. The bot must have the 'can_post_messages' administrator right in the corresponding channel chat. Returns True on success.
 *
 * @param chatId Unique identifier for the target direct messages chat
 * @param messageId Identifier of a suggested post message to approve
 * @param sendDate Point in time (Unix timestamp) when the post is expected to be published; omit if the date has already been specified when the suggested post was created. If specified, then the date must be not more than 2678400 seconds (30 days) in the future
 */
public suspend fun TelegramBotApi.approveSuggestedPost(
    chatId: Long,
    messageId: Long,
    sendDate: Long? = null,
): TelegramResponse<Boolean> {
    val request = ApproveSuggestedPostRequest(
        chatId = chatId,
        messageId = messageId,
        sendDate = sendDate
    )
    return approveSuggestedPost(request)
}

/**
 * Use this method to decline a suggested post in a direct messages chat. The bot must have the 'can_manage_direct_messages' administrator right in the corresponding channel chat. Returns True on success.
 *
 * @param chatId Unique identifier for the target direct messages chat
 * @param messageId Identifier of a suggested post message to decline
 * @param comment Comment for the creator of the suggested post; 0-128 characters
 */
public suspend fun TelegramBotApi.declineSuggestedPost(
    chatId: Long,
    messageId: Long,
    comment: String? = null,
): TelegramResponse<Boolean> {
    val request = DeclineSuggestedPostRequest(
        chatId = chatId,
        messageId = messageId,
        comment = comment
    )
    return declineSuggestedPost(request)
}

/**
 * Use this method to delete a message, including service messages, with the following limitations: - A message can only be deleted if it was sent less than 48 hours ago. - Service messages about a supergroup, channel, or forum topic creation can't be deleted. - A dice message in a private chat can only be deleted if it was sent more than 24 hours ago. - Bots can delete outgoing messages in private chats, groups, and supergroups. - Bots can delete incoming messages in private chats. - Bots granted can_post_messages permissions can delete outgoing messages in channels. - If the bot is an administrator of a group, it can delete any message there. - If the bot has can_delete_messages administrator right in a supergroup or a channel, it can delete any message there. - If the bot has can_manage_direct_messages administrator right in a channel, it can delete any message in the corresponding direct messages chat. Returns True on success.
 *
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format `@channelusername`)
 * @param messageId Identifier of the message to delete
 */
public suspend fun TelegramBotApi.deleteMessage(chatId: String, messageId: Long): TelegramResponse<Boolean> {
    val request = DeleteMessageRequest(
        chatId = chatId,
        messageId = messageId
    )
    return deleteMessage(request)
}

/**
 * Use this method to delete multiple messages simultaneously. If some of the specified messages can't be found, they are skipped. Returns True on success.
 *
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format `@channelusername`)
 * @param messageIds A JSON-serialized list of 1-100 identifiers of messages to delete. See [deleteMessage](https://core.telegram.org/bots/api#deletemessage) for limitations on which messages can be deleted
 */
public suspend fun TelegramBotApi.deleteMessages(chatId: String, messageIds: List<Long>): TelegramResponse<Boolean> {
    val request = DeleteMessagesRequest(
        chatId = chatId,
        messageIds = messageIds
    )
    return deleteMessages(request)
}

/**
 * Use this method to move a sticker in a set created by the bot to a specific position. Returns True on success.
 *
 * @param sticker File identifier of the sticker
 * @param position New sticker position in the set, zero-based
 */
public suspend fun TelegramBotApi.setStickerPositionInSet(sticker: String, position: Long): TelegramResponse<Boolean> {
    val request = SetStickerPositionInSetRequest(
        sticker = sticker,
        position = position
    )
    return setStickerPositionInSet(request)
}

/**
 * Use this method to delete a sticker from a set created by the bot. Returns True on success.
 *
 * @param sticker File identifier of the sticker
 */
public suspend fun TelegramBotApi.deleteStickerFromSet(sticker: String): TelegramResponse<Boolean> {
    val request = DeleteStickerFromSetRequest(
        sticker = sticker
    )
    return deleteStickerFromSet(request)
}

/**
 * Use this method to change the list of emoji assigned to a regular or custom emoji sticker. The sticker must belong to a sticker set created by the bot. Returns True on success.
 *
 * @param sticker File identifier of the sticker
 * @param emojiList A JSON-serialized list of 1-20 emoji associated with the sticker
 */
public suspend fun TelegramBotApi.setStickerEmojiList(sticker: String, emojiList: List<String>): TelegramResponse<Boolean> {
    val request = SetStickerEmojiListRequest(
        sticker = sticker,
        emojiList = emojiList
    )
    return setStickerEmojiList(request)
}

/**
 * Use this method to change search keywords assigned to a regular or custom emoji sticker. The sticker must belong to a sticker set created by the bot. Returns True on success.
 *
 * @param sticker File identifier of the sticker
 * @param keywords A JSON-serialized list of 0-20 search keywords for the sticker with total length of up to 64 characters
 */
public suspend fun TelegramBotApi.setStickerKeywords(sticker: String, keywords: List<String>? = null): TelegramResponse<Boolean> {
    val request = SetStickerKeywordsRequest(
        sticker = sticker,
        keywords = keywords
    )
    return setStickerKeywords(request)
}

/**
 * Use this method to change the mask position of a mask sticker. The sticker must belong to a sticker set that was created by the bot. Returns True on success.
 *
 * @param sticker File identifier of the sticker
 * @param maskPosition A JSON-serialized object with the position where the mask should be placed on faces. Omit the parameter to remove the mask position.
 */
public suspend fun TelegramBotApi.setStickerMaskPosition(sticker: String, maskPosition: MaskPosition? = null): TelegramResponse<Boolean> {
    val request = SetStickerMaskPositionRequest(
        sticker = sticker,
        maskPosition = maskPosition
    )
    return setStickerMaskPosition(request)
}

/**
 * Use this method to set the title of a created sticker set. Returns True on success.
 *
 * @param name Sticker set name
 * @param title Sticker set title, 1-64 characters
 */
public suspend fun TelegramBotApi.setStickerSetTitle(name: String, title: String): TelegramResponse<Boolean> {
    val request = SetStickerSetTitleRequest(
        name = name,
        title = title
    )
    return setStickerSetTitle(request)
}

/**
 * Use this method to set the thumbnail of a custom emoji sticker set. Returns True on success.
 *
 * @param name Sticker set name
 * @param customEmojiId Custom emoji identifier of a sticker from the sticker set; pass an empty string to drop the thumbnail and use the first sticker as the thumbnail.
 */
public suspend fun TelegramBotApi.setCustomEmojiStickerSetThumbnail(name: String, customEmojiId: String? = null): TelegramResponse<Boolean> {
    val request = SetCustomEmojiStickerSetThumbnailRequest(
        name = name,
        customEmojiId = customEmojiId
    )
    return setCustomEmojiStickerSetThumbnail(request)
}

/**
 * Use this method to delete a sticker set that was created by the bot. Returns True on success.
 *
 * @param name Sticker set name
 */
public suspend fun TelegramBotApi.deleteStickerSet(name: String): TelegramResponse<Boolean> {
    val request = DeleteStickerSetRequest(
        name = name
    )
    return deleteStickerSet(request)
}

/**
 * Use this method to send answers to an inline query. On success, True is returned. No more than 50 results per query are allowed.
 *
 * @param inlineQueryId Unique identifier for the answered query
 * @param results A JSON-serialized array of results for the inline query
 * @param cacheTime The maximum amount of time in seconds that the result of the inline query may be cached on the server. Defaults to 300.
 * @param isPersonal Pass *True* if results may be cached on the server side only for the user that sent the query. By default, results may be returned to any user who sends the same query.
 * @param nextOffset Pass the offset that a client should send in the next query with the same text to receive more results. Pass an empty string if there are no more results or if you don't support pagination. Offset length can't exceed 64 bytes.
 * @param button A JSON-serialized object describing a button to be shown above inline query results
 */
public suspend fun TelegramBotApi.answerInlineQuery(
    inlineQueryId: String,
    results: List<InlineQueryResult>,
    cacheTime: Long? = null,
    isPersonal: Boolean? = null,
    nextOffset: String? = null,
    button: InlineQueryResultsButton? = null,
): TelegramResponse<Boolean> {
    val request = AnswerInlineQueryRequest(
        inlineQueryId = inlineQueryId,
        results = results,
        cacheTime = cacheTime,
        isPersonal = isPersonal,
        nextOffset = nextOffset,
        button = button
    )
    return answerInlineQuery(request)
}

/**
 * Use this method to set the result of an interaction with a Web App and send a corresponding message on behalf of the user to the chat from which the query originated. On success, a SentWebAppMessage object is returned.
 *
 * @param webAppQueryId Unique identifier for the query to be answered
 * @param result A JSON-serialized object describing the message to be sent
 */
public suspend fun TelegramBotApi.answerWebAppQuery(webAppQueryId: String, result: InlineQueryResult): TelegramResponse<SentWebAppMessage> {
    val request = AnswerWebAppQueryRequest(
        webAppQueryId = webAppQueryId,
        result = result
    )
    return answerWebAppQuery(request)
}

/**
 * Stores a message that can be sent by a user of a Mini App. Returns a PreparedInlineMessage object.
 *
 * @param userId Unique identifier of the target user that can use the prepared message
 * @param result A JSON-serialized object describing the message to be sent
 * @param allowUserChats Pass *True* if the message can be sent to private chats with users
 * @param allowBotChats Pass *True* if the message can be sent to private chats with bots
 * @param allowGroupChats Pass *True* if the message can be sent to group and supergroup chats
 * @param allowChannelChats Pass *True* if the message can be sent to channel chats
 */
public suspend fun TelegramBotApi.savePreparedInlineMessage(
    userId: Long,
    result: InlineQueryResult,
    allowUserChats: Boolean? = null,
    allowBotChats: Boolean? = null,
    allowGroupChats: Boolean? = null,
    allowChannelChats: Boolean? = null,
): TelegramResponse<PreparedInlineMessage> {
    val request = SavePreparedInlineMessageRequest(
        userId = userId,
        result = result,
        allowUserChats = allowUserChats,
        allowBotChats = allowBotChats,
        allowGroupChats = allowGroupChats,
        allowChannelChats = allowChannelChats
    )
    return savePreparedInlineMessage(request)
}

/**
 * Use this method to send invoices. On success, the sent Message is returned.
 *
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format `@channelusername`)
 * @param messageThreadId Unique identifier for the target message thread (topic) of a forum; for forum supergroups and private chats of bots with forum topic mode enabled only
 * @param directMessagesTopicId Identifier of the direct messages topic to which the message will be sent; required if the message is sent to a direct messages chat
 * @param title Product name, 1-32 characters
 * @param description Product description, 1-255 characters
 * @param payload Bot-defined invoice payload, 1-128 bytes. This will not be displayed to the user, use it for your internal processes.
 * @param providerToken Payment provider token, obtained via [@BotFather](https://t.me/botfather). Pass an empty string for payments in [Telegram Stars](https://t.me/BotNews/90).
 * @param currency Three-letter ISO 4217 currency code, see [more on currencies](https://core.telegram.org/bots/payments#supported-currencies). Pass “XTR” for payments in [Telegram Stars](https://t.me/BotNews/90).
 * @param prices Price breakdown, a JSON-serialized list of components (e.g. product price, tax, discount, delivery cost, delivery tax, bonus, etc.). Must contain exactly one item for payments in [Telegram Stars](https://t.me/BotNews/90).
 * @param maxTipAmount The maximum accepted amount for tips in the *smallest units* of the currency (integer, **not** float/double). For example, for a maximum tip of `US$ 1.45` pass `max_tip_amount = 145`. See the *exp* parameter in [currencies.json](https://core.telegram.org/bots/payments/currencies.json), it shows the number of digits past the decimal point for each currency (2 for the majority of currencies). Defaults to 0. Not supported for payments in [Telegram Stars](https://t.me/BotNews/90).
 * @param suggestedTipAmounts A JSON-serialized array of suggested amounts of tips in the *smallest units* of the currency (integer, **not** float/double). At most 4 suggested tip amounts can be specified. The suggested tip amounts must be positive, passed in a strictly increased order and must not exceed *max_tip_amount*.
 * @param startParameter Unique deep-linking parameter. If left empty, **forwarded copies** of the sent message will have a *Pay* button, allowing multiple users to pay directly from the forwarded message, using the same invoice. If non-empty, forwarded copies of the sent message will have a *URL* button with a deep link to the bot (instead of a *Pay* button), with the value used as the start parameter
 * @param providerData JSON-serialized data about the invoice, which will be shared with the payment provider. A detailed description of required fields should be provided by the payment provider.
 * @param photoUrl URL of the product photo for the invoice. Can be a photo of the goods or a marketing image for a service. People like it better when they see what they are paying for.
 * @param photoSize Photo size in bytes
 * @param photoWidth Photo width
 * @param photoHeight Photo height
 * @param needName Pass *True* if you require the user's full name to complete the order. Ignored for payments in [Telegram Stars](https://t.me/BotNews/90).
 * @param needPhoneNumber Pass *True* if you require the user's phone number to complete the order. Ignored for payments in [Telegram Stars](https://t.me/BotNews/90).
 * @param needEmail Pass *True* if you require the user's email address to complete the order. Ignored for payments in [Telegram Stars](https://t.me/BotNews/90).
 * @param needShippingAddress Pass *True* if you require the user's shipping address to complete the order. Ignored for payments in [Telegram Stars](https://t.me/BotNews/90).
 * @param sendPhoneNumberToProvider Pass *True* if the user's phone number should be sent to the provider. Ignored for payments in [Telegram Stars](https://t.me/BotNews/90).
 * @param sendEmailToProvider Pass *True* if the user's email address should be sent to the provider. Ignored for payments in [Telegram Stars](https://t.me/BotNews/90).
 * @param isFlexible Pass *True* if the final price depends on the shipping method. Ignored for payments in [Telegram Stars](https://t.me/BotNews/90).
 * @param disableNotification Sends the message [silently](https://telegram.org/blog/channels-2-0#silent-messages). Users will receive a notification with no sound.
 * @param protectContent Protects the contents of the sent message from forwarding and saving
 * @param allowPaidBroadcast Pass *True* to allow up to 1000 messages per second, ignoring [broadcasting limits](https://core.telegram.org/bots/faq#how-can-i-message-all-of-my-bot-39s-subscribers-at-once) for a fee of 0.1 Telegram Stars per message. The relevant Stars will be withdrawn from the bot's balance
 * @param messageEffectId Unique identifier of the message effect to be added to the message; for private chats only
 * @param suggestedPostParameters A JSON-serialized object containing the parameters of the suggested post to send; for direct messages chats only. If the message is sent as a reply to another suggested post, then that suggested post is automatically declined.
 * @param replyParameters Description of the message to reply to
 * @param replyMarkup A JSON-serialized object for an [inline keyboard](https://core.telegram.org/bots/features#inline-keyboards). If empty, one 'Pay `total price`' button will be shown. If not empty, the first button must be a Pay button.
 */
public suspend fun TelegramBotApi.sendInvoice(
    chatId: String,
    messageThreadId: Long? = null,
    directMessagesTopicId: Long? = null,
    title: String,
    description: String,
    payload: String,
    providerToken: String? = null,
    currency: String,
    prices: List<LabeledPrice>,
    maxTipAmount: Long? = null,
    suggestedTipAmounts: List<Long>? = null,
    startParameter: String? = null,
    providerData: String? = null,
    photoUrl: String? = null,
    photoSize: Long? = null,
    photoWidth: Long? = null,
    photoHeight: Long? = null,
    needName: Boolean? = null,
    needPhoneNumber: Boolean? = null,
    needEmail: Boolean? = null,
    needShippingAddress: Boolean? = null,
    sendPhoneNumberToProvider: Boolean? = null,
    sendEmailToProvider: Boolean? = null,
    isFlexible: Boolean? = null,
    disableNotification: Boolean? = null,
    protectContent: Boolean? = null,
    allowPaidBroadcast: Boolean? = null,
    messageEffectId: String? = null,
    suggestedPostParameters: SuggestedPostParameters? = null,
    replyParameters: ReplyParameters? = null,
    replyMarkup: InlineKeyboardMarkup? = null,
): TelegramResponse<Message> {
    val request = SendInvoiceRequest(
        chatId = chatId,
        messageThreadId = messageThreadId,
        directMessagesTopicId = directMessagesTopicId,
        title = title,
        description = description,
        payload = payload,
        providerToken = providerToken,
        currency = currency,
        prices = prices,
        maxTipAmount = maxTipAmount,
        suggestedTipAmounts = suggestedTipAmounts,
        startParameter = startParameter,
        providerData = providerData,
        photoUrl = photoUrl,
        photoSize = photoSize,
        photoWidth = photoWidth,
        photoHeight = photoHeight,
        needName = needName,
        needPhoneNumber = needPhoneNumber,
        needEmail = needEmail,
        needShippingAddress = needShippingAddress,
        sendPhoneNumberToProvider = sendPhoneNumberToProvider,
        sendEmailToProvider = sendEmailToProvider,
        isFlexible = isFlexible,
        disableNotification = disableNotification,
        protectContent = protectContent,
        allowPaidBroadcast = allowPaidBroadcast,
        messageEffectId = messageEffectId,
        suggestedPostParameters = suggestedPostParameters,
        replyParameters = replyParameters,
        replyMarkup = replyMarkup
    )
    return sendInvoice(request)
}

/**
 * Use this method to create a link for an invoice. Returns the created invoice link as String on success.
 *
 * @param businessConnectionId Unique identifier of the business connection on behalf of which the link will be created. For payments in [Telegram Stars](https://t.me/BotNews/90) only.
 * @param title Product name, 1-32 characters
 * @param description Product description, 1-255 characters
 * @param payload Bot-defined invoice payload, 1-128 bytes. This will not be displayed to the user, use it for your internal processes.
 * @param providerToken Payment provider token, obtained via [@BotFather](https://t.me/botfather). Pass an empty string for payments in [Telegram Stars](https://t.me/BotNews/90).
 * @param currency Three-letter ISO 4217 currency code, see [more on currencies](https://core.telegram.org/bots/payments#supported-currencies). Pass “XTR” for payments in [Telegram Stars](https://t.me/BotNews/90).
 * @param prices Price breakdown, a JSON-serialized list of components (e.g. product price, tax, discount, delivery cost, delivery tax, bonus, etc.). Must contain exactly one item for payments in [Telegram Stars](https://t.me/BotNews/90).
 * @param subscriptionPeriod The number of seconds the subscription will be active for before the next payment. The currency must be set to “XTR” (Telegram Stars) if the parameter is used. Currently, it must always be 2592000 (30 days) if specified. Any number of subscriptions can be active for a given bot at the same time, including multiple concurrent subscriptions from the same user. Subscription price must no exceed 10000 Telegram Stars.
 * @param maxTipAmount The maximum accepted amount for tips in the *smallest units* of the currency (integer, **not** float/double). For example, for a maximum tip of `US$ 1.45` pass `max_tip_amount = 145`. See the *exp* parameter in [currencies.json](https://core.telegram.org/bots/payments/currencies.json), it shows the number of digits past the decimal point for each currency (2 for the majority of currencies). Defaults to 0. Not supported for payments in [Telegram Stars](https://t.me/BotNews/90).
 * @param suggestedTipAmounts A JSON-serialized array of suggested amounts of tips in the *smallest units* of the currency (integer, **not** float/double). At most 4 suggested tip amounts can be specified. The suggested tip amounts must be positive, passed in a strictly increased order and must not exceed *max_tip_amount*.
 * @param providerData JSON-serialized data about the invoice, which will be shared with the payment provider. A detailed description of required fields should be provided by the payment provider.
 * @param photoUrl URL of the product photo for the invoice. Can be a photo of the goods or a marketing image for a service.
 * @param photoSize Photo size in bytes
 * @param photoWidth Photo width
 * @param photoHeight Photo height
 * @param needName Pass *True* if you require the user's full name to complete the order. Ignored for payments in [Telegram Stars](https://t.me/BotNews/90).
 * @param needPhoneNumber Pass *True* if you require the user's phone number to complete the order. Ignored for payments in [Telegram Stars](https://t.me/BotNews/90).
 * @param needEmail Pass *True* if you require the user's email address to complete the order. Ignored for payments in [Telegram Stars](https://t.me/BotNews/90).
 * @param needShippingAddress Pass *True* if you require the user's shipping address to complete the order. Ignored for payments in [Telegram Stars](https://t.me/BotNews/90).
 * @param sendPhoneNumberToProvider Pass *True* if the user's phone number should be sent to the provider. Ignored for payments in [Telegram Stars](https://t.me/BotNews/90).
 * @param sendEmailToProvider Pass *True* if the user's email address should be sent to the provider. Ignored for payments in [Telegram Stars](https://t.me/BotNews/90).
 * @param isFlexible Pass *True* if the final price depends on the shipping method. Ignored for payments in [Telegram Stars](https://t.me/BotNews/90).
 */
public suspend fun TelegramBotApi.createInvoiceLink(
    businessConnectionId: String? = null,
    title: String,
    description: String,
    payload: String,
    providerToken: String? = null,
    currency: String,
    prices: List<LabeledPrice>,
    subscriptionPeriod: Long? = null,
    maxTipAmount: Long? = null,
    suggestedTipAmounts: List<Long>? = null,
    providerData: String? = null,
    photoUrl: String? = null,
    photoSize: Long? = null,
    photoWidth: Long? = null,
    photoHeight: Long? = null,
    needName: Boolean? = null,
    needPhoneNumber: Boolean? = null,
    needEmail: Boolean? = null,
    needShippingAddress: Boolean? = null,
    sendPhoneNumberToProvider: Boolean? = null,
    sendEmailToProvider: Boolean? = null,
    isFlexible: Boolean? = null,
): TelegramResponse<String> {
    val request = CreateInvoiceLinkRequest(
        businessConnectionId = businessConnectionId,
        title = title,
        description = description,
        payload = payload,
        providerToken = providerToken,
        currency = currency,
        prices = prices,
        subscriptionPeriod = subscriptionPeriod,
        maxTipAmount = maxTipAmount,
        suggestedTipAmounts = suggestedTipAmounts,
        providerData = providerData,
        photoUrl = photoUrl,
        photoSize = photoSize,
        photoWidth = photoWidth,
        photoHeight = photoHeight,
        needName = needName,
        needPhoneNumber = needPhoneNumber,
        needEmail = needEmail,
        needShippingAddress = needShippingAddress,
        sendPhoneNumberToProvider = sendPhoneNumberToProvider,
        sendEmailToProvider = sendEmailToProvider,
        isFlexible = isFlexible
    )
    return createInvoiceLink(request)
}

/**
 * If you sent an invoice requesting a shipping address and the parameter is_flexible was specified, the Bot API will send an Update with a shipping_query field to the bot. Use this method to reply to shipping queries. On success, True is returned.
 *
 * @param shippingQueryId Unique identifier for the query to be answered
 * @param ok Pass *True* if delivery to the specified address is possible and *False* if there are any problems (for example, if delivery to the specified address is not possible)
 * @param shippingOptions Required if *ok* is *True*. A JSON-serialized array of available shipping options.
 * @param errorMessage Required if *ok* is *False*. Error message in human readable form that explains why it is impossible to complete the order (e.g. “Sorry, delivery to your desired address is unavailable”). Telegram will display this message to the user.
 */
public suspend fun TelegramBotApi.answerShippingQuery(
    shippingQueryId: String,
    ok: Boolean,
    shippingOptions: List<ShippingOption>? = null,
    errorMessage: String? = null,
): TelegramResponse<Boolean> {
    val request = AnswerShippingQueryRequest(
        shippingQueryId = shippingQueryId,
        ok = ok,
        shippingOptions = shippingOptions,
        errorMessage = errorMessage
    )
    return answerShippingQuery(request)
}

/**
 * Once the user has confirmed their payment and shipping details, the Bot API sends the final confirmation in the form of an Update with the field pre_checkout_query. Use this method to respond to such pre-checkout queries. On success, True is returned. Note: The Bot API must receive an answer within 10 seconds after the pre-checkout query was sent.
 *
 * @param preCheckoutQueryId Unique identifier for the query to be answered
 * @param ok Specify *True* if everything is alright (goods are available, etc.) and the bot is ready to proceed with the order. Use *False* if there are any problems.
 * @param errorMessage Required if *ok* is *False*. Error message in human readable form that explains the reason for failure to proceed with the checkout (e.g. "Sorry, somebody just bought the last of our amazing black T-shirts while you were busy filling out your payment details. Please choose a different color or garment!"). Telegram will display this message to the user.
 */
public suspend fun TelegramBotApi.answerPreCheckoutQuery(
    preCheckoutQueryId: String,
    ok: Boolean,
    errorMessage: String? = null,
): TelegramResponse<Boolean> {
    val request = AnswerPreCheckoutQueryRequest(
        preCheckoutQueryId = preCheckoutQueryId,
        ok = ok,
        errorMessage = errorMessage
    )
    return answerPreCheckoutQuery(request)
}

/**
 * Refunds a successful payment in Telegram Stars. Returns True on success.
 *
 * @param userId Identifier of the user whose payment will be refunded
 * @param telegramPaymentChargeId Telegram payment identifier
 */
public suspend fun TelegramBotApi.refundStarPayment(userId: Long, telegramPaymentChargeId: String): TelegramResponse<Boolean> {
    val request = RefundStarPaymentRequest(
        userId = userId,
        telegramPaymentChargeId = telegramPaymentChargeId
    )
    return refundStarPayment(request)
}

/**
 * Allows the bot to cancel or re-enable extension of a subscription paid in Telegram Stars. Returns True on success.
 *
 * @param userId Identifier of the user whose subscription will be edited
 * @param telegramPaymentChargeId Telegram payment identifier for the subscription
 * @param isCanceled Pass *True* to cancel extension of the user subscription; the subscription must be active up to the end of the current subscription period. Pass *False* to allow the user to re-enable a subscription that was previously canceled by the bot.
 */
public suspend fun TelegramBotApi.editUserStarSubscription(
    userId: Long,
    telegramPaymentChargeId: String,
    isCanceled: Boolean,
): TelegramResponse<Boolean> {
    val request = EditUserStarSubscriptionRequest(
        userId = userId,
        telegramPaymentChargeId = telegramPaymentChargeId,
        isCanceled = isCanceled
    )
    return editUserStarSubscription(request)
}

/**
 * Informs a user that some of the Telegram Passport elements they provided contains errors. The user will not be able to re-submit their Passport to you until the errors are fixed (the contents of the field for which you returned the error must change). Returns True on success.
 * Use this if the data submitted by the user doesn't satisfy the standards your service requires for any reason. For example, if a birthday date seems invalid, a submitted document is blurry, a scan shows evidence of tampering, etc. Supply some details in the error message to make sure the user knows how to correct the issues.
 *
 * @param userId User identifier
 * @param errors A JSON-serialized array describing the errors
 */
public suspend fun TelegramBotApi.setPassportDataErrors(userId: Long, errors: List<PassportElementError>): TelegramResponse<Boolean> {
    val request = SetPassportDataErrorsRequest(
        userId = userId,
        errors = errors
    )
    return setPassportDataErrors(request)
}

/**
 * Use this method to send a game. On success, the sent Message is returned.
 *
 * @param businessConnectionId Unique identifier of the business connection on behalf of which the message will be sent
 * @param chatId Unique identifier for the target chat. Games can't be sent to channel direct messages chats and channel chats.
 * @param messageThreadId Unique identifier for the target message thread (topic) of a forum; for forum supergroups and private chats of bots with forum topic mode enabled only
 * @param gameShortName Short name of the game, serves as the unique identifier for the game. Set up your games via [@BotFather](https://t.me/botfather).
 * @param disableNotification Sends the message [silently](https://telegram.org/blog/channels-2-0#silent-messages). Users will receive a notification with no sound.
 * @param protectContent Protects the contents of the sent message from forwarding and saving
 * @param allowPaidBroadcast Pass *True* to allow up to 1000 messages per second, ignoring [broadcasting limits](https://core.telegram.org/bots/faq#how-can-i-message-all-of-my-bot-39s-subscribers-at-once) for a fee of 0.1 Telegram Stars per message. The relevant Stars will be withdrawn from the bot's balance
 * @param messageEffectId Unique identifier of the message effect to be added to the message; for private chats only
 * @param replyParameters Description of the message to reply to
 * @param replyMarkup A JSON-serialized object for an [inline keyboard](https://core.telegram.org/bots/features#inline-keyboards). If empty, one 'Play game_title' button will be shown. If not empty, the first button must launch the game.
 */
public suspend fun TelegramBotApi.sendGame(
    businessConnectionId: String? = null,
    chatId: Long,
    messageThreadId: Long? = null,
    gameShortName: String,
    disableNotification: Boolean? = null,
    protectContent: Boolean? = null,
    allowPaidBroadcast: Boolean? = null,
    messageEffectId: String? = null,
    replyParameters: ReplyParameters? = null,
    replyMarkup: InlineKeyboardMarkup? = null,
): TelegramResponse<Message> {
    val request = SendGameRequest(
        businessConnectionId = businessConnectionId,
        chatId = chatId,
        messageThreadId = messageThreadId,
        gameShortName = gameShortName,
        disableNotification = disableNotification,
        protectContent = protectContent,
        allowPaidBroadcast = allowPaidBroadcast,
        messageEffectId = messageEffectId,
        replyParameters = replyParameters,
        replyMarkup = replyMarkup
    )
    return sendGame(request)
}

/**
 * Use this method to set the score of the specified user in a game message. On success, if the message is not an inline message, the Message is returned, otherwise True is returned. Returns an error, if the new score is not greater than the user's current score in the chat and force is False.
 *
 * @param userId User identifier
 * @param score New score, must be non-negative
 * @param force Pass *True* if the high score is allowed to decrease. This can be useful when fixing mistakes or banning cheaters
 * @param disableEditMessage Pass *True* if the game message should not be automatically edited to include the current scoreboard
 * @param chatId Required if *inline_message_id* is not specified. Unique identifier for the target chat
 * @param messageId Required if *inline_message_id* is not specified. Identifier of the sent message
 * @param inlineMessageId Required if *chat_id* and *message_id* are not specified. Identifier of the inline message
 */
public suspend fun TelegramBotApi.setGameScore(
    userId: Long,
    score: Long,
    force: Boolean? = null,
    disableEditMessage: Boolean? = null,
    chatId: Long? = null,
    messageId: Long? = null,
    inlineMessageId: String? = null,
): TelegramResponse<Boolean> {
    val request = SetGameScoreRequest(
        userId = userId,
        score = score,
        force = force,
        disableEditMessage = disableEditMessage,
        chatId = chatId,
        messageId = messageId,
        inlineMessageId = inlineMessageId
    )
    return setGameScore(request)
}
