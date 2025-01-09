package com.group10.uxuiapp.ui.todolist.view.components

import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp

@Composable
fun ColorPicker(onColorSelect: (String) -> Unit) {
    val colors = listOf(
        "#000000", "#FFFFFF", "#FF5733", "#0000FF", "#00FF00", "#FFFF00", "#FF00FF", "#00FFFF"
    )

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text("Pick a Color", style = MaterialTheme.typography.titleMedium)

        Spacer(modifier = Modifier.height(16.dp))

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
                        .background(color = Color(android.graphics.Color.parseColor(colorHex)), CircleShape)
                        .clickable{ onColorSelect(colorHex)}
                )

            }
        }
    }
}

//fun ColorPickerDialog(onDismissed: () -> Unit,  onColorSelected: (String) -> Unit) {
//    AlertDialog(
//        onDismissRequest = { onDismissed },
//        title = { Text("Pick a Color") },
//        text = {
//            ColorPicker(
//                onColorSelect = { hexColor ->
//                onColorSelected(hexColor)
//                })
//        },
//        confirmButton = {
//            Button(onClick = onDismissed ) {
//                Text("Close")
//            }
//        }
//    )
//}


//    val expanded = remember { mutableStateOf(false) }
//
//    Button(onClick = {
//        expanded.value = !expanded.value // Toggle expanded state
//    }) {
//        Text("Choose a color")
//    }
//
//    DropdownMenu(
//        expanded = expanded.value,
//        onDismissRequest = { expanded.value = false },
//        offset = DpOffset(x = (0).dp, y = 0.dp)
//    ) {
//        DropdownMenuItem(
//            text = { Text("Black", color = Color.Black) },
//            onClick = {
//                onColorSelect("#000000")
//                expanded.value = false },
//            modifier = Modifier.fillMaxWidth()
//        )
//        DropdownMenuItem(
//            text = { Text("White", color = Color.White) },
//            onClick = {
//                onColorSelect("#ffffff")
//                expanded.value = false },
//            modifier = Modifier.fillMaxWidth()
//        )
//        DropdownMenuItem(
//            text = { Text("Red", color = Color.Red) },
//            onClick = {
//                onColorSelect("#FF5733")
//                expanded.value = false },
//            modifier = Modifier.fillMaxWidth()
//        )
//        DropdownMenuItem(
//            text = { Text("Blue", color = Color.Blue) },
//            onClick = {
//                onColorSelect("#0000FF")
//                expanded.value = false },
//            modifier = Modifier.fillMaxWidth()
//        )
//        DropdownMenuItem(
//            text = { Text("Green", color = Color.Green) },
//            onClick = {
//                onColorSelect("#00ff00")
//                expanded.value = false },
//            modifier = Modifier.fillMaxWidth()
//        )
//        DropdownMenuItem(
//            text = { Text("Yellow", color = Color.Yellow) },
//            onClick = {
//                onColorSelect("#ffff00")
//                expanded.value = false },
//            modifier = Modifier.fillMaxWidth()
//        )




