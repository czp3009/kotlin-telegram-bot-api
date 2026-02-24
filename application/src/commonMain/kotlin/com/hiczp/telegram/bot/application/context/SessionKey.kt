package com.hiczp.telegram.bot.application.context

import com.hiczp.telegram.bot.protocol.event.*

/**
 * A key that uniquely identifies a conversation session.
 *
 * Used to associate events with active conversations, typically based on the chat and user
 * that triggered the original event.
 *
 * @property chatId The chat ID where the conversation started, or null if not applicable.
 * @property userId The user ID who started the conversation, or null if not applicable (e.g., channel posts).
 */
data class SessionKey(
    val chatId: Long?,
    val userId: Long?
)

/**
 * Extracts a session key from this event for conversation tracking.
 *
 * Returns a [SessionKey] containing the chat ID and user ID from the event,
 * or null if the event type doesn't support session tracking (e.g., [PollEvent]).
 *
 * For most message-based events, both chat and user are extracted.
 * For inline queries and similar events, only the user ID is available.
 * For channel posts and chat boost events, the user ID may be null.
 */
val TelegramBotEvent.sessionKey: SessionKey?
    get() = when (this) {
        is MessageEvent -> SessionKey(message.chat.id, message.from?.id)
        is EditedMessageEvent -> SessionKey(editedMessage.chat.id, editedMessage.from?.id)
        is ChannelPostEvent -> SessionKey(channelPost.chat.id, channelPost.from?.id)
        is EditedChannelPostEvent -> SessionKey(editedChannelPost.chat.id, editedChannelPost.from?.id)
        is BusinessConnectionEvent -> SessionKey(businessConnection.userChatId, businessConnection.user.id)
        is BusinessMessageEvent -> SessionKey(businessMessage.chat.id, businessMessage.from?.id)
        is EditedBusinessMessageEvent -> SessionKey(editedBusinessMessage.chat.id, editedBusinessMessage.from?.id)
        is DeletedBusinessMessagesEvent -> SessionKey(deletedBusinessMessages.chat.id, null)
        is MessageReactionEvent -> SessionKey(messageReaction.chat.id, messageReaction.user?.id)
        is MessageReactionCountEvent -> SessionKey(messageReactionCount.chat.id, null)
        is InlineQueryEvent -> SessionKey(null, inlineQuery.from.id)
        is ChosenInlineResultEvent -> SessionKey(null, chosenInlineResult.from.id)
        is CallbackQueryEvent -> SessionKey(callbackQuery.message?.chat?.id, callbackQuery.from.id)
        is ShippingQueryEvent -> SessionKey(null, shippingQuery.from.id)
        is PreCheckoutQueryEvent -> SessionKey(null, preCheckoutQuery.from.id)
        is PurchasedPaidMediaEvent -> SessionKey(null, purchasedPaidMedia.from.id)
        is PollEvent -> null
        is PollAnswerEvent -> SessionKey(null, pollAnswer.user?.id ?: pollAnswer.voterChat?.id)
        is MyChatMemberEvent -> SessionKey(myChatMember.chat.id, myChatMember.from.id)
        is ChatMemberEvent -> SessionKey(chatMember.chat.id, chatMember.from.id)
        is ChatJoinRequestEvent -> SessionKey(chatJoinRequest.chat.id, chatJoinRequest.from.id)
        is ChatBoostEvent -> SessionKey(chatBoost.chat.id, null)
        is RemovedChatBoostEvent -> SessionKey(removedChatBoost.chat.id, null)
    }
