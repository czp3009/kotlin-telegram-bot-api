@file:Suppress("unused")

package com.hiczp.telegram.bot.application.dispatcher.handler.matcher

import com.hiczp.telegram.bot.application.dispatcher.handler.HandlerBotCall
import com.hiczp.telegram.bot.application.dispatcher.handler.HandlerRoute
import com.hiczp.telegram.bot.protocol.event.MessageEvent
import com.hiczp.telegram.bot.protocol.event.TelegramBotEvent
import kotlin.jvm.JvmName

// ==================== newChatMembers ====================

// --- onMessageEventNewChatMembers (stackable) ---

@JvmName("onMessageEventNewChatMembersMessageEvent")
fun HandlerRoute<MessageEvent>.onMessageEventNewChatMembers(build: HandlerRoute<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.newChatMembers != null) it else null }, build)

@JvmName("onMessageEventNewChatMembersTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onMessageEventNewChatMembers(build: HandlerRoute<MessageEvent>.() -> Unit) =
    on<MessageEvent> { onMessageEventNewChatMembers(build) }

// --- whenMessageEventNewChatMembers (terminal) ---

@JvmName("whenMessageEventNewChatMembersMessageEvent")
fun HandlerRoute<MessageEvent>.whenMessageEventNewChatMembers(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.newChatMembers != null) it else null }) { handle(handler) }

@JvmName("whenMessageEventNewChatMembersTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenMessageEventNewChatMembers(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    on<MessageEvent> { whenMessageEventNewChatMembers(handler) }

// ==================== leftChatMember ====================

// --- onMessageEventLeftChatMember (stackable) ---

@JvmName("onMessageEventLeftChatMemberMessageEvent")
fun HandlerRoute<MessageEvent>.onMessageEventLeftChatMember(build: HandlerRoute<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.leftChatMember != null) it else null }, build)

@JvmName("onMessageEventLeftChatMemberTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onMessageEventLeftChatMember(build: HandlerRoute<MessageEvent>.() -> Unit) =
    on<MessageEvent> { onMessageEventLeftChatMember(build) }

// --- whenMessageEventLeftChatMember (terminal) ---

@JvmName("whenMessageEventLeftChatMemberMessageEvent")
fun HandlerRoute<MessageEvent>.whenMessageEventLeftChatMember(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.leftChatMember != null) it else null }) { handle(handler) }

@JvmName("whenMessageEventLeftChatMemberTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenMessageEventLeftChatMember(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    on<MessageEvent> { whenMessageEventLeftChatMember(handler) }

// ==================== newChatTitle ====================

// --- onMessageEventNewChatTitle (stackable) ---

@JvmName("onMessageEventNewChatTitleMessageEvent")
fun HandlerRoute<MessageEvent>.onMessageEventNewChatTitle(build: HandlerRoute<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.newChatTitle != null) it else null }, build)

@JvmName("onMessageEventNewChatTitleTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onMessageEventNewChatTitle(build: HandlerRoute<MessageEvent>.() -> Unit) =
    on<MessageEvent> { onMessageEventNewChatTitle(build) }

// --- whenMessageEventNewChatTitle (terminal) ---

@JvmName("whenMessageEventNewChatTitleMessageEvent")
fun HandlerRoute<MessageEvent>.whenMessageEventNewChatTitle(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.newChatTitle != null) it else null }) { handle(handler) }

@JvmName("whenMessageEventNewChatTitleTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenMessageEventNewChatTitle(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    on<MessageEvent> { whenMessageEventNewChatTitle(handler) }

// ==================== newChatPhoto ====================

// --- onMessageEventNewChatPhoto (stackable) ---

@JvmName("onMessageEventNewChatPhotoMessageEvent")
fun HandlerRoute<MessageEvent>.onMessageEventNewChatPhoto(build: HandlerRoute<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.newChatPhoto != null) it else null }, build)

@JvmName("onMessageEventNewChatPhotoTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onMessageEventNewChatPhoto(build: HandlerRoute<MessageEvent>.() -> Unit) =
    on<MessageEvent> { onMessageEventNewChatPhoto(build) }

// --- whenMessageEventNewChatPhoto (terminal) ---

@JvmName("whenMessageEventNewChatPhotoMessageEvent")
fun HandlerRoute<MessageEvent>.whenMessageEventNewChatPhoto(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.newChatPhoto != null) it else null }) { handle(handler) }

@JvmName("whenMessageEventNewChatPhotoTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenMessageEventNewChatPhoto(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    on<MessageEvent> { whenMessageEventNewChatPhoto(handler) }

// ==================== deleteChatPhoto ====================

// --- onMessageEventDeleteChatPhoto (stackable) ---

@JvmName("onMessageEventDeleteChatPhotoMessageEvent")
fun HandlerRoute<MessageEvent>.onMessageEventDeleteChatPhoto(build: HandlerRoute<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.deleteChatPhoto == true) it else null }, build)

@JvmName("onMessageEventDeleteChatPhotoTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onMessageEventDeleteChatPhoto(build: HandlerRoute<MessageEvent>.() -> Unit) =
    on<MessageEvent> { onMessageEventDeleteChatPhoto(build) }

// --- whenMessageEventDeleteChatPhoto (terminal) ---

@JvmName("whenMessageEventDeleteChatPhotoMessageEvent")
fun HandlerRoute<MessageEvent>.whenMessageEventDeleteChatPhoto(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.deleteChatPhoto == true) it else null }) { handle(handler) }

@JvmName("whenMessageEventDeleteChatPhotoTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenMessageEventDeleteChatPhoto(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    on<MessageEvent> { whenMessageEventDeleteChatPhoto(handler) }

// ==================== pinnedMessage ====================

// --- onMessageEventPinnedMessage (stackable) ---

@JvmName("onMessageEventPinnedMessageMessageEvent")
fun HandlerRoute<MessageEvent>.onMessageEventPinnedMessage(build: HandlerRoute<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.pinnedMessage != null) it else null }, build)

@JvmName("onMessageEventPinnedMessageTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onMessageEventPinnedMessage(build: HandlerRoute<MessageEvent>.() -> Unit) =
    on<MessageEvent> { onMessageEventPinnedMessage(build) }

// --- whenMessageEventPinnedMessage (terminal) ---

@JvmName("whenMessageEventPinnedMessageMessageEvent")
fun HandlerRoute<MessageEvent>.whenMessageEventPinnedMessage(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.pinnedMessage != null) it else null }) { handle(handler) }

@JvmName("whenMessageEventPinnedMessageTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenMessageEventPinnedMessage(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    on<MessageEvent> { whenMessageEventPinnedMessage(handler) }

// ==================== groupCreated ====================

// --- onMessageEventGroupCreated (stackable) ---

@JvmName("onMessageEventGroupCreatedMessageEvent")
fun HandlerRoute<MessageEvent>.onMessageEventGroupCreated(build: HandlerRoute<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.groupChatCreated == true) it else null }, build)

@JvmName("onMessageEventGroupCreatedTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onMessageEventGroupCreated(build: HandlerRoute<MessageEvent>.() -> Unit) =
    on<MessageEvent> { onMessageEventGroupCreated(build) }

// --- whenMessageEventGroupCreated (terminal) ---

@JvmName("whenMessageEventGroupCreatedMessageEvent")
fun HandlerRoute<MessageEvent>.whenMessageEventGroupCreated(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.groupChatCreated == true) it else null }) { handle(handler) }

@JvmName("whenMessageEventGroupCreatedTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenMessageEventGroupCreated(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    on<MessageEvent> { whenMessageEventGroupCreated(handler) }

// ==================== supergroupCreated ====================

// --- onMessageEventSupergroupCreated (stackable) ---

@JvmName("onMessageEventSupergroupCreatedMessageEvent")
fun HandlerRoute<MessageEvent>.onMessageEventSupergroupCreated(build: HandlerRoute<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.supergroupChatCreated == true) it else null }, build)

