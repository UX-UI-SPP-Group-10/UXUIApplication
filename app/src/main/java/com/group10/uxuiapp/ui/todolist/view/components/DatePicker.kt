package com.group10.uxuiapp.ui.todolist.view.components

import android.app.DatePickerDialog
import android.content.Context
import android.icu.util.Calendar
import android.widget.DatePicker
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.group10.uxuiapp.data.data_class.TodoList
import com.group10.uxuiapp.ui.todolist.viewmodel.TodoListViewModel

@Composable
fun DatePickerComponent(
    context: Context,
    todoList: TodoList,
    viewModel: TodoListViewModel,
    onDateSelected: (String) -> Unit
){
    val selectedDate = remember { mutableStateOf("") }

    val calendar = Calendar.getInstance()
    DatePickerDialog(
        context,
        {
            _, year, month, dayOfMonth ->
            val formattedDate = "$dayOfMonth/${month + 1}/$year"
            selectedDate.value = formattedDate
            onDateSelected(formattedDate)

            val dueDateTimestamp = calendar.apply {
                set(
                    year, month, dayOfMonth
                )
            }.timeInMillis

            viewModel.updateTodoListDueDate(
                todoListId = todoList.id,
                dueDate = dueDateTimestamp,
                context = context,
                todoListTitle = todoList.title
            )
        },

        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    ).show()

    Spacer(modifier = Modifier.height(16.dp))
    Text(
        text = "Selected Date: ${selectedDate.value}",
        style = MaterialTheme.typography.bodyMedium
    )

}