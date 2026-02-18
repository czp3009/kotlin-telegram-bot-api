// Auto-generated from Swagger specification, do not modify this file manually
@file:Suppress(
    "RedundantVisibilityModifier",
    "unused",
    "GrazieStyle",
    "GrazieInspection",
)

package com.hiczp.telegram.bot.protocol

import com.hiczp.telegram.bot.protocol.model.AnswerCallbackQueryRequest
import com.hiczp.telegram.bot.protocol.model.AnswerInlineQueryRequest
import com.hiczp.telegram.bot.protocol.model.AnswerPreCheckoutQueryRequest
import com.hiczp.telegram.bot.protocol.model.AnswerShippingQueryRequest
import com.hiczp.telegram.bot.protocol.model.AnswerWebAppQueryRequest
import com.hiczp.telegram.bot.protocol.model.ApproveChatJoinRequestRequest
import com.hiczp.telegram.bot.protocol.model.ApproveSuggestedPostRequest
import com.hiczp.telegram.bot.protocol.model.BanChatMemberRequest
import com.hiczp.telegram.bot.protocol.model.BanChatSenderChatRequest
import com.hiczp.telegram.bot.protocol.model.BotCommand
import com.hiczp.telegram.bot.protocol.model.BotDescription
import com.hiczp.telegram.bot.protocol.model.BotName
import com.hiczp.telegram.bot.protocol.model.BotShortDescription
import com.hiczp.telegram.bot.protocol.model.BusinessConnection
import com.hiczp.telegram.bot.protocol.model.ChatAdministratorRights
import com.hiczp.telegram.bot.protocol.model.ChatFullInfo
import com.hiczp.telegram.bot.protocol.model.ChatInviteLink
import com.hiczp.telegram.bot.protocol.model.ChatMember
import com.hiczp.telegram.bot.protocol.model.CloseForumTopicRequest
import com.hiczp.telegram.bot.protocol.model.CloseGeneralForumTopicRequest
import com.hiczp.telegram.bot.protocol.model.ConvertGiftToStarsRequest
import com.hiczp.telegram.bot.protocol.model.CopyMessageRequest
import com.hiczp.telegram.bot.protocol.model.CopyMessagesRequest
import com.hiczp.telegram.bot.protocol.model.CreateChatInviteLinkRequest
import com.hiczp.telegram.bot.protocol.model.CreateChatSubscriptionInviteLinkRequest
import com.hiczp.telegram.bot.protocol.model.CreateForumTopicRequest
import com.hiczp.telegram.bot.protocol.model.CreateInvoiceLinkRequest
import com.hiczp.telegram.bot.protocol.model.DeclineChatJoinRequestRequest
import com.hiczp.telegram.bot.protocol.model.DeclineSuggestedPostRequest
import com.hiczp.telegram.bot.protocol.model.DeleteBusinessMessagesRequest
import com.hiczp.telegram.bot.protocol.model.DeleteChatPhotoRequest
import com.hiczp.telegram.bot.protocol.model.DeleteChatStickerSetRequest
import com.hiczp.telegram.bot.protocol.model.DeleteForumTopicRequest
import com.hiczp.telegram.bot.protocol.model.DeleteMessageRequest
import com.hiczp.telegram.bot.protocol.model.DeleteMessagesRequest
import com.hiczp.telegram.bot.protocol.model.DeleteMyCommandsRequest
import com.hiczp.telegram.bot.protocol.model.DeleteStickerFromSetRequest
import com.hiczp.telegram.bot.protocol.model.DeleteStickerSetRequest
import com.hiczp.telegram.bot.protocol.model.DeleteStoryRequest
import com.hiczp.telegram.bot.protocol.model.DeleteWebhookRequest
import com.hiczp.telegram.bot.protocol.model.EditChatInviteLinkRequest
import com.hiczp.telegram.bot.protocol.model.EditChatSubscriptionInviteLinkRequest
import com.hiczp.telegram.bot.protocol.model.EditForumTopicRequest
import com.hiczp.telegram.bot.protocol.model.EditGeneralForumTopicRequest
import com.hiczp.telegram.bot.protocol.model.EditMessageCaptionRequest
import com.hiczp.telegram.bot.protocol.model.EditMessageChecklistRequest
import com.hiczp.telegram.bot.protocol.model.EditMessageLiveLocationRequest
import com.hiczp.telegram.bot.protocol.model.EditMessageReplyMarkupRequest
import com.hiczp.telegram.bot.protocol.model.EditMessageTextRequest
import com.hiczp.telegram.bot.protocol.model.EditUserStarSubscriptionRequest
import com.hiczp.telegram.bot.protocol.model.ExportChatInviteLinkRequest
import com.hiczp.telegram.bot.protocol.model.File
import com.hiczp.telegram.bot.protocol.model.ForumTopic
import com.hiczp.telegram.bot.protocol.model.ForwardMessageRequest
import com.hiczp.telegram.bot.protocol.model.ForwardMessagesRequest
import com.hiczp.telegram.bot.protocol.model.GameHighScore
import com.hiczp.telegram.bot.protocol.model.GiftPremiumSubscriptionRequest
import com.hiczp.telegram.bot.protocol.model.Gifts
import com.hiczp.telegram.bot.protocol.model.HideGeneralForumTopicRequest
import com.hiczp.telegram.bot.protocol.model.LeaveChatRequest
import com.hiczp.telegram.bot.protocol.model.MenuButton
import com.hiczp.telegram.bot.protocol.model.Message
import com.hiczp.telegram.bot.protocol.model.MessageId
import com.hiczp.telegram.bot.protocol.model.OwnedGifts
import com.hiczp.telegram.bot.protocol.model.PinChatMessageRequest
import com.hiczp.telegram.bot.protocol.model.Poll
import com.hiczp.telegram.bot.protocol.model.PreparedInlineMessage
import com.hiczp.telegram.bot.protocol.model.PromoteChatMemberRequest
import com.hiczp.telegram.bot.protocol.model.ReadBusinessMessageRequest
import com.hiczp.telegram.bot.protocol.model.RefundStarPaymentRequest
import com.hiczp.telegram.bot.protocol.model.RemoveBusinessAccountProfilePhotoRequest
import com.hiczp.telegram.bot.protocol.model.RemoveChatVerificationRequest
import com.hiczp.telegram.bot.protocol.model.RemoveUserVerificationRequest
import com.hiczp.telegram.bot.protocol.model.ReopenForumTopicRequest
import com.hiczp.telegram.bot.protocol.model.ReopenGeneralForumTopicRequest
import com.hiczp.telegram.bot.protocol.model.RepostStoryRequest
import com.hiczp.telegram.bot.protocol.model.RestrictChatMemberRequest
import com.hiczp.telegram.bot.protocol.model.RevokeChatInviteLinkRequest
import com.hiczp.telegram.bot.protocol.model.SavePreparedInlineMessageRequest
import com.hiczp.telegram.bot.protocol.model.SendChatActionRequest
import com.hiczp.telegram.bot.protocol.model.SendChecklistRequest
import com.hiczp.telegram.bot.protocol.model.SendContactRequest
import com.hiczp.telegram.bot.protocol.model.SendDiceRequest
import com.hiczp.telegram.bot.protocol.model.SendGameRequest
import com.hiczp.telegram.bot.protocol.model.SendGiftRequest
import com.hiczp.telegram.bot.protocol.model.SendInvoiceRequest
import com.hiczp.telegram.bot.protocol.model.SendLocationRequest
import com.hiczp.telegram.bot.protocol.model.SendMessageDraftRequest
import com.hiczp.telegram.bot.protocol.model.SendMessageRequest
import com.hiczp.telegram.bot.protocol.model.SendPollRequest
import com.hiczp.telegram.bot.protocol.model.SendVenueRequest
import com.hiczp.telegram.bot.protocol.model.SentWebAppMessage
import com.hiczp.telegram.bot.protocol.model.SetBusinessAccountBioRequest
import com.hiczp.telegram.bot.protocol.model.SetBusinessAccountGiftSettingsRequest
import com.hiczp.telegram.bot.protocol.model.SetBusinessAccountNameRequest
import com.hiczp.telegram.bot.protocol.model.SetBusinessAccountUsernameRequest
import com.hiczp.telegram.bot.protocol.model.SetChatAdministratorCustomTitleRequest
import com.hiczp.telegram.bot.protocol.model.SetChatDescriptionRequest
import com.hiczp.telegram.bot.protocol.model.SetChatMenuButtonRequest
import com.hiczp.telegram.bot.protocol.model.SetChatPermissionsRequest
import com.hiczp.telegram.bot.protocol.model.SetChatStickerSetRequest
import com.hiczp.telegram.bot.protocol.model.SetChatTitleRequest
import com.hiczp.telegram.bot.protocol.model.SetCustomEmojiStickerSetThumbnailRequest
import com.hiczp.telegram.bot.protocol.model.SetGameScoreRequest
import com.hiczp.telegram.bot.protocol.model.SetMessageReactionRequest
import com.hiczp.telegram.bot.protocol.model.SetMyCommandsRequest
import com.hiczp.telegram.bot.protocol.model.SetMyDefaultAdministratorRightsRequest
import com.hiczp.telegram.bot.protocol.model.SetMyDescriptionRequest
import com.hiczp.telegram.bot.protocol.model.SetMyNameRequest
import com.hiczp.telegram.bot.protocol.model.SetMyShortDescriptionRequest
import com.hiczp.telegram.bot.protocol.model.SetPassportDataErrorsRequest
import com.hiczp.telegram.bot.protocol.model.SetStickerEmojiListRequest
import com.hiczp.telegram.bot.protocol.model.SetStickerKeywordsRequest
import com.hiczp.telegram.bot.protocol.model.SetStickerMaskPositionRequest
import com.hiczp.telegram.bot.protocol.model.SetStickerPositionInSetRequest
import com.hiczp.telegram.bot.protocol.model.SetStickerSetTitleRequest
import com.hiczp.telegram.bot.protocol.model.SetUserEmojiStatusRequest
import com.hiczp.telegram.bot.protocol.model.StarAmount
import com.hiczp.telegram.bot.protocol.model.StarTransactions
import com.hiczp.telegram.bot.protocol.model.Sticker
import com.hiczp.telegram.bot.protocol.model.StickerSet
import com.hiczp.telegram.bot.protocol.model.StopMessageLiveLocationRequest
import com.hiczp.telegram.bot.protocol.model.StopPollRequest
import com.hiczp.telegram.bot.protocol.model.Story
import com.hiczp.telegram.bot.protocol.model.TransferBusinessAccountStarsRequest
import com.hiczp.telegram.bot.protocol.model.TransferGiftRequest
import com.hiczp.telegram.bot.protocol.model.UnbanChatMemberRequest
import com.hiczp.telegram.bot.protocol.model.UnbanChatSenderChatRequest
import com.hiczp.telegram.bot.protocol.model.UnhideGeneralForumTopicRequest
import com.hiczp.telegram.bot.protocol.model.UnpinAllChatMessagesRequest
import com.hiczp.telegram.bot.protocol.model.UnpinAllForumTopicMessagesRequest
import com.hiczp.telegram.bot.protocol.model.UnpinAllGeneralForumTopicMessagesRequest
import com.hiczp.telegram.bot.protocol.model.UnpinChatMessageRequest
import com.hiczp.telegram.bot.protocol.model.Update
import com.hiczp.telegram.bot.protocol.model.UpgradeGiftRequest
import com.hiczp.telegram.bot.protocol.model.User
import com.hiczp.telegram.bot.protocol.model.UserChatBoosts
import com.hiczp.telegram.bot.protocol.model.UserProfileAudios
import com.hiczp.telegram.bot.protocol.model.UserProfilePhotos
import com.hiczp.telegram.bot.protocol.model.VerifyChatRequest
import com.hiczp.telegram.bot.protocol.model.VerifyUserRequest
import com.hiczp.telegram.bot.protocol.model.WebhookInfo
import com.hiczp.telegram.bot.protocol.plugin.TelegramFileDownload
import com.hiczp.telegram.bot.protocol.type.TelegramResponse
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.Path
import de.jensklingenberg.ktorfit.http.Query
import de.jensklingenberg.ktorfit.http.Streaming
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.statement.HttpStatement
import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List

