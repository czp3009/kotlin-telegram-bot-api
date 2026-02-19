// Auto-generated from the Update model, do not modify this file manually
package com.hiczp.telegram.bot.client.event

import com.hiczp.telegram.bot.protocol.model.BusinessConnection
import com.hiczp.telegram.bot.protocol.model.BusinessMessagesDeleted
import com.hiczp.telegram.bot.protocol.model.CallbackQuery
import com.hiczp.telegram.bot.protocol.model.ChatBoostRemoved
import com.hiczp.telegram.bot.protocol.model.ChatBoostUpdated
import com.hiczp.telegram.bot.protocol.model.ChatJoinRequest
import com.hiczp.telegram.bot.protocol.model.ChatMemberUpdated
import com.hiczp.telegram.bot.protocol.model.ChosenInlineResult
import com.hiczp.telegram.bot.protocol.model.InlineQuery
import com.hiczp.telegram.bot.protocol.model.Message
import com.hiczp.telegram.bot.protocol.model.MessageReactionCountUpdated
import com.hiczp.telegram.bot.protocol.model.MessageReactionUpdated
import com.hiczp.telegram.bot.protocol.model.PaidMediaPurchased
import com.hiczp.telegram.bot.protocol.model.Poll
import com.hiczp.telegram.bot.protocol.model.PollAnswer
import com.hiczp.telegram.bot.protocol.model.PreCheckoutQuery
import com.hiczp.telegram.bot.protocol.model.ShippingQuery
import kotlin.Long

public sealed interface TelegramBotEvent {
    public val updateId: Long
}

public data class MessageEvent(
    override val updateId: Long,
    public val message: Message,
) : TelegramBotEvent

public data class EditedMessageEvent(
    override val updateId: Long,
    public val editedMessage: Message,
) : TelegramBotEvent

public data class ChannelPostEvent(
    override val updateId: Long,
    public val channelPost: Message,
) : TelegramBotEvent

public data class EditedChannelPostEvent(
    override val updateId: Long,
    public val editedChannelPost: Message,
) : TelegramBotEvent

public data class BusinessConnectionEvent(
    override val updateId: Long,
    public val businessConnection: BusinessConnection,
) : TelegramBotEvent

public data class BusinessMessageEvent(
    override val updateId: Long,
    public val businessMessage: Message,
) : TelegramBotEvent

public data class EditedBusinessMessageEvent(
    override val updateId: Long,
    public val editedBusinessMessage: Message,
) : TelegramBotEvent

public data class DeletedBusinessMessagesEvent(
    override val updateId: Long,
    public val deletedBusinessMessages: BusinessMessagesDeleted,
) : TelegramBotEvent

public data class MessageReactionEvent(
    override val updateId: Long,
    public val messageReaction: MessageReactionUpdated,
) : TelegramBotEvent

public data class MessageReactionCountEvent(
    override val updateId: Long,
    public val messageReactionCount: MessageReactionCountUpdated,
) : TelegramBotEvent

public data class InlineQueryEvent(
    override val updateId: Long,
    public val inlineQuery: InlineQuery,
) : TelegramBotEvent

public data class ChosenInlineResultEvent(
    override val updateId: Long,
    public val chosenInlineResult: ChosenInlineResult,
) : TelegramBotEvent

public data class CallbackQueryEvent(
    override val updateId: Long,
    public val callbackQuery: CallbackQuery,
) : TelegramBotEvent

public data class ShippingQueryEvent(
    override val updateId: Long,
    public val shippingQuery: ShippingQuery,
) : TelegramBotEvent

public data class PreCheckoutQueryEvent(
    override val updateId: Long,
    public val preCheckoutQuery: PreCheckoutQuery,
) : TelegramBotEvent

public data class PurchasedPaidMediaEvent(
    override val updateId: Long,
    public val purchasedPaidMedia: PaidMediaPurchased,
) : TelegramBotEvent

public data class PollEvent(
    override val updateId: Long,
    public val poll: Poll,
) : TelegramBotEvent

public data class PollAnswerEvent(
    override val updateId: Long,
    public val pollAnswer: PollAnswer,
) : TelegramBotEvent

public data class MyChatMemberEvent(
    override val updateId: Long,
    public val myChatMember: ChatMemberUpdated,
) : TelegramBotEvent

public data class ChatMemberEvent(
    override val updateId: Long,
    public val chatMember: ChatMemberUpdated,
) : TelegramBotEvent

public data class ChatJoinRequestEvent(
    override val updateId: Long,
    public val chatJoinRequest: ChatJoinRequest,
) : TelegramBotEvent

public data class ChatBoostEvent(
    override val updateId: Long,
    public val chatBoost: ChatBoostUpdated,
) : TelegramBotEvent

public data class RemovedChatBoostEvent(
    override val updateId: Long,
    public val removedChatBoost: ChatBoostRemoved,
) : TelegramBotEvent
