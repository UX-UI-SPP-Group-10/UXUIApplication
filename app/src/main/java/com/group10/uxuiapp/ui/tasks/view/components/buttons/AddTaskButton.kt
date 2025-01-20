package com.group10.uxuiapp.ui.tasks.view.components.buttons

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Button
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp

@Composable
fun AddTaskButton(
    onClick: () -> Unit,
    modifier: Modifier
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.BottomEnd
    ) {
        FloatingActionButton(
            onClick = onClick,
            containerColor = MaterialTheme.colorScheme.primaryContainer, // Background color
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer, // Content color
            shape = RoundedCornerShape(32.dp), // Rounded rectangle for FAB
            modifier = modifier
                .padding(16.dp)
                .width(150.dp)
                .height(60.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Task",
                    modifier = Modifier.size(30.dp) // Icon size
                )
                Spacer(modifier = Modifier.width(8.dp)) // Space between icon and text
                Text(
                    text = "Add Task",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}

@Preview
@Composable
fun AddTaskButtonPreview() {
    AddTaskButton(onClick = {}, modifier = Modifier)
}

