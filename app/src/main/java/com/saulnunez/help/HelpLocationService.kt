package com.saulnunez.help

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ServiceInfo
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.IBinder
import android.telephony.SmsManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

class HelpLocationService : Service() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var repository: HelpRepository
    private val notificationId = 1

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            for (location in locationResult.locations) {
                sendLocationSms(location)
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        repository = HelpRepository(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        startForegroundService()
        requestLocationUpdates()
    }

    private fun startForegroundService() {
        val channelId = "location_service_channel"
        val channelName = "Location Service"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_LOW)
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }

        val stopIntent = Intent(this, HelpLocationService::class.java).apply {
            action = "STOP_SERVICE"
        }
        val stopPendingIntent = PendingIntent.getService(
            this, 0, stopIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Location Tracking")
            .setContentText("Tracking device location. Tap to stop.")
            .setSmallIcon(android.R.drawable.ic_menu_mylocation)
            .setContentIntent(stopPendingIntent)
            .addAction(android.R.drawable.ic_delete, "Stop", stopPendingIntent)
            .setOngoing(true)
            .build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(notificationId, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION)
        } else {
            startForeground(notificationId, notification)
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            val locationRequest = LocationRequest.Builder(Priority.PRIORITY_BALANCED_POWER_ACCURACY, 60 * 1000)
                .setMinUpdateDistanceMeters(200f)
                .build()

            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, mainLooper)
        } else {
            Log.w("HelpLocationService", "Location permission not granted. Stopping service.")
            stopSelf()
        }
    }

    private fun createLocationUrl(location: Location): String {
        return Uri.Builder()
            .scheme("https")
            .authority("www.google.com")
            .appendPath("maps")
            .appendPath("search")
            .appendQueryParameter("api", "1")
            .appendQueryParameter("query", "${location.latitude},${location.longitude}")
            .build()
            .toString()
    }

    private fun sendLocationSms(location: Location) {
        val phoneNumber = repository.phoneNumber
        if (phoneNumber.isNullOrBlank()) {
            Log.w("HelpLocationService", "No phone number set. Skipping SMS.")
            return
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            Log.w("HelpLocationService", "SMS permission not granted. Skipping SMS.")
            return
        }

        try {
            val smsManager = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getSystemService(SmsManager::class.java)
            } else {
                @Suppress("DEPRECATION")
                SmsManager.getDefault()
            }
            val message = "Location Update: Lat=${location.latitude}, Lng=${location.longitude}\n${createLocationUrl(location)}"
            smsManager.sendTextMessage(phoneNumber, null, message, null, null)
        } catch (e: Exception) {
            Log.e("HelpLocationService", "Error sending SMS", e)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == "STOP_SERVICE") {
            repository.isLocationEnabled = false
            stopSelf()
        }
        return START_STICKY
    }

    override fun onDestroy() {
        if (::fusedLocationClient.isInitialized) {
            fusedLocationClient.removeLocationUpdates(locationCallback)
        }
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}
