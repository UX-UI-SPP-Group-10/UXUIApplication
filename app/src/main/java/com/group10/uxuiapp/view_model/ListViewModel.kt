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
import kotlinx.coroutines.launch

class ListViewModel(private val taskRepository: TaskRepository) : ViewModel() {
    private val _lists = mutableStateOf<List<TaskListWithItems>>(emptyList())
    val lists: State<List<TaskListWithItems>> = _lists

    init {
        refreshLists()
    }

    private fun refreshLists() {
        viewModelScope.launch {
            _lists.value = taskRepository.getTaskListsWithItems()
        }
    }

    fun addList(title: String) {
        viewModelScope.launch {
            val newList = TaskList(title = title)
            taskRepository.insertTaskList(newList)
            refreshLists()
        }
    }

    fun removeList(taskList: TaskList) {
        viewModelScope.launch {
            taskRepository.deleteTaskList(taskList)
            refreshLists()
        }
    }

    fun updateTitle(taskList: TaskList, title: String) {
        viewModelScope.launch {
            val updatedList = taskList.copy(title = title)
            taskRepository.insertTaskList(updatedList)
            refreshLists()
        }
    }

    fun toggleLikedStatus(taskList: TaskList) {
        viewModelScope.launch {
            val updatedList = taskList.copy(isLiked = !taskList.isLiked)
            taskRepository.insertTaskList(updatedList)
            refreshLists()
        }
    }

    fun addTaskToList(taskItem: TaskItem) {
        viewModelScope.launch {
            taskRepository.insertTaskItem(taskItem)
            refreshLists()
        }
    }

    fun toggleIsCompleted(taskItem: TaskItem) {
        viewModelScope.launch {
            val updatedTask = taskItem.copy(isComplete = !taskItem.isComplete)
            taskRepository.insertTaskItem(updatedTask)
            refreshLists()
        }
    }

    fun updateTaskLabel(taskItem: TaskItem, label: String) {
        viewModelScope.launch {
            val updatedTask = taskItem.copy(label = label)
            taskRepository.insertTaskItem(updatedTask)
            refreshLists()
        }
    }
}