@JvmName("onMessageEventSupergroupCreatedTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onMessageEventSupergroupCreated(build: HandlerRoute<MessageEvent>.() -> Unit) =
    on<MessageEvent> { onMessageEventSupergroupCreated(build) }

// --- whenMessageEventSupergroupCreated (terminal) ---

@JvmName("whenMessageEventSupergroupCreatedMessageEvent")
fun HandlerRoute<MessageEvent>.whenMessageEventSupergroupCreated(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.supergroupChatCreated == true) it else null }) { handle(handler) }

@JvmName("whenMessageEventSupergroupCreatedTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenMessageEventSupergroupCreated(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    on<MessageEvent> { whenMessageEventSupergroupCreated(handler) }

// ==================== channelCreated ====================

// --- onMessageEventChannelCreated (stackable) ---

@JvmName("onMessageEventChannelCreatedMessageEvent")
fun HandlerRoute<MessageEvent>.onMessageEventChannelCreated(build: HandlerRoute<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.channelChatCreated == true) it else null }, build)

@JvmName("onMessageEventChannelCreatedTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onMessageEventChannelCreated(build: HandlerRoute<MessageEvent>.() -> Unit) =
    on<MessageEvent> { onMessageEventChannelCreated(build) }

// --- whenMessageEventChannelCreated (terminal) ---

@JvmName("whenMessageEventChannelCreatedMessageEvent")
fun HandlerRoute<MessageEvent>.whenMessageEventChannelCreated(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.channelChatCreated == true) it else null }) { handle(handler) }

@JvmName("whenMessageEventChannelCreatedTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenMessageEventChannelCreated(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    on<MessageEvent> { whenMessageEventChannelCreated(handler) }

// ==================== videoChatStarted ====================

// --- onMessageEventVideoChatStarted (stackable) ---

@JvmName("onMessageEventVideoChatStartedMessageEvent")
fun HandlerRoute<MessageEvent>.onMessageEventVideoChatStarted(build: HandlerRoute<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.videoChatStarted != null) it else null }, build)

@JvmName("onMessageEventVideoChatStartedTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onMessageEventVideoChatStarted(build: HandlerRoute<MessageEvent>.() -> Unit) =
    on<MessageEvent> { onMessageEventVideoChatStarted(build) }

// --- whenMessageEventVideoChatStarted (terminal) ---

@JvmName("whenMessageEventVideoChatStartedMessageEvent")
fun HandlerRoute<MessageEvent>.whenMessageEventVideoChatStarted(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.videoChatStarted != null) it else null }) { handle(handler) }

@JvmName("whenMessageEventVideoChatStartedTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenMessageEventVideoChatStarted(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    on<MessageEvent> { whenMessageEventVideoChatStarted(handler) }

// ==================== videoChatEnded ====================

// --- onMessageEventVideoChatEnded (stackable) ---

@JvmName("onMessageEventVideoChatEndedMessageEvent")
fun HandlerRoute<MessageEvent>.onMessageEventVideoChatEnded(build: HandlerRoute<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.videoChatEnded != null) it else null }, build)

@JvmName("onMessageEventVideoChatEndedTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onMessageEventVideoChatEnded(build: HandlerRoute<MessageEvent>.() -> Unit) =
    on<MessageEvent> { onMessageEventVideoChatEnded(build) }

// --- whenMessageEventVideoChatEnded (terminal) ---

@JvmName("whenMessageEventVideoChatEndedMessageEvent")
fun HandlerRoute<MessageEvent>.whenMessageEventVideoChatEnded(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.videoChatEnded != null) it else null }) { handle(handler) }

@JvmName("whenMessageEventVideoChatEndedTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenMessageEventVideoChatEnded(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    on<MessageEvent> { whenMessageEventVideoChatEnded(handler) }

// ==================== videoChatScheduled ====================

// --- onMessageEventVideoChatScheduled (stackable) ---

@JvmName("onMessageEventVideoChatScheduledMessageEvent")
fun HandlerRoute<MessageEvent>.onMessageEventVideoChatScheduled(build: HandlerRoute<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.videoChatScheduled != null) it else null }, build)

