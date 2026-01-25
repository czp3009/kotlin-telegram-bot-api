// Auto-generated from Swagger specificationDo not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlin.Boolean
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents the rights of a business bot.
 */
@Serializable
public data class BusinessBotRights(
    /**
     * *Optional*. *True*, if the bot can send and edit messages in the private chats that had incoming messages in the last 24 hours
     */
    @SerialName("can_reply")
    public val canReply: Boolean? = null,
    /**
     * *Optional*. *True*, if the bot can mark incoming private messages as read
     */
    @SerialName("can_read_messages")
    public val canReadMessages: Boolean? = null,
    /**
     * *Optional*. *True*, if the bot can delete messages sent by the bot
     */
    @SerialName("can_delete_sent_messages")
    public val canDeleteSentMessages: Boolean? = null,
    /**
     * *Optional*. *True*, if the bot can delete all private messages in managed chats
     */
    @SerialName("can_delete_all_messages")
    public val canDeleteAllMessages: Boolean? = null,
    /**
     * *Optional*. *True*, if the bot can edit the first and last name of the business account
     */
    @SerialName("can_edit_name")
    public val canEditName: Boolean? = null,
    /**
     * *Optional*. *True*, if the bot can edit the bio of the business account
     */
    @SerialName("can_edit_bio")
    public val canEditBio: Boolean? = null,
    /**
     * *Optional*. *True*, if the bot can edit the profile photo of the business account
     */
    @SerialName("can_edit_profile_photo")
    public val canEditProfilePhoto: Boolean? = null,
    /**
     * *Optional*. *True*, if the bot can edit the username of the business account
     */
    @SerialName("can_edit_username")
    public val canEditUsername: Boolean? = null,
    /**
     * *Optional*. *True*, if the bot can change the privacy settings pertaining to gifts for the business account
     */
    @SerialName("can_change_gift_settings")
    public val canChangeGiftSettings: Boolean? = null,
    /**
     * *Optional*. *True*, if the bot can view gifts and the amount of Telegram Stars owned by the business account
     */
    @SerialName("can_view_gifts_and_stars")
    public val canViewGiftsAndStars: Boolean? = null,
    /**
     * *Optional*. *True*, if the bot can convert regular gifts owned by the business account to Telegram Stars
     */
    @SerialName("can_convert_gifts_to_stars")
    public val canConvertGiftsToStars: Boolean? = null,
    /**
     * *Optional*. *True*, if the bot can transfer and upgrade gifts owned by the business account
     */
    @SerialName("can_transfer_and_upgrade_gifts")
    public val canTransferAndUpgradeGifts: Boolean? = null,
    /**
     * *Optional*. *True*, if the bot can transfer Telegram Stars received by the business account to its own account, or use them to upgrade and transfer gifts
     */
    @SerialName("can_transfer_stars")
    public val canTransferStars: Boolean? = null,
    /**
     * *Optional*. *True*, if the bot can post, edit and delete stories on behalf of the business account
     */
    @SerialName("can_manage_stories")
    public val canManageStories: Boolean? = null,
)
