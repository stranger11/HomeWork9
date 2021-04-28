package com.example.homework9

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters

class BatteryWorker(context: Context, workerParameters: WorkerParameters) :
    Worker(context, workerParameters) {

    override fun doWork(): Result {
        notificationCreate()
        return Result.success()
    }

    private fun notificationCreate() {
        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setContentTitle("Battery Charge")
            .setContentText("The charge is " + levelOfBattery())
            .setSmallIcon(R.drawable.icon_battery)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
        with(NotificationManagerCompat.from(applicationContext)) {
            notify(1, notification.build())
        }
    }

    private fun levelOfBattery(): String {
        val intent: Intent? =
            applicationContext.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        val level = intent?.getIntExtra(BatteryManager.EXTRA_LEVEL, 0)
        val scale = intent?.getIntExtra(BatteryManager.EXTRA_SCALE, 100)
        val percent = level!! * 100 / scale!!
        return "$percent%"
    }
}