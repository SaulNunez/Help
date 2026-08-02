package com.saulnunez.help

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.view.View
import android.widget.EditText
import androidx.activity.result.contract.ActivityResultContracts
import androidx.preference.PreferenceDialogFragmentCompat

class ContactPreferenceDialogFragmentCompat : PreferenceDialogFragmentCompat() {
    private lateinit var editText: EditText

    private val pickContactLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == android.app.Activity.RESULT_OK) {
            result.data?.data?.let { getPhoneNumberFromUri(it) }
        }
    }

    companion object {
        fun newInstance(key: String): ContactPreferenceDialogFragmentCompat {
            val fragment = ContactPreferenceDialogFragmentCompat()
            val b = Bundle(1)
            b.putString(ARG_KEY, key)
            fragment.arguments = b
            return fragment
        }
    }

    override fun onBindDialogView(view: View) {
        super.onBindDialogView(view)
        editText = view.findViewById(R.id.edit_phone_number)
        val preference = preference as ContactPreference
        editText.setText(preference.phoneNumber)

        view.findViewById<View>(R.id.btn_pick_contact).setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI)
            pickContactLauncher.launch(intent)
        }
    }

    private fun getPhoneNumberFromUri(contactUri: Uri) {
        val context: Context = context ?: return
        val projection = arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER)
        val cursor = context.contentResolver.query(contactUri, projection, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val phoneNumber = it.getString(it.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER))
                editText.setText(phoneNumber)
            }
        }
    }

    override fun onDialogClosed(positiveResult: Boolean) {
        if (positiveResult) {
            val preference = preference as ContactPreference
            val value = editText.text.toString()
            if (preference.callChangeListener(value)) {
                preference.phoneNumber = value
            }
        }
    }
}
