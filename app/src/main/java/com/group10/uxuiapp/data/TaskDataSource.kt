package com.group10.uxuiapp.data

import com.group10.uxuiapp.data.data_class.TaskItem
import com.group10.uxuiapp.data.data_class.TodoList
import com.group10.uxuiapp.data.data_class.TodoListWithTaskItem
import kotlinx.coroutines.flow.Flow

class TaskDataSource(private val taskDao: TaskDao) {
    suspend fun insertTodoList(todoList: TodoList) = taskDao.insertTodoList(todoList)
    suspend fun insertTaskItem(taskItem: TaskItem) = taskDao.insertTaskItem(taskItem)
    fun getTodoListWithTask(): Flow<List<TodoListWithTaskItem>> = taskDao.getTodoListsWithItems()
    fun getTodoListById(id: Int): Flow<TodoList> = taskDao.getTodoListById(id)
    fun getTaskItemsByListId(todoListID: Int): Flow<List<TaskItem>> = taskDao.getTaskItemsByListId(todoListID)

    suspend fun updateTodoList(todoList: TodoList, title: String? = null, isLiked: Boolean? = null) {
        taskDao.updateTodoList(todoList.id, title, isLiked)
    }

    suspend fun updateTaskItem(taskItem: TaskItem, label: String? = null, isComplete: Boolean? = null) {
        taskDao.updateTaskItem(
            id = taskItem.id,
            label = label,
            isComplete = isComplete
        )
    }
    suspend fun updateGifUrl(todoListID: Int, gifUrl: String) {
        taskDao.updateGifUrl(todoListID, gifUrl)
    }

    fun getTaskItemById(taskId: Int): Flow<TaskItem> = taskDao.getTaskItemById(taskId)

    suspend fun deleteTodoList(todoList: TodoList) = taskDao.deleteTodoList(todoList)
    suspend fun deleteTaskItem(taskItem: TaskItem) = taskDao.deleteTaskItem(taskItem)
}
