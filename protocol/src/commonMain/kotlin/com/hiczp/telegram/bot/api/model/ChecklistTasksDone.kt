// Auto-generated from Swagger specificationDo not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlin.Long
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

/**
 * Describes a service message about checklist tasks marked as done or not done.
 */
@Serializable
public data class ChecklistTasksDone(
    /**
     * *Optional*. Message containing the checklist whose tasks were marked as done or not done. Note that the [Message](https://core.telegram.org/bots/api#message) object in this field will not contain the *reply_to_message* field even if it itself is a reply.
     */
    @SerialName("checklist_message")
    public val checklistMessage: JsonElement? = null,
    /**
     * *Optional*. Identifiers of the tasks that were marked as done
     */
    @SerialName("marked_as_done_task_ids")
    public val markedAsDoneTaskIds: List<Long>? = null,
    /**
     * *Optional*. Identifiers of the tasks that were marked as not done
     */
    @SerialName("marked_as_not_done_task_ids")
    public val markedAsNotDoneTaskIds: List<Long>? = null,
)
