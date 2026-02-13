// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlin.Boolean
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Describes a checklist to create.
 */
@Serializable
public data class InputChecklist(
    /**
     * Title of the checklist; 1-255 characters after entities parsing
     */
    public val title: String,
    /**
     * *Optional*. Mode for parsing entities in the title. See [formatting options](https://core.telegram.org/bots/api#formatting-options) for more details.
     */
    @SerialName("parse_mode")
    public val parseMode: String? = null,
    /**
     * *Optional*. List of special entities that appear in the title, which can be specified instead of parse_mode. Currently, only *bold*, *italic*, *underline*, *strikethrough*, *spoiler*, and *custom_emoji* entities are allowed.
     */
    @SerialName("title_entities")
    public val titleEntities: List<MessageEntity>? = null,
    /**
     * List of 1-30 tasks in the checklist
     */
    public val tasks: List<InputChecklistTask>,
    /**
     * *Optional*. Pass *True* if other users can add tasks to the checklist
     */
    @SerialName("others_can_add_tasks")
    public val othersCanAddTasks: Boolean? = null,
    /**
     * *Optional*. Pass *True* if other users can mark tasks as done or not done in the checklist
     */
    @SerialName("others_can_mark_tasks_as_done")
    public val othersCanMarkTasksAsDone: Boolean? = null,
)
