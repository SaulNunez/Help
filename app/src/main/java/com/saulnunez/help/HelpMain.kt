package com.saulnunez.help

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_help_main.*

class HelpMain : AppCompatActivity() {
    var soundAlarm : Boolean = false
    var locationNotifications = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help_main)

        buttonSoundAlarm.setOnClickListener {
            soundAlarm != soundAlarm
        }

        buttonLocationNotifications.setOnClickListener {
            locationNotifications != locationNotifications
        }
    }
}
