package com.group10.uxuiapp.view_model

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.group10.uxuiapp.data.data_class.TaskItem
import com.group10.uxuiapp.data.data_class.TaskList
import com.group10.uxuiapp.data.data_class.TaskListWithItems
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

    fun addTaskToList(taskItem: TaskItem) {
        viewModelScope.launch {
            taskRepository.insertTaskItem(taskItem)
        }
    }

    fun updateTaskLabel(taskItem: TaskItem, newLabel: String) {
        viewModelScope.launch {
            val updatedTask = taskItem.copy(label = newLabel)
            taskRepository.insertTaskItem(updatedTask)
        }
    }

    fun updateTaskList(taskList: TaskList, title: String? = null, isLiked: Boolean? = null) {
        viewModelScope.launch {
            taskRepository.updateTaskList(
                taskList = taskList,
                title = title,
                isLiked = isLiked
            )
        }
    }

    fun updateTaskItem(taskItem: TaskItem, label: String? = null, isComplete: Boolean? = null) {
        viewModelScope.launch {
            taskRepository.updateTaskItem(
                taskItem = taskItem,
                label = label,
                isComplete = isComplete
            )
        }
    }

    fun deleteTask(taskItem: TaskItem) {
        viewModelScope.launch {
            taskRepository.deleteTaskItem(taskItem)
        }
    }
}


