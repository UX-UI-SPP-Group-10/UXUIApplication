package com.group10.uxuiapp.ui.tasks.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.group10.uxuiapp.data.data_class.TaskItem
import com.group10.uxuiapp.data.TaskDataSource
import com.group10.uxuiapp.data.data_class.TodoListWithTaskItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class TaskViewModel(private val taskDataSource: TaskDataSource) : ViewModel() {

    private val _currentTodoList = MutableStateFlow<TodoListWithTaskItem?>(null)
    val currentTodoList: StateFlow<TodoListWithTaskItem?> = _currentTodoList

    fun selectTodoList(todoListId: Int) {
        viewModelScope.launch {
            // Fetch TodoList and associated TaskItems together
            val todoList = taskDataSource.getTodoListById(todoListId).first()
            val taskItems = taskDataSource.getTaskItemsByListId(todoListId).first()

            // Combine into a TodoListWithTaskItem and update state
            _currentTodoList.value = TodoListWithTaskItem(todoList, taskItems)
        }
    }

    fun addTaskToList(taskItem: TaskItem) {
        viewModelScope.launch {
            taskDataSource.insertTaskItem(taskItem)

            // Refresh tasks for the current TodoList
            val currentTodoListId = _currentTodoList.value?.todoList?.id
            if (currentTodoListId != null) {
                refreshTodoList(currentTodoListId)
            }
        }
    }

    fun updateTaskItem(taskItem: TaskItem, label: String? = null, isComplete: Boolean? = null) {
        viewModelScope.launch {
            // Update the TaskItem in the database
            val updatedTask = taskItem.copy(
                label = label ?: taskItem.label,
                isComplete = isComplete ?: taskItem.isComplete
            )
            taskDataSource.updateTaskItem(updatedTask)

            // Update the in-memory state (_currentTodoList)
            val currentTodoList = _currentTodoList.value
            if (currentTodoList != null) {
                val updatedTasks = currentTodoList.taskItems.map {
                    if (it.id == updatedTask.id) updatedTask else it
                }
                _currentTodoList.value = currentTodoList.copy(taskItems = updatedTasks)
            }
        }
    }


    fun deleteTask(taskItem: TaskItem) {
        viewModelScope.launch {
            taskDataSource.deleteTaskItem(taskItem)

            // Refresh tasks for the current TodoList
            val currentTodoListId = _currentTodoList.value?.todoList?.id
            if (currentTodoListId != null) {
                refreshTodoList(currentTodoListId)
            }
        }
    }

    private suspend fun refreshTodoList(todoListId: Int) {
        // Fetch updated TodoList and TaskItems
        val todoList = taskDataSource.getTodoListById(todoListId).first()
        val taskItems = taskDataSource.getTaskItemsByListId(todoListId).first()

        // Update the state with the refreshed data
        _currentTodoList.value = TodoListWithTaskItem(todoList, taskItems)
    }
}

