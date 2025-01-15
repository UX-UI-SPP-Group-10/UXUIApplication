package com.group10.uxuiapp.ui.todolist.view.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.group10.uxuiapp.data.data_class.TodoList
import com.group10.uxuiapp.ui.todolist.viewmodel.TodoListViewModel

@Composable
fun DueByDate(todoList: TodoList, viewModel: TodoListViewModel){
    val context = LocalContext.current

    todoList.dueDate?.let { timestamp ->
        val formattedDate =
            java.text.SimpleDateFormat("MMM dd", java.util.Locale.getDefault()).format(timestamp)
        Text(
            text = "Due: $formattedDate",
            color = Color(android.graphics.Color.parseColor(todoList.textColor)),
            fontSize = 12.sp,
            modifier = Modifier.padding(start = 0.dp)
        )

        // Schedule the notification
        viewModel.updateTodoListDueDate(
            todoListId = todoList.id,
            dueDate = timestamp,
            context = context,
            todoListTitle = todoList.title
        )
    }
}