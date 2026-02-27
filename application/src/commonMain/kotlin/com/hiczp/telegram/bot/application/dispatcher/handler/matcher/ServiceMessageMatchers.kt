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
fun EventRoute<MessageEvent>.newChatMembers(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) {
    select({ if (it.event.message.newChatMembers != null) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers a new chat members handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.newChatMembers(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { newChatMembers(handler) }

/**
 * Registers a handler for messages about a member leaving the chat.
 *
 * Triggered when a member is removed from a group or supergroup.
 *
 * @param handler A suspending function with [CoroutineScope] receiver that handles the event.
 */
fun EventRoute<MessageEvent>.leftChatMember(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) {
    select({ if (it.event.message.leftChatMember != null) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers a left chat member handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.leftChatMember(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { leftChatMember(handler) }

/**
 * Registers a handler for messages about chat title changes.
 *
 * @param handler A suspending function with [CoroutineScope] receiver that handles the event.
 */
fun EventRoute<MessageEvent>.newChatTitle(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) {
    select({ if (it.event.message.newChatTitle != null) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers a new chat title handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.newChatTitle(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { newChatTitle(handler) }

/**
 * Registers a handler for messages about chat photo changes.
 *
 * @param handler A suspending function with [CoroutineScope] receiver that handles the event.
 */
fun EventRoute<MessageEvent>.newChatPhoto(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) {
    select({ if (it.event.message.newChatPhoto != null) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers a new chat photo handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.newChatPhoto(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { newChatPhoto(handler) }

/**
 * Registers a handler for messages about chat photo deletion.
 *
 * @param handler A suspending function with [CoroutineScope] receiver that handles the event.
 */
fun EventRoute<MessageEvent>.deleteChatPhoto(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) {
    select({ if (it.event.message.deleteChatPhoto == true) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers a delete chat photo handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.deleteChatPhoto(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { deleteChatPhoto(handler) }

/**
 * Registers a handler for messages about pinned messages.
 *
 * @param handler A suspending function with [CoroutineScope] receiver that handles the event.
 */
fun EventRoute<MessageEvent>.pinnedMessage(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) {
    select({ if (it.event.message.pinnedMessage != null) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers a pinned message handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.pinnedMessage(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { pinnedMessage(handler) }


/**
 * Registers a handler for messages about group chat creation.
 *
 * @param handler A suspending function with [CoroutineScope] receiver that handles the event.
 */
fun EventRoute<MessageEvent>.groupCreated(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) {
    select({ if (it.event.message.groupChatCreated == true) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers a group created handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.groupCreated(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { groupCreated(handler) }

/**
 * Registers a handler for messages about supergroup chat creation.
 *
 * @param handler A suspending function with [CoroutineScope] receiver that handles the event.
 */
fun EventRoute<MessageEvent>.supergroupCreated(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) {
    select({ if (it.event.message.supergroupChatCreated == true) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers a supergroup created handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.supergroupCreated(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { supergroupCreated(handler) }

/**
 * Registers a handler for messages about channel creation.
 *
 * @param handler A suspending function with [CoroutineScope] receiver that handles the event.
 */
fun EventRoute<MessageEvent>.channelCreated(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) {
    select({ if (it.event.message.channelChatCreated == true) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers a channel created handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.channelCreated(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { channelCreated(handler) }


/**
 * Registers a handler for messages about video chat started.
 *
 * @param handler A suspending function with [CoroutineScope] receiver that handles the event.
 */
fun EventRoute<MessageEvent>.videoChatStarted(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) {
    select({ if (it.event.message.videoChatStarted != null) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers a video chat started handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.videoChatStarted(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { videoChatStarted(handler) }

/**
 * Registers a handler for messages about video chat ended.
 *
 * @param handler A suspending function with [CoroutineScope] receiver that handles the event.
 */
fun EventRoute<MessageEvent>.videoChatEnded(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) {
    select({ if (it.event.message.videoChatEnded != null) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers a video chat ended handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.videoChatEnded(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { videoChatEnded(handler) }

/**
 * Registers a handler for messages about video chat scheduled.
 *
 * @param handler A suspending function with [CoroutineScope] receiver that handles the event.
 */
fun EventRoute<MessageEvent>.videoChatScheduled(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) {
    select({ if (it.event.message.videoChatScheduled != null) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers a video chat scheduled handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.videoChatScheduled(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { videoChatScheduled(handler) }

/**
 * Registers a handler for messages about video chat participants invited.
 *
 * @param handler A suspending function with [CoroutineScope] receiver that handles the event.
 */
fun EventRoute<MessageEvent>.videoChatParticipantsInvited(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) {
    select({ if (it.event.message.videoChatParticipantsInvited != null) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers a video chat participants invited handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.videoChatParticipantsInvited(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { videoChatParticipantsInvited(handler) }


/**
 * Registers a handler for messages about forum topic created.
 *
 * @param handler A suspending function with [CoroutineScope] receiver that handles the event.
 */
fun EventRoute<MessageEvent>.forumTopicCreated(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) {
    select({ if (it.event.message.forumTopicCreated != null) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers a forum topic created handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.forumTopicCreated(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { forumTopicCreated(handler) }

/**
 * Registers a handler for messages about forum topic edited.
 *
 * @param handler A suspending function with [CoroutineScope] receiver that handles the event.
 */
fun EventRoute<MessageEvent>.forumTopicEdited(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) {
    select({ if (it.event.message.forumTopicEdited != null) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers a forum topic edited handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.forumTopicEdited(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { forumTopicEdited(handler) }

/**
 * Registers a handler for messages about forum topic closed.
 *
 * @param handler A suspending function with [CoroutineScope] receiver that handles the event.
 */
fun EventRoute<MessageEvent>.forumTopicClosed(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) {
    select({ if (it.event.message.forumTopicClosed != null) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers a forum topic closed handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.forumTopicClosed(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { forumTopicClosed(handler) }

/**
 * Registers a handler for messages about forum topic reopened.
 *
 * @param handler A suspending function with [CoroutineScope] receiver that handles the event.
 */
fun EventRoute<MessageEvent>.forumTopicReopened(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) {
    select({ if (it.event.message.forumTopicReopened != null) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers a forum topic reopened handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.forumTopicReopened(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { forumTopicReopened(handler) }

/**
 * Registers a handler for messages about general forum topic hidden.
 *
 * @param handler A suspending function with [CoroutineScope] receiver that handles the event.
 */
fun EventRoute<MessageEvent>.generalForumTopicHidden(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) {
    select({ if (it.event.message.generalForumTopicHidden != null) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers a general forum topic hidden handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.generalForumTopicHidden(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { generalForumTopicHidden(handler) }

/**
 * Registers a handler for messages about general forum topic unhidden.
 *
 * @param handler A suspending function with [CoroutineScope] receiver that handles the event.
 */
fun EventRoute<MessageEvent>.generalForumTopicUnhidden(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) {
    select({ if (it.event.message.generalForumTopicUnhidden != null) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers a general forum topic unhidden handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.generalForumTopicUnhidden(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { generalForumTopicUnhidden(handler) }


/**
 * Registers a handler for messages about giveaway created.
 *
 * @param handler A suspending function with [CoroutineScope] receiver that handles the event.
 */
fun EventRoute<MessageEvent>.giveawayCreated(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) {
    select({ if (it.event.message.giveawayCreated != null) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers a giveaway created handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.giveawayCreated(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { giveawayCreated(handler) }

/**
 * Registers a handler for messages about giveaway completed.
 *
 * @param handler A suspending function with [CoroutineScope] receiver that handles the event.
 */
fun EventRoute<MessageEvent>.giveawayCompleted(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) {
    select({ if (it.event.message.giveawayCompleted != null) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers a giveaway completed handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.giveawayCompleted(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { giveawayCompleted(handler) }


/**
 * Registers a handler for messages about boost added to chat.
 *
 * @param handler A suspending function with [CoroutineScope] receiver that handles the event.
 */
fun EventRoute<MessageEvent>.boostAdded(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) {
    select({ if (it.event.message.boostAdded != null) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers a boost added handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.boostAdded(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { boostAdded(handler) }


/**
 * Registers a handler for messages about group migration to supergroup.
 *
 * @param handler A suspending function with [CoroutineScope] receiver that handles the event.
 */
fun EventRoute<MessageEvent>.migrateToSupergroup(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) {
    select({ if (it.event.message.migrateToChatId != null) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers a migrate to supergroup handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.migrateToSupergroup(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { migrateToSupergroup(handler) }

/**
 * Registers a handler for messages about supergroup migration from group.
 *
 * @param handler A suspending function with [CoroutineScope] receiver that handles the event.
 */
fun EventRoute<MessageEvent>.migrateFromGroup(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) {
    select({ if (it.event.message.migrateFromChatId != null) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers a migrate from group handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.migrateFromGroup(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { migrateFromGroup(handler) }


/**
 * Registers a handler for messages about auto-delete timer changed.
 *
 * @param handler A suspending function with [CoroutineScope] receiver that handles the event.
 */
fun EventRoute<MessageEvent>.messageAutoDeleteTimerChanged(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) {
    select({ if (it.event.message.messageAutoDeleteTimerChanged != null) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers a message auto delete timer changed handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.messageAutoDeleteTimerChanged(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { messageAutoDeleteTimerChanged(handler) }


/**
 * Registers a handler for messages containing Web App data.
 *
 * @param handler A suspending function with [CoroutineScope] receiver that handles the event.
 */
fun EventRoute<MessageEvent>.webAppData(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) {
    select({ if (it.event.message.webAppData != null) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers a web app data handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.webAppData(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { webAppData(handler) }
