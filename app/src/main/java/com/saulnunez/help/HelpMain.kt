package com.saulnunez.help

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_help_main.*

class HelpMain : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help_main)

        bottom_navigation.setOnItemSelectedListener {
            when(it.itemId){
                R.id.location_page -> {
                    supportFragmentManager.beginTransaction()
                            .add(R.id.fragment_container_view, LocationFragment())
                            .commit()
                    true
                }
                R.id.sound_page -> {
                    supportFragmentManager.beginTransaction()
                            .add(R.id.fragment_container_view, SoundFragment())
                            .commit()
                    true
                }
                else -> false
            }
        }

        setSupportActionBar(topAppBar)
    }
}
