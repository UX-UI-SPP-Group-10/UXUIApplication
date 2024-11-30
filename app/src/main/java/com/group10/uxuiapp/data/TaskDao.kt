package com.group10.uxuiapp.data

import androidx.room.*

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTaskList(taskList: TaskList): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTaskItem(taskItem: TaskItem)

    @Transaction
    @Query("SELECT * FROM TaskList")
    suspend fun getTaskListsWithItems(): List<TaskListWithItems>

    @Delete
    suspend fun deleteTaskList(taskList: TaskList)

    @Delete
    suspend fun deleteTaskItem(taskItem: TaskItem)
}