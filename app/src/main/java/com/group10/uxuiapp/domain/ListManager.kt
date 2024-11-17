package com.group10.uxuiapp.domain

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import com.group10.uxuiapp.data.TaskItem
import com.group10.uxuiapp.data.TaskList

class ListManager {
    private val _allLists = mutableStateListOf<TaskList>()
    fun getLists(): List<TaskList> = _allLists

    fun addList(title: String) {
        val newIndex = _allLists.size
        val newList = TaskList(index = newIndex, title = title)
        _allLists.add(newList)
    }

    fun addTaskToList(taskListIndex: Int, newTask: TaskItem) {
        val list = _allLists.getOrNull(taskListIndex)
        if (list == null) {
            Log.e("ListManager", "TaskList at index $taskListIndex not found.")
            return
        }
        list.taskItemList.add(newTask) // Directly update the task list

        // Notify Compose about the change
        _allLists[taskListIndex] = list.copy(taskItemList = list.taskItemList) // Replace the modified list to trigger recomposition
    }


    fun removeList(index: Int) {
        val list = _allLists.getOrNull(index) // Safely fetch the list or null if index is invalid
        if (list?.taskItemList.isNullOrEmpty()) {
            // Either the list doesn't exist, or the task list is empty
            Log.e("ListManager", "No valid task list at index $index, or the list is empty.")
            return
        }
        // Re-index the lists
        _allLists.forEachIndexed { i, taskList ->
            taskList.index = i
        }

        // Notify Compose about the change
        _allLists.clear()
        _allLists.addAll(_allLists)  // Trigger recomposition
    }

    fun updateTitle(index: Int, title: String) {
        val list = _allLists.getOrNull(index) // Safely fetch the list or null if index is invalid
        if (list?.taskItemList.isNullOrEmpty()) {
            // Either the list doesn't exist, or the task list is empty
            Log.e("ListManager", "No valid task list at index $index, or the list is empty.")
            return
        }
        list.title = title

        // Notify Compose about the change
        _allLists.clear()
        _allLists.addAll(_allLists)  // Trigger recomposition
    }

    fun toggleLikedStatus(index: Int) {
        val list = _allLists.getOrNull(index) // Safely fetch the list or null if index is invalid
        if (list?.taskItemList.isNullOrEmpty()) {
            // Either the list doesn't exist, or the task list is empty
            Log.e("ListManager", "No valid task list at index $index, or the list is empty.")
            return
        }
        list.isLiked = list.isLiked.not()

        // Notify Compose about the change
        _allLists.clear()
        _allLists.addAll(_allLists)  // Trigger recomposition
    }

    fun toggleIsCompleted(index: Int, taskIndex: Int) {
        val list = _allLists.getOrNull(index)
        val task = list?.taskItemList?.getOrNull(taskIndex)

        if (task == null) {
            Log.e("ListManager", "Task not found at index $taskIndex.")
            return
        }

        task.isComplete = !task.isComplete

        // Notify Compose about the change
        _allLists[index] = list.copy(taskItemList = list.taskItemList)
    }

    fun updateTaskLabel(index: Int, taskIndex: Int, label: String) {
        val list = _allLists.getOrNull(index)
        val task = list?.taskItemList?.getOrNull(taskIndex)

        if (task == null) {
            Log.e("ListManager", "Task not found at index $taskIndex.")
            return
        }

        task.label = label

        // Notify Compose about the change
        _allLists[index] = list.copy(taskItemList = list.taskItemList)
    }

}
