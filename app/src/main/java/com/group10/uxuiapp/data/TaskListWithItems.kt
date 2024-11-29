package com.group10.uxuiapp.data

import androidx.room.Embedded
import androidx.room.Relation

data class TaskListWithItems(
    @Embedded val taskList: TaskList,
    @Relation(
        parentColumn = "id",
        entityColumn = "taskListId"
    )
    val taskItems: List<TaskItem>
)