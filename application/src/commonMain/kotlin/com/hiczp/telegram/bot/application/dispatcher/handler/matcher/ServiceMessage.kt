@file:Suppress("unused")

package com.hiczp.telegram.bot.application.dispatcher.handler.matcher

import com.hiczp.telegram.bot.application.dispatcher.handler.HandlerBotCall
import com.hiczp.telegram.bot.application.dispatcher.handler.HandlerRoute
import com.hiczp.telegram.bot.protocol.event.MessageEvent
import com.hiczp.telegram.bot.protocol.event.TelegramBotEvent
import kotlin.jvm.JvmName

// ==================== newChatMembers ====================

// --- onNewChatMembers (stackable) ---

@JvmName("onNewChatMembersMessageEvent")
fun HandlerRoute<MessageEvent>.onNewChatMembers(build: HandlerRoute<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.newChatMembers != null) it else null }, build)

@JvmName("onNewChatMembersTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onNewChatMembers(build: HandlerRoute<MessageEvent>.() -> Unit) =
    on<MessageEvent> { onNewChatMembers(build) }

// --- whenNewChatMembers (terminal) ---

@JvmName("whenNewChatMembersMessageEvent")
fun HandlerRoute<MessageEvent>.whenNewChatMembers(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.newChatMembers != null) it else null }) { handle(handler) }

@JvmName("whenNewChatMembersTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenNewChatMembers(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    on<MessageEvent> { whenNewChatMembers(handler) }

// ==================== leftChatMember ====================

// --- onLeftChatMember (stackable) ---

@JvmName("onLeftChatMemberMessageEvent")
fun HandlerRoute<MessageEvent>.onLeftChatMember(build: HandlerRoute<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.leftChatMember != null) it else null }, build)

@JvmName("onLeftChatMemberTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onLeftChatMember(build: HandlerRoute<MessageEvent>.() -> Unit) =
    on<MessageEvent> { onLeftChatMember(build) }

// --- whenLeftChatMember (terminal) ---

@JvmName("whenLeftChatMemberMessageEvent")
fun HandlerRoute<MessageEvent>.whenLeftChatMember(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.leftChatMember != null) it else null }) { handle(handler) }

@JvmName("whenLeftChatMemberTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenLeftChatMember(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    on<MessageEvent> { whenLeftChatMember(handler) }

// ==================== newChatTitle ====================

// --- onNewChatTitle (stackable) ---

@JvmName("onNewChatTitleMessageEvent")
fun HandlerRoute<MessageEvent>.onNewChatTitle(build: HandlerRoute<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.newChatTitle != null) it else null }, build)

@JvmName("onNewChatTitleTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onNewChatTitle(build: HandlerRoute<MessageEvent>.() -> Unit) =
    on<MessageEvent> { onNewChatTitle(build) }

// --- whenNewChatTitle (terminal) ---

@JvmName("whenNewChatTitleMessageEvent")
fun HandlerRoute<MessageEvent>.whenNewChatTitle(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.newChatTitle != null) it else null }) { handle(handler) }

@JvmName("whenNewChatTitleTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenNewChatTitle(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    on<MessageEvent> { whenNewChatTitle(handler) }

// ==================== newChatPhoto ====================

// --- onNewChatPhoto (stackable) ---

@JvmName("onNewChatPhotoMessageEvent")
fun HandlerRoute<MessageEvent>.onNewChatPhoto(build: HandlerRoute<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.newChatPhoto != null) it else null }, build)

@JvmName("onNewChatPhotoTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onNewChatPhoto(build: HandlerRoute<MessageEvent>.() -> Unit) =
    on<MessageEvent> { onNewChatPhoto(build) }

// --- whenNewChatPhoto (terminal) ---

@JvmName("whenNewChatPhotoMessageEvent")
fun HandlerRoute<MessageEvent>.whenNewChatPhoto(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.newChatPhoto != null) it else null }) { handle(handler) }