@JvmName("onMessageEventVideoChatScheduledTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onMessageEventVideoChatScheduled(build: HandlerRoute<MessageEvent>.() -> Unit) =
    on<MessageEvent> { onMessageEventVideoChatScheduled(build) }

// --- whenMessageEventVideoChatScheduled (terminal) ---

@JvmName("whenMessageEventVideoChatScheduledMessageEvent")
fun HandlerRoute<MessageEvent>.whenMessageEventVideoChatScheduled(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.videoChatScheduled != null) it else null }) { handle(handler) }

@JvmName("whenMessageEventVideoChatScheduledTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenMessageEventVideoChatScheduled(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    on<MessageEvent> { whenMessageEventVideoChatScheduled(handler) }

// ==================== videoChatParticipantsInvited ====================

// --- onMessageEventVideoChatParticipantsInvited (stackable) ---

@JvmName("onMessageEventVideoChatParticipantsInvitedMessageEvent")
fun HandlerRoute<MessageEvent>.onMessageEventVideoChatParticipantsInvited(build: HandlerRoute<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.videoChatParticipantsInvited != null) it else null }, build)

@JvmName("onMessageEventVideoChatParticipantsInvitedTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onMessageEventVideoChatParticipantsInvited(build: HandlerRoute<MessageEvent>.() -> Unit) =
    on<MessageEvent> { onMessageEventVideoChatParticipantsInvited(build) }

// --- whenMessageEventVideoChatParticipantsInvited (terminal) ---

@JvmName("whenMessageEventVideoChatParticipantsInvitedMessageEvent")
fun HandlerRoute<MessageEvent>.whenMessageEventVideoChatParticipantsInvited(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.videoChatParticipantsInvited != null) it else null }) { handle(handler) }

@JvmName("whenMessageEventVideoChatParticipantsInvitedTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenMessageEventVideoChatParticipantsInvited(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    on<MessageEvent> { whenMessageEventVideoChatParticipantsInvited(handler) }

// ==================== forumTopicCreated ====================

// --- onMessageEventForumTopicCreated (stackable) ---

@JvmName("onMessageEventForumTopicCreatedMessageEvent")
fun HandlerRoute<MessageEvent>.onMessageEventForumTopicCreated(build: HandlerRoute<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.forumTopicCreated != null) it else null }, build)

@JvmName("onMessageEventForumTopicCreatedTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onMessageEventForumTopicCreated(build: HandlerRoute<MessageEvent>.() -> Unit) =
    on<MessageEvent> { onMessageEventForumTopicCreated(build) }

// --- whenMessageEventForumTopicCreated (terminal) ---

@JvmName("whenMessageEventForumTopicCreatedMessageEvent")
fun HandlerRoute<MessageEvent>.whenMessageEventForumTopicCreated(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.forumTopicCreated != null) it else null }) { handle(handler) }

@JvmName("whenMessageEventForumTopicCreatedTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenMessageEventForumTopicCreated(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    on<MessageEvent> { whenMessageEventForumTopicCreated(handler) }

// ==================== forumTopicEdited ====================

// --- onMessageEventForumTopicEdited (stackable) ---

@JvmName("onMessageEventForumTopicEditedMessageEvent")
fun HandlerRoute<MessageEvent>.onMessageEventForumTopicEdited(build: HandlerRoute<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.forumTopicEdited != null) it else null }, build)

@JvmName("onMessageEventForumTopicEditedTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onMessageEventForumTopicEdited(build: HandlerRoute<MessageEvent>.() -> Unit) =
    on<MessageEvent> { onMessageEventForumTopicEdited(build) }

// --- whenMessageEventForumTopicEdited (terminal) ---

@JvmName("whenMessageEventForumTopicEditedMessageEvent")
fun HandlerRoute<MessageEvent>.whenMessageEventForumTopicEdited(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.forumTopicEdited != null) it else null }) { handle(handler) }