public interface TelegramBotApi {
    /**
     * Use this method to receive incoming updates using long polling (wiki). Returns an Array of Update objects.
     * Notes 1. This method will not work if an outgoing webhook is set up. 2. In order to avoid getting duplicate updates, recalculate offset after each server response.
     *
     * @param offset Identifier of the first update to be returned. Must be greater by one than the highest among the identifiers of previously received updates. By default, updates starting with the earliest unconfirmed update are returned. An update is considered confirmed as soon as [getUpdates](https://core.telegram.org/bots/api#getupdates) is called with an *offset* higher than its *update_id*. The negative offset can be specified to retrieve updates starting from *-offset* update from the end of the updates queue. All previous updates will be forgotten.
     * @param limit Limits the number of updates to be retrieved. Values between 1-100 are accepted. Defaults to 100.
     * @param timeout Timeout in seconds for long polling. Defaults to 0, i.e. usual short polling. Should be positive, short polling should be used for testing purposes only.
     * @param allowedUpdates A JSON-serialized list of the update types you want your bot to receive. For example, specify `["message", "edited_channel_post", "callback_query"]` to only receive updates of these types. See [Update](https://core.telegram.org/bots/api#update) for a complete list of available update types. Specify an empty list to receive all update types except *chat_member*, *message_reaction*, and *message_reaction_count* (default). If not specified, the previous setting will be used.Please note that this parameter doesn't affect updates created before the call to getUpdates, so unwanted updates may be received for a short period of time.
     */
    @GET("getUpdates")
    public suspend fun getUpdates(
        @Query offset: Long? = null,
        @Query limit: Long? = null,
        @Query timeout: Long? = null,
        @Query("allowed_updates") allowedUpdates: String? = null,
    ): TelegramResponse<List<Update>>

    /**
     * Use this method to specify a URL and receive incoming updates via an outgoing webhook. Whenever there is an update for the bot, we will send an HTTPS POST request to the specified URL, containing a JSON-serialized Update. In case of an unsuccessful request (a request with response HTTP status code different from 2XY), we will repeat the request and give up after a reasonable amount of attempts. Returns True on success.
     * If you'd like to make sure that the webhook was set by you, you can specify secret data in the parameter secret_token. If specified, the request will contain a header “X-Telegram-Bot-Api-Secret-Token” with the secret token as content.
     * Notes 1. You will not be able to receive updates using getUpdates for as long as an outgoing webhook is set up. 2. To use a self-signed certificate, you need to upload your public key certificate using certificate parameter. Please upload as InputFile, sending a String will not work. 3. Ports currently supported for webhooks: 443, 80, 88, 8443. If you're having any trouble setting up webhooks, please check out this amazing guide to webhooks.
     */
    @POST("setWebhook")
    public suspend fun setWebhook(@Body formData: MultiPartFormDataContent): TelegramResponse<Boolean>

    /**
     * Use this method to remove webhook integration if you decide to switch back to getUpdates. Returns True on success.
     */
    @POST("deleteWebhook")
    public suspend fun deleteWebhook(@Body body: DeleteWebhookRequest): TelegramResponse<Boolean>

    /**
     * Use this method to get current webhook status. Requires no parameters. On success, returns a WebhookInfo object. If the bot is using getUpdates, will return an object with the url field empty.
     */
    @GET("getWebhookInfo")
    public suspend fun getWebhookInfo(): TelegramResponse<WebhookInfo>

    /**
     * A simple method for testing your bot's authentication token. Requires no parameters. Returns basic information about the bot in form of a User object.
     */
    @GET("getMe")
    public suspend fun getMe(): TelegramResponse<User>

    /**
     * Use this method to log out from the cloud Bot API server before launching the bot locally. You must log out the bot before running it locally, otherwise there is no guarantee that the bot will receive updates. After a successful call, you can immediately log in on a local server, but will not be able to log in back to the cloud Bot API server for 10 minutes. Returns True on success. Requires no parameters.
     */
    @POST("logOut")
    public suspend fun logOut(): TelegramResponse<Boolean>

    /**
     * Use this method to close the bot instance before moving it from one local server to another. You need to delete the webhook before calling this method to ensure that the bot isn't launched again after server restart. The method will return error 429 in the first 10 minutes after the bot is launched. Returns True on success. Requires no parameters.
     */
    @POST("close")
    public suspend fun close(): TelegramResponse<Boolean>

    /**
     * Use this method to send text messages. On success, the sent Message is returned.
     */
    @POST("sendMessage")
    public suspend fun sendMessage(@Body body: SendMessageRequest): TelegramResponse<Message>

    /**
     * Use this method to forward messages of any kind. Service messages and messages with protected content can't be forwarded. On success, the sent Message is returned.
     */
    @POST("forwardMessage")
    public suspend fun forwardMessage(@Body body: ForwardMessageRequest): TelegramResponse<Message>

    /**
     * Use this method to forward multiple messages of any kind. If some of the specified messages can't be found or forwarded, they are skipped. Service messages and messages with protected content can't be forwarded. Album grouping is kept for forwarded messages. On success, an array of MessageId of the sent messages is returned.
     */
    @POST("forwardMessages")
    public suspend fun forwardMessages(@Body body: ForwardMessagesRequest): TelegramResponse<List<MessageId>>

    /**
     * Use this method to copy messages of any kind. Service messages, paid media messages, giveaway messages, giveaway winners messages, and invoice messages can't be copied. A quiz poll can be copied only if the value of the field correct_option_id is known to the bot. The method is analogous to the method forwardMessage, but the copied message doesn't have a link to the original message. Returns the MessageId of the sent message on success.
     */
    @POST("copyMessage")
    public suspend fun copyMessage(@Body body: CopyMessageRequest): TelegramResponse<MessageId>

    /**
     * Use this method to copy messages of any kind. If some of the specified messages can't be found or copied, they are skipped. Service messages, paid media messages, giveaway messages, giveaway winners messages, and invoice messages can't be copied. A quiz poll can be copied only if the value of the field correct_option_id is known to the bot. The method is analogous to the method forwardMessages, but the copied messages don't have a link to the original message. Album grouping is kept for copied messages. On success, an array of MessageId of the sent messages is returned.
     */
    @POST("copyMessages")
    public suspend fun copyMessages(@Body body: CopyMessagesRequest): TelegramResponse<List<MessageId>>

    /**
     * Use this method to send photos. On success, the sent Message is returned.
     */
    @POST("sendPhoto")
    public suspend fun sendPhoto(@Body formData: MultiPartFormDataContent): TelegramResponse<Message>

    /**
     * Use this method to send audio files, if you want Telegram clients to display them in the music player. Your audio must be in the .MP3 or .M4A format. On success, the sent Message is returned. Bots can currently send audio files of up to 50 MB in size, this limit may be changed in the future.
     * For sending voice messages, use the sendVoice method instead.
     */
    @POST("sendAudio")
    public suspend fun sendAudio(@Body formData: MultiPartFormDataContent): TelegramResponse<Message>

    /**
     * Use this method to send general files. On success, the sent Message is returned. Bots can currently send files of any type of up to 50 MB in size, this limit may be changed in the future.
     */
    @POST("sendDocument")
    public suspend fun sendDocument(@Body formData: MultiPartFormDataContent): TelegramResponse<Message>

