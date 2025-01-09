package com.group10.uxuiapp.ui.tasks.viewmodel

import android.util.Log
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.group10.uxuiapp.data.data_class.TaskItem
import com.group10.uxuiapp.data.TaskDataSource
import com.group10.uxuiapp.data.data_class.SubTask
import com.group10.uxuiapp.data.data_class.TaskItemWithSubTask
import com.group10.uxuiapp.data.data_class.TodoListWithTaskItem
import com.group10.uxuiapp.ui.tasks.view.components.SubTaskRow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TaskViewModel(private val taskDataSource: TaskDataSource) : ViewModel() {

    private val TAG = "TaskViewModel" // Tag for logging
    private val _currentTodoListId = MutableStateFlow<Int?>(null)

    private val _selectedTask = MutableStateFlow<TaskItem?>(null)
    val selectedTaskItem: StateFlow<TaskItem?> = _selectedTask

    private val _lastSelectedTaskItem = MutableStateFlow<TaskItem?>(null)
    val lastSelectedTaskItem: StateFlow<TaskItem?> = _lastSelectedTaskItem

    private val _lists = MutableStateFlow<List<TaskItemWithSubTask>>(emptyList())
    val lists: StateFlow<List<TaskItemWithSubTask>> = _lists

    init {
        viewModelScope.launch {
            try {
                taskDataSource.getTaskItemWithSubTask()
                    .collect { taskItemWithSubTask ->
                        _lists.value = taskItemWithSubTask.sortedBy { it.taskItem.id }
                    }
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching TodoLists: ${e.message}", e)
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val currentTodoList: StateFlow<TodoListWithTaskItem?> = _currentTodoListId.flatMapLatest { todoListId ->
        if (todoListId != null) {
            Log.d(TAG, "Fetching TodoListWithTaskItem for id: $todoListId")
            taskDataSource.getTodoListWithTaskById(todoListId)
        } else {
            Log.d(TAG, "No TodoList selected, emitting null")
            flowOf(null)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun selectTodoList(todoListId: Int) {
        Log.d(TAG, "Selecting TodoList with id: $todoListId")
        _currentTodoListId.value = todoListId
    }

    fun addTaskToList(taskItem: TaskItem) {
        viewModelScope.launch {
            Log.d(TAG, "Adding TaskItem: $taskItem")
            try {
                taskDataSource.insertTaskItem(taskItem)
                Log.d(TAG, "TaskItem added successfully: $taskItem")
            } catch (e: Exception) {
                Log.e(TAG, "Error adding TaskItem: ${e.message}", e)
            }
        }
    }

    fun updateTaskItem(taskItem: TaskItem, label: String? = null, isComplete: Boolean? = null, isFolded: Boolean? = null) {
        viewModelScope.launch {
            val updatedTask = taskItem.copy(
                label = label ?: taskItem.label,
                isComplete = isComplete ?: taskItem.isComplete,
                isFolded = isFolded ?: taskItem.isFolded
            )

            Log.d(TAG, "Updating TaskItem with id: ${updatedTask.id}, label: ${updatedTask.label}, isComplete: ${updatedTask.isComplete}")

            try {
                taskDataSource.updateTaskItem(updatedTask, label = updatedTask.label, isComplete = updatedTask.isComplete, isFolded = updatedTask.isFolded)
                Log.d(TAG, "TaskItem updated successfully: $updatedTask")
            } catch (e: Exception) {
                Log.e(TAG, "Error updating TaskItem: ${e.message}", e)
            }
        }
    }

    fun updateSubTask(subTask: SubTask, label: String? = null, isComplete: Boolean? = null) {
        viewModelScope.launch {
            val updatedTask = subTask.copy(
                label = label ?: subTask.label,
                isComplete = isComplete ?: subTask.isComplete,
            )

            Log.d(TAG, "Updating TaskItem with id: ${updatedTask.id}, label: ${updatedTask.label}, isComplete: ${updatedTask.isComplete}")

            try {
                taskDataSource.updateSubtask(updatedTask, label = updatedTask.label, isComplete = updatedTask.isComplete)
                Log.d(TAG, "SupTask updated successfully: $updatedTask")
            } catch (e: Exception) {
                Log.e(TAG, "Error updating SupTask: ${e.message}", e)
            }
        }
    }

    fun deleteTask(taskItem: TaskItem) {
        viewModelScope.launch {
            Log.d(TAG, "Deleting TaskItem: $taskItem")
            try {
                taskDataSource.deleteTaskItem(taskItem)
                Log.d(TAG, "TaskItem deleted successfully: $taskItem")
            } catch (e: Exception) {
                Log.e(TAG, "Error deleting TaskItem: ${e.message}", e)
            }
        }
    }

    fun selectTask(taskItem: TaskItem?) {
        Log.d(TAG, "Selecting TaskItem: ${taskItem?.id.toString()}")
        _selectedTask.value = taskItem
        if(taskItem != null){
            _lastSelectedTaskItem.value = taskItem
        }
    }

    fun selectSubtask(subTask: SubTask?) {
        Log.d(TAG, "Selecting TaskItem: ${subTask?.id.toString()}")
        TODO()
    }


    fun addSupTask(subTask: SubTask) {
        viewModelScope.launch {
            Log.d(TAG, "Adding TaskItem: $subTask")
            try {
                taskDataSource.insertSubTsk(subTask)
                Log.d(TAG, "TaskItem added successfully: $subTask")
            } catch (e: Exception) {
                Log.e(TAG, "Error adding TaskItem: ${e.message}", e)
            }
        }
    }

    fun deleteSupTask(subTask: SubTask) {
        viewModelScope.launch {
            Log.d(TAG, "Deleting TaskItem: $subTask")
            try {
                taskDataSource.deleteSubTask(subTask)
                Log.d(TAG, "TaskItem deleted successfully: $subTask")
            } catch (e: Exception) {
                Log.e(TAG, "Error deleting TaskItem: ${e.message}", e)
            }
        }
    }

}
