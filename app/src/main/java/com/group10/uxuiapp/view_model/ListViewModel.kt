package com.group10.uxuiapp.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.group10.uxuiapp.data.data_class.TaskItem
import com.group10.uxuiapp.data.data_class.TodoList
import com.group10.uxuiapp.data.data_class.TodoListWithTaskItem
import com.group10.uxuiapp.data.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ListViewModel(private val taskRepository: TaskRepository) : ViewModel() {
    private val _lists = MutableStateFlow<List<TodoListWithTaskItem>>(emptyList())
    val lists: StateFlow<List<TodoListWithTaskItem>> = _lists

    private val _currentTodoList = MutableStateFlow<TodoListWithTaskItem?>(null)
    val currentTodoList: StateFlow<TodoListWithTaskItem?> = _currentTodoList

    init {
        // Observe all task lists with their items
        viewModelScope.launch {
            taskRepository.getTodoListWithTask()
                .collect { todoListWithTaskItem ->
                    _lists.value = todoListWithTaskItem
                    // Update the current list if it is being viewed
                    _currentTodoList.value?.let { current ->
                        _currentTodoList.value = todoListWithTaskItem.find { it.todoList.id == current.todoList.id }
                    }
                }
        }
    }

    fun selectTodoList(todoListId: Int) {
        // Find the specific task list to observe
        val selectedList = _lists.value.find { it.todoList.id == todoListId }
        _currentTodoList.value = selectedList
    }

    fun addList(title: String) {
        viewModelScope.launch {
            val newList = TodoList(title = title)
            taskRepository.insertTodoList(newList)
        }
    }

    fun removeList(todoList: TodoList) {
        viewModelScope.launch {
            taskRepository.deleteTodoList(todoList)
        }
    }

    fun addTaskToList(taskItem: TaskItem) {
        viewModelScope.launch {
            taskRepository.insertTaskItem(taskItem)
        }
    }

    fun updateTodoList(todoList: TodoList, title: String? = null, isLiked: Boolean? = null) {
        viewModelScope.launch {
            taskRepository.updateTodoList(
                todoList = todoList,
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

    fun updateGifUrl(todoListId: Int, gifUrl: String) {
        viewModelScope.launch {
            val taskList = _lists.value.find { it.todoList.id == todoListId }?.todoList
            if (taskList != null) {
                taskRepository.updateTodoList(taskList.copy(gifUrl = gifUrl))
            }
        }
    }

    fun deleteTask(taskItem: TaskItem) {
        viewModelScope.launch {
            taskRepository.deleteTaskItem(taskItem)
        }
    }
}


