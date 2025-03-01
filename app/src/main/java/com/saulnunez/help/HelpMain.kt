package com.saulnunez.help

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.saulnunez.help.databinding.ActivityHelpMainBinding

class HelpMain : AppCompatActivity() {
    private lateinit var binding: ActivityHelpMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHelpMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavigation.setOnItemSelectedListener {
            when(it.itemId){
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

        setSupportActionBar(binding.topAppBar)
    }
}
