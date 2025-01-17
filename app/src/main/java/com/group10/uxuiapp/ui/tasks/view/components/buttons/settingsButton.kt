package com.group10.uxuiapp.ui.tasks.view.components.buttons

import android.content.Context
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp

@Composable
fun SettingsButton(
    context: Context,
    //sortByComplete: MutableState<Boolean>,
    deleteCompletedClick: () -> Unit,
    sortCompleted: () -> Unit, sortByCompleted: Boolean
) {
    val expanded = remember { mutableStateOf(false) }
    val rotationAngle by animateFloatAsState(
        targetValue = if (expanded.value) 90f else 0f,
        animationSpec = tween(durationMillis = 300), label = ""
    )
    val containerColor = MaterialTheme.colorScheme.background
    IconButton(onClick = {
        expanded.value = !expanded.value // Toggle expanded state
    }) {
        Icon(
            Icons.Filled.MoreVert,
            contentDescription = "MoreVert",
            modifier = Modifier
                .rotate(rotationAngle) // Rotate based on expanded state
                .animateContentSize(), // smooth transition when rotating
            tint = containerColor
        )
    }

    // Dropdown Menu for MoreVert
    DropdownMenu(
        expanded = expanded.value,
        onDismissRequest = { expanded.value = false },
        offset = DpOffset(x = (4).dp, y = 9.dp),
        containerColor = containerColor
    ) {
        DropdownMenuItem(
            text = { Text("Delete Completed") },
            onClick = {
                deleteCompletedClick()
                expanded.value = false
            },
            modifier = Modifier.fillMaxWidth()
        )
        DropdownMenuItem(
                text = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Sort By Incomplete")
                        Checkbox(
                            checked = sortByCompleted,
                            onCheckedChange = {
                                sortCompleted() // Call your sorting function
                            },
                            modifier = Modifier.padding(0.dp),
                            colors = CheckboxDefaults.colors(
                                checkedColor = containerColor,
                                uncheckedColor = containerColor,
                            )
                        )
                    }
                },
                onClick = {
                    sortCompleted()
                    expanded.value = false
                },
                modifier = Modifier.fillMaxWidth(),
        )
    }
}
