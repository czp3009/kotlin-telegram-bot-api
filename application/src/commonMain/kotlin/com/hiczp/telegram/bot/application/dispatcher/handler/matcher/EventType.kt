@file:Suppress("unused")

package com.hiczp.telegram.bot.application.dispatcher.handler.matcher

import com.hiczp.telegram.bot.application.dispatcher.handler.HandlerRoute
import com.hiczp.telegram.bot.protocol.event.*

fun HandlerRoute<TelegramBotEvent>.message(build: HandlerRoute<MessageEvent>.() -> Unit) = filter<MessageEvent>(build)
fun HandlerRoute<TelegramBotEvent>.channelPost(build: HandlerRoute<ChannelPostEvent>.() -> Unit) =
    filter<ChannelPostEvent>(build)

fun HandlerRoute<TelegramBotEvent>.editedMessage(build: HandlerRoute<EditedMessageEvent>.() -> Unit) =
    filter<EditedMessageEvent>(build)

fun HandlerRoute<TelegramBotEvent>.editedChannelPost(build: HandlerRoute<EditedChannelPostEvent>.() -> Unit) =
    filter<EditedChannelPostEvent>(build)

fun HandlerRoute<TelegramBotEvent>.inlineQuery(build: HandlerRoute<InlineQueryEvent>.() -> Unit) =
    filter<InlineQueryEvent>(build)

fun HandlerRoute<TelegramBotEvent>.chosenInlineResult(build: HandlerRoute<ChosenInlineResultEvent>.() -> Unit) =
    filter<ChosenInlineResultEvent>(build)

fun HandlerRoute<TelegramBotEvent>.callbackQuery(build: HandlerRoute<CallbackQueryEvent>.() -> Unit) =
    filter<CallbackQueryEvent>(build)

fun HandlerRoute<TelegramBotEvent>.shippingQuery(build: HandlerRoute<ShippingQueryEvent>.() -> Unit) =
    filter<ShippingQueryEvent>(build)

fun HandlerRoute<TelegramBotEvent>.preCheckoutQuery(build: HandlerRoute<PreCheckoutQueryEvent>.() -> Unit) =
    filter<PreCheckoutQueryEvent>(build)

fun HandlerRoute<TelegramBotEvent>.purchasedPaidMedia(build: HandlerRoute<PurchasedPaidMediaEvent>.() -> Unit) =
    filter<PurchasedPaidMediaEvent>(build)

fun HandlerRoute<TelegramBotEvent>.poll(build: HandlerRoute<PollEvent>.() -> Unit) = filter<PollEvent>(build)
fun HandlerRoute<TelegramBotEvent>.pollAnswer(build: HandlerRoute<PollAnswerEvent>.() -> Unit) =
    filter<PollAnswerEvent>(build)

fun HandlerRoute<TelegramBotEvent>.messageReaction(build: HandlerRoute<MessageReactionEvent>.() -> Unit) =
    filter<MessageReactionEvent>(build)

fun HandlerRoute<TelegramBotEvent>.messageReactionCount(build: HandlerRoute<MessageReactionCountEvent>.() -> Unit) =
    filter<MessageReactionCountEvent>(build)

fun HandlerRoute<TelegramBotEvent>.chatJoinRequest(build: HandlerRoute<ChatJoinRequestEvent>.() -> Unit) =
    filter<ChatJoinRequestEvent>(build)

fun HandlerRoute<TelegramBotEvent>.chatMember(build: HandlerRoute<ChatMemberEvent>.() -> Unit) =
    filter<ChatMemberEvent>(build)

fun HandlerRoute<TelegramBotEvent>.myChatMember(build: HandlerRoute<MyChatMemberEvent>.() -> Unit) =
    filter<MyChatMemberEvent>(build)

fun HandlerRoute<TelegramBotEvent>.chatBoost(build: HandlerRoute<ChatBoostEvent>.() -> Unit) =
    filter<ChatBoostEvent>(build)

fun HandlerRoute<TelegramBotEvent>.removedChatBoost(build: HandlerRoute<RemovedChatBoostEvent>.() -> Unit) =
    filter<RemovedChatBoostEvent>(build)

fun HandlerRoute<TelegramBotEvent>.businessConnection(build: HandlerRoute<BusinessConnectionEvent>.() -> Unit) =
    filter<BusinessConnectionEvent>(build)

fun HandlerRoute<TelegramBotEvent>.businessMessage(build: HandlerRoute<BusinessMessageEvent>.() -> Unit) =
    filter<BusinessMessageEvent>(build)

fun HandlerRoute<TelegramBotEvent>.editedBusinessMessage(build: HandlerRoute<EditedBusinessMessageEvent>.() -> Unit) =
    filter<EditedBusinessMessageEvent>(build)

fun HandlerRoute<TelegramBotEvent>.deletedBusinessMessages(build: HandlerRoute<DeletedBusinessMessagesEvent>.() -> Unit) =
    filter<DeletedBusinessMessagesEvent>(build)
