package com.demo.dhiwise

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar

class LoginScreen : AppCompatActivity() {

    private lateinit var editMobileNumber: EditText
    private lateinit var btnContinue: MaterialButton
    private val prefix = "+1"
    private var isPrefixShown = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login_screen)

        editMobileNumber = findViewById(R.id.edit_mobile_number)
        btnContinue = findViewById(R.id.btn_continue)

        editMobileNumber.hint = "Mobile Number"
        editMobileNumber.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                if (!isPrefixShown) {
                    editMobileNumber.setText(prefix)
                    editMobileNumber.setSelection(prefix.length)
                    isPrefixShown = true
                }
            } else {
                isPrefixShown = false
            }
        }

        editMobileNumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString() != prefix && !s.toString().startsWith(prefix)) {
                    editMobileNumber.setText(prefix)
                    editMobileNumber.setSelection(prefix.length)
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        btnContinue.setOnClickListener {
            val phoneNumber = editMobileNumber.text.toString().trim()
            if (phoneNumber.isNotEmpty() && phoneNumber.startsWith(prefix)) {
                showSnackbar("One-Time Password(OTP) has been sent successfully") {
                    val fullPhoneNumber = phoneNumber
                    val intent = Intent(this, OtpVerificationScreen::class.java)
                    intent.putExtra("PHONE_NUMBER", fullPhoneNumber)
                    startActivity(intent)
                }
            } else {
                editMobileNumber.error = "Please enter your mobile number"
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun showSnackbar(message: String, onDismissed: () -> Unit) {
        val snackbar = Snackbar.make(findViewById(R.id.main), message, Snackbar.LENGTH_SHORT)
            .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
            .setDuration(1500)
            .setBackgroundTint(Color.parseColor("#5FB21A"))

        snackbar.addCallback(object : Snackbar.Callback() {
            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                super.onDismissed(transientBottomBar, event)
                onDismissed()
            }
        })

        snackbar.show()
    }
}
