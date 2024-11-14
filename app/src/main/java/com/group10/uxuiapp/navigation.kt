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

    NavHost(navController = navController, startDestination = "ListOverview") {
        composable("ListOverview") {
            ListOverviewPage(navController = navController, viewModel = viewModel)
        }
        composable("taskList/{taskId}") { backStackEntry ->
            val taskId = backStackEntry.arguments?.getString("taskId") ?: "Unknown"
            TaskPage(navController = navController, taskId = taskId, onNavigateBack = { navController.popBackStack() }, viewModel = viewModel)
        }

    }
}

fun navigateToTask(navController: NavController, taskId: String) {
    navController.navigate("taskList/$taskId")
}
