package com.group10.uxuiapp.view_model

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.group10.uxuiapp.data.TaskItem
import com.group10.uxuiapp.data.TaskList
import com.group10.uxuiapp.data.TaskListWithItems
import com.group10.uxuiapp.data.TaskRepository
import com.group10.uxuiapp.domain.ListManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ListViewModel(private val taskRepository: TaskRepository) : ViewModel() {
    private val _lists = MutableStateFlow<List<TaskListWithItems>>(emptyList())
    val lists: StateFlow<List<TaskListWithItems>> = _lists


    init {
        viewModelScope.launch {
            taskRepository.getTaskListsWithItems()
                .collect { taskListsWithItems ->
                    _lists.value = taskListsWithItems
                }
        }
    }

    // Old implementation, replaced by the init block above
//    private fun refreshLists() {
//        viewModelScope.launch {
//            _lists.value = taskRepository.getTaskListsWithItems()
//        }
//    }

    fun addList(title: String) {
        viewModelScope.launch {
            val newList = TaskList(title = title)
            taskRepository.insertTaskList(newList)
        }
    }

    fun removeList(taskList: TaskList) {
        viewModelScope.launch {
            taskRepository.deleteTaskList(taskList)
        }
    }

    fun updateTitle(taskList: TaskList, title: String) {
        viewModelScope.launch {
            val updatedList = taskList.copy(title = title)
            taskRepository.insertTaskList(updatedList)
        }
    }

    fun toggleLikedStatus(taskList: TaskList) {
        viewModelScope.launch {
            val updatedList = taskList.copy(isLiked = !taskList.isLiked)
            taskRepository.insertTaskList(updatedList)
        }
    }

    fun addTaskToList(taskItem: TaskItem) {
        viewModelScope.launch {
            taskRepository.insertTaskItem(taskItem)
        }
    }

    fun toggleIsCompleted(taskItem: TaskItem) {
        viewModelScope.launch {
            val updatedTask = taskItem.copy(isComplete = !taskItem.isComplete)
            taskRepository.insertTaskItem(updatedTask)
        }
    }

    fun updateTaskLabel(taskItem: TaskItem, label: String) {
        viewModelScope.launch {
            val updatedTask = taskItem.copy(label = label)
            taskRepository.insertTaskItem(updatedTask)
        }
    }
}

