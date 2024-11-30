package com.group10.uxuiapp.view.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.group10.uxuiapp.data.TaskItem
import com.group10.uxuiapp.view_model.ListViewModel

@Composable
fun TaskRowItem(task: TaskItem, viewModel: ListViewModel) {
    var isChecked by remember { mutableStateOf(task.isComplete) }
    var text by remember { mutableStateOf(task.label) }

    // Main container
    Box(
        modifier = Modifier
            .padding(vertical = 6.dp, horizontal = 12.dp)
            .height(40.dp) // Increased height for better usability
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.tertiary, // Background color
                shape = MaterialTheme.shapes.small // Subtle corner rounding
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp), // Space between checkbox and text
            modifier = Modifier.fillMaxWidth()
        ) {
            // Checkbox
            Checkbox(
                checked = isChecked,
                onCheckedChange = {
                    isChecked = it
                    viewModel.toggleIsCompleted(task)
                },
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    uncheckedColor = MaterialTheme.colorScheme.onSurface,
                    checkmarkColor = MaterialTheme.colorScheme.tertiary
                ),
                modifier = Modifier.size(28.dp) // Adjust checkbox size
            )

            // Editable text
            BasicTextField(
                value = text,
                onValueChange = {
                    text = it
                    viewModel.updateTaskLabel(task, it)
                },
                textStyle = MaterialTheme.typography.titleMedium.copy(
                    textDecoration = if (isChecked) TextDecoration.LineThrough else null, // Add strikethrough if checked
                    color = if (isChecked) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f) else MaterialTheme.colorScheme.onSurface // Dim color if checked
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default,
                keyboardActions = KeyboardActions.Default,
                modifier = Modifier
                    .weight(1f) // Take up remaining space
                    .padding(start = 4.dp)
            ) {
                // Placeholder and content
                if (text.isEmpty()) {
                    Text(
                        text = "New Task",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f) // Dimmed placeholder color
                    )
                }
                it() // Render the text field content
            }
        }
    }
}