@JvmName("whenMessageEventForumTopicEditedTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenMessageEventForumTopicEdited(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    on<MessageEvent> { whenMessageEventForumTopicEdited(handler) }

// ==================== forumTopicClosed ====================

// --- onMessageEventForumTopicClosed (stackable) ---

@JvmName("onMessageEventForumTopicClosedMessageEvent")
fun HandlerRoute<MessageEvent>.onMessageEventForumTopicClosed(build: HandlerRoute<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.forumTopicClosed != null) it else null }, build)

@JvmName("onMessageEventForumTopicClosedTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onMessageEventForumTopicClosed(build: HandlerRoute<MessageEvent>.() -> Unit) =
    on<MessageEvent> { onMessageEventForumTopicClosed(build) }

// --- whenMessageEventForumTopicClosed (terminal) ---

@JvmName("whenMessageEventForumTopicClosedMessageEvent")
fun HandlerRoute<MessageEvent>.whenMessageEventForumTopicClosed(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.forumTopicClosed != null) it else null }) { handle(handler) }

@JvmName("whenMessageEventForumTopicClosedTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenMessageEventForumTopicClosed(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    on<MessageEvent> { whenMessageEventForumTopicClosed(handler) }

// ==================== forumTopicReopened ====================

// --- onMessageEventForumTopicReopened (stackable) ---

@JvmName("onMessageEventForumTopicReopenedMessageEvent")
fun HandlerRoute<MessageEvent>.onMessageEventForumTopicReopened(build: HandlerRoute<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.forumTopicReopened != null) it else null }, build)

@JvmName("onMessageEventForumTopicReopenedTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onMessageEventForumTopicReopened(build: HandlerRoute<MessageEvent>.() -> Unit) =
    on<MessageEvent> { onMessageEventForumTopicReopened(build) }

// --- whenMessageEventForumTopicReopened (terminal) ---

@JvmName("whenMessageEventForumTopicReopenedMessageEvent")
fun HandlerRoute<MessageEvent>.whenMessageEventForumTopicReopened(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.forumTopicReopened != null) it else null }) { handle(handler) }

@JvmName("whenMessageEventForumTopicReopenedTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenMessageEventForumTopicReopened(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    on<MessageEvent> { whenMessageEventForumTopicReopened(handler) }

// ==================== generalForumTopicHidden ====================

// --- onMessageEventGeneralForumTopicHidden (stackable) ---

@JvmName("onMessageEventGeneralForumTopicHiddenMessageEvent")
fun HandlerRoute<MessageEvent>.onMessageEventGeneralForumTopicHidden(build: HandlerRoute<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.generalForumTopicHidden != null) it else null }, build)

@JvmName("onMessageEventGeneralForumTopicHiddenTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onMessageEventGeneralForumTopicHidden(build: HandlerRoute<MessageEvent>.() -> Unit) =
    on<MessageEvent> { onMessageEventGeneralForumTopicHidden(build) }

// --- whenMessageEventGeneralForumTopicHidden (terminal) ---

@JvmName("whenMessageEventGeneralForumTopicHiddenMessageEvent")
fun HandlerRoute<MessageEvent>.whenMessageEventGeneralForumTopicHidden(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.generalForumTopicHidden != null) it else null }) { handle(handler) }

@JvmName("whenMessageEventGeneralForumTopicHiddenTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenMessageEventGeneralForumTopicHidden(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    on<MessageEvent> { whenMessageEventGeneralForumTopicHidden(handler) }

// ==================== generalForumTopicUnhidden ====================

// --- onMessageEventGeneralForumTopicUnhidden (stackable) ---

@JvmName("onMessageEventGeneralForumTopicUnhiddenMessageEvent")
fun HandlerRoute<MessageEvent>.onMessageEventGeneralForumTopicUnhidden(build: HandlerRoute<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.generalForumTopicUnhidden != null) it else null }, build)

