package com.group10.uxuiapp.data

import android.util.Log
import com.group10.uxuiapp.data.data_class.SubTask
import com.group10.uxuiapp.data.data_class.TaskItem
import com.group10.uxuiapp.data.data_class.TaskItemWithSubTask
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
    suspend fun insertSubTsk(subTask: SubTask) = taskDao.insertSubTask(subTask)

    // Fetch TodoList and Related Tasks
    fun getTodoListsWithTasks(): Flow<List<TodoListWithTaskItem>> = taskDao.getTodoListsWithItems()
    fun getTodoListById(id: Int): Flow<TodoList> = taskDao.getTodoListById(id)
    fun getTodoListWithTaskById(todoListId: Int): Flow<TodoListWithTaskItem> = taskDao.getTodoListWithTaskById(todoListId)
    fun getTaskItemWithSubTask(): Flow<List<TaskItemWithSubTask>> = taskDao.getTaskItemWithSubTask()
    fun getTaskListWithSubTaskById(taskItemId: Int): Flow<TaskItemWithSubTask> = taskDao.getTaskItemWithSubTaskById(taskItemId)

    // Update Operations
    suspend fun updateTodoList(todoList: TodoList, title: String? = null, isLiked: Boolean? = null, gifUrl: String? = null, textColor: String? = null, dueDate: Long? = null, tags: String? = null) {
        val updatedTitle = title ?: todoList.title
        val updatedIsLiked = isLiked ?: todoList.isLiked
        val updatedGifUrl = gifUrl ?: todoList.gifUrl ?: ""
        val updatedTextColor = textColor ?: todoList.textColor
        val updatedDueDate = dueDate ?: todoList.dueDate
        val updatedTags = tags ?: todoList.tags

        Log.d(
            "TaskDataSource",
            "Updating TodoList: id=${todoList.id}, title=$updatedTitle, isLiked=$updatedIsLiked, gifUrl=$updatedGifUrl, textColor=$updatedTextColor, dueDate=$updatedDueDate"
        )

        // Pass non-nullable values to the DAO
        taskDao.updateTodoList(
            id = todoList.id,
            title = updatedTitle,
            isLiked = updatedIsLiked,
            gifUrl = updatedGifUrl,
            textColor = updatedTextColor,
            dueDate = updatedDueDate,
            tags = updatedTags
        )
    }

    suspend fun updateTaskItem(taskItem: TaskItem, label: String? = null, isComplete: Boolean? = null, isFolded: Boolean? = null) {
        val updatedLabel = label ?: taskItem.label
        val updatedIsComplete = isComplete ?: taskItem.isComplete
        val updateIsFolded = isFolded ?: taskItem.isFolded

        Log.d("TaskDataSource", "Updating TaskItem: id=${taskItem.id}, label=$updatedLabel, isComplete=$updatedIsComplete")

        taskDao.updateTaskItem(
            id = taskItem.id,
            label = updatedLabel,
            isComplete = updatedIsComplete,
            isFolded = updateIsFolded
        )
    }

    suspend fun updateSubtask(subTask: SubTask, label: String? = null, isComplete: Boolean? = null) {
        val updatedLabel = label ?: subTask.label
        val updatedIsComplete = isComplete ?: subTask.isComplete

        Log.d("TaskDataSource", "Updating TaskItem: id=${subTask.id}, label=$updatedLabel, isComplete=$updatedIsComplete")

        taskDao.updateSubTask(
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

    fun getTodoListsWithDueDates() : Flow<List<TodoList>> = taskDao.getTodoListsWithDueDates()

    fun getTodoListsDueBefore(timestamp: Long): Flow<List<TodoList>> = taskDao.getTodoListsDueBefore(timestamp)

    suspend fun deleteSubTask(subTask: SubTask) = taskDao.deleteSubTask(subTask)
}
