package com.group10.uxuiapp.ui.todolist.view.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Surface
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.group10.uxuiapp.R

@Composable
fun OptionsPopup(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    onDelete: () -> Unit,
    onUpdate: () -> Unit,
    onGifSelect: () -> Unit,
    onTagsEdit: () -> Unit,
    //onColorChange: () -> Unit,
    offset: IntOffset
) {
    val showDialog = remember { mutableStateOf(false) }
    val boxHeight = 50.dp
    val boxWidth = 180.dp

    if (expanded) {
        // Wrap your card in a Popup
        Popup(
            onDismissRequest = onDismissRequest,
            properties = PopupProperties(
                focusable = true  // So it dismisses when user taps outside
            ),
            alignment = Alignment.TopCenter,
            offset = offset,
        ) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                tonalElevation = 8.dp, // Elevation for depth
                shadowElevation = 8.dp, // Shadow for a floaty effect
                color = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier
                    .height(boxHeight)
                    .width(boxWidth),
            ) {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 8.dp, vertical = 8.dp)
                ) {
                    // 1) Select GIF
                    IconButton(onClick = onGifSelect) {
                        Icon(
                            painter = painterResource(id = R.drawable.gif),
                            contentDescription = "GIF",
                            tint = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    }
                    Spacer(modifier = Modifier.width(9.dp))

                    // 2) chan visuel
                    IconButton(onClick = onUpdate) {
                        Icon(
                            painter = painterResource(id = R.drawable.edit),
                            contentDescription = "Rename",
                            tint = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    }
                    Spacer(modifier = Modifier.width(9.dp))

                    // 4) Delete
                    IconButton(onClick =  {showDialog.value = true}) {
                        Icon(
                            painter = painterResource(id = R.drawable.delete),
                            contentDescription = "Delete",
                            tint = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    }
                }
            }
        }
    }

    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = { Text("Delete Todo List") },
            text = { Text("Are you sure you want to delete this todo list? \nThis action cannot be undone.") },
            confirmButton = {
                Button (onClick = {
                    showDialog.value = false
                    onDelete() // Perform deletion
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
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB4B4B4))
                ) {
                    Text(
                        text = "Cancel",
                        color = Color.Black //MaterialTheme.colorScheme.onTertiary
                    )
                }
            },
            containerColor = MaterialTheme.colorScheme.tertiary
        )
    }
}
