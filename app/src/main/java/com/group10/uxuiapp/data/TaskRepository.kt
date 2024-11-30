package com.group10.uxuiapp.data

class TaskRepository(private val taskDao: TaskDao) {
    suspend fun insertTaskList(taskList: TaskList) = taskDao.insertTaskList(taskList)
    suspend fun insertTaskItem(taskItem: TaskItem) = taskDao.insertTaskItem(taskItem)
    suspend fun getTaskListsWithItems() = taskDao.getTaskListsWithItems()
    suspend fun deleteTaskList(taskList: TaskList) = taskDao.deleteTaskList(taskList)
    suspend fun deleteTaskItem(taskItem: TaskItem) = taskDao.deleteTaskItem(taskItem)
}