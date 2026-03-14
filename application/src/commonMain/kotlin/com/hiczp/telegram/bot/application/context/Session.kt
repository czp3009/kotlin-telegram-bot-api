package com.hiczp.telegram.bot.application.context

import com.hiczp.telegram.bot.protocol.event.*

/**
 * Extracts the chat ID from this Telegram event.
 *
 * This function returns the chat ID for event types that are associated with a chat.
 * For events without an associated chat (e.g., inline queries, polls), it returns null.
 *
 * @return The chat ID, or null if the event has no associated chat.
 * @see extractUserId
 * @see extractThreadId
 */
fun TelegramBotEvent.extractChatId(): Long? = when (this) {
    is MessageEvent -> message.chat.id
    is EditedMessageEvent -> editedMessage.chat.id
    is ChannelPostEvent -> channelPost.chat.id
    is EditedChannelPostEvent -> editedChannelPost.chat.id
    is BusinessConnectionEvent -> businessConnection.userChatId
    is BusinessMessageEvent -> businessMessage.chat.id
    is EditedBusinessMessageEvent -> editedBusinessMessage.chat.id
    is DeletedBusinessMessagesEvent -> deletedBusinessMessages.chat.id
    is MessageReactionEvent -> messageReaction.chat.id
    is MessageReactionCountEvent -> messageReactionCount.chat.id
    is InlineQueryEvent -> null
    is ChosenInlineResultEvent -> null
    is CallbackQueryEvent -> callbackQuery.message?.chat?.id
    is ShippingQueryEvent -> null
    is PreCheckoutQueryEvent -> null
    is PurchasedPaidMediaEvent -> null
    is PollEvent -> null
    is PollAnswerEvent -> null
    is MyChatMemberEvent -> myChatMember.chat.id
    is ChatMemberEvent -> chatMember.chat.id
    is ChatJoinRequestEvent -> chatJoinRequest.chat.id
    is ChatBoostEvent -> chatBoost.chat.id
    is RemovedChatBoostEvent -> removedChatBoost.chat.id
}

/**
 * Extracts the thread/topic ID from this Telegram event.
 *
 * This function returns the message thread ID for event types that are associated with a forum topic
 * or message thread. For events without a thread association, it returns null.
 *
 * @return The thread/topic ID, or null if the event has no associated thread.
 * @see extractChatId
 */
fun TelegramBotEvent.extractThreadId(): Long? = when (this) {
    is MessageEvent -> message.messageThreadId
    is EditedMessageEvent -> editedMessage.messageThreadId
    is ChannelPostEvent -> channelPost.messageThreadId
    is EditedChannelPostEvent -> editedChannelPost.messageThreadId
    is BusinessConnectionEvent -> null
    is BusinessMessageEvent -> businessMessage.messageThreadId
    is EditedBusinessMessageEvent -> editedBusinessMessage.messageThreadId
    is DeletedBusinessMessagesEvent -> null
    is MessageReactionEvent -> null
    is MessageReactionCountEvent -> null
    is InlineQueryEvent -> null
    is ChosenInlineResultEvent -> null
    is CallbackQueryEvent -> callbackQuery.message?.messageThreadId
    is ShippingQueryEvent -> null
    is PreCheckoutQueryEvent -> null
    is PurchasedPaidMediaEvent -> null
    is PollEvent -> null
    is PollAnswerEvent -> null
    is MyChatMemberEvent -> null
    is ChatMemberEvent -> null
    is ChatJoinRequestEvent -> null
    is ChatBoostEvent -> null
    is RemovedChatBoostEvent -> null
}

/**
 * Extracts the message ID from this Telegram event.
 *
 * This function returns the message ID for event types that are associated with a specific message.
 * For events without an associated message (e.g., inline queries, polls, chat member updates), it returns null.
 *
 * Note: For [DeletedBusinessMessagesEvent], this returns null because it contains multiple message IDs.
 * For [CallbackQueryEvent], this returns null if the callback is from an inline message (no chat message).
 *
 * @return The message ID, or null if the event has no associated message.
 * @see extractChatId
 */
