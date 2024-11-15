package com.group10.uxuiapp.view

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MenuDefaults.containerColor
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.key
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.group10.uxuiapp.R
import kotlinx.coroutines.Job
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.group10.uxuiapp.view_model.ListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskPage(taskId: String, onNavigateBack: () -> Unit, viewModel: ListViewModel) {
    val context = LocalContext.current
    var n: Int = 1
    val task = viewModel.lists.value.find { it.index.toString() == taskId }
    if(task == null) {
        Toast.makeText(context, "Task not found", Toast.LENGTH_SHORT).show()
        onNavigateBack()
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(task?.title ?: "ERROR LOADING TITLE") },  // Display the task ID in the title
                modifier = Modifier.padding(8.dp),
                navigationIcon = {
                    IconButton(onClick = {
                        onNavigateBack()  // Trigger back navigation
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.arrow_left),
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        Toast.makeText(context, "Search clicked", Toast.LENGTH_SHORT).show()
                    }) {
                        Icon(Icons.Filled.Search, contentDescription = "Search")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(),
                scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
            )
        }
    ) { innerPadding ->
        LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = innerPadding) {

            items(n) { index ->
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                ) {
                    var isChecked by remember { mutableStateOf(false) }

                    Checkbox(
                        checked = isChecked,
                        onCheckedChange = {isChecked = it},
                        modifier = Modifier.padding(end = 5.dp)
                        )
                    // Remember state for each TextField
                    val text = remember { mutableStateOf("") }
                    TextField(
                        value = text.value,
                        onValueChange = { newText -> text.value = newText },
                        modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth()
                    )
                }
            }
        }
    }
}
