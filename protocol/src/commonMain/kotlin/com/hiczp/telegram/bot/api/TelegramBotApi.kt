// Auto-generated from Swagger specificationDo not modify this file manually
@file:Suppress(
    "RedundantVisibilityModifier",
    "unused",
)

package com.hiczp.telegram.bot.api

import com.hiczp.telegram.bot.api.model.AddStickerToSetRequest
import com.hiczp.telegram.bot.api.model.AnswerCallbackQueryRequest
import com.hiczp.telegram.bot.api.model.AnswerInlineQueryRequest
import com.hiczp.telegram.bot.api.model.AnswerPreCheckoutQueryRequest
import com.hiczp.telegram.bot.api.model.AnswerShippingQueryRequest
import com.hiczp.telegram.bot.api.model.AnswerWebAppQueryRequest
import com.hiczp.telegram.bot.api.model.ApproveChatJoinRequestRequest
import com.hiczp.telegram.bot.api.model.ApproveSuggestedPostRequest
import com.hiczp.telegram.bot.api.model.BanChatMemberRequest
import com.hiczp.telegram.bot.api.model.BanChatSenderChatRequest
import com.hiczp.telegram.bot.api.model.BotCommand
import com.hiczp.telegram.bot.api.model.BotCommandScope
import com.hiczp.telegram.bot.api.model.BotDescription
import com.hiczp.telegram.bot.api.model.BotName
import com.hiczp.telegram.bot.api.model.BotShortDescription
import com.hiczp.telegram.bot.api.model.BusinessConnection
import com.hiczp.telegram.bot.api.model.ChatAdministratorRights
import com.hiczp.telegram.bot.api.model.ChatFullInfo
import com.hiczp.telegram.bot.api.model.ChatInviteLink
import com.hiczp.telegram.bot.api.model.ChatMember
import com.hiczp.telegram.bot.api.model.CloseForumTopicRequest
import com.hiczp.telegram.bot.api.model.CloseGeneralForumTopicRequest
import com.hiczp.telegram.bot.api.model.CloseRequest
import com.hiczp.telegram.bot.api.model.ConvertGiftToStarsRequest
import com.hiczp.telegram.bot.api.model.CopyMessageRequest
import com.hiczp.telegram.bot.api.model.CopyMessagesRequest
import com.hiczp.telegram.bot.api.model.CreateChatInviteLinkRequest
import com.hiczp.telegram.bot.api.model.CreateChatSubscriptionInviteLinkRequest
import com.hiczp.telegram.bot.api.model.CreateForumTopicRequest
import com.hiczp.telegram.bot.api.model.CreateInvoiceLinkRequest
import com.hiczp.telegram.bot.api.model.CreateNewStickerSetRequest
import com.hiczp.telegram.bot.api.model.DeclineChatJoinRequestRequest
import com.hiczp.telegram.bot.api.model.DeclineSuggestedPostRequest
import com.hiczp.telegram.bot.api.model.DeleteBusinessMessagesRequest
import com.hiczp.telegram.bot.api.model.DeleteChatPhotoRequest
import com.hiczp.telegram.bot.api.model.DeleteChatStickerSetRequest
import com.hiczp.telegram.bot.api.model.DeleteForumTopicRequest
import com.hiczp.telegram.bot.api.model.DeleteMessageRequest
import com.hiczp.telegram.bot.api.model.DeleteMessagesRequest
import com.hiczp.telegram.bot.api.model.DeleteMyCommandsRequest
import com.hiczp.telegram.bot.api.model.DeleteStickerFromSetRequest
import com.hiczp.telegram.bot.api.model.DeleteStickerSetRequest
import com.hiczp.telegram.bot.api.model.DeleteStoryRequest
import com.hiczp.telegram.bot.api.model.DeleteWebhookRequest
import com.hiczp.telegram.bot.api.model.EditChatInviteLinkRequest
import com.hiczp.telegram.bot.api.model.EditChatSubscriptionInviteLinkRequest
import com.hiczp.telegram.bot.api.model.EditForumTopicRequest
import com.hiczp.telegram.bot.api.model.EditGeneralForumTopicRequest
import com.hiczp.telegram.bot.api.model.EditMessageCaptionRequest
import com.hiczp.telegram.bot.api.model.EditMessageChecklistRequest
import com.hiczp.telegram.bot.api.model.EditMessageLiveLocationRequest
import com.hiczp.telegram.bot.api.model.EditMessageReplyMarkupRequest
import com.hiczp.telegram.bot.api.model.EditMessageTextRequest
import com.hiczp.telegram.bot.api.model.EditStoryRequest
import com.hiczp.telegram.bot.api.model.EditUserStarSubscriptionRequest
import com.hiczp.telegram.bot.api.model.ExportChatInviteLinkRequest
import com.hiczp.telegram.bot.api.model.File
import com.hiczp.telegram.bot.api.model.ForumTopic
import com.hiczp.telegram.bot.api.model.ForwardMessageRequest
import com.hiczp.telegram.bot.api.model.ForwardMessagesRequest
import com.hiczp.telegram.bot.api.model.GameHighScore
import com.hiczp.telegram.bot.api.model.GiftPremiumSubscriptionRequest
import com.hiczp.telegram.bot.api.model.Gifts
import com.hiczp.telegram.bot.api.model.HideGeneralForumTopicRequest
import com.hiczp.telegram.bot.api.model.LeaveChatRequest
import com.hiczp.telegram.bot.api.model.LogOutRequest
import com.hiczp.telegram.bot.api.model.MenuButton
import com.hiczp.telegram.bot.api.model.Message
import com.hiczp.telegram.bot.api.model.MessageId
import com.hiczp.telegram.bot.api.model.OwnedGifts
import com.hiczp.telegram.bot.api.model.PinChatMessageRequest
import com.hiczp.telegram.bot.api.model.Poll
import com.hiczp.telegram.bot.api.model.PostStoryRequest
import com.hiczp.telegram.bot.api.model.PreparedInlineMessage
import com.hiczp.telegram.bot.api.model.PromoteChatMemberRequest
import com.hiczp.telegram.bot.api.model.ReadBusinessMessageRequest
import com.hiczp.telegram.bot.api.model.RefundStarPaymentRequest
import com.hiczp.telegram.bot.api.model.RemoveBusinessAccountProfilePhotoRequest
import com.hiczp.telegram.bot.api.model.RemoveChatVerificationRequest
import com.hiczp.telegram.bot.api.model.RemoveUserVerificationRequest
import com.hiczp.telegram.bot.api.model.ReopenForumTopicRequest
import com.hiczp.telegram.bot.api.model.ReopenGeneralForumTopicRequest
import com.hiczp.telegram.bot.api.model.ReplaceStickerInSetRequest
import com.hiczp.telegram.bot.api.model.RepostStoryRequest
import com.hiczp.telegram.bot.api.model.RestrictChatMemberRequest
import com.hiczp.telegram.bot.api.model.RevokeChatInviteLinkRequest
import com.hiczp.telegram.bot.api.model.SavePreparedInlineMessageRequest
import com.hiczp.telegram.bot.api.model.SendChatActionRequest
import com.hiczp.telegram.bot.api.model.SendChecklistRequest
import com.hiczp.telegram.bot.api.model.SendContactRequest
import com.hiczp.telegram.bot.api.model.SendDiceRequest
import com.hiczp.telegram.bot.api.model.SendGameRequest
import com.hiczp.telegram.bot.api.model.SendGiftRequest
import com.hiczp.telegram.bot.api.model.SendInvoiceRequest
import com.hiczp.telegram.bot.api.model.SendLocationRequest
import com.hiczp.telegram.bot.api.model.SendMessageDraftRequest
import com.hiczp.telegram.bot.api.model.SendMessageRequest
import com.hiczp.telegram.bot.api.model.SendPaidMediaRequest
import com.hiczp.telegram.bot.api.model.SendPollRequest
import com.hiczp.telegram.bot.api.model.SendVenueRequest
import com.hiczp.telegram.bot.api.model.SentWebAppMessage
import com.hiczp.telegram.bot.api.model.SetBusinessAccountBioRequest
import com.hiczp.telegram.bot.api.model.SetBusinessAccountGiftSettingsRequest
import com.hiczp.telegram.bot.api.model.SetBusinessAccountNameRequest
import com.hiczp.telegram.bot.api.model.SetBusinessAccountProfilePhotoRequest
import com.hiczp.telegram.bot.api.model.SetBusinessAccountUsernameRequest
import com.hiczp.telegram.bot.api.model.SetChatAdministratorCustomTitleRequest
import com.hiczp.telegram.bot.api.model.SetChatDescriptionRequest
import com.hiczp.telegram.bot.api.model.SetChatMenuButtonRequest
import com.hiczp.telegram.bot.api.model.SetChatPermissionsRequest
import com.hiczp.telegram.bot.api.model.SetChatStickerSetRequest
import com.hiczp.telegram.bot.api.model.SetChatTitleRequest
import com.hiczp.telegram.bot.api.model.SetCustomEmojiStickerSetThumbnailRequest
import com.hiczp.telegram.bot.api.model.SetGameScoreRequest
import com.hiczp.telegram.bot.api.model.SetMessageReactionRequest
import com.hiczp.telegram.bot.api.model.SetMyCommandsRequest
import com.hiczp.telegram.bot.api.model.SetMyDefaultAdministratorRightsRequest
import com.hiczp.telegram.bot.api.model.SetMyDescriptionRequest
import com.hiczp.telegram.bot.api.model.SetMyNameRequest
import com.hiczp.telegram.bot.api.model.SetMyShortDescriptionRequest
import com.hiczp.telegram.bot.api.model.SetPassportDataErrorsRequest
import com.hiczp.telegram.bot.api.model.SetStickerEmojiListRequest
import com.hiczp.telegram.bot.api.model.SetStickerKeywordsRequest
import com.hiczp.telegram.bot.api.model.SetStickerMaskPositionRequest
import com.hiczp.telegram.bot.api.model.SetStickerPositionInSetRequest
import com.hiczp.telegram.bot.api.model.SetStickerSetTitleRequest
import com.hiczp.telegram.bot.api.model.SetUserEmojiStatusRequest
import com.hiczp.telegram.bot.api.model.StarAmount
import com.hiczp.telegram.bot.api.model.StarTransactions
import com.hiczp.telegram.bot.api.model.Sticker
import com.hiczp.telegram.bot.api.model.StickerSet
import com.hiczp.telegram.bot.api.model.StopMessageLiveLocationRequest
import com.hiczp.telegram.bot.api.model.StopPollRequest
import com.hiczp.telegram.bot.api.model.Story
import com.hiczp.telegram.bot.api.model.TransferBusinessAccountStarsRequest
import com.hiczp.telegram.bot.api.model.TransferGiftRequest
import com.hiczp.telegram.bot.api.model.UnbanChatMemberRequest
import com.hiczp.telegram.bot.api.model.UnbanChatSenderChatRequest
import com.hiczp.telegram.bot.api.model.UnhideGeneralForumTopicRequest
import com.hiczp.telegram.bot.api.model.UnpinAllChatMessagesRequest
import com.hiczp.telegram.bot.api.model.UnpinAllForumTopicMessagesRequest
import com.hiczp.telegram.bot.api.model.UnpinAllGeneralForumTopicMessagesRequest
import com.hiczp.telegram.bot.api.model.UnpinChatMessageRequest
import com.hiczp.telegram.bot.api.model.Update
import com.hiczp.telegram.bot.api.model.UpgradeGiftRequest
import com.hiczp.telegram.bot.api.model.User
import com.hiczp.telegram.bot.api.model.UserChatBoosts
import com.hiczp.telegram.bot.api.model.UserProfilePhotos
import com.hiczp.telegram.bot.api.model.VerifyChatRequest
import com.hiczp.telegram.bot.api.model.VerifyUserRequest
import com.hiczp.telegram.bot.api.model.WebhookInfo
import com.hiczp.telegram.bot.api.type.TelegramResponse
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.Query
import io.ktor.client.request.forms.MultiPartFormDataContent
import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List

