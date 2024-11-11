package com.group10.uxuiapp.view

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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListOverviewPage(navController: NavController) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var selectedIndex by remember { mutableStateOf<Int?>(null) }
    val expanded = remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("List Overview") },
                modifier = Modifier.padding(8.dp),
                actions = {
                    IconButton(onClick = {
                        Toast.makeText(context, "Search clicked", Toast.LENGTH_SHORT).show()
                    }) {
                        Icon(Icons.Filled.Search, contentDescription = "Search")
                    }
                    IconButton(onClick =  {
                        expanded.value = true
                    }){
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
    ) { innerPadding ->
        LazyColumn(contentPadding = innerPadding) {
            items(10) { index ->
                Box(Modifier.fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(100.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colorScheme.secondary)
                    .clickable {
                    navController.navigate("taskList/$index") }
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onLongPress = {
                                selectedIndex = index
                            }
                        )
                    }) {
                    Text(
                        text = "list $index",
                        modifier = Modifier
                            .padding(16.dp)
                    )
                        // Show ChangeButton if this item is selected
                        if (selectedIndex == index) {
                            ChangeButton(onClose = { selectedIndex = null })
                        }
                }
              Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}
