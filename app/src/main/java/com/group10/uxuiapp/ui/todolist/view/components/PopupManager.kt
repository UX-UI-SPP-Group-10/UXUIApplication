package com.group10.uxuiapp.ui.todolist.view.components

import GiphyDialog
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.platform.LocalContext
import com.group10.uxuiapp.data.data_class.TodoList
import com.group10.uxuiapp.ui.todolist.viewmodel.TodoListState

@Composable
fun PopupManager(
    popupState: TodoListState,
    // Provide any callbacks your dialogs need
    onNewListConfirm: (String) -> Unit,
    onRenameConfirm: (TodoList, String, String, String) -> Unit,
    onGifSelected: (TodoList, String) -> Unit,
    onColorSelected: (TodoList, String) -> Unit,
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
                onConfirm = { newName, selectedColor, selectedDate ->
                    onRenameConfirm(
                        popupState.todoList,
                        newName,
                        selectedColor,
                        selectedDate
                    )
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
                    onGifSelected(popupState.todoList, gifUrl)
                },
                onDismissed = {
                    onDismiss()
                }
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
