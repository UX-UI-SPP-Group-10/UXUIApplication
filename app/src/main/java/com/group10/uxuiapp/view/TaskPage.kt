package com.group10.uxuiapp.view

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.group10.uxuiapp.R
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import com.group10.uxuiapp.data.data_class.TaskItem
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.tooling.preview.Preview
import com.group10.uxuiapp.view.component.SettingsButton
import com.group10.uxuiapp.view.component.TaskRowItem
import com.group10.uxuiapp.view.component.buttons.AddTaskButton
import com.group10.uxuiapp.view_model.ListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskPage(taskId: Int, onNavigateBack: () -> Unit, viewModel: ListViewModel) {
    val context = LocalContext.current

    // Observe the currently selected task list
    LaunchedEffect(taskId) {
        viewModel.selectTaskList(taskId)
    }

    val taskListWithItems by viewModel.currentTaskList.collectAsState()

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
                title = { Text(taskListWithItems!!.taskList.title) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
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
                val newTask = TaskItem(label = "New Task", taskListId = taskId)
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