    /**
     * Use this method to send video files, Telegram clients support MPEG4 videos (other formats may be sent as Document). On success, the sent Message is returned. Bots can currently send video files of up to 50 MB in size, this limit may be changed in the future.
     */
    @POST("sendVideo")
    public suspend fun sendVideo(@Body formData: MultiPartFormDataContent): TelegramResponse<Message>

    /**
     * Use this method to send animation files (GIF or H.264/MPEG-4 AVC video without sound). On success, the sent Message is returned. Bots can currently send animation files of up to 50 MB in size, this limit may be changed in the future.
     */
    @POST("sendAnimation")
    public suspend fun sendAnimation(@Body formData: MultiPartFormDataContent): TelegramResponse<Message>

    /**
     * Use this method to send audio files, if you want Telegram clients to display the file as a playable voice message. For this to work, your audio must be in an .OGG file encoded with OPUS, or in .MP3 format, or in .M4A format (other formats may be sent as Audio or Document). On success, the sent Message is returned. Bots can currently send voice messages of up to 50 MB in size, this limit may be changed in the future.
     */
    @POST("sendVoice")
    public suspend fun sendVoice(@Body formData: MultiPartFormDataContent): TelegramResponse<Message>

    /**
     * As of v.4.0, Telegram clients support rounded square MPEG4 videos of up to 1 minute long. Use this method to send video messages. On success, the sent Message is returned.
     */
    @POST("sendVideoNote")
    public suspend fun sendVideoNote(@Body formData: MultiPartFormDataContent): TelegramResponse<Message>

    /**
     * Use this method to send paid media. On success, the sent Message is returned.
     */
    @POST("sendPaidMedia")
    public suspend fun sendPaidMedia(@Body formData: MultiPartFormDataContent): TelegramResponse<Message>

    /**
     * Use this method to send a group of photos, videos, documents or audios as an album. Documents and audio files can be only grouped in an album with messages of the same type. On success, an array of Message objects that were sent is returned.
     */
    @POST("sendMediaGroup")
    public suspend fun sendMediaGroup(@Body formData: MultiPartFormDataContent): TelegramResponse<List<Message>>

    /**
     * Use this method to send point on the map. On success, the sent Message is returned.
     */
    @POST("sendLocation")
    public suspend fun sendLocation(@Body body: SendLocationRequest): TelegramResponse<Message>

    /**
     * Use this method to send information about a venue. On success, the sent Message is returned.
     */
    @POST("sendVenue")
    public suspend fun sendVenue(@Body body: SendVenueRequest): TelegramResponse<Message>

    /**
     * Use this method to send phone contacts. On success, the sent Message is returned.
     */
    @POST("sendContact")
    public suspend fun sendContact(@Body body: SendContactRequest): TelegramResponse<Message>

    /**
     * Use this method to send a native poll. On success, the sent Message is returned.
     */
    @POST("sendPoll")
    public suspend fun sendPoll(@Body body: SendPollRequest): TelegramResponse<Message>

    /**
     * Use this method to send a checklist on behalf of a connected business account. On success, the sent Message is returned.
     */
    @POST("sendChecklist")
    public suspend fun sendChecklist(@Body body: SendChecklistRequest): TelegramResponse<Message>

    /**
     * Use this method to send an animated emoji that will display a random value. On success, the sent Message is returned.
     */
    @POST("sendDice")
    public suspend fun sendDice(@Body body: SendDiceRequest): TelegramResponse<Message>

    /**
     * Use this method to stream a partial message to a user while the message is being generated; supported only for bots with forum topic mode enabled. Returns True on success.
     */
    @POST("sendMessageDraft")
    public suspend fun sendMessageDraft(@Body body: SendMessageDraftRequest): TelegramResponse<Boolean>

    /**
     * Use this method when you need to tell the user that something is happening on the bot's side. The status is set for 5 seconds or less (when a message arrives from your bot, Telegram clients clear its typing status). Returns True on success.
     * Example: The ImageBot needs some time to process a request and upload the image. Instead of sending a text message along the lines of “Retrieving image, please wait…”, the bot may use sendChatAction with action = upload_photo. The user will see a “sending photo” status for the bot.
     * We only recommend using this method when a response from the bot will take a noticeable amount of time to arrive.
     */
    @POST("sendChatAction")
    public suspend fun sendChatAction(@Body body: SendChatActionRequest): TelegramResponse<Boolean>

    /**
     * Use this method to change the chosen reactions on a message. Service messages of some types can't be reacted to. Automatically forwarded messages from a channel to its discussion group have the same available reactions as messages in the channel. Bots can't use paid reactions. Returns True on success.
     */
    @POST("setMessageReaction")
    public suspend fun setMessageReaction(@Body body: SetMessageReactionRequest): TelegramResponse<Boolean>

    /**
     * Use this method to get a list of profile pictures for a user. Returns a UserProfilePhotos object.
     *
     * @param userId Unique identifier of the target user
     * @param offset Sequential number of the first photo to be returned. By default, all photos are returned.
     * @param limit Limits the number of photos to be retrieved. Values between 1-100 are accepted. Defaults to 100.
     */
    @GET("getUserProfilePhotos")
    public suspend fun getUserProfilePhotos(
        @Query("user_id") userId: Long,
        @Query offset: Long? = null,
        @Query limit: Long? = null,
    ): TelegramResponse<UserProfilePhotos>

    /**
     * Use this method to get a list of profile audios for a user. Returns a UserProfileAudios object.
     *
     * @param userId Unique identifier of the target user
     * @param offset Sequential number of the first audio to be returned. By default, all audios are returned.
     * @param limit Limits the number of audios to be retrieved. Values between 1-100 are accepted. Defaults to 100.
     */
    @GET("getUserProfileAudios")
    public suspend fun getUserProfileAudios(
        @Query("user_id") userId: Long,
        @Query offset: Long? = null,
        @Query limit: Long? = null,
    ): TelegramResponse<UserProfileAudios>

    /**
     * Changes the emoji status for a given user that previously allowed the bot to manage their emoji status via the Mini App method requestEmojiStatusAccess. Returns True on success.
     */
    @POST("setUserEmojiStatus")
    public suspend fun setUserEmojiStatus(@Body body: SetUserEmojiStatusRequest): TelegramResponse<Boolean>

    /**
     * Use this method to get basic information about a file and prepare it for downloading. For the moment, bots can download files of up to 20MB in size. On success, a File object is returned. The file can then be downloaded via the link https://api.telegram.org/file/bot<token>/<file_path>, where <file_path> is taken from the response. It is guaranteed that the link will be valid for at least 1 hour. When the link expires, a new one can be requested by calling getFile again.
     * Note: This function may not preserve the original file name and MIME type. You should save the file's MIME type and name (if available) when the File object is received.
     *
     * @param fileId File identifier to get information about
     */
    @GET("getFile")
    public suspend fun getFile(@Query("file_id") fileId: String): TelegramResponse<File>

    /**
     * Use this method to ban a user in a group, a supergroup or a channel. In the case of supergroups and channels, the user will not be able to return to the chat on their own using invite links, etc., unless unbanned first. The bot must be an administrator in the chat for this to work and must have the appropriate administrator rights. Returns True on success.
     */
    @POST("banChatMember")
    public suspend fun banChatMember(@Body body: BanChatMemberRequest): TelegramResponse<Boolean>

    /**
     * Use this method to unban a previously banned user in a supergroup or channel. The user will not return to the group or channel automatically, but will be able to join via link, etc. The bot must be an administrator for this to work. By default, this method guarantees that after the call the user is not a member of the chat, but will be able to join it. So if the user is a member of the chat they will also be removed from the chat. If you don't want this, use the parameter only_if_banned. Returns True on success.
     */
    @POST("unbanChatMember")
    public suspend fun unbanChatMember(@Body body: UnbanChatMemberRequest): TelegramResponse<Boolean>

    /**
     * Use this method to restrict a user in a supergroup. The bot must be an administrator in the supergroup for this to work and must have the appropriate administrator rights. Pass True for all permissions to lift restrictions from a user. Returns True on success.
     */
    @POST("restrictChatMember")
    public suspend fun restrictChatMember(@Body body: RestrictChatMemberRequest): TelegramResponse<Boolean>

    /**
     * Use this method to promote or demote a user in a supergroup or a channel. The bot must be an administrator in the chat for this to work and must have the appropriate administrator rights. Pass False for all boolean parameters to demote a user. Returns True on success.
     */
    @POST("promoteChatMember")
    public suspend fun promoteChatMember(@Body body: PromoteChatMemberRequest): TelegramResponse<Boolean>

    /**
     * Use this method to set a custom title for an administrator in a supergroup promoted by the bot. Returns True on success.
     */
    @POST("setChatAdministratorCustomTitle")
    public suspend fun setChatAdministratorCustomTitle(@Body body: SetChatAdministratorCustomTitleRequest): TelegramResponse<Boolean>

    /**
     * Use this method to ban a channel chat in a supergroup or a channel. Until the chat is unbanned, the owner of the banned chat won't be able to send messages on behalf of any of their channels. The bot must be an administrator in the supergroup or channel for this to work and must have the appropriate administrator rights. Returns True on success.
     */
    @POST("banChatSenderChat")
    public suspend fun banChatSenderChat(@Body body: BanChatSenderChatRequest): TelegramResponse<Boolean>

    /**
     * Use this method to unban a previously banned channel chat in a supergroup or channel. The bot must be an administrator for this to work and must have the appropriate administrator rights. Returns True on success.
     */
    @POST("unbanChatSenderChat")
    public suspend fun unbanChatSenderChat(@Body body: UnbanChatSenderChatRequest): TelegramResponse<Boolean>

    /**
     * Use this method to set default chat permissions for all members. The bot must be an administrator in the group or a supergroup for this to work and must have the can_restrict_members administrator rights. Returns True on success.
     */
    @POST("setChatPermissions")
    public suspend fun setChatPermissions(@Body body: SetChatPermissionsRequest): TelegramResponse<Boolean>

