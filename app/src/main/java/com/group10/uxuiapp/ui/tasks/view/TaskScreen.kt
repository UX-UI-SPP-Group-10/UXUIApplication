package com.group10.uxuiapp.ui.tasks.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.group10.uxuiapp.R
import com.group10.uxuiapp.data.data_class.TaskItem
import com.group10.uxuiapp.ui.navigation.AppNavigator
import com.group10.uxuiapp.ui.tasks.view.components.SubTaskRow
import com.group10.uxuiapp.ui.tasks.view.components.TaskRowItem
import com.group10.uxuiapp.ui.tasks.view.components.buttons.AddTaskButton
import com.group10.uxuiapp.ui.tasks.view.components.buttons.SettingsButton
import com.group10.uxuiapp.ui.tasks.viewmodel.TaskViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


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
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    val coroutineScope = rememberCoroutineScope()

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
    Box(modifier = Modifier
        .fillMaxSize()
        .background(
            Brush.verticalGradient(
                colors = listOf(
                    Color(0xFFF8F8F8),
                    MaterialTheme.colorScheme.background
                )
            )
        )
        .pointerInput(Unit) {
            detectTapGestures(
                onTap = {
                    focusManager.clearFocus()
                }
            )
        }
    ) {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                AnimatedVisibility(
                    visible = topBarVisible
                )
                {
                    TopAppBar(
                        title = { Text(text = taskListWithItems!!.todoList.title,
                            color = MaterialTheme.colorScheme.onPrimary) },
                        navigationIcon = {
                            IconButton(onClick = { appNavigator.popBackStack() }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.arrow_left),
                                    contentDescription = "Back",
                                    tint = MaterialTheme.colorScheme.onPrimary
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
                                sortByCompleted = sortByComplete.value
                            )
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                        ),
                        scrollBehavior = scrollBehavior,
                    )
                }
            },
            floatingActionButton = {
                AddTaskButton(
                    onClick = {
                        // Add a new task to the TodoList
                        val newTask = TaskItem(label = "", todoListId = todoListId)
                        viewModel.addTaskToList(newTask)
                        coroutineScope.launch {
                            delay(100) // Delay for 100ms
                            focusRequester.requestFocus() // Request focus after the delay
                        }
                    }
                )
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
                            top = 12.dp,
                            bottom = innerPadding.calculateBottomPadding() + extraBottomPadding
                        ),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(
                            items = sortedTasks.flatMap { task ->
                                val taskWithSubTasks = taskItemWithSubTask.find { it.taskItem.id == task.id }
                                val sortedSubTasks = taskWithSubTasks?.subTasks?.let { subTasks ->
                                    if (sortByComplete.value) {
                                        subTasks.sortedBy { it.isComplete }
                                    } else {
                                        subTasks
                                    }
                                }.orEmpty()
                                // If the task is folded, only include the task itself
                                if (task.isFolded) {
                                    listOf(task to null)
                                } else {
                                    // Combine the task and its subtasks for rendering
                                    listOf(task to null) + sortedSubTasks.map { task to it }
                                }
                            },
                            key = { (task, subTask) ->
                                subTask?.let { "${task.id}_${it.id}" } ?: task.id // Unique key for task and subtask
                            }
                        ) { (task, subTask) ->
                            if (subTask == null) {
                                TaskRowItem(
                                    task = task,
                                    viewModel = viewModel,
                                    focusManager = focusManager,
                                    focusRequester = focusRequester
                                )
                            } else {
                                SubTaskRow(
                                    task = subTask,
                                    viewModel = viewModel,
                                    focusManager = focusManager,
                                    focusRequester = focusRequester
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
