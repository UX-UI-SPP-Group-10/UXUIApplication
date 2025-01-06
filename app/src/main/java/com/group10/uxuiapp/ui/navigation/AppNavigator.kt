package com.group10.uxuiapp.ui.navigation
import androidx.navigation.NavController

class AppNavigator(private val navController: NavController) {

    fun navigateToListOverview() {
        navController.navigate(Screen.TodoList.route)
    }

    fun navigateToTask(taskId: Int) {
        navController.navigate(Screen.Tasks.createRoute(taskId))
    }

    fun popBackStack() {
        navController.popBackStack()
    }
}