@JvmName("whenNewChatPhotoTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenNewChatPhoto(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    on<MessageEvent> { whenNewChatPhoto(handler) }

// ==================== deleteChatPhoto ====================

// --- onDeleteChatPhoto (stackable) ---

@JvmName("onDeleteChatPhotoMessageEvent")
fun HandlerRoute<MessageEvent>.onDeleteChatPhoto(build: HandlerRoute<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.deleteChatPhoto == true) it else null }, build)

@JvmName("onDeleteChatPhotoTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onDeleteChatPhoto(build: HandlerRoute<MessageEvent>.() -> Unit) =
    on<MessageEvent> { onDeleteChatPhoto(build) }

// --- whenDeleteChatPhoto (terminal) ---

@JvmName("whenDeleteChatPhotoMessageEvent")
fun HandlerRoute<MessageEvent>.whenDeleteChatPhoto(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.deleteChatPhoto == true) it else null }) { handle(handler) }

@JvmName("whenDeleteChatPhotoTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenDeleteChatPhoto(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    on<MessageEvent> { whenDeleteChatPhoto(handler) }

// ==================== pinnedMessage ====================

// --- onPinnedMessage (stackable) ---

@JvmName("onPinnedMessageMessageEvent")
fun HandlerRoute<MessageEvent>.onPinnedMessage(build: HandlerRoute<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.pinnedMessage != null) it else null }, build)

@JvmName("onPinnedMessageTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onPinnedMessage(build: HandlerRoute<MessageEvent>.() -> Unit) =
    on<MessageEvent> { onPinnedMessage(build) }

// --- whenPinnedMessage (terminal) ---

@JvmName("whenPinnedMessageMessageEvent")
fun HandlerRoute<MessageEvent>.whenPinnedMessage(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.pinnedMessage != null) it else null }) { handle(handler) }

@JvmName("whenPinnedMessageTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenPinnedMessage(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    on<MessageEvent> { whenPinnedMessage(handler) }

// ==================== groupCreated ====================

// --- onGroupCreated (stackable) ---

@JvmName("onGroupCreatedMessageEvent")
fun HandlerRoute<MessageEvent>.onGroupCreated(build: HandlerRoute<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.groupChatCreated == true) it else null }, build)

@JvmName("onGroupCreatedTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onGroupCreated(build: HandlerRoute<MessageEvent>.() -> Unit) =
    on<MessageEvent> { onGroupCreated(build) }

// --- whenGroupCreated (terminal) ---

@JvmName("whenGroupCreatedMessageEvent")
fun HandlerRoute<MessageEvent>.whenGroupCreated(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.groupChatCreated == true) it else null }) { handle(handler) }

@JvmName("whenGroupCreatedTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenGroupCreated(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    on<MessageEvent> { whenGroupCreated(handler) }

// ==================== supergroupCreated ====================

// --- onSupergroupCreated (stackable) ---

@JvmName("onSupergroupCreatedMessageEvent")
fun HandlerRoute<MessageEvent>.onSupergroupCreated(build: HandlerRoute<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.supergroupChatCreated == true) it else null }, build)

@JvmName("onSupergroupCreatedTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onSupergroupCreated(build: HandlerRoute<MessageEvent>.() -> Unit) =
    on<MessageEvent> { onSupergroupCreated(build) }

// --- whenSupergroupCreated (terminal) ---

@JvmName("whenSupergroupCreatedMessageEvent")
fun HandlerRoute<MessageEvent>.whenSupergroupCreated(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.supergroupChatCreated == true) it else null }) { handle(handler) }

@JvmName("whenSupergroupCreatedTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenSupergroupCreated(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    on<MessageEvent> { whenSupergroupCreated(handler) }

// ==================== channelCreated ====================

// --- onChannelCreated (stackable) ---

@JvmName("onChannelCreatedMessageEvent")
fun HandlerRoute<MessageEvent>.onChannelCreated(build: HandlerRoute<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.channelChatCreated == true) it else null }, build)

@JvmName("onChannelCreatedTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onChannelCreated(build: HandlerRoute<MessageEvent>.() -> Unit) =
    on<MessageEvent> { onChannelCreated(build) }

// --- whenChannelCreated (terminal) ---

@JvmName("whenChannelCreatedMessageEvent")
fun HandlerRoute<MessageEvent>.whenChannelCreated(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.channelChatCreated == true) it else null }) { handle(handler) }

