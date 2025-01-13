package com.group10.uxuiapp.ui.todolist.view

import android.util.Log
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.group10.uxuiapp.data.data_class.TodoList
import com.group10.uxuiapp.data.data_class.TodoListWithTaskItem
import com.group10.uxuiapp.ui.navigation.AppNavigator
import com.group10.uxuiapp.ui.todolist.view.components.*
import com.group10.uxuiapp.ui.todolist.view.components.buttons.AddNewTodoListButton
import com.group10.uxuiapp.ui.todolist.view.components.buttons.SettingsButton
import com.group10.uxuiapp.ui.todolist.viewmodel.TodoListViewModel
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState

// Main TodoListScreen with Scaffold and LazyColumn
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun TodoListScreen(viewModel: TodoListViewModel, appNavigator: AppNavigator) {
    val query = remember { mutableStateOf("") }
    val popupOffset = remember { mutableStateOf(IntOffset.Zero) }
    val showLiked = remember { mutableStateOf(false) }

    // Colorpicker relevance
    val showColorPickerDialog = remember { mutableStateOf(false) }
    val selectedColor = remember { mutableStateOf("#FFFFFF") }

    // Collect the lists from the ViewModel's Flow
    val todoListsWithItems by viewModel.lists.collectAsState(emptyList())
    val selectedTodoList by viewModel.selectedTodoList.collectAsState()
    val temporaryList by viewModel.temporaryList.collectAsState()
    val popupState by viewModel.todoListState.collectAsState()




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
    //val listsToShow = if (query.value.isBlank()) todoListsWithItems else filteredLists
    val listsToShow = remember(query.value, showLiked.value, filteredLists) {
        filteredLists.filter { item ->
            val matchesQuery = item.todoList.title.contains(query.value, ignoreCase = true)
            val matchesLiked = !showLiked.value || item.todoList.isLiked
            matchesQuery && matchesLiked
        }
    }

    val searchList by viewModel.searchList.collectAsState()
    val todoLists = remember { mutableStateOf(emptyList<TodoListWithTaskItem>()) }      // temporary list while dragging. Need optimization
    todoLists.value = searchList

    val lazyListState = rememberLazyListState()
    val reorderableLazyListState = rememberReorderableLazyListState(lazyListState) { from, to ->
        val updatedList = todoLists.value.toMutableList().apply {
            add(to.index, removeAt(from.index))
        }
        todoLists.value = updatedList // Update the MutableState
    }



    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = { TopAppBarWithMenu(query, showLiked, viewModel) },
            floatingActionButton = {
                AddNewTodoListButton {
                    viewModel.setNewlistState()
                }
            }
        ) { innerPadding  ->
            val extraBottomPadding = 150.dp // Change this for more bottom padding
            LazyColumn(
                state = lazyListState,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .fillMaxSize(),
                contentPadding = PaddingValues(
                    start = innerPadding.calculateStartPadding(LocalLayoutDirection.current) + 12.dp,
                    top = innerPadding.calculateTopPadding(),
                    end = innerPadding.calculateEndPadding(LocalLayoutDirection.current) + 12.dp,
                    bottom = innerPadding.calculateBottomPadding() + extraBottomPadding
                )
            ) {
                items(todoLists.value, key = { it.todoList.id }) { item ->
                    ReorderableItem(
                        reorderableLazyListState,
                        key = item.todoList.id,
                        animateItemModifier = Modifier.animateItem()
                    ) { isDragging ->
                        val elevation by animateDpAsState(if (isDragging) 12.dp else 0.dp)
                        if(!isDragging) {
                            viewModel.updateAllListIndexes(todoLists.value)
                        }
                        TodoListCard(
                            elevation = elevation,
                            todoList = item.todoList,
                            onPositionChange = { offset, list ->
                                popupOffset.value = offset
                                viewModel.selectTodoList(list)
                            },
                            viewModel = viewModel,
                            appNavigator = appNavigator,
                            taskListsWithItems = todoListsWithItems,
                            scope = this
                        )
                    }
                    if (selectedTodoList == item.todoList) {
                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(10.dp) // spacer when active
                        )
                        Log.d("LazyColumn", "Adding Spacer below TodoList: ${item.todoList.title}")
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
////            onColorChange = {
////                viewModel.setColorPickState(selectedTodoList!!)
////            }
            onTagsEdit = {
                viewModel.setTagEditState(selectedTodoList!!)
            }
        )

        PopupManager(
            popupState = popupState,
            viewModel = viewModel,
            onNewListConfirm = { name ->
                viewModel.addTodoList(name)
                viewModel.setNoneState()
            },
            onRenameConfirm = { todoList, newName, selectedColor, selectedDate ->
                viewModel.updateTodoList(
                    todoList = todoList,
                    title = newName,
                    textColor = selectedColor,
                    dueDate = selectedDate
                    // Handle selectedDate if applicable
                )
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
private fun TopAppBarWithMenu(query: MutableState<String>, showLiked: MutableState<Boolean>, viewModel: TodoListViewModel) {
    val context = LocalContext.current
    val textFieldVisible = remember { mutableStateOf(false) }

    val searchQuery by viewModel.searchQuery.collectAsState()
    val isSearching by viewModel.isSearching.collectAsState()
    val searchList by viewModel.searchList.collectAsState()

    TopAppBar(
        title = {

            if (textFieldVisible.value) {
                val textStyle = TextStyle(
                    fontSize = 16.sp,
                    color = Color.Black
                ) // Define a consistent text style

                BasicTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.onSearchQueryChange(it) }, // Update the query state
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(45.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(color = MaterialTheme.colorScheme.onTertiary) // Use the made colors after merge
                        .padding(horizontal = 16.dp, vertical = 10.dp), // Inner padding
                    textStyle = textStyle,
                    decorationBox = { innerTextField ->
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            if (searchQuery.isEmpty()) {
                                Text(
                                    text = "Search...",
                                    style = textStyle, // Apply the same text style to the placeholder
                                    color = Color.Gray // Differentiate placeholder color
                                )
                            }
                            innerTextField()
                        }
                    }
                )
//                TextField(
//                    value = query.value,
//                    onValueChange = { query.value = it }, // Update the query state
//                    placeholder = { Text("Search...") },
//                    singleLine = true,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(45.dp)
//                        .clip(RoundedCornerShape(20.dp)),
//
//                    )
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
                showLiked = showLiked
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(),
        scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    )
}