package com.group10.uxuiapp.data

import kotlin.collections.List

data class TaskList(
    var index: Int,
    var title: String,
    var task: List<TaskItem> = emptyList() // Default to empty list
)