    /**
     * Use this method to generate a new primary invite link for a chat; any previously generated primary link is revoked. The bot must be an administrator in the chat for this to work and must have the appropriate administrator rights. Returns the new invite link as String on success.
     * Note: Each administrator in a chat generates their own invite links. Bots can't use invite links generated by other administrators. If you want your bot to work with invite links, it will need to generate its own link using exportChatInviteLink or by calling the getChat method. If your bot needs to generate a new primary invite link replacing its previous one, use exportChatInviteLink again.
     */
    @POST("exportChatInviteLink")
    public suspend fun exportChatInviteLink(@Body body: ExportChatInviteLinkRequest): TelegramResponse<String>

    /**
     * Use this method to create an additional invite link for a chat. The bot must be an administrator in the chat for this to work and must have the appropriate administrator rights. The link can be revoked using the method revokeChatInviteLink. Returns the new invite link as ChatInviteLink object.
     */
    @POST("createChatInviteLink")
    public suspend fun createChatInviteLink(@Body body: CreateChatInviteLinkRequest): TelegramResponse<ChatInviteLink>

    /**
     * Use this method to edit a non-primary invite link created by the bot. The bot must be an administrator in the chat for this to work and must have the appropriate administrator rights. Returns the edited invite link as a ChatInviteLink object.
     */
    @POST("editChatInviteLink")
    public suspend fun editChatInviteLink(@Body body: EditChatInviteLinkRequest): TelegramResponse<ChatInviteLink>

    /**
     * Use this method to create a subscription invite link for a channel chat. The bot must have the can_invite_users administrator rights. The link can be edited using the method editChatSubscriptionInviteLink or revoked using the method revokeChatInviteLink. Returns the new invite link as a ChatInviteLink object.
     */
    @POST("createChatSubscriptionInviteLink")
    public suspend fun createChatSubscriptionInviteLink(@Body body: CreateChatSubscriptionInviteLinkRequest): TelegramResponse<ChatInviteLink>

    /**
     * Use this method to edit a subscription invite link created by the bot. The bot must have the can_invite_users administrator rights. Returns the edited invite link as a ChatInviteLink object.
     */
    @POST("editChatSubscriptionInviteLink")
    public suspend fun editChatSubscriptionInviteLink(@Body body: EditChatSubscriptionInviteLinkRequest): TelegramResponse<ChatInviteLink>

    /**
     * Use this method to revoke an invite link created by the bot. If the primary link is revoked, a new link is automatically generated. The bot must be an administrator in the chat for this to work and must have the appropriate administrator rights. Returns the revoked invite link as ChatInviteLink object.
     */
    @POST("revokeChatInviteLink")
    public suspend fun revokeChatInviteLink(@Body body: RevokeChatInviteLinkRequest): TelegramResponse<ChatInviteLink>

    /**
     * Use this method to approve a chat join request. The bot must be an administrator in the chat for this to work and must have the can_invite_users administrator right. Returns True on success.
     */
    @POST("approveChatJoinRequest")
    public suspend fun approveChatJoinRequest(@Body body: ApproveChatJoinRequestRequest): TelegramResponse<Boolean>

    /**
     * Use this method to decline a chat join request. The bot must be an administrator in the chat for this to work and must have the can_invite_users administrator right. Returns True on success.
     */
    @POST("declineChatJoinRequest")
    public suspend fun declineChatJoinRequest(@Body body: DeclineChatJoinRequestRequest): TelegramResponse<Boolean>

    /**
     * Use this method to set a new profile photo for the chat. Photos can't be changed for private chats. The bot must be an administrator in the chat for this to work and must have the appropriate administrator rights. Returns True on success.
     */
    @POST("setChatPhoto")
    public suspend fun setChatPhoto(@Body formData: MultiPartFormDataContent): TelegramResponse<Boolean>

    /**
     * Use this method to delete a chat photo. Photos can't be changed for private chats. The bot must be an administrator in the chat for this to work and must have the appropriate administrator rights. Returns True on success.
     */
    @POST("deleteChatPhoto")
    public suspend fun deleteChatPhoto(@Body body: DeleteChatPhotoRequest): TelegramResponse<Boolean>

    /**
     * Use this method to change the title of a chat. Titles can't be changed for private chats. The bot must be an administrator in the chat for this to work and must have the appropriate administrator rights. Returns True on success.
     */
    @POST("setChatTitle")
    public suspend fun setChatTitle(@Body body: SetChatTitleRequest): TelegramResponse<Boolean>

    /**
     * Use this method to change the description of a group, a supergroup or a channel. The bot must be an administrator in the chat for this to work and must have the appropriate administrator rights. Returns True on success.
     */
    @POST("setChatDescription")
    public suspend fun setChatDescription(@Body body: SetChatDescriptionRequest): TelegramResponse<Boolean>

    /**
     * Use this method to add a message to the list of pinned messages in a chat. In private chats and channel direct messages chats, all non-service messages can be pinned. Conversely, the bot must be an administrator with the 'can_pin_messages' right or the 'can_edit_messages' right to pin messages in groups and channels respectively. Returns True on success.
     */
    @POST("pinChatMessage")
    public suspend fun pinChatMessage(@Body body: PinChatMessageRequest): TelegramResponse<Boolean>

    /**
     * Use this method to remove a message from the list of pinned messages in a chat. In private chats and channel direct messages chats, all messages can be unpinned. Conversely, the bot must be an administrator with the 'can_pin_messages' right or the 'can_edit_messages' right to unpin messages in groups and channels respectively. Returns True on success.
     */
    @POST("unpinChatMessage")
    public suspend fun unpinChatMessage(@Body body: UnpinChatMessageRequest): TelegramResponse<Boolean>

    /**
     * Use this method to clear the list of pinned messages in a chat. In private chats and channel direct messages chats, no additional rights are required to unpin all pinned messages. Conversely, the bot must be an administrator with the 'can_pin_messages' right or the 'can_edit_messages' right to unpin all pinned messages in groups and channels respectively. Returns True on success.
     */
    @POST("unpinAllChatMessages")
    public suspend fun unpinAllChatMessages(@Body body: UnpinAllChatMessagesRequest): TelegramResponse<Boolean>

    /**
     * Use this method for your bot to leave a group, supergroup or channel. Returns True on success.
     */
    @POST("leaveChat")
    public suspend fun leaveChat(@Body body: LeaveChatRequest): TelegramResponse<Boolean>

    /**
     * Use this method to get up-to-date information about the chat. Returns a ChatFullInfo object on success.
     *
     * @param chatId Unique identifier for the target chat or username of the target supergroup or channel (in the format `@channelusername`)
     */
    @GET("getChat")
    public suspend fun getChat(@Query("chat_id") chatId: String): TelegramResponse<ChatFullInfo>

    /**
     * Use this method to get a list of administrators in a chat, which aren't bots. Returns an Array of ChatMember objects.
     *
     * @param chatId Unique identifier for the target chat or username of the target supergroup or channel (in the format `@channelusername`)
     */
    @GET("getChatAdministrators")
    public suspend fun getChatAdministrators(@Query("chat_id") chatId: String): TelegramResponse<List<ChatMember>>

    /**
     * Use this method to get the number of members in a chat. Returns Int on success.
     *
     * @param chatId Unique identifier for the target chat or username of the target supergroup or channel (in the format `@channelusername`)
     */
    @GET("getChatMemberCount")
    public suspend fun getChatMemberCount(@Query("chat_id") chatId: String): TelegramResponse<Long>

    /**
     * Use this method to get information about a member of a chat. The method is only guaranteed to work for other users if the bot is an administrator in the chat. Returns a ChatMember object on success.
     *
     * @param chatId Unique identifier for the target chat or username of the target supergroup or channel (in the format `@channelusername`)
     * @param userId Unique identifier of the target user
     */
    @GET("getChatMember")
    public suspend fun getChatMember(@Query("chat_id") chatId: String, @Query("user_id") userId: Long): TelegramResponse<ChatMember>

    /**
     * Use this method to set a new group sticker set for a supergroup. The bot must be an administrator in the chat for this to work and must have the appropriate administrator rights. Use the field can_set_sticker_set optionally returned in getChat requests to check if the bot can use this method. Returns True on success.
     */
    @POST("setChatStickerSet")
    public suspend fun setChatStickerSet(@Body body: SetChatStickerSetRequest): TelegramResponse<Boolean>

    /**
     * Use this method to delete a group sticker set from a supergroup. The bot must be an administrator in the chat for this to work and must have the appropriate administrator rights. Use the field can_set_sticker_set optionally returned in getChat requests to check if the bot can use this method. Returns True on success.
     */
    @POST("deleteChatStickerSet")
    public suspend fun deleteChatStickerSet(@Body body: DeleteChatStickerSetRequest): TelegramResponse<Boolean>

    /**
     * Use this method to get custom emoji stickers, which can be used as a forum topic icon by any user. Requires no parameters. Returns an Array of Sticker objects.
     */
    @GET("getForumTopicIconStickers")
    public suspend fun getForumTopicIconStickers(): TelegramResponse<List<Sticker>>

    /**
     * Use this method to create a topic in a forum supergroup chat or a private chat with a user. In the case of a supergroup chat the bot must be an administrator in the chat for this to work and must have the can_manage_topics administrator right. Returns information about the created topic as a ForumTopic object.
     */
    @POST("createForumTopic")
    public suspend fun createForumTopic(@Body body: CreateForumTopicRequest): TelegramResponse<ForumTopic>

    /**
     * Use this method to edit name and icon of a topic in a forum supergroup chat or a private chat with a user. In the case of a supergroup chat the bot must be an administrator in the chat for this to work and must have the can_manage_topics administrator rights, unless it is the creator of the topic. Returns True on success.
     */
    @POST("editForumTopic")
    public suspend fun editForumTopic(@Body body: EditForumTopicRequest): TelegramResponse<Boolean>

