package com.group10.uxuiapp.data

import androidx.compose.runtime.mutableStateOf

data class TaskItem(
    var label: String,
    var isComplete: Boolean = false
) {
    // Make isComplete mutable state
    var isChecked = mutableStateOf(isComplete)
}