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
)