public interface TelegramBotApi {
    /**
     * getUpdates
     */
    @GET("getUpdates")
    public suspend fun getUpdates(
        @Query("offset") offset: Long? = null,
        @Query("limit") limit: Long? = null,
        @Query("timeout") timeout: Long? = null,
        @Query("allowed_updates") allowedUpdates: List<String>? = null,
    ): TelegramResponse<List<Update>>

    /**
     * setWebhook
     */
    @POST("setWebhook")
    public suspend fun setWebhook(@Body formData: MultiPartFormDataContent): TelegramResponse<Boolean>

    /**
     * deleteWebhook
     */
    @POST("deleteWebhook")
    public suspend fun deleteWebhook(@Body body: DeleteWebhookRequest): TelegramResponse<Boolean>

    /**
     * getWebhookInfo
     */
    @GET("getWebhookInfo")
    public suspend fun getWebhookInfo(): TelegramResponse<WebhookInfo>

    /**
     * getMe
     */
    @GET("getMe")
    public suspend fun getMe(): TelegramResponse<User>

    /**
     * logOut
     */
    @POST("logOut")
    public suspend fun logOut(@Body body: LogOutRequest): TelegramResponse<Boolean>

    /**
     * close
     */
    @POST("close")
    public suspend fun close(@Body body: CloseRequest): TelegramResponse<Boolean>

