package com.group10.uxuiapp.view


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
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.room.Query
import com.example.uxuiapplication.ChangeButton
import com.group10.uxuiapp.data.TaskList
import com.group10.uxuiapp.view.component.ListNameInputDialog
import com.group10.uxuiapp.view.component.SettingsButton
import com.group10.uxuiapp.view_model.ListViewModel


// Main ListOverviewPage with Scaffold and LazyColumn
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListOverviewPage(navigateTo: (route: String) -> Unit, viewModel: ListViewModel) {
    val selectedIndex = remember { mutableStateOf<Int?>(null) }
    val showDialog = remember { mutableStateOf(false) }
    val listNameState = remember { mutableStateOf("") }
    val context = LocalContext.current
    val query = remember { mutableStateOf("") }

    // Collect the lists from the ViewModel's Flow
    //val taskListsWithItems2 = remember(query.value) {
       // viewModel.filterLists(query.value)
    //}
    val taskListsWithItems by viewModel.lists.collectAsState(emptyList())

    // Use LaunchedEffect to reset selectedIndex if list size changes
    LaunchedEffect(taskListsWithItems) {
        if (selectedIndex.value != null &&
            taskListsWithItems.none { it.taskList.id == selectedIndex.value }) {
            selectedIndex.value = null
        }
    }

    val filteredLists = taskListsWithItems.filter {
        it.taskList.title.contains(query.value, ignoreCase = true)
    }

    Scaffold(
        topBar = { TopAppBarWithMenu(query)},
        floatingActionButton = {
            AddNewListButton {
                showDialog.value = true // activate add list name popup
            }
        }
    ) { innerPadding ->
        LazyColumn(
            contentPadding = PaddingValues(
                start = innerPadding.calculateStartPadding(LocalLayoutDirection.current),
                top = innerPadding.calculateTopPadding(),
                end = innerPadding.calculateEndPadding(LocalLayoutDirection.current),
                bottom = innerPadding.calculateBottomPadding() + 50.dp
            )
        ) {
            val listsToShow = if (query.value.isNotEmpty()) filteredLists else taskListsWithItems

            items(listsToShow, key = { it.taskList.id }) { taskListWithItems ->
                ListItem(
                    taskList = taskListWithItems.taskList,
                    navigateTo = navigateTo,
                    selectedIndex = selectedIndex,
                    viewModel = viewModel
                )
            }
        }
    }

    // Show the ListNamePopup when the button is clicked
    if (showDialog.value) {
        ListNameInputDialog(
            onDismiss = { showDialog.value = false },
            onConfirm = { name ->
                if (name.isNotBlank()) {
                    viewModel.addList(name)
                    listNameState.value = name
                    showDialog.value = false
                    Toast.makeText(context, "List '$name' created", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Please enter a valid name", Toast.LENGTH_SHORT).show()
                }
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
            SettingsButton(context = context)
        },
        colors = TopAppBarDefaults.topAppBarColors(),
        scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    )
}
@Composable
private fun ListItem(
    taskList: TaskList,
    navigateTo: (String) -> Unit,
    selectedIndex: MutableState<Int?>,
    viewModel: ListViewModel
) {
    val context = LocalContext.current
    val listNameState = remember { mutableStateOf(taskList.title) }
    val showDialog = remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary,               // Top color
                            MaterialTheme.colorScheme.secondary,
                            Color(0xFFC0DCEF)
                        ),
                        start = Offset(0f, 0f), // Start at the top
                        end = Offset(0f, Float.POSITIVE_INFINITY) // End at the bottom
                    )
                )
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {
                            val refreshedTaskList = viewModel.lists.value.find { it.taskList.id == taskList.id }
                            if (refreshedTaskList != null) {
                                navigateTo("taskList/${refreshedTaskList.taskList.id}")
                            } else {
                                Log.e("ListItem", "Attempted to navigate to a deleted or invalid list.")
                            }
                        },
                        onLongPress = {
                            selectedIndex.value = taskList.id
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
                    text = taskList.title,
                    color = MaterialTheme.colorScheme.background,
                    modifier = Modifier.width(320.dp)
                )
                LikedButton(taskList, viewModel)
            }
        }

        if (selectedIndex.value == taskList.id) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset(y = 24.dp)
            ) {
                ChangeButton(
                    onClose = {
                        selectedIndex.value = null
                    },
                    onDelete = {
                        viewModel.removeList(taskList)
                        selectedIndex.value = null // Reset index after deletion
                    },
                    onOpdate = { showDialog.value = true // activate add list name popup
                    }
                )
            }
        }
    }
    if(showDialog.value){
        ListNameInputDialog(
            onDismiss = { showDialog.value = false },
            onConfirm = { name ->
                viewModel.updateTitle(taskList, name)
                listNameState.value = name
                showDialog.value = false
                Toast.makeText(context, "List '$name' created", Toast.LENGTH_SHORT).show()
                selectedIndex.value = null
            }
        )
    }

    if (selectedIndex.value == taskList.id) {
        Spacer(modifier = Modifier.height(24.dp))
    }
}




@Composable
private fun LikedButton(taskList: TaskList, viewModel: ListViewModel) {
    val isLiked = taskList.isLiked

    Icon(
        imageVector = if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
        contentDescription = if (isLiked) "Liked" else "Add Favorite",
        modifier = Modifier
            .size(25.dp)
            .clickable {
                viewModel.toggleLikedStatus(taskList) // Update the global state as well
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
            .offset(x = 22.dp, y = (-18).dp)
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


