package com.group10.uxuiapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.group10.uxuiapp.data.data_class.TaskItem
import com.group10.uxuiapp.data.data_class.TaskList

@Database(entities = [TaskList::class, TaskItem::class], version = 1 , exportSchema = false)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}
