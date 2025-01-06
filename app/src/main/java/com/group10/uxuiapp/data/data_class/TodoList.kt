package com.group10.uxuiapp.data.data_class

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TodoList(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var title: String,
    val isLiked: Boolean = false
)
