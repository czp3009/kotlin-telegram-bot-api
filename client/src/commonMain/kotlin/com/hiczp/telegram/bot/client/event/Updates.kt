// Auto-generated from Update model, do not modify this file manually
package com.hiczp.telegram.bot.client.event

import com.hiczp.telegram.bot.protocol.model.Update

public fun Update.toTelegramBotEvent(): TelegramBotEvent {
    message?.let {
        return MessageEvent(updateId, it)
    }
    editedMessage?.let {
        return EditedMessageEvent(updateId, it)
    }
    channelPost?.let {
        return ChannelPostEvent(updateId, it)
    }
    editedChannelPost?.let {
        return EditedChannelPostEvent(updateId, it)
    }
    businessConnection?.let {
        return BusinessConnectionEvent(updateId, it)
    }
    businessMessage?.let {
        return BusinessMessageEvent(updateId, it)
    }
    editedBusinessMessage?.let {
        return EditedBusinessMessageEvent(updateId, it)
    }
    deletedBusinessMessages?.let {
        return DeletedBusinessMessagesEvent(updateId, it)
    }
    messageReaction?.let {
        return MessageReactionEvent(updateId, it)
    }
    messageReactionCount?.let {
        return MessageReactionCountEvent(updateId, it)
    }
    inlineQuery?.let {
        return InlineQueryEvent(updateId, it)
    }
    chosenInlineResult?.let {
        return ChosenInlineResultEvent(updateId, it)
    }
    callbackQuery?.let {
        return CallbackQueryEvent(updateId, it)
    }
    shippingQuery?.let {
        return ShippingQueryEvent(updateId, it)
    }
    preCheckoutQuery?.let {
        return PreCheckoutQueryEvent(updateId, it)
    }
    purchasedPaidMedia?.let {
        return PurchasedPaidMediaEvent(updateId, it)
    }
    poll?.let {
        return PollEvent(updateId, it)
    }
    pollAnswer?.let {
        return PollAnswerEvent(updateId, it)
    }
    myChatMember?.let {
        return MyChatMemberEvent(updateId, it)
    }
    chatMember?.let {
        return ChatMemberEvent(updateId, it)
    }
    chatJoinRequest?.let {
        return ChatJoinRequestEvent(updateId, it)
    }
    chatBoost?.let {
        return ChatBoostEvent(updateId, it)
    }
    removedChatBoost?.let {
        return RemovedChatBoostEvent(updateId, it)
    }
    error("Unrecognized Update: $this")
}
