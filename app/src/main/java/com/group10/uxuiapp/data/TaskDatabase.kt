package com.group10.uxuiapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.group10.uxuiapp.data.data_class.TaskItem
import com.group10.uxuiapp.data.data_class.TodoList

@Database(entities = [TodoList::class, TaskItem::class], version = 3, exportSchema = false)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}
