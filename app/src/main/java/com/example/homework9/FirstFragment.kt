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
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.*
import java.util.concurrent.TimeUnit

class FirstFragment : Fragment() {

    private lateinit var memoryButton: Button
    private lateinit var batteryButton: Button
    private lateinit var locButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_first, container, false)
        memoryButton = root.findViewById(R.id.memory_button)
        batteryButton = root.findViewById(R.id.battery_button)
        locButton = root.findViewById(R.id.loc_button)

        memoryButton.setOnClickListener {

            val storageAvailable =
                    PeriodicWorkRequestBuilder<MemoryWorker>(
                            1,
                            TimeUnit.HOURS).build()

            WorkManager
                    .getInstance(requireContext())
                    .enqueue(storageAvailable)
        }

        batteryButton.setOnClickListener {
            val batteryInfo =
                    PeriodicWorkRequestBuilder<BatteryWorker>(
                            1,
                            TimeUnit.HOURS).build()

            WorkManager
                    .getInstance(requireContext())
                    .enqueue(batteryInfo)
        }

        locButton.setOnClickListener {
            val cal = Calendar.getInstance()
            val intent = Intent(requireContext(), LocationService::class.java)
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