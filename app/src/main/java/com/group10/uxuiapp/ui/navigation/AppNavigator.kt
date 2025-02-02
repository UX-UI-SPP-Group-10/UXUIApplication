package com.group10.uxuiapp.ui.navigation
import androidx.navigation.NavController

class AppNavigator(private val navController: NavController) {

    fun navigateToListOverview() {
        navController.navigate(Screen.TodoListScreen.route)
    }

    fun navigateToTask(todoID: Int) {
        navController.navigate(Screen.TasksScreen.createRoute(todoID))
    }

    fun popBackStack() {
        navController.popBackStack()
    }
}