package com.group10.uxuiapp.ui.tasks.view

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import com.group10.uxuiapp.ui.tasks.view.components.buttons.SettingsButton
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
    val selectedSubTask by viewModel.selectedSubTask.collectAsState()
    val taskItemWithSubTask by viewModel.lists.collectAsState()
    val lazyListState = rememberLazyListState()
    val topBarVisible = lazyListState.firstVisibleItemIndex == 0 && lazyListState.firstVisibleItemScrollOffset < 150
    val sortByComplete = remember { mutableStateOf(false) }


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

    val sortedTasks = remember(taskListWithItems?.taskItems, sortByComplete.value) {
        taskListWithItems?.taskItems?.let {
            if (sortByComplete.value) it.sortedBy { task ->  task.isComplete } else it
        } ?: emptyList()
    }

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    Scaffold(
        topBar = {
            AnimatedVisibility(
                  visible = topBarVisible,
                  enter = slideInVertically { -it },
                  exit = slideOutVertically { -it }
            )
            {
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
                        SettingsButton(
                            context = context,
                            //sortByComplete = sortByComplete
                            deleteCompletedClick = { viewModel.deleteCompletedTasks(todoListId) },
                            sortCompleted = {
                                sortByComplete.value = !sortByComplete.value
                            },
                            onSetting3Click = { Log.d("TaskPage", "Setting 3 clicked") }
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(),
                    scrollBehavior = scrollBehavior
                )
            }
        },
        floatingActionButton = {
            AddTaskButton(onClick = {
                // Add a new task to the TodoList
                val newTask = TaskItem(label = "", todoListId = todoListId)
                viewModel.addTaskToList(newTask)
            })
        }
    ) { innerPadding ->
        val extraBottomPadding = 450.dp
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.TopCenter
        ) {
            if (sortedTasks.isEmpty()) {
                // Empty state
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No tasks yet. Add some!")
                }
            } else {
                // Display task list
                LazyColumn(
                    state = lazyListState,
                    modifier = Modifier
                        .fillMaxSize(),
                    contentPadding = PaddingValues(
                        end = innerPadding.calculateEndPadding(LocalLayoutDirection.current) + 12.dp,
                        start = innerPadding.calculateStartPadding(LocalLayoutDirection.current) + 12.dp,
                        bottom = innerPadding.calculateBottomPadding() + extraBottomPadding
                    ),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ){
                    items(
                        items = sortedTasks,
                        key = { task -> task.id }
                    ) { task ->
                        TaskRowItem(task = task, viewModel = viewModel)
                        val taskWithSubTasks = taskItemWithSubTask.find { it.taskItem.id == task.id }

                        if (taskWithSubTasks != null && !task.isFolded) {
                            val sortedSubTasks = remember(taskWithSubTasks.subTasks, sortByComplete.value) {
                                if (sortByComplete.value) {
                                    taskWithSubTasks.subTasks.sortedBy { subTask -> subTask.isComplete }
                                } else {
                                    taskWithSubTasks.subTasks
                                }
                            }
                            sortedSubTasks.forEach {
                                subTask ->  SubTaskRow(subTask, viewModel)
                            }
                        }
                    }
                }
            }
        }
    }
}
