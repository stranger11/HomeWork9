package com.example.homework9

import android.content.Context
import android.os.Environment
import android.os.StatFs
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters

class MemoryWorker(context: Context, workerParameters: WorkerParameters) :
        Worker(context, workerParameters) {

    override fun doWork(): Result {
        createNotification()
        return Result.success()
    }

    private fun createNotification() {
        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                .setContentTitle("Memory")
                .setContentText("Available memory: " + getAvailableInternalMemorySize()+" MB")
                .setSmallIcon(R.drawable.icon_memory)
                .setPriority(NotificationCompat.PRIORITY_HIGH)

        with(NotificationManagerCompat.from(applicationContext)) {
            notify(0, notification.build())
        }
    }

    private fun getAvailableInternalMemorySize(): String {
        val stat = StatFs(Environment.getDataDirectory().path)
        val bytesAvailable: Long =
                stat.blockSizeLong * stat.availableBlocksLong
        val megAvailable = bytesAvailable / (1024 * 1024)
        return megAvailable.toString()
    }
}