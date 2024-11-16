package com.group10.uxuiapp

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.group10.uxuiapp.view.ListOverviewPage
import com.group10.uxuiapp.view.TaskPage
import com.group10.uxuiapp.view_model.ListViewModel

@OptIn(ExperimentalAnimationApi::class)  // Enable experimental animation APIs
@Composable
fun MainNavigation() {
    val navController = rememberNavController()
    val viewModel: ListViewModel = viewModel()

    // Set up the NavHost with screen transitions
    NavHost(
        navController = navController,
        startDestination = Screen.ListOverview.route,
        enterTransition = { slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(700)) },
        exitTransition = { slideOutHorizontally(targetOffsetX = { -it }, animationSpec = tween(700)) },
        popEnterTransition = { slideInHorizontally(initialOffsetX = { -it }, animationSpec = tween(700)) },
        popExitTransition = { slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(700)) }
    ) {
        // ListOverview screen composable
        composable(Screen.ListOverview.route) {
            ListOverviewPage(viewModel = viewModel, navigateTo = { route ->
                navController.navigate(route)
            })
        }

        // TaskList screen composable with taskId as an argument
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
