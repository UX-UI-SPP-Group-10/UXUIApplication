package com.group10.uxuiapp.data

import kotlinx.coroutines.flow.Flow

class TaskRepository(private val taskDao: TaskDao) {
    suspend fun insertTaskList(taskList: TaskList) = taskDao.insertTaskList(taskList)
    suspend fun insertTaskItem(taskItem: TaskItem) = taskDao.insertTaskItem(taskItem)
    fun getTaskListsWithItems(): Flow<List<TaskListWithItems>> = taskDao.getTaskListsWithItems()
    suspend fun deleteTaskList(taskList: TaskList) = taskDao.deleteTaskList(taskList)
    suspend fun deleteTaskItem(taskItem: TaskItem) = taskDao.deleteTaskItem(taskItem)
}
