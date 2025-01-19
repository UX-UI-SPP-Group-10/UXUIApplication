package com.group10.uxuiapp.data

import android.util.Log
import com.group10.uxuiapp.data.data_class.SubTask
import com.group10.uxuiapp.data.data_class.TaskItem
import com.group10.uxuiapp.data.data_class.TaskItemWithSubTask
import com.group10.uxuiapp.data.data_class.TodoList
import com.group10.uxuiapp.data.data_class.TodoListWithTaskItem
import kotlinx.coroutines.flow.Flow
import java.util.Calendar

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
    suspend fun updateTodoList(
        todoListId: Int,
        title: String? = null,
        isLiked: Boolean? = null,
        gifUrl: String? = null,
        textColor: String? = null,
        dueDate: Long? = null,
        tags: String? = null,
        repeatDay: Int? = null,
        isRepeating: Boolean? = null,
        backgroundColor: String? = null
    ) {
        // Pass non-nullable values to the DAO
        taskDao.updateTodoList(
            todoListId = todoListId,
            title = title,
            isLiked = isLiked,
            gifUrl = gifUrl,
            textColor = textColor,
            dueDate = dueDate,
            tags = tags,
            repeatDay = repeatDay,
            isRepeating = isRepeating,
            backgroundColor = backgroundColor
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

    suspend fun updateAllIndexes(updatedIndexes: List<Pair<Int, Int>>) {
        taskDao.updateAllListIndexes(updatedIndexes)
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

    suspend fun updateDueDate(todoListId: Int, dueDate: Long?, todoListTitle: String) {
        Log.d("NotificationTest","updateDueDate called with ID: $todoListId and dueDate: $dueDate")
        taskDao.updateDueDate(todoListId, dueDate)
    }

    suspend fun resetTaskForRepeat(){
        val currentDayOfTheWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
        Log.d("TaskDataSource", "Current day of the week: $currentDayOfTheWeek")
        val repeatingLists = taskDao.getTodoListByRepeatDay(currentDayOfTheWeek)
        Log.d("TaskDataSource", "Repeating lists count: ${repeatingLists.size}")

        repeatingLists.forEach { list ->
            Log.d("TaskDataSource", "Resetting tasks for list: ${list.title}")
            taskDao.resetTaskByTodoListId(list.id)
            taskDao.resetSubTasksByTodoListId(list.id)
        }
    }

    suspend fun deleteCompletedTasksAndSubTasks(todoListId: Int) {
        Log.d("TaskDataSource", "Deleting completed tasks and subtasks for todoListId=$todoListId")
        taskDao.deleteCompletedSubTasksByTodoListId(todoListId)
        taskDao.deleteCompletedTasksByTodoListId(todoListId)
    }

    suspend fun deleteAllTodoLists() = taskDao.deleteAllTodoLists()



    suspend fun deleteSubTask(subTask: SubTask) = taskDao.deleteSubTask(subTask)
}
