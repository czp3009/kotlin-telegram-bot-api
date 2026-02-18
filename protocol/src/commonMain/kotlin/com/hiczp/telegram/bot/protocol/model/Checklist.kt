// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.model

import kotlin.Boolean
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Describes a checklist.
 */
@Serializable
public data class Checklist(
    /**
     * Title of the checklist
     */
    public val title: String,
    /**
     * *Optional*. Special entities that appear in the checklist title
     */
    @SerialName("title_entities")
    public val titleEntities: List<MessageEntity>? = null,
    /**
     * List of tasks in the checklist
     */
    public val tasks: List<ChecklistTask>,
    /**
     * *Optional*. *True*, if users other than the creator of the list can add tasks to the list
     */
    @SerialName("others_can_add_tasks")
    public val othersCanAddTasks: Boolean? = null,
    /**
     * *Optional*. *True*, if users other than the creator of the list can mark tasks as done or not done
     */
    @SerialName("others_can_mark_tasks_as_done")
    public val othersCanMarkTasksAsDone: Boolean? = null,
)
