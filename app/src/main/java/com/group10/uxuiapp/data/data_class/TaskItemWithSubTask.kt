package com.group10.uxuiapp.data.data_class

import androidx.room.Embedded
import androidx.room.Relation

data class TaskItemWithSubTask(
    @Embedded val taskItem: TaskItem,
    @Relation(
        parentColumn = "id",
        entityColumn = "taskItemId"
    )
    val subTasks: List<SubTask>
)
