package com.group10.uxuiapp.domain

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import com.group10.uxuiapp.data.TaskItem
import com.group10.uxuiapp.data.TaskList
import com.group10.uxuiapp.data.TaskListWithItems

class ListManager {
    private val _allLists = mutableStateListOf<TaskListWithItems>()

    // Get the lists
    fun getLists(): List<TaskListWithItems> = _allLists

    // Add a new list
    fun addList(title: String) {
        val newList = TaskList(title = title)
        val newTaskListWithItems = TaskListWithItems(
            taskList = newList,
            taskItems = emptyList() // Initially, no tasks in the list
        )
        _allLists.add(newTaskListWithItems)
    }

    // Add a task to a list
    fun addTaskToList(taskListId: Int, newTask: TaskItem) {
        val listIndex = _allLists.indexOfFirst { it.taskList.id == taskListId }
        if (listIndex == -1) {
            Log.e("ListManager", "TaskList with id $taskListId not found.")
            return
        }
        val listWithItems = _allLists[listIndex]
        val updatedTaskItems = listWithItems.taskItems.toMutableList()
        updatedTaskItems.add(newTask)

        _allLists[listIndex] = listWithItems.copy(taskItems = updatedTaskItems)
    }

    // Remove a list by its ID
    fun removeList(taskListId: Int) {
        val listIndex = _allLists.indexOfFirst { it.taskList.id == taskListId }
        if (listIndex == -1) {
            Log.e("ListManager", "TaskList with id $taskListId not found.")
            return
        }
        _allLists.removeAt(listIndex)
    }

    // Update list title
    fun updateTitle(taskListId: Int, title: String) {
        val listIndex = _allLists.indexOfFirst { it.taskList.id == taskListId }
        if (listIndex == -1) {
            Log.e("ListManager", "TaskList with id $taskListId not found.")
            return
        }
        val listWithItems = _allLists[listIndex]
        val updatedTaskList = listWithItems.taskList.copy(title = title)
        _allLists[listIndex] = listWithItems.copy(taskList = updatedTaskList)
    }

    // Toggle liked status of a list
    fun toggleLikedStatus(taskListId: Int) {
        val listIndex = _allLists.indexOfFirst { it.taskList.id == taskListId }
        if (listIndex == -1) {
            Log.e("ListManager", "TaskList with id $taskListId not found.")
            return
        }
        val listWithItems = _allLists[listIndex]
        val updatedTaskList = listWithItems.taskList.copy(isLiked = !listWithItems.taskList.isLiked)
        _allLists[listIndex] = listWithItems.copy(taskList = updatedTaskList)
    }

    // Toggle task completion status
    fun toggleIsCompleted(taskListId: Int, taskId: Int) {
        val listIndex = _allLists.indexOfFirst { it.taskList.id == taskListId }
        if (listIndex == -1) {
            Log.e("ListManager", "TaskList with id $taskListId not found.")
            return
        }
        val listWithItems = _allLists[listIndex]
        val updatedTaskItems = listWithItems.taskItems.map {
            if (it.id == taskId) it.copy(isComplete = !it.isComplete) else it
        }
        _allLists[listIndex] = listWithItems.copy(taskItems = updatedTaskItems)
    }

    // Update task label
    fun updateTaskLabel(taskListId: Int, taskId: Int, label: String) {
        val listIndex = _allLists.indexOfFirst { it.taskList.id == taskListId }
        if (listIndex == -1) {
            Log.e("ListManager", "TaskList with id $taskListId not found.")
            return
        }
        val listWithItems = _allLists[listIndex]
        val updatedTaskItems = listWithItems.taskItems.map {
            if (it.id == taskId) it.copy(label = label) else it
        }
        _allLists[listIndex] = listWithItems.copy(taskItems = updatedTaskItems)
    }
}
