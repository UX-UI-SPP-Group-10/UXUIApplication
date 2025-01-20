package com.group10.uxuiapp.ui.todolist.view.components

import GiphyDialog
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.group10.uxuiapp.data.data_class.TodoList
import com.group10.uxuiapp.ui.todolist.viewmodel.TodoListState
import com.group10.uxuiapp.ui.todolist.viewmodel.TodoListViewModel
import timber.log.Timber
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun PopupManager(
    popupState: TodoListState,
    // Provide any callbacks your dialogs need
    todoList: TodoList,
    viewModel: TodoListViewModel,
    onNewListConfirm: (String) -> Unit,
    onRenameConfirm: (TodoList, String, String, Long?, String?) -> Unit,
    onGifSelected: (TodoList, String) -> Unit,
    onColorSelected: (TodoList, String) -> Unit,
    onColorBackgroundSelected: (TodoList, String) -> Unit,
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
                todoList = todoList,
                onDismiss = onDismiss,
                onConfirm = { name ->
                    onNewListConfirm(name)
                },
                onRemoveGif = {
                    todoList?.let {
                        viewModel.updateTodoList(id = todoList.id, gifUrl = null)
                    }
                }
            )
        }

        is TodoListState.Rename -> {
            EditTodolistDialog(
                todoList = todoList, // Pass the current TodoList
                viewModel = viewModel, // Pass the ViewModel
                onDismiss = onDismiss,
                onConfirm = { newName, selectedColor, selectedBackgroundColor, selectedTags,selectedDate, isRepeating, selectedDay, gifUrl ->
                    val currentTodoList = popupState.todoList

                    val finalName = if (newName.isBlank()) currentTodoList.title else newName
                    val finalColor = if (selectedColor.isBlank()) currentTodoList.textColor else selectedColor
                    val finalBackgroundColor = if (selectedBackgroundColor.isBlank()) currentTodoList.backgroundColor else selectedBackgroundColor
                    val finalTags = if (selectedTags.isBlank()) "" else selectedTags
                    val finalGifUrl = if (gifUrl.isNullOrEmpty()) null else gifUrl
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
                        id = currentTodoList.id,
                        title = finalName,
                        textColor = finalColor,
                        backgroundColor = finalBackgroundColor,
                        tags = finalTags,
                        dueDate = finalDate,
                        isRepeating = isRepeating,
                        repeatDay = selectedDay,
                        gifUrl = finalGifUrl
                    )
                    onDismiss()
                }
            )
        }

        is TodoListState.SelectGif -> {
            // Show Giphy dialog
            GiphyDialog(
                context = LocalContext.current,
                onGifSelected = { gifUrl ->
                    Timber.tag("GiphyDialog").d("GIF selected: $gifUrl")
                    onGifSelected(popupState.todoList, gifUrl)
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
