package com.group10.uxuiapp.ui.todolist.view.components

import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp

@Composable
fun ColorPicker(
    currentTextColor: String,
    currentBackgroundColor: String,
    onColorSelect: (String) -> Unit,
    onBackgroundColorSelect: (String) -> Unit,
    onResetGifUrl:()-> Unit) {
    val colors = listOf(
        "#000000", "#FFFFFF", "#FF5733", "#0000FF", "#00FF00", "#FFFF00", "#FF00FF", "#00FFFF"
    )


    val windowCurrentColor = remember { mutableStateOf(currentTextColor) }
    val windowCurrentBackgroundColor = remember { mutableStateOf(currentBackgroundColor) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Text Color Picker
        Column {
            Text(
                "Pick Text Color",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(4), // 4 columns
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(colors.size) { index ->
                    val colorHex = colors[index]
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(
                                color = Color(android.graphics.Color.parseColor(colorHex)),
                                CircleShape
                            )
                            .border(
                                if (windowCurrentColor.value == colorHex) 4.dp else 0.dp,
                                color = if (windowCurrentColor.value == colorHex) MaterialTheme.colorScheme.primary else Color.Transparent,
                                shape = CircleShape
                            )
                            .clickable {
                                onColorSelect(colorHex)
                                windowCurrentColor.value = colorHex
                            }
                    )
                }
            }
        }

        // Background Color Picker
        Column {
            Text(
                "Pick Background Color",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(4), // 4 columns
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(colors.size) { index ->
                    val backgroundColorHex = colors[index]
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(
                                color = Color(android.graphics.Color.parseColor(backgroundColorHex)),
                                CircleShape
                            )
                            .border(
                                if (windowCurrentBackgroundColor.value == backgroundColorHex) 4.dp else 0.dp,
                                color = if (windowCurrentBackgroundColor.value == backgroundColorHex) MaterialTheme.colorScheme.primary else Color.Transparent,
                                shape = CircleShape
                            )
                            .clickable {
                                onBackgroundColorSelect(backgroundColorHex)
                                windowCurrentBackgroundColor.value = backgroundColorHex
                                onResetGifUrl()
                            }
                    )
                }
            }
        }
    }
}




