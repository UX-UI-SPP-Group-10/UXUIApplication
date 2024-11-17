package com.group10.uxuiapp.view

import android.widget.Toast
import android.util.Log
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
import com.group10.uxuiapp.data.TaskList
import com.group10.uxuiapp.view.component.SettingsButton
import com.group10.uxuiapp.view_model.ListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskPage(taskId: Int, onNavigateBack: () -> Unit, viewModel: ListViewModel) {
    val context = LocalContext.current

    // Log the taskId and the size of the list to understand the state of data
    Log.d("TaskPage", "Task ID: $taskId, List size: ${viewModel.lists.value.size}")

    // Find the task with the given ID
    val task = viewModel.lists.value.getOrNull(taskId)

    // Check if the task is found and log the result
    if (task == null) {
        Log.d("TaskPage", "Task not found with ID: $taskId")
        Toast.makeText(context, "Task not found", Toast.LENGTH_SHORT).show()
        onNavigateBack()
        return
    } else {
        Log.d("TaskPage", "Task found: ${task.title}")
    }

    // Start the composable Scaffold layout
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(task.title) },
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
                        Toast.makeText(context, "Search clicked", Toast.LENGTH_SHORT).show()
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
                val newTask = TaskItem(label = "")
                viewModel.addTaskToList(taskId, newTask)
            }, modifier = Modifier
                .padding(bottom = 20.dp)
                .offset(x = 20.dp, y = 0.dp)) {
                Icon(Icons.Outlined.AddCircle,
                    contentDescription = "Add Task",
                    modifier = Modifier.size(40.dp))
            }
        }
    ) { innerPadding ->
        // LazyColumn will only invoke composable functions within it
        LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = innerPadding) {
            itemsIndexed(task.taskItemList) { index, taskItem -> // Use itemsIndexed to get the index
                Log.d("TaskPage", "Rendering TaskItem at index $index: ${taskItem.label}")
                TaskRow(
                    task = taskItem,
                    taskListIndex = taskId,
                    taskIndex = index,
                    viewModel = viewModel
                )
            }
        }

    }
}

@Composable
fun TaskRow(task: TaskItem, taskListIndex: Int, taskIndex: Int, viewModel: ListViewModel) {
    var isChecked by remember { mutableStateOf(task.isComplete) }
    var text by remember { mutableStateOf(task.label) }


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Checkbox(
            checked = isChecked,
            onCheckedChange = {
                isChecked = it
                viewModel.toggleIsCompletedStatus(taskListIndex, taskIndex)
            },
            modifier = Modifier.padding(end = 5.dp)
        )

        TextField(
            value = text,
            onValueChange = {
                text = it
                // Update the task's label in the ViewModel as well
                viewModel.updateTaskLabel(taskListIndex, taskIndex, it)
            },
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
        )
    }
}



@Preview(showBackground = true)
@Composable
fun TaskPagePreview() {
    // Create and set up the ViewModel inline with mock data
    val viewModel = ListViewModel().apply {
        lists.value = listOf(
            TaskList(index = 1, title = "Sample Task 1", taskItemList = mutableListOf(TaskItem(label = "Task 1"))),
            TaskList(index = 2, title = "Sample Task 2", taskItemList = mutableListOf(TaskItem(label = "Task 2")))
        )
    }

    TaskPage(taskId = 1, onNavigateBack = {}, viewModel = viewModel)
}
