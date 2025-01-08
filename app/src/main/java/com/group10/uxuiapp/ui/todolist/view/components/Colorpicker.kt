package com.group10.uxuiapp.ui.todolist.view.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.state.ToggleableState

@Composable
fun ColorPickerDialog(isDialogOpen: MutableState<Boolean>, onColorSelected: (String) -> Unit) {
    if (isDialogOpen.value) {
        AlertDialog(
            onDismissRequest = { isDialogOpen.value = false },
            title = { Text("Pick a Color") },
            text = {
                ColorPicker(onColorSelect = { hexColor ->
                    onColorSelected(hexColor)
                    isDialogOpen.value = false
                })
            },
            confirmButton = {
                Button(onClick = { isDialogOpen.value = false }) {
                    Text("Close")
                }
            }
        )
    }
}

@Composable
fun ColorPicker(onColorSelect: (String) -> Unit) {
    // Dummy function for demonstration
    // In real application, integrate actual color picker logic
    Button(onClick = { onColorSelect("#FF5733") }) { // Example color
        Text("Choose Red")
    }
}
