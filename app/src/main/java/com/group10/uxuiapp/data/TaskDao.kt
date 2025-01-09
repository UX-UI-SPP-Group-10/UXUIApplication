package com.group10.uxuiapp.data

import androidx.room.*
import com.group10.uxuiapp.data.data_class.SubTask
import com.group10.uxuiapp.data.data_class.TaskItem
import com.group10.uxuiapp.data.data_class.TaskItemWithSubTask
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
    suspend fun insertSubTask(subTask: SubTask): Long

    @Query("SELECT * FROM TodoList WHERE id = :id")
    fun getTodoListById(id: Int): Flow<TodoList>

    @Query("SELECT * FROM TaskItem WHERE todoListId = :todoListId")
    fun getTaskItemsByListId(todoListId: Int): Flow<List<TaskItem>>

    @Query("SELECT * FROM SubTask WHERE taskItemId = :taskItemId")
    fun getSubTaskByTaskId(taskItemId: Int): Flow<List<SubTask>>

    @Transaction
    @Query("SELECT * FROM TodoList")
    fun getTodoListsWithItems(): Flow<List<TodoListWithTaskItem>>

    @Transaction
    @Query("SELECT * FROM TaskItem")
    fun getTaskItemWithSubTask(): Flow<List<TaskItemWithSubTask>>

    @Transaction
    @Query("SELECT * FROM TodoList WHERE id = :todoListId")
    fun getTodoListWithTaskById(todoListId: Int): Flow<TodoListWithTaskItem>

    @Transaction
    @Query("SELECT * FROM TaskItem WHERE id = :taskItemId")
    fun getTaskItemWithSubTaskById(taskItemId: Int): Flow<TaskItemWithSubTask>

    @Query("""
    UPDATE TodoList
    SET 
        title = :title,
        isLiked = :isLiked,
        gifUrl = :gifUrl,
        dueDate = :dueDate,
        textColor = :textColor
    WHERE id = :id
""")
    suspend fun updateTodoList(
        id: Int,
        title: String,
        isLiked: Boolean,
        gifUrl: String,
        textColor: String,
        dueDate: Long?
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
            isFolded = COALESCE(:isFolded, isFolded)
        WHERE id = :id
    """
    )
    suspend fun updateTaskItem(
        id: Int,
        label: String? = null,
        isComplete: Boolean? = null,
        isFolded: Boolean? = null
    )

    @Query("""
        UPDATE SubTask
        SET 
            label = COALESCE(:label, label),
            isComplete = COALESCE(:isComplete, isComplete)
        WHERE id = :id
    """)
    suspend fun updateSubTask(
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
        DELETE FROM SubTask WHERE taskItemId = :taskItemId
    """)
    suspend fun deleteTasksByTaskItemId(taskItemId: Int)

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

    @Delete
    suspend fun deleteSubTask(subTask: SubTask)
}
