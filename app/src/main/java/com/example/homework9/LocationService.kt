package com.example.homework9

import android.Manifest
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.location.*
import android.os.IBinder
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import java.util.*

class LocationService : Service() {

    private lateinit var locationManager: LocationManager

    override fun onStartCommand(
            intent: Intent?,
            flags: Int,
            startId: Int
    ): Int {
        locationManager = this.getSystemService(LocationManager::class.java)
        checkPermission()
        return START_NOT_STICKY
    }

    private fun checkPermission() {
        val locationListener: LocationListener =
                LocationListener { location ->
                    showLocation(location)
                }
        if (ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    MainActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) && ActivityCompat.shouldShowRequestPermissionRationale(
                    MainActivity(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            ) {
                ActivityCompat.requestPermissions(
                    MainActivity(),
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ), LOCATION_REQUEST
                )
            } else {
                ActivityCompat.requestPermissions(
                    MainActivity(),
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ), LOCATION_REQUEST
                )
            }
        } else {
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                30000,
                2f,
                locationListener
            )
            locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                30000,
                2f,
                locationListener
            )
        }
    }

    private fun createNotification(address: String) {
        val pendingIntent: PendingIntent =
            Intent(this, MainActivity::class.java).let { notificationIntent ->
                PendingIntent.getActivity(
                        this,
                        0,
                        notificationIntent,
                        0
                )
            }
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Location")
            .setContentText(address)
            .setSmallIcon(R.drawable.icon_location)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()
        startForeground(1, notification)
    }

    private fun showLocation(location: Location) {
        if (location.provider.equals(LocationManager.GPS_PROVIDER)) {
            createNotification(getGeo(location))
        } else if (location.provider.equals(
                LocationManager.NETWORK_PROVIDER
            )
        ) {
            createNotification(getGeo(location))
        }
    }

    private fun getGeo(location: Location): String {
        val addresses: List<Address>
        val geoCoder = Geocoder(applicationContext, Locale.getDefault())
        addresses = geoCoder.getFromLocation(location.latitude, location.longitude, 1)
        return addresses[0].getAddressLine(0).toString()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}