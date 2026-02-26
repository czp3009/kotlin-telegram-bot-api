package com.hiczp.telegram.bot.application.context

import com.hiczp.telegram.bot.protocol.event.BusinessConnectionEvent
import com.hiczp.telegram.bot.protocol.form.setBusinessAccountProfilePhoto
import com.hiczp.telegram.bot.protocol.model.*
import io.ktor.client.request.forms.*

suspend fun TelegramBotEventContext<BusinessConnectionEvent>.setBusinessAccountName(
    firstName: String,
    lastName: String? = null,
) = client.setBusinessAccountName(
    businessConnectionId = event.businessConnection.id,
    firstName = firstName,
    lastName = lastName,
)

suspend fun TelegramBotEventContext<BusinessConnectionEvent>.setBusinessAccountUsername(
    username: String? = null,
) = client.setBusinessAccountUsername(
    businessConnectionId = event.businessConnection.id,
    username = username,
)

suspend fun TelegramBotEventContext<BusinessConnectionEvent>.setBusinessAccountBio(
    bio: String? = null,
) = client.setBusinessAccountBio(
    businessConnectionId = event.businessConnection.id,
    bio = bio,
)

suspend fun TelegramBotEventContext<BusinessConnectionEvent>.setBusinessAccountProfilePhoto(
    photo: InputProfilePhoto,
    isPublic: Boolean? = null,
    attachments: List<FormPart<ChannelProvider>>? = null,
) = client.setBusinessAccountProfilePhoto(
    businessConnectionId = event.businessConnection.id,
    photo = photo,
    isPublic = isPublic,
    attachments = attachments,
)

suspend fun TelegramBotEventContext<BusinessConnectionEvent>.removeBusinessAccountProfilePhoto(
    isPublic: Boolean? = null,
) = client.removeBusinessAccountProfilePhoto(
    businessConnectionId = event.businessConnection.id,
    isPublic = isPublic,
)

suspend fun TelegramBotEventContext<BusinessConnectionEvent>.setBusinessAccountGiftSettings(
    showGiftButton: Boolean,
    acceptedGiftTypes: AcceptedGiftTypes,
) = client.setBusinessAccountGiftSettings(
    businessConnectionId = event.businessConnection.id,
    showGiftButton = showGiftButton,
    acceptedGiftTypes = acceptedGiftTypes,
)

suspend fun TelegramBotEventContext<BusinessConnectionEvent>.transferBusinessAccountStars(
    starCount: Long,
) = client.transferBusinessAccountStars(
    businessConnectionId = event.businessConnection.id,
    starCount = starCount,
)
