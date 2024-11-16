package com.group10.uxuiapp.data

data class TaskList(
    var index: Int,
    var title: String,
    var task: MutableList<TaskItem> = mutableListOf(),
    var isLiked: Boolean = false
)