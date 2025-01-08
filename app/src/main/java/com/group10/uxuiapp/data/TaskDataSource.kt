package com.group10.uxuiapp.data

import android.util.Log
import com.group10.uxuiapp.data.data_class.SupTask
import com.group10.uxuiapp.data.data_class.TaskItem
import com.group10.uxuiapp.data.data_class.TaskItemWhithSupTask
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
    suspend fun insertSubTsk(subTask: SupTask) = taskDao.insertSubTask(subTask)

    // Fetch TodoList and Related Tasks
    fun getTodoListsWithTasks(): Flow<List<TodoListWithTaskItem>> = taskDao.getTodoListsWithItems()
    fun getTodoListById(id: Int): Flow<TodoList> = taskDao.getTodoListById(id)
    fun getTodoListWithTaskById(todoListId: Int): Flow<TodoListWithTaskItem> = taskDao.getTodoListWithTaskById(todoListId)
    fun getTaskItemWithSubTask(): Flow<List<TaskItemWhithSupTask>> = taskDao.getTaskItemWithSubTask()
    fun getTaskListWithSubTaskById(taskItemId: Int): Flow<TaskItemWhithSupTask> = taskDao.getTaskItemWithSubTaskById(taskItemId)

    // Update Operations
    suspend fun updateTodoList(todoList: TodoList, title: String? = null, isLiked: Boolean? = null, gifUrl: String? = null) {
        val updatedTitle = title ?: todoList.title
        val updatedIsLiked = isLiked ?: todoList.isLiked
        val updatedGifUrl = gifUrl ?: todoList.gifUrl ?: ""

        Log.d(
            "TaskDataSource",
            "Updating TodoList: id=${todoList.id}, title=$updatedTitle, isLiked=$updatedIsLiked, gifUrl=$updatedGifUrl"
        )

        // Pass non-nullable values to the DAO
        taskDao.updateTodoList(
            id = todoList.id,
            title = updatedTitle,
            isLiked = updatedIsLiked,
            gifUrl = updatedGifUrl
        )
    }

    suspend fun updateTaskItem(taskItem: TaskItem, label: String? = null, isComplete: Boolean? = null, isFoldet: Boolean? = null) {
        val updatedLabel = label ?: taskItem.label
        val updatedIsComplete = isComplete ?: taskItem.isComplete
        val updateIsFoldet = isFoldet ?: taskItem.isfoldet

        Log.d("TaskDataSource", "Updating TaskItem: id=${taskItem.id}, label=$updatedLabel, isComplete=$updatedIsComplete")

        taskDao.updateTaskItem(
            id = taskItem.id,
            label = updatedLabel,
            isComplete = updatedIsComplete,
            isFoldet = updateIsFoldet
        )
    }

    suspend fun updateSuptask(subTask: SupTask, label: String? = null, isComplete: Boolean? = null) {
        val updatedLabel = label ?: subTask.label
        val updatedIsComplete = isComplete ?: subTask.isComplete

        Log.d("TaskDataSource", "Updating TaskItem: id=${subTask.id}, label=$updatedLabel, isComplete=$updatedIsComplete")

        taskDao.updateSupTask(
            id = subTask.id,
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

    suspend fun deleteSupTask(subTask: SupTask) = taskDao.deleteSubTask(subTask)
}
