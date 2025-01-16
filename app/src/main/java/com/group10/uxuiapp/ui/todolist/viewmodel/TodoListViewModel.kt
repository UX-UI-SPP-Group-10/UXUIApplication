package com.group10.uxuiapp.ui.todolist.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.group10.uxuiapp.data.NotificationHelper
import com.group10.uxuiapp.data.data_class.TodoList
import com.group10.uxuiapp.data.data_class.TodoListWithTaskItem
import com.group10.uxuiapp.data.TaskDataSource
import com.group10.uxuiapp.data.data_class.TaskItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import java.sql.Date

class TodoListViewModel(private val taskDataSource: TaskDataSource) : ViewModel() {
    private val TAG = "TodoListViewModel" // Debug tag for logs

    // Our Main List from database
    private val _lists = MutableStateFlow<List<TodoListWithTaskItem>>(emptyList())
    val lists: StateFlow<List<TodoListWithTaskItem>> = _lists

    // This list is used while dragging to keep track of the temporary order
    private val _temporaryList = MutableStateFlow<List<TodoListWithTaskItem>>(emptyList())
    val temporaryList: StateFlow<List<TodoListWithTaskItem>> = _temporaryList

    // StateFlow to keep track of the selected TodoList
    private val _selectedTodoList = MutableStateFlow<TodoList?>(null)
    val selectedTodoList = _selectedTodoList.asStateFlow()

    // StateFlow to change popup states of the todolist screen
    private val _todoListState = MutableStateFlow<TodoListState>(TodoListState.None)
    val todoListState = _todoListState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _newTodoListId = MutableStateFlow<Int?>(null)
    val newTodoListId = _newTodoListId.asStateFlow()

    private val _isCurrentlyDragging = MutableStateFlow(false)
    val isCurrentlyDragging: StateFlow<Boolean> = _isCurrentlyDragging.asStateFlow()

    fun setDraggingState(isDragging: Boolean) {
        _isCurrentlyDragging.value = isDragging
    }

    val searchList = searchQuery
        .combine(_lists) { query, lists ->
            if(query.isEmpty()) {
                lists
            } else {
                lists.filter {
                    it.doesMatchSearchQuery(query)
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = _lists.value
        )

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

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

    fun setRenameState(todoList: TodoList) {
        Log.d(TAG, "Setting Rename state for TodoList with id: ${todoList.id}")
        _selectedTodoList.value = todoList
        _todoListState.value = TodoListState.Rename(todoList)
    }

    fun setSelectGifState(todoList: TodoList) {
        Log.d(TAG, "Setting SelectGif state for TodoList with id: ${todoList.id}")
        _todoListState.value = TodoListState.SelectGif(todoList)
    }

    fun setTagEditState(todoList: TodoList) {
        Log.d(TAG, "Setting TagEdit state for TodoList with id: ${todoList.id}")
        _todoListState.value = TodoListState.TagsEdit(todoList)
    }

    fun setNoneState() {
        Log.d(TAG, "Setting None state")
        _todoListState.value = TodoListState.None
    }


    fun selectTodoList(todoList: TodoList?) {
        Log.d(TAG, "Selecting TodoList with id: ${todoList?.title}")
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
                val id = taskDataSource.insertTodoList(newList).toInt()
                _newTodoListId.value = id // Track the newly created list's ID
                Log.d(TAG, "Added new TodoList with id: $id and title: $title")
            } catch (e: Exception) {
                Log.e(TAG, "Error adding TodoList: ${e.message}", e)
            }
        }
    }

    fun resetNewTodoList() {
        _newTodoListId.value = null
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

    fun updateAllListIndexes(updatedOrder: List<TodoListWithTaskItem>) {
        viewModelScope.launch {
            try {
                // Update indexes only if dragging is not active
                if (_isCurrentlyDragging.value) {
                    Log.d(TAG, "Skipping update: Dragging is active")
                    return@launch
                }

                val updatedIndexes = updatedOrder.mapIndexed { index, todoListWithTaskItem ->
                    todoListWithTaskItem.todoList.id to index
                }

                taskDataSource.updateAllIndexes(updatedIndexes)
                _lists.value = updatedOrder
                Log.d(TAG, "Updated all list indexes successfully")
            } catch (e: Exception) {
                Log.e(TAG, "Error updating all list indexes: ${e.message}", e)
            }
        }
    }


    fun updateTodoList(
        id: Int,
        title: String? = null,
        isLiked: Boolean? = null,
        gifUrl: String? = null,
        textColor: String? = null,
        tags: String? = null,
        dueDate: Long? = null,
        newIndex: Int? = null,  // unused for now, updating all indexes instead when moving
        isRepeating: Boolean? = null,
        repeatDay: Int? = null
    ) {
        viewModelScope.launch {
            try {

                // Update other fields in the database
                taskDataSource.updateTodoList(
                    todoListId = id,
                    title = title,
                    isLiked = isLiked,
                    gifUrl = gifUrl,
                    textColor = textColor,
                    tags = tags,
                    dueDate = dueDate,
                    isRepeating = isRepeating,
                    repeatDay = repeatDay
                )
                Log.d(TAG, "Updated TodoList with id: ${id}")
            } catch (e: Exception) {
                Log.e(TAG, "Error updating TodoList: ${e.message}", e)
            }
        }
    }

    fun updateTodoListDueDate(todoListId: Int, dueDate: Long?, context: Context, todoListTitle: String) {
        Log.d("NotificationTest","updateTodoListDueDate called with ID: $todoListId and dueDate: $dueDate")

        viewModelScope.launch {
            try {
                updateTodoList(id = todoListId, dueDate = dueDate, title = todoListTitle)
                Log.d("NotificationTest", "Due date updated for TodoList ID: $todoListId")

                // Schedule notification
                dueDate?.let {
                    NotificationHelper(context).scheduleDueDateNotification(
                        todoTitle = ": $todoListTitle",
                        message = "Your task is due soon!",
                        dueDateMillis = it
                    )
                    Log.d("NotificationTest", "Notification scheduled for due date: ${Date(it)}")
                }
            } catch (e: Exception) {
                Timber.tag("NotificationTest").e(e, "Error updating TodoList due date")
            }
        }
    }

    val taskWithDueDates: LiveData<List<TodoList>> = taskDataSource.getTodoListsWithDueDates().asLiveData()

    fun getTaskDueBefore(timestamp: Long): LiveData<List<TodoList>> {
        return taskDataSource.getTodoListsDueBefore(timestamp).asLiveData()
    }




}
