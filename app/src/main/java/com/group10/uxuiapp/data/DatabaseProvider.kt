package com.group10.uxuiapp.data

import android.content.Context
import androidx.room.Room

object DatabaseProvider {
    private var INSTANCE: TaskDatabase? = null

    fun getDatabase(context: Context): TaskDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                TaskDatabase::class.java,
                "task_database"
            )
                .fallbackToDestructiveMigration() // TODO: Remove after development. Add migration strategy for data
                .build()
            INSTANCE = instance
            instance
        }
    }
}