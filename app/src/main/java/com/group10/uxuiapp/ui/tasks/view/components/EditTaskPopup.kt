package com.group10.uxuiapp.ui.tasks.view.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun EditTaskPopup(
    taskName: String,
    onSaveTask: (String) -> Unit,
    onDeleteTask: () -> Unit,
    onDismiss: () -> Unit
) {
    // State for the text field
    var taskText by remember { mutableStateOf(taskName) }

    // Popup content
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Edit Task",
                    style = MaterialTheme.typography.titleMedium.copy(fontSize = 20.sp, fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Editable text field
                OutlinedTextField(
                    value = taskText,
                    onValueChange = { taskText = it },
                    label = { Text("Task Name") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Buttons for actions
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // Save Button
                    Button(onClick = {
                        onSaveTask(taskText)
                        onDismiss()
                    }) {
                        Text("Save")
                    }

                    // Delete Button
                    Button(
                        onClick = {
                            onDeleteTask()
                            onDismiss()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                    ) {
                        Text("Delete")
                    }
                }

                // Cancel/Dismiss Button
                TextButton(onClick = { onDismiss() }) {
                    Text("Cancel")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EditTaskPopupPreview() {
    MaterialTheme {
        EditTaskPopup(
            taskName = "Sample Task",
            onSaveTask = { newName -> println("Task edited to: $newName") },
            onDeleteTask = { println("Task deleted") },
            onDismiss = { println("Popup dismissed") }
        )
    }
}
