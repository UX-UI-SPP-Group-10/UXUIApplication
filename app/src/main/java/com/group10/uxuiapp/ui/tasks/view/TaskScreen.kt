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
import androidx.compose.ui.Alignment
import com.group10.uxuiapp.ui.todolist.view.components.SettingsButton
import com.group10.uxuiapp.ui.tasks.view.components.TaskRowItem
import com.group10.uxuiapp.ui.tasks.view.components.AddTaskButton
import com.group10.uxuiapp.ui.navigation.AppNavigator
import com.group10.uxuiapp.ui.todolist.viewmodel.TodoListViewModel
import okhttp3.internal.concurrent.Task

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreen(todoListId: Int, appNavigator: AppNavigator, viewModel: TaskListViewModel) {
    val context = LocalContext.current

    // Load the TodoList and its tasks when the screen is displayed
    LaunchedEffect(todoListId) {
        viewModel.loadTodoList(todoListId)
    }

    // Collect the current TodoList and its tasks as state
    val currentTodoList by viewModel.currentTodoList.collectAsState()
    val tasks by viewModel.tasks.collectAsState()

    // Show a loading indicator if the TodoList data is not yet available
    if (currentTodoList == null) {
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
                title = { Text(currentTodoList!!.title) }, // Use the TodoList title
                navigationIcon = {
                    IconButton(onClick = { appNavigator.popBackStack() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.arrow_left),
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        Log.d("TaskPage", "Search clicked")
                    }) {
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
                // Add a new task to the current TodoList
                viewModel.addTask("New Task")
            })
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.TopCenter
        ) {
            if (tasks.isEmpty()) {
                // Show a message if no tasks are present
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No tasks yet. Add some!")
                }
            } else {
                // Display the list of tasks
                LazyColumn {
                    itemsIndexed(tasks) { _, task ->
                        TaskRowItem(task = task, viewModel = viewModel)
                    }
                }
            }
        }
    }
}
