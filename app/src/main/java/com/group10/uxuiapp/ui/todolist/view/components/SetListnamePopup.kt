package com.group10.uxuiapp.ui.todolist.view.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.group10.uxuiapp.data.data_class.TodoList

@Composable
fun ListNameInputDialog(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    val listName = remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Enter List Name") },
        text = {
            Column {
                OutlinedTextField(
                    value = listName.value,
                    onValueChange = { listName.value = it },
                    label = { Text("List Name") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm(listName.value) // Pass the name entered to the onConfirm handler
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Dismiss")
            }
        }
    )
}
