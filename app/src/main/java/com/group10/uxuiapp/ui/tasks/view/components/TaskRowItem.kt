package com.group10.uxuiapp.ui.tasks.view.components

import android.util.Log
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.semantics.SemanticsProperties.Selected
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.internal.concurrent.Task

@Composable
fun TaskRowItem(
    task: TaskItem,
    viewModel: TaskViewModel,
    focusManager: FocusManager,
    focusRequester: FocusRequester
) {
    val taskItemWithSubTask by viewModel.lists.collectAsState()
    val selectedTask by viewModel.selectedTaskItem.collectAsState()
    var isChecked = task.isComplete
    var isFoldet = task.isFolded
    var textValue by remember { mutableStateOf(task.label) }

    val coroutineScope = rememberCoroutineScope()
    var debounceJob by remember { mutableStateOf<Job?>(null) }
    val animatedEndPadding by animateDpAsState(
        targetValue = if (selectedTask?.id == task.id) 45.dp else 0.dp,
        animationSpec = tween(durationMillis = 300),
        label = ""
    )

    // Check if this task has subtasks
    val hasSubTasks = taskItemWithSubTask.find { it.taskItem.id == task.id }?.subTasks?.isNotEmpty() == true

    val boxWhith = Modifier
        .fillMaxWidth()
        .padding(end = animatedEndPadding)

    Box(
        modifier = Modifier
            .height(50.dp)
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .padding(end = 1.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            Delete(onClick = { viewModel.deleteTask(task) })
        }
        Card(
            modifier = Modifier
                .then(boxWhith)
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
                },
            elevation = androidx.compose.material3.CardDefaults.cardElevation(
                defaultElevation = 4.dp
            ),
            colors = androidx.compose.material3.CardDefaults.cardColors(
                containerColor = Color(0xFFF8FCFF)
            )
        ) {
            Row(
                modifier = Modifier
                    .padding(vertical = 0.dp, horizontal = 12.dp)   // This is the inner padding
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 2) Checkbox
                Checkbox(
                    checked = isChecked,
                    onCheckedChange = { newChecked ->
                        isChecked = newChecked
                        viewModel.updateTaskItem(task, isComplete = newChecked)
                        val taskWithSubTasks =
                            taskItemWithSubTask.find { it.taskItem.id == task.id }

                        taskWithSubTasks?.subTasks?.forEach { subTask ->
                            viewModel.updateSubTask(subTask, isComplete = newChecked)
                        }
                    },
                    colors = CheckboxDefaults.colors(
                        checkedColor = Color(0xFF7d8597),
                        uncheckedColor = MaterialTheme.colorScheme.onSurface
                    ),
                    modifier = Modifier.size(28.dp)
                )

                BasicTextField(
                    value = textValue,
                    onValueChange = { newText ->
                        if (newText.length <= 20) {
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
                    modifier = Modifier
                        .width(225.dp)
                        .focusRequester(focusRequester)
                        .padding(start = 8.dp),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                        }
                    )
                )

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Row(
                        modifier = Modifier.width(56.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.End),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (hasSubTasks) {
                            TaskFolderButton(
                                onClick = {
                                    isFoldet = !isFoldet
                                    viewModel.updateTaskItem(task, isFolded = isFoldet)
                                },
                                isFoldet = isFoldet
                            )
                        }

                        AddSubTaskButton(onClick = {
                            val newSubTask = SubTask(label = "", taskItemId = task.id)
                            viewModel.addSupTask(newSubTask)
                            if(isFoldet) {
                                viewModel.updateTaskItem(task, isFolded = false)
                            }
                            coroutineScope.launch {
                                delay(100) // Delay for 100ms
                                focusRequester.requestFocus() // Request focus after the delay
                            }
                        })

                    }
                }
            }
        }
    }
}

