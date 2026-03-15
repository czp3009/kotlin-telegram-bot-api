package com.hiczp.telegram.bot.application.compose.message.applier

import androidx.compose.runtime.AbstractApplier
import com.hiczp.telegram.bot.application.compose.message.node.*

/**
 * Exception thrown when an invalid node operation is attempted in the message tree.
 */
class MessageApplierException(
    message: String,
    cause: Throwable? = null
) : RuntimeException(message, cause)

/**
 * Applier for building message node trees in Compose.
 *
 * This applier follows the standard Compose pattern with top-down insertion.
 * The key feature is [onEndChanges] which is called when composition reaches
 * a steady state, allowing batched API updates.
 *
 * Tree structure:
 * ```
 * RootMessageNode
 *   └── InlineKeyboardNode | ReplyKeyboardNode | ForceReplyNode | ReplyKeyboardRemoveNode
 *         └── RowNode* (for Inline/Reply keyboards)
 *               └── ButtonNode* (for inline keyboards with onClick callbacks)
 * ```
 *
 * @param root The root node of the message tree.
 * @param onSteadyState Callback invoked when composition reaches a steady state.
 *        This is the ideal time to commit changes to the Telegram API.
 */
class MessageApplier(
    private val rootNode: RootMessageNode,
    private val onSteadyState: (RootMessageNode) -> Unit = {},
) : AbstractApplier<MessageNode>(rootNode) {

    override fun insertTopDown(index: Int, instance: MessageNode) {
        try {
            when (val parent = current) {
                is RootMessageNode -> {
                    when (instance) {
                        is KeyboardNode -> parent.keyboard = instance
                        else -> throw MessageApplierException(
                            "RootMessageNode can only contain KeyboardNode, got ${instance::class.simpleName}"
                        )
                    }
                }

                is InlineKeyboardNode -> {
                    require(instance is InlineKeyboardRowNode) {
                        "InlineKeyboardNode can only contain InlineKeyboardRowNode, got ${instance::class.simpleName}"
                    }
                    parent.rows.add(index, instance)
                }

                is ReplyKeyboardNode -> {
                    require(instance is ReplyKeyboardRowNode) {
                        "ReplyKeyboardNode can only contain ReplyKeyboardRowNode, got ${instance::class.simpleName}"
                    }
                    parent.rows.add(index, instance)
                }

                is InlineKeyboardRowNode -> {
                    require(instance is ButtonNode) {
                        "InlineKeyboardRowNode can only contain ButtonNode, got ${instance::class.simpleName}"
                    }
                    parent.buttons.add(index, instance)
                }

                else -> throw MessageApplierException(
                    "Cannot insert ${instance::class.simpleName} into ${parent::class.simpleName}"
                )
            }
            instance.parent = current
        } catch (e: MessageApplierException) {
            throw e
        } catch (e: Exception) {
            throw MessageApplierException(
                "Failed to insert ${instance::class.simpleName} at index $index",
                e
            )
        }
    }

    override fun insertBottomUp(index: Int, instance: MessageNode) {
        // Ignored as the tree is built top-down (standard pattern from official examples)
    }

    override fun remove(index: Int, count: Int) {
        try {
            when (val parent = current) {
                is RootMessageNode -> {
                    parent.keyboard = null
                }

                is InlineKeyboardNode -> {
                    repeat(count) { parent.rows.removeAt(index) }
                }

                is ReplyKeyboardNode -> {
                    repeat(count) { parent.rows.removeAt(index) }
                }

                is InlineKeyboardRowNode -> {
                    repeat(count) { parent.buttons.removeAt(index) }
                }

                else -> throw MessageApplierException(
                    "Cannot remove nodes from ${parent::class.simpleName}"
                )
            }
        } catch (e: IndexOutOfBoundsException) {
            throw MessageApplierException(
                "Failed to remove $count nodes at index $index from ${current::class.simpleName}",
                e
            )
        }
    }

    override fun move(from: Int, to: Int, count: Int) {
        when (val parent = current) {
            is InlineKeyboardNode -> {
                parent.rows.move(from, to, count)
            }

            is ReplyKeyboardNode -> {
                parent.rows.move(from, to, count)
            }

            is InlineKeyboardRowNode -> {
                parent.buttons.move(from, to, count)
            }

            else -> { /* Other node types don't support move */
            }
        }
    }

    override fun onClear() {
        rootNode.keyboard = null
        rootNode.text = ""
    }

    /**
     * Called when a complete recomposition cycle finishes (steady state reached).
     * This is the ideal time to commit changes to the Telegram API as all state
     * changes have been batched together.
     */
    override fun onEndChanges() {
        super.onEndChanges()
        onSteadyState(rootNode)
    }
}

/**
 * Extension function to move elements in a MutableList.
 */
private fun <T> MutableList<T>.move(from: Int, to: Int, count: Int) {
    if (from > to) {
        repeat(count) { i -> add(to + i, removeAt(from + i)) }
    } else {
        repeat(count) { add(to + count - 1, removeAt(from)) }
    }
}
