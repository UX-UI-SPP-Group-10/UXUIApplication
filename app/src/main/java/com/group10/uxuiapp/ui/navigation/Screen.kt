package com.group10.uxuiapp.ui.navigation

sealed class Screen(val route: String) {
    object TodoListScreen : Screen("todoList")
    object TasksScreen : Screen("tasks/{todoListId}") {
        fun createRoute(todoListId: Int) = "tasks/$todoListId"
    }
}
