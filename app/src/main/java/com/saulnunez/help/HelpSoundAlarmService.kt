package com.saulnunez.help

import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.content.edit

class HelpSoundAlarmService: Service() {
    private val notificationId = 1
    private val soundAlarmEnabled = "sound_alarm_enabled"

    private val binder = SoundAlarmBinder()
    private var mediaPlayer: MediaPlayer? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val pendingIntent: PendingIntent =
                Intent(this, HelpMain::class.java).let { notificationIntent ->
                    PendingIntent.getActivity(this, 0, notificationIntent, 0)
                }

        val notification = NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_baseline_notifications_active_24)
                .setContentIntent(pendingIntent)
                .build()

        startForeground(notificationId, notification)
        return super.onStartCommand(intent, flags, startId)
    }

    inner class SoundAlarmBinder: Binder() {
        val service: HelpSoundAlarmService
            get() = this@HelpSoundAlarmService
    }

    override fun onBind(p0: Intent?): IBinder = binder

    fun playAlarm(){
        val sharedPrefs = getSharedPreferences("current_state", Context.MODE_PRIVATE)
        sharedPrefs.edit {
            this.putBoolean(soundAlarmEnabled, true)
        }
        try {
            //TODO: Find audio and load from resources
            //mediaPlayer = MediaPlayer.create(applicationContext, R.raw.sound_alarm_1)
            mediaPlayer?.start()
            // TODO: Repeat
        } catch (e: Exception){
            e.printStackTrace()
        }
    }

    fun stopAlarm(){
        val sharedPrefs = getSharedPreferences("current_state", Context.MODE_PRIVATE)
        sharedPrefs.edit {
            this.putBoolean(soundAlarmEnabled, false)
        }
        mediaPlayer?.stop()
    }
}