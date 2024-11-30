package com.group10.uxuiapp.view

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.group10.uxuiapp.data.TaskItem
import com.group10.uxuiapp.data.TaskListWithItems
import com.group10.uxuiapp.view.component.SettingsButton
import com.group10.uxuiapp.view.component.TaskRowItem
import com.group10.uxuiapp.view_model.ListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskPage(taskId: Int, onNavigateBack: () -> Unit, viewModel: ListViewModel) {
    val context = LocalContext.current

    // Fetch the task list with items from the ViewModel
    val taskListWithItems = viewModel.lists.value.find { it.taskList.id == taskId }

    if (taskListWithItems == null) {
        Log.d("TaskPage", "Task not found with ID: $taskId")
        onNavigateBack()
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(taskListWithItems.taskList.title) },
                modifier = Modifier.padding(8.dp),
                navigationIcon = {
                    IconButton(onClick = { onNavigateBack() }) {
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
            // Add Task button
            IconButton(onClick = {
                Log.d("TaskPage", "Add task button clicked for taskId: $taskId")
                val newTask = TaskItem(label = "", taskListId = taskId)
                viewModel.addTaskToList(newTask)
            }, modifier = Modifier
                .padding(bottom = 20.dp)
                .offset(x = 20.dp, y = 0.dp)) {
                Icon(Icons.Outlined.AddCircle,
                    contentDescription = "Add Task",
                    modifier = Modifier.size(40.dp))
            }
        }
    ) { innerPadding ->
        LazyColumn(contentPadding = innerPadding) {
            itemsIndexed(taskListWithItems.taskItems) { index, taskItem ->
                TaskRowItem(taskItem, viewModel)
            }
        }
    }
}