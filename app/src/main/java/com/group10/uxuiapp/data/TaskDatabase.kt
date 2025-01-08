package com.group10.uxuiapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.group10.uxuiapp.data.data_class.TaskItem
import com.group10.uxuiapp.data.data_class.TodoList
//Increment the version number when you make changes to the database
@Database(entities = [TodoList::class, TaskItem::class], version = 6, exportSchema = false)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}
