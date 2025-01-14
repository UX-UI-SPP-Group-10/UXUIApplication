package com.group10.uxuiapp.ui.tasks.view.components.buttons

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.group10.uxuiapp.R

@Composable
fun Delete(onClick: () -> Unit){
    Icon(
        painter = painterResource(id = R.drawable.delete),
        contentDescription = "Delete",
        modifier = Modifier.size(20.dp).clickable { onClick() },
        tint = MaterialTheme.colorScheme.onTertiaryContainer
    )
}