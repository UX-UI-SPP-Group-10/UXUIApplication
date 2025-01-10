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
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.graphics.Color

@Composable
fun AddTaskButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxWidth().padding(12.dp),
        contentAlignment = Alignment.BottomEnd
    ) {
        Button(
            onClick = onClick,
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .padding(8.dp)
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.tertiary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.AddCircle,
                    contentDescription = "Add Task",
                    modifier = Modifier.size(20.dp),
                    tint = Color(0XFF20792F)
                )
                Text(
                    text = "Add task",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color(0XFF20792F)
                )
            }
        }
    }
}

