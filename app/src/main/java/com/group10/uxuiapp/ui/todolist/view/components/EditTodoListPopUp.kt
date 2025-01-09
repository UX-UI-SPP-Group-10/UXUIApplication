package com.group10.uxuiapp.ui.todolist.view.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.group10.uxuiapp.R
import com.group10.uxuiapp.ui.todolist.view.components.ColorPicker

sealed class EditListPage {
    object NameInput : EditListPage()
    object ColorPicker : EditListPage()
    object DueDatePicker : EditListPage()
}


@Composable
fun EditTodolistDialog(onDismiss: () -> Unit, onConfirm: (String, String, String) -> Unit) {
    var currentPage by remember { mutableStateOf<EditListPage>(EditListPage.NameInput) }
    var listName by remember { mutableStateOf("") }
    var selectedColor by remember { mutableStateOf("#FFFFFF") }
    var selectedDate by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = {
            Row(
               modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceEvenly
            ){
                IconButton(onClick = { currentPage = EditListPage.NameInput }) {
                    Icon(
                        painter = painterResource(id = R.drawable.edit),
                        contentDescription = "Edit",
                        tint = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                }
                IconButton(onClick = { currentPage = EditListPage.ColorPicker }) {
                    Icon(
                        painter = painterResource(id = R.drawable.palette),
                        contentDescription = "Color",
                        tint = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                }
                IconButton(onClick = { currentPage = EditListPage.DueDatePicker }) {
                    Icon(
                        painter = painterResource(id = R.drawable.calendar),
                        contentDescription = "Date",
                        tint = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                }
            }
        },

        text = {
            when (currentPage) {
                is EditListPage.NameInput -> {
                    Column {
                        OutlinedTextField(
                            value = listName,
                            onValueChange = { listName = it },
                            label = { Text("List Name") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
                is EditListPage.ColorPicker -> {
                    ColorPicker { selectedColor = it }
                }
                is EditListPage.DueDatePicker -> {
                    // Due Date Picker Content
                }
            }
       },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm(listName, selectedColor, selectedDate) // Pass the name entered to the onConfirm handler
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