    /**
     * sendMessage
     */
    @POST("sendMessage")
    public suspend fun sendMessage(@Body body: SendMessageRequest): TelegramResponse<Message>

    /**
     * forwardMessage
     */
    @POST("forwardMessage")
    public suspend fun forwardMessage(@Body body: ForwardMessageRequest): TelegramResponse<Message>

    /**
     * forwardMessages
     */
    @POST("forwardMessages")
    public suspend fun forwardMessages(@Body body: ForwardMessagesRequest): TelegramResponse<List<MessageId>>

    /**
     * copyMessage
     */
    @POST("copyMessage")
    public suspend fun copyMessage(@Body body: CopyMessageRequest): TelegramResponse<MessageId>

    /**
     * copyMessages
     */
    @POST("copyMessages")
    public suspend fun copyMessages(@Body body: CopyMessagesRequest): TelegramResponse<List<MessageId>>

    /**
     * sendPhoto
     */
    @POST("sendPhoto")
    public suspend fun sendPhoto(@Body formData: MultiPartFormDataContent): TelegramResponse<Message>

    /**
     * sendAudio
     */
    @POST("sendAudio")
    public suspend fun sendAudio(@Body formData: MultiPartFormDataContent): TelegramResponse<Message>

    /**
     * sendDocument
     */
    @POST("sendDocument")
    public suspend fun sendDocument(@Body formData: MultiPartFormDataContent): TelegramResponse<Message>

    /**
     * sendVideo
     */
    @POST("sendVideo")
    public suspend fun sendVideo(@Body formData: MultiPartFormDataContent): TelegramResponse<Message>

    /**
     * sendAnimation
     */
    @POST("sendAnimation")
    public suspend fun sendAnimation(@Body formData: MultiPartFormDataContent): TelegramResponse<Message>

    /**
     * sendVoice
     */
    @POST("sendVoice")
    public suspend fun sendVoice(@Body formData: MultiPartFormDataContent): TelegramResponse<Message>

    /**
     * sendVideoNote
     */
    @POST("sendVideoNote")
    public suspend fun sendVideoNote(@Body formData: MultiPartFormDataContent): TelegramResponse<Message>

    /**
     * sendPaidMedia
     */
    @POST("sendPaidMedia")
    public suspend fun sendPaidMedia(@Body body: SendPaidMediaRequest): TelegramResponse<Message>

    /**
     * sendMediaGroup
     */
    @POST("sendMediaGroup")
    public suspend fun sendMediaGroup(@Body formData: MultiPartFormDataContent): TelegramResponse<List<Message>>

    /**
     * sendLocation
     */
    @POST("sendLocation")
    public suspend fun sendLocation(@Body body: SendLocationRequest): TelegramResponse<Message>

    /**
     * sendVenue
     */
    @POST("sendVenue")
    public suspend fun sendVenue(@Body body: SendVenueRequest): TelegramResponse<Message>

    /**
     * sendContact
     */
    @POST("sendContact")
    public suspend fun sendContact(@Body body: SendContactRequest): TelegramResponse<Message>

    /**
     * sendPoll
     */
    @POST("sendPoll")
    public suspend fun sendPoll(@Body body: SendPollRequest): TelegramResponse<Message>

    /**
     * sendChecklist
     */
    @POST("sendChecklist")
    public suspend fun sendChecklist(@Body body: SendChecklistRequest): TelegramResponse<Message>

    /**
     * sendDice
     */
    @POST("sendDice")
    public suspend fun sendDice(@Body body: SendDiceRequest): TelegramResponse<Message>