    /**
     * Use this method to close an open topic in a forum supergroup chat. The bot must be an administrator in the chat for this to work and must have the can_manage_topics administrator rights, unless it is the creator of the topic. Returns True on success.
     */
    @POST("closeForumTopic")
    public suspend fun closeForumTopic(@Body body: CloseForumTopicRequest): TelegramResponse<Boolean>

    /**
     * Use this method to reopen a closed topic in a forum supergroup chat. The bot must be an administrator in the chat for this to work and must have the can_manage_topics administrator rights, unless it is the creator of the topic. Returns True on success.
     */
    @POST("reopenForumTopic")
    public suspend fun reopenForumTopic(@Body body: ReopenForumTopicRequest): TelegramResponse<Boolean>

    /**
     * Use this method to delete a forum topic along with all its messages in a forum supergroup chat or a private chat with a user. In the case of a supergroup chat the bot must be an administrator in the chat for this to work and must have the can_delete_messages administrator rights. Returns True on success.
     */
    @POST("deleteForumTopic")
    public suspend fun deleteForumTopic(@Body body: DeleteForumTopicRequest): TelegramResponse<Boolean>

    /**
     * Use this method to clear the list of pinned messages in a forum topic in a forum supergroup chat or a private chat with a user. In the case of a supergroup chat the bot must be an administrator in the chat for this to work and must have the can_pin_messages administrator right in the supergroup. Returns True on success.
     */
    @POST("unpinAllForumTopicMessages")
    public suspend fun unpinAllForumTopicMessages(@Body body: UnpinAllForumTopicMessagesRequest): TelegramResponse<Boolean>

    /**
     * Use this method to edit the name of the 'General' topic in a forum supergroup chat. The bot must be an administrator in the chat for this to work and must have the can_manage_topics administrator rights. Returns True on success.
     */
    @POST("editGeneralForumTopic")
    public suspend fun editGeneralForumTopic(@Body body: EditGeneralForumTopicRequest): TelegramResponse<Boolean>

    /**
     * Use this method to close an open 'General' topic in a forum supergroup chat. The bot must be an administrator in the chat for this to work and must have the can_manage_topics administrator rights. Returns True on success.
     */
    @POST("closeGeneralForumTopic")
    public suspend fun closeGeneralForumTopic(@Body body: CloseGeneralForumTopicRequest): TelegramResponse<Boolean>

    /**
     * Use this method to reopen a closed 'General' topic in a forum supergroup chat. The bot must be an administrator in the chat for this to work and must have the can_manage_topics administrator rights. The topic will be automatically unhidden if it was hidden. Returns True on success.
     */
    @POST("reopenGeneralForumTopic")
    public suspend fun reopenGeneralForumTopic(@Body body: ReopenGeneralForumTopicRequest): TelegramResponse<Boolean>

    /**
     * Use this method to hide the 'General' topic in a forum supergroup chat. The bot must be an administrator in the chat for this to work and must have the can_manage_topics administrator rights. The topic will be automatically closed if it was open. Returns True on success.
     */
    @POST("hideGeneralForumTopic")
    public suspend fun hideGeneralForumTopic(@Body body: HideGeneralForumTopicRequest): TelegramResponse<Boolean>

    /**
     * Use this method to unhide the 'General' topic in a forum supergroup chat. The bot must be an administrator in the chat for this to work and must have the can_manage_topics administrator rights. Returns True on success.
     */
    @POST("unhideGeneralForumTopic")
    public suspend fun unhideGeneralForumTopic(@Body body: UnhideGeneralForumTopicRequest): TelegramResponse<Boolean>

    /**
     * Use this method to clear the list of pinned messages in a General forum topic. The bot must be an administrator in the chat for this to work and must have the can_pin_messages administrator right in the supergroup. Returns True on success.
     */
    @POST("unpinAllGeneralForumTopicMessages")
    public suspend fun unpinAllGeneralForumTopicMessages(@Body body: UnpinAllGeneralForumTopicMessagesRequest): TelegramResponse<Boolean>

    /**
     * Use this method to send answers to callback queries sent from inline keyboards. The answer will be displayed to the user as a notification at the top of the chat screen or as an alert. On success, True is returned.
     * Alternatively, the user can be redirected to the specified Game URL. For this option to work, you must first create a game for your bot via @BotFather and accept the terms. Otherwise, you may use links like t.me/your_bot?start=XXXX that open your bot with a parameter.
     */
    @POST("answerCallbackQuery")
    public suspend fun answerCallbackQuery(@Body body: AnswerCallbackQueryRequest): TelegramResponse<Boolean>

    /**
     * Use this method to get the list of boosts added to a chat by a user. Requires administrator rights in the chat. Returns a UserChatBoosts object.
     *
     * @param chatId Unique identifier for the chat or username of the channel (in the format `@channelusername`)
     * @param userId Unique identifier of the target user
     */
    @GET("getUserChatBoosts")
    public suspend fun getUserChatBoosts(@Query("chat_id") chatId: String, @Query("user_id") userId: Long): TelegramResponse<UserChatBoosts>

    /**
     * Use this method to get information about the connection of the bot with a business account. Returns a BusinessConnection object on success.
     *
     * @param businessConnectionId Unique identifier of the business connection
     */
    @GET("getBusinessConnection")
    public suspend fun getBusinessConnection(@Query("business_connection_id") businessConnectionId: String): TelegramResponse<BusinessConnection>

    /**
     * Use this method to change the list of the bot's commands. See this manual for more details about bot commands. Returns True on success.
     */
    @POST("setMyCommands")
    public suspend fun setMyCommands(@Body body: SetMyCommandsRequest): TelegramResponse<Boolean>

    /**
     * Use this method to delete the list of the bot's commands for the given scope and user language. After deletion, higher level commands will be shown to affected users. Returns True on success.
     */
    @POST("deleteMyCommands")
    public suspend fun deleteMyCommands(@Body body: DeleteMyCommandsRequest): TelegramResponse<Boolean>

    /**
     * Use this method to get the current list of the bot's commands for the given scope and user language. Returns an Array of BotCommand objects. If commands aren't set, an empty list is returned.
     *
     * @param scope A JSON-serialized object, describing scope of users. Defaults to [BotCommandScopeDefault](https://core.telegram.org/bots/api#botcommandscopedefault).
     * @param languageCode A two-letter ISO 639-1 language code or an empty string
     */
    @GET("getMyCommands")
    public suspend fun getMyCommands(@Query scope: String? = null, @Query("language_code") languageCode: String? = null): TelegramResponse<List<BotCommand>>

    /**
     * Use this method to change the bot's name. Returns True on success.
     */
    @POST("setMyName")
    public suspend fun setMyName(@Body body: SetMyNameRequest): TelegramResponse<Boolean>

    /**
     * Use this method to get the current bot name for the given user language. Returns BotName on success.
     *
     * @param languageCode A two-letter ISO 639-1 language code or an empty string
     */
    @GET("getMyName")
    public suspend fun getMyName(@Query("language_code") languageCode: String? = null): TelegramResponse<BotName>

    /**
     * Use this method to change the bot's description, which is shown in the chat with the bot if the chat is empty. Returns True on success.
     */
    @POST("setMyDescription")
    public suspend fun setMyDescription(@Body body: SetMyDescriptionRequest): TelegramResponse<Boolean>

    /**
     * Use this method to get the current bot description for the given user language. Returns BotDescription on success.
     *
     * @param languageCode A two-letter ISO 639-1 language code or an empty string
     */
    @GET("getMyDescription")
    public suspend fun getMyDescription(@Query("language_code") languageCode: String? = null): TelegramResponse<BotDescription>

    /**
     * Use this method to change the bot's short description, which is shown on the bot's profile page and is sent together with the link when users share the bot. Returns True on success.
     */
    @POST("setMyShortDescription")
    public suspend fun setMyShortDescription(@Body body: SetMyShortDescriptionRequest): TelegramResponse<Boolean>

    /**
     * Use this method to get the current bot short description for the given user language. Returns BotShortDescription on success.
     *
     * @param languageCode A two-letter ISO 639-1 language code or an empty string
     */
    @GET("getMyShortDescription")
    public suspend fun getMyShortDescription(@Query("language_code") languageCode: String? = null): TelegramResponse<BotShortDescription>

    /**
     * Changes the profile photo of the bot. Returns True on success.
     */
    @POST("setMyProfilePhoto")
    public suspend fun setMyProfilePhoto(@Body formData: MultiPartFormDataContent): TelegramResponse<Boolean>

    /**
     * Removes the profile photo of the bot. Requires no parameters. Returns True on success.
     */
    @POST("removeMyProfilePhoto")
    public suspend fun removeMyProfilePhoto(): TelegramResponse<Boolean>

    /**
     * Use this method to change the bot's menu button in a private chat, or the default menu button. Returns True on success.
     */
    @POST("setChatMenuButton")
    public suspend fun setChatMenuButton(@Body body: SetChatMenuButtonRequest): TelegramResponse<Boolean>

    /**
     * Use this method to get the current value of the bot's menu button in a private chat, or the default menu button. Returns MenuButton on success.
     *
     * @param chatId Unique identifier for the target private chat. If not specified, default bot's menu button will be returned
     */
    @GET("getChatMenuButton")
    public suspend fun getChatMenuButton(@Query("chat_id") chatId: Long? = null): TelegramResponse<MenuButton>

    /**
     * Use this method to change the default administrator rights requested by the bot when it's added as an administrator to groups or channels. These rights will be suggested to users, but they are free to modify the list before adding the bot. Returns True on success.
     */
    @POST("setMyDefaultAdministratorRights")
    public suspend fun setMyDefaultAdministratorRights(@Body body: SetMyDefaultAdministratorRightsRequest): TelegramResponse<Boolean>

