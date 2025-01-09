package com.group10.uxuiapp.ui.todolist.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.group10.uxuiapp.ui.navigation.AppNavigator
import com.group10.uxuiapp.ui.todolist.view.components.*
import com.group10.uxuiapp.ui.todolist.view.components.buttons.AddNewTodoListButton
import com.group10.uxuiapp.ui.todolist.view.components.buttons.SettingsButton
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
            onColorChange = {
                viewModel.setColorPickState(selectedTodoList!!)
            },
            onTagsEdit = {
                viewModel.setTagEditState(selectedTodoList!!)
            }
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
            onColorSelected = { todoList, color ->
                viewModel.updateTextColor(todoList.id, color)
                viewModel.setNoneState()
            },
            onTagsEdited = { todoList, tags ->
                viewModel.updateTags(todoList.id, tags)
                viewModel.setNoneState()
            },
            onDismiss = {
                viewModel.setNoneState()
            }
        )
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
            SettingsButton(
                context = context,
                onSetting1Click = {  }, // TODO add settings 1
                onSetting2Click = {  } , // TODO add settings 2
                onSetting3Click = {  }) // TODO add settings 3
        },
        colors = TopAppBarDefaults.topAppBarColors(),
        scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    )
}