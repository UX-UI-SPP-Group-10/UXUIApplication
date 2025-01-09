package com.group10.uxuiapp.ui.todolist.view.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.group10.uxuiapp.data.data_class.TodoList

@Composable
fun TagEditDialog(todoList: TodoList, onTagsUpdated: (String) -> Unit, onDismiss: () -> Unit) {
    var text by remember { mutableStateOf(todoList.tags ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Tags") },
        text = {
            TextField(
                value = text,
                onValueChange = { text = it },
                label = { Text("Enter tags, separated by commas") }
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    onTagsUpdated(text)
                    onDismiss()
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
