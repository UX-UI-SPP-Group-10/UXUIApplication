package com.group10.uxuiapp.ui.todolist.view.components.buttons

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.group10.uxuiapp.data.data_class.TodoList


@Composable
fun IsLikedButton(todoList: TodoList, onClick: () -> Unit) {
    val isLiked = todoList.isLiked

    Box(
        modifier = Modifier
            .size(30.dp)
            .clickable { onClick() }
    ) {
        Icon(
            imageVector = Icons.Default.FavoriteBorder,
            contentDescription = "Favorite Outline",
            modifier = Modifier
                .size(25.dp)
                .align(Alignment.Center),
            tint = Color(android.graphics.Color.parseColor(todoList.textColor))
        )
        if (isLiked) {
            Icon(
                imageVector = Icons.Default.FavoriteBorder,
                contentDescription = "Favorite Outline",
                modifier = Modifier
                    .size(29.dp)
                    .align(Alignment.Center),
                tint = Color(0X33000000)
            )
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = "Liked",
                modifier = Modifier
                    .size(25.dp)
                    .align(Alignment.Center),
                tint = Color.Red
            )
        }
    }
}