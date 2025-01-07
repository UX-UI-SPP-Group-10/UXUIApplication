package com.group10.uxuiapp.data

import android.util.Log
import com.group10.uxuiapp.data.data_class.TaskItem
import com.group10.uxuiapp.data.data_class.TodoList
import com.group10.uxuiapp.data.data_class.TodoListWithTaskItem
import kotlinx.coroutines.flow.Flow

class TaskDataSource(private val taskDao: TaskDao) {

    // Insert Operations
    suspend fun insertTodoList(todoList: TodoList) = taskDao.insertTodoList(todoList)
    suspend fun insertTaskItem(taskItem: TaskItem) = taskDao.insertTaskItem(taskItem)

    // Fetch TodoList and Related Tasks
    fun getTodoListsWithTasks(): Flow<List<TodoListWithTaskItem>> = taskDao.getTodoListsWithItems()
    fun getTodoListById(id: Int): Flow<TodoList> = taskDao.getTodoListById(id)
    fun getTodoListWithTaskById(todoListId: Int): Flow<TodoListWithTaskItem> = taskDao.getTodoListWithTaskById(todoListId)

    // Update Operations
    suspend fun updateTodoList(todoList: TodoList, title: String? = null, isLiked: Boolean? = null, gifUrl: String? = null) {
        taskDao.updateTodoList(todoList.id, title, isLiked, gifUrl)
    }

    suspend fun updateTaskItem(taskItem: TaskItem, label: String? = null, isComplete: Boolean? = null) {
        val updatedLabel = label ?: taskItem.label
        val updatedIsComplete = isComplete ?: taskItem.isComplete

        Log.d("TaskDataSource", "Updating TaskItem: id=${taskItem.id}, label=$updatedLabel, isComplete=$updatedIsComplete")

        taskDao.updateTaskItem(
            id = taskItem.id,
            label = updatedLabel,
            isComplete = updatedIsComplete
        )
    }



    fun getTasksByCompletionStatus(isComplete: Boolean): Flow<List<TaskItem>> {
        return taskDao.getTasksByCompletionStatus(isComplete)
    }

    // Delete Operations
    suspend fun deleteTodoList(todoList: TodoList) {
        taskDao.deleteTodoList(todoList)
    }

    suspend fun deleteTaskItem(taskItem: TaskItem) = taskDao.deleteTaskItem(taskItem)

    suspend fun deleteTaskById(taskId: Int) = taskDao.deleteTaskItemById(taskId)
}
