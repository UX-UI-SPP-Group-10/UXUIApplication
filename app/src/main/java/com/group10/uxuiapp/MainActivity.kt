package com.group10.uxuiapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.group10.uxuiapp.data.DatabaseProvider
import com.group10.uxuiapp.data.TaskDataSource
import com.group10.uxuiapp.ui.navigation.MainNavigation
import com.group10.uxuiapp.ui.tasks.viewmodel.TaskListViewModelFactory
import com.group10.uxuiapp.ui.theme.UXUIApplicationTheme
import com.group10.uxuiapp.ui.todolist.viewmodel.TodoListViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Create the database and repository
        val database = DatabaseProvider.getDatabase(this)
        val taskDataSource = TaskDataSource(database.taskDao())

        setContent {
            UXUIApplicationTheme {
                // Pass the ViewModelFactory to the MainNavigation
                MainNavigation(taskListViewModelFactory = TaskListViewModelFactory(taskDataSource), todoListViewModelFactory = TodoListViewModelFactory(taskDataSource))
            }
        }
    }
}