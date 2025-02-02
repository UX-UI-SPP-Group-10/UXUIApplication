package com.group10.uxuiapp.ui.todolist.view.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    todoList: TodoList,
    viewModel: TodoListViewModel,
    onDismiss: () -> Unit,
    onConfirm: (String, String, String, String, String, Boolean, Int?, String?) -> Unit
) {
    var currentPage by remember { mutableStateOf<EditListPage>(EditListPage.NameInput) }
    var listName by remember { mutableStateOf(todoList.title) }
    var selectedColor by remember { mutableStateOf(todoList.textColor ?: "") }
    var selectedDate by remember { mutableStateOf("") }
    var selectedTags by remember { mutableStateOf(todoList.tags ?: "") }
    var isRepeating by remember { mutableStateOf(todoList.isRepeating) }
    var selectedDay by remember { mutableStateOf<Int?>(todoList.repeatDay) }
    var gifUrl by remember { mutableStateOf((todoList.gifUrl)) }
    var selectedBackgroundColor by remember { mutableStateOf(todoList.backgroundColor ?: "") }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceEvenly
            ) {
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
                        if (!gifUrl.isNullOrEmpty()) {
                            TextButton(
                                onClick = {
                                    gifUrl = null
                                    viewModel.updateTodoList(
                                        id = todoList.id,
                                        resetGif = true // Explicit reset
                                    )
                                },
                                modifier = Modifier.padding(top = 16.dp, start = 0.dp)
                            ) {
                                Text(text = "Remove GIF")
                            }
                        }
                    }
                }
                is EditListPage.ColorPicker -> {
                    ColorPicker (
                        currentTextColor = selectedColor,
                        currentBackgroundColor = selectedBackgroundColor,
                        onColorSelect = { color ->
                            selectedColor = color
                            viewModel.updateTodoList(todoList.id, textColor = color)
                        },
                        onBackgroundColorSelect = {newBackgroundColor ->
                            // Update the ViewModel and set gifUrl to null
                            selectedBackgroundColor = newBackgroundColor
                            viewModel.updateTodoList(
                                id = todoList.id,
                                backgroundColor = newBackgroundColor,
                                resetGif = true
                            )
                            gifUrl = null
                        },
                        onResetGifUrl = {viewModel.updateTodoList(todoList.id, gifUrl = null, resetGif = true)
                        }
                    )
                    TextButton(
                        onClick = {
                            // Reset the background color to null (default)
                            selectedBackgroundColor = "" // Optional: Reset the local state if needed
                            viewModel.updateTodoList(
                                id = todoList.id,
                                resetBackgroundColor = true, // Explicit reset
                                resetGif = true
                            )
                            gifUrl = null
                        },
                        modifier = Modifier.padding(top = 320.dp, start = 4.dp)
                    ) {
                        Text("Reset Background Color to Default")
                    }
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
                                    onClick = {
                                        selectedDay = index + 1
                                        isRepeating = true},
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
                                    onClick = {
                                        selectedDay = index + 4
                                        isRepeating = true},
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
                                onClick = {
                                    selectedDay = 7
                                    isRepeating = true},
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
            OutlinedButton (
                onClick = {
                    val finalTags = if(selectedTags.isBlank()) "" else selectedTags
                    onConfirm(listName, selectedColor, selectedBackgroundColor, finalTags, selectedDate, isRepeating, selectedDay, gifUrl) // Pass the name entered to the onConfirm handler
                },
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            OutlinedButton (
                onClick = onDismiss,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary))
            {
                Text("Dismiss")
            }
        },
        containerColor = MaterialTheme.colorScheme.tertiary
    )
}
