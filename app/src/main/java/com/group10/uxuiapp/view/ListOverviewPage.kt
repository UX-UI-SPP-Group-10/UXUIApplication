package com.group10.uxuiapp.view

import android.R.attr.font
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp

// Main ListOverviewPage with Scaffold and LazyColumn
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListOverviewPage(navController: NavController) {
    val selectedIndex = remember { mutableStateOf<Int?>(null) }

    Scaffold(
        topBar = { TopAppBarWithMenu() },
        floatingActionButton = {
            AddNewListButton {
                navController.navigate("taskList/new")
            }
        }
    ) { innerPadding ->
        LazyColumn(contentPadding = innerPadding) {
            items(10) { index ->
                ListItem(
                    index = index,
                    navController = navController,
                    selectedIndex = selectedIndex
                )
            }
        }
    }
}

// Top app bar with search and settings icons and dropdown menu
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopAppBarWithMenu() {
    val context = LocalContext.current
    val expanded = remember { mutableStateOf(false) }

    TopAppBar(
        title = { Text("List Overview") },
        modifier = Modifier.padding(8.dp),
        actions = {
            IconButton(onClick = {
                Toast.makeText(context, "Search clicked", Toast.LENGTH_SHORT).show()
            }) {
                Icon(Icons.Filled.Search, contentDescription = "Search")
            }
            IconButton(onClick =  { expanded.value = true }) {
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
    navController: NavController,
    selectedIndex: MutableState<Int?>
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(100.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.secondary)
            .clickable {
                navController.navigate("taskList/$index")
            }
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        navController.navigate("taskList/$index")
                    },
                    onLongPress = {
                        selectedIndex.value = index
                    }
                )
            }
    ) {
        Text(
            text = "list $index",
            modifier = Modifier.padding(16.dp)
        )
        // Show ChangeButton if this item is selected
        if (selectedIndex.value == index) {
            ChangeButton(onClose = { selectedIndex.value = null })
        }
    }
    Spacer(modifier = Modifier.height(8.dp))
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


@Preview (showBackground = true)
@Composable
fun ListOverviewPagePreview() {
    ListOverviewPage(navController = NavController(LocalContext.current))
}