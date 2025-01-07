package com.group10.uxuiapp.ui.todolist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.group10.uxuiapp.data.data_class.TodoList
import com.group10.uxuiapp.data.data_class.TodoListWithTaskItem
import com.group10.uxuiapp.data.TaskDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TodoListViewModel(private val taskDataSource: TaskDataSource) : ViewModel() {
    private val _lists = MutableStateFlow<List<TodoListWithTaskItem>>(emptyList())
    val lists: StateFlow<List<TodoListWithTaskItem>> = _lists

    private val _currentTodoList = MutableStateFlow<TodoListWithTaskItem?>(null)
    val currentTodoList: StateFlow<TodoListWithTaskItem?> = _currentTodoList

    init {
        observeTodoListsWithTasks()
    }

    private fun observeTodoListsWithTasks() {
        viewModelScope.launch {
            taskDataSource.getTodoListWithTask().collect { todoListWithTaskItems ->
                _lists.value = todoListWithTaskItems
                // Update the current TodoList if it is being viewed
                _currentTodoList.value?.let { current ->
                    _currentTodoList.value = todoListWithTaskItems.find { it.todoList.id == current.todoList.id }
                }
            }
        }
    }

    fun selectTodoList(todoListId: Int) {
        // Select and observe a specific TodoList
        _currentTodoList.value = _lists.value.find { it.todoList.id == todoListId }
    }

    fun refreshTodoLists() {
        // Force refresh
        observeTodoListsWithTasks()
    }

    fun addTodoList(title: String) {
        viewModelScope.launch {
            val newList = TodoList(title = title)
            taskDataSource.insertTodoList(newList)
        }
    }

    fun removeTodoList(todoList: TodoList) {
        viewModelScope.launch {
            taskDataSource.deleteTodoList(todoList)
        }
    }

    fun updateTodoList(todoList: TodoList, title: String? = null, isLiked: Boolean? = null, gifUrl: String? = null) {
        viewModelScope.launch {
            taskDataSource.updateTodoList(
                todoList.copy(
                    title = title ?: todoList.title,
                    isLiked = isLiked ?: todoList.isLiked,
                    gifUrl = gifUrl ?: todoList.gifUrl
                )
            )
        }
    }

    fun updateGifUrl(todoListId: Int, gifUrl: String) {
        viewModelScope.launch {
            val todoList = _lists.value.find { it.todoList.id == todoListId }?.todoList
            if (todoList != null) {
                taskDataSource.updateTodoList(todoList.copy(gifUrl = gifUrl))
            }
        }
    }
}
