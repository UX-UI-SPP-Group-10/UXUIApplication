package com.group10.uxuiapp.ui.todolist.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.group10.uxuiapp.data.data_class.TodoList
import com.group10.uxuiapp.data.data_class.TodoListWithTaskItem
import com.group10.uxuiapp.data.TaskDataSource
import com.group10.uxuiapp.data.data_class.TaskItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TodoListViewModel(private val taskDataSource: TaskDataSource) : ViewModel() {
    private val TAG = "TodoListViewModel" // Debug tag for logs

    private val _lists = MutableStateFlow<List<TodoListWithTaskItem>>(emptyList())
    val lists: StateFlow<List<TodoListWithTaskItem>> = _lists

    private val _currentTodoList = MutableStateFlow<TodoListWithTaskItem?>(null)
    val currentTodoList: StateFlow<TodoListWithTaskItem?> = _currentTodoList

    init {
        Log.d(TAG, "Initializing TodoListViewModel")
        // Observe all task lists with their items
        viewModelScope.launch {
            try {
                taskDataSource.getTodoListsWithTasks()
                    .collect { todoListWithTaskItem ->
                        Log.d(TAG, "Fetched ${todoListWithTaskItem.size} TodoLists with tasks")
                        _lists.value = todoListWithTaskItem
                        _currentTodoList.value?.let { current ->
                            _currentTodoList.value = todoListWithTaskItem.find { it.todoList.id == current.todoList.id }
                        }
                    }
            } catch (e: Exception) {
                Log.e(TAG, "Error initializing TodoListViewModel: ${e.message}", e)
            }
        }
    }

    fun selectTodoList(todoListId: Int) {
        Log.d(TAG, "Selecting TodoList with id: $todoListId")
        val selectedList = _lists.value.find { it.todoList.id == todoListId }
        if (selectedList != null) {
            _currentTodoList.value = selectedList
            Log.d(TAG, "Selected TodoList: $selectedList")
        } else {
            Log.w(TAG, "TodoList with id $todoListId not found")
        }
    }

    fun addTodoList(title: String) {
        viewModelScope.launch {
            val newList = TodoList(title = title)
            try {
                val id = taskDataSource.insertTodoList(newList)
                Log.d(TAG, "Added new TodoList with id: $id and title: $title")
            } catch (e: Exception) {
                Log.e(TAG, "Error adding TodoList: ${e.message}", e)
            }
        }
    }

    fun removeTodoList(todoList: TodoList) {
        viewModelScope.launch {
            try {
                taskDataSource.deleteTodoList(todoList)
                Log.d(TAG, "Removed TodoList with id: ${todoList.id}")
            } catch (e: Exception) {
                Log.e(TAG, "Error removing TodoList: ${e.message}", e)
            }
        }
    }

    fun addTaskToList(taskItem: TaskItem) {
        viewModelScope.launch {
            try {
                val insertedTask = taskDataSource.insertTaskItem(taskItem)
                Log.d(TAG, "Added TaskItem: $insertedTask")
            } catch (e: Exception) {
                Log.e(TAG, "Error adding TaskItem: ${e.message}", e)
            }
        }
    }

    fun updateTodoList(todoList: TodoList, title: String? = null, isLiked: Boolean? = null) {
        viewModelScope.launch {
            try {
                Log.d(TAG, "Updating TodoList with id: ${todoList.id}, title: $title, isLiked: $isLiked")
                taskDataSource.updateTodoList(
                    todoList = todoList,
                    title = title,
                    isLiked = isLiked
                )
                Log.d(TAG, "TodoList updated successfully")
            } catch (e: Exception) {
                Log.e(TAG, "Error updating TodoList: ${e.message}", e)
            }
        }
    }

    fun updateTaskItem(taskItem: TaskItem, label: String? = null, isComplete: Boolean? = null) {
        viewModelScope.launch {
            try {
                Log.d(TAG, "Updating TaskItem with id: ${taskItem.id}, label: $label, isComplete: $isComplete")
                taskDataSource.updateTaskItem(
                    taskItem = taskItem,
                    label = label,
                    isComplete = isComplete
                )
                Log.d(TAG, "TaskItem updated successfully")
            } catch (e: Exception) {
                Log.e(TAG, "Error updating TaskItem: ${e.message}", e)
            }
        }
    }

    fun updateGifUrl(todoListId: Int, gifUrl: String) {
        viewModelScope.launch {
            try {
                val todoList = _lists.value.find { it.todoList.id == todoListId }?.todoList
                if (todoList != null) {
                    Log.d(TAG, "Updating gifUrl for TodoList with id: $todoListId, gifUrl: $gifUrl")
                    taskDataSource.updateTodoList(todoList.copy(gifUrl = gifUrl))
                    Log.d(TAG, "GifUrl updated successfully")
                } else {
                    Log.w(TAG, "TodoList with id $todoListId not found for gifUrl update")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error updating gifUrl: ${e.message}", e)
            }
        }
    }

    fun deleteTask(taskItem: TaskItem) {
        viewModelScope.launch {
            try {
                taskDataSource.deleteTaskItem(taskItem)
                Log.d(TAG, "Deleted TaskItem with id: ${taskItem.id}")
            } catch (e: Exception) {
                Log.e(TAG, "Error deleting TaskItem: ${e.message}", e)
            }
        }
    }
}
