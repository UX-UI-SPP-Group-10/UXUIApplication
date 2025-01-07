package com.group10.uxuiapp.ui.tasks.view

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.group10.uxuiapp.R
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import com.group10.uxuiapp.data.data_class.TaskItem
import com.group10.uxuiapp.ui.todolist.view.components.SettingsButton
import com.group10.uxuiapp.ui.tasks.view.components.TaskRowItem
import com.group10.uxuiapp.ui.tasks.view.components.AddTaskButton
import com.group10.uxuiapp.ui.navigation.AppNavigator
import com.group10.uxuiapp.ui.tasks.view.components.EditTaskPopup
import com.group10.uxuiapp.ui.tasks.viewmodel.TaskViewModel
import com.group10.uxuiapp.ui.todolist.viewmodel.TodoListViewModel
import okhttp3.internal.concurrent.Task
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.lazy.items


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreen(todoListId: Int, appNavigator: AppNavigator, viewModel: TaskViewModel) {
    val context = LocalContext.current

    // Set the selected TodoList ID
    LaunchedEffect(todoListId) {
        viewModel.selectTodoList(todoListId)
    }

    // Observe the current TodoList and its tasks
    val taskListWithItems by viewModel.currentTodoList.collectAsState()
    val selectedTask by viewModel.selectedTaskItem.collectAsState()

    if (taskListWithItems == null) {
        // Loading state
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(taskListWithItems!!.todoList.title) },
                navigationIcon = {
                    IconButton(onClick = { appNavigator.popBackStack() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.arrow_left),
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { Log.d("TaskPage", "Search clicked") }) {
                        Icon(Icons.Filled.Search, contentDescription = "Search")
                    }
                    SettingsButton(context = context)
                },
                colors = TopAppBarDefaults.topAppBarColors(),
                scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
            )
        },
        bottomBar = {
            AddTaskButton(onClick = {
                // Add a new task to the TodoList
                val newTask = TaskItem(label = "New Task", todoListId = todoListId)
                viewModel.addTaskToList(newTask)
            })
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.TopCenter
        ) {
            if (taskListWithItems!!.taskItems.isEmpty()) {
                // Empty state
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No tasks yet. Add some!")
                }
            } else {
                // Display task list
                LazyColumn {
                    items(
                        items = taskListWithItems!!.taskItems,
                        key = { task -> task.id }  // <--- Use each task’s unique ID here
                    ) { task ->
                        TaskRowItem(task = task, viewModel = viewModel)
                    }
                }

            }
        }
    }

    var text by remember { mutableStateOf("") }
    if (selectedTask!= null) {
        EditTaskPopup(
            taskName = selectedTask!!.label,
            onEditTask = { newName ->
                text = newName
                viewModel.updateTaskItem(selectedTask!!, label = newName)
            },
            onDeleteTask = { viewModel.deleteTask(selectedTask!!) },
            onDismiss = { viewModel.selectTask(null) }
        )
    }
}
