package com.group10.uxuiapp.ui.todolist.view.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TagsDisplay(tags: String?, color: androidx.compose.ui.graphics.Color) {
    if (!tags.isNullOrEmpty()) {
        Text(
            text = tags.replace(",", " • "), // Using bullet points to separate tags
            color = color,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}
