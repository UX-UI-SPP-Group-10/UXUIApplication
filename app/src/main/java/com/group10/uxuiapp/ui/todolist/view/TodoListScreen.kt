package com.group10.uxuiapp.ui.todolist.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.group10.uxuiapp.ui.navigation.AppNavigator
import com.group10.uxuiapp.ui.todolist.view.components.*
import com.group10.uxuiapp.ui.todolist.view.components.buttons.AddNewTodoListButton
import com.group10.uxuiapp.ui.todolist.view.components.buttons.SettingsButton
import com.group10.uxuiapp.data.GiphyActivity
import com.group10.uxuiapp.data.data_class.TodoListWithTaskItem
import com.group10.uxuiapp.ui.todolist.view.components.ColorPickerDialog
import com.group10.uxuiapp.ui.todolist.view.components.ListNameInputDialog
import com.group10.uxuiapp.ui.todolist.view.components.SettingsButton
import com.group10.uxuiapp.ui.todolist.viewmodel.TodoListViewModel

// Main TodoListScreen with Scaffold and LazyColumn
@Composable
fun TodoListScreen(viewModel: TodoListViewModel, appNavigator: AppNavigator) {
    val query = remember { mutableStateOf("") }
    val popupOffset = remember { mutableStateOf(IntOffset.Zero) }

    // Colorpicker relevance
    val showColorPickerDialog = remember { mutableStateOf(false) }
    val selectedColor = remember { mutableStateOf("#FFFFFF") }

    // Collect the lists from the ViewModel's Flow
    val todoListsWithItems by viewModel.lists.collectAsState(emptyList())
    val selectedTodoList by viewModel.selectedTodoList.collectAsState()
    val popupState by viewModel.todoListState.collectAsState()
    val listPositionState = rememberLazyListState()


    // Use LaunchedEffect to reset selectedIndex if list size changes
    LaunchedEffect(todoListsWithItems) {
        if (selectedTodoList != null &&
            todoListsWithItems.none { it.todoList == selectedTodoList }
        ) {
            viewModel.selectTodoList(null)
        }
    }

    // Filter the lists whenever query.value changes
    val filteredLists = remember(query.value, todoListsWithItems) {
        todoListsWithItems.filter {
            it.todoList.title.contains(query.value, ignoreCase = true)
        }
    }

    // Decide which list to show: full or filtered
    val listsToShow = if (query.value.isBlank()) todoListsWithItems else filteredLists

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = { TopAppBarWithMenu(query) },
            floatingActionButton = {
                AddNewTodoListButton {
                    viewModel.setNewlistState()
                }
            }
        ) { innerPadding  ->

            LazyColumn (
                state = listPositionState,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(
                    start = innerPadding.calculateStartPadding(LocalLayoutDirection.current) + 12.dp, // Extra left padding
                    top = innerPadding.calculateTopPadding(),
                    end = innerPadding.calculateEndPadding(LocalLayoutDirection.current) + 12.dp, // Extra right padding
                    bottom = innerPadding.calculateBottomPadding() + 100.dp // Existing extra bottom padding
                )
            ){
                items(
                    items = listsToShow,
                    key = { it.todoList.id }
                ) { taskListWithItems ->
                    val todoList = taskListWithItems.todoList

                    TodoListCard(
                        todoList = todoList,
                        onPositionChange = { offset, list ->
                            popupOffset.value = offset
                            viewModel.selectTodoList(list)
                        },
                        viewModel = viewModel,
                        appNavigator = appNavigator,
                        taskListsWithItems = todoListsWithItems
                    )
                    if (todoList == selectedTodoList) {
                        Spacer(modifier = Modifier.height(15.dp)) // Adjust height as needed
                    }
                }
            }
        }

        OptionsPopup(
            expanded = selectedTodoList != null,
            onDismissRequest = { viewModel.selectTodoList(null) },
            onClose = {
                viewModel.selectTodoList(null)
            },
            onDelete = {
                viewModel.removeTodoList(selectedTodoList!!)
                viewModel.selectTodoList(null)
            },
            onUpdate = {
                viewModel.setRenameState(selectedTodoList!!)
            },
            onGifSelect = {
                viewModel.setSelectGifState(selectedTodoList!!)
            },
            offset = popupOffset.value,
            onColorChange = {showColorPickerDialog.value = true}
        )

