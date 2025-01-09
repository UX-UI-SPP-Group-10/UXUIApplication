package com.group10.uxuiapp.data

import androidx.room.*
import coil.request.Tags
import com.group10.uxuiapp.data.data_class.TaskItem
import com.group10.uxuiapp.data.data_class.TodoList
import com.group10.uxuiapp.data.data_class.TodoListWithTaskItem
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTodoList(todoList: TodoList): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTaskItem(taskItem: TaskItem): Long

    @Query("SELECT * FROM TodoList WHERE id = :id")
    fun getTodoListById(id: Int): Flow<TodoList>

    @Query("SELECT * FROM TaskItem WHERE todoListId = :todoListId")
    fun getTaskItemsByListId(todoListId: Int): Flow<List<TaskItem>>

    @Transaction
    @Query("SELECT * FROM TodoList")
    fun getTodoListsWithItems(): Flow<List<TodoListWithTaskItem>>

    @Transaction
    @Query("SELECT * FROM TodoList WHERE id = :todoListId")
    fun getTodoListWithTaskById(todoListId: Int): Flow<TodoListWithTaskItem>

    @Query("""
    UPDATE TodoList
    SET 
        title = :title,
        isLiked = :isLiked,
        gifUrl = :gifUrl,
        dueDate = :dueDate,
        textColor = :textColor,
        tags = :tags
    WHERE id = :id
""")
    suspend fun updateTodoList(
        id: Int,
        title: String,
        isLiked: Boolean,
        gifUrl: String,
        textColor: String,
        dueDate: Long?,
        tags: String?
    )

    @Query("""
    UPDATE TodoList
    SET listIndex = :listIndex
    WHERE id = :todoListId
""")
    suspend fun updateListIndex(
        todoListId: Int,
        listIndex: Int
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

    @Query("""
        DELETE FROM TaskItem WHERE id = :taskId
    """)
    suspend fun deleteTaskItemById(taskId: Int)

    @Query("""
        DELETE FROM TaskItem WHERE todoListId = :todoListId
    """)
    suspend fun deleteTasksByTodoListId(todoListId: Int)

    @Query("SELECT * FROM TaskItem WHERE isComplete = :isComplete")
    fun getTasksByCompletionStatus(isComplete: Boolean): Flow<List<TaskItem>>

    @Delete
    suspend fun deleteTodoList(todoList: TodoList)

    @Delete
    suspend fun deleteTaskItem(taskItem: TaskItem)

    @Query("SELECT * FROM TodoList WHERE dueDate IS NOT NULL ORDER BY dueDate ASC")
    fun getTodoListsWithDueDates(): Flow<List<TodoList>>

    @Query("SELECT * FROM TodoList WHERE dueDate <= :timestamp ORDER BY dueDate ASC")
    fun getTodoListsDueBefore(timestamp: Long): Flow<List<TodoList>>
}
