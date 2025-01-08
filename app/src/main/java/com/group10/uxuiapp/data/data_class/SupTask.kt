package com.group10.uxuiapp.data.data_class

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = TaskItem::class,
            parentColumns = ["id"],
            childColumns = ["TaskItemId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)

data class SupTask(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val label: String,
    val isComplete: Boolean = false,
    val taskItemId: Int // Foreign key to TaskItemId
)
