package com.group10.uxuiapp.ui.todolist.view


import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.uxuiapplication.ChangeButton
import com.giphy.sdk.analytics.GiphyPingbacks.context
import com.group10.uxuiapp.data.data_class.TodoList
import com.group10.uxuiapp.ui.navigation.AppNavigator
import com.group10.uxuiapp.data.GiphyActivity
import com.group10.uxuiapp.data.data_class.TodoListWithTaskItem
import com.group10.uxuiapp.ui.todolist.view.components.ListNameInputDialog
import com.group10.uxuiapp.ui.todolist.view.components.SettingsButton
import com.group10.uxuiapp.ui.todolist.viewmodel.TodoListViewModel
import kotlinx.coroutines.flow.map
import kotlin.collections.find
import androidx.compose.foundation.lazy.items


// Main ListOverviewPage with Scaffold and LazyColumn
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoListScreen(viewModel: TodoListViewModel, appNavigator: AppNavigator) {
    val showDialog = remember { mutableStateOf(false) }
    val listNameState = remember { mutableStateOf("") }
    val context = LocalContext.current
    val query = remember { mutableStateOf("") }
    val changeButtonAnchor = remember { mutableStateOf<Offset?>(null) }
    val currentTaskList = remember { mutableStateOf<TodoList?>(null) }

    // Collect the lists from the ViewModel's Flow
    //val taskListsWithItems2 = remember(query.value) {
    // viewModel.filterLists(query.value)
    //}
    val taskListsWithItems by viewModel.lists.collectAsState(emptyList())
    val selectedTodoList by viewModel.selectedTodoList.collectAsState()


    // Use LaunchedEffect to reset selectedIndex if list size changes
    LaunchedEffect(taskListsWithItems) {
        if (selectedTodoList != null &&
            taskListsWithItems.none { it.todoList == selectedTodoList }
        ) {
            viewModel.selectTodoList(null)
        }
    }




    val filteredLists = taskListsWithItems.filter {
        it.todoList.title.contains(query.value, ignoreCase = true)
    }



    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = { TopAppBarWithMenu(query) },
            floatingActionButton = {
                AddNewListButton {
                    showDialog.value = true
                }
            }
        ) { innerPadding  ->

            LazyColumn (contentPadding = PaddingValues(
                start = innerPadding.calculateStartPadding(LocalLayoutDirection.current),
                top = innerPadding.calculateTopPadding(),
                end = innerPadding.calculateEndPadding(LocalLayoutDirection.current),
                bottom = innerPadding.calculateBottomPadding()
            )){
                items(
                    items = taskListsWithItems,
                    key = { it.todoList.id }
                ) { taskListWithItems ->

                    // 2) Determine if this list item is selected
                    val isSelected = (selectedTodoList?.id == taskListWithItems.todoList.id)

                    ListItem(
                        todoList = taskListWithItems.todoList,
                        isSelected = isSelected,                 // <-- NEW
                        onPositionChange = { offset, todoList ->
                            // This callback is triggered by a long-press
                            changeButtonAnchor.value = offset
                            currentTaskList.value = todoList
                            // Mark this item as selected in the ViewModel
                            viewModel.selectTodoList(todoList)
                        },
                        viewModel = viewModel,
                        appNavigator = appNavigator,
                        taskListsWithItems = taskListsWithItems
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
                        val intent = Intent(context, GiphyActivity::class.java).apply {
                            // If we have a selected list, drop in its ID
                            selectedTodoList?.let { todoList ->
                                putExtra("todoListId", todoList.id)
                            }
                        }
                        context.startActivity(intent)
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

        if(showDialog.value && currentTaskList.value != null){
            ListNameInputDialog(
                onDismiss = { showDialog.value = false },
                onConfirm = { newName ->
                    currentTaskList.value?.let { taskList ->
                        viewModel.updateTodoList(taskList, newName)
                    }
                    showDialog.value = false
                    Toast.makeText(context, "List '$newName' created", Toast.LENGTH_SHORT).show()
                    viewModel.selectTodoList(null)
                    currentTaskList.value = null
                }
            )
        }
        else if (showDialog.value){
            ListNameInputDialog(
                onDismiss = { showDialog.value = false },
                onConfirm = { name ->
                    if (name.isNotBlank()) {
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
    val backgroundModifier = if (!todoList.gifUrl.isNullOrEmpty()) {

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
                .then(backgroundModifier)
                // Apply dynamic background
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
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = todoList.title,
                    color = MaterialTheme.colorScheme.background,
                    modifier = Modifier.width(320.dp)
                )
                LikedButton(todoList, viewModel)
            }
        }
    }

    if (isSelected) {
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun LikedButton(todoList: TodoList, viewModel: TodoListViewModel) {
    val isLiked = todoList.isLiked

    Icon(
        imageVector = if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
        contentDescription = if (isLiked) "Liked" else "Add Favorite",
        modifier = Modifier
            .size(25.dp)
            .clickable {
                viewModel.updateTodoList(todoList = todoList, isLiked = !isLiked)
            },
        tint = if (isLiked) Color.Red else Color.White
    )
}



// Floating Action Button composable for adding a new list item
@Composable
private fun AddNewListButton(onClick: () -> Unit) {
    FloatingActionButton(
        onClick = onClick,
        containerColor = Color(0xFF4658FF),
        contentColor = Color.White,
        shape = RoundedCornerShape(32.dp),
        modifier = Modifier
            .padding(16.dp)
            .width(135.dp)
            .height(60.dp)
            .offset(x = 15.dp, y = (-10).dp)
    ) {
        // Row to position icon and text horizontally
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add Item",
                modifier = Modifier.size(30.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "NEW LIST",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}


