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
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import com.group10.uxuiapp.data.data_class.SubTask
import com.group10.uxuiapp.ui.tasks.view.components.buttons.Delete
import com.group10.uxuiapp.ui.tasks.viewmodel.TaskViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun SubTaskRow(
    task: SubTask,
    viewModel: TaskViewModel
) {
    var isChecked = task.isComplete
    val selectedTask by viewModel.selectedSubTask.collectAsState()
    var textValue by remember { mutableStateOf(task.label) }
    val coroutineScope = rememberCoroutineScope()
    var debounceJob by remember { mutableStateOf<Job?>(null) }
    val animatedEndPadding by animateDpAsState(
        targetValue = if (selectedTask == task) 45.dp else 0.dp,
        animationSpec = tween(durationMillis = 200), label = ""
    )

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
            modifier = Modifier.fillMaxHeight().fillMaxWidth(),
            contentAlignment = Alignment.CenterEnd
        ) {
            Delete(onClick = { viewModel.deleteSupTask(selectedTask!!) })
        }
        Box(
            modifier = Modifier
                .then(boxWhith)
                .background(
                    color = MaterialTheme.colorScheme.onTertiary,
                    shape = MaterialTheme.shapes.small
                )
                .pointerInput(Unit) {
                    detectHorizontalDragGestures { change, dragAmount ->
                        change.consume()

                        if (dragAmount < -30) {
                            viewModel.selectTaskForChange(subTask = task)
                        } else if (dragAmount > 30) {
                            viewModel.selectTaskForChange(null, null)
                        }
                    }
                }
        ) {
            Row(
                modifier = Modifier
                    .padding(vertical = 0.dp, horizontal = 12.dp)
                    .fillMaxWidth(),
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
                        checkedColor = Color(0XFF20792F),
                        uncheckedColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        checkmarkColor = MaterialTheme.colorScheme.onTertiary
                    ),
                    modifier = Modifier.size(28.dp)
                )

                // 3) Editable text
                TextField(
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
                    ),
                    modifier = Modifier.width(225.dp)
                )
            }
        }
    }
}

