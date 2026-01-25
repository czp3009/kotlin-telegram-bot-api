// Auto-generated from Swagger specificationDo not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlinx.serialization.Serializable

/**
 * This object represents a service message about new members invited to a video chat.
 */
@Serializable
public data class VideoChatParticipantsInvited(
    /**
     * New members that were invited to the video chat
     */
    public val users: List<User>,
)
