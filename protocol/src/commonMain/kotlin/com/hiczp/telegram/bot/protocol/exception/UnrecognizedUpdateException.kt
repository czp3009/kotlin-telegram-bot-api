package com.hiczp.telegram.bot.protocol.exception

import com.hiczp.telegram.bot.protocol.model.Update
import kotlinx.serialization.Serializable

@Serializable
data class UnrecognizedUpdateException(
    val update: Update,
) : IllegalStateException() {
    override val message
        get() = "Unrecognized update: $update"
}
