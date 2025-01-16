package com.group10.uxuiapp.data.data_class

import androidx.room.Embedded
import androidx.room.Relation

data class TodoListWithTaskItem(
    @Embedded val todoList: TodoList,
    @Relation(
        parentColumn = "id",
        entityColumn = "todoListId"
    )
    val taskItems: List<TaskItem>
) {
    fun doesMatchSearchQuery(query: String, taskItemsWithSubTasks: List<TaskItemWithSubTask>): Boolean {
        val subTasks = taskItemsWithSubTasks.flatMap { it.subTasks }
        val matchCombinations = listOf(
            todoList.title,
            todoList.tags,
            taskItems.joinToString { it.label }, // Search in task item labels
            subTasks.joinToString { it.label }  // Search in subtask labels
        )
        return matchCombinations.any { it.contains(query, ignoreCase = true) }
    }
}
