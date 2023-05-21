package com.saulnunez.help

import android.Manifest
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.IBinder
import android.telephony.SmsManager
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*


class HelpLocationService : Service() {
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var locationCallback: LocationCallback? = null
    override fun onCreate() {
        super.onCreate()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                val location = locationResult.lastLocation
                location?.let { sendLocationViaSMS(it) }
            }
        }
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        startLocationUpdates()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onDestroy() {
        stopLocationUpdates()
        super.onDestroy()
    }

    private fun startLocationUpdates() {
        val locationRequest = LocationRequest.create()
        locationRequest.interval = INTERVAL
        locationRequest.fastestInterval = FASTEST_INTERVAL
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient!!.requestLocationUpdates(locationRequest, locationCallback!!, null)
    }

    private fun stopLocationUpdates() {
        fusedLocationClient!!.removeLocationUpdates(locationCallback!!)
    }

    private fun sendLocationViaSMS(location: Location) {
        val message = "Latitude: " + location.latitude + ", Longitude: " + location.longitude
        val smsManager = SmsManager.getDefault()
        smsManager.sendTextMessage(PHONE_NUMBER, null, message, null, null)
    }

    companion object {
        private const val INTERVAL = (1000 * 60 * 15 // 15 minutes
                ).toLong()
        private const val FASTEST_INTERVAL = (1000 * 60 * 5 // 5 minutes
                ).toLong()
        private const val PHONE_NUMBER = "your_phone_number_here"
    }
}
