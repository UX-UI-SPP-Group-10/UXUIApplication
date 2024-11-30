package com.group10.uxuiapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TaskItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val label: String,
    val isComplete: Boolean = false,
    val taskListId: Int
)