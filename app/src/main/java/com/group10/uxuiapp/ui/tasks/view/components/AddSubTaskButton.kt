package com.group10.uxuiapp.ui.tasks.view.components

import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.Color

@Composable
fun AddSubTaskButton(onClick: () -> Unit) {

    Box(modifier = Modifier
        .fillMaxHeight()
        .width(25.dp)
        .clickable { onClick() },
        contentAlignment = Alignment.Center)
    {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = "Add sub Task",
            modifier = Modifier.size(20.dp)
        )
    }
}
