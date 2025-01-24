package com.group10.uxuiapp.ui.todolist.view

import android.R
import android.R.attr.navigationIcon
import android.R.attr.singleLine
import android.R.attr.textStyle
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.group10.uxuiapp.data.data_class.TodoListWithTaskItem
import com.group10.uxuiapp.ui.navigation.AppNavigator
import com.group10.uxuiapp.ui.todolist.view.components.*
import com.group10.uxuiapp.ui.todolist.view.components.buttons.AddNewTodoListButton
import com.group10.uxuiapp.ui.todolist.view.components.buttons.SettingsButton
import com.group10.uxuiapp.ui.todolist.viewmodel.TodoListViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import com.giphy.sdk.analytics.GiphyPingbacks.context


// Main TodoListScreen with Scaffold and LazyColumn
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun TodoListScreen(viewModel: TodoListViewModel, appNavigator: AppNavigator) {
    val popupOffset = remember { mutableStateOf(IntOffset.Zero) }
    val showLiked = remember { mutableStateOf(false) }

    // Collect the lists from the ViewModel's Flow
    val selectedTodoList by viewModel.selectedTodoList.collectAsState()
    val popupState by viewModel.todoListState.collectAsState()
    val searchList by viewModel.searchList.collectAsState()

    val todoLists = remember { mutableStateOf(emptyList<TodoListWithTaskItem>()) }      // temporary list while dragging. Need optimization
    todoLists.value = searchList
    todoLists.value = if (showLiked.value) {
        searchList.filter { it.todoList.isLiked }
    } else {
        searchList
    }

    val lazyListState = rememberLazyListState()
    val reorderableLazyListState = rememberReorderableLazyListState(lazyListState) { from, to ->
        val updatedList = todoLists.value.toMutableList().apply {
            add(to.index, removeAt(from.index))
        }
        todoLists.value = updatedList // Update the MutableState
    }

    val coroutineScope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current

    Box(modifier = Modifier
        .fillMaxSize()
        .pointerInput(Unit) {
            // Detect tap gestures on the whole screen
            detectTapGestures(
                onTap = {
                    focusManager.clearFocus() // Clear focus when tapping anywhere outside the focused TextField
                    viewModel.resetNewTodoList()
                }
            )
        }
        .background(
            Brush.verticalGradient(
                colors = listOf(
                    Color(0xFFF8F8F8),
                    MaterialTheme.colorScheme.background
                )
            )
        )
    ) {
        Scaffold(
            topBar = { TopAppBarWithMenu(showLiked, viewModel) },
            floatingActionButton = {
                AddNewTodoListButton {
                    viewModel.addTodoList("")
                    coroutineScope.launch {
                        val lastIndex = todoLists.value.size - 1
                        if (lastIndex >= 0) {
                            lazyListState.animateScrollToItem(index = lastIndex)
                        }
                    }
                }
            },
            containerColor = Color.Transparent
        ) { innerPadding  ->
            val extraBottomPadding = 300.dp // Change this for more bottom padding
            LazyColumn(
                state = lazyListState,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .fillMaxSize(),
                contentPadding = PaddingValues(
                    start = innerPadding.calculateStartPadding(LocalLayoutDirection.current) + 12.dp,
                    top = innerPadding.calculateTopPadding() + 12.dp,
                    end = innerPadding.calculateEndPadding(LocalLayoutDirection.current) + 12.dp,
                    bottom = innerPadding.calculateBottomPadding() + extraBottomPadding
                )
            ) {
                if (todoLists.value.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(top = 100.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Create a new list to get started",
                                style = TextStyle(
                                    fontSize = 20.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                                    fontWeight = FontWeight.Bold
                                ),
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                } else {
                    items(todoLists.value, key = { it.todoList.id }) { item ->
                        ReorderableItem(
                            reorderableLazyListState,
                            key = item.todoList.id,
                            animateItemModifier = Modifier.animateItem()
                        ) { isDragging ->
                            val elevation by animateDpAsState(if (isDragging) 12.dp else 4.dp)
                            LaunchedEffect(isDragging) {
                                viewModel.setDraggingState(isDragging)
                                if (!isDragging) {
                                    // Save the updated order when dragging stops
                                    viewModel.updateAllListIndexes(todoLists.value)
                                }
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
                                scope = this,
                                focusManager = focusManager
                            )
                        }
                        if (selectedTodoList == item.todoList) {
                            Spacer(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(10.dp) // spacer when active
                            )
                            Log.d(
                                "LazyColumn",
                                "Adding Spacer below TodoList: ${item.todoList.title}"
                            )
                        }
                    }
                }
            }
        }

        OptionsPopup(
            expanded = selectedTodoList != null,
            onDismissRequest = { viewModel.selectTodoList(null) },
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
            offset = IntOffset(popupOffset.value.x, popupOffset.value.y + 26),
            onTagsEdit = {
                viewModel.setTagEditState(selectedTodoList!!)
            }
        )

        selectedTodoList?.let {
            PopupManager(
                popupState = popupState,
                todoList = it,
                viewModel = viewModel,
                onNewListConfirm = { name ->
                    viewModel.addTodoList(name)
                    viewModel.setNoneState()
                },
                onRenameConfirm = { todoList, newName, selectedColor, selectedDate, updatedGifUrl  ->
                    viewModel.updateTodoList(
                        id = todoList.id,
                        title = newName,
                        textColor = selectedColor,
                        dueDate = selectedDate,
                        gifUrl = updatedGifUrl
                        // Handle selectedDate if applicable
                    )
                    viewModel.setNoneState()
                },
                onGifSelected = { todoList, gifUrl ->
                    viewModel.updateTodoList(id = todoList.id, gifUrl = gifUrl)
                    viewModel.setNoneState()
                },
                onColorSelected = { todoList, color ->
                    viewModel.updateTodoList(id = todoList.id, textColor = color)
                    viewModel.setNoneState()
                },
                onColorBackgroundSelected = { todoList, color ->
                    viewModel.updateTodoList(id = todoList.id, backgroundColor = color)
                    viewModel.updateTodoList(id = todoList.id, gifUrl = null)
                    viewModel.setNoneState()

                },
                onTagsEdited = { todoList, tags ->
                    viewModel.updateTodoList(id = todoList.id, tags = tags)
                    viewModel.setNoneState()
                },
                onDismiss = {
                    viewModel.setNoneState()
                }
            )
        }
    }
}


// Top app bar with search and settings icons and dropdown menu
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopAppBarWithMenu(showLiked: MutableState<Boolean>, viewModel: TodoListViewModel) {
    val context = LocalContext.current
    val textFieldVisible = remember { mutableStateOf(false) }

    val searchQuery by viewModel.searchQuery.collectAsState()

    // Focus the search field when it becomes visible
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(textFieldVisible.value) {
        if (textFieldVisible.value) {
            focusRequester.requestFocus()
        }
    }

    TopAppBar(
        title = {
            AnimatedVisibility(
                visible = textFieldVisible.value,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                val textFieldHeight = 48.dp
                BasicTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.onSearchQueryChange(it) },
                    textStyle = TextStyle(fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface),
                    singleLine = true,
                    modifier = Modifier
                        .focusRequester(focusRequester)
                        .fillMaxWidth()
                        .heightIn(min = textFieldHeight)
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.secondary,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .background(
                            color = MaterialTheme.colorScheme.onPrimary,
                            shape = RoundedCornerShape(12.dp)
                        ),
                    decorationBox = { innerTextField ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = textFieldHeight)
                                .padding(vertical = 12.dp, horizontal = 16.dp)
                        ) {
                            // Input field
                            Box(Modifier.weight(1f)) {
                                if (searchQuery.isEmpty()) {
                                    Text(
                                        text = "Search...",
                                        style = TextStyle(fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
                                    )
                                }
                                innerTextField()
                            }
                            // Clear button
                            if (searchQuery.isNotEmpty()) {
                                Box(modifier = Modifier
                                    .clickable { viewModel.onSearchQueryChange("") }
                            ) {
                                    Icon(
                                        imageVector = Icons.Filled.Close,
                                        contentDescription = "Clear",
                                        tint = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }
                )
            }
        },
        modifier = Modifier
            .fillMaxWidth(),
        navigationIcon = {
            IconButton(onClick = {
                textFieldVisible.value = !textFieldVisible.value
            }) {
                Icon(Icons.Filled.Search, contentDescription = "Search", tint = MaterialTheme.colorScheme.onPrimary)
            }
        },
        actions = {
            SettingsButton(
                context = context,
                showLiked = showLiked,
                onDeleteAllConfirmed = { viewModel.deleteAllTodoLists() },
                color = MaterialTheme.colorScheme.onPrimary
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFF0D47A1),
        ),
        scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    )
}