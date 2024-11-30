package com.group10.uxuiapp.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [TaskList::class, TaskItem::class], version = 1, exportSchema = false)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}
