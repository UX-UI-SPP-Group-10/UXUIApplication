package com.group10.uxuiapp

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.group10.uxuiapp.view.ListOverviewPage
import com.group10.uxuiapp.view.TaskPage

@Composable
fun MainNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "ListOverview") {
        composable("ListOverview") {
            ListOverviewPage(navController = navController)
        }
        composable("taskList/{taskId}") { backStackEntry ->
            val taskId = backStackEntry.arguments?.getString("taskId") ?: "Unknown"
            TaskPage(navController = navController, taskId = taskId, onNavigateBack = { navController.popBackStack() })
        }

    }
}

fun navigateToTask(navController: NavController, taskId: String) {
    navController.navigate("taskList/$taskId")
}
