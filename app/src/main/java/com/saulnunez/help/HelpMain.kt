package com.saulnunez.help

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.saulnunez.help.databinding.ActivityHelpMainBinding

class HelpMain : AppCompatActivity() {
    private val requestedPermissions = listOf(
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.SEND_SMS)

    private lateinit var binding: ActivityHelpMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHelpMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.topAppBar)

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

        val requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestMultiplePermissions()
            ) { map: Map<String, @JvmSuppressWildcards Boolean> ->
                if (map.containsValue(false)) {
                    // Explain to the user that the feature is unavailable because the
                    // feature requires a permission that the user has denied. At the
                    // same time, respect the user's decision. Don't link to system
                    // settings in an effort to convince the user to change their
                    // decision.
                }
            }

        if(hasAllPermissions()){
            binding.banner.dismiss()
        }else{
            binding.banner.show()
        }

        binding.banner.setLeftButtonAction { binding.banner.dismiss() }
        binding.banner.setRightButtonAction {
            requestPermissionLauncher.launch(
                requestedPermissions.toTypedArray()
            )
            binding.banner.dismiss()
        }
    }

    private fun hasAllPermissions(): Boolean {
        return requestedPermissions
            .all { ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED }
    }
}
