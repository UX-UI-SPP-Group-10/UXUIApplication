package com.group10.uxuiapp.data

import androidx.room.*
import com.group10.uxuiapp.data.data_class.TaskItem
import com.group10.uxuiapp.data.data_class.TaskList
import com.group10.uxuiapp.data.data_class.TaskListWithItems
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

    @Query("""
        UPDATE TaskList
        SET 
            title = COALESCE(:title, title),
            isLiked = COALESCE(:isLiked, isLiked)
        WHERE id = :id
    """)
    suspend fun updateTaskList(
        id: Int,
        title: String? = null,
        isLiked: Boolean? = null
    )

    @Query("""
        UPDATE TaskItem
        SET 
            label = COALESCE(:label, label),
            isComplete = COALESCE(:isComplete, isComplete)
        WHERE id = :id
    """)
    suspend fun updateTaskItem(
        id: Int,
        label: String? = null,
        isComplete: Boolean? = null
    )

    @Delete
    suspend fun deleteTaskList(taskList: TaskList)

    @Delete
    suspend fun deleteTaskItem(taskItem: TaskItem)
}