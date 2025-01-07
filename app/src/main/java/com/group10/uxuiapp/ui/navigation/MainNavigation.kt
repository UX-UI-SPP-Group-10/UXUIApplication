package com.group10.uxuiapp.ui.navigation


import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController

import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.viewModelFactory

@Composable
fun MainNavigation(todoListViewModelFactory: ViewModelProvider.Factory, taskListViewModelFactory: ViewModelProvider.Factory) {
    val navController = rememberNavController()

    NavigationGraph(
        navController = navController,
        taskListViewModel = taskListViewModelFactory,
        todoListViewModelFactory = todoListViewModelFactory
    )
}