    /**
     * sendMessageDraft
     */
    @POST("sendMessageDraft")
    public suspend fun sendMessageDraft(@Body body: SendMessageDraftRequest): TelegramResponse<Boolean>

    /**
     * sendChatAction
     */
    @POST("sendChatAction")
    public suspend fun sendChatAction(@Body body: SendChatActionRequest): TelegramResponse<Boolean>

    /**
     * setMessageReaction
     */
    @POST("setMessageReaction")
    public suspend fun setMessageReaction(@Body body: SetMessageReactionRequest): TelegramResponse<Boolean>

    /**
     * getUserProfilePhotos
     */
    @GET("getUserProfilePhotos")
    public suspend fun getUserProfilePhotos(
        @Query("user_id") userId: Long,
        @Query("offset") offset: Long? = null,
        @Query("limit") limit: Long? = null,
    ): TelegramResponse<UserProfilePhotos>

    /**
     * setUserEmojiStatus
     */
    @POST("setUserEmojiStatus")
    public suspend fun setUserEmojiStatus(@Body body: SetUserEmojiStatusRequest): TelegramResponse<Boolean>

    /**
     * getFile
     */
    @GET("getFile")
    public suspend fun getFile(@Query("file_id") fileId: String): TelegramResponse<File>

    /**
     * banChatMember
     */
    @POST("banChatMember")
    public suspend fun banChatMember(@Body body: BanChatMemberRequest): TelegramResponse<Boolean>

    /**
     * unbanChatMember
     */
    @POST("unbanChatMember")
    public suspend fun unbanChatMember(@Body body: UnbanChatMemberRequest): TelegramResponse<Boolean>

    /**
     * restrictChatMember
     */
    @POST("restrictChatMember")
    public suspend fun restrictChatMember(@Body body: RestrictChatMemberRequest): TelegramResponse<Boolean>

    /**
     * promoteChatMember
     */
    @POST("promoteChatMember")
    public suspend fun promoteChatMember(@Body body: PromoteChatMemberRequest): TelegramResponse<Boolean>

    /**
     * setChatAdministratorCustomTitle
     */
    @POST("setChatAdministratorCustomTitle")
    public suspend fun setChatAdministratorCustomTitle(@Body body: SetChatAdministratorCustomTitleRequest): TelegramResponse<Boolean>

    /**
     * banChatSenderChat
     */
    @POST("banChatSenderChat")
    public suspend fun banChatSenderChat(@Body body: BanChatSenderChatRequest): TelegramResponse<Boolean>

    /**
     * unbanChatSenderChat
     */
    @POST("unbanChatSenderChat")
    public suspend fun unbanChatSenderChat(@Body body: UnbanChatSenderChatRequest): TelegramResponse<Boolean>

    /**
     * setChatPermissions
     */
    @POST("setChatPermissions")
    public suspend fun setChatPermissions(@Body body: SetChatPermissionsRequest): TelegramResponse<Boolean>

    /**
     * exportChatInviteLink
     */
    @POST("exportChatInviteLink")
    public suspend fun exportChatInviteLink(@Body body: ExportChatInviteLinkRequest): TelegramResponse<String>

    /**
     * createChatInviteLink
     */
    @POST("createChatInviteLink")
    public suspend fun createChatInviteLink(@Body body: CreateChatInviteLinkRequest): TelegramResponse<ChatInviteLink>

    /**
     * editChatInviteLink
     */
    @POST("editChatInviteLink")
    public suspend fun editChatInviteLink(@Body body: EditChatInviteLinkRequest): TelegramResponse<ChatInviteLink>

    /**
     * createChatSubscriptionInviteLink
     */
    @POST("createChatSubscriptionInviteLink")
    public suspend fun createChatSubscriptionInviteLink(@Body body: CreateChatSubscriptionInviteLinkRequest): TelegramResponse<ChatInviteLink>

    /**
     * editChatSubscriptionInviteLink
     */
    @POST("editChatSubscriptionInviteLink")
    public suspend fun editChatSubscriptionInviteLink(@Body body: EditChatSubscriptionInviteLinkRequest): TelegramResponse<ChatInviteLink>

    /**
     * revokeChatInviteLink
     */
    @POST("revokeChatInviteLink")
    public suspend fun revokeChatInviteLink(@Body body: RevokeChatInviteLinkRequest): TelegramResponse<ChatInviteLink>

    /**
     * approveChatJoinRequest
     */
    @POST("approveChatJoinRequest")
    public suspend fun approveChatJoinRequest(@Body body: ApproveChatJoinRequestRequest): TelegramResponse<Boolean>

    /**
     * declineChatJoinRequest
     */
    @POST("declineChatJoinRequest")
    public suspend fun declineChatJoinRequest(@Body body: DeclineChatJoinRequestRequest): TelegramResponse<Boolean>

    /**
     * setChatPhoto
     */
    @POST("setChatPhoto")
    public suspend fun setChatPhoto(@Body formData: MultiPartFormDataContent): TelegramResponse<Boolean>

    /**
     * deleteChatPhoto
     */
    @POST("deleteChatPhoto")
    public suspend fun deleteChatPhoto(@Body body: DeleteChatPhotoRequest): TelegramResponse<Boolean>

    /**
     * setChatTitle
     */
    @POST("setChatTitle")
    public suspend fun setChatTitle(@Body body: SetChatTitleRequest): TelegramResponse<Boolean>

    /**
     * setChatDescription
     */
    @POST("setChatDescription")
    public suspend fun setChatDescription(@Body body: SetChatDescriptionRequest): TelegramResponse<Boolean>

