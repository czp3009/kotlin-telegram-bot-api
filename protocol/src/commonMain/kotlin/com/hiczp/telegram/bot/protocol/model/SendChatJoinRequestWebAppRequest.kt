// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class SendChatJoinRequestWebAppRequest(
    /**
     * Unique identifier of the join request query
     */
    @SerialName("chat_join_request_query_id")
    public val chatJoinRequestQueryId: String,
    /**
     * The URL of the Mini App to be opened
     */
    @SerialName("web_app_url")
    public val webAppUrl: String,
)
