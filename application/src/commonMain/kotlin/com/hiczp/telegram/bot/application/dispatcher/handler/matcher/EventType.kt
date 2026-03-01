@file:Suppress("unused")

package com.hiczp.telegram.bot.application.dispatcher.handler.matcher

import com.hiczp.telegram.bot.application.dispatcher.handler.HandlerBotCall
import com.hiczp.telegram.bot.application.dispatcher.handler.HandlerRoute
import com.hiczp.telegram.bot.protocol.event.*
import kotlin.jvm.JvmName

// ==================== MessageEvent ====================

// --- onMessageEvent (stackable) ---

@JvmName("onMessageEventTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onMessageEvent(build: HandlerRoute<MessageEvent>.() -> Unit) =
    on<MessageEvent> { build() }

// --- whenMessageEvent (terminal) ---

@JvmName("whenMessageEventTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenMessageEvent(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    onMessageEvent { handle(handler) }

// ==================== ChannelPostEvent ====================

// --- onChannelPostEvent (stackable) ---

@JvmName("onChannelPostEventTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onChannelPostEvent(build: HandlerRoute<ChannelPostEvent>.() -> Unit) =
    on<ChannelPostEvent> { build() }

// --- whenChannelPostEvent (terminal) ---

@JvmName("whenChannelPostEventTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenChannelPostEvent(handler: suspend HandlerBotCall<ChannelPostEvent>.() -> Unit) =
    onChannelPostEvent { handle(handler) }

// ==================== EditedMessageEvent ====================

// --- onEditedMessageEvent (stackable) ---

@JvmName("onEditedMessageEventTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onEditedMessageEvent(build: HandlerRoute<EditedMessageEvent>.() -> Unit) =
    on<EditedMessageEvent> { build() }

// --- whenEditedMessageEvent (terminal) ---

@JvmName("whenEditedMessageEventTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenEditedMessageEvent(handler: suspend HandlerBotCall<EditedMessageEvent>.() -> Unit) =
    onEditedMessageEvent { handle(handler) }

// ==================== EditedChannelPostEvent ====================

// --- onEditedChannelPostEvent (stackable) ---

@JvmName("onEditedChannelPostEventTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onEditedChannelPostEvent(build: HandlerRoute<EditedChannelPostEvent>.() -> Unit) =
    on<EditedChannelPostEvent> { build() }

// --- whenEditedChannelPostEvent (terminal) ---

@JvmName("whenEditedChannelPostEventTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenEditedChannelPostEvent(handler: suspend HandlerBotCall<EditedChannelPostEvent>.() -> Unit) =
    onEditedChannelPostEvent { handle(handler) }

// ==================== InlineQueryEvent ====================

// --- onInlineQueryEvent (stackable) ---

@JvmName("onInlineQueryEventTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onInlineQueryEvent(build: HandlerRoute<InlineQueryEvent>.() -> Unit) =
    on<InlineQueryEvent> { build() }

// --- whenInlineQueryEvent (terminal) ---

@JvmName("whenInlineQueryEventTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenInlineQueryEvent(handler: suspend HandlerBotCall<InlineQueryEvent>.() -> Unit) =
    onInlineQueryEvent { handle(handler) }

// ==================== ChosenInlineResultEvent ====================

// --- onChosenInlineResultEvent (stackable) ---

@JvmName("onChosenInlineResultEventTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onChosenInlineResultEvent(build: HandlerRoute<ChosenInlineResultEvent>.() -> Unit) =
    on<ChosenInlineResultEvent> { build() }

// --- whenChosenInlineResultEvent (terminal) ---

@JvmName("whenChosenInlineResultEventTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenChosenInlineResultEvent(handler: suspend HandlerBotCall<ChosenInlineResultEvent>.() -> Unit) =
    onChosenInlineResultEvent { handle(handler) }

// ==================== CallbackQueryEvent ====================

// --- onCallbackQueryEvent (stackable) ---

@JvmName("onCallbackQueryEventTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onCallbackQueryEvent(build: HandlerRoute<CallbackQueryEvent>.() -> Unit) =
    on<CallbackQueryEvent> { build() }

// --- whenCallbackQueryEvent (terminal) ---

@JvmName("whenCallbackQueryEventTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenCallbackQueryEvent(handler: suspend HandlerBotCall<CallbackQueryEvent>.() -> Unit) =
    onCallbackQueryEvent { handle(handler) }

// ==================== ShippingQueryEvent ====================

// --- onShippingQueryEvent (stackable) ---

@JvmName("onShippingQueryEventTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onShippingQueryEvent(build: HandlerRoute<ShippingQueryEvent>.() -> Unit) =
    on<ShippingQueryEvent> { build() }

// --- whenShippingQueryEvent (terminal) ---

@JvmName("whenShippingQueryEventTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenShippingQueryEvent(handler: suspend HandlerBotCall<ShippingQueryEvent>.() -> Unit) =
    onShippingQueryEvent { handle(handler) }

// ==================== PreCheckoutQueryEvent ====================

// --- onPreCheckoutQueryEvent (stackable) ---

@JvmName("onPreCheckoutQueryEventTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onPreCheckoutQueryEvent(build: HandlerRoute<PreCheckoutQueryEvent>.() -> Unit) =
    on<PreCheckoutQueryEvent> { build() }

// --- whenPreCheckoutQueryEvent (terminal) ---

@JvmName("whenPreCheckoutQueryEventTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenPreCheckoutQueryEvent(handler: suspend HandlerBotCall<PreCheckoutQueryEvent>.() -> Unit) =
    onPreCheckoutQueryEvent { handle(handler) }

// ==================== PurchasedPaidMediaEvent ====================

// --- onPurchasedPaidMediaEvent (stackable) ---

@JvmName("onPurchasedPaidMediaEventTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onPurchasedPaidMediaEvent(build: HandlerRoute<PurchasedPaidMediaEvent>.() -> Unit) =
    on<PurchasedPaidMediaEvent> { build() }

// --- whenPurchasedPaidMediaEvent (terminal) ---

@JvmName("whenPurchasedPaidMediaEventTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenPurchasedPaidMediaEvent(handler: suspend HandlerBotCall<PurchasedPaidMediaEvent>.() -> Unit) =
    onPurchasedPaidMediaEvent { handle(handler) }

// ==================== PollEvent ====================

// --- onPollEvent (stackable) ---

@JvmName("onPollEventTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onPollEvent(build: HandlerRoute<PollEvent>.() -> Unit) =
    on<PollEvent> { build() }

// --- whenPollEvent (terminal) ---

@JvmName("whenPollEventTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenPollEvent(handler: suspend HandlerBotCall<PollEvent>.() -> Unit) =
    onPollEvent { handle(handler) }

// ==================== PollAnswerEvent ====================

// --- onPollAnswerEvent (stackable) ---

@JvmName("onPollAnswerEventTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onPollAnswerEvent(build: HandlerRoute<PollAnswerEvent>.() -> Unit) =
    on<PollAnswerEvent> { build() }

// --- whenPollAnswerEvent (terminal) ---

@JvmName("whenPollAnswerEventTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenPollAnswerEvent(handler: suspend HandlerBotCall<PollAnswerEvent>.() -> Unit) =
    onPollAnswerEvent { handle(handler) }

// ==================== MessageReactionEvent ====================

// --- onMessageReactionEvent (stackable) ---

@JvmName("onMessageReactionEventTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onMessageReactionEvent(build: HandlerRoute<MessageReactionEvent>.() -> Unit) =
    on<MessageReactionEvent> { build() }

// --- whenMessageReactionEvent (terminal) ---

@JvmName("whenMessageReactionEventTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenMessageReactionEvent(handler: suspend HandlerBotCall<MessageReactionEvent>.() -> Unit) =
    onMessageReactionEvent { handle(handler) }

// ==================== MessageReactionCountEvent ====================

// --- onMessageReactionCountEvent (stackable) ---

@JvmName("onMessageReactionCountEventTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onMessageReactionCountEvent(build: HandlerRoute<MessageReactionCountEvent>.() -> Unit) =
    on<MessageReactionCountEvent> { build() }

// --- whenMessageReactionCountEvent (terminal) ---

@JvmName("whenMessageReactionCountEventTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenMessageReactionCountEvent(handler: suspend HandlerBotCall<MessageReactionCountEvent>.() -> Unit) =
    onMessageReactionCountEvent { handle(handler) }

// ==================== ChatJoinRequestEvent ====================

// --- onChatJoinRequestEvent (stackable) ---

@JvmName("onChatJoinRequestEventTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onChatJoinRequestEvent(build: HandlerRoute<ChatJoinRequestEvent>.() -> Unit) =
    on<ChatJoinRequestEvent> { build() }

// --- whenChatJoinRequestEvent (terminal) ---

@JvmName("whenChatJoinRequestEventTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenChatJoinRequestEvent(handler: suspend HandlerBotCall<ChatJoinRequestEvent>.() -> Unit) =
    onChatJoinRequestEvent { handle(handler) }

// ==================== ChatMemberEvent ====================

// --- onChatMemberEvent (stackable) ---

@JvmName("onChatMemberEventTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onChatMemberEvent(build: HandlerRoute<ChatMemberEvent>.() -> Unit) =
    on<ChatMemberEvent> { build() }

// --- whenChatMemberEvent (terminal) ---

@JvmName("whenChatMemberEventTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenChatMemberEvent(handler: suspend HandlerBotCall<ChatMemberEvent>.() -> Unit) =
    onChatMemberEvent { handle(handler) }

// ==================== MyChatMemberEvent ====================

// --- onMyChatMemberEvent (stackable) ---

@JvmName("onMyChatMemberEventTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onMyChatMemberEvent(build: HandlerRoute<MyChatMemberEvent>.() -> Unit) =
    on<MyChatMemberEvent> { build() }

// --- whenMyChatMemberEvent (terminal) ---

@JvmName("whenMyChatMemberEventTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenMyChatMemberEvent(handler: suspend HandlerBotCall<MyChatMemberEvent>.() -> Unit) =
    onMyChatMemberEvent { handle(handler) }

// ==================== ChatBoostEvent ====================

// --- onChatBoostEvent (stackable) ---

@JvmName("onChatBoostEventTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onChatBoostEvent(build: HandlerRoute<ChatBoostEvent>.() -> Unit) =
    on<ChatBoostEvent> { build() }

// --- whenChatBoostEvent (terminal) ---

@JvmName("whenChatBoostEventTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenChatBoostEvent(handler: suspend HandlerBotCall<ChatBoostEvent>.() -> Unit) =
    onChatBoostEvent { handle(handler) }

// ==================== RemovedChatBoostEvent ====================

// --- onRemovedChatBoostEvent (stackable) ---

@JvmName("onRemovedChatBoostEventTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onRemovedChatBoostEvent(build: HandlerRoute<RemovedChatBoostEvent>.() -> Unit) =
    on<RemovedChatBoostEvent> { build() }

// --- whenRemovedChatBoostEvent (terminal) ---

@JvmName("whenRemovedChatBoostEventTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenRemovedChatBoostEvent(handler: suspend HandlerBotCall<RemovedChatBoostEvent>.() -> Unit) =
    onRemovedChatBoostEvent { handle(handler) }

// ==================== BusinessConnectionEvent ====================

// --- onBusinessConnectionEvent (stackable) ---

@JvmName("onBusinessConnectionEventTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onBusinessConnectionEvent(build: HandlerRoute<BusinessConnectionEvent>.() -> Unit) =
    on<BusinessConnectionEvent> { build() }

// --- whenBusinessConnectionEvent (terminal) ---

@JvmName("whenBusinessConnectionEventTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenBusinessConnectionEvent(handler: suspend HandlerBotCall<BusinessConnectionEvent>.() -> Unit) =
    onBusinessConnectionEvent { handle(handler) }

// ==================== BusinessMessageEvent ====================

// --- onBusinessMessageEvent (stackable) ---

@JvmName("onBusinessMessageEventTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onBusinessMessageEvent(build: HandlerRoute<BusinessMessageEvent>.() -> Unit) =
    on<BusinessMessageEvent> { build() }

// --- whenBusinessMessageEvent (terminal) ---

@JvmName("whenBusinessMessageEventTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenBusinessMessageEvent(handler: suspend HandlerBotCall<BusinessMessageEvent>.() -> Unit) =
    onBusinessMessageEvent { handle(handler) }

// ==================== EditedBusinessMessageEvent ====================

// --- onEditedBusinessMessageEvent (stackable) ---

@JvmName("onEditedBusinessMessageEventTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onEditedBusinessMessageEvent(build: HandlerRoute<EditedBusinessMessageEvent>.() -> Unit) =
    on<EditedBusinessMessageEvent> { build() }

// --- whenEditedBusinessMessageEvent (terminal) ---

@JvmName("whenEditedBusinessMessageEventTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenEditedBusinessMessageEvent(handler: suspend HandlerBotCall<EditedBusinessMessageEvent>.() -> Unit) =
    onEditedBusinessMessageEvent { handle(handler) }

// ==================== DeletedBusinessMessagesEvent ====================

// --- onDeletedBusinessMessagesEvent (stackable) ---

@JvmName("onDeletedBusinessMessagesEventTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onDeletedBusinessMessagesEvent(build: HandlerRoute<DeletedBusinessMessagesEvent>.() -> Unit) =
    on<DeletedBusinessMessagesEvent> { build() }

// --- whenDeletedBusinessMessagesEvent (terminal) ---

@JvmName("whenDeletedBusinessMessagesEventTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenDeletedBusinessMessagesEvent(handler: suspend HandlerBotCall<DeletedBusinessMessagesEvent>.() -> Unit) =
    onDeletedBusinessMessagesEvent { handle(handler) }
