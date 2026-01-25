// Auto-generated from Swagger specificationDo not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

/**
 * This object represents a message.
 */
@Serializable
public data class Message(
    /**
     * Unique message identifier inside this chat. In specific instances (e.g., message containing a video sent to a big chat), the server might automatically schedule a message instead of sending it immediately. In such cases, this field will be 0 and the relevant message will be unusable until it is actually sent
     */
    @SerialName("message_id")
    public val messageId: Long,
    /**
     * *Optional*. Unique identifier of a message thread or forum topic to which the message belongs; for supergroups and private chats only
     */
    @SerialName("message_thread_id")
    public val messageThreadId: Long? = null,
    /**
     * *Optional*. Information about the direct messages chat topic that contains the message
     */
    @SerialName("direct_messages_topic")
    public val directMessagesTopic: JsonElement? = null,
    /**
     * *Optional*. Sender of the message; may be empty for messages sent to channels. For backward compatibility, if the message was sent on behalf of a chat, the field contains a fake sender user in non-channel chats
     */
    public val from: JsonElement? = null,
    /**
     * *Optional*. Sender of the message when sent on behalf of a chat. For example, the supergroup itself for messages sent by its anonymous administrators or a linked channel for messages automatically forwarded to the channel's discussion group. For backward compatibility, if the message was sent on behalf of a chat, the field *from* contains a fake sender user in non-channel chats.
     */
    @SerialName("sender_chat")
    public val senderChat: JsonElement? = null,
    /**
     * *Optional*. If the sender of the message boosted the chat, the number of boosts added by the user
     */
    @SerialName("sender_boost_count")
    public val senderBoostCount: Long? = null,
    /**
     * *Optional*. The bot that actually sent the message on behalf of the business account. Available only for outgoing messages sent on behalf of the connected business account.
     */
    @SerialName("sender_business_bot")
    public val senderBusinessBot: JsonElement? = null,
    /**
     * Date the message was sent in Unix time. It is always a positive number, representing a valid date.
     */
    public val date: Long,
    /**
     * *Optional*. Unique identifier of the business connection from which the message was received. If non-empty, the message belongs to a chat of the corresponding business account that is independent from any potential bot chat which might share the same identifier.
     */
    @SerialName("business_connection_id")
    public val businessConnectionId: String? = null,
    /**
     * Chat the message belongs to
     */
    public val chat: JsonElement?,
    /**
     * *Optional*. Information about the original message for forwarded messages
     */
    @SerialName("forward_origin")
    public val forwardOrigin: JsonElement? = null,
    /**
     * *Optional*. *True*, if the message is sent to a topic in a forum supergroup or a private chat with the bot
     */
    @SerialName("is_topic_message")
    public val isTopicMessage: Boolean? = null,
    /**
     * *Optional*. *True*, if the message is a channel post that was automatically forwarded to the connected discussion group
     */
    @SerialName("is_automatic_forward")
    public val isAutomaticForward: Boolean? = null,
    /**
     * *Optional*. For replies in the same chat and message thread, the original message. Note that the [Message](https://core.telegram.org/bots/api#message) object in this field will not contain further *reply_to_message* fields even if it itself is a reply.
     */
    @SerialName("reply_to_message")
    public val replyToMessage: JsonElement? = null,
    /**
     * *Optional*. Information about the message that is being replied to, which may come from another chat or forum topic
     */
    @SerialName("external_reply")
    public val externalReply: JsonElement? = null,
    /**
     * *Optional*. For replies that quote part of the original message, the quoted part of the message
     */
    public val quote: JsonElement? = null,
    /**
     * *Optional*. For replies to a story, the original story
     */
    @SerialName("reply_to_story")
    public val replyToStory: JsonElement? = null,
    /**
     * *Optional*. Identifier of the specific checklist task that is being replied to
     */
    @SerialName("reply_to_checklist_task_id")
    public val replyToChecklistTaskId: Long? = null,
    /**
     * *Optional*. Bot through which the message was sent
     */
    @SerialName("via_bot")
    public val viaBot: JsonElement? = null,
    /**
     * *Optional*. Date the message was last edited in Unix time
     */
    @SerialName("edit_date")
    public val editDate: Long? = null,
    /**
     * *Optional*. *True*, if the message can't be forwarded
     */
    @SerialName("has_protected_content")
    public val hasProtectedContent: Boolean? = null,
    /**
     * *Optional*. *True*, if the message was sent by an implicit action, for example, as an away or a greeting business message, or as a scheduled message
     */
    @SerialName("is_from_offline")
    public val isFromOffline: Boolean? = null,
    /**
     * *Optional*. *True*, if the message is a paid post. Note that such posts must not be deleted for 24 hours to receive the payment and can't be edited.
     */
    @SerialName("is_paid_post")
    public val isPaidPost: Boolean? = null,
    /**
     * *Optional*. The unique identifier of a media message group this message belongs to
     */
    @SerialName("media_group_id")
    public val mediaGroupId: String? = null,
    /**
     * *Optional*. Signature of the post author for messages in channels, or the custom title of an anonymous group administrator
     */
    @SerialName("author_signature")
    public val authorSignature: String? = null,
    /**
     * *Optional*. The number of Telegram Stars that were paid by the sender of the message to send it
     */
    @SerialName("paid_star_count")
    public val paidStarCount: Long? = null,
    /**
     * *Optional*. For text messages, the actual UTF-8 text of the message
     */
    public val text: String? = null,
    /**
     * *Optional*. For text messages, special entities like usernames, URLs, bot commands, etc. that appear in the text
     */
    public val entities: List<MessageEntity>? = null,
    /**
     * *Optional*. Options used for link preview generation for the message, if it is a text message and link preview options were changed
     */
    @SerialName("link_preview_options")
    public val linkPreviewOptions: JsonElement? = null,
    /**
     * *Optional*. Information about suggested post parameters if the message is a suggested post in a channel direct messages chat. If the message is an approved or declined suggested post, then it can't be edited.
     */
    @SerialName("suggested_post_info")
    public val suggestedPostInfo: JsonElement? = null,
    /**
     * *Optional*. Unique identifier of the message effect added to the message
     */
    @SerialName("effect_id")
    public val effectId: String? = null,
    /**
     * *Optional*. Message is an animation, information about the animation. For backward compatibility, when this field is set, the *document* field will also be set
     */
    public val animation: JsonElement? = null,
    /**
     * *Optional*. Message is an audio file, information about the file
     */
    public val audio: JsonElement? = null,
    /**
     * *Optional*. Message is a general file, information about the file
     */
    public val document: JsonElement? = null,
    /**
     * *Optional*. Message contains paid media; information about the paid media
     */
    @SerialName("paid_media")
    public val paidMedia: JsonElement? = null,
    /**
     * *Optional*. Message is a photo, available sizes of the photo
     */
    public val photo: List<PhotoSize>? = null,
    /**
     * *Optional*. Message is a sticker, information about the sticker
     */
    public val sticker: JsonElement? = null,
    /**
     * *Optional*. Message is a forwarded story
     */
    public val story: JsonElement? = null,
    /**
     * *Optional*. Message is a video, information about the video
     */
    public val video: JsonElement? = null,
    /**
     * *Optional*. Message is a [video note](https://telegram.org/blog/video-messages-and-telescope), information about the video message
     */
    @SerialName("video_note")
    public val videoNote: JsonElement? = null,
    /**
     * *Optional*. Message is a voice message, information about the file
     */
    public val voice: JsonElement? = null,
    /**
     * *Optional*. Caption for the animation, audio, document, paid media, photo, video or voice
     */
    public val caption: String? = null,
    /**
     * *Optional*. For messages with a caption, special entities like usernames, URLs, bot commands, etc. that appear in the caption
     */
    @SerialName("caption_entities")
    public val captionEntities: List<MessageEntity>? = null,
    /**
     * *Optional*. *True*, if the caption must be shown above the message media
     */
    @SerialName("show_caption_above_media")
    public val showCaptionAboveMedia: Boolean? = null,
    /**
     * *Optional*. *True*, if the message media is covered by a spoiler animation
     */
    @SerialName("has_media_spoiler")
    public val hasMediaSpoiler: Boolean? = null,
    /**
     * *Optional*. Message is a checklist
     */
    public val checklist: JsonElement? = null,
    /**
     * *Optional*. Message is a shared contact, information about the contact
     */
    public val contact: JsonElement? = null,
    /**
     * *Optional*. Message is a dice with random value
     */
    public val dice: JsonElement? = null,
    /**
     * *Optional*. Message is a game, information about the game. [More about games ](https://core.telegram.org/bots/api#games)
     */
    public val game: JsonElement? = null,
    /**
     * *Optional*. Message is a native poll, information about the poll
     */
    public val poll: JsonElement? = null,
    /**
     * *Optional*. Message is a venue, information about the venue. For backward compatibility, when this field is set, the *location* field will also be set
     */
    public val venue: JsonElement? = null,
    /**
     * *Optional*. Message is a shared location, information about the location
     */
    public val location: JsonElement? = null,
    /**
     * *Optional*. New members that were added to the group or supergroup and information about them (the bot itself may be one of these members)
     */
    @SerialName("new_chat_members")
    public val newChatMembers: List<User>? = null,
    /**
     * *Optional*. A member was removed from the group, information about them (this member may be the bot itself)
     */
    @SerialName("left_chat_member")
    public val leftChatMember: JsonElement? = null,
    /**
     * *Optional*. A chat title was changed to this value
     */
    @SerialName("new_chat_title")
    public val newChatTitle: String? = null,
    /**
     * *Optional*. A chat photo was change to this value
     */
    @SerialName("new_chat_photo")
    public val newChatPhoto: List<PhotoSize>? = null,
    /**
     * *Optional*. Service message: the chat photo was deleted
     */
    @SerialName("delete_chat_photo")
    public val deleteChatPhoto: Boolean? = null,
    /**
     * *Optional*. Service message: the group has been created
     */
    @SerialName("group_chat_created")
    public val groupChatCreated: Boolean? = null,
    /**
     * *Optional*. Service message: the supergroup has been created. This field can't be received in a message coming through updates, because bot can't be a member of a supergroup when it is created. It can only be found in reply_to_message if someone replies to a very first message in a directly created supergroup.
     */
    @SerialName("supergroup_chat_created")
    public val supergroupChatCreated: Boolean? = null,
    /**
     * *Optional*. Service message: the channel has been created. This field can't be received in a message coming through updates, because bot can't be a member of a channel when it is created. It can only be found in reply_to_message if someone replies to a very first message in a channel.
     */
    @SerialName("channel_chat_created")
    public val channelChatCreated: Boolean? = null,
    /**
     * *Optional*. Service message: auto-delete timer settings changed in the chat
     */
    @SerialName("message_auto_delete_timer_changed")
    public val messageAutoDeleteTimerChanged: JsonElement? = null,
    /**
     * *Optional*. The group has been migrated to a supergroup with the specified identifier. This number may have more than 32 significant bits and some programming languages may have difficulty/silent defects in interpreting it. But it has at most 52 significant bits, so a signed 64-bit integer or double-precision float type are safe for storing this identifier.
     */
    @SerialName("migrate_to_chat_id")
    public val migrateToChatId: Long? = null,
    /**
     * *Optional*. The supergroup has been migrated from a group with the specified identifier. This number may have more than 32 significant bits and some programming languages may have difficulty/silent defects in interpreting it. But it has at most 52 significant bits, so a signed 64-bit integer or double-precision float type are safe for storing this identifier.
     */
    @SerialName("migrate_from_chat_id")
    public val migrateFromChatId: Long? = null,
    /**
     * *Optional*. Specified message was pinned. Note that the [Message](https://core.telegram.org/bots/api#message) object in this field will not contain further *reply_to_message* fields even if it itself is a reply.
     */
    @SerialName("pinned_message")
    public val pinnedMessage: JsonElement? = null,
    /**
     * *Optional*. Message is an invoice for a [payment](https://core.telegram.org/bots/api#payments), information about the invoice. [More about payments ](https://core.telegram.org/bots/api#payments)
     */
    public val invoice: JsonElement? = null,
    /**
     * *Optional*. Message is a service message about a successful payment, information about the payment. [More about payments ](https://core.telegram.org/bots/api#payments)
     */
    @SerialName("successful_payment")
    public val successfulPayment: JsonElement? = null,
    /**
     * *Optional*. Message is a service message about a refunded payment, information about the payment. [More about payments ](https://core.telegram.org/bots/api#payments)
     */
    @SerialName("refunded_payment")
    public val refundedPayment: JsonElement? = null,
    /**
     * *Optional*. Service message: users were shared with the bot
     */
    @SerialName("users_shared")
    public val usersShared: JsonElement? = null,
    /**
     * *Optional*. Service message: a chat was shared with the bot
     */
    @SerialName("chat_shared")
    public val chatShared: JsonElement? = null,
    /**
     * *Optional*. Service message: a regular gift was sent or received
     */
    public val gift: JsonElement? = null,
    /**
     * *Optional*. Service message: a unique gift was sent or received
     */
    @SerialName("unique_gift")
    public val uniqueGift: JsonElement? = null,
    /**
     * *Optional*. Service message: upgrade of a gift was purchased after the gift was sent
     */
    @SerialName("gift_upgrade_sent")
    public val giftUpgradeSent: JsonElement? = null,
    /**
     * *Optional*. The domain name of the website on which the user has logged in. [More about Telegram Login ](https://core.telegram.org/widgets/login)
     */
    @SerialName("connected_website")
    public val connectedWebsite: String? = null,
    /**
     * *Optional*. Service message: the user allowed the bot to write messages after adding it to the attachment or side menu, launching a Web App from a link, or accepting an explicit request from a Web App sent by the method [requestWriteAccess](https://core.telegram.org/bots/webapps#initializing-mini-apps)
     */
    @SerialName("write_access_allowed")
    public val writeAccessAllowed: JsonElement? = null,
    /**
     * *Optional*. Telegram Passport data
     */
    @SerialName("passport_data")
    public val passportData: JsonElement? = null,
    /**
     * *Optional*. Service message. A user in the chat triggered another user's proximity alert while sharing Live Location.
     */
    @SerialName("proximity_alert_triggered")
    public val proximityAlertTriggered: JsonElement? = null,
    /**
     * *Optional*. Service message: user boosted the chat
     */
    @SerialName("boost_added")
    public val boostAdded: JsonElement? = null,
    /**
     * *Optional*. Service message: chat background set
     */
    @SerialName("chat_background_set")
    public val chatBackgroundSet: JsonElement? = null,
    /**
     * *Optional*. Service message: some tasks in a checklist were marked as done or not done
     */
    @SerialName("checklist_tasks_done")
    public val checklistTasksDone: JsonElement? = null,
    /**
     * *Optional*. Service message: tasks were added to a checklist
     */
    @SerialName("checklist_tasks_added")
    public val checklistTasksAdded: JsonElement? = null,
    /**
     * *Optional*. Service message: the price for paid messages in the corresponding direct messages chat of a channel has changed
     */
    @SerialName("direct_message_price_changed")
    public val directMessagePriceChanged: JsonElement? = null,
    /**
     * *Optional*. Service message: forum topic created
     */
    @SerialName("forum_topic_created")
    public val forumTopicCreated: JsonElement? = null,
    /**
     * *Optional*. Service message: forum topic edited
     */
    @SerialName("forum_topic_edited")
    public val forumTopicEdited: JsonElement? = null,
    /**
     * *Optional*. Service message: forum topic closed
     */
    @SerialName("forum_topic_closed")
    public val forumTopicClosed: JsonElement? = null,
    /**
     * *Optional*. Service message: forum topic reopened
     */
    @SerialName("forum_topic_reopened")
    public val forumTopicReopened: JsonElement? = null,
    /**
     * *Optional*. Service message: the 'General' forum topic hidden
     */
    @SerialName("general_forum_topic_hidden")
    public val generalForumTopicHidden: JsonElement? = null,
    /**
     * *Optional*. Service message: the 'General' forum topic unhidden
     */
    @SerialName("general_forum_topic_unhidden")
    public val generalForumTopicUnhidden: JsonElement? = null,
    /**
     * *Optional*. Service message: a scheduled giveaway was created
     */
    @SerialName("giveaway_created")
    public val giveawayCreated: JsonElement? = null,
    /**
     * *Optional*. The message is a scheduled giveaway message
     */
    public val giveaway: JsonElement? = null,
    /**
     * *Optional*. A giveaway with public winners was completed
     */
    @SerialName("giveaway_winners")
    public val giveawayWinners: JsonElement? = null,
    /**
     * *Optional*. Service message: a giveaway without public winners was completed
     */
    @SerialName("giveaway_completed")
    public val giveawayCompleted: JsonElement? = null,
    /**
     * *Optional*. Service message: the price for paid messages has changed in the chat
     */
    @SerialName("paid_message_price_changed")
    public val paidMessagePriceChanged: JsonElement? = null,
    /**
     * *Optional*. Service message: a suggested post was approved
     */
    @SerialName("suggested_post_approved")
    public val suggestedPostApproved: JsonElement? = null,
    /**
     * *Optional*. Service message: approval of a suggested post has failed
     */
    @SerialName("suggested_post_approval_failed")
    public val suggestedPostApprovalFailed: JsonElement? = null,
    /**
     * *Optional*. Service message: a suggested post was declined
     */
    @SerialName("suggested_post_declined")
    public val suggestedPostDeclined: JsonElement? = null,
    /**
     * *Optional*. Service message: payment for a suggested post was received
     */
    @SerialName("suggested_post_paid")
    public val suggestedPostPaid: JsonElement? = null,
    /**
     * *Optional*. Service message: payment for a suggested post was refunded
     */
    @SerialName("suggested_post_refunded")
    public val suggestedPostRefunded: JsonElement? = null,
    /**
     * *Optional*. Service message: video chat scheduled
     */
    @SerialName("video_chat_scheduled")
    public val videoChatScheduled: JsonElement? = null,
    /**
     * *Optional*. Service message: video chat started
     */
    @SerialName("video_chat_started")
    public val videoChatStarted: JsonElement? = null,
    /**
     * *Optional*. Service message: video chat ended
     */
    @SerialName("video_chat_ended")
    public val videoChatEnded: JsonElement? = null,
    /**
     * *Optional*. Service message: new participants invited to a video chat
     */
    @SerialName("video_chat_participants_invited")
    public val videoChatParticipantsInvited: JsonElement? = null,
    /**
     * *Optional*. Service message: data sent by a Web App
     */
    @SerialName("web_app_data")
    public val webAppData: JsonElement? = null,
    /**
     * *Optional*. Inline keyboard attached to the message. `login_url` buttons are represented as ordinary `url` buttons.
     */
    @SerialName("reply_markup")
    public val replyMarkup: JsonElement? = null,
)
