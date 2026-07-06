package com.hiczp.telegram.bot.protocol.extension

import com.hiczp.telegram.bot.protocol.model.ChatMember
import com.hiczp.telegram.bot.protocol.model.ChatMemberAdministrator
import com.hiczp.telegram.bot.protocol.model.ChatMemberBanned
import com.hiczp.telegram.bot.protocol.model.ChatMemberLeft
import com.hiczp.telegram.bot.protocol.model.ChatMemberMember
import com.hiczp.telegram.bot.protocol.model.ChatMemberOwner
import com.hiczp.telegram.bot.protocol.model.ChatMemberRestricted
import com.hiczp.telegram.bot.protocol.model.ChatMemberUpdated

/**
 * Returns true if the user is currently present in the chat.
 */
val ChatMember.isPresent: Boolean
    get() = when (this) {
        is ChatMemberOwner,
        is ChatMemberAdministrator,
        is ChatMemberMember -> true

        is ChatMemberRestricted -> isMember
        is ChatMemberLeft,
        is ChatMemberBanned -> false
    }

/**
 * Returns true if the user owns the chat or has administrator privileges.
 */
val ChatMember.isAdministrator: Boolean
    get() = this is ChatMemberOwner || this is ChatMemberAdministrator

/**
 * Returns true if this update changed the user from absent to present.
 */
val ChatMemberUpdated.didJoin: Boolean
    get() = !oldChatMember.isPresent && newChatMember.isPresent

/**
 * Returns true if this update changed the user from present to absent.
 */
val ChatMemberUpdated.didLeave: Boolean
    get() = oldChatMember.isPresent && !newChatMember.isPresent
