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