    /**
     * pinChatMessage
     */
    @POST("pinChatMessage")
    public suspend fun pinChatMessage(@Body body: PinChatMessageRequest): TelegramResponse<Boolean>

    /**
     * unpinChatMessage
     */
    @POST("unpinChatMessage")
    public suspend fun unpinChatMessage(@Body body: UnpinChatMessageRequest): TelegramResponse<Boolean>

    /**
     * unpinAllChatMessages
     */
    @POST("unpinAllChatMessages")
    public suspend fun unpinAllChatMessages(@Body body: UnpinAllChatMessagesRequest): TelegramResponse<Boolean>

    /**
     * leaveChat
     */
    @POST("leaveChat")
    public suspend fun leaveChat(@Body body: LeaveChatRequest): TelegramResponse<Boolean>

    /**
     * getChat
     */
    @GET("getChat")
    public suspend fun getChat(@Query("chat_id") chatId: String): TelegramResponse<ChatFullInfo>

    /**
     * getChatAdministrators
     */
    @GET("getChatAdministrators")
    public suspend fun getChatAdministrators(@Query("chat_id") chatId: String): TelegramResponse<List<ChatMember>>

    /**
     * getChatMemberCount
     */
    @GET("getChatMemberCount")
    public suspend fun getChatMemberCount(@Query("chat_id") chatId: String): TelegramResponse<Long>

    /**
     * getChatMember
     */
    @GET("getChatMember")
    public suspend fun getChatMember(@Query("chat_id") chatId: String, @Query("user_id") userId: Long): TelegramResponse<ChatMember>

    /**
     * setChatStickerSet
     */
    @POST("setChatStickerSet")
    public suspend fun setChatStickerSet(@Body body: SetChatStickerSetRequest): TelegramResponse<Boolean>

    /**
     * deleteChatStickerSet
     */
    @POST("deleteChatStickerSet")
    public suspend fun deleteChatStickerSet(@Body body: DeleteChatStickerSetRequest): TelegramResponse<Boolean>

    /**
     * getForumTopicIconStickers
     */
    @GET("getForumTopicIconStickers")
    public suspend fun getForumTopicIconStickers(): TelegramResponse<List<Sticker>>

    /**
     * createForumTopic
     */
    @POST("createForumTopic")
    public suspend fun createForumTopic(@Body body: CreateForumTopicRequest): TelegramResponse<ForumTopic>

    /**
     * editForumTopic
     */
    @POST("editForumTopic")
    public suspend fun editForumTopic(@Body body: EditForumTopicRequest): TelegramResponse<Boolean>

    /**
     * closeForumTopic
     */
    @POST("closeForumTopic")
    public suspend fun closeForumTopic(@Body body: CloseForumTopicRequest): TelegramResponse<Boolean>

    /**
     * reopenForumTopic
     */
    @POST("reopenForumTopic")
    public suspend fun reopenForumTopic(@Body body: ReopenForumTopicRequest): TelegramResponse<Boolean>

    /**
     * deleteForumTopic
     */
    @POST("deleteForumTopic")
    public suspend fun deleteForumTopic(@Body body: DeleteForumTopicRequest): TelegramResponse<Boolean>

    /**
     * unpinAllForumTopicMessages
     */
    @POST("unpinAllForumTopicMessages")
    public suspend fun unpinAllForumTopicMessages(@Body body: UnpinAllForumTopicMessagesRequest): TelegramResponse<Boolean>

    /**
     * editGeneralForumTopic
     */
    @POST("editGeneralForumTopic")
    public suspend fun editGeneralForumTopic(@Body body: EditGeneralForumTopicRequest): TelegramResponse<Boolean>

    /**
     * closeGeneralForumTopic
     */
    @POST("closeGeneralForumTopic")
    public suspend fun closeGeneralForumTopic(@Body body: CloseGeneralForumTopicRequest): TelegramResponse<Boolean>

    /**
     * reopenGeneralForumTopic
     */
    @POST("reopenGeneralForumTopic")
    public suspend fun reopenGeneralForumTopic(@Body body: ReopenGeneralForumTopicRequest): TelegramResponse<Boolean>

    /**
     * hideGeneralForumTopic
     */
    @POST("hideGeneralForumTopic")
    public suspend fun hideGeneralForumTopic(@Body body: HideGeneralForumTopicRequest): TelegramResponse<Boolean>

    /**
     * unhideGeneralForumTopic
     */
    @POST("unhideGeneralForumTopic")
    public suspend fun unhideGeneralForumTopic(@Body body: UnhideGeneralForumTopicRequest): TelegramResponse<Boolean>

    /**
     * unpinAllGeneralForumTopicMessages
     */
    @POST("unpinAllGeneralForumTopicMessages")
    public suspend fun unpinAllGeneralForumTopicMessages(@Body body: UnpinAllGeneralForumTopicMessagesRequest): TelegramResponse<Boolean>

    /**
     * answerCallbackQuery
     */
    @POST("answerCallbackQuery")
    public suspend fun answerCallbackQuery(@Body body: AnswerCallbackQueryRequest): TelegramResponse<Boolean>

    /**
     * getUserChatBoosts
     */
    @GET("getUserChatBoosts")
    public suspend fun getUserChatBoosts(@Query("chat_id") chatId: String, @Query("user_id") userId: Long): TelegramResponse<UserChatBoosts>

    /**
     * getBusinessConnection
     */
    @GET("getBusinessConnection")
    public suspend fun getBusinessConnection(@Query("business_connection_id") businessConnectionId: String): TelegramResponse<BusinessConnection>

    /**
     * setMyCommands
     */
    @POST("setMyCommands")
    public suspend fun setMyCommands(@Body body: SetMyCommandsRequest): TelegramResponse<Boolean>

