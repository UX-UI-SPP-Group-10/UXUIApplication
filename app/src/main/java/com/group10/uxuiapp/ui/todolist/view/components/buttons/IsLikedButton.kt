package com.group10.uxuiapp.ui.todolist.view.components.buttons

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.group10.uxuiapp.data.data_class.TodoList


@Composable
fun IsLikedButton(todoList: TodoList, onClick: () -> Unit) {
    val isLiked = todoList.isLiked

    Icon(
        imageVector = if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
        contentDescription = if (isLiked) "Liked" else "Add Favorite",
        modifier = Modifier
            .size(25.dp)
            .clickable { onClick() }, // Use the passed lambda here
        tint = if (isLiked) Color.Red else Color(android.graphics.Color.parseColor(todoList.textColor))
    )
}