    /**
     * Use this method to get the current default administrator rights of the bot. Returns ChatAdministratorRights on success.
     *
     * @param forChannels Pass *True* to get default administrator rights of the bot in channels. Otherwise, default administrator rights of the bot for groups and supergroups will be returned.
     */
    @GET("getMyDefaultAdministratorRights")
    public suspend fun getMyDefaultAdministratorRights(@Query("for_channels") forChannels: Boolean? = null): TelegramResponse<ChatAdministratorRights>

    /**
     * Returns the list of gifts that can be sent by the bot to users and channel chats. Requires no parameters. Returns a Gifts object.
     */
    @GET("getAvailableGifts")
    public suspend fun getAvailableGifts(): TelegramResponse<Gifts>

    /**
     * Sends a gift to the given user or channel chat. The gift can't be converted to Telegram Stars by the receiver. Returns True on success.
     */
    @POST("sendGift")
    public suspend fun sendGift(@Body body: SendGiftRequest): TelegramResponse<Boolean>

    /**
     * Gifts a Telegram Premium subscription to the given user. Returns True on success.
     */
    @POST("giftPremiumSubscription")
    public suspend fun giftPremiumSubscription(@Body body: GiftPremiumSubscriptionRequest): TelegramResponse<Boolean>

    /**
     * Verifies a user on behalf of the organization which is represented by the bot. Returns True on success.
     */
    @POST("verifyUser")
    public suspend fun verifyUser(@Body body: VerifyUserRequest): TelegramResponse<Boolean>

    /**
     * Verifies a chat on behalf of the organization which is represented by the bot. Returns True on success.
     */
    @POST("verifyChat")
    public suspend fun verifyChat(@Body body: VerifyChatRequest): TelegramResponse<Boolean>

    /**
     * Removes verification from a user who is currently verified on behalf of the organization represented by the bot. Returns True on success.
     */
    @POST("removeUserVerification")
    public suspend fun removeUserVerification(@Body body: RemoveUserVerificationRequest): TelegramResponse<Boolean>

    /**
     * Removes verification from a chat that is currently verified on behalf of the organization represented by the bot. Returns True on success.
     */
    @POST("removeChatVerification")
    public suspend fun removeChatVerification(@Body body: RemoveChatVerificationRequest): TelegramResponse<Boolean>

    /**
     * Marks incoming message as read on behalf of a business account. Requires the can_read_messages business bot right. Returns True on success.
     */
    @POST("readBusinessMessage")
    public suspend fun readBusinessMessage(@Body body: ReadBusinessMessageRequest): TelegramResponse<Boolean>

    /**
     * Delete messages on behalf of a business account. Requires the can_delete_sent_messages business bot right to delete messages sent by the bot itself, or the can_delete_all_messages business bot right to delete any message. Returns True on success.
     */
    @POST("deleteBusinessMessages")
    public suspend fun deleteBusinessMessages(@Body body: DeleteBusinessMessagesRequest): TelegramResponse<Boolean>

    /**
     * Changes the first and last name of a managed business account. Requires the can_change_name business bot right. Returns True on success.
     */
    @POST("setBusinessAccountName")
    public suspend fun setBusinessAccountName(@Body body: SetBusinessAccountNameRequest): TelegramResponse<Boolean>

    /**
     * Changes the username of a managed business account. Requires the can_change_username business bot right. Returns True on success.
     */
    @POST("setBusinessAccountUsername")
    public suspend fun setBusinessAccountUsername(@Body body: SetBusinessAccountUsernameRequest): TelegramResponse<Boolean>

    /**
     * Changes the bio of a managed business account. Requires the can_change_bio business bot right. Returns True on success.
     */
    @POST("setBusinessAccountBio")
    public suspend fun setBusinessAccountBio(@Body body: SetBusinessAccountBioRequest): TelegramResponse<Boolean>

    /**
     * Changes the profile photo of a managed business account. Requires the can_edit_profile_photo business bot right. Returns True on success.
     */
    @POST("setBusinessAccountProfilePhoto")
    public suspend fun setBusinessAccountProfilePhoto(@Body formData: MultiPartFormDataContent): TelegramResponse<Boolean>

    /**
     * Removes the current profile photo of a managed business account. Requires the can_edit_profile_photo business bot right. Returns True on success.
     */
    @POST("removeBusinessAccountProfilePhoto")
    public suspend fun removeBusinessAccountProfilePhoto(@Body body: RemoveBusinessAccountProfilePhotoRequest): TelegramResponse<Boolean>

    /**
     * Changes the privacy settings pertaining to incoming gifts in a managed business account. Requires the can_change_gift_settings business bot right. Returns True on success.
     */
    @POST("setBusinessAccountGiftSettings")
    public suspend fun setBusinessAccountGiftSettings(@Body body: SetBusinessAccountGiftSettingsRequest): TelegramResponse<Boolean>

    /**
     * Returns the amount of Telegram Stars owned by a managed business account. Requires the can_view_gifts_and_stars business bot right. Returns StarAmount on success.
     *
     * @param businessConnectionId Unique identifier of the business connection
     */
    @GET("getBusinessAccountStarBalance")
    public suspend fun getBusinessAccountStarBalance(@Query("business_connection_id") businessConnectionId: String): TelegramResponse<StarAmount>

    /**
     * Transfers Telegram Stars from the business account balance to the bot's balance. Requires the can_transfer_stars business bot right. Returns True on success.
     */
    @POST("transferBusinessAccountStars")
    public suspend fun transferBusinessAccountStars(@Body body: TransferBusinessAccountStarsRequest): TelegramResponse<Boolean>

    /**
     * Returns the gifts received and owned by a managed business account. Requires the can_view_gifts_and_stars business bot right. Returns OwnedGifts on success.
     *
     * @param businessConnectionId Unique identifier of the business connection
     * @param excludeUnsaved Pass *True* to exclude gifts that aren't saved to the account's profile page
     * @param excludeSaved Pass *True* to exclude gifts that are saved to the account's profile page
     * @param excludeUnlimited Pass *True* to exclude gifts that can be purchased an unlimited number of times
     * @param excludeLimitedUpgradable Pass *True* to exclude gifts that can be purchased a limited number of times and can be upgraded to unique
     * @param excludeLimitedNonUpgradable Pass *True* to exclude gifts that can be purchased a limited number of times and can't be upgraded to unique
     * @param excludeUnique Pass *True* to exclude unique gifts
     * @param excludeFromBlockchain Pass *True* to exclude gifts that were assigned from the TON blockchain and can't be resold or transferred in Telegram
     * @param sortByPrice Pass *True* to sort results by gift price instead of send date. Sorting is applied before pagination.
     * @param offset Offset of the first entry to return as received from the previous request; use empty string to get the first chunk of results
     * @param limit The maximum number of gifts to be returned; 1-100. Defaults to 100
     */
    @GET("getBusinessAccountGifts")
    public suspend fun getBusinessAccountGifts(
        @Query("business_connection_id") businessConnectionId: String,
        @Query("exclude_unsaved") excludeUnsaved: Boolean? = null,
        @Query("exclude_saved") excludeSaved: Boolean? = null,
        @Query("exclude_unlimited") excludeUnlimited: Boolean? = null,
        @Query("exclude_limited_upgradable") excludeLimitedUpgradable: Boolean? = null,
        @Query("exclude_limited_non_upgradable") excludeLimitedNonUpgradable: Boolean? = null,
        @Query("exclude_unique") excludeUnique: Boolean? = null,
        @Query("exclude_from_blockchain") excludeFromBlockchain: Boolean? = null,
        @Query("sort_by_price") sortByPrice: Boolean? = null,
        @Query offset: String? = null,
        @Query limit: Long? = null,
    ): TelegramResponse<OwnedGifts>

    /**
     * Returns the gifts owned and hosted by a user. Returns OwnedGifts on success.
     *
     * @param userId Unique identifier of the user
     * @param excludeUnlimited Pass *True* to exclude gifts that can be purchased an unlimited number of times
     * @param excludeLimitedUpgradable Pass *True* to exclude gifts that can be purchased a limited number of times and can be upgraded to unique
     * @param excludeLimitedNonUpgradable Pass *True* to exclude gifts that can be purchased a limited number of times and can't be upgraded to unique
     * @param excludeFromBlockchain Pass *True* to exclude gifts that were assigned from the TON blockchain and can't be resold or transferred in Telegram
     * @param excludeUnique Pass *True* to exclude unique gifts
     * @param sortByPrice Pass *True* to sort results by gift price instead of send date. Sorting is applied before pagination.
     * @param offset Offset of the first entry to return as received from the previous request; use an empty string to get the first chunk of results
     * @param limit The maximum number of gifts to be returned; 1-100. Defaults to 100
     */
    @GET("getUserGifts")
    public suspend fun getUserGifts(
        @Query("user_id") userId: Long,
        @Query("exclude_unlimited") excludeUnlimited: Boolean? = null,
        @Query("exclude_limited_upgradable") excludeLimitedUpgradable: Boolean? = null,
        @Query("exclude_limited_non_upgradable") excludeLimitedNonUpgradable: Boolean? = null,
        @Query("exclude_from_blockchain") excludeFromBlockchain: Boolean? = null,
        @Query("exclude_unique") excludeUnique: Boolean? = null,
        @Query("sort_by_price") sortByPrice: Boolean? = null,
        @Query offset: String? = null,
        @Query limit: Long? = null,
    ): TelegramResponse<OwnedGifts>

