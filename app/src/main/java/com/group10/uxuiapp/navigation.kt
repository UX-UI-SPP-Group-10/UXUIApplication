package com.group10.uxuiapp

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.group10.uxuiapp.view.ListOverviewPage
import com.group10.uxuiapp.view.TaskPage
import com.group10.uxuiapp.view_model.ListViewModel

@Composable
fun MainNavigation() {
    val navController = rememberNavController()
    val viewModel: ListViewModel = viewModel()

    // Use your Screen sealed class to set up the NavHost
    NavHost(navController = navController, startDestination = Screen.ListOverview.route) {
        composable(Screen.ListOverview.route) {
            ListOverviewPage(viewModel = viewModel, navigateTo = { route ->
                navController.navigate(route)
            })
        }
        composable(Screen.TaskList.route) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getString("taskId") ?: "Unknown"
            TaskPage(
                taskId = taskId,
                onNavigateBack = { navController.popBackStack() },
                viewModel = viewModel
            )
        }
    }
}

sealed class Screen(val route: String) {
    object ListOverview : Screen("ListOverview")
    object TaskList : Screen("taskList/{taskId}")
}
