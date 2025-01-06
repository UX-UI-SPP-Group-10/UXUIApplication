package com.group10.uxuiapp.ui.navigation


import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController

import androidx.compose.runtime.remember

@Composable
fun MainNavigation(viewModelFactory: ViewModelProvider.Factory) {
    val navController = rememberNavController()
    val appNavigator = remember { AppNavigator(navController) }

    NavigationGraph(
        navController = navController,
        viewModelFactory = viewModelFactory
    )
}