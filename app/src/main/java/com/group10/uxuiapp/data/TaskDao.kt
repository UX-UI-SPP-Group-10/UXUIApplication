package com.group10.uxuiapp.data

import androidx.room.*
import com.group10.uxuiapp.data.data_class.SupTask
import com.group10.uxuiapp.data.data_class.TaskItem
import com.group10.uxuiapp.data.data_class.TaskItemWhithSupTask
import com.group10.uxuiapp.data.data_class.TodoList
import com.group10.uxuiapp.data.data_class.TodoListWithTaskItem
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTodoList(todoList: TodoList): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTaskItem(taskItem: TaskItem): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubTask(subTask: SupTask): Long

    @Query("SELECT * FROM TodoList WHERE id = :id")
    fun getTodoListById(id: Int): Flow<TodoList>

    @Query("SELECT * FROM TaskItem WHERE todoListId = :todoListId")
    fun getTaskItemsByListId(todoListId: Int): Flow<List<TaskItem>>

    @Query("SELECT * FROM SupTask WHERE taskItemId = :taskItemId")
    fun getSubTaskByTaskId(taskItemId: Int): Flow<List<SupTask>>

    @Transaction
    @Query("SELECT * FROM TodoList")
    fun getTodoListsWithItems(): Flow<List<TodoListWithTaskItem>>

    @Transaction
    @Query("SELECT * FROM TaskItem")
    fun getTaskItemWithSubTask(): Flow<List<TaskItemWhithSupTask>>

    @Transaction
    @Query("SELECT * FROM TodoList WHERE id = :todoListId")
    fun getTodoListWithTaskById(todoListId: Int): Flow<TodoListWithTaskItem>

    @Transaction
    @Query("SELECT * FROM TaskItem WHERE id = :taskItemId")
    fun getTaskItemWithSubTaskById(taskItemId: Int): Flow<TaskItemWhithSupTask>

    @Query("""
    UPDATE TodoList
    SET 
        title = :title,
        isLiked = :isLiked,
        gifUrl = :gifUrl
    WHERE id = :id
""")
    suspend fun updateTodoList(
        id: Int,
        title: String,
        isLiked: Boolean,
        gifUrl: String
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
            isComplete = COALESCE(:isComplete, isComplete),
            isFoldet = COALESCE(:isFoldet, isfoldet)
        WHERE id = :id
    """)
    suspend fun updateTaskItem(
        id: Int,
        label: String? = null,
        isComplete: Boolean? = null,
        isFoldet: Boolean? = null
    )

    @Query("""
        UPDATE SupTask
        SET 
            label = COALESCE(:label, label),
            isComplete = COALESCE(:isComplete, isComplete)
        WHERE id = :id
    """)
    suspend fun updateSupTask(
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

    @Query("""
        DELETE FROM SUPTASK WHERE taskItemId = :taskItemId
    """)
    suspend fun deleteTasksByTaskItemId(taskItemId: Int)

    @Query("SELECT * FROM TaskItem WHERE isComplete = :isComplete")
    fun getTasksByCompletionStatus(isComplete: Boolean): Flow<List<TaskItem>>


    @Delete
    suspend fun deleteTodoList(todoList: TodoList)

    @Delete
    suspend fun deleteTaskItem(taskItem: TaskItem)

    @Delete
    suspend fun deleteSubTask(subTask: SupTask)
}