    /**
     * Returns the gifts owned by a chat. Returns OwnedGifts on success.
     *
     * @param chatId Unique identifier for the target chat or username of the target channel (in the format `@channelusername`)
     * @param excludeUnsaved Pass *True* to exclude gifts that aren't saved to the chat's profile page. Always *True*, unless the bot has the *can_post_messages* administrator right in the channel.
     * @param excludeSaved Pass *True* to exclude gifts that are saved to the chat's profile page. Always *False*, unless the bot has the *can_post_messages* administrator right in the channel.
     * @param excludeUnlimited Pass *True* to exclude gifts that can be purchased an unlimited number of times
     * @param excludeLimitedUpgradable Pass *True* to exclude gifts that can be purchased a limited number of times and can be upgraded to unique
     * @param excludeLimitedNonUpgradable Pass *True* to exclude gifts that can be purchased a limited number of times and can't be upgraded to unique
     * @param excludeFromBlockchain Pass *True* to exclude gifts that were assigned from the TON blockchain and can't be resold or transferred in Telegram
     * @param excludeUnique Pass *True* to exclude unique gifts
     * @param sortByPrice Pass *True* to sort results by gift price instead of send date. Sorting is applied before pagination.
     * @param offset Offset of the first entry to return as received from the previous request; use an empty string to get the first chunk of results
     * @param limit The maximum number of gifts to be returned; 1-100. Defaults to 100
     */
    @GET("getChatGifts")
    public suspend fun getChatGifts(
        @Query("chat_id") chatId: String,
        @Query("exclude_unsaved") excludeUnsaved: Boolean? = null,
        @Query("exclude_saved") excludeSaved: Boolean? = null,
        @Query("exclude_unlimited") excludeUnlimited: Boolean? = null,
        @Query("exclude_limited_upgradable") excludeLimitedUpgradable: Boolean? = null,
        @Query("exclude_limited_non_upgradable") excludeLimitedNonUpgradable: Boolean? = null,
        @Query("exclude_from_blockchain") excludeFromBlockchain: Boolean? = null,
        @Query("exclude_unique") excludeUnique: Boolean? = null,
        @Query("sort_by_price") sortByPrice: Boolean? = null,
        @Query offset: String? = null,
        @Query limit: Long? = null,
    ): TelegramResponse<OwnedGifts>

    /**
     * Converts a given regular gift to Telegram Stars. Requires the can_convert_gifts_to_stars business bot right. Returns True on success.
     */
    @POST("convertGiftToStars")
    public suspend fun convertGiftToStars(@Body body: ConvertGiftToStarsRequest): TelegramResponse<Boolean>

    /**
     * Upgrades a given regular gift to a unique gift. Requires the can_transfer_and_upgrade_gifts business bot right. Additionally requires the can_transfer_stars business bot right if the upgrade is paid. Returns True on success.
     */
    @POST("upgradeGift")
    public suspend fun upgradeGift(@Body body: UpgradeGiftRequest): TelegramResponse<Boolean>

    /**
     * Transfers an owned unique gift to another user. Requires the can_transfer_and_upgrade_gifts business bot right. Requires can_transfer_stars business bot right if the transfer is paid. Returns True on success.
     */
    @POST("transferGift")
    public suspend fun transferGift(@Body body: TransferGiftRequest): TelegramResponse<Boolean>

    /**
     * Posts a story on behalf of a managed business account. Requires the can_manage_stories business bot right. Returns Story on success.
     */
    @POST("postStory")
    public suspend fun postStory(@Body formData: MultiPartFormDataContent): TelegramResponse<Story>

    /**
     * Reposts a story on behalf of a business account from another business account. Both business accounts must be managed by the same bot, and the story on the source account must have been posted (or reposted) by the bot. Requires the can_manage_stories business bot right for both business accounts. Returns Story on success.
     */
    @POST("repostStory")
    public suspend fun repostStory(@Body body: RepostStoryRequest): TelegramResponse<Story>

    /**
     * Edits a story previously posted by the bot on behalf of a managed business account. Requires the can_manage_stories business bot right. Returns Story on success.
     */
    @POST("editStory")
    public suspend fun editStory(@Body formData: MultiPartFormDataContent): TelegramResponse<Story>

    /**
     * Deletes a story previously posted by the bot on behalf of a managed business account. Requires the can_manage_stories business bot right. Returns True on success.
     */
    @POST("deleteStory")
    public suspend fun deleteStory(@Body body: DeleteStoryRequest): TelegramResponse<Boolean>

    /**
     * Use this method to edit text and game messages. On success, if the edited message is not an inline message, the edited Message is returned, otherwise True is returned. Note that business messages that were not sent by the bot and do not contain an inline keyboard can only be edited within 48 hours from the time they were sent.
     */
    @POST("editMessageText")
    public suspend fun editMessageText(@Body body: EditMessageTextRequest): TelegramResponse<Boolean>

    /**
     * Use this method to edit captions of messages. On success, if the edited message is not an inline message, the edited Message is returned, otherwise True is returned. Note that business messages that were not sent by the bot and do not contain an inline keyboard can only be edited within 48 hours from the time they were sent.
     */
    @POST("editMessageCaption")
    public suspend fun editMessageCaption(@Body body: EditMessageCaptionRequest): TelegramResponse<Boolean>

    /**
     * Use this method to edit animation, audio, document, photo, or video messages, or to add media to text messages. If a message is part of a message album, then it can be edited only to an audio for audio albums, only to a document for document albums and to a photo or a video otherwise. When an inline message is edited, a new file can't be uploaded; use a previously uploaded file via its file_id or specify a URL. On success, if the edited message is not an inline message, the edited Message is returned, otherwise True is returned. Note that business messages that were not sent by the bot and do not contain an inline keyboard can only be edited within 48 hours from the time they were sent.
     */
    @POST("editMessageMedia")
    public suspend fun editMessageMedia(@Body formData: MultiPartFormDataContent): TelegramResponse<Boolean>

    /**
     * Use this method to edit live location messages. A location can be edited until its live_period expires or editing is explicitly disabled by a call to stopMessageLiveLocation. On success, if the edited message is not an inline message, the edited Message is returned, otherwise True is returned.
     */
    @POST("editMessageLiveLocation")
    public suspend fun editMessageLiveLocation(@Body body: EditMessageLiveLocationRequest): TelegramResponse<Boolean>

    /**
     * Use this method to stop updating a live location message before live_period expires. On success, if the message is not an inline message, the edited Message is returned, otherwise True is returned.
     */
    @POST("stopMessageLiveLocation")
    public suspend fun stopMessageLiveLocation(@Body body: StopMessageLiveLocationRequest): TelegramResponse<Boolean>

    /**
     * Use this method to edit a checklist on behalf of a connected business account. On success, the edited Message is returned.
     */
    @POST("editMessageChecklist")
    public suspend fun editMessageChecklist(@Body body: EditMessageChecklistRequest): TelegramResponse<Message>

    /**
     * Use this method to edit only the reply markup of messages. On success, if the edited message is not an inline message, the edited Message is returned, otherwise True is returned. Note that business messages that were not sent by the bot and do not contain an inline keyboard can only be edited within 48 hours from the time they were sent.
     */
    @POST("editMessageReplyMarkup")
    public suspend fun editMessageReplyMarkup(@Body body: EditMessageReplyMarkupRequest): TelegramResponse<Boolean>

    /**
     * Use this method to stop a poll which was sent by the bot. On success, the stopped Poll is returned.
     */
    @POST("stopPoll")
    public suspend fun stopPoll(@Body body: StopPollRequest): TelegramResponse<Poll>

    /**
     * Use this method to approve a suggested post in a direct messages chat. The bot must have the 'can_post_messages' administrator right in the corresponding channel chat. Returns True on success.
     */
    @POST("approveSuggestedPost")
    public suspend fun approveSuggestedPost(@Body body: ApproveSuggestedPostRequest): TelegramResponse<Boolean>

    /**
     * Use this method to decline a suggested post in a direct messages chat. The bot must have the 'can_manage_direct_messages' administrator right in the corresponding channel chat. Returns True on success.
     */
    @POST("declineSuggestedPost")
    public suspend fun declineSuggestedPost(@Body body: DeclineSuggestedPostRequest): TelegramResponse<Boolean>

    /**
     * Use this method to delete a message, including service messages, with the following limitations: - A message can only be deleted if it was sent less than 48 hours ago. - Service messages about a supergroup, channel, or forum topic creation can't be deleted. - A dice message in a private chat can only be deleted if it was sent more than 24 hours ago. - Bots can delete outgoing messages in private chats, groups, and supergroups. - Bots can delete incoming messages in private chats. - Bots granted can_post_messages permissions can delete outgoing messages in channels. - If the bot is an administrator of a group, it can delete any message there. - If the bot has can_delete_messages administrator right in a supergroup or a channel, it can delete any message there. - If the bot has can_manage_direct_messages administrator right in a channel, it can delete any message in the corresponding direct messages chat. Returns True on success.
     */
    @POST("deleteMessage")
    public suspend fun deleteMessage(@Body body: DeleteMessageRequest): TelegramResponse<Boolean>

    /**
     * Use this method to delete multiple messages simultaneously. If some of the specified messages can't be found, they are skipped. Returns True on success.
     */
    @POST("deleteMessages")
    public suspend fun deleteMessages(@Body body: DeleteMessagesRequest): TelegramResponse<Boolean>

    /**
     * Use this method to send static .WEBP, animated .TGS, or video .WEBM stickers. On success, the sent Message is returned.
     */
    @POST("sendSticker")
    public suspend fun sendSticker(@Body formData: MultiPartFormDataContent): TelegramResponse<Message>

    /**
     * Use this method to get a sticker set. On success, a StickerSet object is returned.
     *
     * @param name Name of the sticker set
     */
    @GET("getStickerSet")
    public suspend fun getStickerSet(@Query name: String): TelegramResponse<StickerSet>

    /**
     * Use this method to get information about custom emoji stickers by their identifiers. Returns an Array of Sticker objects.
     *
     * @param customEmojiIds A JSON-serialized list of custom emoji identifiers. At most 200 custom emoji identifiers can be specified.
     */
    @GET("getCustomEmojiStickers")
    public suspend fun getCustomEmojiStickers(@Query("custom_emoji_ids") customEmojiIds: String): TelegramResponse<List<Sticker>>

