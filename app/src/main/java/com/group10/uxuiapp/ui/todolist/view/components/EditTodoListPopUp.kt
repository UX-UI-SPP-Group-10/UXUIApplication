package com.group10.uxuiapp.ui.todolist.view.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.group10.uxuiapp.R
import com.group10.uxuiapp.data.data_class.TodoList
import com.group10.uxuiapp.ui.todolist.view.components.ColorPicker
import com.group10.uxuiapp.ui.todolist.viewmodel.TodoListViewModel
import java.text.SimpleDateFormat
import java.util.Locale

sealed class EditListPage {
    object NameInput : EditListPage()
    object ColorPicker : EditListPage()
    object TagPicker : EditListPage()
    object DueDatePicker : EditListPage()
    object RepeatPicker : EditListPage()
}


@Composable
fun EditTodolistDialog(
    todoList: TodoList, // Pass the current TodoList
    viewModel: TodoListViewModel, // Pass the ViewModel

    onDismiss: () -> Unit,
    onConfirm: (String, String, String , String, Boolean, Int?) -> Unit) {
    var currentPage by remember { mutableStateOf<EditListPage>(EditListPage.NameInput) }
    var listName by remember { mutableStateOf("") }
    var selectedColor by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf("") }
    var selectedTags by remember { mutableStateOf(todoList.tags ?: "") }
    var isRepeating by remember { mutableStateOf(false) }
    var selectedDay by remember { mutableStateOf<Int?>(null) }

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
                IconButton(onClick = { currentPage = EditListPage.TagPicker }) {
                    Icon(
                        painter = painterResource(id = R.drawable.tag),
                        contentDescription = "Tag",
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
                IconButton(onClick = { currentPage = EditListPage.RepeatPicker }) {
                    Icon(
                        painter = painterResource(id = R.drawable.repeat),
                        contentDescription = "Repeat",
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
                is EditListPage.TagPicker -> {
                    Column {
                        Text("Edit Tags", style = MaterialTheme.typography.bodyLarge)
                        OutlinedTextField(
                            value = selectedTags,
                            onValueChange = { selectedTags = it },
                            label = { Text("Enter tags, separated by commas") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                is EditListPage.DueDatePicker -> {
                    val context = LocalContext.current
                    DatePickerComponent(
                        context = context,
                        todoList = todoList, // Pass the current TodoList
                        viewModel = viewModel,
                        onDateSelected = { date ->
                            val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                            val parsedDate = formatter.parse(date)
                            selectedDate = date
                            currentPage = EditListPage.NameInput // Navigate back to NameInput after selecting the date
                            currentPage = EditListPage.NameInput
                        }
                    )
                }
                is EditListPage.RepeatPicker -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp) // Space between rows
                    ) {
                        val days = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

                        // First Row: Mon, Tue, Wed
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceEvenly
                        ) {
                            days.subList(0, 3).forEachIndexed { index, day ->
                                TextButton(
                                    onClick = { selectedDay = index + 1 },
                                    modifier = Modifier.padding(horizontal = 8.dp),
                                    colors = androidx.compose.material3.ButtonDefaults.textButtonColors(
                                        containerColor = if (selectedDay == index + 1) MaterialTheme.colorScheme.primary
                                        else MaterialTheme.colorScheme.surface
                                    )
                                ) {
                                    Text(
                                        text = day,
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = if (selectedDay == index + 1) MaterialTheme.colorScheme.onPrimary
                                        else MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }

                        // Second Row: Thu, Fri, Sat
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceEvenly
                        ) {
                            days.subList(3, 6).forEachIndexed { index, day ->
                                TextButton(
                                    onClick = { selectedDay = index + 4 },
                                    modifier = Modifier.padding(horizontal = 8.dp),
                                    colors = androidx.compose.material3.ButtonDefaults.textButtonColors(
                                        containerColor = if (selectedDay == index + 4) MaterialTheme.colorScheme.primary
                                        else MaterialTheme.colorScheme.surface
                                    )
                                ) {
                                    Text(
                                        text = day,
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = if (selectedDay == index + 4) MaterialTheme.colorScheme.onPrimary
                                        else MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }

                        // Third Row: Sun (aligned to the left)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.Start // Align Sun to the left
                        ) {
                            TextButton(
                                onClick = { selectedDay = 7 },
                                modifier = Modifier.padding(start = 21.dp), // Add padding to create space from the edge
                                colors = androidx.compose.material3.ButtonDefaults.textButtonColors(
                                    containerColor = if (selectedDay == 7) MaterialTheme.colorScheme.primary
                                    else MaterialTheme.colorScheme.surface
                                )
                            ) {
                                Text(
                                    text = "Sun",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = if (selectedDay == 7) MaterialTheme.colorScheme.onPrimary
                                    else MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }
       },
        confirmButton = {
            TextButton(
                onClick = {
                    val finalTags = if(selectedTags.isBlank()) "" else selectedTags
                    onConfirm(listName, selectedColor, finalTags,selectedDate, isRepeating, selectedDay) // Pass the name entered to the onConfirm handler
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
