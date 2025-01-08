package com.group10.uxuiapp.ui.todolist.view

import GiphyDialog
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.uxuiapplication.ChangeButton
import com.group10.uxuiapp.ui.navigation.AppNavigator
import com.group10.uxuiapp.ui.todolist.view.components.*
import com.group10.uxuiapp.ui.todolist.viewmodel.TodoListViewModel

// Main TodoListScreen with Scaffold and LazyColumn
@Composable
fun TodoListScreen(viewModel: TodoListViewModel, appNavigator: AppNavigator) {
    val showDialog = remember { mutableStateOf(false) }
    val showGiphyDialog = remember { mutableStateOf(false) } // Add this state
    val listNameState = remember { mutableStateOf("") }
    val context = LocalContext.current
    val query = remember { mutableStateOf("") }
    val changeButtonAnchor = remember { mutableStateOf<Offset?>(null) }

    // Collect the lists from the ViewModel's Flow
    val todoListsWithItems by viewModel.lists.collectAsState(emptyList())
    val selectedTodoList by viewModel.selectedTodoList.collectAsState()

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
                    showDialog.value = true
                }
            }
        ) { innerPadding  ->

            LazyColumn (
                contentPadding = PaddingValues(
                start = innerPadding.calculateStartPadding(LocalLayoutDirection.current),
                top = innerPadding.calculateTopPadding(),
                end = innerPadding.calculateEndPadding(LocalLayoutDirection.current),
                bottom = innerPadding.calculateBottomPadding()
            )){
                items(
                    items = listsToShow,
                    key = { it.todoList.id }
                ) { taskListWithItems ->
                    val isSelected = (selectedTodoList?.id == taskListWithItems.todoList.id)

                    TodoListCard(
                        todoList = taskListWithItems.todoList,
                        isSelected = isSelected,
                        onPositionChange = { offset, todoList ->
                            changeButtonAnchor.value = offset
                            viewModel.selectTodoList(todoList)
                        },
                        viewModel = viewModel,
                        appNavigator = appNavigator,
                        taskListsWithItems = todoListsWithItems
                    )
                }
            }
        }

        // Render ChangeButton
        if (selectedTodoList != null && changeButtonAnchor.value != null) {
            Box(
                modifier = Modifier
                    .offset(
                        x = changeButtonAnchor.value!!.x.dp,
                        y = 24.dp + changeButtonAnchor.value!!.y.dp
                    )
                    .zIndex(1f) // Ensure ChangeButton is on top
            ) {
                ChangeButton(
                    onClose = {
                        viewModel.selectTodoList(null)
                    },
                    onDelete = {
                        selectedTodoList?.let { todoList ->
                            viewModel.removeTodoList(todoList)
                        }
                        viewModel.selectTodoList(null)
                    },
                    onOpdate = { showDialog.value = true },
                    onGifSelect = {
                        showGiphyDialog.value = true
                    }
                )
            }
        }

        // Full-screen overlay to detect taps
        if (selectedTodoList != null) {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f))
                    .pointerInput(Unit) {
                        detectTapGestures(onTap = { viewModel.selectTodoList(null) })
                    }
            )
        }

        if(showDialog.value && selectedTodoList != null) {
            Log.d("TodoListScreen", "showDialog.value && currentTaskList.value != null")
            ListNameInputDialog(
                onDismiss = { showDialog.value = false },
                onConfirm = { newName ->
                    selectedTodoList?.let { taskList ->
                        viewModel.updateTodoList(taskList, newName)
                    }
                    showDialog.value = false
                    Toast.makeText(context, "List '$newName' created", Toast.LENGTH_SHORT).show()
                    viewModel.selectTodoList(null)
                }
            )
        }
        else if (showDialog.value){
            Log.d("TodoListScreen", "showDialog.value")
            ListNameInputDialog(
                onDismiss = { showDialog.value = false },
                onConfirm = { name ->
                    if (true) {  // Can add a check for valid name here
                        viewModel.addTodoList(name)
                        listNameState.value = name
                        showDialog.value = false
                        Toast.makeText(context, "List '$name' created", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Please enter a valid name", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            )
        }
        // Show GiphyDialog when needed
        if (showGiphyDialog.value) {
            GiphyDialog(
                context = context,
                onGifSelected = { gifUrl ->
                    // Update the GIF URL in ViewModel
                    selectedTodoList?.let { todoList ->
                        viewModel.updateGifUrl(todoList.id, gifUrl)
                    }
                    showGiphyDialog.value = false
                },
                onDismissed = {
                    showGiphyDialog.value = false
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