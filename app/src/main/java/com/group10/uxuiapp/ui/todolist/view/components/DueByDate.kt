package com.group10.uxuiapp.ui.todolist.view.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.group10.uxuiapp.data.data_class.TodoList

@Composable
fun DueByDate(todoList: TodoList){
    todoList.dueDate?.let { timestamp ->
        val formattedDate =
            java.text.SimpleDateFormat("MMM dd", java.util.Locale.getDefault()).format(timestamp)
        Text(
            text = "Due: $formattedDate",
            color = MaterialTheme.colorScheme.background,
            fontSize = 12.sp,
            modifier = Modifier.padding(start = 4.dp)
        )
    }
}