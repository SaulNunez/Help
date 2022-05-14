package com.saulnunez.help

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.commit
import kotlinx.android.synthetic.main.activity_help_main.*
import androidx.fragment.app.replace

class HelpMain : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help_main)

        bottom_navigation.setOnItemSelectedListener {
            when(it.itemId){
                R.id.location_page -> {
                    supportFragmentManager.
                    commit {
                        setReorderingAllowed(true)
                        replace<LocationFragment>(R.id.fragment_container_view)
                    }
                    true
                }
                R.id.sound_page -> {
                    supportFragmentManager.commit {
                        setReorderingAllowed(true)
                        replace<SoundFragment>(R.id.fragment_container_view)
                    }
                    true
                }
                R.id.settings_page -> {
                    supportFragmentManager.commit {
                        setReorderingAllowed(true)
                        replace<SettingsActivity.SettingsFragment>(R.id.fragment_container_view)
                    }
                    true
                }
                else -> false
            }
        }

        setSupportActionBar(topAppBar)
    }
}