    /**
     * Use this method to upload a file with a sticker for later use in the createNewStickerSet, addStickerToSet, or replaceStickerInSet methods (the file can be used multiple times). Returns the uploaded File on success.
     */
    @POST("uploadStickerFile")
    public suspend fun uploadStickerFile(@Body formData: MultiPartFormDataContent): TelegramResponse<File>

    /**
     * Use this method to create a new sticker set owned by a user. The bot will be able to edit the sticker set thus created. Returns True on success.
     */
    @POST("createNewStickerSet")
    public suspend fun createNewStickerSet(@Body formData: MultiPartFormDataContent): TelegramResponse<Boolean>

    /**
     * Use this method to add a new sticker to a set created by the bot. Emoji sticker sets can have up to 200 stickers. Other sticker sets can have up to 120 stickers. Returns True on success.
     */
    @POST("addStickerToSet")
    public suspend fun addStickerToSet(@Body formData: MultiPartFormDataContent): TelegramResponse<Boolean>

    /**
     * Use this method to move a sticker in a set created by the bot to a specific position. Returns True on success.
     */
    @POST("setStickerPositionInSet")
    public suspend fun setStickerPositionInSet(@Body body: SetStickerPositionInSetRequest): TelegramResponse<Boolean>

    /**
     * Use this method to delete a sticker from a set created by the bot. Returns True on success.
     */
    @POST("deleteStickerFromSet")
    public suspend fun deleteStickerFromSet(@Body body: DeleteStickerFromSetRequest): TelegramResponse<Boolean>

    /**
     * Use this method to replace an existing sticker in a sticker set with a new one. The method is equivalent to calling deleteStickerFromSet, then addStickerToSet, then setStickerPositionInSet. Returns True on success.
     */
    @POST("replaceStickerInSet")
    public suspend fun replaceStickerInSet(@Body formData: MultiPartFormDataContent): TelegramResponse<Boolean>

    /**
     * Use this method to change the list of emoji assigned to a regular or custom emoji sticker. The sticker must belong to a sticker set created by the bot. Returns True on success.
     */
    @POST("setStickerEmojiList")
    public suspend fun setStickerEmojiList(@Body body: SetStickerEmojiListRequest): TelegramResponse<Boolean>

    /**
     * Use this method to change search keywords assigned to a regular or custom emoji sticker. The sticker must belong to a sticker set created by the bot. Returns True on success.
     */
    @POST("setStickerKeywords")
    public suspend fun setStickerKeywords(@Body body: SetStickerKeywordsRequest): TelegramResponse<Boolean>

    /**
     * Use this method to change the mask position of a mask sticker. The sticker must belong to a sticker set that was created by the bot. Returns True on success.
     */
    @POST("setStickerMaskPosition")
    public suspend fun setStickerMaskPosition(@Body body: SetStickerMaskPositionRequest): TelegramResponse<Boolean>

    /**
     * Use this method to set the title of a created sticker set. Returns True on success.
     */
    @POST("setStickerSetTitle")
    public suspend fun setStickerSetTitle(@Body body: SetStickerSetTitleRequest): TelegramResponse<Boolean>

    /**
     * Use this method to set the thumbnail of a regular or mask sticker set. The format of the thumbnail file must match the format of the stickers in the set. Returns True on success.
     */
    @POST("setStickerSetThumbnail")
    public suspend fun setStickerSetThumbnail(@Body formData: MultiPartFormDataContent): TelegramResponse<Boolean>

    /**
     * Use this method to set the thumbnail of a custom emoji sticker set. Returns True on success.
     */
    @POST("setCustomEmojiStickerSetThumbnail")
    public suspend fun setCustomEmojiStickerSetThumbnail(@Body body: SetCustomEmojiStickerSetThumbnailRequest): TelegramResponse<Boolean>

    /**
     * Use this method to delete a sticker set that was created by the bot. Returns True on success.
     */
    @POST("deleteStickerSet")
    public suspend fun deleteStickerSet(@Body body: DeleteStickerSetRequest): TelegramResponse<Boolean>

    /**
     * Use this method to send answers to an inline query. On success, True is returned. No more than 50 results per query are allowed.
     */
    @POST("answerInlineQuery")
    public suspend fun answerInlineQuery(@Body body: AnswerInlineQueryRequest): TelegramResponse<Boolean>

    /**
     * Use this method to set the result of an interaction with a Web App and send a corresponding message on behalf of the user to the chat from which the query originated. On success, a SentWebAppMessage object is returned.
     */
    @POST("answerWebAppQuery")
    public suspend fun answerWebAppQuery(@Body body: AnswerWebAppQueryRequest): TelegramResponse<SentWebAppMessage>

    /**
     * Stores a message that can be sent by a user of a Mini App. Returns a PreparedInlineMessage object.
     */
    @POST("savePreparedInlineMessage")
    public suspend fun savePreparedInlineMessage(@Body body: SavePreparedInlineMessageRequest): TelegramResponse<PreparedInlineMessage>

    /**
     * Use this method to send invoices. On success, the sent Message is returned.
     */
    @POST("sendInvoice")
    public suspend fun sendInvoice(@Body body: SendInvoiceRequest): TelegramResponse<Message>

    /**
     * Use this method to create a link for an invoice. Returns the created invoice link as String on success.
     */
    @POST("createInvoiceLink")
    public suspend fun createInvoiceLink(@Body body: CreateInvoiceLinkRequest): TelegramResponse<String>

    /**
     * If you sent an invoice requesting a shipping address and the parameter is_flexible was specified, the Bot API will send an Update with a shipping_query field to the bot. Use this method to reply to shipping queries. On success, True is returned.
     */
    @POST("answerShippingQuery")
    public suspend fun answerShippingQuery(@Body body: AnswerShippingQueryRequest): TelegramResponse<Boolean>

    /**
     * Once the user has confirmed their payment and shipping details, the Bot API sends the final confirmation in the form of an Update with the field pre_checkout_query. Use this method to respond to such pre-checkout queries. On success, True is returned. Note: The Bot API must receive an answer within 10 seconds after the pre-checkout query was sent.
     */
    @POST("answerPreCheckoutQuery")
    public suspend fun answerPreCheckoutQuery(@Body body: AnswerPreCheckoutQueryRequest): TelegramResponse<Boolean>

    /**
     * A method to get the current Telegram Stars balance of the bot. Requires no parameters. On success, returns a StarAmount object.
     */
    @GET("getMyStarBalance")
    public suspend fun getMyStarBalance(): TelegramResponse<StarAmount>

    /**
     * Returns the bot's Telegram Star transactions in chronological order. On success, returns a StarTransactions object.
     *
     * @param offset Number of transactions to skip in the response
     * @param limit The maximum number of transactions to be retrieved. Values between 1-100 are accepted. Defaults to 100.
     */
    @GET("getStarTransactions")
    public suspend fun getStarTransactions(@Query offset: Long? = null, @Query limit: Long? = null): TelegramResponse<StarTransactions>

    /**
     * Refunds a successful payment in Telegram Stars. Returns True on success.
     */
    @POST("refundStarPayment")
    public suspend fun refundStarPayment(@Body body: RefundStarPaymentRequest): TelegramResponse<Boolean>

    /**
     * Allows the bot to cancel or re-enable extension of a subscription paid in Telegram Stars. Returns True on success.
     */
    @POST("editUserStarSubscription")
    public suspend fun editUserStarSubscription(@Body body: EditUserStarSubscriptionRequest): TelegramResponse<Boolean>

    /**
     * Informs a user that some of the Telegram Passport elements they provided contains errors. The user will not be able to re-submit their Passport to you until the errors are fixed (the contents of the field for which you returned the error must change). Returns True on success.
     * Use this if the data submitted by the user doesn't satisfy the standards your service requires for any reason. For example, if a birthday date seems invalid, a submitted document is blurry, a scan shows evidence of tampering, etc. Supply some details in the error message to make sure the user knows how to correct the issues.
     */
    @POST("setPassportDataErrors")
    public suspend fun setPassportDataErrors(@Body body: SetPassportDataErrorsRequest): TelegramResponse<Boolean>

    /**
     * Use this method to send a game. On success, the sent Message is returned.
     */
    @POST("sendGame")
    public suspend fun sendGame(@Body body: SendGameRequest): TelegramResponse<Message>

    /**
     * Use this method to set the score of the specified user in a game message. On success, if the message is not an inline message, the Message is returned, otherwise True is returned. Returns an error, if the new score is not greater than the user's current score in the chat and force is False.
     */
    @POST("setGameScore")
    public suspend fun setGameScore(@Body body: SetGameScoreRequest): TelegramResponse<Boolean>

    /**
     * Use this method to get data for high score tables. Will return the score of the specified user and several of their neighbors in a game. Returns an Array of GameHighScore objects.
     * This method will currently return scores for the target user, plus two of their closest neighbors on each side. Will also return the top three users if the user and their neighbors are not among them. Please note that this behavior is subject to change.
     *
     * @param userId Target user id
     * @param chatId Required if *inline_message_id* is not specified. Unique identifier for the target chat
     * @param messageId Required if *inline_message_id* is not specified. Identifier of the sent message
     * @param inlineMessageId Required if *chat_id* and *message_id* are not specified. Identifier of the inline message
     */
    @GET("getGameHighScores")
    public suspend fun getGameHighScores(
        @Query("user_id") userId: Long,
        @Query("chat_id") chatId: Long? = null,
        @Query("message_id") messageId: Long? = null,
        @Query("inline_message_id") inlineMessageId: String? = null,
    ): TelegramResponse<List<GameHighScore>>

    /**
     * Downloads a file from Telegram servers.
     */
    @TelegramFileDownload
    @GET("{filePath}")
    @Streaming
    public suspend fun downloadFile(@Path("filePath") filePath: String): HttpStatement
}