fun TelegramBotEvent.extractMessageId(): Long? = when (this) {
    is MessageEvent -> message.messageId
    is EditedMessageEvent -> editedMessage.messageId
    is ChannelPostEvent -> channelPost.messageId
    is EditedChannelPostEvent -> editedChannelPost.messageId
    is BusinessConnectionEvent -> null
    is BusinessMessageEvent -> businessMessage.messageId
    is EditedBusinessMessageEvent -> editedBusinessMessage.messageId
    is DeletedBusinessMessagesEvent -> null
    is MessageReactionEvent -> messageReaction.messageId
    is MessageReactionCountEvent -> messageReactionCount.messageId
    is InlineQueryEvent -> null
    is ChosenInlineResultEvent -> null
    is CallbackQueryEvent -> callbackQuery.message?.messageId
    is ShippingQueryEvent -> null
    is PreCheckoutQueryEvent -> null
    is PurchasedPaidMediaEvent -> null
    is PollEvent -> null
    is PollAnswerEvent -> null
    is MyChatMemberEvent -> null
    is ChatMemberEvent -> null
    is ChatJoinRequestEvent -> null
    is ChatBoostEvent -> null
    is RemovedChatBoostEvent -> null
}

/**
 * Extracts the user ID from this Telegram event.
 *
 * This function returns the user ID for event types that are associated with a user.
 * For events without an associated user (e.g., channel posts without author, deleted messages), it returns null.
 *
 * Note: For some events like [PollAnswerEvent], the "user" might be a chat (via `voterChat`) rather than an individual.
 *
 * @return The user ID, or null if the event has no associated user.
 * @see extractChatId
 */
fun TelegramBotEvent.extractUserId(): Long? = when (this) {
    is MessageEvent -> message.from?.id
    is EditedMessageEvent -> editedMessage.from?.id
    is ChannelPostEvent -> channelPost.from?.id
    is EditedChannelPostEvent -> editedChannelPost.from?.id
    is BusinessConnectionEvent -> businessConnection.user.id
    is BusinessMessageEvent -> businessMessage.from?.id
    is EditedBusinessMessageEvent -> editedBusinessMessage.from?.id
    is DeletedBusinessMessagesEvent -> null
    is MessageReactionEvent -> messageReaction.user?.id
    is MessageReactionCountEvent -> null
    is InlineQueryEvent -> inlineQuery.from.id
    is ChosenInlineResultEvent -> chosenInlineResult.from.id
    is CallbackQueryEvent -> callbackQuery.from.id
    is ShippingQueryEvent -> shippingQuery.from.id
    is PreCheckoutQueryEvent -> preCheckoutQuery.from.id
    is PurchasedPaidMediaEvent -> purchasedPaidMedia.from.id
    is PollEvent -> null
    is PollAnswerEvent -> pollAnswer.user?.id ?: pollAnswer.voterChat?.id
    is MyChatMemberEvent -> myChatMember.from.id
    is ChatMemberEvent -> chatMember.from.id
    is ChatJoinRequestEvent -> chatJoinRequest.from.id
    is ChatBoostEvent -> null
    is RemovedChatBoostEvent -> null
}

/**
 * Extracts the language code from this Telegram event.
 *
 * This function returns the IETF language tag (e.g., "en", "zh", "ja") from the user object
 * in supported event types. For events without an associated user or language code, it returns null.
 *
 * @return The language code, or null if the event has no associated language code.
 * @see extractUserId
 */
fun TelegramBotEvent.extractLanguageCode(): String? = when (this) {
    is MessageEvent -> message.from?.languageCode
    is EditedMessageEvent -> editedMessage.from?.languageCode
    is ChannelPostEvent -> channelPost.from?.languageCode
    is EditedChannelPostEvent -> editedChannelPost.from?.languageCode
    is BusinessConnectionEvent -> businessConnection.user.languageCode
    is BusinessMessageEvent -> businessMessage.from?.languageCode
    is EditedBusinessMessageEvent -> editedBusinessMessage.from?.languageCode
    is DeletedBusinessMessagesEvent -> null
    is MessageReactionEvent -> messageReaction.user?.languageCode
    is MessageReactionCountEvent -> null
    is InlineQueryEvent -> inlineQuery.from.languageCode
    is ChosenInlineResultEvent -> chosenInlineResult.from.languageCode
    is CallbackQueryEvent -> callbackQuery.from.languageCode
    is ShippingQueryEvent -> shippingQuery.from.languageCode
    is PreCheckoutQueryEvent -> preCheckoutQuery.from.languageCode
    is PurchasedPaidMediaEvent -> purchasedPaidMedia.from.languageCode
    is PollEvent -> null
    is PollAnswerEvent -> pollAnswer.user?.languageCode
    is MyChatMemberEvent -> myChatMember.from.languageCode
    is ChatMemberEvent -> chatMember.from.languageCode
    is ChatJoinRequestEvent -> chatJoinRequest.from.languageCode
    is ChatBoostEvent -> null
    is RemovedChatBoostEvent -> null
}
