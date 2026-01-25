// Auto-generated from Swagger specificationDo not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

/**
 * Describes a task in a checklist.
 */
@Serializable
public data class ChecklistTask(
    /**
     * Unique identifier of the task
     */
    public val id: Long,
    /**
     * Text of the task
     */
    public val text: String,
    /**
     * *Optional*. Special entities that appear in the task text
     */
    @SerialName("text_entities")
    public val textEntities: List<MessageEntity>? = null,
    /**
     * *Optional*. User that completed the task; omitted if the task wasn't completed by a user
     */
    @SerialName("completed_by_user")
    public val completedByUser: JsonElement? = null,
    /**
     * *Optional*. Chat that completed the task; omitted if the task wasn't completed by a chat
     */
    @SerialName("completed_by_chat")
    public val completedByChat: JsonElement? = null,
    /**
     * *Optional*. Point in time (Unix timestamp) when the task was completed; 0 if the task wasn't completed
     */
    @SerialName("completion_date")
    public val completionDate: Long? = null,
)
