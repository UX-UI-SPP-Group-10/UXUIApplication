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
import com.group10.uxuiapp.data.data_class.TaskItem
import com.group10.uxuiapp.ui.tasks.view.components.TaskRowItem
import com.group10.uxuiapp.ui.tasks.view.components.buttons.AddTaskButton
import com.group10.uxuiapp.ui.navigation.AppNavigator
import com.group10.uxuiapp.ui.tasks.view.components.EditTaskPopup
import com.group10.uxuiapp.ui.tasks.viewmodel.TaskViewModel
import androidx.compose.foundation.lazy.items
import com.group10.uxuiapp.ui.tasks.view.components.buttons.SettingsButton
import com.group10.uxuiapp.data.data_class.SubTask
import com.group10.uxuiapp.ui.tasks.view.components.AddSubTaskButton
import com.group10.uxuiapp.ui.tasks.view.components.SubTaskRow


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
    val taskItemWithSubTask by viewModel.lists.collectAsState()
    val lastSelectedTask by viewModel.lastSelectedTaskItem.collectAsState()

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
                    SettingsButton(
                        context = context,
                        onSetting1Click = { Log.d("TaskPage", "Setting 1 clicked") },
                        onSetting2Click = { Log.d("TaskPage", "Setting 2 clicked") },
                        onSetting3Click = { Log.d("TaskPage", "Setting 3 clicked") }
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(),
                scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
            )
        },
        bottomBar = {
            AddSubTaskButton(onClick = {
                if(lastSelectedTask != null){
                    val newSubTask = lastSelectedTask.let { SubTask(label = "", taskItemId = lastSelectedTask!!.id) }
                    viewModel.addSupTask(newSubTask)
                }
            })
            AddTaskButton(onClick = {
                // Add a new task to the TodoList
                val newTask = TaskItem(label = "", todoListId = todoListId)
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
                        key = { task -> task.id }
                    ) { task ->
                        TaskRowItem(task = task, viewModel = viewModel)
                        val taskWithSubTasks = taskItemWithSubTask.find { it.taskItem.id == task.id }

                        if (taskWithSubTasks != null && !task.isFolded) {
                            taskWithSubTasks.subTasks.forEach { subTask ->
                                SubTaskRow(subTask, viewModel)
                            }
                        }
                    }
                }

            }
        }
    }

    if (selectedTask != null) {
        EditTaskPopup(
            taskName = selectedTask!!.label,
            onSaveTask = { newName ->
                viewModel.updateTaskItem(taskItem = selectedTask!!, label = newName)
                viewModel.selectTask(null)
            },
            onDeleteTask = {
                viewModel.deleteTask(selectedTask!!)
                viewModel.selectTask(null)
            },
            onDismiss = {
                viewModel.selectTask(null)
            }
        )
    }

}
