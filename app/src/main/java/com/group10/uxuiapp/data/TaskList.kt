package com.group10.uxuiapp.data

import androidx.compose.runtime.mutableStateListOf

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TaskList(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val isLiked: Boolean = false
)
