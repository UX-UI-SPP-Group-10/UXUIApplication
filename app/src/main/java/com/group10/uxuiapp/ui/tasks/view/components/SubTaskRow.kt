package com.group10.uxuiapp.ui.tasks.view.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
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
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.input.ImeAction
import com.group10.uxuiapp.data.data_class.SubTask
import com.group10.uxuiapp.ui.tasks.view.components.buttons.Delete
import com.group10.uxuiapp.ui.tasks.viewmodel.TaskViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun SubTaskRow(
    task: SubTask,
    viewModel: TaskViewModel,
    focusManager: FocusManager,
    focusRequester: FocusRequester
) {
    var isChecked = task.isComplete
    val selectedTask by viewModel.selectedSubTask.collectAsState()
    var textValue by remember { mutableStateOf(task.label) }
    val coroutineScope = rememberCoroutineScope()
    var debounceJob by remember { mutableStateOf<Job?>(null) }
    val animatedEndPadding by animateDpAsState(
        targetValue = if (selectedTask?.id == task.id) 45.dp else 0.dp,
        animationSpec = tween(durationMillis = 200), label = ""
    )

    LaunchedEffect(task.label) {
        textValue = task.label
    }

    val boxWhith = Modifier
        .fillMaxWidth()
        .padding(end = animatedEndPadding)

    Box(
        modifier = Modifier
            .padding(top = 7.dp, start = 40.dp)
            .fillMaxWidth()
            .height(50.dp)
    )
    {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .padding(start = 1.dp, end = 1.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            Delete(onClick = {
                viewModel.deleteSupTask(selectedTask!!)
                viewModel.selectTaskForChange(null, null)
            }
            )
        }
        Card(
            modifier = Modifier
                .then(boxWhith)
                .pointerInput(Unit) {
                    detectHorizontalDragGestures { change, dragAmount ->
                        change.consume()

                        if (dragAmount < -30) {
                            viewModel.selectTaskForChange(subTask = task)
                        } else if (dragAmount > 30) {
                            viewModel.selectTaskForChange(null, null)
                        }
                    }
                },
            colors = androidx.compose.material3.CardDefaults.cardColors(
                containerColor = Color(0xFFDBEBFF)
            ),
            elevation = androidx.compose.material3.CardDefaults.cardElevation(
                defaultElevation = 4.dp
            )
        ) {
            Row(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth()
                    .alpha(if (task.isComplete) 0.6f else 1f),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 2) Checkbox
                Checkbox(
                    checked = isChecked,
                    onCheckedChange = { newChecked ->
                        isChecked = newChecked
                        viewModel.updateSubTask(task, isComplete = newChecked)
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
                                viewModel.updateSubTask(
                                    task,
                                    label = newText
                                ) // Update ViewModel
                            }
                        }
                    },
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodyLarge.copy(
                        textDecoration = if (isChecked) TextDecoration.LineThrough else null,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
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
            }
        }
    }
}

