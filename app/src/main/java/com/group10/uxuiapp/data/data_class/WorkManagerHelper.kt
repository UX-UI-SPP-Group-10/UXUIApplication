package com.group10.uxuiapp.data.data_class

import android.annotation.SuppressLint
import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.group10.uxuiapp.data.ResetTaskWorker
import java.time.Duration

object WorkManagerHelper {

    @SuppressLint("NewApi")
    fun scheduleResetWorker(context: Context) {
        val resetWorkRequest = PeriodicWorkRequestBuilder<ResetTaskWorker>(
            Duration.ofDays(1)
        ).build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "resetTasksWorker",
            ExistingPeriodicWorkPolicy.KEEP,
            resetWorkRequest
        )
    }
}