package com.group10.uxuiapp.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTaskList(taskList: TaskList): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTaskItem(taskItem: TaskItem)

    @Query("SELECT * FROM TaskList WHERE id = :id")
    fun getTaskListById(id: Int): Flow<TaskList>

    @Query("SELECT * FROM TaskItem WHERE taskListId = :taskListId")
    fun getTaskItemsByListId(taskListId: Int): Flow<List<TaskItem>>

    @Transaction
    @Query("SELECT * FROM TaskList")
    fun getTaskListsWithItems(): Flow<List<TaskListWithItems>>

    @Delete
    suspend fun deleteTaskList(taskList: TaskList)

    @Delete
    suspend fun deleteTaskItem(taskItem: TaskItem)
}