    /**
     * deleteMyCommands
     */
    @POST("deleteMyCommands")
    public suspend fun deleteMyCommands(@Body body: DeleteMyCommandsRequest): TelegramResponse<Boolean>

    /**
     * getMyCommands
     */
    @GET("getMyCommands")
    public suspend fun getMyCommands(@Query("scope") scope: BotCommandScope? = null, @Query("language_code") languageCode: String? = null): TelegramResponse<List<BotCommand>>

    /**
     * setMyName
     */
    @POST("setMyName")
    public suspend fun setMyName(@Body body: SetMyNameRequest): TelegramResponse<Boolean>

    /**
     * getMyName
     */
    @GET("getMyName")
    public suspend fun getMyName(@Query("language_code") languageCode: String? = null): TelegramResponse<BotName>

    /**
     * setMyDescription
     */
    @POST("setMyDescription")
    public suspend fun setMyDescription(@Body body: SetMyDescriptionRequest): TelegramResponse<Boolean>

    /**
     * getMyDescription
     */
    @GET("getMyDescription")
    public suspend fun getMyDescription(@Query("language_code") languageCode: String? = null): TelegramResponse<BotDescription>

    /**
     * setMyShortDescription
     */
    @POST("setMyShortDescription")
    public suspend fun setMyShortDescription(@Body body: SetMyShortDescriptionRequest): TelegramResponse<Boolean>

    /**
     * getMyShortDescription
     */
    @GET("getMyShortDescription")
    public suspend fun getMyShortDescription(@Query("language_code") languageCode: String? = null): TelegramResponse<BotShortDescription>

    /**
     * setChatMenuButton
     */
    @POST("setChatMenuButton")
    public suspend fun setChatMenuButton(@Body body: SetChatMenuButtonRequest): TelegramResponse<Boolean>

    /**
     * getChatMenuButton
     */
    @GET("getChatMenuButton")
    public suspend fun getChatMenuButton(@Query("chat_id") chatId: Long? = null): TelegramResponse<MenuButton>

    /**
     * setMyDefaultAdministratorRights
     */
    @POST("setMyDefaultAdministratorRights")
    public suspend fun setMyDefaultAdministratorRights(@Body body: SetMyDefaultAdministratorRightsRequest): TelegramResponse<Boolean>

    /**
     * getMyDefaultAdministratorRights
     */
    @GET("getMyDefaultAdministratorRights")
    public suspend fun getMyDefaultAdministratorRights(@Query("for_channels") forChannels: Boolean? = null): TelegramResponse<ChatAdministratorRights>

    /**
     * getAvailableGifts
     */
    @GET("getAvailableGifts")
    public suspend fun getAvailableGifts(): TelegramResponse<Gifts>

    /**
     * sendGift
     */
    @POST("sendGift")
    public suspend fun sendGift(@Body body: SendGiftRequest): TelegramResponse<Boolean>

    /**
     * giftPremiumSubscription
     */
    @POST("giftPremiumSubscription")
    public suspend fun giftPremiumSubscription(@Body body: GiftPremiumSubscriptionRequest): TelegramResponse<Boolean>

    /**
     * verifyUser
     */
    @POST("verifyUser")
    public suspend fun verifyUser(@Body body: VerifyUserRequest): TelegramResponse<Boolean>

    /**
     * verifyChat
     */
    @POST("verifyChat")
    public suspend fun verifyChat(@Body body: VerifyChatRequest): TelegramResponse<Boolean>

    /**
     * removeUserVerification
     */
    @POST("removeUserVerification")
    public suspend fun removeUserVerification(@Body body: RemoveUserVerificationRequest): TelegramResponse<Boolean>

    /**
     * removeChatVerification
     */
    @POST("removeChatVerification")
    public suspend fun removeChatVerification(@Body body: RemoveChatVerificationRequest): TelegramResponse<Boolean>

    /**
     * readBusinessMessage
     */
    @POST("readBusinessMessage")
    public suspend fun readBusinessMessage(@Body body: ReadBusinessMessageRequest): TelegramResponse<Boolean>

    /**
     * deleteBusinessMessages
     */
    @POST("deleteBusinessMessages")
    public suspend fun deleteBusinessMessages(@Body body: DeleteBusinessMessagesRequest): TelegramResponse<Boolean>

    /**
     * setBusinessAccountName
     */
    @POST("setBusinessAccountName")
    public suspend fun setBusinessAccountName(@Body body: SetBusinessAccountNameRequest): TelegramResponse<Boolean>

    /**
     * setBusinessAccountUsername
     */
    @POST("setBusinessAccountUsername")
    public suspend fun setBusinessAccountUsername(@Body body: SetBusinessAccountUsernameRequest): TelegramResponse<Boolean>

    /**
     * setBusinessAccountBio
     */
    @POST("setBusinessAccountBio")
    public suspend fun setBusinessAccountBio(@Body body: SetBusinessAccountBioRequest): TelegramResponse<Boolean>

    /**
     * setBusinessAccountProfilePhoto
     */
    @POST("setBusinessAccountProfilePhoto")
    public suspend fun setBusinessAccountProfilePhoto(@Body body: SetBusinessAccountProfilePhotoRequest): TelegramResponse<Boolean>

    /**
     * removeBusinessAccountProfilePhoto
     */
    @POST("removeBusinessAccountProfilePhoto")
    public suspend fun removeBusinessAccountProfilePhoto(@Body body: RemoveBusinessAccountProfilePhotoRequest): TelegramResponse<Boolean>

    /**
     * setBusinessAccountGiftSettings
     */
    @POST("setBusinessAccountGiftSettings")
    public suspend fun setBusinessAccountGiftSettings(@Body body: SetBusinessAccountGiftSettingsRequest): TelegramResponse<Boolean>