@JvmName("whenChannelCreatedTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenChannelCreated(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    on<MessageEvent> { whenChannelCreated(handler) }

// ==================== videoChatStarted ====================

// --- onVideoChatStarted (stackable) ---

@JvmName("onVideoChatStartedMessageEvent")
fun HandlerRoute<MessageEvent>.onVideoChatStarted(build: HandlerRoute<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.videoChatStarted != null) it else null }, build)

@JvmName("onVideoChatStartedTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onVideoChatStarted(build: HandlerRoute<MessageEvent>.() -> Unit) =
    on<MessageEvent> { onVideoChatStarted(build) }

// --- whenVideoChatStarted (terminal) ---

@JvmName("whenVideoChatStartedMessageEvent")
fun HandlerRoute<MessageEvent>.whenVideoChatStarted(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.videoChatStarted != null) it else null }) { handle(handler) }

@JvmName("whenVideoChatStartedTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenVideoChatStarted(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    on<MessageEvent> { whenVideoChatStarted(handler) }

// ==================== videoChatEnded ====================

// --- onVideoChatEnded (stackable) ---

@JvmName("onVideoChatEndedMessageEvent")
fun HandlerRoute<MessageEvent>.onVideoChatEnded(build: HandlerRoute<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.videoChatEnded != null) it else null }, build)

@JvmName("onVideoChatEndedTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onVideoChatEnded(build: HandlerRoute<MessageEvent>.() -> Unit) =
    on<MessageEvent> { onVideoChatEnded(build) }

// --- whenVideoChatEnded (terminal) ---

@JvmName("whenVideoChatEndedMessageEvent")
fun HandlerRoute<MessageEvent>.whenVideoChatEnded(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.videoChatEnded != null) it else null }) { handle(handler) }

@JvmName("whenVideoChatEndedTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenVideoChatEnded(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    on<MessageEvent> { whenVideoChatEnded(handler) }

// ==================== videoChatScheduled ====================

// --- onVideoChatScheduled (stackable) ---

@JvmName("onVideoChatScheduledMessageEvent")
fun HandlerRoute<MessageEvent>.onVideoChatScheduled(build: HandlerRoute<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.videoChatScheduled != null) it else null }, build)

@JvmName("onVideoChatScheduledTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onVideoChatScheduled(build: HandlerRoute<MessageEvent>.() -> Unit) =
    on<MessageEvent> { onVideoChatScheduled(build) }

// --- whenVideoChatScheduled (terminal) ---

@JvmName("whenVideoChatScheduledMessageEvent")
fun HandlerRoute<MessageEvent>.whenVideoChatScheduled(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.videoChatScheduled != null) it else null }) { handle(handler) }

@JvmName("whenVideoChatScheduledTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenVideoChatScheduled(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    on<MessageEvent> { whenVideoChatScheduled(handler) }

// ==================== videoChatParticipantsInvited ====================

// --- onVideoChatParticipantsInvited (stackable) ---

@JvmName("onVideoChatParticipantsInvitedMessageEvent")
fun HandlerRoute<MessageEvent>.onVideoChatParticipantsInvited(build: HandlerRoute<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.videoChatParticipantsInvited != null) it else null }, build)

@JvmName("onVideoChatParticipantsInvitedTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onVideoChatParticipantsInvited(build: HandlerRoute<MessageEvent>.() -> Unit) =
    on<MessageEvent> { onVideoChatParticipantsInvited(build) }

// --- whenVideoChatParticipantsInvited (terminal) ---

@JvmName("whenVideoChatParticipantsInvitedMessageEvent")
fun HandlerRoute<MessageEvent>.whenVideoChatParticipantsInvited(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.videoChatParticipantsInvited != null) it else null }) { handle(handler) }

