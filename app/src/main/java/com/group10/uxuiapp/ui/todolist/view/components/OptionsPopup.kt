package com.group10.uxuiapp.ui.todolist.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.Composable
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
    onClose: () -> Unit,
    onDelete: () -> Unit,
    onUpdate: () -> Unit,
    onGifSelect: () -> Unit,
    onColorChange: () -> Unit,
    offset: IntOffset
) {
    val boxHeight = 60.dp
    val boxWidth = 225.dp

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
            // The content that was previously in Box(...)
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.tertiary)
                    .height(boxHeight)
                    .width(boxWidth),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = androidx.compose.ui.Modifier
                        .padding(horizontal = 8.dp, vertical = 8.dp)
                ) {
                    // 1) Close
                    IconButton(onClick = onClose) {
                        Icon(
                            painter = painterResource(id = R.drawable.close),
                            contentDescription = "Close",
                            tint = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    }
                    Spacer(modifier = androidx.compose.ui.Modifier.width(8.dp))

                    // 2) Select GIF
                    IconButton(onClick = onGifSelect) {
                        Icon(
                            painter = painterResource(id = R.drawable.gif),
                            contentDescription = "GIF",
                            tint = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    }
                    Spacer(modifier = androidx.compose.ui.Modifier.width(8.dp))

                    // 3) Rename
                    IconButton(onClick = onUpdate) {
                        Icon(
                            painter = painterResource(id = R.drawable.edit),
                            contentDescription = "Rename",
                            tint = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    }
                    Spacer(modifier = androidx.compose.ui.Modifier.width(8.dp))

                    // 4) Change color
                    IconButton(onClick = onColorChange) {
                        Icon(
                            painter = painterResource(id = R.drawable.palette),
                            contentDescription = "Color",
                            tint = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    }

                    // 5) Delete
                    IconButton(onClick = onDelete) {
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
}
