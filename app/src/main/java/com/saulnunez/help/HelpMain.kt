package com.saulnunez.help

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import android.view.View
import com.saulnunez.help.databinding.ActivityHelpMainBinding

class HelpMain : AppCompatActivity() {
    private val requestedPermissions = listOf(
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.SEND_SMS)

    private lateinit var binding: ActivityHelpMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        binding = ActivityHelpMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.topAppBar) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updatePadding(top = systemBars.top)
            insets
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.bottomNavigation) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updatePadding(bottom = systemBars.bottom)
            insets
        }

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
            updateBannerVisibility(false)
        }else{
            updateBannerVisibility(true)
        }

        binding.banner.setLeftButtonAction { updateBannerVisibility(false) }
        binding.banner.setRightButtonAction {
            requestPermissionLauncher.launch(
                requestedPermissions.toTypedArray()
            )
            updateBannerVisibility(false)
        }
    }

    private fun updateBannerVisibility(visible: Boolean) {
        if (visible) {
            binding.banner.show()
            binding.bannerDivider.visibility = View.VISIBLE
        } else {
            binding.banner.dismiss()
            binding.bannerDivider.visibility = View.GONE
        }
    }

    private fun hasAllPermissions(): Boolean {
        return requestedPermissions
            .all { ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED }
    }
}
