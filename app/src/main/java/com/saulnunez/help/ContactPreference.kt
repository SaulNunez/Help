package com.saulnunez.help

import android.content.Context
import android.util.AttributeSet
import androidx.preference.DialogPreference

class ContactPreference(context: Context, attrs: AttributeSet?) : DialogPreference(context, attrs) {
    var phoneNumber: String? = null
        set(value) {
            field = value
            persistString(value)
            notifyChanged()
        }

    init {
        dialogLayoutResource = R.layout.contact_preference_dialog
    }

    override fun onSetInitialValue(defaultValue: Any?) {
        phoneNumber = getPersistedString(defaultValue as? String)
    }

    override fun getSummary(): CharSequence? {
        return phoneNumber ?: super.getSummary()
    }
}
