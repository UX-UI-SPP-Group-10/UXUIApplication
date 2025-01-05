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
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ListViewModel(private val taskRepository: TaskRepository) : ViewModel() {
    private val _lists = MutableStateFlow<List<TaskListWithItems>>(emptyList())
    val lists: StateFlow<List<TaskListWithItems>> = _lists

    private val _currentTaskList = MutableStateFlow<TaskListWithItems?>(null)
    val currentTaskList: StateFlow<TaskListWithItems?> = _currentTaskList

    init {
        // Observe all task lists with their items
        viewModelScope.launch {
            taskRepository.getTaskListsWithItems()
                .collect { taskListsWithItems ->
                    _lists.value = taskListsWithItems
                    // Update the current list if it is being viewed
                    _currentTaskList.value?.let { current ->
                        _currentTaskList.value = taskListsWithItems.find { it.taskList.id == current.taskList.id }
                    }
                }
        }
    }

    fun selectTaskList(taskListId: Int) {
        // Find the specific task list to observe
        val selectedList = _lists.value.find { it.taskList.id == taskListId }
        _currentTaskList.value = selectedList
    }

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

    fun updateTask(taskItem: TaskItem, newLabel: String? = null, isComplete: Boolean? = null) {
        viewModelScope.launch {
            val updatedTask = taskItem.copy(
                label = newLabel ?: taskItem.label,
                isComplete = isComplete ?: taskItem.isComplete
            )
            taskRepository.insertTaskItem(updatedTask)
        }
    }

    fun toggleIsCompleted(taskItem: TaskItem) {
        viewModelScope.launch {
            val updatedTask = taskItem.copy(isComplete = !taskItem.isComplete)
            taskRepository.insertTaskItem(updatedTask)
        }
    }

    fun updateTaskLabel(taskItem: TaskItem, newLabel: String) {
        viewModelScope.launch {
            val updatedTask = taskItem.copy(label = newLabel)
            taskRepository.insertTaskItem(updatedTask)
        }
    }

    fun deleteTask(taskItem: TaskItem) {
        viewModelScope.launch {
            taskRepository.deleteTaskItem(taskItem)
        }
    }
}