@JvmName("whenVideoChatParticipantsInvitedTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenVideoChatParticipantsInvited(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    on<MessageEvent> { whenVideoChatParticipantsInvited(handler) }

// ==================== forumTopicCreated ====================

// --- onForumTopicCreated (stackable) ---

@JvmName("onForumTopicCreatedMessageEvent")
fun HandlerRoute<MessageEvent>.onForumTopicCreated(build: HandlerRoute<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.forumTopicCreated != null) it else null }, build)

@JvmName("onForumTopicCreatedTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onForumTopicCreated(build: HandlerRoute<MessageEvent>.() -> Unit) =
    on<MessageEvent> { onForumTopicCreated(build) }

// --- whenForumTopicCreated (terminal) ---

@JvmName("whenForumTopicCreatedMessageEvent")
fun HandlerRoute<MessageEvent>.whenForumTopicCreated(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.forumTopicCreated != null) it else null }) { handle(handler) }

@JvmName("whenForumTopicCreatedTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenForumTopicCreated(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    on<MessageEvent> { whenForumTopicCreated(handler) }

// ==================== forumTopicEdited ====================

// --- onForumTopicEdited (stackable) ---

@JvmName("onForumTopicEditedMessageEvent")
fun HandlerRoute<MessageEvent>.onForumTopicEdited(build: HandlerRoute<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.forumTopicEdited != null) it else null }, build)

@JvmName("onForumTopicEditedTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onForumTopicEdited(build: HandlerRoute<MessageEvent>.() -> Unit) =
    on<MessageEvent> { onForumTopicEdited(build) }

// --- whenForumTopicEdited (terminal) ---

@JvmName("whenForumTopicEditedMessageEvent")
fun HandlerRoute<MessageEvent>.whenForumTopicEdited(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.forumTopicEdited != null) it else null }) { handle(handler) }

@JvmName("whenForumTopicEditedTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenForumTopicEdited(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    on<MessageEvent> { whenForumTopicEdited(handler) }

// ==================== forumTopicClosed ====================

// --- onForumTopicClosed (stackable) ---

@JvmName("onForumTopicClosedMessageEvent")
fun HandlerRoute<MessageEvent>.onForumTopicClosed(build: HandlerRoute<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.forumTopicClosed != null) it else null }, build)

@JvmName("onForumTopicClosedTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onForumTopicClosed(build: HandlerRoute<MessageEvent>.() -> Unit) =
    on<MessageEvent> { onForumTopicClosed(build) }

// --- whenForumTopicClosed (terminal) ---

@JvmName("whenForumTopicClosedMessageEvent")
fun HandlerRoute<MessageEvent>.whenForumTopicClosed(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.forumTopicClosed != null) it else null }) { handle(handler) }

@JvmName("whenForumTopicClosedTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenForumTopicClosed(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    on<MessageEvent> { whenForumTopicClosed(handler) }

// ==================== forumTopicReopened ====================

// --- onForumTopicReopened (stackable) ---

@JvmName("onForumTopicReopenedMessageEvent")
fun HandlerRoute<MessageEvent>.onForumTopicReopened(build: HandlerRoute<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.forumTopicReopened != null) it else null }, build)

@JvmName("onForumTopicReopenedTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onForumTopicReopened(build: HandlerRoute<MessageEvent>.() -> Unit) =
    on<MessageEvent> { onForumTopicReopened(build) }

// --- whenForumTopicReopened (terminal) ---

@JvmName("whenForumTopicReopenedMessageEvent")
fun HandlerRoute<MessageEvent>.whenForumTopicReopened(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.forumTopicReopened != null) it else null }) { handle(handler) }

@JvmName("whenForumTopicReopenedTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenForumTopicReopened(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    on<MessageEvent> { whenForumTopicReopened(handler) }

// ==================== generalForumTopicHidden ====================

// --- onGeneralForumTopicHidden (stackable) ---

@JvmName("onGeneralForumTopicHiddenMessageEvent")
fun HandlerRoute<MessageEvent>.onGeneralForumTopicHidden(build: HandlerRoute<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.generalForumTopicHidden != null) it else null }, build)

@JvmName("onGeneralForumTopicHiddenTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onGeneralForumTopicHidden(build: HandlerRoute<MessageEvent>.() -> Unit) =
    on<MessageEvent> { onGeneralForumTopicHidden(build) }

// --- whenGeneralForumTopicHidden (terminal) ---

@JvmName("whenGeneralForumTopicHiddenMessageEvent")
fun HandlerRoute<MessageEvent>.whenGeneralForumTopicHidden(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.generalForumTopicHidden != null) it else null }) { handle(handler) }

@JvmName("whenGeneralForumTopicHiddenTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenGeneralForumTopicHidden(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    on<MessageEvent> { whenGeneralForumTopicHidden(handler) }

// ==================== generalForumTopicUnhidden ====================

// --- onGeneralForumTopicUnhidden (stackable) ---

@JvmName("onGeneralForumTopicUnhiddenMessageEvent")
fun HandlerRoute<MessageEvent>.onGeneralForumTopicUnhidden(build: HandlerRoute<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.generalForumTopicUnhidden != null) it else null }, build)

@JvmName("onGeneralForumTopicUnhiddenTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onGeneralForumTopicUnhidden(build: HandlerRoute<MessageEvent>.() -> Unit) =
    on<MessageEvent> { onGeneralForumTopicUnhidden(build) }

// --- whenGeneralForumTopicUnhidden (terminal) ---

@JvmName("whenGeneralForumTopicUnhiddenMessageEvent")
fun HandlerRoute<MessageEvent>.whenGeneralForumTopicUnhidden(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.generalForumTopicUnhidden != null) it else null }) { handle(handler) }

@JvmName("whenGeneralForumTopicUnhiddenTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenGeneralForumTopicUnhidden(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    on<MessageEvent> { whenGeneralForumTopicUnhidden(handler) }

// ==================== giveawayCreated ====================

// --- onGiveawayCreated (stackable) ---

@JvmName("onGiveawayCreatedMessageEvent")
fun HandlerRoute<MessageEvent>.onGiveawayCreated(build: HandlerRoute<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.giveawayCreated != null) it else null }, build)

@JvmName("onGiveawayCreatedTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onGiveawayCreated(build: HandlerRoute<MessageEvent>.() -> Unit) =
    on<MessageEvent> { onGiveawayCreated(build) }

// --- whenGiveawayCreated (terminal) ---

@JvmName("whenGiveawayCreatedMessageEvent")
fun HandlerRoute<MessageEvent>.whenGiveawayCreated(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.giveawayCreated != null) it else null }) { handle(handler) }

@JvmName("whenGiveawayCreatedTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenGiveawayCreated(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    on<MessageEvent> { whenGiveawayCreated(handler) }

// ==================== giveawayCompleted ====================

// --- onGiveawayCompleted (stackable) ---

@JvmName("onGiveawayCompletedMessageEvent")
fun HandlerRoute<MessageEvent>.onGiveawayCompleted(build: HandlerRoute<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.giveawayCompleted != null) it else null }, build)

@JvmName("onGiveawayCompletedTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onGiveawayCompleted(build: HandlerRoute<MessageEvent>.() -> Unit) =
    on<MessageEvent> { onGiveawayCompleted(build) }

// --- whenGiveawayCompleted (terminal) ---

@JvmName("whenGiveawayCompletedMessageEvent")
fun HandlerRoute<MessageEvent>.whenGiveawayCompleted(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.giveawayCompleted != null) it else null }) { handle(handler) }

@JvmName("whenGiveawayCompletedTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenGiveawayCompleted(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    on<MessageEvent> { whenGiveawayCompleted(handler) }

// ==================== boostAdded ====================

// --- onBoostAdded (stackable) ---

@JvmName("onBoostAddedMessageEvent")
fun HandlerRoute<MessageEvent>.onBoostAdded(build: HandlerRoute<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.boostAdded != null) it else null }, build)

@JvmName("onBoostAddedTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onBoostAdded(build: HandlerRoute<MessageEvent>.() -> Unit) =
    on<MessageEvent> { onBoostAdded(build) }

// --- whenBoostAdded (terminal) ---

@JvmName("whenBoostAddedMessageEvent")
fun HandlerRoute<MessageEvent>.whenBoostAdded(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.boostAdded != null) it else null }) { handle(handler) }

