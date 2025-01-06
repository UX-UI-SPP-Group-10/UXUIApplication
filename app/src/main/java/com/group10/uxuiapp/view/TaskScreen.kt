package com.group10.uxuiapp.view

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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
import com.group10.uxuiapp.data.data_class.TaskItem
import com.group10.uxuiapp.view.component.SettingsButton
import com.group10.uxuiapp.view.component.TaskRowItem
import com.group10.uxuiapp.view.component.buttons.AddTaskButton
import com.group10.uxuiapp.view_model.ListViewModel
import com.group10.uxuiapp.ui.navigation.AppNavigator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreen(taskId: Int, appNavigator: AppNavigator, viewModel: ListViewModel) {
    val context = LocalContext.current

    // Observe the currently selected task list
    LaunchedEffect(taskId) {
        viewModel.selectTodoList(taskId)
    }

    val taskListWithItems by viewModel.currentTodoList.collectAsState()

    if (taskListWithItems == null) {
        // Show a loading indicator while the data is being fetched
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
                val newTask = TaskItem(label = "New Task", todoListId = taskId)
                viewModel.addTaskToList(newTask)
            })
        }
    ) { innerPadding ->
        if (taskListWithItems!!.taskItems.isEmpty()) {
            // Show a message if no tasks are present
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("No tasks yet. Add some!")
            }
        } else {
            // Display the list of tasks
            LazyColumn(contentPadding = innerPadding) {
                itemsIndexed(taskListWithItems!!.taskItems) { _, task ->
                    TaskRowItem(task = task, viewModel = viewModel)
                }
            }
        }
    }
}