@JvmName("onMessageEventGeneralForumTopicUnhiddenTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onMessageEventGeneralForumTopicUnhidden(build: HandlerRoute<MessageEvent>.() -> Unit) =
    on<MessageEvent> { onMessageEventGeneralForumTopicUnhidden(build) }

// --- whenMessageEventGeneralForumTopicUnhidden (terminal) ---

@JvmName("whenMessageEventGeneralForumTopicUnhiddenMessageEvent")
fun HandlerRoute<MessageEvent>.whenMessageEventGeneralForumTopicUnhidden(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.generalForumTopicUnhidden != null) it else null }) { handle(handler) }

@JvmName("whenMessageEventGeneralForumTopicUnhiddenTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenMessageEventGeneralForumTopicUnhidden(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    on<MessageEvent> { whenMessageEventGeneralForumTopicUnhidden(handler) }

// ==================== giveawayCreated ====================

// --- onMessageEventGiveawayCreated (stackable) ---

@JvmName("onMessageEventGiveawayCreatedMessageEvent")
fun HandlerRoute<MessageEvent>.onMessageEventGiveawayCreated(build: HandlerRoute<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.giveawayCreated != null) it else null }, build)

@JvmName("onMessageEventGiveawayCreatedTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onMessageEventGiveawayCreated(build: HandlerRoute<MessageEvent>.() -> Unit) =
    on<MessageEvent> { onMessageEventGiveawayCreated(build) }

// --- whenMessageEventGiveawayCreated (terminal) ---

@JvmName("whenMessageEventGiveawayCreatedMessageEvent")
fun HandlerRoute<MessageEvent>.whenMessageEventGiveawayCreated(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.giveawayCreated != null) it else null }) { handle(handler) }

@JvmName("whenMessageEventGiveawayCreatedTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenMessageEventGiveawayCreated(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    on<MessageEvent> { whenMessageEventGiveawayCreated(handler) }

// ==================== giveawayCompleted ====================

// --- onMessageEventGiveawayCompleted (stackable) ---

@JvmName("onMessageEventGiveawayCompletedMessageEvent")
fun HandlerRoute<MessageEvent>.onMessageEventGiveawayCompleted(build: HandlerRoute<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.giveawayCompleted != null) it else null }, build)

@JvmName("onMessageEventGiveawayCompletedTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onMessageEventGiveawayCompleted(build: HandlerRoute<MessageEvent>.() -> Unit) =
    on<MessageEvent> { onMessageEventGiveawayCompleted(build) }

// --- whenMessageEventGiveawayCompleted (terminal) ---

@JvmName("whenMessageEventGiveawayCompletedMessageEvent")
fun HandlerRoute<MessageEvent>.whenMessageEventGiveawayCompleted(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.giveawayCompleted != null) it else null }) { handle(handler) }

@JvmName("whenMessageEventGiveawayCompletedTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenMessageEventGiveawayCompleted(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    on<MessageEvent> { whenMessageEventGiveawayCompleted(handler) }

// ==================== boostAdded ====================

// --- onMessageEventBoostAdded (stackable) ---

@JvmName("onMessageEventBoostAddedMessageEvent")
fun HandlerRoute<MessageEvent>.onMessageEventBoostAdded(build: HandlerRoute<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.boostAdded != null) it else null }, build)

@JvmName("onMessageEventBoostAddedTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onMessageEventBoostAdded(build: HandlerRoute<MessageEvent>.() -> Unit) =
    on<MessageEvent> { onMessageEventBoostAdded(build) }

// --- whenMessageEventBoostAdded (terminal) ---

@JvmName("whenMessageEventBoostAddedMessageEvent")
fun HandlerRoute<MessageEvent>.whenMessageEventBoostAdded(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.boostAdded != null) it else null }) { handle(handler) }

@JvmName("whenMessageEventBoostAddedTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenMessageEventBoostAdded(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    on<MessageEvent> { whenMessageEventBoostAdded(handler) }

// ==================== migrateToSupergroup ====================

// --- onMessageEventMigrateToSupergroup (stackable) ---

@JvmName("onMessageEventMigrateToSupergroupMessageEvent")
fun HandlerRoute<MessageEvent>.onMessageEventMigrateToSupergroup(build: HandlerRoute<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.migrateToChatId != null) it else null }, build)

