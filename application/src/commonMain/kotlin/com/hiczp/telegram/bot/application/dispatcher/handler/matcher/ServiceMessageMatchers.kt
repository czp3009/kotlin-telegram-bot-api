package com.hiczp.telegram.bot.application.dispatcher.handler.matcher

import com.hiczp.telegram.bot.application.context.TelegramBotEventContext
import com.hiczp.telegram.bot.application.dispatcher.handler.EventRoute
import com.hiczp.telegram.bot.protocol.event.MessageEvent
import com.hiczp.telegram.bot.protocol.event.TelegramBotEvent
import kotlinx.coroutines.CoroutineScope
import kotlin.jvm.JvmName

@JvmName("newChatMembersMessageEvent")
fun EventRoute<MessageEvent>.newChatMembers(
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> = select({ if (it.event.message.newChatMembers != null) it else null }, build)

@JvmName("newChatMembersTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.newChatMembers(
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> = on<MessageEvent> { newChatMembers(build) }

@JvmName("whenNewChatMembersMessageEvent")
fun EventRoute<MessageEvent>.whenNewChatMembers(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = newChatMembers { handle(handler) }

@JvmName("whenNewChatMembersTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.whenNewChatMembers(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { whenNewChatMembers(handler) }

@JvmName("leftChatMemberMessageEvent")
fun EventRoute<MessageEvent>.leftChatMember(
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> = select({ if (it.event.message.leftChatMember != null) it else null }, build)

@JvmName("leftChatMemberTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.leftChatMember(
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> = on<MessageEvent> { leftChatMember(build) }

@JvmName("whenLeftChatMemberMessageEvent")
fun EventRoute<MessageEvent>.whenLeftChatMember(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = leftChatMember { handle(handler) }

@JvmName("whenLeftChatMemberTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.whenLeftChatMember(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { whenLeftChatMember(handler) }

@JvmName("newChatTitleMessageEvent")
fun EventRoute<MessageEvent>.newChatTitle(
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> = select({ if (it.event.message.newChatTitle != null) it else null }, build)

@JvmName("newChatTitleTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.newChatTitle(
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> = on<MessageEvent> { newChatTitle(build) }

@JvmName("whenNewChatTitleMessageEvent")
fun EventRoute<MessageEvent>.whenNewChatTitle(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = newChatTitle { handle(handler) }

@JvmName("whenNewChatTitleTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.whenNewChatTitle(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { whenNewChatTitle(handler) }

@JvmName("newChatPhotoMessageEvent")
fun EventRoute<MessageEvent>.newChatPhoto(
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> = select({ if (it.event.message.newChatPhoto != null) it else null }, build)

@JvmName("newChatPhotoTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.newChatPhoto(
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> = on<MessageEvent> { newChatPhoto(build) }

@JvmName("whenNewChatPhotoMessageEvent")
fun EventRoute<MessageEvent>.whenNewChatPhoto(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = newChatPhoto { handle(handler) }

@JvmName("whenNewChatPhotoTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.whenNewChatPhoto(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { whenNewChatPhoto(handler) }

@JvmName("deleteChatPhotoMessageEvent")
fun EventRoute<MessageEvent>.deleteChatPhoto(
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> = select({ if (it.event.message.deleteChatPhoto == true) it else null }, build)

@JvmName("deleteChatPhotoTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.deleteChatPhoto(
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> = on<MessageEvent> { deleteChatPhoto(build) }

@JvmName("whenDeleteChatPhotoMessageEvent")
fun EventRoute<MessageEvent>.whenDeleteChatPhoto(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = deleteChatPhoto { handle(handler) }

@JvmName("whenDeleteChatPhotoTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.whenDeleteChatPhoto(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { whenDeleteChatPhoto(handler) }

@JvmName("pinnedMessageMessageEvent")
fun EventRoute<MessageEvent>.pinnedMessage(
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> = select({ if (it.event.message.pinnedMessage != null) it else null }, build)

@JvmName("pinnedMessageTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.pinnedMessage(
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> = on<MessageEvent> { pinnedMessage(build) }

@JvmName("whenPinnedMessageMessageEvent")
fun EventRoute<MessageEvent>.whenPinnedMessage(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = pinnedMessage { handle(handler) }

@JvmName("whenPinnedMessageTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.whenPinnedMessage(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { whenPinnedMessage(handler) }

@JvmName("groupCreatedMessageEvent")
fun EventRoute<MessageEvent>.groupCreated(
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> = select({ if (it.event.message.groupChatCreated == true) it else null }, build)

@JvmName("groupCreatedTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.groupCreated(
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> = on<MessageEvent> { groupCreated(build) }

@JvmName("whenGroupCreatedMessageEvent")
fun EventRoute<MessageEvent>.whenGroupCreated(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = groupCreated { handle(handler) }

@JvmName("whenGroupCreatedTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.whenGroupCreated(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { whenGroupCreated(handler) }

@JvmName("supergroupCreatedMessageEvent")
fun EventRoute<MessageEvent>.supergroupCreated(
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> = select({ if (it.event.message.supergroupChatCreated == true) it else null }, build)

@JvmName("supergroupCreatedTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.supergroupCreated(
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> = on<MessageEvent> { supergroupCreated(build) }

@JvmName("whenSupergroupCreatedMessageEvent")
fun EventRoute<MessageEvent>.whenSupergroupCreated(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = supergroupCreated { handle(handler) }

@JvmName("whenSupergroupCreatedTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.whenSupergroupCreated(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { whenSupergroupCreated(handler) }

@JvmName("channelCreatedMessageEvent")
fun EventRoute<MessageEvent>.channelCreated(
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> = select({ if (it.event.message.channelChatCreated == true) it else null }, build)

@JvmName("channelCreatedTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.channelCreated(
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> = on<MessageEvent> { channelCreated(build) }

@JvmName("whenChannelCreatedMessageEvent")
fun EventRoute<MessageEvent>.whenChannelCreated(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = channelCreated { handle(handler) }

@JvmName("whenChannelCreatedTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.whenChannelCreated(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { whenChannelCreated(handler) }

@JvmName("videoChatStartedMessageEvent")
fun EventRoute<MessageEvent>.videoChatStarted(
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> = select({ if (it.event.message.videoChatStarted != null) it else null }, build)

@JvmName("videoChatStartedTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.videoChatStarted(
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> = on<MessageEvent> { videoChatStarted(build) }

@JvmName("whenVideoChatStartedMessageEvent")
fun EventRoute<MessageEvent>.whenVideoChatStarted(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = videoChatStarted { handle(handler) }

@JvmName("whenVideoChatStartedTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.whenVideoChatStarted(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { whenVideoChatStarted(handler) }

@JvmName("videoChatEndedMessageEvent")
fun EventRoute<MessageEvent>.videoChatEnded(
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> = select({ if (it.event.message.videoChatEnded != null) it else null }, build)

@JvmName("videoChatEndedTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.videoChatEnded(
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> = on<MessageEvent> { videoChatEnded(build) }

@JvmName("whenVideoChatEndedMessageEvent")
fun EventRoute<MessageEvent>.whenVideoChatEnded(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = videoChatEnded { handle(handler) }

@JvmName("whenVideoChatEndedTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.whenVideoChatEnded(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { whenVideoChatEnded(handler) }

@JvmName("videoChatScheduledMessageEvent")
fun EventRoute<MessageEvent>.videoChatScheduled(
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> = select({ if (it.event.message.videoChatScheduled != null) it else null }, build)

@JvmName("videoChatScheduledTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.videoChatScheduled(
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> = on<MessageEvent> { videoChatScheduled(build) }

@JvmName("whenVideoChatScheduledMessageEvent")
fun EventRoute<MessageEvent>.whenVideoChatScheduled(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = videoChatScheduled { handle(handler) }

@JvmName("whenVideoChatScheduledTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.whenVideoChatScheduled(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { whenVideoChatScheduled(handler) }

@JvmName("videoChatParticipantsInvitedMessageEvent")
fun EventRoute<MessageEvent>.videoChatParticipantsInvited(
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> = select({ if (it.event.message.videoChatParticipantsInvited != null) it else null }, build)

@JvmName("videoChatParticipantsInvitedTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.videoChatParticipantsInvited(
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> = on<MessageEvent> { videoChatParticipantsInvited(build) }

@JvmName("whenVideoChatParticipantsInvitedMessageEvent")
fun EventRoute<MessageEvent>.whenVideoChatParticipantsInvited(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = videoChatParticipantsInvited { handle(handler) }

@JvmName("whenVideoChatParticipantsInvitedTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.whenVideoChatParticipantsInvited(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { whenVideoChatParticipantsInvited(handler) }

@JvmName("forumTopicCreatedMessageEvent")
fun EventRoute<MessageEvent>.forumTopicCreated(
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> = select({ if (it.event.message.forumTopicCreated != null) it else null }, build)

@JvmName("forumTopicCreatedTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.forumTopicCreated(
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> = on<MessageEvent> { forumTopicCreated(build) }

@JvmName("whenForumTopicCreatedMessageEvent")
fun EventRoute<MessageEvent>.whenForumTopicCreated(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = forumTopicCreated { handle(handler) }

@JvmName("whenForumTopicCreatedTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.whenForumTopicCreated(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { whenForumTopicCreated(handler) }

@JvmName("forumTopicEditedMessageEvent")
fun EventRoute<MessageEvent>.forumTopicEdited(
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> = select({ if (it.event.message.forumTopicEdited != null) it else null }, build)

@JvmName("forumTopicEditedTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.forumTopicEdited(
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> = on<MessageEvent> { forumTopicEdited(build) }

@JvmName("whenForumTopicEditedMessageEvent")
fun EventRoute<MessageEvent>.whenForumTopicEdited(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = forumTopicEdited { handle(handler) }

@JvmName("whenForumTopicEditedTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.whenForumTopicEdited(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { whenForumTopicEdited(handler) }

@JvmName("forumTopicClosedMessageEvent")
fun EventRoute<MessageEvent>.forumTopicClosed(
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> = select({ if (it.event.message.forumTopicClosed != null) it else null }, build)

@JvmName("forumTopicClosedTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.forumTopicClosed(
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> = on<MessageEvent> { forumTopicClosed(build) }

@JvmName("whenForumTopicClosedMessageEvent")
fun EventRoute<MessageEvent>.whenForumTopicClosed(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = forumTopicClosed { handle(handler) }

@JvmName("whenForumTopicClosedTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.whenForumTopicClosed(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { whenForumTopicClosed(handler) }

@JvmName("forumTopicReopenedMessageEvent")
fun EventRoute<MessageEvent>.forumTopicReopened(
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> = select({ if (it.event.message.forumTopicReopened != null) it else null }, build)

@JvmName("forumTopicReopenedTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.forumTopicReopened(
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> = on<MessageEvent> { forumTopicReopened(build) }

@JvmName("whenForumTopicReopenedMessageEvent")
fun EventRoute<MessageEvent>.whenForumTopicReopened(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = forumTopicReopened { handle(handler) }

@JvmName("whenForumTopicReopenedTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.whenForumTopicReopened(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { whenForumTopicReopened(handler) }

@JvmName("generalForumTopicHiddenMessageEvent")
fun EventRoute<MessageEvent>.generalForumTopicHidden(
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> = select({ if (it.event.message.generalForumTopicHidden != null) it else null }, build)

@JvmName("generalForumTopicHiddenTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.generalForumTopicHidden(
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> = on<MessageEvent> { generalForumTopicHidden(build) }

@JvmName("whenGeneralForumTopicHiddenMessageEvent")
fun EventRoute<MessageEvent>.whenGeneralForumTopicHidden(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = generalForumTopicHidden { handle(handler) }

@JvmName("whenGeneralForumTopicHiddenTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.whenGeneralForumTopicHidden(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { whenGeneralForumTopicHidden(handler) }

@JvmName("generalForumTopicUnhiddenMessageEvent")
fun EventRoute<MessageEvent>.generalForumTopicUnhidden(
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> = select({ if (it.event.message.generalForumTopicUnhidden != null) it else null }, build)

@JvmName("generalForumTopicUnhiddenTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.generalForumTopicUnhidden(
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> = on<MessageEvent> { generalForumTopicUnhidden(build) }

@JvmName("whenGeneralForumTopicUnhiddenMessageEvent")
fun EventRoute<MessageEvent>.whenGeneralForumTopicUnhidden(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = generalForumTopicUnhidden { handle(handler) }

@JvmName("whenGeneralForumTopicUnhiddenTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.whenGeneralForumTopicUnhidden(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { whenGeneralForumTopicUnhidden(handler) }

@JvmName("giveawayCreatedMessageEvent")
fun EventRoute<MessageEvent>.giveawayCreated(
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> = select({ if (it.event.message.giveawayCreated != null) it else null }, build)

@JvmName("giveawayCreatedTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.giveawayCreated(
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> = on<MessageEvent> { giveawayCreated(build) }

@JvmName("whenGiveawayCreatedMessageEvent")
fun EventRoute<MessageEvent>.whenGiveawayCreated(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = giveawayCreated { handle(handler) }

@JvmName("whenGiveawayCreatedTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.whenGiveawayCreated(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { whenGiveawayCreated(handler) }

@JvmName("giveawayCompletedMessageEvent")
fun EventRoute<MessageEvent>.giveawayCompleted(
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> = select({ if (it.event.message.giveawayCompleted != null) it else null }, build)

@JvmName("giveawayCompletedTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.giveawayCompleted(
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> = on<MessageEvent> { giveawayCompleted(build) }

@JvmName("whenGiveawayCompletedMessageEvent")
fun EventRoute<MessageEvent>.whenGiveawayCompleted(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = giveawayCompleted { handle(handler) }

@JvmName("whenGiveawayCompletedTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.whenGiveawayCompleted(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { whenGiveawayCompleted(handler) }

@JvmName("boostAddedMessageEvent")
fun EventRoute<MessageEvent>.boostAdded(
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> = select({ if (it.event.message.boostAdded != null) it else null }, build)

@JvmName("boostAddedTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.boostAdded(
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> = on<MessageEvent> { boostAdded(build) }

@JvmName("whenBoostAddedMessageEvent")
fun EventRoute<MessageEvent>.whenBoostAdded(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = boostAdded { handle(handler) }

@JvmName("whenBoostAddedTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.whenBoostAdded(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { whenBoostAdded(handler) }

@JvmName("migrateToSupergroupMessageEvent")
fun EventRoute<MessageEvent>.migrateToSupergroup(
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> = select({ if (it.event.message.migrateToChatId != null) it else null }, build)

@JvmName("migrateToSupergroupTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.migrateToSupergroup(
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> = on<MessageEvent> { migrateToSupergroup(build) }

@JvmName("whenMigrateToSupergroupMessageEvent")
fun EventRoute<MessageEvent>.whenMigrateToSupergroup(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = migrateToSupergroup { handle(handler) }

@JvmName("whenMigrateToSupergroupTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.whenMigrateToSupergroup(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { whenMigrateToSupergroup(handler) }

@JvmName("migrateFromGroupMessageEvent")
fun EventRoute<MessageEvent>.migrateFromGroup(
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> = select({ if (it.event.message.migrateFromChatId != null) it else null }, build)

@JvmName("migrateFromGroupTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.migrateFromGroup(
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> = on<MessageEvent> { migrateFromGroup(build) }

@JvmName("whenMigrateFromGroupMessageEvent")
fun EventRoute<MessageEvent>.whenMigrateFromGroup(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = migrateFromGroup { handle(handler) }

@JvmName("whenMigrateFromGroupTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.whenMigrateFromGroup(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { whenMigrateFromGroup(handler) }

@JvmName("messageAutoDeleteTimerChangedMessageEvent")
fun EventRoute<MessageEvent>.messageAutoDeleteTimerChanged(
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> =
    select({ if (it.event.message.messageAutoDeleteTimerChanged != null) it else null }, build)

@JvmName("messageAutoDeleteTimerChangedTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.messageAutoDeleteTimerChanged(
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> = on<MessageEvent> { messageAutoDeleteTimerChanged(build) }

@JvmName("whenMessageAutoDeleteTimerChangedMessageEvent")
fun EventRoute<MessageEvent>.whenMessageAutoDeleteTimerChanged(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = messageAutoDeleteTimerChanged { handle(handler) }

@JvmName("whenMessageAutoDeleteTimerChangedTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.whenMessageAutoDeleteTimerChanged(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { whenMessageAutoDeleteTimerChanged(handler) }

@JvmName("webAppDataMessageEvent")
fun EventRoute<MessageEvent>.webAppData(
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> = select({ if (it.event.message.webAppData != null) it else null }, build)

@JvmName("webAppDataTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.webAppData(
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> = on<MessageEvent> { webAppData(build) }

@JvmName("whenWebAppDataMessageEvent")
fun EventRoute<MessageEvent>.whenWebAppData(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = webAppData { handle(handler) }

@JvmName("whenWebAppDataTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.whenWebAppData(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { whenWebAppData(handler) }
