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
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import com.group10.uxuiapp.data.data_class.SubTask
import com.group10.uxuiapp.ui.tasks.viewmodel.TaskViewModel


@Composable
fun SubTaskRow(
    task: SubTask,
    viewModel: TaskViewModel
) {
    var isChecked = task.isComplete

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ){
        Box(
            modifier = Modifier
                .padding(vertical = 6.dp, horizontal = 12.dp)
                .height(40.dp)
                .width(340.dp)
                .background(
                    color = MaterialTheme.colorScheme.onTertiary,
                    shape = MaterialTheme.shapes.small
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .pointerInput(Unit) {
                        // detectTapGestures only for the "empty space"
                        detectTapGestures(onLongPress = {viewModel.selectTaskForChange(subTask = task)})
                    },
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.width(16.dp))
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
                        checkmarkColor = MaterialTheme.colorScheme.secondary
                    ),
                    modifier = Modifier.size(28.dp)
                )

                // 3) Editable text
                BasicTextField(
                    value = task.label,
                    onValueChange = { newText ->
                        viewModel.updateSubTask(task, label = newText)
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
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

