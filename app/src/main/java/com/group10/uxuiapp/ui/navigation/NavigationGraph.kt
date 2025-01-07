package com.group10.uxuiapp.ui.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.group10.uxuiapp.ui.todolist.view.TodoListScreen
import com.group10.uxuiapp.ui.tasks.view.TaskScreen
import com.group10.uxuiapp.ui.todolist.viewmodel.TodoListViewModel
import com.group10.uxuiapp.ui.todolist.viewmodel.TodoListViewModelFactory

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NavigationGraph(
    navController: NavHostController,
    taskListViewModel: ViewModelProvider.Factory,
    todoListViewModelFactory: ViewModelProvider.Factory
) {
    // Create AppNavigator
    val appNavigator = AppNavigator(navController)

    // ViewModel instance
    val todoListViewModel: TodoListViewModel = viewModel(factory = todoListViewModelFactory)
    val taskListViewModel: TaskListViewModel = viewModel(factory = taskListViewModel)

    NavHost(
        navController = navController,
        startDestination = Screen.TodoList.route
    ) {
        // TodoListScreen
        composable(route = Screen.TodoList.route) {
            TodoListScreen(
                viewModel = todoListViewModel,
                appNavigator = appNavigator
            )
        }

        // TaskScreen
        composable(
            route = Screen.Tasks.route,
            enterTransition = {
                scaleIn(
                    initialScale = 0.8f,
                    animationSpec = tween(500, easing = FastOutSlowInEasing)
                ) + fadeIn(animationSpec = tween(300))
            },
            exitTransition = {
                scaleOut(
                    targetScale = 0.8f,
                    animationSpec = tween(500, easing = FastOutSlowInEasing)
                ) + fadeOut(animationSpec = tween(300))
            },
            popEnterTransition = {
                scaleIn(
                    initialScale = 0.8f,
                    animationSpec = tween(500, easing = FastOutSlowInEasing)
                ) + fadeIn(animationSpec = tween(300))
            },
            popExitTransition = {
                scaleOut(
                    targetScale = 0.5f,
                    animationSpec = tween(700, easing = FastOutSlowInEasing)
                ) + fadeOut(animationSpec = tween(300))
            }
        ) { backStackEntry ->
            val todoListId = backStackEntry.arguments?.getString("todoListId")?.toIntOrNull() ?: -1
            TaskScreen(
                todoListId = todoListId,
                appNavigator = appNavigator,
                viewModel = taskListViewModel
            )
        }
    }
}
