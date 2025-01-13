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
    fun doesMatchSearchQuery(query: String): Boolean {
        val matchCombinations = listOf(
            todoList.title,
            todoList.tags,
            taskItems.joinToString { it.label }
        )
        return matchCombinations.any { it.contains(query, ignoreCase = true) }
    }
}
