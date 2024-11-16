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

    fun addTaskToList(index: Int, taskName: String) {
        val taskList = _allLists.find { it.index == index }

        taskList?.let {
            val newTask = TaskItem(label = taskName)

            it.task.add(newTask)

        }
        Log.d("ListManager", "Task added to list $index, All tasks: ${taskList?.task?.size}")
    }

    fun removeList(index: Int) {
        _allLists.removeIf { it.index == index }

        // Re-index the lists
        _allLists.forEachIndexed { i, taskList ->
            taskList.index = i
        }

        // Notify Compose about the change
        _allLists.clear()
        _allLists.addAll(_allLists)  // Trigger recomposition
    }

    fun updateTitle(index: Int, title: String) {
        val list = _allLists.find { it.index == index }
        list?.title = title

        // Notify Compose about the change
        _allLists.clear()
        _allLists.addAll(_allLists)  // Trigger recomposition
    }

    fun toggleLikedStatus(index: Int) {
        val list = _allLists.find { it.index == index }
        list?.isLiked = list?.isLiked?.not() ?: false

        // Notify Compose about the change
        _allLists.clear()
        _allLists.addAll(_allLists)  // Trigger recomposition
    }
}
