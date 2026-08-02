package com.saulnunez.help

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.saulnunez.help.databinding.ActivityHelpMainBinding

class HelpMain : AppCompatActivity() {
    private val requestedPermissions = listOf(
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.SEND_SMS,
    )

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
                ActivityResultContracts.RequestMultiplePermissions(),
            ) { map: Map<String, @JvmSuppressWildcards Boolean> ->
                if (map.containsValue(false)) {
                    val permanentlyDenied = requestedPermissions.any {
                        !ActivityCompat.shouldShowRequestPermissionRationale(this, it) &&
                                ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
                    }

                    if (permanentlyDenied) {
                        showSettingsDialog()
                    } else {
                        updateBannerVisibility(true)
                    }
                } else {
                    updateBannerVisibility(false)
                }
            }

        if(hasAllPermissions()){
            updateBannerVisibility(false)
        }else{
            updateBannerVisibility(true)
        }

        binding.banner.setLeftButtonAction { updateBannerVisibility(false) }
        binding.banner.setRightButtonAction {
            val showRationale = requestedPermissions.any {
                ActivityCompat.shouldShowRequestPermissionRationale(this, it)
            }

            if (showRationale) {
                showPermissionRationaleDialog {
                    requestPermissionLauncher.launch(requestedPermissions.toTypedArray())
                }
            } else {
                requestPermissionLauncher.launch(requestedPermissions.toTypedArray())
            }
        }
    }

    private fun showPermissionRationaleDialog(onConfirm: () -> Unit) {
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.permission_required_title)
            .setMessage(getString(R.string.permission_explanation_sms) + "\n\n" + getString(R.string.permission_explanation_location))
            .setPositiveButton(R.string.ok) { _, _ -> onConfirm() }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    private fun showSettingsDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.permission_required_title)
            .setMessage(R.string.not_all_permissions_set)
            .setPositiveButton(R.string.open_settings) { _, _ -> openAppSettings() }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", packageName, null)
        }
        startActivity(intent)
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
