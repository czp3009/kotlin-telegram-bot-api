package com.hiczp.telegram.bot.protocol.test.extension

import com.hiczp.telegram.bot.protocol.constant.ChatType
import com.hiczp.telegram.bot.protocol.extension.didJoin
import com.hiczp.telegram.bot.protocol.extension.didLeave
import com.hiczp.telegram.bot.protocol.extension.isAdministrator
import com.hiczp.telegram.bot.protocol.extension.isPresent
import com.hiczp.telegram.bot.protocol.model.Chat
import com.hiczp.telegram.bot.protocol.model.ChatMember
import com.hiczp.telegram.bot.protocol.model.ChatMemberAdministrator
import com.hiczp.telegram.bot.protocol.model.ChatMemberBanned
import com.hiczp.telegram.bot.protocol.model.ChatMemberLeft
import com.hiczp.telegram.bot.protocol.model.ChatMemberMember
import com.hiczp.telegram.bot.protocol.model.ChatMemberOwner
import com.hiczp.telegram.bot.protocol.model.ChatMemberRestricted
import com.hiczp.telegram.bot.protocol.model.ChatMemberUpdated
import com.hiczp.telegram.bot.protocol.model.User
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ChatMembersTest {
    private val user = User(id = 1L, isBot = false, firstName = "User")
    private val actor = User(id = 2L, isBot = false, firstName = "Actor")
    private val chat = Chat(id = 100L, type = ChatType.GROUP)

    @Test
    fun userReturnsMemberUserForEveryState() {
        val members = listOf(
            owner(),
            administrator(),
            ChatMemberMember(user = user),
            restricted(isMember = true),
            ChatMemberLeft(user),
            ChatMemberBanned(user, untilDate = 0L),
        )

        members.forEach { assertEquals(user, it.user) }
    }

    @Test
    fun isPresentHandlesRestrictedMemberState() {
        assertTrue(owner().isPresent)
        assertTrue(administrator().isPresent)
        assertTrue(ChatMemberMember(user = user).isPresent)
        assertTrue(restricted(isMember = true).isPresent)

        assertFalse(restricted(isMember = false).isPresent)
        assertFalse(ChatMemberLeft(user).isPresent)
        assertFalse(ChatMemberBanned(user, untilDate = 0L).isPresent)
    }

    @Test
    fun isAdministratorIncludesOwnerAndAdministrator() {
        assertTrue(owner().isAdministrator)
        assertTrue(administrator().isAdministrator)
        assertFalse(ChatMemberMember(user = user).isAdministrator)
        assertFalse(restricted(isMember = true).isAdministrator)
    }

    @Test
    fun didJoinAndDidLeaveUsePresenceSemantics() {
        val joined = update(
            oldChatMember = ChatMemberLeft(user),
            newChatMember = ChatMemberMember(user = user),
        )
        val left = update(
            oldChatMember = restricted(isMember = true),
            newChatMember = restricted(isMember = false),
        )
        val changedRestrictions = update(
            oldChatMember = restricted(isMember = true),
            newChatMember = ChatMemberMember(user = user),
        )

        assertTrue(joined.didJoin)
        assertFalse(joined.didLeave)

        assertFalse(left.didJoin)
        assertTrue(left.didLeave)

        assertFalse(changedRestrictions.didJoin)
        assertFalse(changedRestrictions.didLeave)
    }

    private fun owner() = ChatMemberOwner(user = user, isAnonymous = false)

    private fun administrator() = ChatMemberAdministrator(
        user = user,
        canBeEdited = false,
        isAnonymous = false,
        canManageChat = true,
        canDeleteMessages = false,
        canManageVideoChats = false,
        canRestrictMembers = false,
        canPromoteMembers = false,
        canChangeInfo = false,
        canInviteUsers = false,
        canPostStories = false,
        canEditStories = false,
        canDeleteStories = false,
    )

    private fun restricted(isMember: Boolean) = ChatMemberRestricted(
        user = user,
        isMember = isMember,
        canSendMessages = false,
        canSendAudios = false,
        canSendDocuments = false,
        canSendPhotos = false,
        canSendVideos = false,
        canSendVideoNotes = false,
        canSendVoiceNotes = false,
        canSendPolls = false,
        canSendOtherMessages = false,
        canAddWebPagePreviews = false,
        canReactToMessages = false,
        canEditTag = false,
        canChangeInfo = false,
        canInviteUsers = false,
        canPinMessages = false,
        canManageTopics = false,
        untilDate = 0L,
    )

    private fun update(
        oldChatMember: ChatMember,
        newChatMember: ChatMember,
    ) = ChatMemberUpdated(
        chat = chat,
        from = actor,
        date = 1L,
        oldChatMember = oldChatMember,
        newChatMember = newChatMember,
    )
}