//        // Show GiphyDialog when needed
//        if (showGiphyDialog.value) {
//            GiphyDialog(
//                context = context,
//                onGifSelected = { gifUrl ->
//                    // Update the GIF URL in ViewModel
//                    selectedTodoList?.let { todoList ->
//                        viewModel.updateGifUrl(todoList.id, gifUrl)
//                    }
//                    showGiphyDialog.value = false
//                },
//                onDismissed = {
//                    showGiphyDialog.value = false
//                }
//            )
//        }

        PopupManager(
            popupState = popupState,
            onNewListConfirm = { name ->
                viewModel.addTodoList(name)
                viewModel.setNoneState()
            },
            onRenameConfirm = { todoList, newName ->
                viewModel.updateTodoList(todoList, newName)
                viewModel.setNoneState()
            },
            onGifSelected = { todoList, gifUrl ->
                viewModel.updateGifUrl(todoList.id, gifUrl)
                viewModel.setNoneState()
            },
            onDismiss = {
                viewModel.setNoneState()
            }
        )
        // Show ColorPickerDialog
        if (showColorPickerDialog.value) {
            ColorPickerDialog(
                isDialogOpen = showColorPickerDialog,
                onColorSelected = { color ->
                    selectedTodoList?.let { todoList ->
                        viewModel.updateTextColor(todoList.id, color)
                        showColorPickerDialog.value = false
                    }
                }
            )
        }
    }
}


// Top app bar with search and settings icons and dropdown menu
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopAppBarWithMenu(query: MutableState<String>) {
    val context = LocalContext.current
    val textFieldVisible = remember { mutableStateOf(false) }

    TopAppBar(
        title = {

            if (textFieldVisible.value) {
                TextField(
                    value = query.value,
                    onValueChange = { query.value = it }, // Update the query state
                    placeholder = { Text("Search...") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(45.dp)
                        .clip(RoundedCornerShape(20.dp)),

                )
            } else {
                Text(text = "")
            }
        },
        modifier = Modifier,
        navigationIcon = {
            IconButton(onClick = {
                textFieldVisible.value = !textFieldVisible.value
            }) {
                Icon(Icons.Filled.Search, contentDescription = "Search")
            }
        },
        actions = {
            SettingsButton(context = context)
        },
        colors = TopAppBarDefaults.topAppBarColors(),
        scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    )
}

@Composable
private fun ListItem(
    todoList: TodoList,
    isSelected: Boolean,
    viewModel: TodoListViewModel,
    appNavigator: AppNavigator,
    onPositionChange: (Offset, TodoList) -> Unit,
    taskListsWithItems: List<TodoListWithTaskItem>
) {
    val context = LocalContext.current
    val listNameState = remember { mutableStateOf(todoList.title) }
    val showDialog = remember { mutableStateOf(false) }
    val density = LocalDensity.current
    var itemPosition by remember { mutableStateOf(Offset.Zero) }

    // Remember the background based on gifUrl
    if (!todoList.gifUrl.isNullOrEmpty()) {

        Modifier.fillMaxSize()
            .then(
                Modifier.background(Color.Transparent)
            )

    } else {
        Modifier.background(
            Brush.linearGradient(
                colors = listOf(
                    MaterialTheme.colorScheme.primary,
                    MaterialTheme.colorScheme.secondary,
                    Color(0xFFC0DCEF)
                ),
                start = Offset(0f, 0f),
                end = Offset(0f, Float.POSITIVE_INFINITY)
            )
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .clip(RoundedCornerShape(20.dp))
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {
                            val refreshedTaskList = taskListsWithItems.find { it.todoList.id == todoList.id }
                            Log.d("ListItem", "Tapped Item Index: Task ID: ${todoList.id}, Refreshed Task: ${refreshedTaskList?.todoList?.id}")
                            if (refreshedTaskList != null) {
                                appNavigator.navigateToTask(refreshedTaskList.todoList.id)
                            } else {
                                Log.e("ListItem", "No valid list to navigate to.")
                            }
                        },
                        onLongPress = {
                            val yOffset = with(density) { itemPosition.y * ( + 1) + 4.dp.toPx() }
                            onPositionChange(Offset(98f, yOffset), todoList)
                        }
                    )
                }
        ) {
            // GIF as background (placed first to be behind everything else)
            if (!todoList.gifUrl.isNullOrEmpty()) {
                val gifPainter = rememberAsyncImagePainter(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(todoList.gifUrl)
                        .decoderFactory(GifDecoder.Factory())
                        .build()
                )
                Image(
                    painter = gifPainter,
                    contentDescription = "GIF Background",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop // Ensures the GIF fills the box area
                )
            } else {
                // Default gradient background if no GIF is provided
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primary,
                                    MaterialTheme.colorScheme.secondary,
                                    Color(0xFFC0DCEF)
                                ),
                                start = Offset(0f, 0f),
                                end = Offset(0f, Float.POSITIVE_INFINITY)
                            )
                        )
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = todoList.title,
                    color = Color(android.graphics.Color.parseColor(todoList.textColor)),
                    modifier = Modifier.width(320.dp)
                )
                LikedButton(todoList, onClick = {
                    viewModel.updateTodoList(todoList, isLiked = !todoList.isLiked)
                })
            }
        }
    }

    if (isSelected) {
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun LikedButton(todoList: TodoList, onClick: () -> Unit) {
    val isLiked = todoList.isLiked
