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
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TodoListViewModel(private val taskDataSource: TaskDataSource) : ViewModel() {
    private val TAG = "TodoListViewModel" // Debug tag for logs

    private val _lists = MutableStateFlow<List<TodoListWithTaskItem>>(emptyList())
    val lists: StateFlow<List<TodoListWithTaskItem>> = _lists

    private val _selectedTodoList = MutableStateFlow<TodoList?>(null)
    val selectedTodoList = _selectedTodoList.asStateFlow()

    private val _todoListState = MutableStateFlow<TodoListState>(TodoListState.None)
    val todoListState = _todoListState.asStateFlow()


    init {
        viewModelScope.launch {
            try {
                taskDataSource.getTodoListsWithTasks()
                    .collect { todoListWithTaskItem ->
                        _lists.value = todoListWithTaskItem.sortedBy { it.todoList.listIndex }
                    }
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching TodoLists: ${e.message}", e)
            }
        }
    }

    fun setNewlistState() {
        Log.d(TAG, "Setting NewList state")
        _todoListState.value = TodoListState.NewList
    }

    fun setRenameState(todoList: TodoList) {
        Log.d(TAG, "Setting Rename state for TodoList with id: ${todoList.id}")
        _todoListState.value = TodoListState.Rename(todoList)
    }

    fun setSelectGifState(todoList: TodoList) {
        Log.d(TAG, "Setting SelectGif state for TodoList with id: ${todoList.id}")
        _todoListState.value = TodoListState.SelectGif(todoList)
    }

    fun setNoneState() {
        Log.d(TAG, "Setting None state")
        _todoListState.value = TodoListState.None
    }


    fun selectTodoList(todoList: TodoList?) {
        // If null is passed, it means we want to clear the selection
        _selectedTodoList.value = todoList
    }

    fun addTodoList(title: String) {
        viewModelScope.launch {
            Log.d(TAG, "addTodoList() called with title=$title")
            val newList = TodoList(
                title = title,
                listIndex = _lists.value.size // Set index to the current list size
            )
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

                val updatedLists = _lists.value
                    .filter { it.todoList.id != todoList.id }
                    .sortedBy { it.todoList.listIndex }
                    .mapIndexed { index, todoListWithTaskItem ->
                        val updatedTodoList = todoListWithTaskItem.todoList.copy(listIndex = index)
                        taskDataSource.updateListIndex(updatedTodoList.id, index)
                        todoListWithTaskItem.copy(todoList = updatedTodoList)
                    }

                _lists.value = updatedLists
            } catch (e: Exception) {
                Log.e(TAG, "Error removing TodoList: ${e.message}", e)
            }
        }
    }



    fun updateListOrder(updatedOrder: List<TodoList>) {
        viewModelScope.launch {
            try {
                updatedOrder.forEachIndexed { index, todoList ->
                    taskDataSource.updateListIndex(todoList.id, index)
                }
                Log.d(TAG, "Updated list order successfully")
            } catch (e: Exception) {
                Log.e(TAG, "Error updating list order: ${e.message}", e)
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

    fun updateTodoList(todoList: TodoList, title: String? = null, isLiked: Boolean? = null, newIndex: Int? = null) {
        viewModelScope.launch {
            try {
                val updatedTodoList = todoList.copy(
                    title = title ?: todoList.title,
                    isLiked = isLiked ?: todoList.isLiked
                )

                // If newIndex is provided, update the listIndex
                newIndex?.let {
                    taskDataSource.updateListIndex(todoList.id, it)
                    Log.d(TAG, "Updated listIndex for TodoList with id: ${todoList.id} to $it")
                }

                // Update other fields in the database
                taskDataSource.updateTodoList(updatedTodoList)
                Log.d(TAG, "Updated TodoList with id: ${updatedTodoList.id}")
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
