// Auto-generated from Swagger specificationDo not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlin.Boolean
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class SetMyDefaultAdministratorRightsRequest(
    /**
     * A JSON-serialized object describing new default administrator rights. If not specified, the default administrator rights will be cleared.
     */
    public val rights: ChatAdministratorRights? = null,
    /**
     * Pass *True* to change the default administrator rights of the bot in channels. Otherwise, the default administrator rights of the bot for groups and supergroups will be changed.
     */
    @SerialName("for_channels")
    public val forChannels: Boolean? = null,
)
