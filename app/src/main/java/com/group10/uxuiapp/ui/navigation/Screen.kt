package com.group10.uxuiapp.ui.navigation

sealed class Screen(val route: String) {
    object TodoList : Screen("TodoList")
    object Tasks : Screen("tasks/{taskId}") {
        fun createRoute(taskId: Int): String = "tasks/$taskId"
    }
}
