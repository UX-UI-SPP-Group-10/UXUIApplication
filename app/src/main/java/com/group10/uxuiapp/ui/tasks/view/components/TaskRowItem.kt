package com.group10.uxuiapp.ui.tasks.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import com.group10.uxuiapp.data.data_class.SubTask
import com.group10.uxuiapp.ui.tasks.view.components.buttons.AddTaskButton
import com.group10.uxuiapp.ui.tasks.view.components.buttons.Delete
import com.group10.uxuiapp.ui.tasks.viewmodel.TaskViewModel
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun TaskRowItem(
    task: TaskItem,
    viewModel: TaskViewModel
) {
    val taskItemWithSubTask by viewModel.lists.collectAsState()
    val selectedTask by viewModel.selectedTaskItem.collectAsState()
    var isChecked = task.isComplete
    var isFoldet = task.isFolded
    var textValue by remember { mutableStateOf(task.label) }

    val coroutineScope = rememberCoroutineScope()
    var debounceJob by remember { mutableStateOf<Job?>(null) }
    val boxWhith =
        if (selectedTask == task) {
            Modifier.width(340.dp)
        } else {
            Modifier.fillMaxWidth()
        }

    Box(
        modifier = Modifier
            .padding(vertical = 6.dp, horizontal = 12.dp)
            .height(40.dp)
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier.fillMaxHeight().fillMaxWidth(),
            contentAlignment = Alignment.CenterEnd
        ) {
            Delete(onClick = { viewModel.deleteTask(task) })
        }
        Box(
            modifier = Modifier
                .then(boxWhith)
                .background(
                    color = MaterialTheme.colorScheme.secondary,
                    shape = MaterialTheme.shapes.small
                )
                .pointerInput(Unit) {
                    detectHorizontalDragGestures { change, dragAmount ->
                        change.consume()

                        if (dragAmount < -30) {
                            viewModel.selectTaskForChange(task)
                        }
                        else if (dragAmount > 30) {
                            viewModel.selectTaskForChange(null, null)
                        }
                    }
                }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.width(16.dp))
                // 2) Checkbox
                Checkbox(
                    checked = isChecked,
                    onCheckedChange = { newChecked ->
                        isChecked = newChecked
                        viewModel.updateTaskItem(task, isComplete = newChecked)
                        val taskWithSubTasks =
                            taskItemWithSubTask.find { it.taskItem.id == task.id }

                        if (taskWithSubTasks != null && !task.isFolded) {
                            taskWithSubTasks.subTasks.forEach { subTask ->
                                viewModel.updateSubTask(subTask, isComplete = newChecked)
                            }
                        }
                    },
                    colors = CheckboxDefaults.colors(
                        checkedColor = Color(0XFF20792F),
                        uncheckedColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        checkmarkColor = MaterialTheme.colorScheme.secondary
                    ),
                    modifier = Modifier.size(28.dp)
                )

                // 3) Editable text
                TextField(
                    value = textValue,
                    onValueChange = { newText ->
                        if (newText.length <= 25) {
                            textValue = newText

                            debounceJob?.cancel() // Cancel the ongoing debounce job
                            debounceJob = coroutineScope.launch {
                                delay(200) // 200ms debounce delay
                                viewModel.updateTaskItem(task, label = newText) // Update ViewModel
                            }
                        }
                    },
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodyLarge.copy(
                        textDecoration = if (isChecked) TextDecoration.LineThrough else null,
                        fontWeight = FontWeight.Medium,
                        color = if (isChecked) {
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        } else {
                            MaterialTheme.colorScheme.onSurface
                        }
                    ),
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.Transparent, // No background
                        focusedContainerColor = Color.Transparent,  // No background on focus
                        unfocusedIndicatorColor = Color.Transparent, // No underline
                        focusedIndicatorColor = Color.Transparent // No underline
                    )
                )


                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Row {
                        AddSubTaskButton(onClick = {
                            val newSubTask = SubTask(label = "", taskItemId = task.id)
                            viewModel.addSupTask(newSubTask)
                        })

                        Spacer(modifier = Modifier.width(6.dp))

                        TaskFolderButton(
                            onClick = {
                                isFoldet = !isFoldet
                                viewModel.updateTaskItem(task, isFolded = isFoldet)
                            },
                            isFoldet = isFoldet
                        )
                    }
                }
            }
        }
    }
}