    /**
     * getBusinessAccountStarBalance
     */
    @GET("getBusinessAccountStarBalance")
    public suspend fun getBusinessAccountStarBalance(@Query("business_connection_id") businessConnectionId: String): TelegramResponse<StarAmount>

    /**
     * transferBusinessAccountStars
     */
    @POST("transferBusinessAccountStars")
    public suspend fun transferBusinessAccountStars(@Body body: TransferBusinessAccountStarsRequest): TelegramResponse<Boolean>

    /**
     * getBusinessAccountGifts
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
        @Query("offset") offset: String? = null,
        @Query("limit") limit: Long? = null,
    ): TelegramResponse<OwnedGifts>

    /**
     * getUserGifts
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
        @Query("offset") offset: String? = null,
        @Query("limit") limit: Long? = null,
    ): TelegramResponse<OwnedGifts>

    /**
     * getChatGifts
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
        @Query("offset") offset: String? = null,
        @Query("limit") limit: Long? = null,
    ): TelegramResponse<OwnedGifts>

    /**
     * convertGiftToStars
     */
    @POST("convertGiftToStars")
    public suspend fun convertGiftToStars(@Body body: ConvertGiftToStarsRequest): TelegramResponse<Boolean>

    /**
     * upgradeGift
     */
    @POST("upgradeGift")
    public suspend fun upgradeGift(@Body body: UpgradeGiftRequest): TelegramResponse<Boolean>

    /**
     * transferGift
     */
    @POST("transferGift")
    public suspend fun transferGift(@Body body: TransferGiftRequest): TelegramResponse<Boolean>

    /**
     * postStory
     */
    @POST("postStory")
    public suspend fun postStory(@Body body: PostStoryRequest): TelegramResponse<Story>

    /**
     * repostStory
     */
    @POST("repostStory")
    public suspend fun repostStory(@Body body: RepostStoryRequest): TelegramResponse<Story>

    /**
     * editStory
     */
    @POST("editStory")
    public suspend fun editStory(@Body body: EditStoryRequest): TelegramResponse<Story>

    /**
     * deleteStory
     */
    @POST("deleteStory")
    public suspend fun deleteStory(@Body body: DeleteStoryRequest): TelegramResponse<Boolean>

    /**
     * editMessageText
     */
    @POST("editMessageText")
    public suspend fun editMessageText(@Body body: EditMessageTextRequest): TelegramResponse<Boolean>

    /**
     * editMessageCaption
     */
    @POST("editMessageCaption")
    public suspend fun editMessageCaption(@Body body: EditMessageCaptionRequest): TelegramResponse<Boolean>

    /**
     * editMessageMedia
     */
    @POST("editMessageMedia")
    public suspend fun editMessageMedia(@Body formData: MultiPartFormDataContent): TelegramResponse<Boolean>

    /**
     * editMessageLiveLocation
     */
    @POST("editMessageLiveLocation")
    public suspend fun editMessageLiveLocation(@Body body: EditMessageLiveLocationRequest): TelegramResponse<Boolean>

    /**
     * stopMessageLiveLocation
     */
    @POST("stopMessageLiveLocation")
    public suspend fun stopMessageLiveLocation(@Body body: StopMessageLiveLocationRequest): TelegramResponse<Boolean>

    /**
     * editMessageChecklist
     */
    @POST("editMessageChecklist")
    public suspend fun editMessageChecklist(@Body body: EditMessageChecklistRequest): TelegramResponse<Message>

    /**
     * editMessageReplyMarkup
     */
    @POST("editMessageReplyMarkup")
    public suspend fun editMessageReplyMarkup(@Body body: EditMessageReplyMarkupRequest): TelegramResponse<Boolean>

    /**
     * stopPoll
     */
    @POST("stopPoll")
    public suspend fun stopPoll(@Body body: StopPollRequest): TelegramResponse<Poll>

    /**
     * approveSuggestedPost
     */
    @POST("approveSuggestedPost")
    public suspend fun approveSuggestedPost(@Body body: ApproveSuggestedPostRequest): TelegramResponse<Boolean>

    /**
     * declineSuggestedPost
     */
    @POST("declineSuggestedPost")
    public suspend fun declineSuggestedPost(@Body body: DeclineSuggestedPostRequest): TelegramResponse<Boolean>

    /**
     * deleteMessage
     */
    @POST("deleteMessage")
    public suspend fun deleteMessage(@Body body: DeleteMessageRequest): TelegramResponse<Boolean>

    /**
     * deleteMessages
     */
    @POST("deleteMessages")
    public suspend fun deleteMessages(@Body body: DeleteMessagesRequest): TelegramResponse<Boolean>

    /**
     * sendSticker
     */
    @POST("sendSticker")
    public suspend fun sendSticker(@Body formData: MultiPartFormDataContent): TelegramResponse<Message>

    /**
     * getStickerSet
     */
    @GET("getStickerSet")
    public suspend fun getStickerSet(@Query("name") name: String): TelegramResponse<StickerSet>

    /**
     * getCustomEmojiStickers
     */
    @GET("getCustomEmojiStickers")
    public suspend fun getCustomEmojiStickers(@Query("custom_emoji_ids") customEmojiIds: List<String>): TelegramResponse<List<Sticker>>

    /**
     * uploadStickerFile
     */
    @POST("uploadStickerFile")
    public suspend fun uploadStickerFile(@Body formData: MultiPartFormDataContent): TelegramResponse<File>

    /**
     * createNewStickerSet
     */
    @POST("createNewStickerSet")
    public suspend fun createNewStickerSet(@Body body: CreateNewStickerSetRequest): TelegramResponse<Boolean>

