package com.group10.uxuiapp.ui.tasks.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
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
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.input.pointer.pointerInput
import com.group10.uxuiapp.ui.tasks.viewmodel.TaskViewModel


@Composable
fun TaskRowItem(
    task: TaskItem,
    viewModel: TaskViewModel
) {
    val taskItemWithSubTask by viewModel.lists.collectAsState()
    var isChecked = task.isComplete
    var isFoldet = task.isFolded

    Box(
        modifier = Modifier
            .padding(vertical = 6.dp, horizontal = 12.dp)
            .height(40.dp)
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = MaterialTheme.shapes.small
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .pointerInput(Unit) {
                    // detectTapGestures only for the "empty space"
                    detectTapGestures(onPress = { viewModel.selectTask(task) })
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(16.dp))
            // 2) Checkbox
            Checkbox(
                checked = isChecked,
                onCheckedChange = { newChecked ->
                    isChecked = newChecked
                    viewModel.updateTaskItem(task, isComplete = newChecked)
                    val taskWithSubTasks = taskItemWithSubTask.find { it.taskItem.id == task.id }

                    if (taskWithSubTasks != null && !task.isFolded) {
                        taskWithSubTasks.subTasks.forEach { subTask ->
                            viewModel.updateSubTask(subTask, isComplete = newChecked)
                        }
                    }
                },
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.primary,
                    uncheckedColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    checkmarkColor = MaterialTheme.colorScheme.onPrimary
                ),
                modifier = Modifier.size(28.dp)
            )

            // 3) Editable text
            BasicTextField(
                value = task.label,
                onValueChange = { newText ->
                    viewModel.updateTaskItem(task, label = newText)
                },
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    textDecoration = if (isChecked) TextDecoration.LineThrough else null,
                    fontWeight = FontWeight.Medium,
                    color = if (isChecked) {
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    }
                ),
                singleLine = true,
                modifier = Modifier
                    .widthIn(max = 200.dp)
                    .padding(start = 4.dp)
            ) {
                // Placeholder if you want one
                if (task.label.isEmpty()) {
                    Text(
                        text = "new Task",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                    )
                }
                it()
            }

            TaskFolderButton(onClick = {isFoldet = !isFoldet
                viewModel.updateTaskItem(task, isFolded = isFoldet)}, isFoldet)

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

