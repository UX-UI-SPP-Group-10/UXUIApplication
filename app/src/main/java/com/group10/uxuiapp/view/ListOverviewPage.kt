package com.group10.uxuiapp.view

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.group10.uxuiapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListOverviewPage(navController: NavController) {
    val context = LocalContext.current

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
                },
                colors = TopAppBarDefaults.topAppBarColors(),
                scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
            )
        }
    ) { innerPadding ->
        LazyColumn(contentPadding = innerPadding) {
            items(100) { index ->
                Text(
                    text = "Item $index",
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable {
                            navController.navigate("taskList/$index")
                        }
                )
            }
        }
    }
}
