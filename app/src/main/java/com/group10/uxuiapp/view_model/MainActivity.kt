package com.group10.uxuiapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.group10.uxuiapp.data.DatabaseProvider
import com.group10.uxuiapp.data.TaskRepository
import com.group10.uxuiapp.navigation.navigation.MainNavigation
import com.group10.uxuiapp.ui.theme.UXUIApplicationTheme
import com.group10.uxuiapp.view_model.ListViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Create the database and repository
        val database = DatabaseProvider.getDatabase(this)
        val taskRepository = TaskRepository(database.taskDao())

        setContent {
            UXUIApplicationTheme {
                // Pass the ViewModelFactory to the MainNavigation
                MainNavigation(viewModelFactory = ListViewModelFactory(taskRepository))
            }
        }
    }
}