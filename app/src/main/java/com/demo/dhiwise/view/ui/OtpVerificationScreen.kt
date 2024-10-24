package com.demo.dhiwise.view.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextWatcher
import android.text.style.StyleSpan
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.demo.dhiwise.R
import com.demo.dhiwise.viewmodel.OtpViewModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar

class OtpVerificationScreen : AppCompatActivity() {
    private lateinit var txtSentTheCode: TextView
    private lateinit var txtDidNotReceive: TextView
    private lateinit var txtResend: TextView
    private lateinit var img_icon: ImageButton
    private lateinit var btn_verify: MaterialButton
    private lateinit var edit_otp_1: EditText
    private lateinit var edit_otp_2: EditText
    private lateinit var edit_otp_3: EditText
    private lateinit var edit_otp_4: EditText
    private lateinit var tv_otp_error_message: TextView
    private lateinit var progressBar: ProgressBar

    private val viewModel: OtpViewModel by viewModels()
    private val resendOtpMessage = "Didn’t receive OTP? %d secs"
    private var countDownTimer: CountDownTimer? = null
    private var timeLeftInMillis: Long = 30000

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable) {
            if (edit_otp_1.length() != 0) edit_otp_2.requestFocus()
            if (edit_otp_2.length() != 0) edit_otp_3.requestFocus()
            if (edit_otp_3.length() != 0) edit_otp_4.requestFocus()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(R.layout.activity_otp_verification_screen)
        enableEdgeToEdge()

        btn_verify = findViewById(R.id.btn_verify)
        edit_otp_1 = findViewById(R.id.edit_otp_1)
        edit_otp_2 = findViewById(R.id.edit_otp_2)
        edit_otp_3 = findViewById(R.id.edit_otp_3)
        edit_otp_4 = findViewById(R.id.edit_otp_4)
        tv_otp_error_message = findViewById(R.id.tv_error_message)

        edit_otp_1.addTextChangedListener(textWatcher)
        edit_otp_2.addTextChangedListener(textWatcher)
        edit_otp_3.addTextChangedListener(textWatcher)
        edit_otp_4.addTextChangedListener(textWatcher)

        val phoneNumber = intent.getStringExtra("contact_number") ?: ""
        val message = "We have sent the verification code to your\n$phoneNumber mobile number."
        val spannableString = SpannableString(message)
        val start = message.indexOf(phoneNumber)
        val end = start + phoneNumber.length

        img_icon = findViewById(R.id.back_icon)
        img_icon.setOnClickListener { finish() }

        btn_verify.setOnClickListener {
            if (isOtpValid()) {
                hideKeyboard()
                showProgressBar(true)

                val otp = "${edit_otp_1.text}${edit_otp_2.text}${edit_otp_3.text}${edit_otp_4.text}"
                viewModel.verifyOtp(otp, phoneNumber)
            } else {
                showError("Please enter a 4-digit OTP")
            }
        }

        spannableString.setSpan(StyleSpan(android.graphics.Typeface.BOLD), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        txtSentTheCode = findViewById(R.id.txt_sent_the_code)
        txtSentTheCode.text = spannableString

        txtDidNotReceive = findViewById(R.id.txt_did_not_received)
        txtResend = findViewById(R.id.txt_resend)
        progressBar = findViewById(R.id.progress_bar_verify)

        startTimer()
        txtResend.setOnClickListener {
            viewModel.resendOtp(phoneNumber)
            startTimer()
        }


        viewModel.apiResponse.observe(this) { response ->
            showProgressBar(false)

            if (response != null) {
                response.meta?.message?.let { successMessage ->
                    showSnackbar(successMessage, Intent(this, LocationPermissionScreen::class.java)) {}
                } ?: showSnackbar("OTP verified successfully!", Intent(this, LocationPermissionScreen::class.java)) {}
            } else {
                viewModel.errorMessage.observe(this, { error ->
                    error?.let { showErrorSnackbar(it) }
                })
                showErrorSnackbar("Response was null.")
            }
        }

        viewModel.otpResponse.observe(this) { response ->
            if (response != null) {
                response.meta?.message?.let { message ->
                    showresendotpSnackbar(message) {}
                } ?: showresendotpSnackbar("OTP resent successfully!") {}
            } else {
                showErrorSnackbar("Failed to resend OTP.")
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main))

        { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun showresendotpSnackbar(message: String, onDismissed: () -> Unit = {}) {
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


    private fun isOtpValid(): Boolean {
        return edit_otp_1.text.isNotEmpty() &&
                edit_otp_2.text.isNotEmpty() &&
                edit_otp_3.text.isNotEmpty() &&
                edit_otp_4.text.isNotEmpty()
    }

    private fun showError(message: String) {
        tv_otp_error_message.text = message
        tv_otp_error_message.visibility = View.VISIBLE
    }

    private fun startTimer() {
        timeLeftInMillis = 30000
        txtResend.isEnabled = false
        txtResend.setTextColor(resources.getColor(R.color.button_disable, theme))

        countDownTimer?.cancel()

        countDownTimer = object : CountDownTimer(timeLeftInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                val secondsRemaining = (millisUntilFinished / 1000).toInt()
                txtDidNotReceive.text = String.format(resendOtpMessage, secondsRemaining)
            }

            override fun onFinish() {
                txtDidNotReceive.text = "Didn’t receive OTP? 0 secs"
                txtResend.isEnabled = true
                txtResend.setTextColor(resources.getColor(R.color.S19, theme))
            }
        }.start()
    }

    private fun showSnackbar(message: String, intent: Intent, onDismissed: () -> Unit) {
        val snackbar = Snackbar.make(findViewById(R.id.main), message, Snackbar.LENGTH_SHORT)
            .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
            .setDuration(1500)
            .setBackgroundTint(Color.parseColor("#5FB21A"))
        snackbar.addCallback(object : Snackbar.Callback() {
            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                super.onDismissed(transientBottomBar, event)
                startActivity(intent)
                onDismissed()
            }
        })
        snackbar.show()
    }

    private fun showErrorSnackbar(message: String, onDismissed: () -> Unit = {}) {
        val snackbar = Snackbar.make(findViewById(R.id.main), message, Snackbar.LENGTH_SHORT)
            .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
            .setBackgroundTint(Color.parseColor("#FF0000")) // Change to red for error
        snackbar.addCallback(object : Snackbar.Callback() {
            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                super.onDismissed(transientBottomBar, event)
                onDismissed()
            }
        })
        snackbar.show()
    }

    private fun hideKeyboard() {
        val view: View? = this.currentFocus
        if (view != null) {
            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun showProgressBar(show: Boolean) {
        progressBar.visibility = if (show) View.VISIBLE else View.GONE
        btn_verify.isEnabled = !show
        btn_verify.text = if (show) "" else getString(R.string.btn_txt_continue)
    }
}
