package com.group10.uxuiapp.data

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ResetTaskWorker(
    context: Context,
    workerParams: WorkerParameters,
    private val taskDataSource: TaskDataSource
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            taskDataSource.resetTaskForRepeat() // Your reset logic here
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}