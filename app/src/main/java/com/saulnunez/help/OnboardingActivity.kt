package com.saulnunez.help

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding

class OnboardingActivity : AppCompatActivity() {
    private lateinit var repository: HelpRepository
    private lateinit var editPhoneNumber: EditText

    private val pickContactLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                result.data?.data?.let { getPhoneNumberFromUri(it) }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        repository = HelpRepository(this)

        if (repository.isOnboardingCompleted) {
            goToMainActivity()
            return
        }

        setContentView(R.layout.activity_onboarding)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.onboarding_root)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updatePadding(
                top = systemBars.top,
                left = systemBars.left,
                right = systemBars.right,
                bottom = systemBars.bottom
            )
            insets
        }

        editPhoneNumber = findViewById(R.id.edit_phone_number)

        findViewById<android.view.View>(R.id.btn_pick_contact).setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI)
            pickContactLauncher.launch(intent)
        }

        findViewById<android.view.View>(R.id.btn_get_started).setOnClickListener {
            val phoneNumber = editPhoneNumber.text.toString().trim()
            if (phoneNumber.isEmpty()) {
                editPhoneNumber.error = getString(R.string.phone_number_required)
                return@setOnClickListener
            }
            repository.phoneNumber = phoneNumber
            completeOnboarding()
        }

        findViewById<android.view.View>(R.id.btn_skip).setOnClickListener {
            completeOnboarding()
        }
    }

    private fun getPhoneNumberFromUri(contactUri: Uri) {
        val context: Context = this
        val projection = arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER)
        val cursor = context.contentResolver.query(contactUri, projection, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val phoneNumber = it.getString(it.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER))
                editPhoneNumber.setText(phoneNumber)
            }
        }
    }

    private fun completeOnboarding() {
        repository.isOnboardingCompleted = true
        goToMainActivity()
    }

    private fun goToMainActivity() {
        startActivity(Intent(this, HelpMain::class.java))
        finish()
    }
}