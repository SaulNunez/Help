package com.saulnunez.help

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.preference.PreferenceFragmentCompat

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.settings)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updatePadding(
                top = systemBars.top,
                left = systemBars.left,
                right = systemBars.right,
                bottom = systemBars.bottom
            )
            insets
        }
        if (savedInstanceState == null) {
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.settings, SettingsFragment())
                    .commit()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
        }

        override fun onDisplayPreferenceDialog(preference: androidx.preference.Preference) {
            if (preference is ContactPreference) {
                if (parentFragmentManager.findFragmentByTag("androidx.preference.PreferenceFragment.DIALOG") == null) {
                    val dialogFragment = ContactPreferenceDialogFragmentCompat.newInstance(preference.key)
                    @Suppress("DEPRECATION")
                    dialogFragment.setTargetFragment(this, 0)
                    dialogFragment.show(parentFragmentManager, "androidx.preference.PreferenceFragment.DIALOG")
                }
            } else {
                super.onDisplayPreferenceDialog(preference)
            }
        }
    }
}