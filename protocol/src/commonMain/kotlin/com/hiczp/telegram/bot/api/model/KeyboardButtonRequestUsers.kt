// Auto-generated from Swagger specificationDo not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlin.Boolean
import kotlin.Long
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * This object defines the criteria used to request suitable users. Information about the selected users will be shared with the bot when the corresponding button is pressed. More about requesting users
 */
@Serializable
public data class KeyboardButtonRequestUsers(
    /**
     * Signed 32-bit identifier of the request that will be received back in the [UsersShared](https://core.telegram.org/bots/api#usersshared) object. Must be unique within the message
     */
    @SerialName("request_id")
    public val requestId: Long,
    /**
     * *Optional*. Pass *True* to request bots, pass *False* to request regular users. If not specified, no additional restrictions are applied.
     */
    @SerialName("user_is_bot")
    public val userIsBot: Boolean? = null,
    /**
     * *Optional*. Pass *True* to request premium users, pass *False* to request non-premium users. If not specified, no additional restrictions are applied.
     */
    @SerialName("user_is_premium")
    public val userIsPremium: Boolean? = null,
    /**
     * *Optional*. The maximum number of users to be selected; 1-10. Defaults to 1.
     */
    @SerialName("max_quantity")
    public val maxQuantity: Long? = null,
    /**
     * *Optional*. Pass *True* to request the users' first and last names
     */
    @SerialName("request_name")
    public val requestName: Boolean? = null,
    /**
     * *Optional*. Pass *True* to request the users' usernames
     */
    @SerialName("request_username")
    public val requestUsername: Boolean? = null,
    /**
     * *Optional*. Pass *True* to request the users' photos
     */
    @SerialName("request_photo")
    public val requestPhoto: Boolean? = null,
)
