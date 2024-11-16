package com.group10.uxuiapp

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
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

    // Set up the NavHost with scale and fade transitions
    NavHost(
        navController = navController,
        startDestination = Screen.ListOverview.route
    ) {
        // ListOverview screen composable with scale and fade transitions
        composable(
            route = Screen.ListOverview.route,
            // Remove popEnterTransition and popExitTransition to prevent animation when navigating back
        ) {
            ListOverviewPage(viewModel = viewModel, navigateTo = { route -> navController.navigate(route) })
        }

        // TaskList screen composable with taskId as an argument, using scale and fade transitions
        composable(
            route = Screen.TaskList.route,
            enterTransition = {
                scaleIn(initialScale = 0.8f, animationSpec = tween(500, easing = FastOutSlowInEasing)) + fadeIn(animationSpec = tween(300))
            },
            exitTransition = {
                scaleOut(targetScale = 0.8f, animationSpec = tween(500, easing = FastOutSlowInEasing)) + fadeOut(animationSpec = tween(300))
            },
            popEnterTransition = {
                scaleIn(initialScale = 0.8f, animationSpec = tween(500, easing = FastOutSlowInEasing)) + fadeIn(animationSpec = tween(300))
            },
            popExitTransition = {
                scaleOut(targetScale = 0.5f, animationSpec = tween(700, easing = FastOutSlowInEasing)) + fadeOut(animationSpec = tween(300))
            }
        ) { backStackEntry ->
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
