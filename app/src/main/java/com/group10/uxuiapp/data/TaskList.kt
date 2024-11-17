package com.group10.uxuiapp.data

import androidx.compose.runtime.mutableStateListOf

data class TaskList(
    var index: Int,
    var title: String,
    var taskItemList: MutableList<TaskItem> = mutableStateListOf(),
    var isLiked: Boolean = false
)