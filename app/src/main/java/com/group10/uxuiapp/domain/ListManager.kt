package com.group10.uxuiapp.domain

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import com.group10.uxuiapp.data.TaskItem
import com.group10.uxuiapp.data.TaskList
import com.group10.uxuiapp.data.TaskListWithItems
import com.group10.uxuiapp.data.TaskRepository
import kotlinx.coroutines.flow.Flow

class ListManager(private val taskRepository: TaskRepository) {

    fun getLists(): Flow<List<TaskListWithItems>> {
        return taskRepository.getTaskListsWithItems()
    }

    suspend fun addList(title: String) {
        val newList = TaskList(title = title)
        taskRepository.insertTaskList(newList)
    }

    suspend fun removeList(taskList: TaskList) {
        taskRepository.deleteTaskList(taskList)
    }

    suspend fun updateTitle(taskList: TaskList, title: String) {
        val updatedList = taskList.copy(title = title)
        taskRepository.insertTaskList(updatedList)
    }

    suspend fun toggleLikedStatus(taskList: TaskList) {
        val updatedList = taskList.copy(isLiked = !taskList.isLiked)
        taskRepository.insertTaskList(updatedList)
    }

    suspend fun addTaskToList(taskItem: TaskItem) {
        taskRepository.insertTaskItem(taskItem)
    }

    suspend fun toggleIsCompleted(taskItem: TaskItem) {
        val updatedTask = taskItem.copy(isComplete = !taskItem.isComplete)
        taskRepository.insertTaskItem(updatedTask)
    }

    suspend fun updateTaskLabel(taskItem: TaskItem, label: String) {
        val updatedTask = taskItem.copy(label = label)
        taskRepository.insertTaskItem(updatedTask)
    }
}
