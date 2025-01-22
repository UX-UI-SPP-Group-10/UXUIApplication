package com.group10.uxuiapp.ui.tasks.view.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.ui.Alignment

@Composable
fun TaskFolderButton(onClick: () -> Unit, isFoldet: Boolean) {

    Box(modifier = Modifier
        .fillMaxHeight()
        .width(25.dp)
        .clickable { onClick() },
        contentAlignment = Alignment.Center)
    {
        Icon(
            imageVector = if (isFoldet) Icons.Filled.KeyboardArrowDown else Icons.Filled.KeyboardArrowUp,
            contentDescription = "fold in or out",
            modifier = Modifier.size(20.dp)
        )
    }
}

