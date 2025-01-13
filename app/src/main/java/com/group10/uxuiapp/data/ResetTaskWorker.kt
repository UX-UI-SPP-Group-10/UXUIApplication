package com.group10.uxuiapp.data

import android.content.Context
import android.util.Log
import androidx.test.espresso.core.internal.deps.dagger.assisted.Assisted
import androidx.test.espresso.core.internal.deps.dagger.assisted.AssistedInject
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class ResetTaskWorker(
    context: Context,
    workerParams: WorkerParameters,
) : CoroutineWorker(context, workerParams) {

    private val taskDataSource: TaskDataSource by lazy {
        val database = DatabaseProvider.getDatabase(context)
        TaskDataSource(database.taskDao())
    }

    override suspend fun doWork(): Result {
        Timber.tag("ResetTaskWorker").d("Worker is running...")
        return try {
            taskDataSource.resetTaskForRepeat() // Your reset logic here
            Timber.tag("ResetTaskWorker").d("Worker ran successfully")
            Result.success()
        } catch (e: Exception) {
            Timber.tag("ResetTaskWorker").d("Worker didnt run successfully")
            Result.failure()
        }
    }
}