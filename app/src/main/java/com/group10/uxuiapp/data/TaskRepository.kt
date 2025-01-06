package com.group10.uxuiapp.data

import com.group10.uxuiapp.data.data_class.TaskItem
import com.group10.uxuiapp.data.data_class.TaskList
import com.group10.uxuiapp.data.data_class.TaskListWithItems
import kotlinx.coroutines.flow.Flow

class TaskRepository(private val taskDao: TaskDao) {
    suspend fun insertTaskList(taskList: TaskList) = taskDao.insertTaskList(taskList)
    suspend fun insertTaskItem(taskItem: TaskItem) = taskDao.insertTaskItem(taskItem)
    fun getTaskListsWithItems(): Flow<List<TaskListWithItems>> = taskDao.getTaskListsWithItems()
    fun getTaskListById(id: Int): Flow<TaskList> = taskDao.getTaskListById(id)
    fun getTaskItemsByListId(taskListId: Int): Flow<List<TaskItem>> = taskDao.getTaskItemsByListId(taskListId)
    suspend fun updateTaskList(taskList: TaskList, title: String? = null, isLiked: Boolean? = null) {
        taskDao.updateTaskList(taskList.id, title, isLiked)
    }
    suspend fun updateTaskItem(taskItem: TaskItem, label: String? = null, isComplete: Boolean? = null) {
        taskDao.updateTaskItem(
            id = taskItem.id,
            label = label,
            isComplete = isComplete
        )
    }
    suspend fun updateGifUrl(taskListId: Int, gifUrl: String) {
        taskDao.updateGifUrl(taskListId, gifUrl)
    }

    suspend fun deleteTaskList(taskList: TaskList) = taskDao.deleteTaskList(taskList)
    suspend fun deleteTaskItem(taskItem: TaskItem) = taskDao.deleteTaskItem(taskItem)
}
