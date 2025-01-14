package com.group10.uxuiapp.data

import android.content.BroadcastReceiver

import android.content.Context
import android.content.Intent
import android.util.Log
import timber.log.Timber


class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.d("NotificationTest","ReminderReceiver triggered")

        val title = intent.getStringExtra("title") ?: "Task Reminder"
        val message = intent.getStringExtra("message") ?: "Your task is due soon!"

        val notificationHelper = NotificationHelper(context)
        notificationHelper.sendNotification(title, message)
    }
}