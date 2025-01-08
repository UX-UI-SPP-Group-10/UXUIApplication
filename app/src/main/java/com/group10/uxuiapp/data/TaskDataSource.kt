package com.group10.uxuiapp.data

import android.util.Log
import com.group10.uxuiapp.data.data_class.TaskItem
import com.group10.uxuiapp.data.data_class.TodoList
import com.group10.uxuiapp.data.data_class.TodoListWithTaskItem
import kotlinx.coroutines.flow.Flow

class TaskDataSource(private val taskDao: TaskDao) {

    // Insert Operations
    suspend fun insertTodoList(todoList: TodoList): Long {
        Log.d("TaskDataSource", "Creating new TodoList with title='${todoList.title}'")
        val insertedId = taskDao.insertTodoList(todoList)
        Log.d("TaskDataSource", "New TodoList created with ID=$insertedId")
        return insertedId
    }

    suspend fun insertTaskItem(taskItem: TaskItem) = taskDao.insertTaskItem(taskItem)

    // Fetch TodoList and Related Tasks
    fun getTodoListsWithTasks(): Flow<List<TodoListWithTaskItem>> = taskDao.getTodoListsWithItems()
    fun getTodoListById(id: Int): Flow<TodoList> = taskDao.getTodoListById(id)
    fun getTodoListWithTaskById(todoListId: Int): Flow<TodoListWithTaskItem> = taskDao.getTodoListWithTaskById(todoListId)

    // Update Operations
    suspend fun updateTodoList(todoList: TodoList, title: String? = null, isLiked: Boolean? = null, gifUrl: String? = null, dueDate: Long? = null) {
        val updatedTitle = title ?: todoList.title
        val updatedIsLiked = isLiked ?: todoList.isLiked
        val updatedGifUrl = gifUrl ?: todoList.gifUrl ?: ""
        val updatedDueDate = dueDate ?: todoList.dueDate

        Log.d(
            "TaskDataSource",
            "Updating TodoList: id=${todoList.id}, title=$updatedTitle, isLiked=$updatedIsLiked, gifUrl=$updatedGifUrl, dueDate=$updatedDueDate"
        )

        // Pass non-nullable values to the DAO
        taskDao.updateTodoList(
            id = todoList.id,
            title = updatedTitle,
            isLiked = updatedIsLiked,
            gifUrl = updatedGifUrl,
            dueDate = updatedDueDate
        )
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

    suspend fun updateListIndex(todoListId: Int, newIndex: Int) {
        Log.d("TaskDataSource", "Updating listIndex for TodoList with id=$todoListId to newIndex=$newIndex")
        taskDao.updateListIndex(
            todoListId = todoListId,
            listIndex = newIndex
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

    fun getTodoListsWithDueDates() : Flow<List<TodoList>> = taskDao.getTodoListsWithDueDates()

    fun getTodoListsDueBefore(timestamp: Long): Flow<List<TodoList>> = taskDao.getTodoListsDueBefore(timestamp)
}