@JvmName("whenBoostAddedTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenBoostAdded(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    on<MessageEvent> { whenBoostAdded(handler) }

// ==================== migrateToSupergroup ====================

// --- onMigrateToSupergroup (stackable) ---

@JvmName("onMigrateToSupergroupMessageEvent")
fun HandlerRoute<MessageEvent>.onMigrateToSupergroup(build: HandlerRoute<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.migrateToChatId != null) it else null }, build)

@JvmName("onMigrateToSupergroupTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onMigrateToSupergroup(build: HandlerRoute<MessageEvent>.() -> Unit) =
    on<MessageEvent> { onMigrateToSupergroup(build) }

// --- whenMigrateToSupergroup (terminal) ---

@JvmName("whenMigrateToSupergroupMessageEvent")
fun HandlerRoute<MessageEvent>.whenMigrateToSupergroup(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.migrateToChatId != null) it else null }) { handle(handler) }

@JvmName("whenMigrateToSupergroupTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenMigrateToSupergroup(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    on<MessageEvent> { whenMigrateToSupergroup(handler) }

// ==================== migrateFromGroup ====================

// --- onMigrateFromGroup (stackable) ---

@JvmName("onMigrateFromGroupMessageEvent")
fun HandlerRoute<MessageEvent>.onMigrateFromGroup(build: HandlerRoute<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.migrateFromChatId != null) it else null }, build)

@JvmName("onMigrateFromGroupTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onMigrateFromGroup(build: HandlerRoute<MessageEvent>.() -> Unit) =
    on<MessageEvent> { onMigrateFromGroup(build) }

// --- whenMigrateFromGroup (terminal) ---

@JvmName("whenMigrateFromGroupMessageEvent")
fun HandlerRoute<MessageEvent>.whenMigrateFromGroup(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.migrateFromChatId != null) it else null }) { handle(handler) }

@JvmName("whenMigrateFromGroupTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenMigrateFromGroup(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    on<MessageEvent> { whenMigrateFromGroup(handler) }

// ==================== messageAutoDeleteTimerChanged ====================

// --- onMessageAutoDeleteTimerChanged (stackable) ---

@JvmName("onMessageAutoDeleteTimerChangedMessageEvent")
fun HandlerRoute<MessageEvent>.onMessageAutoDeleteTimerChanged(build: HandlerRoute<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.messageAutoDeleteTimerChanged != null) it else null }, build)

@JvmName("onMessageAutoDeleteTimerChangedTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onMessageAutoDeleteTimerChanged(build: HandlerRoute<MessageEvent>.() -> Unit) =
    on<MessageEvent> { onMessageAutoDeleteTimerChanged(build) }

// --- whenMessageAutoDeleteTimerChanged (terminal) ---

@JvmName("whenMessageAutoDeleteTimerChangedMessageEvent")
fun HandlerRoute<MessageEvent>.whenMessageAutoDeleteTimerChanged(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.messageAutoDeleteTimerChanged != null) it else null }) { handle(handler) }

@JvmName("whenMessageAutoDeleteTimerChangedTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenMessageAutoDeleteTimerChanged(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    on<MessageEvent> { whenMessageAutoDeleteTimerChanged(handler) }

// ==================== webAppData ====================

// --- onWebAppData (stackable) ---

@JvmName("onWebAppDataMessageEvent")
fun HandlerRoute<MessageEvent>.onWebAppData(build: HandlerRoute<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.webAppData != null) it else null }, build)

@JvmName("onWebAppDataTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onWebAppData(build: HandlerRoute<MessageEvent>.() -> Unit) =
    on<MessageEvent> { onWebAppData(build) }

// --- whenWebAppData (terminal) ---

@JvmName("whenWebAppDataMessageEvent")
fun HandlerRoute<MessageEvent>.whenWebAppData(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.webAppData != null) it else null }) { handle(handler) }

@JvmName("whenWebAppDataTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenWebAppData(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    on<MessageEvent> { whenWebAppData(handler) }
