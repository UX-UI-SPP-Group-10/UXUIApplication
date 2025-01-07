package com.group10.uxuiapp.ui.navigation

sealed class Screen(val route: String) {
    object TodoList : Screen("todoList")
    object Tasks : Screen("tasks/{todoListId}") {
        fun createRoute(todoListId: Int) = "tasks/$todoListId"
    }
}
