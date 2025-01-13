package com.group10.uxuiapp.data

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters

class TaskWorkerFactory (
    private val taskDataSource: TaskDataSource
) : WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ResetTaskWorker? {
        return when (workerClassName) {
            ResetTaskWorker::class.java.name ->
                ResetTaskWorker(appContext, workerParameters, taskDataSource)
            else -> null
        }
    }
}