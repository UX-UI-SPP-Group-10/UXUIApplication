package com.group10.uxuiapp.view_model

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.group10.uxuiapp.data.TaskItem
import com.group10.uxuiapp.data.TaskList
import com.group10.uxuiapp.data.TaskRepository
import com.group10.uxuiapp.domain.ListManager
import kotlinx.coroutines.launch

class ListViewModel(private val repository: TaskRepository) : ViewModel() {

    private val listManager = ListManager()
    private val _lists = mutableStateOf<List<TaskList>>(emptyList())
    val lists: State<List<TaskList>> = _lists

    init {
        // Initialize the lists from the ListManager
        _lists.value = listManager.getLists()
    }

    fun addList(title: String) {
        listManager.addList(title)
        refreshLists()
    }

    fun removeList(index: Int) {
        if (index in _lists.value.indices) {
            listManager.removeList(index)
            refreshLists()
        }
    }

    fun updateTitle(index: Int, title: String) {
        if (index in _lists.value.indices) {
            listManager.updateTitle(index, title)
            refreshLists()
        }
    }

    fun toggleLikedStatus(index: Int) {
        if (index in _lists.value.indices) {
            listManager.toggleLikedStatus(index)
            refreshLists()
        }
    }

    fun toggleIsCompletedStatus(taskListIndex: Int, taskIndex: Int) {
        if (taskListIndex in _lists.value.indices) {
            listManager.toggleIsCompleted(taskListIndex, taskIndex)
            refreshLists()
        }
    }

    fun addTaskToList(taskListIndex: Int) {
        if (taskListIndex in _lists.value.indices) {
            val newTask = TaskItem(label = "")
            listManager.addTaskToList(taskListIndex, newTask)
            refreshLists()
        }
    }

    fun updateTaskLabel(taskListIndex: Int, taskIndex: Int, label: String) {
        if (taskListIndex in _lists.value.indices) {
            listManager.updateTaskLabel(taskListIndex, taskIndex, label)
            refreshLists()
        }
    }

    // This function now returns nullable data to avoid crashes
    fun getTaskById(taskID: Int): TaskList? {
        return _lists.value.getOrNull(taskID)
    }

    fun getTaskItem(taskListIndex: Int, taskIndex: Int): TaskItem? {
        return _lists.value.getOrNull(taskListIndex)?.taskItemList?.getOrNull(taskIndex)
    }

    // Refresh the entire list state
    private fun refreshLists() {
        _lists.value = listManager.getLists()
    }

    fun insertTaskList(taskList: TaskList) {
        viewModelScope.launch {
            repository.insertTaskList(taskList)
        }
    }

    fun insertTaskItem(taskItem: TaskItem) {
        viewModelScope.launch {
            repository.insertTaskItem(taskItem)
        }
    }

    fun getTaskListsWithItems() {
        viewModelScope.launch {
            val taskListsWithItems = repository.getTaskListsWithItems()
            // Handle the fetched data
        }
    }
}