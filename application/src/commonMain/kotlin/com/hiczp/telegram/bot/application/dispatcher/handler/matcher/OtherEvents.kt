@file:Suppress("unused")

package com.hiczp.telegram.bot.application.dispatcher.handler.matcher

import com.hiczp.telegram.bot.application.dispatcher.handler.HandlerRoute
import com.hiczp.telegram.bot.protocol.event.*
import kotlin.jvm.JvmName

@JvmName("pollId")
fun HandlerRoute<PollEvent>.id(id: String, build: HandlerRoute<PollEvent>.() -> Unit) =
    filter({ event.poll.id == id }, build)

@JvmName("pollAnswerPollId")
fun HandlerRoute<PollAnswerEvent>.pollId(id: String, build: HandlerRoute<PollAnswerEvent>.() -> Unit) =
    filter({ event.pollAnswer.pollId == id }, build)

@JvmName("pollAnswerFromUser")
fun HandlerRoute<PollAnswerEvent>.fromUser(userId: Long, build: HandlerRoute<PollAnswerEvent>.() -> Unit) =
    filter({ event.pollAnswer.user?.id == userId }, build)

@JvmName("shippingFromUser")
fun HandlerRoute<ShippingQueryEvent>.fromUser(userId: Long, build: HandlerRoute<ShippingQueryEvent>.() -> Unit) =
    filter({ event.shippingQuery.from.id == userId }, build)

fun HandlerRoute<ShippingQueryEvent>.payload(value: String, build: HandlerRoute<ShippingQueryEvent>.() -> Unit) =
    filter({ event.shippingQuery.invoicePayload == value }, build)

@JvmName("preCheckoutFromUser")
fun HandlerRoute<PreCheckoutQueryEvent>.fromUser(userId: Long, build: HandlerRoute<PreCheckoutQueryEvent>.() -> Unit) =
    filter({ event.preCheckoutQuery.from.id == userId }, build)

@JvmName("preCheckoutPayload")
fun HandlerRoute<PreCheckoutQueryEvent>.payload(value: String, build: HandlerRoute<PreCheckoutQueryEvent>.() -> Unit) =
    filter({ event.preCheckoutQuery.invoicePayload == value }, build)

fun HandlerRoute<PreCheckoutQueryEvent>.currency(value: String, build: HandlerRoute<PreCheckoutQueryEvent>.() -> Unit) =
    filter({ event.preCheckoutQuery.currency == value }, build)

@JvmName("purchasedPaidMediaFromUser")
fun HandlerRoute<PurchasedPaidMediaEvent>.fromUser(
    userId: Long,
    build: HandlerRoute<PurchasedPaidMediaEvent>.() -> Unit
) = filter({ event.purchasedPaidMedia.from.id == userId }, build)

@JvmName("chatJoinRequestFromUser")
fun HandlerRoute<ChatJoinRequestEvent>.fromUser(userId: Long, build: HandlerRoute<ChatJoinRequestEvent>.() -> Unit) =
    filter({ event.chatJoinRequest.from.id == userId }, build)

@JvmName("chatJoinRequestInChat")
fun HandlerRoute<ChatJoinRequestEvent>.inChat(chatId: Long, build: HandlerRoute<ChatJoinRequestEvent>.() -> Unit) =
    filter({ event.chatJoinRequest.chat.id == chatId }, build)

@JvmName("chatMemberInChat")
fun HandlerRoute<ChatMemberEvent>.inChat(chatId: Long, build: HandlerRoute<ChatMemberEvent>.() -> Unit) =
    filter({ event.chatMember.chat.id == chatId }, build)

@JvmName("myChatMemberInChat")
fun HandlerRoute<MyChatMemberEvent>.inChat(chatId: Long, build: HandlerRoute<MyChatMemberEvent>.() -> Unit) =
    filter({ event.myChatMember.chat.id == chatId }, build)

@JvmName("chatBoostInChat")
fun HandlerRoute<ChatBoostEvent>.inChat(chatId: Long, build: HandlerRoute<ChatBoostEvent>.() -> Unit) =
    filter({ event.chatBoost.chat.id == chatId }, build)

@JvmName("removedChatBoostInChat")
fun HandlerRoute<RemovedChatBoostEvent>.inChat(chatId: Long, build: HandlerRoute<RemovedChatBoostEvent>.() -> Unit) =
    filter({ event.removedChatBoost.chat.id == chatId }, build)
