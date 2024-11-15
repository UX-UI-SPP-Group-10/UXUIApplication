package com.group10.uxuiapp.view


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.uxuiapplication.ChangeButton
import com.group10.uxuiapp.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.group10.uxuiapp.view.component.ListNameInputDialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.group10.uxuiapp.view_model.ListViewModel
import android.util.Log
import android.widget.Toast
import com.group10.uxuiapp.data.TaskList

// Main ListOverviewPage with Scaffold and LazyColumn
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListOverviewPage(navigateTo: (route: String) -> Unit, viewModel: ListViewModel) {
    val selectedIndex = remember { mutableStateOf<Int?>(null) }
    val showDialog = remember { mutableStateOf(false) }
    val listNameState = remember { mutableStateOf("") }
    val context = LocalContext.current
    val expanded = remember { mutableStateOf(false) }

    // Debugging statement for selectedIndex and list size
    Log.d("ListOverviewPage", "Initial selectedIndex: ${selectedIndex.value}, list size: ${viewModel.lists.value.size}")

    // Use LaunchedEffect to reset selectedIndex if list size changes
    LaunchedEffect(viewModel.lists.value.size) {
        if (selectedIndex.value != null && selectedIndex.value!! >= viewModel.lists.value.size) {
            Log.d("ListOverviewPage", "Resetting selectedIndex because it's out of bounds")
            selectedIndex.value = null
        }
    }

    Scaffold(
        topBar = { TopAppBarWithMenu() },
        floatingActionButton = {
            AddNewListButton {
                showDialog.value = true // activate add list name popup
            }
        }
    ) { innerPadding ->
        LazyColumn(contentPadding = innerPadding) {
            items(viewModel.lists.value) { taskList ->
                ListItem(
                    index = taskList.index,
                    title = taskList.title,
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
private fun TopAppBarWithMenu() {
    val context = LocalContext.current
    val expanded = remember { mutableStateOf(false) }

    TopAppBar(
        title = {},
        modifier = Modifier.padding(8.dp),
        navigationIcon = {
            IconButton(onClick = {
                Toast.makeText(context, "Search clicked", Toast.LENGTH_SHORT).show()
            }) {
                Icon(Icons.Filled.Search, contentDescription = "Search")
            }
        },
        actions = {
            IconButton(onClick = { expanded.value = true }) {
                Icon(Icons.Filled.Settings, contentDescription = "Settings")
            }
            DropdownMenu(
                expanded = expanded.value,
                onDismissRequest = { expanded.value = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Option 1") },
                    onClick = {
                        expanded.value = false
                        Toast.makeText(context, "Option 1 clicked", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                DropdownMenuItem(
                    text = { Text("Option 2") },
                    onClick = {
                        expanded.value = false
                        Toast.makeText(context, "Option 2 clicked", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                DropdownMenuItem(
                    text = { Text("Option 3") },
                    onClick = {
                        expanded.value = false
                        Toast.makeText(context, "Option 3 clicked", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(),
        scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    )
}

// ListItem composable for each item in the list
@Composable
private fun ListItem(
    index: Int,
    title: String,
    navigateTo: (String) -> Unit,
    selectedIndex: MutableState<Int?>,
    viewModel: ListViewModel
) {
    val isLiked = remember { mutableStateOf(false) }

    // Wrapper Box for list item
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
                .background(MaterialTheme.colorScheme.secondary)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {
                            navigateTo("taskList/$index")
                        },
                        onLongPress = {
                            selectedIndex.value = index // Set selectedIndex on long press
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
                    text = title,
                    color = Color.White
                )

                Icon(
                    imageVector = if (isLiked.value) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = if (isLiked.value) "Liked" else "Add Favorite",
                    modifier = Modifier
                        .size(25.dp)
                        .clickable {
                            isLiked.value = !isLiked.value
                        },
                    tint = if (isLiked.value) Color.Red else Color.White
                )
            }
        }

        // Show ChangeButton when the item is selected
        if (selectedIndex.value == index) {
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
                        viewModel.removeList(index)
                        selectedIndex.value = null
                    }
                )
            }
        }
    }

    // Spacer to push next list item down when selected
    if (selectedIndex.value == index) {
        Spacer(modifier = Modifier.height(24.dp))
    }
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

// just a previerw
@Preview(showBackground = true)
@Composable
fun ListOverviewPagePreview() {
    // Create and set up the ViewModel inline with mock data
    val viewModel = ListViewModel().apply {
        lists.value = listOf(
            TaskList(index = 0, title = "List 1"),
            TaskList(index = 1, title = "List 2"),
            TaskList(index = 2, title = "List 3"),
        )
    }

    ListOverviewPage(navigateTo = {}, viewModel = viewModel)
}


