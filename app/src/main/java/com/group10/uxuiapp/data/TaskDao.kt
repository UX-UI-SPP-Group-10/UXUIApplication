package com.group10.uxuiapp.data

import androidx.room.*
import com.group10.uxuiapp.data.data_class.TaskItem
import com.group10.uxuiapp.data.data_class.TodoList
import com.group10.uxuiapp.data.data_class.TodoListWithTaskItem
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTodoList(todoList: TodoList): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTaskItem(taskItem: TaskItem)

    @Query("SELECT * FROM TodoList WHERE id = :id")
    fun getTodoListById(id: Int): Flow<TodoList>

    @Query("SELECT * FROM TaskItem WHERE todoListId = :todoListId")
    fun getTaskItemsByListId(todoListId: Int): Flow<List<TaskItem>>

    @Transaction
    @Query("SELECT * FROM TodoList")
    fun getTodoListsWithItems(): Flow<List<TodoListWithTaskItem>>

    @Query(
        """
        UPDATE TodoList
        SET 
            title = COALESCE(:title, title),
            isLiked = COALESCE(:isLiked, isLiked)
        WHERE id = :id
    """
    )
    suspend fun updateTodoList(
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
    suspend fun deleteTodoList(todoList: TodoList)

    @Delete
    suspend fun deleteTaskItem(taskItem: TaskItem)
}