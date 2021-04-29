package com.example.homework9

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.work.OneTimeWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.*
import java.util.concurrent.TimeUnit

class FirstFragment : Fragment() {

    private lateinit var locButton: Button
    private lateinit var batteryAndMemoryButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_first, container, false)
        locButton = root.findViewById(R.id.loc_button)
        batteryAndMemoryButton = root.findViewById(R.id.battery_memory_button)

        batteryAndMemoryButton.setOnClickListener {
            val storage = OneTimeWorkRequest.from(MemoryWorker::class.java)
            val battery = OneTimeWorkRequest.from(BatteryWorker::class.java)
            WorkManager
                    .getInstance(requireContext())
                    .beginWith(storage)
                    .then(battery)
                    .enqueue()
        }

        locButton.setOnClickListener {
            val cal = Calendar.getInstance()
            val intent = Intent(context, LocationService::class.java)
            if (context != null) {
                requireContext().startService(intent)
            }
            val pendingIntent = PendingIntent.getService(
                    requireContext(),
                    0,
                    intent,
                    0)
            val alarm = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarm.setInexactRepeating(
                    AlarmManager.RTC_WAKEUP,
                    cal.timeInMillis,
                    30000,
                    pendingIntent)
        }
        return root
    }
}