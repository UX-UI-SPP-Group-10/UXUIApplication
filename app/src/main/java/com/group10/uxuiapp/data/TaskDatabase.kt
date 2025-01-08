package com.group10.uxuiapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.group10.uxuiapp.data.data_class.TaskItem
import com.group10.uxuiapp.data.data_class.TodoList

// Database class for the TaskList app
@Database(entities = [TodoList::class, TaskItem::class], version = 5, exportSchema = false) // Increment version number when schema changes
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}
