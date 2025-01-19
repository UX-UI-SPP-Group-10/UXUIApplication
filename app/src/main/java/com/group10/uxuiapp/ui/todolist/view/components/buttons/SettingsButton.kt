package com.group10.uxuiapp.ui.todolist.view.components.buttons

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp

@Composable
fun SettingsButton(
    context: Context,
    showLiked: MutableState<Boolean>,
    onDeleteAllConfirmed: () -> Unit,
    color : Color
) {
    val expanded = remember { mutableStateOf(false) }
    val showDialog = remember { mutableStateOf(false) }
    val rotationAngle by animateFloatAsState(
        targetValue = if (expanded.value) 90f else 0f,
        animationSpec = tween(durationMillis = 300), label = ""
    )
    val settingsColor = MaterialTheme.colorScheme.background
    IconButton(onClick = {
        expanded.value = !expanded.value // Toggle expanded state
    }) {
        Icon(
            Icons.Filled.MoreVert,
            contentDescription = "MoreVert",
            modifier = Modifier
                .rotate(rotationAngle) // Rotate based on expanded state
                .animateContentSize(), // smooth transition when rotating
            tint = color
        )
    }

    // Dropdown Menu for MoreVert
    DropdownMenu(
        expanded = expanded.value,
        onDismissRequest = { expanded.value = false },
        offset = DpOffset(x = (.5).dp, y = 8.5.dp),
        containerColor = settingsColor,
        shape = RoundedCornerShape(12.dp)
    ) {
        DropdownMenuItem(
            text = { Text(if (showLiked.value) "Show All" else "Show Liked")},
            onClick = {
                expanded.value = false
                showLiked.value = !showLiked.value
            },
            modifier = Modifier.fillMaxWidth()
        )
        DropdownMenuItem(
            text = { Text("Delete All") },
            onClick = {
                expanded.value = false
                showDialog.value = true
            },
            modifier = Modifier.fillMaxWidth()
        )
    }

    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = { Text("Delete All Todo Lists") },
            text = { Text("Are you sure you want to delete all todo lists? \nThis action cannot be undone.") },
            confirmButton = {
                Button (onClick = {
                    showDialog.value = false
                    onDeleteAllConfirmed() // Perform deletion
                    Toast.makeText(context, "All todo lists deleted", Toast.LENGTH_SHORT).show()
                },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFCE2D22))
                ){
                    Text(
                        text = "Delete",
                        color = MaterialTheme.colorScheme.onTertiary
                    )
                }
            },
            dismissButton = {
                Button(onClick = { showDialog.value = false },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF268036))
                    ) {
                    Text(
                        text = "Cancel",
                        color = MaterialTheme.colorScheme.onTertiary
                    )
                }
            }
        )
    }
}