@JvmName("onMessageEventMigrateToSupergroupTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onMessageEventMigrateToSupergroup(build: HandlerRoute<MessageEvent>.() -> Unit) =
    on<MessageEvent> { onMessageEventMigrateToSupergroup(build) }

// --- whenMessageEventMigrateToSupergroup (terminal) ---

@JvmName("whenMessageEventMigrateToSupergroupMessageEvent")
fun HandlerRoute<MessageEvent>.whenMessageEventMigrateToSupergroup(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.migrateToChatId != null) it else null }) { handle(handler) }

@JvmName("whenMessageEventMigrateToSupergroupTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenMessageEventMigrateToSupergroup(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    on<MessageEvent> { whenMessageEventMigrateToSupergroup(handler) }

// ==================== migrateFromGroup ====================

// --- onMessageEventMigrateFromGroup (stackable) ---

@JvmName("onMessageEventMigrateFromGroupMessageEvent")
fun HandlerRoute<MessageEvent>.onMessageEventMigrateFromGroup(build: HandlerRoute<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.migrateFromChatId != null) it else null }, build)

@JvmName("onMessageEventMigrateFromGroupTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onMessageEventMigrateFromGroup(build: HandlerRoute<MessageEvent>.() -> Unit) =
    on<MessageEvent> { onMessageEventMigrateFromGroup(build) }

// --- whenMessageEventMigrateFromGroup (terminal) ---

@JvmName("whenMessageEventMigrateFromGroupMessageEvent")
fun HandlerRoute<MessageEvent>.whenMessageEventMigrateFromGroup(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.migrateFromChatId != null) it else null }) { handle(handler) }

@JvmName("whenMessageEventMigrateFromGroupTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenMessageEventMigrateFromGroup(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    on<MessageEvent> { whenMessageEventMigrateFromGroup(handler) }

// ==================== messageAutoDeleteTimerChanged ====================

// --- onMessageEventMessageAutoDeleteTimerChanged (stackable) ---

@JvmName("onMessageEventMessageAutoDeleteTimerChangedMessageEvent")
fun HandlerRoute<MessageEvent>.onMessageEventMessageAutoDeleteTimerChanged(build: HandlerRoute<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.messageAutoDeleteTimerChanged != null) it else null }, build)

@JvmName("onMessageEventMessageAutoDeleteTimerChangedTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onMessageEventMessageAutoDeleteTimerChanged(build: HandlerRoute<MessageEvent>.() -> Unit) =
    on<MessageEvent> { onMessageEventMessageAutoDeleteTimerChanged(build) }

// --- whenMessageEventMessageAutoDeleteTimerChanged (terminal) ---

@JvmName("whenMessageEventMessageAutoDeleteTimerChangedMessageEvent")
fun HandlerRoute<MessageEvent>.whenMessageEventMessageAutoDeleteTimerChanged(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.messageAutoDeleteTimerChanged != null) it else null }) { handle(handler) }

@JvmName("whenMessageEventMessageAutoDeleteTimerChangedTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenMessageEventMessageAutoDeleteTimerChanged(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    on<MessageEvent> { whenMessageEventMessageAutoDeleteTimerChanged(handler) }

// ==================== webAppData ====================

// --- onMessageEventWebAppData (stackable) ---

@JvmName("onMessageEventWebAppDataMessageEvent")
fun HandlerRoute<MessageEvent>.onMessageEventWebAppData(build: HandlerRoute<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.webAppData != null) it else null }, build)

@JvmName("onMessageEventWebAppDataTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onMessageEventWebAppData(build: HandlerRoute<MessageEvent>.() -> Unit) =
    on<MessageEvent> { onMessageEventWebAppData(build) }

// --- whenMessageEventWebAppData (terminal) ---

@JvmName("whenMessageEventWebAppDataMessageEvent")
fun HandlerRoute<MessageEvent>.whenMessageEventWebAppData(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.webAppData != null) it else null }) { handle(handler) }

@JvmName("whenMessageEventWebAppDataTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenMessageEventWebAppData(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    on<MessageEvent> { whenMessageEventWebAppData(handler) }
