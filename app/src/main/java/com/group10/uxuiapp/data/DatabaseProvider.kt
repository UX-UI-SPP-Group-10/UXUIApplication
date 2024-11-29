package com.group10.uxuiapp.data

import android.content.Context
import androidx.room.Room
import com.group10.uxuiapp.data.TaskDatabase

object DatabaseProvider {
    private var instance: TaskDatabase? = null

    fun getDatabase(context: Context): TaskDatabase {
        if (instance == null) {
            synchronized(TaskDatabase::class) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    TaskDatabase::class.java,
                    "task_database"
                ).build()
            }
        }
        return instance!!
    }
}