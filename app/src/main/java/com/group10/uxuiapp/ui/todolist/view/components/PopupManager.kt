package com.group10.uxuiapp.ui.todolist.view.components

import GiphyDialog
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.platform.LocalContext
import com.group10.uxuiapp.data.data_class.TodoList
import com.group10.uxuiapp.ui.todolist.viewmodel.TodoListState
import com.group10.uxuiapp.ui.todolist.viewmodel.TodoListViewModel
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun PopupManager(
    popupState: TodoListState,
    // Provide any callbacks your dialogs need
    viewModel: TodoListViewModel,
    onNewListConfirm: (String) -> Unit,
    onRenameConfirm: (TodoList, String, String, Long?) -> Unit,
    onGifSelected: (TodoList, String) -> Unit,
    onColorSelected: (TodoList, String) -> Unit,
    onTagsEdited: (TodoList, String) -> Unit,
    onDismiss: () -> Unit
) {
    when (popupState) {
        is TodoListState.None -> {
            // No dialog to show
        }

        is TodoListState.NewList -> {
            // Show dialog for new list
            ListNameInputDialog(
                onDismiss = onDismiss,
                onConfirm = { name ->
                    onNewListConfirm(name)
                }
            )
        }

        is TodoListState.Rename -> {
            EditTodolistDialog(
                onDismiss = onDismiss,
                onConfirm = { newName, selectedColor, selectedDate, isRepeating, selectedDay ->
                    val currentTodoList = popupState.todoList

                    val finalName = if (newName.isBlank()) currentTodoList.title else newName
                    val finalColor = if (selectedColor.isBlank()) currentTodoList.textColor else selectedColor
                    val finalDate = if (selectedDate.isBlank()) currentTodoList.dueDate else {
                        try {
                            val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                            val date = formatter.parse(selectedDate) // Parse the selected date
                            date?.time // Convert date to Long (timestamp)
                        } catch (e: ParseException) {
                            Log.e("EditTodolistDialog", "Error parsing date: ${e.message}")
                            currentTodoList.dueDate // Fallback to current due date
                        }
                    }

                    // Update the TodoList using the ViewModel
                    viewModel.updateTodoList(
                        todoList = currentTodoList,
                        title = finalName,
                        textColor = finalColor,
                        dueDate = finalDate,
                        isRepeating = isRepeating,
                        repeatDay = selectedDay
                    )
                    onDismiss()
                }
            )
            // Show dialog for rename
//            ListNameInputDialog(
//                onDismiss = onDismiss,
//                onConfirm = { newName ->
//                    onRenameConfirm(popupState.todoList, newName)
//                }
//            )

        }

        is TodoListState.SelectGif -> {
            // Show Giphy dialog
            GiphyDialog(
                context = LocalContext.current,
                onGifSelected = { gifUrl ->
                    viewModel.updateGifUrl(
                        popupState.todoList.id,
                        gifUrl = gifUrl)
                },
                onDismissed = {
                    onDismiss()
                }
            )
        }

        is TodoListState.TagsEdit -> {
            // Show Tag Dialog
            TagEditDialog(
                todoList = popupState.todoList,
                onTagsUpdated = { tags -> onTagsEdited(popupState.todoList, tags) },
                onDismiss = onDismiss
            )
        }
//        is TodoListState.ColorPick -> {
//            // Show color picker dialog
//            ColorPickerDialog(
//                onDismissed = onDismiss,
//                onColorSelected = { color ->
//                    onColorSelected(popupState.todoList, color)
//                }
//            )
//        }
    }
}