    /**
     * addStickerToSet
     */
    @POST("addStickerToSet")
    public suspend fun addStickerToSet(@Body body: AddStickerToSetRequest): TelegramResponse<Boolean>

    /**
     * setStickerPositionInSet
     */
    @POST("setStickerPositionInSet")
    public suspend fun setStickerPositionInSet(@Body body: SetStickerPositionInSetRequest): TelegramResponse<Boolean>

    /**
     * deleteStickerFromSet
     */
    @POST("deleteStickerFromSet")
    public suspend fun deleteStickerFromSet(@Body body: DeleteStickerFromSetRequest): TelegramResponse<Boolean>

    /**
     * replaceStickerInSet
     */
    @POST("replaceStickerInSet")
    public suspend fun replaceStickerInSet(@Body body: ReplaceStickerInSetRequest): TelegramResponse<Boolean>

    /**
     * setStickerEmojiList
     */
    @POST("setStickerEmojiList")
    public suspend fun setStickerEmojiList(@Body body: SetStickerEmojiListRequest): TelegramResponse<Boolean>

    /**
     * setStickerKeywords
     */
    @POST("setStickerKeywords")
    public suspend fun setStickerKeywords(@Body body: SetStickerKeywordsRequest): TelegramResponse<Boolean>

    /**
     * setStickerMaskPosition
     */
    @POST("setStickerMaskPosition")
    public suspend fun setStickerMaskPosition(@Body body: SetStickerMaskPositionRequest): TelegramResponse<Boolean>

    /**
     * setStickerSetTitle
     */
    @POST("setStickerSetTitle")
    public suspend fun setStickerSetTitle(@Body body: SetStickerSetTitleRequest): TelegramResponse<Boolean>

    /**
     * setStickerSetThumbnail
     */
    @POST("setStickerSetThumbnail")
    public suspend fun setStickerSetThumbnail(@Body formData: MultiPartFormDataContent): TelegramResponse<Boolean>

    /**
     * setCustomEmojiStickerSetThumbnail
     */
    @POST("setCustomEmojiStickerSetThumbnail")
    public suspend fun setCustomEmojiStickerSetThumbnail(@Body body: SetCustomEmojiStickerSetThumbnailRequest): TelegramResponse<Boolean>

    /**
     * deleteStickerSet
     */
    @POST("deleteStickerSet")
    public suspend fun deleteStickerSet(@Body body: DeleteStickerSetRequest): TelegramResponse<Boolean>

    /**
     * answerInlineQuery
     */
    @POST("answerInlineQuery")
    public suspend fun answerInlineQuery(@Body body: AnswerInlineQueryRequest): TelegramResponse<Boolean>

    /**
     * answerWebAppQuery
     */
    @POST("answerWebAppQuery")
    public suspend fun answerWebAppQuery(@Body body: AnswerWebAppQueryRequest): TelegramResponse<SentWebAppMessage>

    /**
     * savePreparedInlineMessage
     */
    @POST("savePreparedInlineMessage")
    public suspend fun savePreparedInlineMessage(@Body body: SavePreparedInlineMessageRequest): TelegramResponse<PreparedInlineMessage>

    /**
     * sendInvoice
     */
    @POST("sendInvoice")
    public suspend fun sendInvoice(@Body body: SendInvoiceRequest): TelegramResponse<Message>

    /**
     * createInvoiceLink
     */
    @POST("createInvoiceLink")
    public suspend fun createInvoiceLink(@Body body: CreateInvoiceLinkRequest): TelegramResponse<String>

    /**
     * answerShippingQuery
     */
    @POST("answerShippingQuery")
    public suspend fun answerShippingQuery(@Body body: AnswerShippingQueryRequest): TelegramResponse<Boolean>

    /**
     * answerPreCheckoutQuery
     */
    @POST("answerPreCheckoutQuery")
    public suspend fun answerPreCheckoutQuery(@Body body: AnswerPreCheckoutQueryRequest): TelegramResponse<Boolean>

    /**
     * getMyStarBalance
     */
    @GET("getMyStarBalance")
    public suspend fun getMyStarBalance(): TelegramResponse<StarAmount>

    /**
     * getStarTransactions
     */
    @GET("getStarTransactions")
    public suspend fun getStarTransactions(@Query("offset") offset: Long? = null, @Query("limit") limit: Long? = null): TelegramResponse<StarTransactions>

    /**
     * refundStarPayment
     */
    @POST("refundStarPayment")
    public suspend fun refundStarPayment(@Body body: RefundStarPaymentRequest): TelegramResponse<Boolean>

    /**
     * editUserStarSubscription
     */
    @POST("editUserStarSubscription")
    public suspend fun editUserStarSubscription(@Body body: EditUserStarSubscriptionRequest): TelegramResponse<Boolean>

    /**
     * setPassportDataErrors
     */
    @POST("setPassportDataErrors")
    public suspend fun setPassportDataErrors(@Body body: SetPassportDataErrorsRequest): TelegramResponse<Boolean>

    /**
     * sendGame
     */
    @POST("sendGame")
    public suspend fun sendGame(@Body body: SendGameRequest): TelegramResponse<Message>

    /**
     * setGameScore
     */
    @POST("setGameScore")
    public suspend fun setGameScore(@Body body: SetGameScoreRequest): TelegramResponse<Boolean>

    /**
     * getGameHighScores
     */
    @GET("getGameHighScores")
    public suspend fun getGameHighScores(
        @Query("user_id") userId: Long,
        @Query("chat_id") chatId: Long? = null,
        @Query("message_id") messageId: Long? = null,
        @Query("inline_message_id") inlineMessageId: String? = null,
    ): TelegramResponse<List<GameHighScore>>
}
