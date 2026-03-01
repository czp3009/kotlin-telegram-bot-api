package com.hiczp.telegram.bot.application.dispatcher.handler.matcher

import com.hiczp.telegram.bot.application.context.TelegramBotEventContext
import com.hiczp.telegram.bot.application.dispatcher.handler.EventRoute
import com.hiczp.telegram.bot.protocol.event.MessageEvent
import com.hiczp.telegram.bot.protocol.event.TelegramBotEvent
import kotlinx.coroutines.CoroutineScope

/**
 * Registers a handler for messages about new chat members.
 *
 * Triggered when new members are added to a group or supergroup.
 *
 * @param handler A suspending function with [CoroutineScope] receiver that handles the event.
 */
fun EventRoute<MessageEvent>.whenNewChatMembers(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) {
    select({ if (it.event.message.newChatMembers != null) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers a new chat members handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.whenNewChatMembers(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { whenNewChatMembers(handler) }

/**
 * Registers a handler for messages about a member leaving the chat.
 *
 * Triggered when a member is removed from a group or supergroup.
 *
 * @param handler A suspending function with [CoroutineScope] receiver that handles the event.
 */
fun EventRoute<MessageEvent>.whenLeftChatMember(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) {
    select({ if (it.event.message.leftChatMember != null) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers a left chat member handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.whenLeftChatMember(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { whenLeftChatMember(handler) }

/**
 * Registers a handler for messages about chat title changes.
 *
 * @param handler A suspending function with [CoroutineScope] receiver that handles the event.
 */
fun EventRoute<MessageEvent>.whenNewChatTitle(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) {
    select({ if (it.event.message.newChatTitle != null) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers a new chat title handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.whenNewChatTitle(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { whenNewChatTitle(handler) }

/**
 * Registers a handler for messages about chat photo changes.
 *
 * @param handler A suspending function with [CoroutineScope] receiver that handles the event.
 */
fun EventRoute<MessageEvent>.whenNewChatPhoto(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) {
    select({ if (it.event.message.newChatPhoto != null) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers a new chat photo handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.whenNewChatPhoto(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { whenNewChatPhoto(handler) }

/**
 * Registers a handler for messages about chat photo deletion.
 *
 * @param handler A suspending function with [CoroutineScope] receiver that handles the event.
 */
fun EventRoute<MessageEvent>.whenDeleteChatPhoto(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) {
    select({ if (it.event.message.deleteChatPhoto == true) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers a delete chat photo handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.whenDeleteChatPhoto(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { whenDeleteChatPhoto(handler) }

/**
 * Registers a handler for messages about pinned messages.
 *
 * @param handler A suspending function with [CoroutineScope] receiver that handles the event.
 */
fun EventRoute<MessageEvent>.whenPinnedMessage(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) {
    select({ if (it.event.message.pinnedMessage != null) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers a pinned message handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.whenPinnedMessage(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { whenPinnedMessage(handler) }


/**
 * Registers a handler for messages about group chat creation.
 *
 * @param handler A suspending function with [CoroutineScope] receiver that handles the event.
 */
fun EventRoute<MessageEvent>.whenGroupCreated(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) {
    select({ if (it.event.message.groupChatCreated == true) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers a group created handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.whenGroupCreated(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { whenGroupCreated(handler) }

/**
 * Registers a handler for messages about supergroup chat creation.
 *
 * @param handler A suspending function with [CoroutineScope] receiver that handles the event.
 */
fun EventRoute<MessageEvent>.whenSupergroupCreated(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) {
    select({ if (it.event.message.supergroupChatCreated == true) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers a supergroup created handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.whenSupergroupCreated(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { whenSupergroupCreated(handler) }

/**
 * Registers a handler for messages about channel creation.
 *
 * @param handler A suspending function with [CoroutineScope] receiver that handles the event.
 */
fun EventRoute<MessageEvent>.whenChannelCreated(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) {
    select({ if (it.event.message.channelChatCreated == true) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers a channel created handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.whenChannelCreated(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { whenChannelCreated(handler) }


/**
 * Registers a handler for messages about video chat started.
 *
 * @param handler A suspending function with [CoroutineScope] receiver that handles the event.
 */
fun EventRoute<MessageEvent>.whenVideoChatStarted(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) {
    select({ if (it.event.message.videoChatStarted != null) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers a video chat started handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.whenVideoChatStarted(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { whenVideoChatStarted(handler) }

/**
 * Registers a handler for messages about video chat ended.
 *
 * @param handler A suspending function with [CoroutineScope] receiver that handles the event.
 */
fun EventRoute<MessageEvent>.whenVideoChatEnded(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) {
    select({ if (it.event.message.videoChatEnded != null) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers a video chat ended handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.whenVideoChatEnded(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { whenVideoChatEnded(handler) }

/**
 * Registers a handler for messages about video chat scheduled.
 *
 * @param handler A suspending function with [CoroutineScope] receiver that handles the event.
 */
fun EventRoute<MessageEvent>.whenVideoChatScheduled(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) {
    select({ if (it.event.message.videoChatScheduled != null) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers a video chat scheduled handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.whenVideoChatScheduled(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { whenVideoChatScheduled(handler) }

/**
 * Registers a handler for messages about video chat participants invited.
 *
 * @param handler A suspending function with [CoroutineScope] receiver that handles the event.
 */
fun EventRoute<MessageEvent>.whenVideoChatParticipantsInvited(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) {
    select({ if (it.event.message.videoChatParticipantsInvited != null) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers a video chat participants invited handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.whenVideoChatParticipantsInvited(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { whenVideoChatParticipantsInvited(handler) }


/**
 * Registers a handler for messages about forum topic created.
 *
 * @param handler A suspending function with [CoroutineScope] receiver that handles the event.
 */
fun EventRoute<MessageEvent>.whenForumTopicCreated(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) {
    select({ if (it.event.message.forumTopicCreated != null) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers a forum topic created handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.whenForumTopicCreated(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { whenForumTopicCreated(handler) }

/**
 * Registers a handler for messages about forum topic edited.
 *
 * @param handler A suspending function with [CoroutineScope] receiver that handles the event.
 */
fun EventRoute<MessageEvent>.whenForumTopicEdited(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) {
    select({ if (it.event.message.forumTopicEdited != null) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers a forum topic edited handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.whenForumTopicEdited(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { whenForumTopicEdited(handler) }

/**
 * Registers a handler for messages about forum topic closed.
 *
 * @param handler A suspending function with [CoroutineScope] receiver that handles the event.
 */
fun EventRoute<MessageEvent>.whenForumTopicClosed(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) {
    select({ if (it.event.message.forumTopicClosed != null) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers a forum topic closed handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.whenForumTopicClosed(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { whenForumTopicClosed(handler) }

/**
 * Registers a handler for messages about forum topic reopened.
 *
 * @param handler A suspending function with [CoroutineScope] receiver that handles the event.
 */
fun EventRoute<MessageEvent>.whenForumTopicReopened(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) {
    select({ if (it.event.message.forumTopicReopened != null) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers a forum topic reopened handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.whenForumTopicReopened(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { whenForumTopicReopened(handler) }

/**
 * Registers a handler for messages about general forum topic hidden.
 *
 * @param handler A suspending function with [CoroutineScope] receiver that handles the event.
 */
fun EventRoute<MessageEvent>.whenGeneralForumTopicHidden(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) {
    select({ if (it.event.message.generalForumTopicHidden != null) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers a general forum topic hidden handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.whenGeneralForumTopicHidden(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { whenGeneralForumTopicHidden(handler) }

/**
 * Registers a handler for messages about general forum topic unhidden.
 *
 * @param handler A suspending function with [CoroutineScope] receiver that handles the event.
 */
fun EventRoute<MessageEvent>.whenGeneralForumTopicUnhidden(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) {
    select({ if (it.event.message.generalForumTopicUnhidden != null) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers a general forum topic unhidden handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.whenGeneralForumTopicUnhidden(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { whenGeneralForumTopicUnhidden(handler) }


/**
 * Registers a handler for messages about giveaway created.
 *
 * @param handler A suspending function with [CoroutineScope] receiver that handles the event.
 */
fun EventRoute<MessageEvent>.whenGiveawayCreated(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) {
    select({ if (it.event.message.giveawayCreated != null) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers a giveaway created handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.whenGiveawayCreated(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { whenGiveawayCreated(handler) }

/**
 * Registers a handler for messages about giveaway completed.
 *
 * @param handler A suspending function with [CoroutineScope] receiver that handles the event.
 */
fun EventRoute<MessageEvent>.whenGiveawayCompleted(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) {
    select({ if (it.event.message.giveawayCompleted != null) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers a giveaway completed handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.whenGiveawayCompleted(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { whenGiveawayCompleted(handler) }


/**
 * Registers a handler for messages about boost added to chat.
 *
 * @param handler A suspending function with [CoroutineScope] receiver that handles the event.
 */
fun EventRoute<MessageEvent>.whenBoostAdded(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) {
    select({ if (it.event.message.boostAdded != null) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers a boost added handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.whenBoostAdded(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { whenBoostAdded(handler) }


/**
 * Registers a handler for messages about group migration to supergroup.
 *
 * @param handler A suspending function with [CoroutineScope] receiver that handles the event.
 */
fun EventRoute<MessageEvent>.whenMigrateToSupergroup(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) {
    select({ if (it.event.message.migrateToChatId != null) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers a migrate to supergroup handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.whenMigrateToSupergroup(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { whenMigrateToSupergroup(handler) }

/**
 * Registers a handler for messages about supergroup migration from group.
 *
 * @param handler A suspending function with [CoroutineScope] receiver that handles the event.
 */
fun EventRoute<MessageEvent>.whenMigrateFromGroup(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) {
    select({ if (it.event.message.migrateFromChatId != null) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers a migrate from group handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.whenMigrateFromGroup(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { whenMigrateFromGroup(handler) }


/**
 * Registers a handler for messages about auto-delete timer changed.
 *
 * @param handler A suspending function with [CoroutineScope] receiver that handles the event.
 */
fun EventRoute<MessageEvent>.whenMessageAutoDeleteTimerChanged(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) {
    select({ if (it.event.message.messageAutoDeleteTimerChanged != null) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers a message auto delete timer changed handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.whenMessageAutoDeleteTimerChanged(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { whenMessageAutoDeleteTimerChanged(handler) }


/**
 * Registers a handler for messages containing Web App data.
 *
 * @param handler A suspending function with [CoroutineScope] receiver that handles the event.
 */
fun EventRoute<MessageEvent>.whenWebAppData(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) {
    select({ if (it.event.message.webAppData != null) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers a web app data handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.whenWebAppData(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { whenWebAppData(handler) }
