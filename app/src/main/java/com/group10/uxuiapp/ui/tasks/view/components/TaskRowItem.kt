package com.group10.uxuiapp.ui.tasks.view.components

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.group10.uxuiapp.data.data_class.TaskItem
import com.group10.uxuiapp.ui.todolist.viewmodel.TodoListViewModel
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.remember
import androidx.compose.ui.input.pointer.pointerInput


@Composable
fun TaskRowItem(
    task: TaskItem,
    viewModel: TaskListViewModel
) {
    var isChecked by remember { mutableStateOf(task.isComplete) }
    var text by remember { mutableStateOf(task.label) }
    var showPopup by remember { mutableStateOf(false) } // Track popup visibility

    // Main container
    Box(
        modifier = Modifier
            .padding(vertical = 6.dp, horizontal = 12.dp)
            .height(40.dp)
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = MaterialTheme.shapes.small // Subtle corner rounding
            )
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = { showPopup = true } // Show popup on long press
                )
            }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
        ) {
            // Checkbox
            Checkbox(
                checked = isChecked,
                onCheckedChange = { newChecked ->
                    isChecked = newChecked
                    viewModel.updateTaskItem(taskItem = task, isComplete = newChecked)
                },
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.primary,
                    uncheckedColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    checkmarkColor = MaterialTheme.colorScheme.onPrimary
                ),
                modifier = Modifier.size(28.dp)
            )

            // Editable text
            BasicTextField(
                value = text,
                onValueChange = { newText ->
                    text = newText
                    viewModel.updateTaskItem(taskItem = task, label = newText)
                },
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    textDecoration = if (isChecked) TextDecoration.LineThrough else null,
                    fontWeight = FontWeight.Medium,
                    color = if (isChecked) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f) else MaterialTheme.colorScheme.onSurface
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default,
                keyboardActions = KeyboardActions.Default,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 4.dp)
            ) {
                if (text.isEmpty()) {
                    Text(
                        text = "",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                    )
                }
                it()
            }
        }
    }

    // Show popup if `showPopup` is true
    if (showPopup) {
        EditTaskPopup(
            taskName = text,
            onEditTask = { newName ->
                text = newName
                viewModel.updateTaskItem(taskItem = task, label = newName)
            },
            onDeleteTask = {
                // viewModel.deleteTaskItem(task)
            },
            onDismiss = {
                showPopup = false
            }
        )
    }
}
