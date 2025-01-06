package com.group10.uxuiapp.domain

import com.group10.uxuiapp.data.data_class.TaskItem
import com.group10.uxuiapp.data.data_class.TodoList
import com.group10.uxuiapp.data.data_class.TodoListWithTaskItem
import com.group10.uxuiapp.data.TaskRepository
import kotlinx.coroutines.flow.Flow

class ListManager(private val taskRepository: TaskRepository) {

    fun getLists(): Flow<List<TodoListWithTaskItem>> {
        return taskRepository.getTodoListWithTask()
    }

    suspend fun addList(title: String) {
        val newList = TodoList(title = title)
        taskRepository.insertTodoList(newList)
    }

    suspend fun removeList(todoList: TodoList) {
        taskRepository.deleteTodoList(todoList)
    }

    suspend fun updateTitle(todoList: TodoList, title: String) {
        val updatedList = todoList.copy(title = title)
        taskRepository.insertTodoList(updatedList)
    }

    suspend fun toggleLikedStatus(todoList: TodoList) {
        val updatedList = todoList.copy(isLiked = !todoList.isLiked)
        taskRepository.insertTodoList(updatedList)
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
