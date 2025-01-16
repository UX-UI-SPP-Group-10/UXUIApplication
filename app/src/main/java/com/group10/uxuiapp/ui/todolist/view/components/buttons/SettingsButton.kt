package com.group10.uxuiapp.ui.todolist.view.components.buttons

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
fun SettingsButton(context: Context, showLiked: MutableState<Boolean>) {
    val expanded = remember { mutableStateOf(false) }
    val rotationAngle by animateFloatAsState(
        targetValue = if (expanded.value) 90f else 0f,
        animationSpec = tween(durationMillis = 300), label = ""
    )
    val settingsColor = Color(0XFFE6ECE7)
    IconButton(onClick = {
        expanded.value = !expanded.value // Toggle expanded state
    }) {
        Icon(
            Icons.Filled.MoreVert,
            contentDescription = "MoreVert",
            modifier = Modifier
                .rotate(rotationAngle) // Rotate based on expanded state
                .animateContentSize() // smooth transition when rotating
        )
    }

    // Dropdown Menu for MoreVert
    DropdownMenu(
        expanded = expanded.value,
        onDismissRequest = { expanded.value = false },
        offset = DpOffset(x = (0).dp, y = 0.dp),
        containerColor = settingsColor
    ) {
        DropdownMenuItem(
            text = { Text(if (showLiked.value) "Show All" else "Show Liked")},
            onClick = {
                expanded.value = false
                showLiked.value = !showLiked.value
                //Toast.makeText(context, "Option 1 clicked", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.fillMaxWidth()
        )
        DropdownMenuItem(
            text = { Text("Option 2") },
            onClick = {
                expanded.value = false
                Toast.makeText(context, "Option 2 clicked", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.fillMaxWidth()
        )
        DropdownMenuItem(
            text = { Text("Option 3") },
            onClick = {
                expanded.value = false
                Toast.makeText(context, "Option 3 clicked", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}