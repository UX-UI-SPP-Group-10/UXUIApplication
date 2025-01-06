package com.group10.uxuiapp.ui.navigation

sealed class Screen(val route: String) {
    object ListOverview : Screen("list_overview")
    object TaskList : Screen("task_list/{taskId}") {
        fun createRoute(taskId: Int): String = "task_list/$taskId"
    }
}
