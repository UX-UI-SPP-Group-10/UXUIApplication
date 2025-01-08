package com.group10.uxuiapp.ui.todolist.view.components

import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp

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
    val expanded = remember { mutableStateOf(false) }

    Button(onClick = {
        expanded.value = !expanded.value // Toggle expanded state
    }) {
        Text("Choose a color")
    }
    DropdownMenu(
        expanded = expanded.value,
        onDismissRequest = { expanded.value = false },
        offset = DpOffset(x = (0).dp, y = 0.dp)
    ) {
        DropdownMenuItem(
            text = { Text("Red", color = Color.Red) },
            onClick = {
                onColorSelect("#FF5733")
                expanded.value = false },
            modifier = Modifier.fillMaxWidth()
        )
        DropdownMenuItem(
            text = { Text("Blue", color = Color.Blue) },
            onClick = {
                onColorSelect("#0000FF")
                expanded.value = false },
            modifier = Modifier.fillMaxWidth()
        )
        DropdownMenuItem(
            text = { Text("Green", color = Color.Green) },
            onClick = {
                onColorSelect("#00ff00")
                expanded.value = false },
            modifier = Modifier.fillMaxWidth()
        )
    }
}
