package com.saulnunez.help

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.IBinder
import android.telephony.SmsManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat


class HelpLocationService : Service(), LocationListener {
    private lateinit var locationManager: LocationManager

    override fun onCreate() {
        super.onCreate()
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
        val stopPendingIntent = PendingIntent.getService(this, 0, stopIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Location Tracking")
            .setContentText("Tracking device location...")
            .setSmallIcon(android.R.drawable.ic_menu_mylocation)
            //.addAction(android.R.drawable.ic_delete, "Stop", stopPendingIntent)
            .setOngoing(true)
            .build()

        startForeground(1, notification)
    }

    @SuppressLint("MissingPermission")
    private fun requestLocationUpdates() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60 * 1000, 200f, this)
        }
    }

    override fun onLocationChanged(location: Location) {
        sendLocationSms(location)
    }

    private fun getPhoneNumber(): String {
        val sharedPreferences = getSharedPreferences("phone_number", Context.MODE_PRIVATE)
        return sharedPreferences.getString("phone_number", "") ?: ""
    }

    private fun createLocationUrl(location: Location): String {
        val builder = Uri.Builder()
        builder.scheme("https")
            .authority("www.google.com")
            .appendPath("maps")
            .appendPath("search")
            .appendQueryParameter("api", "1")
            .appendQueryParameter("query", "${location.latitude},${location.longitude}")
        return builder.build().toString()
    }

    private fun sendLocationSms(location: Location) {
        try {
            val smsManager = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                applicationContext.getSystemService(SmsManager::class.java)
            } else {
                SmsManager.getDefault()
            }
            val message = "Location Update: Lat=${location.latitude}, Lng=${location.longitude}\n${createLocationUrl(location)}"
            smsManager.sendTextMessage(getPhoneNumber(), null, message, null, null)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == "STOP_SERVICE") {
            stopSelf()
        }
        return START_STICKY
    }

    override fun onDestroy() {
        locationManager.removeUpdates(this)
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}
