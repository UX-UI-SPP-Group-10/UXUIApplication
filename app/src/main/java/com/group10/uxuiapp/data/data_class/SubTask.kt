package com.group10.uxuiapp.data.data_class

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = TaskItem::class,
            parentColumns = ["id"],
            childColumns = ["taskItemId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)

data class SubTask(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val label: String,
    val isComplete: Boolean = false,
    val taskItemId: Int // Foreign key to TaskItemId
)
