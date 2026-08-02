package com.saulnunez.help

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

class HelpRepository(context: Context) {
    private val sharedPreferences: SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(context)

    companion object {
        private const val KEY_PHONE_NUMBER = "phone_number"
        private const val KEY_ALARM_ENABLED = "alarm_enabled"
        private const val KEY_LOCATION_ENABLED = "location_enabled"
    }

    var phoneNumber: String?
        get() = sharedPreferences.getString(KEY_PHONE_NUMBER, null)
        set(value) = sharedPreferences.edit().putString(KEY_PHONE_NUMBER, value).apply()

    var isAlarmEnabled: Boolean
        get() = sharedPreferences.getBoolean(KEY_ALARM_ENABLED, false)
        set(value) = sharedPreferences.edit().putBoolean(KEY_ALARM_ENABLED, value).apply()

    var isLocationEnabled: Boolean
        get() = sharedPreferences.getBoolean(KEY_LOCATION_ENABLED, false)
        set(value) = sharedPreferences.edit().putBoolean(KEY_LOCATION_ENABLED, value).apply()
}
