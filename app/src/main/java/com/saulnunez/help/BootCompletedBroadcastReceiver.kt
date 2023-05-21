package com.saulnunez.help

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build

class BootCompletedBroadcastReceiver : BroadcastReceiver(){
    override fun onReceive(context: Context?, intent: Intent?) {
        if(intent?.action == Intent.ACTION_BOOT_COMPLETED){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context?.startForegroundService(Intent(context, HelpLocationService::class.java))
            } else {
                context?.startService(Intent(context, HelpLocationService::class.java))
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context?.startForegroundService(Intent(context, HelpSoundAlarmService::class.java))
            } else {
                context?.startService(Intent(context, HelpSoundAlarmService::class.java))
            }
        }
    }
}
