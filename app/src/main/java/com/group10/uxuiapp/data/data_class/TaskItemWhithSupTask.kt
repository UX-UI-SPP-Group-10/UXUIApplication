package com.group10.uxuiapp.data.data_class

import androidx.room.Embedded
import androidx.room.Relation

data class TaskItemWhithSupTask(
    @Embedded val taskItem: TaskItem,
    @Relation(
        parentColumn = "id",
        entityColumn = "TaskItemId"
    )
    val subTasks: List<SupTask>
)
