package com.group10.uxuiapp.data.data_class

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TaskItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val label: String,
    val isComplete: Boolean = false,
    val todoListId: Int
)