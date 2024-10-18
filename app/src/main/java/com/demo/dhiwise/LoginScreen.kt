package com.demo.dhiwise

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
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
    private lateinit var tvErrorMessage: TextView
    private lateinit var progressBar: ProgressBar
    private val prefix = "+1"
    private var isPrefixShown = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login_screen)

        editMobileNumber = findViewById(R.id.edit_mobile_number)
        btnContinue = findViewById(R.id.btn_continue)
        tvErrorMessage = findViewById(R.id.tv_error_message)
        progressBar = findViewById(R.id.progress_bar)

        editMobileNumber.hint = "Mobile Number"
        editMobileNumber.setOnFocusChangeListener { _, hasFocus ->
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
                tvErrorMessage.visibility = View.GONE
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        btnContinue.setOnClickListener {
            val phoneNumber = editMobileNumber.text.toString().trim()
            if (isValidPhoneNumber(phoneNumber)) {
                hideKeyboard()
                showProgressBar(true)
                showSnackbar("One-Time Password(OTP) has been sent successfully") {
                    val fullPhoneNumber = phoneNumber
                    val intent = Intent(this, OtpVerificationScreen::class.java)
                    intent.putExtra("PHONE_NUMBER", fullPhoneNumber)
                    showProgressBar(false) // Hide progress bar  before starting new activity
                    startActivity(intent)
                }
            } else {
                displayErrorMessage(validPhonenumber())
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun isValidPhoneNumber(phoneNumber: String): Boolean {
        return phoneNumber.isNotEmpty() && phoneNumber.startsWith(prefix) && phoneNumber.length == 12
    }

    private fun displayErrorMessage(message: String?) {
        if (message != null) {
            tvErrorMessage.text = message
            tvErrorMessage.visibility = View.VISIBLE
        }
    }

    private fun validPhonenumber(): String? {
        val phoneNumber = editMobileNumber.text.toString().trim()
        return when {
            phoneNumber.isEmpty() -> "Please enter a valid phone number"
            phoneNumber.length < 12 -> "Enter Valid Phone Number"
            else -> null
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

    private fun showProgressBar(show: Boolean) {
        progressBar.visibility = if (show) View.VISIBLE else View.GONE
        btnContinue.isEnabled = !show
        if (show) {
            btnContinue.text = ""
        } else {
            btnContinue.text = getString(R.string.btn_txt_continue)
        }
    }

    private fun hideKeyboard() {
        val view: View? = this.currentFocus
        if (view != null) {
            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}
